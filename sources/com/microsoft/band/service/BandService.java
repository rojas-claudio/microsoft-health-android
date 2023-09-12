package com.microsoft.band.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import com.microsoft.band.cloud.CargoServiceInfo;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.ServiceCommand;
import com.microsoft.band.internal.SessionToken;
import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.service.device.CargoDeviceManager;
import com.microsoft.band.service.device.DeviceCommand;
import com.microsoft.band.service.device.DeviceServiceProvider;
import com.microsoft.band.service.logger.CargoLogger;
import com.microsoft.band.service.logger.LoggerFactory;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public class BandService extends Service {
    public static final int MSG_SESSION_PROCESS_COMMAND = 1000;
    private static final String TAG = BandService.class.getSimpleName();
    private static BandService mSharedInstance;
    private CargoDeviceManager mDeviceManager;
    private volatile boolean mIsTerminating;
    private CargoLogger mLogger;
    private final Messenger mMessenger;
    private int mRegisteredClientNextID;
    private final SparseArray<CargoClientSession> mRegisteredClients;

    /* loaded from: classes.dex */
    private static class IncomingHandler extends Handler {
        private final WeakReference<BandService> mServiceRef;

        IncomingHandler(BandService service) {
            this.mServiceRef = new WeakReference<>(service);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            BandService service = this.mServiceRef.get();
            if (service != null) {
                service.handleIncomingMessage(msg);
            }
        }

        @Override // android.os.Handler
        public boolean sendMessageAtTime(@NonNull Message msg, long uptimeMillis) {
            if (BandServiceMessage.REGISTER_CLIENT.isEqual(msg.what) || BandServiceMessage.REGISTER_CLIENT_WITH_VERSION.isEqual(msg.what)) {
                msg.arg2 = Binder.getCallingUid();
            }
            return super.sendMessageAtTime(msg, uptimeMillis);
        }
    }

    public static BandService getInstance() {
        return mSharedInstance;
    }

    public BandService() {
        mSharedInstance = this;
        this.mMessenger = new Messenger(new IncomingHandler(this));
        this.mRegisteredClients = new SparseArray<>();
    }

    public boolean isTerminating() {
        return this.mIsTerminating;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        KDKLog.d(TAG, "*********************************************");
        KDKLog.d(TAG, "** C A R G O  S E R V I C E  C R E A T E D **");
        KDKLog.d(TAG, "*********************************************");
        this.mLogger = LoggerFactory.getLogger(this);
        this.mDeviceManager = CargoDeviceManager.create(this);
        this.mDeviceManager.startup();
    }

    @Override // android.app.Service
    public void onDestroy() {
        KDKLog.d(TAG, "onDestroy");
        this.mIsTerminating = true;
        synchronized (this.mRegisteredClients) {
            for (int i = 0; i < this.mRegisteredClients.size(); i++) {
                this.mRegisteredClients.valueAt(i).dispose();
            }
            this.mRegisteredClients.clear();
        }
        this.mDeviceManager.shutdown();
        this.mDeviceManager = null;
        super.onDestroy();
        KDKLog.d(TAG, "*************************************************");
        KDKLog.d(TAG, "** C A R G O  S E R V I C E  D E S T R O Y E D **");
        KDKLog.d(TAG, "*************************************************");
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        KDKLog.d(TAG, "*** Starting Cargo Service ***");
        Intent startServiceIntent = new Intent(this, BandService.class);
        startService(startServiceIntent);
        return this.mMessenger.getBinder();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (this.mIsTerminating) {
            KDKLog.w(TAG, "onStartCommand - shutdown has been initiated...");
            return 2;
        }
        KDKLog.i(TAG, "onStartCommand: service started.");
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DeviceServiceProvider getDeviceServiceProvider(DeviceInfo deviceInfo) {
        return this.mDeviceManager.getDeviceServiceProvider(deviceInfo);
    }

    protected void handleIncomingMessage(Message msg) {
        if (!isTerminating()) {
            if (msg.getData() != null) {
                msg.getData().setClassLoader(getClassLoader());
            }
            KDKLog.i(TAG, "Incoming Message: %s, Arg1 = %08X, Arg2 = %08X.", BandServiceMessage.lookup(msg.what), Integer.valueOf(msg.arg1), Integer.valueOf(msg.arg2));
            if (BandServiceMessage.REGISTER_CLIENT.isEqual(msg.what) || BandServiceMessage.REGISTER_CLIENT_WITH_VERSION.isEqual(msg.what)) {
                handleRegisterClient(msg);
            } else if (BandServiceMessage.UNREGISTER_CLIENT.isEqual(msg.what)) {
                handleUnregisterClient(msg);
            } else if (BandServiceMessage.PROCESS_COMMAND.isEqual(msg.what)) {
                handleProcessCommand(msg);
            } else if (BandServiceMessage.QUERY_IS_CLIENT_ALIVE_RESPONSE.isEqual(msg.what)) {
                handleQueryIsClientAliveResponse(msg);
            }
        }
    }

    private byte GetMajorVersion(int version) {
        return (byte) (version >>> 24);
    }

    private byte GetMinorVersion(int version) {
        return (byte) ((version >>> 16) & 255);
    }

    protected void handleRegisterClient(Message msg) {
        Bundle bundle = msg.getData();
        BandServiceMessage.Response response = BandServiceMessage.Response.INVALID_ARG_ERROR;
        Bundle sessionData = null;
        int clientVersion = -1;
        if (BandServiceMessage.REGISTER_CLIENT_WITH_VERSION.isEqual(msg.what) && (clientVersion = msg.arg1) != 16842752 && (GetMajorVersion(clientVersion) != GetMajorVersion(16842752) || GetMinorVersion(clientVersion) > GetMinorVersion(16842752))) {
            response = BandServiceMessage.Response.CLIENT_VERSION_UNSUPPORTED_ERROR;
        }
        if (response != BandServiceMessage.Response.CLIENT_VERSION_UNSUPPORTED_ERROR && bundle != null) {
            boolean isAdminSession = isAdminSession(msg);
            CargoServiceInfo serviceInfo = (CargoServiceInfo) bundle.getParcelable(CargoServiceInfo.EXTRA_SERVICE_INFO);
            if (serviceInfo == null) {
                serviceInfo = CargoServiceInfo.EMPTY_SERVICE_INFO;
            }
            DeviceInfo deviceInfo = (DeviceInfo) bundle.getParcelable(InternalBandConstants.EXTRA_DEVICE_INFO);
            String appName = getInstance().getPackageManager().getNameForUid(msg.arg2);
            try {
                CargoClientSession clientSession = addClientSessionContext(serviceInfo, deviceInfo, msg.replyTo, appName, isAdminSession, clientVersion);
                if (clientSession != null) {
                    sessionData = clientSession.getToken().toBundle();
                    response = BandServiceMessage.Response.SUCCESS;
                    clientSession.queryIsClientAlive();
                }
            } catch (IllegalArgumentException e) {
                KDKLog.e(TAG, "Failed to create session token.", e);
            }
        }
        sendResponseMessage(msg.replyTo, BandServiceMessage.REGISTER_CLIENT_RESPONSE, response, 16842752, sessionData);
    }

    protected void handleUnregisterClient(Message msg) {
        Bundle bundle = msg.getData();
        CargoClientSession sessionContext = getCargoClientSession(SessionToken.fromBundle(bundle));
        if (sessionContext != null) {
            sessionContext.postUnregisterClient();
        }
    }

    protected void handleProcessCommand(Message msg) {
        int commandId = msg.arg1;
        BandServiceMessage.Response response = BandServiceMessage.Response.INVALID_ARG_ERROR;
        Bundle bundle = msg.getData();
        if (bundle != null) {
            try {
                response = BandServiceMessage.Response.INVALID_SESSION_TOKEN_ERROR;
                CargoClientSession sessionContext = getCargoClientSession(SessionToken.fromBundle(bundle));
                if (sessionContext != null) {
                    sessionContext.queryIsClientAlive();
                    Object command = null;
                    if (BandDeviceConstants.Facility.toFacility(commandId) == BandDeviceConstants.Facility.CARGO_SERVICE) {
                        command = new ServiceCommand(commandId, bundle);
                    } else {
                        try {
                            DeviceCommand command2 = new DeviceCommand(commandId, bundle);
                            command = command2;
                        } catch (IllegalArgumentException e) {
                            response = BandServiceMessage.Response.DEVICE_COMMAND_ERROR;
                            KDKLog.e(TAG, "Invalid command received.", e);
                        }
                    }
                    if (command != null) {
                        KDKLog.i(TAG, "%s command received with %s commands already queued", command.getClass().getSimpleName(), Integer.valueOf(sessionContext.getCommandCounter()));
                        if (sessionContext.getCommandCounter() <= 100) {
                            if (sessionContext.sendMessage(1000, 0, 0, command)) {
                                return;
                            }
                        } else {
                            response = BandServiceMessage.Response.TOO_MANY_SIMULTANEOUS_COMMANDS_ERROR;
                        }
                    }
                }
            } catch (Exception e2) {
                KDKLog.e(TAG, e2, "Exception thrown when processing command: %s", e2.getMessage());
            }
        }
        sendResponseMessage(msg.replyTo, BandServiceMessage.PROCESS_COMMAND_RESPONSE, response, commandId, bundle);
    }

    protected void handleQueryIsClientAliveResponse(Message msg) {
        Bundle bundle = msg.getData();
        if (bundle != null) {
            SessionToken sessionToken = SessionToken.fromBundle(bundle);
            CargoClientSession clientSession = getCargoClientSession(sessionToken);
            if (clientSession != null) {
                this.mLogger.signal(System.currentTimeMillis(), TAG, "alive", "session=%", clientSession.getToken());
                clientSession.queryIsClientAlive();
            }
        }
    }

    protected CargoClientSession addClientSessionContext(CargoServiceInfo serviceInfo, DeviceInfo deviceInfo, Messenger messenger, String appName, boolean isAdminSession, int clientVersion) {
        int i = this.mRegisteredClientNextID + 1;
        this.mRegisteredClientNextID = i;
        CargoClientSession sessionContext = new CargoClientSession(this, i, serviceInfo, deviceInfo, messenger, appName, isAdminSession, clientVersion);
        synchronized (this.mRegisteredClients) {
            SessionToken sessionToken = sessionContext.getToken();
            this.mRegisteredClients.put(sessionToken.getId(), sessionContext);
        }
        this.mLogger.signal(System.currentTimeMillis(), TAG, "session", "register session=%s, ", sessionContext.getToken());
        return sessionContext;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean removeClientSessionContext(SessionToken sessionToken) {
        CargoClientSession sessionContext;
        if (sessionToken == null) {
            throw new NullPointerException("sessionToken");
        }
        int sessionTokenId = sessionToken.getId();
        synchronized (this.mRegisteredClients) {
            sessionContext = this.mRegisteredClients.get(sessionTokenId);
            if (sessionContext != null) {
                this.mRegisteredClients.remove(sessionTokenId);
                this.mLogger.signal(System.currentTimeMillis(), TAG, "session", "remove session=%", sessionToken);
            }
        }
        if (sessionContext != null) {
            sessionContext.dispose();
        }
        return sessionContext != null;
    }

    protected CargoClientSession getCargoClientSession(SessionToken sessionToken) {
        CargoClientSession sessionContext = null;
        if (sessionToken != null) {
            synchronized (this.mRegisteredClients) {
                sessionContext = this.mRegisteredClients.get(sessionToken.getId());
            }
        }
        return sessionContext;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean sendResponseMessage(Messenger messenger, BandServiceMessage message, BandServiceMessage.Response response, int arg1, Bundle data) {
        if (isTerminating() || messenger == null) {
            return false;
        }
        Message msg = Message.obtain();
        msg.what = message.getMessageId();
        msg.arg1 = arg1;
        msg.arg2 = response.getCode();
        msg.obj = null;
        msg.setData(data);
        try {
            messenger.send(msg);
            return true;
        } catch (RemoteException e) {
            KDKLog.w(TAG, "RemoteException caught while trying to send a response message to the client", e);
            return false;
        }
    }

    private boolean isAdminSession(Message msg) {
        if (msg != null) {
            try {
                int callingAppUID = msg.arg2;
                KDKLog.i(TAG, "CallingUid " + callingAppUID);
                String callingApp = getInstance().getPackageManager().getNameForUid(callingAppUID);
                KDKLog.i(TAG, "CallingApp " + callingApp);
                PackageManager pm = getPackageManager();
                int canProcess = getPackageManager().checkPermission("com.microsoft.band.service.access.BIND_BAND_SERVICE_ADMIN", pm.getNameForUid(callingAppUID));
                return canProcess == 0;
            } catch (Exception e) {
                KDKLog.e(TAG, "Exception checking app permissions: ", e);
                return false;
            }
        }
        return false;
    }
}

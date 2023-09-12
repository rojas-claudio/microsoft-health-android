package com.microsoft.band.service;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Base64;
import com.microsoft.band.CargoConstants;
import com.microsoft.band.cloud.CargoFirmwareUpdateInfo;
import com.microsoft.band.cloud.CargoServiceInfo;
import com.microsoft.band.cloud.EphemerisUpdateInfo;
import com.microsoft.band.cloud.TimeZoneSettingsUpdateInfo;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.EventHandlerThread;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.ServiceCommand;
import com.microsoft.band.internal.SessionToken;
import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.band.internal.device.subscription.DeviceContactData;
import com.microsoft.band.internal.device.subscription.SubscriptionDataModel;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.band.sensors.BandContactState;
import com.microsoft.band.service.ServiceCommandHandler;
import com.microsoft.band.service.cloud.CloudDataResource;
import com.microsoft.band.service.cloud.CloudServiceProvider;
import com.microsoft.band.service.cloud.UploadMetadata;
import com.microsoft.band.service.device.DeviceCommand;
import com.microsoft.band.service.device.DeviceServiceProvider;
import com.microsoft.band.service.device.PushServicePayload;
import com.microsoft.band.service.logger.CargoLogger;
import com.microsoft.band.service.logger.LoggerFactory;
import com.microsoft.band.service.subscription.SubscriptionDataContract;
import com.microsoft.band.service.task.BenchmarkTask;
import com.microsoft.band.service.task.EphemerisUpdateTask;
import com.microsoft.band.service.task.FirmwareUpdateTask;
import com.microsoft.band.service.task.SyncDeviceToCloudTask;
import com.microsoft.band.service.task.SyncWebTilesTask;
import com.microsoft.band.service.task.TimeZoneSettingsUpdateTask;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public class CargoClientSession implements EventHandlerThread.IEventHandlerDelegate, DeviceServiceProvider.DeviceConnectionListener {
    private static final int INVALIDATE_CLIENT_SESSION_GRACE_PERIOD = 20000;
    private static final int MSG_INVALIDATE_CLIENT_SESSION = 2001;
    private static final int MSG_QUERY_CLIENT_IS_ALIVE = 2000;
    private static final int MSG_UNREGISTER_CLIENT_SESSION = 2002;
    private static final long QUERY_CLIENT_IS_ALIVE_INTERVAL = 60000;
    private static String mDirectoryPath;
    private BenchmarkTask mBenchmarkTask;
    private final byte[] mCallingAppId;
    private final AtomicInteger mClientIsAliveToken;
    private int mClientVersion;
    private CloudServiceProvider mCloudProvider;
    private volatile DeviceServiceProvider mCurrentDeviceProvider;
    private volatile DeviceInfo mDeviceInfo;
    private EphemerisUpdateTask mDownloadEphemerisUpdateTask;
    private FirmwareUpdateTask mDownloadFirmwareUpdateTask;
    private TimeZoneSettingsUpdateTask mDownloadTimeZoneSettingsUpdateTask;
    private volatile EventHandlerThread mEventHandler;
    private boolean mIsAdminSession;
    private volatile boolean mIsUnregistering;
    private final Messenger mMessenger;
    private final CargoServiceInfo mServiceInfo;
    private final WeakReference<BandService> mServiceRef;
    private final SharedPreferences mSharedPreferences;
    private final BitSet mSubscriptions;
    private SyncDeviceToCloudTask mSyncDeviceToCloudTask;
    private SyncWebTilesTask mSyncWebTilesTask;
    private final SessionToken mToken;
    private volatile UploadMetadata mUploadMetadata;
    private static final String TAG = CargoClientSession.class.getSimpleName();
    private static final String STREAMING_TAG = TAG + ": " + InternalBandConstants.STREAM_TAG;
    private static final ExecutorService mExecutorService = Executors.newCachedThreadPool();
    private final CargoLogger mLogger = LoggerFactory.getLogger();
    private volatile int mCommandCounter = 0;
    private long mAccelerationTracker = 0;
    private long mGyroscopeTracker = 0;

    /* JADX INFO: Access modifiers changed from: protected */
    public CargoClientSession(BandService bandService, int sessionTokenId, CargoServiceInfo serviceInfo, DeviceInfo deviceInfo, Messenger messenger, String appName, boolean isAdminSession, int clientVersion) {
        this.mClientVersion = -1;
        if (bandService == null) {
            throw new IllegalArgumentException("bandService is required.");
        }
        if (serviceInfo == null) {
            throw new IllegalArgumentException("serviceInfo is required.");
        }
        if (messenger == null) {
            throw new IllegalArgumentException("messenger is required.");
        }
        this.mServiceRef = new WeakReference<>(bandService);
        this.mToken = new SessionToken(sessionTokenId);
        this.mServiceInfo = serviceInfo;
        this.mDeviceInfo = deviceInfo;
        this.mMessenger = messenger;
        this.mEventHandler = new EventHandlerThread(TAG, this);
        this.mEventHandler.startLooper();
        this.mClientIsAliveToken = new AtomicInteger(Integer.MIN_VALUE);
        this.mSharedPreferences = bandService.getSharedPreferences(BandService.class.getSimpleName(), 0);
        if (mDirectoryPath == null) {
            mDirectoryPath = bandService.getFilesDir().getAbsolutePath();
        }
        setUploadMetadata(new UploadMetadata());
        getUploadMetadata().setAppVersion(this.mToken.getVersion());
        this.mSubscriptions = SubscriptionDataContract.createBitSet(null);
        this.mCallingAppId = StringUtil.toMD5Hash(appName);
        this.mIsAdminSession = isAdminSession;
        this.mClientVersion = clientVersion;
        SharedPreferences prefs = bandService.getSharedPreferences(CargoConstants.HASH_TO_APP_NAME_MAP, 0);
        String base64AppID = Base64.encodeToString(this.mCallingAppId, 3);
        if (!prefs.contains(base64AppID)) {
            prefs.edit().putString(base64AppID, appName).commit();
        }
    }

    public int getClientVersion() {
        return this.mClientVersion;
    }

    public byte[] getCallingAppId() {
        return this.mCallingAppId;
    }

    public BandService getService() {
        return this.mServiceRef.get();
    }

    public SessionToken getToken() {
        return this.mToken;
    }

    public CargoServiceInfo getServiceInfo() {
        return this.mServiceInfo;
    }

    public DeviceInfo getDeviceInfo() {
        return this.mDeviceInfo;
    }

    public Messenger getMessenger() {
        return this.mMessenger;
    }

    public boolean isTerminating() {
        BandService service = getService();
        return this.mEventHandler == null || service == null || service.isTerminating();
    }

    public boolean isDownloadingFirmwareUpdate() {
        return this.mDownloadFirmwareUpdateTask.isRunning() && !this.mDownloadFirmwareUpdateTask.isUpgrade();
    }

    public boolean isUpgradingFirmware() {
        return this.mDownloadFirmwareUpdateTask.isRunning() && this.mDownloadFirmwareUpdateTask.isUpgrade();
    }

    public boolean isDownloadingEphemerisUpdate() {
        return this.mDownloadEphemerisUpdateTask.isRunning() && !this.mDownloadEphemerisUpdateTask.isUpgrade();
    }

    public boolean isUpgradingEphemeris() {
        return this.mDownloadEphemerisUpdateTask.isRunning() && this.mDownloadEphemerisUpdateTask.isUpgrade();
    }

    public boolean isDownloadingTimeZoneSettingsUpdate() {
        return this.mDownloadTimeZoneSettingsUpdateTask.isRunning() && !this.mDownloadTimeZoneSettingsUpdateTask.isUpgrade();
    }

    public boolean isUpgradingTimeZoneSettings() {
        return this.mDownloadTimeZoneSettingsUpdateTask.isRunning() && this.mDownloadTimeZoneSettingsUpdateTask.isUpgrade();
    }

    public void cancelSync() {
        if (this.mSyncDeviceToCloudTask != null) {
            this.mSyncDeviceToCloudTask.cancel();
        }
        if (this.mSyncWebTilesTask != null) {
            this.mSyncWebTilesTask.cancel();
        }
    }

    public DeviceServiceProvider getDeviceProvider() throws CargoServiceException {
        BandService service;
        DeviceServiceProvider deviceProvider = null;
        if (!isTerminating() && (service = getService()) != null) {
            DeviceServiceProvider currentDeviceProvider = this.mCurrentDeviceProvider;
            deviceProvider = currentDeviceProvider;
            if (deviceProvider == null) {
                deviceProvider = service.getDeviceServiceProvider(getDeviceInfo());
            }
            if (currentDeviceProvider != deviceProvider) {
                deviceProvider.registerListener(this);
                this.mCurrentDeviceProvider = deviceProvider;
            }
        }
        if (deviceProvider == null) {
            throw new CargoServiceException(String.format("Device not bonded: %s", getDeviceInfo()), BandServiceMessage.Response.DEVICE_NOT_BONDED_ERROR);
        }
        return deviceProvider;
    }

    public CloudServiceProvider getCloudProvider() {
        if (this.mCloudProvider == null) {
            this.mCloudProvider = new CloudServiceProvider(this.mServiceInfo);
        }
        return this.mCloudProvider;
    }

    public SharedPreferences getSharedPreferences() {
        return this.mSharedPreferences;
    }

    public BandServiceMessage.Response syncDeviceToCloud(boolean fullSync) {
        BandServiceMessage.Response response = BandServiceMessage.Response.PENDING;
        if (this.mSyncDeviceToCloudTask == null) {
            this.mSyncDeviceToCloudTask = new SyncDeviceToCloudTask();
        }
        if (isTerminating()) {
            return BandServiceMessage.Response.SERVICE_TERMINATED_ERROR;
        }
        if (!this.mSyncDeviceToCloudTask.isRunning()) {
            this.mSyncDeviceToCloudTask.execute(this, fullSync);
            return response;
        }
        return response;
    }

    public BandServiceMessage.Response syncWebTiles(boolean foregroundSync) {
        BandServiceMessage.Response response = BandServiceMessage.Response.PENDING;
        if (this.mSyncWebTilesTask == null) {
            this.mSyncWebTilesTask = new SyncWebTilesTask();
        }
        if (isTerminating()) {
            return BandServiceMessage.Response.SERVICE_TERMINATED_ERROR;
        }
        if (!this.mSyncWebTilesTask.isRunning()) {
            this.mSyncWebTilesTask.execute(this, foregroundSync);
            return response;
        }
        return response;
    }

    public BandServiceMessage.Response benchmarkSync(int maxTransferChunks) {
        BandServiceMessage.Response response = BandServiceMessage.Response.PENDING;
        if (this.mBenchmarkTask == null) {
            this.mBenchmarkTask = new BenchmarkTask();
        }
        if (isTerminating()) {
            return BandServiceMessage.Response.SERVICE_TERMINATED_ERROR;
        }
        if (!this.mBenchmarkTask.isRunning()) {
            KDKLog.i(TAG, "benchmarker being executed");
            this.mBenchmarkTask.execute(this, maxTransferChunks);
            return response;
        }
        return response;
    }

    public BandServiceMessage.Response downloadFirmwareUpdate(CargoFirmwareUpdateInfo firmwareUpdateInfo) {
        BandServiceMessage.Response response = BandServiceMessage.Response.PENDING;
        if (this.mDownloadFirmwareUpdateTask == null) {
            this.mDownloadFirmwareUpdateTask = new FirmwareUpdateTask();
        }
        if (isTerminating()) {
            return BandServiceMessage.Response.SERVICE_TERMINATED_ERROR;
        }
        if (!isDownloadingFirmwareUpdate()) {
            if (isUpgradingFirmware()) {
                this.mDownloadFirmwareUpdateTask.waitForCompletion();
            }
            this.mDownloadFirmwareUpdateTask.execute(this, firmwareUpdateInfo, false);
            return response;
        }
        return response;
    }

    public BandServiceMessage.Response upgradeFirmwareUpdate(CargoFirmwareUpdateInfo firmwareUpdateInfo) {
        BandServiceMessage.Response response = BandServiceMessage.Response.PENDING;
        if (this.mDownloadFirmwareUpdateTask == null) {
            this.mDownloadFirmwareUpdateTask = new FirmwareUpdateTask();
        }
        if (isTerminating()) {
            return BandServiceMessage.Response.SERVICE_TERMINATED_ERROR;
        }
        if (!isUpgradingFirmware()) {
            if (isDownloadingFirmwareUpdate()) {
                this.mDownloadFirmwareUpdateTask.waitForCompletion();
            }
            this.mDownloadFirmwareUpdateTask.execute(this, firmwareUpdateInfo, true);
            return response;
        }
        return response;
    }

    public BandServiceMessage.Response downloadEphemerisUpdate(EphemerisUpdateInfo ephemerisUpdateInfo) {
        BandServiceMessage.Response response = BandServiceMessage.Response.PENDING;
        if (this.mDownloadEphemerisUpdateTask == null) {
            this.mDownloadEphemerisUpdateTask = new EphemerisUpdateTask();
        }
        if (isTerminating()) {
            return BandServiceMessage.Response.SERVICE_TERMINATED_ERROR;
        }
        if (!isDownloadingEphemerisUpdate()) {
            if (isUpgradingEphemeris()) {
                this.mDownloadEphemerisUpdateTask.waitForCompletion();
            }
            this.mDownloadEphemerisUpdateTask.execute(this, ephemerisUpdateInfo, false);
            return response;
        }
        return response;
    }

    public BandServiceMessage.Response upgradeEphemerisUpdate(EphemerisUpdateInfo ephemerisUpdateInfo) {
        BandServiceMessage.Response response = BandServiceMessage.Response.PENDING;
        if (this.mDownloadEphemerisUpdateTask == null) {
            this.mDownloadEphemerisUpdateTask = new EphemerisUpdateTask();
        }
        if (isTerminating()) {
            return BandServiceMessage.Response.SERVICE_TERMINATED_ERROR;
        }
        if (!isUpgradingEphemeris()) {
            if (isDownloadingEphemerisUpdate()) {
                this.mDownloadEphemerisUpdateTask.waitForCompletion();
            }
            this.mDownloadEphemerisUpdateTask.execute(this, ephemerisUpdateInfo, true);
            return response;
        }
        return response;
    }

    public BandServiceMessage.Response downloadTimeZoneSettingsUpdate(TimeZoneSettingsUpdateInfo timeZoneSettingsUpdateInfo) {
        BandServiceMessage.Response response = BandServiceMessage.Response.PENDING;
        if (this.mDownloadTimeZoneSettingsUpdateTask == null) {
            this.mDownloadTimeZoneSettingsUpdateTask = new TimeZoneSettingsUpdateTask();
        }
        if (isTerminating()) {
            return BandServiceMessage.Response.SERVICE_TERMINATED_ERROR;
        }
        if (!isDownloadingTimeZoneSettingsUpdate()) {
            if (isUpgradingTimeZoneSettings()) {
                this.mDownloadTimeZoneSettingsUpdateTask.waitForCompletion();
            }
            this.mDownloadTimeZoneSettingsUpdateTask.execute(this, timeZoneSettingsUpdateInfo, false);
            return response;
        }
        return response;
    }

    public BandServiceMessage.Response upgradeTimeZoneSettingsUpdate(TimeZoneSettingsUpdateInfo timeZoneSettingsUpdateInfo) {
        BandServiceMessage.Response response = BandServiceMessage.Response.PENDING;
        if (this.mDownloadTimeZoneSettingsUpdateTask == null) {
            this.mDownloadTimeZoneSettingsUpdateTask = new TimeZoneSettingsUpdateTask();
        }
        if (isTerminating()) {
            return BandServiceMessage.Response.SERVICE_TERMINATED_ERROR;
        }
        if (!isUpgradingTimeZoneSettings()) {
            if (isDownloadingTimeZoneSettingsUpdate()) {
                this.mDownloadTimeZoneSettingsUpdateTask.waitForCompletion();
            }
            this.mDownloadTimeZoneSettingsUpdateTask.execute(this, timeZoneSettingsUpdateInfo, true);
            return response;
        }
        return response;
    }

    public BandServiceMessage.Response uploadLogToToCloud(byte[] data, CloudDataResource.LogFileTypes type) {
        if (isTerminating()) {
            BandServiceMessage.Response response = BandServiceMessage.Response.SERVICE_TERMINATED_ERROR;
            return response;
        }
        CloudDataResource cdr = new CloudDataResource();
        try {
            cdr.setUploadId(new Date());
            cdr.setLogFileType(type);
            cdr.setMetaData(getUploadMetadata());
            BandServiceMessage.Response response2 = getCloudProvider().uploadBytesToCloud(data, cdr);
            return response2;
        } catch (CargoServiceException e) {
            KDKLog.e(TAG, e, "Upload log to cloud failed with %s.", e.getMessage());
            BandServiceMessage.Response response3 = e.getResponse();
            return response3;
        }
    }

    public Future<?> runAsyncTask(Runnable task) {
        if (isTerminating() || task == null) {
            return null;
        }
        return mExecutorService.submit(task);
    }

    public <V> Future<V> runAsyncTask(Callable<V> task) {
        if (isTerminating() || task == null) {
            return null;
        }
        return mExecutorService.submit(task);
    }

    public void sendServiceMessage(BandServiceMessage message, BandServiceMessage.Response response, int arg1, Bundle data) {
        BandService service;
        if (!isTerminating() && (service = getService()) != null && !service.isTerminating()) {
            if (data == null) {
                data = getToken().toBundle();
            } else {
                getToken().putInBundle(data);
            }
            if (!this.mIsUnregistering && !service.sendResponseMessage(getMessenger(), message, response, arg1, data)) {
                service.removeClientSessionContext(getToken());
            }
        }
    }

    public void sendSyncProgressMessage(int value) {
        sendServiceMessage(BandServiceMessage.SYNC_PROGRESS, BandServiceMessage.Response.UNSPECIFIED, value, null);
    }

    public void dispose() {
        EventHandlerThread eventHandler = this.mEventHandler;
        this.mEventHandler = null;
        if (eventHandler != null) {
            eventHandler.stopLooper();
        }
        cancelSync();
        DeviceServiceProvider currentDeviceProvicer = this.mCurrentDeviceProvider;
        this.mCurrentDeviceProvider = null;
        if (currentDeviceProvicer != null) {
            currentDeviceProvicer.unregisterListener(this);
        }
        this.mServiceRef.clear();
        KDKLog.i(TAG, "Session disposed %s.", getToken());
    }

    @Override // com.microsoft.band.internal.EventHandlerThread.IEventHandlerDelegate
    public void handleMessage(Message msg) {
        if (!isTerminating()) {
            BandService service = getService();
            switch (msg.what) {
                case 1000:
                    if (msg.obj instanceof DeviceCommand) {
                        this.mLogger.signal(System.currentTimeMillis(), TAG, "handle command", "command=%s", ((DeviceCommand) msg.obj).getCommandType());
                        sendDeviceCommand((DeviceCommand) msg.obj, true);
                    } else if (msg.obj instanceof ServiceCommand) {
                        this.mLogger.signal(System.currentTimeMillis(), TAG, "handle command", "command=%s", ((ServiceCommand) msg.obj).getCommandType());
                        handleServiceCommand((ServiceCommand) msg.obj);
                    }
                    this.mCommandCounter--;
                    return;
                case 2000:
                    if (this.mClientIsAliveToken.get() == msg.arg1) {
                        this.mLogger.traceDiagnostics(System.currentTimeMillis(), TAG, "MSG_QUERY_CLIENT_IS_ALIVE", "session token=%s", getToken());
                        sendServiceMessage(BandServiceMessage.QUERY_IS_CLIENT_ALIVE, BandServiceMessage.Response.UNSPECIFIED, 0, null);
                        sendMessage(2001, msg.arg1, 0, null, 20000L);
                        return;
                    }
                    return;
                case 2001:
                    if (service != null && this.mClientIsAliveToken.get() == msg.arg1) {
                        this.mLogger.traceDiagnostics(System.currentTimeMillis(), TAG, "MSG_INVALIDATE_CLIENT_SESSION", "session token=%s", getToken());
                        service.removeClientSessionContext(getToken());
                        return;
                    }
                    return;
                case 2002:
                    if (service != null) {
                        this.mLogger.traceDiagnostics(System.currentTimeMillis(), TAG, "MSG_UNREGISTER_CLIENT_SESSION", "session token=%s", getToken());
                        service.removeClientSessionContext(getToken());
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void queryIsClientAlive() {
        sendMessage(2000, this.mClientIsAliveToken.addAndGet(1), 0, null, 60000L);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void postUnregisterClient() {
        this.mIsUnregistering = true;
        sendMessage(2002, this.mClientIsAliveToken.addAndGet(1), 0, null, 0L);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean sendMessage(int messageId, int arg1, int arg2, Object obj) {
        this.mCommandCounter++;
        return sendMessage(messageId, arg1, arg2, obj, 0L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCommandCounter() {
        return this.mCommandCounter;
    }

    protected boolean sendMessage(int messageId, int arg1, int arg2, Object obj, long delayMillis) {
        Handler handler;
        EventHandlerThread eventHandler = this.mEventHandler;
        if (eventHandler == null || (handler = eventHandler.getHandler()) == null) {
            return false;
        }
        if (2000 == messageId || 2002 == messageId) {
            handler.removeMessages(2000);
            handler.removeMessages(2001);
        }
        Message msg = Message.obtain(handler, messageId);
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = obj;
        if (delayMillis > 0) {
            handler.sendMessageDelayed(msg, delayMillis);
        } else {
            msg.sendToTarget();
        }
        return true;
    }

    private void sendDeviceCommand(DeviceCommand command, boolean sendResponse) {
        if (command == null) {
            throw new NullPointerException("command");
        }
        synchronized (command) {
            BandServiceMessage.Response response = BandServiceMessage.Response.DEVICE_NOT_BONDED_ERROR;
            if (isAdminSession()) {
                try {
                    response = getDeviceProvider().processCommand(command);
                } catch (CargoServiceException e) {
                    response = e.getResponse();
                }
            }
            if (sendResponse) {
                if (isAdminSession()) {
                    Bundle data = getToken().toBundle();
                    data.putInt(InternalBandConstants.EXTRA_COMMAND_RESULT_CODE, command.getResultCode());
                    data.putLong(InternalBandConstants.EXTRA_COMMAND_INDEX, command.getCommandIndex());
                    if (command.isResultSuccessful() && command.isReceivingPayload()) {
                        data.putByteArray(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, command.getPayload());
                    }
                    sendServiceMessage(BandServiceMessage.PROCESS_COMMAND_RESPONSE, response, command.getCommandId(), data);
                } else {
                    sendServiceMessage(BandServiceMessage.PROCESS_COMMAND_RESPONSE, BandServiceMessage.Response.PERMISSION_DENIED, command.getCommandId(), null);
                }
            }
            command.notify();
        }
    }

    private void handleServiceCommand(ServiceCommand command) {
        if (command == null) {
            throw new NullPointerException("command");
        }
        Bundle data = getToken().toBundle();
        data.putLong(InternalBandConstants.EXTRA_COMMAND_INDEX, command.getCommandIndex());
        BandServiceMessage.Response response = BandServiceMessage.Response.SERVICE_COMMAND_ERROR;
        ServiceCommandHandler.ICommandHandler commandHandler = ServiceCommandHandler.getHandler(command);
        if (commandHandler == null) {
            KDKLog.e(TAG, "No handler for Service Command %s.", command.getCommandType());
        } else {
            try {
                response = commandHandler.execute(this, command, data);
            } catch (CargoServiceException e) {
                KDKLog.e(TAG, e, "Failed to execute service command %s: %s.", command.getCommandType(), e.getMessage());
                response = e.getResponse();
            } catch (IllegalArgumentException e2) {
                KDKLog.e(TAG, e2, "Service command %s argument error: %s.", command.getCommandType(), e2.getMessage());
                response = BandServiceMessage.Response.INVALID_ARG_ERROR;
            } catch (IllegalStateException e3) {
                KDKLog.e(TAG, e3, "Service command %s state error: %s.", command.getCommandType(), e3.getMessage());
                response = BandServiceMessage.Response.INVALID_OPERATION_ERROR;
            } catch (NullPointerException e4) {
                KDKLog.e(TAG, e4, "Service command %s argument error: %s.", command.getCommandType(), e4.getMessage());
                response = BandServiceMessage.Response.INVALID_ARG_ERROR;
            }
        }
        sendServiceMessage(BandServiceMessage.PROCESS_COMMAND_RESPONSE, response, command.getCommandId(), data);
    }

    @Override // com.microsoft.band.service.device.DeviceServiceProvider.DeviceConnectionListener
    public void onDeviceConnected(DeviceServiceProvider deviceProvider) {
        this.mDeviceInfo = deviceProvider.getDeviceInfo();
        getUploadMetadata().setDeviceMetadata(this.mDeviceInfo);
        Bundle bundle = getToken().toBundle();
        bundle.putParcelable(InternalBandConstants.EXTRA_DEVICE_INFO, getModifiedDeviceInfo(deviceProvider.getDeviceInfo()));
        sendServiceMessage(BandServiceMessage.DEVICE_STATUS_NOTIFICATION, BandServiceMessage.Response.DEVICE_CONNECTED, 0, bundle);
    }

    @Override // com.microsoft.band.service.device.DeviceServiceProvider.DeviceConnectionListener
    public void onDeviceDisconnected(DeviceServiceProvider deviceProvider) {
        Bundle bundle = getToken().toBundle();
        bundle.putParcelable(InternalBandConstants.EXTRA_DEVICE_INFO, getModifiedDeviceInfo(deviceProvider.getDeviceInfo()));
        sendServiceMessage(BandServiceMessage.DEVICE_STATUS_NOTIFICATION, BandServiceMessage.Response.DEVICE_DISCONNECTED, 0, bundle);
    }

    @Override // com.microsoft.band.service.device.DeviceServiceProvider.DeviceConnectionListener
    public void onDeviceTimeout(DeviceServiceProvider deviceProvider) {
        Bundle bundle = getToken().toBundle();
        bundle.putParcelable(InternalBandConstants.EXTRA_DEVICE_INFO, getModifiedDeviceInfo(deviceProvider.getDeviceInfo()));
        sendServiceMessage(BandServiceMessage.DEVICE_STATUS_NOTIFICATION, BandServiceMessage.Response.DEVICE_TIMEOUT_ERROR, 0, bundle);
    }

    private DeviceInfo getModifiedDeviceInfo(DeviceInfo dIn) {
        DeviceInfo dOut = new DeviceInfo(dIn.getName(), dIn.getMacAddress());
        dOut.setDeviceUUID(dIn.getDeviceUUID());
        dOut.setFWVersion(dIn.getFWVersion());
        dOut.setHardwareVersion(dIn.getHardwareVersion());
        dOut.setLogVersion(dIn.getLogVersion());
        dOut.setSerialNumber(dIn.getSerialNumber());
        dOut.setVersion(getClientVersion());
        return dOut;
    }

    @Override // com.microsoft.band.service.device.DeviceServiceProvider.DeviceConnectionListener
    public void onDeviceConnectionDisposed(DeviceServiceProvider deviceProvider) {
        if (deviceProvider == this.mCurrentDeviceProvider) {
            this.mCurrentDeviceProvider = null;
        }
    }

    public BandServiceMessage.Response subscribeToSensor(int sensor) throws CargoServiceException {
        KDKLog.i(STREAMING_TAG, "CargoClientSession %d subscribing to %s data", Integer.valueOf(getToken().getId()), SubscriptionDataContract.SensorType.lookup(sensor));
        synchronized (this.mSubscriptions) {
            this.mSubscriptions.set(sensor, true);
            extraSubscriptionLogic(sensor);
        }
        if (sensor == SubscriptionDataContract.SensorType.DeviceContact.getId() && getDeviceProvider().getCurrentBandContactState() != BandContactState.UNKNOWN) {
            sendDeviceContactSubscriptionData(getDeviceProvider().getCurrentBandContactState());
        }
        return getDeviceProvider().updateSubscriptions();
    }

    private void sendDeviceContactSubscriptionData(BandContactState currentBandContactStatus) {
        byte[] deviceContactBytes = {currentBandContactStatus.getId(), BandContactState.UNKNOWN.getId(), BandContactState.UNKNOWN.getId()};
        DeviceContactData contactData = new DeviceContactData(ByteBuffer.wrap(deviceContactBytes));
        contactData.setTimestamp(System.currentTimeMillis());
        sendSubscriptionData(SubscriptionDataContract.SensorType.DeviceContact.getId(), contactData);
    }

    public BandServiceMessage.Response unsubscribeToSensor(int sensor) throws CargoServiceException {
        KDKLog.i(STREAMING_TAG, "CargoClientSession %d unsubscribing to %s data", Integer.valueOf(getToken().getId()), SubscriptionDataContract.SensorType.lookup(sensor));
        synchronized (this.mSubscriptions) {
            this.mSubscriptions.set(sensor, false);
        }
        return getDeviceProvider().updateSubscriptions();
    }

    public BandServiceMessage.Response unsubscribeToAllSensors() throws CargoServiceException {
        KDKLog.i(STREAMING_TAG, "CargoClientSession %d unsubscribing to all sensors", Integer.valueOf(getToken().getId()));
        synchronized (this.mSubscriptions) {
            this.mSubscriptions.clear();
        }
        return getDeviceProvider().updateSubscriptions();
    }

    private void extraSubscriptionLogic(int sensor) {
        switch (sensor) {
            case 0:
                this.mSubscriptions.set(1, false);
                this.mSubscriptions.set(48, false);
                return;
            case 1:
                this.mSubscriptions.set(0, false);
                this.mSubscriptions.set(48, false);
                return;
            case 4:
                this.mSubscriptions.set(5, false);
                this.mSubscriptions.set(49, false);
                return;
            case 5:
                this.mSubscriptions.set(4, false);
                this.mSubscriptions.set(49, false);
                return;
            case 48:
                this.mSubscriptions.set(0, false);
                this.mSubscriptions.set(1, false);
                return;
            case 49:
                this.mSubscriptions.set(4, false);
                this.mSubscriptions.set(5, false);
                return;
            default:
                return;
        }
    }

    @Override // com.microsoft.band.service.subscription.PushConnectionListener
    public void updateSubscriptionsSet(BitSet subscriptionsList) {
        synchronized (this.mSubscriptions) {
            subscriptionsList.or(this.mSubscriptions);
        }
    }

    @Override // com.microsoft.band.service.subscription.PushConnectionListener
    public void onPushPacketReceived(PushServicePayload payload) {
        boolean sendPayload;
        synchronized (this.mSubscriptions) {
            sendPayload = this.mSubscriptions.get(payload.getSensorType().getId());
        }
        if (sendPayload) {
            sendSubscriptionData(payload, payload.getSensorType());
        } else {
            handleSendingForSpecialCases(payload);
        }
    }

    @Override // com.microsoft.band.service.subscription.PushConnectionListener
    public int getSubscriptionCount() {
        int cardinality;
        synchronized (this.mSubscriptions) {
            cardinality = this.mSubscriptions.cardinality();
        }
        return cardinality;
    }

    private void handleSendingForSpecialCases(PushServicePayload payload) {
        if (payload.getSensorType().getId() == 48) {
            if (this.mSubscriptions.get(1)) {
                this.mAccelerationTracker++;
                if (this.mAccelerationTracker % 2 == 0) {
                    sendSubscriptionData(payload, SubscriptionDataContract.SensorType.Accelerometer32MS);
                }
            } else if (this.mSubscriptions.get(0)) {
                this.mAccelerationTracker++;
                if (this.mAccelerationTracker % 8 == 0) {
                    sendSubscriptionData(payload, SubscriptionDataContract.SensorType.Accelerometer128MS);
                }
            }
        } else if (payload.getSensorType().getId() == 1) {
            if (this.mSubscriptions.get(0)) {
                this.mAccelerationTracker++;
                if (this.mAccelerationTracker % 4 == 0) {
                    sendSubscriptionData(payload, SubscriptionDataContract.SensorType.Accelerometer128MS);
                }
            }
        } else if (payload.getSensorType().getId() == 49) {
            if (this.mSubscriptions.get(5)) {
                this.mGyroscopeTracker++;
                if (this.mGyroscopeTracker % 2 == 0) {
                    sendSubscriptionData(payload, SubscriptionDataContract.SensorType.AccelerometerGyroscope32MS);
                }
            } else if (this.mSubscriptions.get(4)) {
                this.mGyroscopeTracker++;
                if (this.mGyroscopeTracker % 8 == 0) {
                    sendSubscriptionData(payload, SubscriptionDataContract.SensorType.AccelerometerGyroscope128MS);
                }
            }
        } else if (payload.getSensorType().getId() == 5 && this.mSubscriptions.get(4)) {
            this.mGyroscopeTracker++;
            if (this.mGyroscopeTracker % 4 == 0) {
                sendSubscriptionData(payload, SubscriptionDataContract.SensorType.AccelerometerGyroscope128MS);
            }
        }
    }

    private void sendSubscriptionData(PushServicePayload payload, SubscriptionDataContract.SensorType sensorType) {
        List<SubscriptionDataModel> dataModels = SubscriptionDataContract.createDataModels(payload);
        for (SubscriptionDataModel dataModel : dataModels) {
            sendSubscriptionData(sensorType.getId(), dataModel);
        }
    }

    private void sendSubscriptionData(int sensorType, SubscriptionDataModel dataModel) {
        Bundle bundle = getToken().toBundle();
        bundle.putParcelable(InternalBandConstants.EXTRA_SUBSCRIPTION_DATA, dataModel);
        KDKLog.i(STREAMING_TAG, "CargoClientSession %d sending %s data", Integer.valueOf(getToken().getId()), Integer.valueOf(sensorType));
        sendServiceMessage(BandServiceMessage.PROCESS_PUSH_DATA, BandServiceMessage.Response.UNSPECIFIED, sensorType, bundle);
    }

    public UploadMetadata getUploadMetadata() {
        return this.mUploadMetadata;
    }

    public void setUploadMetadata(UploadMetadata mUploadMetadata) {
        this.mUploadMetadata = mUploadMetadata;
    }

    public static String getDirectoryPath() {
        return mDirectoryPath;
    }

    public boolean isAdminSession() {
        return this.mIsAdminSession;
    }

    public boolean clientHasPatchLevel(int patchLevel) {
        return (this.mClientVersion & (-65536)) == ((-65536) & patchLevel) && (this.mClientVersion & 65535) >= (patchLevel & 65535);
    }
}

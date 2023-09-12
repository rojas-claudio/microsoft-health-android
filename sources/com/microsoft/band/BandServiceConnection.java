package com.microsoft.band;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.CommandBase;
import com.microsoft.band.internal.EventHandlerThread;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.ServiceCommand;
import com.microsoft.band.internal.SessionToken;
import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.kapp.utils.StrappConstants;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class BandServiceConnection {
    private static final long PROCESS_COMMAND_RESPONSE_TIMEOUT_IN_MILLIS = 120000;
    private static final String TAG = BandServiceConnection.class.getSimpleName();
    private static final long WAIT_FOR_SERVICE_TO_BIND_TIMEOUT = 10000;
    private volatile BandConnectionCallback mBandConnectionCallback;
    private volatile ConnectionState mConnectionState;
    private Context mContext;
    private volatile DeviceInfo mDeviceInfo;
    private DeviceStatusListener mDeviceStatusListener;
    private DownloadNotificationListener mDownloadNotificationListener;
    EventHandlerThread mEventHandler;
    private final EventHandlerDelegate mEventHandlerDelegate;
    private final Bundle mExtraData;
    private FirmwareUpgradeProgressNotificationListener mFirmwareUpgradeProgressNotificationListener;
    Messenger mMessenger;
    private PushHandler mPushHandler;
    private final ConcurrentMap<Long, WaitingCommandTask<?, ? extends CommandBase>> mSentCommands;
    private final AtomicBoolean mServiceBoundFlag;
    private Messenger mServiceMessenger;
    private volatile SessionToken mSessionToken;
    private SyncNotificationListener mSyncNotificationListener;
    private SyncProgressNotificationListener mSyncProgressNotificationListener;
    private UpgradeNotificationListener mUpgradeNotificationListener;
    private final AtomicLong mCommandCounter = new AtomicLong(0);
    private int mServiceSdkVersion = -1;
    final ServiceConnection mServiceConnection = new ServiceConnection() { // from class: com.microsoft.band.BandServiceConnection.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder serviceBinder) {
            KDKLog.i(BandServiceConnection.TAG, "onServiceConnected: %s", className);
            BandServiceConnection.this.onServiceBound(serviceBinder);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName className) {
            KDKLog.i(BandServiceConnection.TAG, "onServiceDisconnected: %s", className);
            BandServiceConnection.this.onServiceUnbound();
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface DeviceStatusListener {
        void onDeviceStatusNotification(Message message);
    }

    /* loaded from: classes.dex */
    interface DownloadNotificationListener {
        void onDownloadNotification(Message message);
    }

    /* loaded from: classes.dex */
    interface FirmwareUpgradeProgressNotificationListener {
        void onFirmwareUpgradeProgressNotification(Message message);
    }

    /* loaded from: classes.dex */
    interface SyncNotificationListener {
        void onSyncNotification(Message message);
    }

    /* loaded from: classes.dex */
    interface SyncProgressNotificationListener {
        void onSyncProgressNotification(Message message);
    }

    /* loaded from: classes.dex */
    interface UpgradeNotificationListener {
        void onUpgradeNotification(Message message);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BandServiceConnection create(Context context, DeviceInfo deviceInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(InternalBandConstants.EXTRA_DEVICE_INFO, deviceInfo);
        return new BandServiceConnection(context, bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BandServiceConnection create(Context context, BandInfo deviceInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(InternalBandConstants.EXTRA_DEVICE_INFO, new DeviceInfo(deviceInfo.getName(), deviceInfo.getMacAddress()));
        return new BandServiceConnection(context, bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BandServiceConnection(Context context, Bundle extraData) throws IllegalArgumentException {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        this.mServiceBoundFlag = new AtomicBoolean(false);
        this.mContext = context;
        this.mExtraData = extraData;
        this.mEventHandlerDelegate = new EventHandlerDelegate();
        this.mConnectionState = ConnectionState.UNBOUND;
        this.mSentCommands = new ConcurrentHashMap();
        KDKLog.i(TAG, "New Instance created.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Context getContext() {
        return this.mContext;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DeviceInfo getDeviceInfo() {
        return this.mDeviceInfo;
    }

    int getServiceVersion() {
        if (this.mSessionToken == null) {
            return 0;
        }
        return this.mSessionToken.getVersion();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isServiceBound() {
        return this.mConnectionState != null && this.mConnectionState.ordinal() >= ConnectionState.BOUND.ordinal();
    }

    boolean isServiceBinding() {
        return ConnectionState.BINDING == this.mConnectionState;
    }

    boolean isServiceUnbinding() {
        return ConnectionState.UNBINDING == this.mConnectionState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDeviceConnected() {
        return ConnectionState.CONNECTED == this.mConnectionState;
    }

    boolean isInitialized() {
        return (this.mConnectionState == null || ConnectionState.DISPOSED == this.mConnectionState) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConnectionState getConnectionState() {
        ConnectionState connectionState = this.mConnectionState;
        return connectionState;
    }

    private void fireConnectionStateListener(ConnectionState state) {
        if (this.mBandConnectionCallback != null) {
            if (state == null) {
                state = ConnectionState.DISPOSED;
            }
            this.mBandConnectionCallback.onStateChanged(state);
        }
    }

    public void registerConnectionCallback(BandConnectionCallback callback) {
        synchronized (this.mServiceBoundFlag) {
            this.mBandConnectionCallback = callback;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unregisterConnectionCallback() {
        synchronized (this.mServiceBoundFlag) {
            this.mBandConnectionCallback = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void bind() throws CargoException {
        synchronized (this.mServiceConnection) {
            exceptionIfNotInitialized();
            if (isServiceUnbinding()) {
                throw new IllegalStateException("Cannot bind to BandService while unbinding.");
            }
            if (!isServiceBound()) {
                setConnectionState(ConnectionState.BINDING);
                KDKLog.d(TAG, "Start binding to service");
                Intent i = new Intent(InternalBandConstants.ACTION_BIND_BAND_SERVICE);
                i.setClassName(StrappConstants.NOTIFICATION_SERVICE_MICROSOFT_HEALTH, "com.microsoft.band.service.BandService");
                getContext().bindService(i, this.mServiceConnection, 1);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void unbind() {
        synchronized (this.mServiceConnection) {
            boolean isBound = isServiceBound();
            if (isBound || isServiceBinding()) {
                setConnectionState(ConnectionState.UNBINDING);
                if (isBound) {
                    try {
                        sendServiceMessage(BandServiceMessage.UNREGISTER_CLIENT, 0, null);
                    } catch (CargoException e) {
                        KDKLog.w(TAG, "Failed to send %s to service before unbinding.", BandServiceMessage.UNREGISTER_CLIENT);
                    }
                }
                getContext().unbindService(this.mServiceConnection);
                onServiceUnbound();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean waitForServiceToBind() {
        if (isMainThread()) {
            throw new RuntimeException("This method cannot be called from main thread");
        }
        if (!isServiceBound()) {
            synchronized (this.mServiceBoundFlag) {
                long deadline = SystemClock.uptimeMillis() + WAIT_FOR_SERVICE_TO_BIND_TIMEOUT;
                boolean isTimeoutHappened = false;
                while (!isServiceBound() && !isTimeoutHappened) {
                    try {
                        KDKLog.d(TAG, "Waiting for service to bind timeout = %s", Long.valueOf((long) WAIT_FOR_SERVICE_TO_BIND_TIMEOUT));
                        this.mServiceBoundFlag.wait(WAIT_FOR_SERVICE_TO_BIND_TIMEOUT);
                        isTimeoutHappened = deadline - SystemClock.uptimeMillis() <= 0;
                    } catch (InterruptedException e) {
                        KDKLog.e(TAG, "Interrupted while waiting for the service to bind.", e);
                        this.mServiceBoundFlag.notifyAll();
                    }
                }
                this.mServiceBoundFlag.notifyAll();
            }
        }
        return isServiceBound();
    }

    private boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    void sendServiceMessage(BandServiceMessage serviceMessage, int arg1, Bundle data) throws CargoException {
        if (serviceMessage == null || BandServiceMessage.NULL_MESSAGE == serviceMessage) {
            throw new IllegalArgumentException("BandServiceMessage cannot be null");
        }
        Messenger serviceMessenger = this.mServiceMessenger;
        if (serviceMessenger == null) {
            if (ConnectionState.INVALID_SDK_VERSION == this.mConnectionState) {
                throw new CargoException("SDK Version is not supported.", BandServiceMessage.Response.CLIENT_VERSION_UNSUPPORTED_ERROR);
            }
            throw new CargoException("Service is not available.", BandServiceMessage.Response.SERVICE_TERMINATED_ERROR);
        }
        try {
            Message message = Message.obtain();
            message.what = serviceMessage.getMessageId();
            message.arg1 = arg1;
            message.arg2 = 0;
            message.obj = null;
            message.replyTo = this.mMessenger;
            SessionToken sessionToken = this.mSessionToken;
            if (sessionToken != null) {
                if (data == null) {
                    data = sessionToken.toBundle();
                } else {
                    sessionToken.putInBundle(data);
                }
            }
            message.setData(data);
            serviceMessenger.send(message);
        } catch (RemoteException e) {
            KDKLog.e(TAG, e.getMessage(), e);
            throw new CargoException("Service is not available.", e, BandServiceMessage.Response.SERVICE_TERMINATED_ERROR);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Deprecated
    public boolean sendCommandAsync(CommandBase cmd) throws CargoException {
        return sendCommand(cmd, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Deprecated
    public boolean sendCommand(CommandBase cmd) throws CargoException {
        return sendCommand(cmd, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <ResultT, C extends CommandBase> BandPendingResult<ResultT> send(WaitingCommandTask<ResultT, C> task) throws BandIOException {
        Bundle bundle;
        if (this.mSentCommands.size() > 100) {
            KDKLog.e(TAG, "Too many commands (%s) sent at once! Only %s commands allowed to be sent at a time! Command %s not sent!", Integer.valueOf(this.mSentCommands.size()), 100, task.getCommand().getCommandType());
            throw new BandIOException(String.format("Only %s simultaneous commands can be sent at once!", 100), BandErrorType.TOO_MANY_CONCURRENT_COMMANDS_ERROR);
        }
        C cmd = task.getCommand();
        if (cmd instanceof ServiceCommand) {
            bundle = ((ServiceCommand) cmd).getBundle();
        } else {
            bundle = new Bundle();
            bundle.putByteArray(InternalBandConstants.EXTRA_COMMAND_DATA, cmd.getCommandRelatedData());
            bundle.putInt(InternalBandConstants.EXTRA_COMMAND_QUEUE_LIMIT, cmd.getQueueLimit());
        }
        byte[] payload = cmd.getExtendedData();
        bundle.putByteArray(InternalBandConstants.EXTRA_COMMAND_PAYLOAD, payload);
        bundle.putInt(InternalBandConstants.EXTRA_COMMAND_PAYLOAD_SIZE, payload == null ? cmd.getMessageSize() : payload.length);
        long commandIndex = this.mCommandCounter.incrementAndGet();
        bundle.putLong(InternalBandConstants.EXTRA_COMMAND_INDEX, commandIndex);
        this.mSentCommands.put(Long.valueOf(commandIndex), task);
        cmd.setCommandIndex(commandIndex);
        try {
            sendServiceMessage(BandServiceMessage.PROCESS_COMMAND, cmd.getCommandId(), bundle);
            return task;
        } catch (CargoException e) {
            this.mSentCommands.remove(Long.valueOf(commandIndex));
            KDKLog.e(TAG, e, "%s caught during send method. Task with index %s removed", e.getClass().getSimpleName(), Long.valueOf(commandIndex));
            if (BandServiceMessage.Response.CLIENT_VERSION_UNSUPPORTED_ERROR == e.getResponse()) {
                throw new BandIOException("Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.", BandErrorType.UNSUPPORTED_SDK_VERSION_ERROR);
            }
            throw new BandIOException("Microsoft Health BandService is not bound. Please make sure Microsoft Health is installed and that you have connected to it with the correct permissions.", BandErrorType.SERVICE_ERROR);
        } catch (Error e2) {
            this.mSentCommands.remove(Long.valueOf(commandIndex));
            KDKLog.e(TAG, e2, "%s caught during send method. Task with index %s removed", e2.getClass().getSimpleName(), Long.valueOf(commandIndex));
            throw e2;
        } catch (RuntimeException e3) {
            this.mSentCommands.remove(Long.valueOf(commandIndex));
            KDKLog.e(TAG, e3, "%s caught during send method. Task with index %s removed", e3.getClass().getSimpleName(), Long.valueOf(commandIndex));
            throw e3;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BandPendingResult<Void> sendWriteCommand(BandDeviceConstants.Command commandType, Bundle bundle) throws BandIOException {
        return send(new WaitingCommandTask(new ServiceCommand(commandType, bundle)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Deprecated
    public boolean sendCommand(CommandBase cmd, boolean waitForCompletion) throws CargoException {
        try {
            BandPendingResult<CommandBase> pendingResult = send(new WaitingCommandTask<CommandBase, CommandBase>(cmd) { // from class: com.microsoft.band.BandServiceConnection.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.microsoft.band.WaitingCommandTask
                public CommandBase toResult(CommandBase command, boolean isTimeOut) {
                    return command;
                }
            });
            CommandBase command = cmd;
            if (waitForCompletion) {
                try {
                    command = pendingResult.await(PROCESS_COMMAND_RESPONSE_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS);
                } catch (BandException e) {
                    throw new CargoException(TAG, e, BandServiceMessage.Response.DEVICE_COMMAND_ERROR);
                } catch (InterruptedException e2) {
                    KDKLog.d(TAG, "something wrong", e2);
                } catch (TimeoutException e3) {
                    KDKLog.i(TAG, e3, "%s command timed out.", cmd.getCommandType());
                    cmd.setResultCode(BandServiceMessage.Response.OPERATION_TIMEOUT_ERROR.getCode());
                }
            } else {
                KDKLog.i(TAG, "Sent service %s command asynchronously.", cmd.getCommandType());
                cmd.setResultCode(BandServiceMessage.Response.PENDING.getCode());
            }
            if (command.isResultCodeSevere()) {
                throw new CargoException(String.format("%s command failed with %s", command.getCommandType(), command.getResultString()), BandServiceMessage.Response.lookup(cmd.getResultCode()));
            }
            return command.getResult();
        } catch (BandIOException e4) {
            throw new CargoException(e4.getMessage(), BandServiceMessage.Response.SERVICE_TERMINATED_ERROR);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getHardwareVersion() throws BandIOException, InterruptedException, BandException {
        DeviceInfo di = getDeviceInfo();
        if (di != null && di.getHardwareVersion() != 0) {
            return di.getHardwareVersion();
        }
        ServiceCommand cmdGetHardwareVersion = new ServiceCommand(BandDeviceConstants.Command.BandGetHardwareVersion);
        WaitingCommandTask<Short, ServiceCommand> task = new WaitingCommandTask<Short, ServiceCommand>(cmdGetHardwareVersion) { // from class: com.microsoft.band.BandServiceConnection.3
            @Override // com.microsoft.band.WaitingCommandTask
            public Short toResult(ServiceCommand command, boolean isTimeOut) throws BandException {
                Validation.validateTimeoutAndResultCode(command, isTimeOut, BandServiceConnection.TAG);
                return Short.valueOf(command.getBundle().getShort(InternalBandConstants.EXTRA_HARDWARE_VERSION));
            }
        };
        return ((Short) send(task).await()).shortValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dispose() {
        synchronized (this.mServiceConnection) {
            if (isInitialized()) {
                unbind();
                if (!isServiceUnbinding()) {
                    setConnectionState(null);
                } else {
                    setConnectionState(ConnectionState.DISPOSED);
                }
            }
        }
    }

    private void setConnectionState(ConnectionState connectionState) {
        if (isInitialized()) {
            synchronized (this.mServiceBoundFlag) {
                if (this.mConnectionState != connectionState) {
                    KDKLog.d(TAG, "Change connection state from %s to %s", this.mConnectionState, connectionState);
                    this.mConnectionState = connectionState;
                    fireConnectionStateListener(connectionState);
                    if (this.mConnectionState == null) {
                        KDKLog.i(TAG, "Instance disposed.");
                    }
                    KDKLog.i(TAG, "Set Connection State: %s", connectionState);
                }
                if (this.mServiceBoundFlag.get() != isServiceBound()) {
                    KDKLog.d(TAG, "notify mServiceBoundFlag waiters");
                    this.mServiceBoundFlag.set(isServiceBound());
                    this.mServiceBoundFlag.notifyAll();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRegisterClientResponse(Message msg) {
        KDKLog.d(TAG, "Got register client response error=" + BandServiceMessage.Response.isErrorCode(msg.arg2));
        if (isServiceBinding()) {
            if (!BandServiceMessage.Response.isErrorCode(msg.arg2) && msg.getData() != null) {
                this.mSessionToken = SessionToken.fromBundle(msg.getData());
                if (this.mSessionToken != null) {
                    KDKLog.i(TAG, "Client registration successful: %s", this.mSessionToken);
                    setConnectionState(ConnectionState.BOUND);
                    return;
                } else if (msg.arg1 != 0) {
                    this.mServiceSdkVersion = msg.arg1;
                }
            }
            KDKLog.e(TAG, "Client registration failed: %s(%d).", BandServiceMessage.Response.lookup(msg.arg2), Integer.valueOf(msg.arg2));
            unbind();
            if (BandServiceMessage.Response.CLIENT_VERSION_UNSUPPORTED_ERROR.getCode() == msg.arg2) {
                setConnectionState(ConnectionState.INVALID_SDK_VERSION);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCommandResponse(Message msg) {
        int commandId = msg.arg1;
        int responseCode = msg.arg2;
        Bundle data = msg.getData();
        long commandIndex = data.getLong(InternalBandConstants.EXTRA_COMMAND_INDEX);
        WaitingCommandTask<?, ? extends CommandBase> waitingTask = this.mSentCommands.remove(Long.valueOf(commandIndex));
        if (waitingTask == null) {
            KDKLog.w(TAG, "Command (id=%s, command=%s) not found in waiting list.", Integer.valueOf(commandId), BandDeviceConstants.Command.lookup(commandId));
            return;
        }
        synchronized (waitingTask.lock()) {
            CommandBase command = waitingTask.getCommand();
            if (BandServiceMessage.Response.isErrorCode(responseCode)) {
                command.setResultCode(responseCode);
                KDKLog.e(TAG, "Command response error %s for command %s.", BandServiceMessage.Response.lookup(responseCode), BandDeviceConstants.Command.lookup(commandId));
            } else if (command instanceof ServiceCommand) {
                ((ServiceCommand) command).setBundle(data, responseCode);
            } else {
                int resultCode = data.getInt(InternalBandConstants.EXTRA_COMMAND_RESULT_CODE);
                byte[] responseData = data.getByteArray(InternalBandConstants.EXTRA_COMMAND_PAYLOAD);
                try {
                    command.processResponse(resultCode, responseData);
                } catch (IOException e) {
                    KDKLog.e(TAG, "Failed to process command response for command %s: %s", BandDeviceConstants.Command.lookup(command.getCommandId()), e.getMessage());
                }
            }
            waitingTask.ready();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setPushHandler(PushHandler pushHandler) {
        this.mPushHandler = pushHandler;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePushData(Message msg) {
        if (this.mPushHandler != null) {
            Bundle data = msg.getData();
            this.mPushHandler.handlePushData(msg.arg1, data.getParcelable(InternalBandConstants.EXTRA_SUBSCRIPTION_DATA));
        }
    }

    void onServiceBound(IBinder serviceBinder) {
        synchronized (this.mServiceConnection) {
            if (isServiceBinding()) {
                this.mServiceMessenger = new Messenger(serviceBinder);
                this.mEventHandler = new EventHandlerThread(TAG, this.mEventHandlerDelegate);
                this.mEventHandler.startLooper();
                this.mMessenger = new Messenger(this.mEventHandler.getHandler());
                try {
                    KDKLog.i(TAG, "Registering with Cargo Service... with messenger = %s", this.mMessenger);
                    sendServiceMessage(BandServiceMessage.REGISTER_CLIENT_WITH_VERSION, 16842752, this.mExtraData);
                    return;
                } catch (CargoException e) {
                    KDKLog.e(TAG, "Failed to send %s, aborting service binding.", BandServiceMessage.REGISTER_CLIENT);
                }
            }
            unbind();
        }
    }

    void onServiceUnbound() {
        synchronized (this.mServiceConnection) {
            this.mSentCommands.clear();
            this.mSessionToken = null;
            this.mServiceMessenger = null;
            this.mCommandCounter.set(0L);
            if (this.mEventHandler != null) {
                this.mEventHandler.stopLooper();
                this.mEventHandler = null;
            }
            this.mMessenger = null;
            KDKLog.i(TAG, "Cargo Service is unbound.");
            if (isInitialized()) {
                setConnectionState(ConnectionState.UNBOUND);
            } else {
                setConnectionState(null);
            }
        }
    }

    private void exceptionIfNotInitialized() throws CargoException {
        if (!isInitialized()) {
            throw new CargoException("Cargo Connection instance has been terminated.", BandServiceMessage.Response.SERVICE_TERMINATED_ERROR);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDeviceStatusNotification(Message msg) {
        if (isServiceBound()) {
            if (BandServiceMessage.Response.DEVICE_CONNECTED.getCode() == msg.arg2) {
                setConnectionState(ConnectionState.CONNECTED);
            } else if (BandServiceMessage.Response.DEVICE_DISCONNECTED.getCode() == msg.arg2) {
                setConnectionState(ConnectionState.BOUND);
            }
            Bundle data = msg.getData();
            DeviceInfo deviceInfo = (DeviceInfo) data.getParcelable(InternalBandConstants.EXTRA_DEVICE_INFO);
            if (deviceInfo != null) {
                this.mDeviceInfo = deviceInfo;
            }
            if (this.mDeviceStatusListener != null) {
                this.mDeviceStatusListener.onDeviceStatusNotification(msg);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BandServiceConnection setDeviceStatusListener(DeviceStatusListener mDeviceStatusListener) {
        this.mDeviceStatusListener = mDeviceStatusListener;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BandServiceConnection setSyncNotificationListener(SyncNotificationListener mSyncNotificationListener) {
        this.mSyncNotificationListener = mSyncNotificationListener;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BandServiceConnection setSyncProgressNotificationListener(SyncProgressNotificationListener mSyncProgressNotificationListener) {
        this.mSyncProgressNotificationListener = mSyncProgressNotificationListener;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BandServiceConnection setFirmwareUpgradeProgressNotificationListener(FirmwareUpgradeProgressNotificationListener mFirmwareUpgradeProgressNotificationListener) {
        this.mFirmwareUpgradeProgressNotificationListener = mFirmwareUpgradeProgressNotificationListener;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BandServiceConnection setDownloadNotificationListener(DownloadNotificationListener mDownloadNotificationListener) {
        this.mDownloadNotificationListener = mDownloadNotificationListener;
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BandServiceConnection setUpgradeNotificationListener(UpgradeNotificationListener mUpgradeNotificationListener) {
        this.mUpgradeNotificationListener = mUpgradeNotificationListener;
        return this;
    }

    boolean serviceHasPatchLevel(int patchLevel) {
        return (this.mServiceSdkVersion & (-16777216)) == ((-16777216) & patchLevel) && ((this.mServiceSdkVersion & InternalBandConstants.BAND_SDK_MINOR_VERSION_MASK) > (patchLevel & InternalBandConstants.BAND_SDK_MINOR_VERSION_MASK) || ((this.mServiceSdkVersion & InternalBandConstants.BAND_SDK_MINOR_VERSION_MASK) == (patchLevel & InternalBandConstants.BAND_SDK_MINOR_VERSION_MASK) && (this.mServiceSdkVersion & 65535) >= (patchLevel & 65535)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class EventHandlerDelegate implements EventHandlerThread.IEventHandlerDelegate {
        private EventHandlerDelegate() {
        }

        @Override // com.microsoft.band.internal.EventHandlerThread.IEventHandlerDelegate
        public void handleMessage(Message msg) {
            synchronized (BandServiceConnection.this.mServiceConnection) {
                KDKLog.i(BandServiceConnection.TAG, "Message Received: %s with %s response.", BandServiceMessage.lookup(msg.what), BandServiceMessage.Response.lookup(msg.arg2));
                if (msg.getData() != null) {
                    msg.getData().setClassLoader(getClass().getClassLoader());
                }
                if (BandServiceConnection.this.isInitialized()) {
                    if (BandServiceMessage.PROCESS_PUSH_DATA.isEqual(msg.what)) {
                        BandServiceConnection.this.handlePushData(msg);
                    } else if (BandServiceMessage.REGISTER_CLIENT_RESPONSE.isEqual(msg.what)) {
                        BandServiceConnection.this.handleRegisterClientResponse(msg);
                    } else if (BandServiceMessage.PROCESS_COMMAND_RESPONSE.isEqual(msg.what)) {
                        if (msg.getData() != null) {
                            BandServiceConnection.this.handleCommandResponse(msg);
                        }
                    } else if (BandServiceMessage.SYNC_NOTIFICATION.isEqual(msg.what)) {
                        if (BandServiceConnection.this.mSyncNotificationListener != null) {
                            BandServiceConnection.this.mSyncNotificationListener.onSyncNotification(msg);
                        }
                    } else if (BandServiceMessage.SYNC_PROGRESS.isEqual(msg.what)) {
                        if (BandServiceConnection.this.mSyncProgressNotificationListener != null) {
                            BandServiceConnection.this.mSyncProgressNotificationListener.onSyncProgressNotification(msg);
                        }
                    } else if (BandServiceMessage.DEVICE_STATUS_NOTIFICATION.isEqual(msg.what)) {
                        BandServiceConnection.this.handleDeviceStatusNotification(msg);
                    } else if (BandServiceMessage.DOWNLOAD_NOTIFICATION.isEqual(msg.what)) {
                        if (BandServiceConnection.this.mDownloadNotificationListener != null) {
                            BandServiceConnection.this.mDownloadNotificationListener.onDownloadNotification(msg);
                        }
                    } else if (BandServiceMessage.UPGRADE_NOTIFICATION.isEqual(msg.what)) {
                        if (BandServiceConnection.this.mUpgradeNotificationListener != null) {
                            BandServiceConnection.this.mUpgradeNotificationListener.onUpgradeNotification(msg);
                        }
                    } else if (BandServiceMessage.FIRMWARE_UPGRADE_PROGRESS.isEqual(msg.what)) {
                        if (BandServiceConnection.this.mFirmwareUpgradeProgressNotificationListener != null) {
                            BandServiceConnection.this.mFirmwareUpgradeProgressNotificationListener.onFirmwareUpgradeProgressNotification(msg);
                        }
                    } else if (BandServiceMessage.QUERY_IS_CLIENT_ALIVE.isEqual(msg.what)) {
                        try {
                            BandServiceConnection.this.sendServiceMessage(BandServiceMessage.QUERY_IS_CLIENT_ALIVE_RESPONSE, 0, null);
                        } catch (CargoException e) {
                            KDKLog.e(BandServiceConnection.TAG, "Failed send %s to service, client session will be invalidated by the service.", BandServiceMessage.QUERY_IS_CLIENT_ALIVE_RESPONSE);
                        }
                    }
                }
            }
        }
    }
}

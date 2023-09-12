package com.microsoft.band;

import android.content.Context;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.ServiceCommand;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.notifications.BandNotificationManager;
import com.microsoft.band.personalization.BandPersonalizationManager;
import com.microsoft.band.sensors.BandSensorManager;
import com.microsoft.band.tiles.BandTileManager;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class BandClientImpl implements BandClient {
    private final BandTileManager mBandTileManager;
    private final BandNotificationManager mNotificationMagager;
    private final BandPersonalizationManager mPersonalizationManager;
    private final BandSensorManager mSensorManager;
    private final BandServiceConnection mServiceConnection;
    private static final String TAG = BandClientImpl.class.getSimpleName();
    private static final BandPendingResult<Void> EMPTY_TASK = new EmptyResult();

    /* loaded from: classes.dex */
    private static class ConnectionBandPendingResult implements BandPendingResult<ConnectionState> {
        private final WaitingCommandTask<ConnectionState, ServiceCommand> mConnectionTask;
        private volatile BandException mException;
        private final Object mLock = new Object();
        private final BandServiceConnection mServiceConnection;
        private volatile BandPendingResult<ConnectionState> mWrappedTask;

        public ConnectionBandPendingResult(BandServiceConnection connection, WaitingCommandTask<ConnectionState, ServiceCommand> task) {
            this.mServiceConnection = connection;
            this.mConnectionTask = task;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.microsoft.band.BandPendingResult
        public ConnectionState await() throws InterruptedException, BandException {
            if (this.mException != null) {
                throw this.mException;
            }
            return sendInternal().await();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.microsoft.band.BandPendingResult
        public ConnectionState await(long timeout, TimeUnit timeUnit) throws InterruptedException, TimeoutException, BandException {
            if (this.mException != null) {
                throw this.mException;
            }
            return sendInternal().await(timeout, timeUnit);
        }

        public BandPendingResult<ConnectionState> sendInternal() throws BandException {
            this.mServiceConnection.waitForServiceToBind();
            if (this.mWrappedTask == null) {
                synchronized (this.mLock) {
                    if (this.mWrappedTask == null) {
                        this.mWrappedTask = this.mServiceConnection.send(this.mConnectionTask);
                    }
                }
            }
            return this.mWrappedTask;
        }

        @Override // com.microsoft.band.BandPendingResult
        public void registerResultCallback(BandResultCallback<ConnectionState> callback) {
            this.mConnectionTask.registerResultCallback(callback);
        }

        @Override // com.microsoft.band.BandPendingResult
        public void registerResultCallback(BandResultCallback<ConnectionState> callback, long timeout, TimeUnit timeUnit) {
            this.mConnectionTask.registerResultCallback(callback, timeout, timeUnit);
        }

        public void setException(BandException e) {
            this.mException = e;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BandClientImpl(Context context, BandInfo device) {
        this(BandServiceConnection.create(context, device));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BandClientImpl(BandServiceConnection serviceConnection) {
        this.mServiceConnection = serviceConnection;
        this.mNotificationMagager = new BandNotificationManagerImpl(this.mServiceConnection);
        this.mSensorManager = new BandSensorManagerImpl(this.mServiceConnection);
        this.mBandTileManager = new BandTileManagerImpl(this.mServiceConnection);
        this.mPersonalizationManager = new BandPersonalizationManagerImpl(this.mServiceConnection);
    }

    @Override // com.microsoft.band.BandClient
    public BandPendingResult<ConnectionState> connect() {
        try {
            this.mServiceConnection.bind();
            ServiceCommand cmdConnect = new ServiceCommand(BandDeviceConstants.Command.CargoConnectDevice);
            WaitingCommandTask<ConnectionState, ServiceCommand> connectionTask = new WaitingCommandTask<ConnectionState, ServiceCommand>(cmdConnect) { // from class: com.microsoft.band.BandClientImpl.1
                @Override // com.microsoft.band.WaitingCommandTask
                public ConnectionState toResult(ServiceCommand command, boolean isTimeOut) {
                    return BandClientImpl.this.getConnectionState();
                }
            };
            final ConnectionBandPendingResult result = new ConnectionBandPendingResult(this.mServiceConnection, connectionTask);
            new Thread(new Runnable() { // from class: com.microsoft.band.BandClientImpl.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        result.sendInternal();
                    } catch (BandException e) {
                        result.setException(e);
                    }
                }
            }).start();
            return result;
        } catch (CargoException e) {
            return new ErrorBandPendingResult(new BandException("Unable to bind to the Microsoft Health BandService", BandErrorType.UNKNOWN_ERROR), ConnectionState.UNBOUND);
        }
    }

    @Override // com.microsoft.band.BandClient
    public boolean isConnected() {
        return this.mServiceConnection.isDeviceConnected();
    }

    @Override // com.microsoft.band.BandClient
    public BandPendingResult<Void> disconnect() {
        try {
            ServiceCommand cmdDisconnect = new ServiceCommand(BandDeviceConstants.Command.CargoDisconnectDevice);
            WaitingCommandTask<Void, ServiceCommand> disconnectionTask = new WaitingCommandTask<Void, ServiceCommand>(cmdDisconnect) { // from class: com.microsoft.band.BandClientImpl.3
                @Override // com.microsoft.band.WaitingCommandTask
                public Void toResult(ServiceCommand command, boolean isTimeOut) {
                    BandClientImpl.this.mServiceConnection.unbind();
                    return null;
                }
            };
            return this.mServiceConnection.send(disconnectionTask);
        } catch (BandIOException e) {
            return EMPTY_TASK;
        }
    }

    @Override // com.microsoft.band.BandClient
    public void registerConnectionCallback(BandConnectionCallback callback) {
        this.mServiceConnection.registerConnectionCallback(callback);
    }

    @Override // com.microsoft.band.BandClient
    public void unregisterConnectionCallback() {
        this.mServiceConnection.unregisterConnectionCallback();
    }

    @Override // com.microsoft.band.BandClient
    public ConnectionState getConnectionState() {
        return this.mServiceConnection.getConnectionState();
    }

    @Override // com.microsoft.band.BandClient
    public BandPendingResult<String> getFirmwareVersion() throws BandIOException {
        ServiceCommand cmdGetFirmwareVersion = new ServiceCommand(BandDeviceConstants.Command.BandGetFirmwareVersion);
        WaitingCommandTask<String, ServiceCommand> task = new WaitingCommandTask<String, ServiceCommand>(cmdGetFirmwareVersion) { // from class: com.microsoft.band.BandClientImpl.4
            @Override // com.microsoft.band.WaitingCommandTask
            public String toResult(ServiceCommand command, boolean isTimeOut) throws BandException {
                Validation.validateTimeoutAndResultCode(command, isTimeOut, BandClientImpl.TAG);
                return command.getBundle().getString(InternalBandConstants.EXTRA_FIRMWARE_VERSION_APP);
            }
        };
        return this.mServiceConnection.send(task);
    }

    @Override // com.microsoft.band.BandClient
    public BandPendingResult<String> getHardwareVersion() throws BandIOException {
        ServiceCommand cmdGetHardwareVersion = new ServiceCommand(BandDeviceConstants.Command.BandGetHardwareVersion);
        WaitingCommandTask<String, ServiceCommand> task = new WaitingCommandTask<String, ServiceCommand>(cmdGetHardwareVersion) { // from class: com.microsoft.band.BandClientImpl.5
            @Override // com.microsoft.band.WaitingCommandTask
            public String toResult(ServiceCommand command, boolean isTimeOut) throws BandException {
                Validation.validateTimeoutAndResultCode(command, isTimeOut, BandClientImpl.TAG);
                return Short.toString(command.getBundle().getShort(InternalBandConstants.EXTRA_HARDWARE_VERSION));
            }
        };
        return this.mServiceConnection.send(task);
    }

    @Override // com.microsoft.band.BandClient
    public BandSensorManager getSensorManager() {
        return this.mSensorManager;
    }

    @Override // com.microsoft.band.BandClient
    public BandNotificationManager getNotificationManager() {
        return this.mNotificationMagager;
    }

    @Override // com.microsoft.band.BandClient
    public BandTileManager getTileManager() {
        return this.mBandTileManager;
    }

    @Override // com.microsoft.band.BandClient
    public BandPersonalizationManager getPersonalizationManager() {
        return this.mPersonalizationManager;
    }
}

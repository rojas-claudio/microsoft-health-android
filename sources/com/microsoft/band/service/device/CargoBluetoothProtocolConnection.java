package com.microsoft.band.service.device;

import com.microsoft.band.CargoCloudClient;
import com.microsoft.band.device.DeviceConstants;
import com.microsoft.band.device.FWVersion;
import com.microsoft.band.device.command.GetProductSerialNumber;
import com.microsoft.band.device.command.GetVersion;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.internal.util.UUIDHelper;
import com.microsoft.band.internal.util.VersionCheck;
import com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnection;
import com.microsoft.band.service.logger.CargoLogger;
import com.microsoft.band.service.logger.IntervalLogger;
import com.microsoft.band.service.logger.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public class CargoBluetoothProtocolConnection extends CargoBluetoothConnection implements CargoProtocolConnection {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final int COMMAND_RETRY_DELAY = 2000;
    private static final int COMMAND_RETRY_LIMIT = 2;
    private static final int FIRMWARE_UPGRAGE_RECONNECT_TIMEOUT = 300000;
    public static final UUID NORMAL_PROTOCOL_UUID;
    private static final String TAG;
    private volatile UUID mDeviceUUID;
    private volatile FWVersion[] mFwVersions;
    private volatile int mLogVersion;
    private final CargoLogger mLogger;
    private volatile DeviceConstants.AppRunning mRunningApplication;
    private volatile String mSerialNumber;
    private final AtomicBoolean mUpdatingFirmwareFlag;

    static {
        $assertionsDisabled = !CargoBluetoothProtocolConnection.class.desiredAssertionStatus();
        TAG = CargoBluetoothProtocolConnection.class.getSimpleName();
        NORMAL_PROTOCOL_UUID = UUID.fromString(BandDeviceConstants.GUID_CARGO_BLUETOOTH_PROTOCOL);
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection, com.microsoft.band.service.device.DeviceConnection
    public /* bridge */ /* synthetic */ void connect() throws IOException {
        super.connect();
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection, com.microsoft.band.service.device.DeviceConnection
    public /* bridge */ /* synthetic */ void connect(boolean x0) throws IOException {
        super.connect(x0);
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection, com.microsoft.band.service.device.DeviceConnection
    public /* bridge */ /* synthetic */ void disconnect() {
        super.disconnect();
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection, com.microsoft.band.service.device.DeviceConnection
    public /* bridge */ /* synthetic */ void dispose() {
        super.dispose();
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection, com.microsoft.band.service.device.DeviceConnection
    public /* bridge */ /* synthetic */ String getDeviceAddress() {
        return super.getDeviceAddress();
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection, com.microsoft.band.service.device.DeviceConnection
    public /* bridge */ /* synthetic */ DeviceInfo getDeviceInfo() {
        return super.getDeviceInfo();
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection, com.microsoft.band.service.device.DeviceConnection
    public /* bridge */ /* synthetic */ String getDeviceName() {
        return super.getDeviceName();
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection, com.microsoft.band.service.device.DeviceConnection
    public /* bridge */ /* synthetic */ DeviceServiceProvider getServiceProvider() {
        return super.getServiceProvider();
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection, com.microsoft.band.service.device.DeviceConnection
    public /* bridge */ /* synthetic */ boolean isConnected() {
        return super.isConnected();
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection, com.microsoft.band.service.device.DeviceConnection
    public /* bridge */ /* synthetic */ boolean isDisposed() {
        return super.isDisposed();
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection, com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnection.BluetoothDeviceConnectionListener
    public /* bridge */ /* synthetic */ boolean onBluetoothDeviceBound(BluetoothDeviceConnection x0) {
        return super.onBluetoothDeviceBound(x0);
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection, com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnection.BluetoothDeviceConnectionListener
    public /* bridge */ /* synthetic */ boolean onBluetoothDeviceConnected(BluetoothDeviceConnection x0) {
        return super.onBluetoothDeviceConnected(x0);
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection, com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnection.BluetoothDeviceConnectionListener
    public /* bridge */ /* synthetic */ void onBluetoothDeviceDataReceived(BluetoothDeviceConnection x0, byte[] x1, int x2) {
        super.onBluetoothDeviceDataReceived(x0, x1, x2);
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection, com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnection.BluetoothDeviceConnectionListener
    public /* bridge */ /* synthetic */ void onBluetoothDeviceDisconnected(BluetoothDeviceConnection x0) {
        super.onBluetoothDeviceDisconnected(x0);
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection, com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnection.BluetoothDeviceConnectionListener
    public /* bridge */ /* synthetic */ void onBluetoothDeviceError(BluetoothDeviceConnection x0, Exception x1) {
        super.onBluetoothDeviceError(x0, x1);
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection, com.microsoft.band.service.device.DeviceConnection
    public /* bridge */ /* synthetic */ void reset() {
        super.reset();
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection, com.microsoft.band.service.device.DeviceConnection
    public /* bridge */ /* synthetic */ void waitForDeviceToDisconnect() {
        super.waitForDeviceToDisconnect();
    }

    public CargoBluetoothProtocolConnection(DeviceServiceProvider deviceProvider) {
        super(deviceProvider);
        this.mLogger = LoggerFactory.getLogger();
        this.mUpdatingFirmwareFlag = new AtomicBoolean(false);
    }

    @Override // com.microsoft.band.service.device.DeviceConnection
    public UUID getProtocolUUID() {
        return NORMAL_PROTOCOL_UUID;
    }

    @Override // com.microsoft.band.service.device.CargoProtocolConnection
    public boolean isUpdatingFirmware() {
        return this.mUpdatingFirmwareFlag.get();
    }

    @Override // com.microsoft.band.service.device.CargoProtocolConnection
    public UUID getDeviceUUID() {
        return this.mDeviceUUID;
    }

    @Override // com.microsoft.band.service.device.CargoProtocolConnection
    public int getLogVersion() {
        return this.mLogVersion;
    }

    @Override // com.microsoft.band.service.device.CargoProtocolConnection
    public String getSerialNumber() {
        return this.mSerialNumber;
    }

    @Override // com.microsoft.band.service.device.CargoProtocolConnection
    public DeviceConstants.AppRunning getRunningApplication() {
        return this.mRunningApplication;
    }

    @Override // com.microsoft.band.service.device.CargoProtocolConnection
    public FWVersion[] getFirmwareVersions() {
        return this.mFwVersions;
    }

    @Override // com.microsoft.band.service.device.CargoProtocolConnection
    public BandServiceMessage.Response processCommand(DeviceCommand command) {
        if (command == null) {
            throw new NullPointerException("command");
        }
        BandServiceMessage.Response response = BandServiceMessage.Response.DEVICE_NOT_CONNECTED_ERROR;
        synchronized (this.mConnectedFlag) {
            if (!isConnected()) {
                try {
                    connect(true);
                } catch (IOException e) {
                    KDKLog.e(TAG, "Failed to connection to device.", e);
                }
            }
            if (isConnected()) {
                int retryCount = 0;
                while (true) {
                    if (retryCount > 0) {
                        try {
                            this.mConnectedFlag.wait(CargoCloudClient.CLOUD_PROCESSING_SUCCESS_WAIT_TIME);
                            KDKLog.i(TAG, "Retrying command %d of %d times...", Integer.valueOf(retryCount), 2);
                        } catch (InterruptedException e2) {
                            KDKLog.w(TAG, "Interrupt for wait:" + e2.getMessage());
                            response = BandServiceMessage.Response.OPERATION_INTERRUPTED_ERROR;
                        }
                    }
                    response = performIO(command);
                    if (BandServiceMessage.Response.DEVICE_IO_ERROR == response || BandServiceMessage.Response.DEVICE_TIMEOUT_ERROR == response || BandServiceMessage.Response.DEVICE_NOT_CONNECTED_ERROR == response) {
                        disconnect();
                    }
                    if (BandServiceMessage.Response.DEVICE_COMMAND_RESPONSE_ERROR != response || !command.canRetry()) {
                        break;
                    }
                    int retryCount2 = retryCount + 1;
                    if (retryCount >= 2) {
                        break;
                    }
                    retryCount = retryCount2;
                }
            }
        }
        if (response.isError() && !command.hasResponse()) {
            command.setResultCode(response.getCode());
        }
        return response;
    }

    @Override // com.microsoft.band.service.device.CargoProtocolConnection
    public BandServiceMessage.Response bootIntoFirmwareUpdateMode() {
        BandServiceMessage.Response response = BandServiceMessage.Response.DEVICE_STATE_ERROR;
        if (isConnected() && DeviceConstants.AppRunning.APP_RUNNING_UPAPP != getRunningApplication() && !isUpdatingFirmware()) {
            this.mUpdatingFirmwareFlag.set(true);
            KDKLog.w(TAG, "Booting device %s into firmware update mode..", this);
            DeviceCommand cmdBootUpApp = new DeviceCommand(BandDeviceConstants.Command.CargoSRAMFWUpdateBootIntoUpdateMode, (byte[]) null);
            response = performIO(cmdBootUpApp);
            if (response.isError()) {
                this.mUpdatingFirmwareFlag.set(false);
                KDKLog.e(TAG, "Failed to put device into firmware update mode, response code: %s.", response);
            } else {
                reset();
            }
            KDKLog.w(TAG, "...device %s is in %s state.", this, getRunningApplication());
        }
        return response;
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x002d, code lost:
        com.microsoft.band.internal.util.KDKLog.e(com.microsoft.band.service.device.CargoBluetoothProtocolConnection.TAG, "Device is not in the proper state: " + r0);
        r2 = com.microsoft.band.internal.BandServiceMessage.Response.DEVICE_STATE_ERROR;
     */
    @Override // com.microsoft.band.service.device.CargoProtocolConnection
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.microsoft.band.internal.BandServiceMessage.Response waitForFirmwareUpdateToComplete() {
        /*
            r11 = this;
            com.microsoft.band.internal.BandServiceMessage$Response r2 = com.microsoft.band.internal.BandServiceMessage.Response.INVALID_OPERATION_ERROR
            java.util.concurrent.atomic.AtomicBoolean r6 = r11.mUpdatingFirmwareFlag
            monitor-enter(r6)
            boolean r5 = r11.isUpdatingFirmware()     // Catch: java.lang.Throwable -> Lcb
            if (r5 == 0) goto L55
            r3 = 0
            com.microsoft.band.internal.BandServiceMessage$Response r2 = com.microsoft.band.internal.BandServiceMessage.Response.PENDING     // Catch: java.lang.Throwable -> Lcb
        Lf:
            com.microsoft.band.device.DeviceConstants$AppRunning r5 = com.microsoft.band.device.DeviceConstants.AppRunning.APP_RUNNING_APP     // Catch: java.lang.Throwable -> Lcb
            com.microsoft.band.device.DeviceConstants$AppRunning r0 = r11.queryRunningApplication()     // Catch: java.lang.Throwable -> Lcb
            if (r5 == r0) goto L48
            boolean r5 = r2.isError()     // Catch: java.lang.Throwable -> Lcb
            if (r5 != 0) goto L48
            java.util.concurrent.atomic.AtomicBoolean r5 = r11.mUpdatingFirmwareFlag     // Catch: java.lang.Throwable -> Lcb
            boolean r5 = r5.get()     // Catch: java.lang.Throwable -> Lcb
            if (r5 == 0) goto L48
            com.microsoft.band.device.DeviceConstants$AppRunning r5 = com.microsoft.band.device.DeviceConstants.AppRunning.APP_RUNNING_1BL     // Catch: java.lang.Throwable -> Lcb
            if (r0 == r5) goto L2d
            com.microsoft.band.device.DeviceConstants$AppRunning r5 = com.microsoft.band.device.DeviceConstants.AppRunning.APP_RUNNING_2UP     // Catch: java.lang.Throwable -> Lcb
            if (r0 != r5) goto L57
        L2d:
            java.lang.String r5 = com.microsoft.band.service.device.CargoBluetoothProtocolConnection.TAG     // Catch: java.lang.Throwable -> Lcb
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lcb
            r7.<init>()     // Catch: java.lang.Throwable -> Lcb
            java.lang.String r8 = "Device is not in the proper state: "
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch: java.lang.Throwable -> Lcb
            java.lang.StringBuilder r7 = r7.append(r0)     // Catch: java.lang.Throwable -> Lcb
            java.lang.String r7 = r7.toString()     // Catch: java.lang.Throwable -> Lcb
            com.microsoft.band.internal.util.KDKLog.e(r5, r7)     // Catch: java.lang.Throwable -> Lcb
            com.microsoft.band.internal.BandServiceMessage$Response r2 = com.microsoft.band.internal.BandServiceMessage.Response.DEVICE_STATE_ERROR     // Catch: java.lang.Throwable -> Lcb
        L48:
            boolean r5 = r2.isError()     // Catch: java.lang.Throwable -> Lcb
            if (r5 != 0) goto L50
            com.microsoft.band.internal.BandServiceMessage$Response r2 = com.microsoft.band.internal.BandServiceMessage.Response.SUCCESS     // Catch: java.lang.Throwable -> Lcb
        L50:
            java.util.concurrent.atomic.AtomicBoolean r5 = r11.mUpdatingFirmwareFlag     // Catch: java.lang.Throwable -> Lcb
            r5.notifyAll()     // Catch: java.lang.Throwable -> Lcb
        L55:
            monitor-exit(r6)     // Catch: java.lang.Throwable -> Lcb
            return r2
        L57:
            if (r0 != 0) goto L78
            r7 = 0
            int r5 = (r3 > r7 ? 1 : (r3 == r7 ? 0 : -1))
            if (r5 != 0) goto Lab
            java.lang.String r5 = com.microsoft.band.service.device.CargoBluetoothProtocolConnection.TAG     // Catch: java.lang.Throwable -> Lcb
            java.lang.String r7 = "Device is now finalizing firmware update. Waiting upto %d ms for it to reboot..."
            r8 = 1
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch: java.lang.Throwable -> Lcb
            r9 = 0
            r10 = 300000(0x493e0, float:4.2039E-40)
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)     // Catch: java.lang.Throwable -> Lcb
            r8[r9] = r10     // Catch: java.lang.Throwable -> Lcb
            com.microsoft.band.internal.util.KDKLog.d(r5, r7, r8)     // Catch: java.lang.Throwable -> Lcb
            long r3 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> Lcb
        L78:
            boolean r5 = r11.isConnected()     // Catch: java.lang.Throwable -> Lcb
            if (r5 != 0) goto L81
            r11.reconnectDevice()     // Catch: java.lang.Throwable -> Lcb
        L81:
            java.lang.String r5 = com.microsoft.band.service.device.CargoBluetoothProtocolConnection.TAG     // Catch: java.lang.InterruptedException -> L9e java.lang.Throwable -> Lcb
            java.lang.String r7 = "Waiting %d ms for device to reboot after firmware update..."
            r8 = 1
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch: java.lang.InterruptedException -> L9e java.lang.Throwable -> Lcb
            r9 = 0
            r10 = 10000(0x2710, float:1.4013E-41)
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)     // Catch: java.lang.InterruptedException -> L9e java.lang.Throwable -> Lcb
            r8[r9] = r10     // Catch: java.lang.InterruptedException -> L9e java.lang.Throwable -> Lcb
            com.microsoft.band.internal.util.KDKLog.w(r5, r7, r8)     // Catch: java.lang.InterruptedException -> L9e java.lang.Throwable -> Lcb
            java.util.concurrent.atomic.AtomicBoolean r5 = r11.mUpdatingFirmwareFlag     // Catch: java.lang.InterruptedException -> L9e java.lang.Throwable -> Lcb
            r7 = 10000(0x2710, double:4.9407E-320)
            r5.wait(r7)     // Catch: java.lang.InterruptedException -> L9e java.lang.Throwable -> Lcb
            goto Lf
        L9e:
            r1 = move-exception
            java.lang.String r5 = com.microsoft.band.service.device.CargoBluetoothProtocolConnection.TAG     // Catch: java.lang.Throwable -> Lcb
            java.lang.String r7 = "Interupted while waiting for device to reboot after firmware update."
            com.microsoft.band.internal.util.KDKLog.e(r5, r7)     // Catch: java.lang.Throwable -> Lcb
            com.microsoft.band.internal.BandServiceMessage$Response r2 = com.microsoft.band.internal.BandServiceMessage.Response.OPERATION_INTERRUPTED_ERROR     // Catch: java.lang.Throwable -> Lcb
            goto Lf
        Lab:
            long r7 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> Lcb
            long r7 = r7 - r3
            r9 = 300000(0x493e0, double:1.482197E-318)
            int r5 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r5 <= 0) goto L78
            java.lang.String r5 = com.microsoft.band.service.device.CargoBluetoothProtocolConnection.TAG     // Catch: java.lang.Throwable -> Lcb
            java.lang.String r7 = "Timed out waiting for device to reconnect after firmware update."
            com.microsoft.band.internal.util.KDKLog.e(r5, r7)     // Catch: java.lang.Throwable -> Lcb
            boolean r5 = r11.isConnected()     // Catch: java.lang.Throwable -> Lcb
            if (r5 == 0) goto Lc8
            com.microsoft.band.internal.BandServiceMessage$Response r2 = com.microsoft.band.internal.BandServiceMessage.Response.DEVICE_TIMEOUT_ERROR     // Catch: java.lang.Throwable -> Lcb
        Lc7:
            goto L48
        Lc8:
            com.microsoft.band.internal.BandServiceMessage$Response r2 = com.microsoft.band.internal.BandServiceMessage.Response.DEVICE_NOT_CONNECTED_ERROR     // Catch: java.lang.Throwable -> Lcb
            goto Lc7
        Lcb:
            r5 = move-exception
            monitor-exit(r6)     // Catch: java.lang.Throwable -> Lcb
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.microsoft.band.service.device.CargoBluetoothProtocolConnection.waitForFirmwareUpdateToComplete():com.microsoft.band.internal.BandServiceMessage$Response");
    }

    @Override // com.microsoft.band.service.device.CargoProtocolConnection
    public void resetDevice() {
        DeviceCommand cmdReset = new DeviceCommand(BandDeviceConstants.Command.CargoCoreModuleReset, (byte[]) null);
        this.mUpdatingFirmwareFlag.set(false);
        try {
            performIO(cmdReset);
        } finally {
            disconnect();
        }
    }

    @Override // com.microsoft.band.service.device.CargoProtocolConnection
    public BandServiceMessage.Response performIO(DeviceCommand cmd) {
        BandServiceMessage.Response response;
        String str;
        String str2;
        BandServiceMessage.Response response2;
        String str3;
        String str4;
        byte[] cmdBytes = cmd.getCommandBuffer();
        OutputStream out = getOutputStream();
        InputStream in = getInputStream();
        IntervalLogger rxLogger = new IntervalLogger(this.mLogger);
        IntervalLogger txLogger = new IntervalLogger(this.mLogger);
        rxLogger.mark("Start process command:" + cmd.getCommandId());
        txLogger.mark("Start process command:" + cmd.getCommandId());
        if (in == null || out == null) {
            return BandServiceMessage.Response.DEVICE_NOT_CONNECTED_ERROR;
        }
        BandServiceMessage.Response response3 = BandServiceMessage.Response.DEVICE_IO_ERROR;
        boolean isSent = false;
        try {
            try {
                this.mLogger.traceDiagnostics(System.currentTimeMillis(), TAG, "IO", "Writing command data [%s], command data size %d bytes.", cmdBytes, Integer.valueOf(cmd.getCommandDataSize()));
                txLogger.mark("start send command");
                out.write(cmdBytes, 0, cmd.getCommandDataSize());
                txLogger.mark("finish send command");
                if (cmd.isReceivingPayload()) {
                    rxLogger.mark("start read payload");
                    readWithTimeout(in, cmd.getPayload(), 0, cmd.getPayloadSize());
                    rxLogger.mark("finish read payload");
                    this.mLogger.traceDiagnostics(System.currentTimeMillis(), TAG, "IO", "Reading command data [%s], payload size %d bytes.", cmd.getPayload(), Integer.valueOf(cmd.getPayloadSize()));
                } else if (cmd.getPayloadSize() > 0) {
                    this.mLogger.traceDiagnostics(System.currentTimeMillis(), TAG, "IO", "Writing command payload [%s], payload size %d bytes.", cmd.getPayload(), Integer.valueOf(cmd.getPayloadSize()));
                    txLogger.mark("start send command payload");
                    out.write(cmd.getPayload(), 0, cmd.getPayloadSize());
                    txLogger.mark("end send command payload");
                }
                isSent = true;
                readWithTimeout(in, cmdBytes, cmd.getBufferOffsetToStatusPacket(), 6);
                rxLogger.mark("command status received");
                this.mLogger.traceDiagnostics(System.currentTimeMillis(), "IO", TAG, "Reading response command data [%s], command data size %d bytes.", cmdBytes, Integer.valueOf(cmd.getCommandDataSize()));
                response3 = cmd.processResponse();
                int commandSize = cmd.getCommandDataSize() + cmd.getPayloadSize();
                txLogger.dump(TAG, "TX", "Time=%d, size=%d, speed(byte/ms)=%d", Long.valueOf(txLogger.total()), Integer.valueOf(commandSize), Long.valueOf(commandSize / txLogger.total()));
                int payLoadSize = cmd.isReceivingPayload() ? cmd.getPayloadSize() : 0;
                rxLogger.dump(TAG, "RX", "Time=%d, size=%d, speed(byte/ms=%d", Long.valueOf(rxLogger.total()), Integer.valueOf(payLoadSize), Long.valueOf(payLoadSize / rxLogger.total()));
            } catch (IOException ioe) {
                if (cmd.getCommandId() == BandDeviceConstants.Command.CargoSRAMFWUpdateBootIntoUpdateMode.getCode() && isSent) {
                    response3 = BandServiceMessage.Response.SUCCESS;
                } else {
                    KDKLog.e(TAG, ioe.getMessage(), ioe);
                }
                if (!isSent || cmd.getCommandId() != BandDeviceConstants.Command.CargoSRAMFWUpdateBootIntoUpdateMode.getCode()) {
                    return response3;
                }
                if (!response3.isError()) {
                    response = BandServiceMessage.Response.SUCCESS;
                    cmd.setResultCode(response.getCode());
                    str = TAG;
                    str2 = "Sent command to device to boot into firmware update mode.";
                } else if (cmd.getResultCode() == BandDeviceConstants.ResultCode.SRAMFWUPDATE_BATTERY_TOO_LOW.getCode()) {
                    return BandServiceMessage.Response.DEVICE_BATTERY_LOW_ERROR;
                } else {
                    if (cmd.getResultCode() != BandDeviceConstants.ResultCode.SRAMFWUPDATE_RESET_REASON_SRAM_TIMEOUT.getCode()) {
                        return response3;
                    }
                    response2 = BandServiceMessage.Response.DEVICE_TIMEOUT_ERROR;
                    str3 = TAG;
                    str4 = "Result SRAMFWUPDATE_RESET_REASON_SRAM_TIMEOUT received from band";
                }
            } catch (TimeoutException toe) {
                BandServiceMessage.Response response4 = BandServiceMessage.Response.DEVICE_TIMEOUT_ERROR;
                KDKLog.e(TAG, toe.getMessage(), toe);
                if (!isSent || cmd.getCommandId() != BandDeviceConstants.Command.CargoSRAMFWUpdateBootIntoUpdateMode.getCode()) {
                    return response4;
                }
                if (!response4.isError()) {
                    response = BandServiceMessage.Response.SUCCESS;
                    cmd.setResultCode(response.getCode());
                    str = TAG;
                    str2 = "Sent command to device to boot into firmware update mode.";
                } else if (cmd.getResultCode() == BandDeviceConstants.ResultCode.SRAMFWUPDATE_BATTERY_TOO_LOW.getCode()) {
                    return BandServiceMessage.Response.DEVICE_BATTERY_LOW_ERROR;
                } else {
                    if (cmd.getResultCode() != BandDeviceConstants.ResultCode.SRAMFWUPDATE_RESET_REASON_SRAM_TIMEOUT.getCode()) {
                        return response4;
                    }
                    response2 = BandServiceMessage.Response.DEVICE_TIMEOUT_ERROR;
                    str3 = TAG;
                    str4 = "Result SRAMFWUPDATE_RESET_REASON_SRAM_TIMEOUT received from band";
                }
            }
            if (1 == 0 || cmd.getCommandId() != BandDeviceConstants.Command.CargoSRAMFWUpdateBootIntoUpdateMode.getCode()) {
                return response3;
            }
            if (!response3.isError()) {
                response = BandServiceMessage.Response.SUCCESS;
                cmd.setResultCode(response.getCode());
                str = TAG;
                str2 = "Sent command to device to boot into firmware update mode.";
                KDKLog.w(str, str2);
                return response;
            } else if (cmd.getResultCode() == BandDeviceConstants.ResultCode.SRAMFWUPDATE_BATTERY_TOO_LOW.getCode()) {
                return BandServiceMessage.Response.DEVICE_BATTERY_LOW_ERROR;
            } else {
                if (cmd.getResultCode() == BandDeviceConstants.ResultCode.SRAMFWUPDATE_RESET_REASON_SRAM_TIMEOUT.getCode()) {
                    response2 = BandServiceMessage.Response.DEVICE_TIMEOUT_ERROR;
                    str3 = TAG;
                    str4 = "Result SRAMFWUPDATE_RESET_REASON_SRAM_TIMEOUT received from band";
                    KDKLog.w(str3, str4);
                    return response2;
                }
                return response3;
            }
        } catch (Throwable th) {
            if (isSent && cmd.getCommandId() == BandDeviceConstants.Command.CargoSRAMFWUpdateBootIntoUpdateMode.getCode()) {
                if (!response3.isError()) {
                    cmd.setResultCode(BandServiceMessage.Response.SUCCESS.getCode());
                    KDKLog.w(TAG, "Sent command to device to boot into firmware update mode.");
                } else if (cmd.getResultCode() == BandDeviceConstants.ResultCode.SRAMFWUPDATE_BATTERY_TOO_LOW.getCode()) {
                    BandServiceMessage.Response response5 = BandServiceMessage.Response.DEVICE_BATTERY_LOW_ERROR;
                } else if (cmd.getResultCode() == BandDeviceConstants.ResultCode.SRAMFWUPDATE_RESET_REASON_SRAM_TIMEOUT.getCode()) {
                    BandServiceMessage.Response response6 = BandServiceMessage.Response.DEVICE_TIMEOUT_ERROR;
                    KDKLog.w(TAG, "Result SRAMFWUPDATE_RESET_REASON_SRAM_TIMEOUT received from band");
                }
            }
            throw th;
        }
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection
    protected boolean performHandshake() {
        boolean isValid = false;
        this.mRunningApplication = queryRunningApplication();
        if (this.mRunningApplication != null) {
            synchronized (this.mUpdatingFirmwareFlag) {
                if (DeviceConstants.AppRunning.APP_RUNNING_APP == this.mRunningApplication) {
                    this.mUpdatingFirmwareFlag.set(false);
                    this.mFwVersions = queryFWVersions();
                    isValid = this.mFwVersions != null;
                    if (isValid) {
                        this.mSerialNumber = querySerialNumber();
                        if (VersionCheck.isV2DeviceOrGreater(this.mFwVersions[0].getPcbId())) {
                            if (this.mSerialNumber != null) {
                                this.mDeviceUUID = UUIDHelper.getUUIDFromSerialNumber(this.mSerialNumber);
                            }
                        } else {
                            this.mDeviceUUID = queryDeviceUUID();
                            if (this.mFwVersions[0].getPcbId() != 9) {
                                KDKLog.w(TAG, "PCB id = %s", Byte.valueOf(this.mFwVersions[0].getPcbId()));
                            }
                        }
                        this.mLogVersion = queryLogVersion();
                        if (this.mSerialNumber == null || this.mDeviceUUID == null || this.mLogVersion == 0) {
                            isValid = false;
                        }
                    }
                } else {
                    isValid = this.mUpdatingFirmwareFlag.get() && DeviceConstants.AppRunning.APP_RUNNING_UPAPP == this.mRunningApplication;
                    if (!isValid) {
                        KDKLog.w(TAG, "!!! RESETTING DEVICE !!! -- Required to return it to APP mode.");
                        resetDevice();
                    }
                }
                this.mUpdatingFirmwareFlag.notifyAll();
            }
        }
        if (isValid) {
            KDKLog.i(TAG, "Handshake with %s succeeded.", this);
        } else {
            KDKLog.w(TAG, "Handshake with %s FAILED because device is unresponsive or not in the expected state.", this);
            disconnectDevice(isUpdatingFirmware());
        }
        return isValid;
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection
    protected boolean onDeviceBound() {
        if (isUpdatingFirmware()) {
            KDKLog.d(TAG, "Reconnecting to device for Firmware Upgrade.");
            return true;
        }
        return false;
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection
    protected boolean onDeviceConnected() {
        if (DeviceConstants.AppRunning.APP_RUNNING_APP == getRunningApplication()) {
            KDKLog.i(TAG, "Protocol connection connected");
            getServiceProvider().sendDeviceStatusNotification(false);
        }
        return false;
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection
    protected void onDeviceDisconnected() {
        if (DeviceConstants.AppRunning.APP_RUNNING_APP == getRunningApplication()) {
            KDKLog.i(TAG, "Protocol connection disconnected");
            getServiceProvider().sendDeviceStatusNotification(false);
        }
        this.mRunningApplication = null;
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection
    protected void onDeviceDataReceived(byte[] data, int length) {
        if (!$assertionsDisabled) {
            throw new AssertionError();
        }
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection
    protected void onDeviceError(Exception error) {
        KDKLog.e(TAG, error.getMessage());
    }

    private DeviceConstants.AppRunning queryRunningApplication() {
        DeviceConstants.AppRunning runningApplication = null;
        DeviceCommand cmdWhoAmI = new DeviceCommand(BandDeviceConstants.Command.CargoCoreModuleWhoAmI, (byte[]) null);
        try {
            BandServiceMessage.Response response = performIO(cmdWhoAmI);
            if (response.isError()) {
                return null;
            }
            runningApplication = DeviceConstants.AppRunning.lookup(cmdWhoAmI.getPayload()[0]);
            KDKLog.i(TAG, "Running Application: %s", runningApplication);
            return runningApplication;
        } catch (NullPointerException npe) {
            KDKLog.e(TAG, "NullPointerException encountered. Returning null runningApplication.", npe);
            return runningApplication;
        }
    }

    private FWVersion[] queryFWVersions() {
        GetVersion cmdGetVersion = new GetVersion();
        DeviceCommand cmdGetFWVersions = new DeviceCommand(cmdGetVersion);
        BandServiceMessage.Response response = performIO(cmdGetFWVersions);
        if (response.isError()) {
            return null;
        }
        FWVersion[] fwVersions = cmdGetVersion.getFWVersion();
        if (fwVersions == null || fwVersions.length < DeviceConstants.AppRunning.APP_RUNNING_UPAPP.getFirmwareVersionIndex()) {
            String str = TAG;
            Object[] objArr = new Object[1];
            objArr[0] = Integer.valueOf(fwVersions == null ? 0 : fwVersions.length);
            KDKLog.e(str, "Firmware versions does not contain expected number of entries: %d", objArr);
            return null;
        }
        KDKLog.i(TAG, "Firmware Version: %s", fwVersions[DeviceConstants.AppRunning.APP_RUNNING_APP.getFirmwareVersionIndex()]);
        return fwVersions;
    }

    private UUID queryDeviceUUID() {
        UUID deviceUUID = null;
        DeviceCommand cmdDeviceUUID = new DeviceCommand(BandDeviceConstants.Command.CargoCoreModuleGetUniqueID, (byte[]) null);
        BandServiceMessage.Response response = performIO(cmdDeviceUUID);
        if (!response.isError()) {
            ByteBuffer buffer = ByteBuffer.wrap(cmdDeviceUUID.getPayload()).order(ByteOrder.LITTLE_ENDIAN);
            int structSize = buffer.get() & 255;
            byte structId = buffer.get();
            if (structSize == 66 && structId == 3) {
                long mostSigBits = 0;
                long leastSigBits = 0;
                for (int i = 0; i < 32; i++) {
                    short c = buffer.getShort();
                    if (c >= 48 && c <= 57) {
                        c = (short) (c - 48);
                    } else if (c >= 97 && c <= 102) {
                        c = (short) (c - 87);
                    } else if (c >= 65 && c <= 70) {
                        c = (short) (c - 55);
                    }
                    if (i < 16) {
                        mostSigBits = (mostSigBits << 4) + (c & 15);
                    } else {
                        leastSigBits = (leastSigBits << 4) + (c & 15);
                    }
                }
                deviceUUID = new UUID(mostSigBits, leastSigBits);
                KDKLog.i(TAG, "Device UUID: %s", deviceUUID);
            } else {
                response = BandServiceMessage.Response.DEVICE_DATA_ERROR;
            }
        }
        if (deviceUUID == null) {
            KDKLog.e(TAG, "Failed to get Device Unique ID: %s", response);
        }
        return deviceUUID;
    }

    private String querySerialNumber() {
        GetProductSerialNumber cmd = new GetProductSerialNumber();
        DeviceCommand cmdGetProductSerialNumber = new DeviceCommand(cmd);
        BandServiceMessage.Response response = performIO(cmdGetProductSerialNumber);
        if (response.isError()) {
            return null;
        }
        return cmd.getSerialNumber();
    }

    private int queryLogVersion() {
        if (getRunningApplication() == DeviceConstants.AppRunning.APP_RUNNING_APP) {
            DeviceCommand cmd = new DeviceCommand(BandDeviceConstants.Command.CargoCoreModuleGetLogVersion);
            BandServiceMessage.Response response = performIO(cmd);
            if (response.isError()) {
                return 0;
            }
            ByteBuffer buffer = ByteBuffer.wrap(cmd.getPayload()).order(ByteOrder.LITTLE_ENDIAN);
            int logVersion = BitHelper.unsignedShortToInteger(buffer.getShort());
            KDKLog.i(TAG, "Log Version: %d.%d", Integer.valueOf((65280 & logVersion) >> 8), Integer.valueOf(logVersion & 255));
            return logVersion;
        }
        KDKLog.d(TAG, "%s command not supported while device is not in %s mode.", BandDeviceConstants.Command.CargoCoreModuleGetLogVersion, DeviceConstants.AppRunning.APP_RUNNING_APP);
        return 0;
    }
}

package com.microsoft.band.service.device;

import com.microsoft.band.device.DeviceConstants;
import com.microsoft.band.device.keyboard.KeyboardManager;
import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.sensors.BandConnectionStatus;
import com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnection;
import com.microsoft.band.service.subscription.SubscriptionDataContract;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public class CargoBluetoothPushConnection extends CargoBluetoothConnection implements CargoPushConnection {
    private static final int CALL_DISMISSED_PACKET_TYPE = 101;
    private static final int KEYBOARD_EVENT_PACKET_TYPE = 220;
    private static final int KEYBOARD_SET_CONTEXT_PACKET_TYPE = 222;
    private static final int MAX_CONNECTION_RETRIES = 2;
    private static final int PUSH_PACKET_HEADER_SIZE = 6;
    private static final int PUSH_SERVICE_READ_BUFFER_SIZE = 2048;
    private static final int SMS_DISMISSED_PACKET_TYPE = 100;
    private static final int SUBSCRIPTION_PACKET_TYPE = 1;
    private static final int TILE_EVENT_PACKET_TYPE = 204;
    private final AtomicInteger mConnectionRetries;
    private final ByteBuffer mDataReceived;
    private KeyboardManager mKeyboardManager;
    static final UUID PUSH_PROTOCOL_UUID = UUID.fromString(DeviceConstants.GUID_CARGO_BLUETOOTH_PUSH_PROTOCOL);
    private static final String TAG = CargoBluetoothPushConnection.class.getSimpleName();
    private static final String STREAMING_TAG = TAG + ": " + InternalBandConstants.STREAM_TAG;

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

    public CargoBluetoothPushConnection(DeviceServiceProvider deviceProvider) {
        super(deviceProvider);
        this.mDataReceived = BufferUtil.allocateLittleEndian(2048);
        this.mKeyboardManager = new KeyboardManager(deviceProvider);
        this.mConnectionRetries = new AtomicInteger(0);
    }

    @Override // com.microsoft.band.service.device.DeviceConnection
    public UUID getProtocolUUID() {
        return PUSH_PROTOCOL_UUID;
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection, com.microsoft.band.service.device.DeviceConnection
    public void connect() throws IOException {
        this.mConnectionRetries.set(0);
        super.connect();
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection
    protected boolean performHandshake() {
        return true;
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection
    protected boolean onDeviceBound() {
        return !getServiceProvider().isUpdatingFirmware() && canRetryConnecting();
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection
    protected boolean onDeviceConnected() {
        KDKLog.i(TAG, "Push connection connected");
        this.mDataReceived.clear();
        this.mConnectionRetries.set(0);
        getServiceProvider().updateBandSubscriptions();
        broadcastDeviceStatusChanged();
        return true;
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection
    protected void onDeviceDisconnected() {
        this.mConnectionRetries.set(0);
        KDKLog.i(TAG, "Push connection disconnected");
        broadcastDeviceStatusChanged();
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection
    protected void onDeviceDataReceived(byte[] data, int length) {
        PushServicePayload payload;
        this.mDataReceived.put(data, 0, length);
        ByteBuffer readBuffer = ByteBuffer.wrap(this.mDataReceived.array(), this.mDataReceived.arrayOffset(), this.mDataReceived.position()).order(ByteOrder.LITTLE_ENDIAN);
        while (true) {
            if (readBuffer.remaining() < 6) {
                break;
            }
            readBuffer.mark();
            long timestamp = System.currentTimeMillis();
            int packetTypeId = readBuffer.getShort() & 65535;
            long payloadSize = readBuffer.getInt() & 4294967295L;
            if (packetTypeId == 0 || payloadSize == 0) {
                KDKLog.w(STREAMING_TAG, "Throwing away bad packet header: Packet ID = %04X, length = %d", Integer.valueOf(packetTypeId), Long.valueOf(payloadSize));
            } else if (readBuffer.remaining() >= payloadSize) {
                while (payloadSize > 0 && (payload = parsePushServicePayload(readBuffer, packetTypeId, payloadSize, timestamp)) != null) {
                    payloadSize -= payload.getLength();
                    KDKLog.v(STREAMING_TAG, "%s payload received from device %s", payload.getSensorType(), getServiceProvider().getDeviceInfo().getName());
                    if (payload.getSensorType().getId() < 100) {
                        getServiceProvider().sendSubscription(payload);
                    } else if (payload.getSensorType().getId() == SubscriptionDataContract.SensorType.KeyboardEvent.getId()) {
                        this.mKeyboardManager.processKeyboardEventPushMessage(payload);
                    } else if (payload.getSensorType().getId() == SubscriptionDataContract.SensorType.KeyboardSetContext.getId()) {
                        this.mKeyboardManager.processContextPushMessage(payload);
                    } else {
                        getServiceProvider().sendBroadcast(payload);
                    }
                }
            } else {
                readBuffer.reset();
                break;
            }
        }
        if (readBuffer.position() > 0) {
            this.mDataReceived.clear();
            if (readBuffer.remaining() > 0) {
                this.mDataReceived.put(readBuffer.array(), readBuffer.arrayOffset() + readBuffer.position(), readBuffer.remaining());
            }
        }
    }

    @Override // com.microsoft.band.service.device.CargoBluetoothConnection
    protected void onDeviceError(Exception error) {
        KDKLog.e(TAG, error.getMessage());
    }

    protected boolean canRetryConnecting() {
        int count = this.mConnectionRetries.addAndGet(1);
        if (count > 2) {
            KDKLog.w(TAG, "Failed to connect to Push Service after %d attempts. No more attempts will be made to reconnect until the device is connected via the Command Protocol.", 2);
        } else {
            KDKLog.d(TAG, "Attempting connection to Push Service %d of %d times...", Integer.valueOf(count), 2);
        }
        return count <= 2;
    }

    protected PushServicePayload parsePushServicePayload(ByteBuffer readBuffer, int packetTypeId, long payloadSize, long timestamp) {
        if (packetTypeId == 1) {
            PushServicePayload payload = PushServicePayload.create(readBuffer, timestamp);
            return payload;
        } else if (packetTypeId == 100) {
            PushServicePayload payload2 = PushServicePayload.create(readBuffer, SubscriptionDataContract.SensorType.SmsDismissed, 0, (int) payloadSize, timestamp);
            return payload2;
        } else if (packetTypeId == 101) {
            PushServicePayload payload3 = PushServicePayload.create(readBuffer, SubscriptionDataContract.SensorType.CallDismissed, 0, (int) payloadSize, timestamp);
            return payload3;
        } else if (packetTypeId == TILE_EVENT_PACKET_TYPE) {
            PushServicePayload payload4 = PushServicePayload.create(readBuffer, SubscriptionDataContract.SensorType.TileEvent, 0, (int) payloadSize, timestamp);
            return payload4;
        } else if (packetTypeId == KEYBOARD_EVENT_PACKET_TYPE) {
            PushServicePayload payload5 = PushServicePayload.create(readBuffer, SubscriptionDataContract.SensorType.KeyboardEvent, 0, (int) payloadSize, timestamp);
            return payload5;
        } else if (packetTypeId == KEYBOARD_SET_CONTEXT_PACKET_TYPE) {
            PushServicePayload payload6 = PushServicePayload.create(readBuffer, SubscriptionDataContract.SensorType.KeyboardSetContext, 0, (int) payloadSize, timestamp);
            return payload6;
        } else {
            KDKLog.w(TAG, "Ignoring unsupported packet [%s]: type = %04X, length = %d", BufferUtil.toHexString(readBuffer.array(), readBuffer.arrayOffset() + readBuffer.position(), (int) payloadSize), Integer.valueOf(packetTypeId), Long.valueOf(payloadSize));
            readBuffer.position(readBuffer.position() + ((int) payloadSize));
            return null;
        }
    }

    private void broadcastDeviceStatusChanged() {
        if (getServiceProvider().hasSubscribersForSensor(SubscriptionDataContract.SensorType.DeviceStatus)) {
            DeviceStatusPushPayload _deviceStatusPayload = new DeviceStatusPushPayload();
            _deviceStatusPayload.setDeviceState(isConnected() ? BandConnectionStatus.CONNECTED : BandConnectionStatus.DISCONNECTED);
            getServiceProvider().sendSubscription(_deviceStatusPayload);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class DeviceStatusPushPayload extends PushServicePayload {
        public DeviceStatusPushPayload() {
            super(SubscriptionDataContract.SensorType.DeviceStatus, 0, System.currentTimeMillis(), new byte[4], 0, 4);
        }

        public void setDeviceState(BandConnectionStatus deviceState) {
            ByteBuffer buffer = getDataBuffer();
            buffer.putInt(deviceState.ordinal());
        }
    }
}

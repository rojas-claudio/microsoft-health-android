package com.microsoft.band.service.device;

import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class CargoBluetoothConnection implements DeviceConnection, BluetoothDeviceConnection.BluetoothDeviceConnectionListener {
    protected static final int CONNECTION_TIME_OUT = 5000;
    protected static final int FIRMWARE_UPDATE_WAIT_INTERVAL = 10000;
    protected static final int READ_TIMEOUT = 30000;
    private static final int RECONNECT_RETRY_LIMIT = 5;
    private static final int RETRY_CONNECTION_TIME_OUT = 10000;
    private static final String TAG = CargoBluetoothConnection.class.getSimpleName();
    private volatile BluetoothDeviceConnection mBluetoothConnection;
    protected final AtomicBoolean mConnectedFlag;
    protected final AtomicBoolean mConnectingFlag;
    protected final AtomicBoolean mDisposedFlag;
    private final DeviceServiceProvider mServiceProvider;

    protected abstract boolean onDeviceBound();

    protected abstract boolean onDeviceConnected();

    protected abstract void onDeviceDataReceived(byte[] bArr, int i);

    protected abstract void onDeviceDisconnected();

    protected abstract void onDeviceError(Exception exc);

    protected abstract boolean performHandshake();

    public CargoBluetoothConnection(DeviceServiceProvider deviceProvider) {
        if (deviceProvider == null) {
            throw new IllegalArgumentException("deviceProvider not specified");
        }
        this.mServiceProvider = deviceProvider;
        this.mConnectedFlag = new AtomicBoolean(false);
        this.mConnectingFlag = new AtomicBoolean(false);
        this.mDisposedFlag = new AtomicBoolean(false);
    }

    @Override // com.microsoft.band.service.device.DeviceConnection
    public boolean isConnected() {
        return this.mConnectedFlag.get() && this.mBluetoothConnection != null && this.mBluetoothConnection.isConnected();
    }

    @Override // com.microsoft.band.service.device.DeviceConnection
    public boolean isDisposed() {
        return this.mDisposedFlag.get();
    }

    @Override // com.microsoft.band.service.device.DeviceConnection
    public String getDeviceName() {
        BluetoothDeviceConnection deviceConnection = this.mBluetoothConnection;
        return deviceConnection == null ? "" : deviceConnection.getDeviceName();
    }

    @Override // com.microsoft.band.service.device.DeviceConnection
    public String getDeviceAddress() {
        BluetoothDeviceConnection deviceConnection = this.mBluetoothConnection;
        return deviceConnection == null ? "" : deviceConnection.getDeviceAddress();
    }

    @Override // com.microsoft.band.service.device.DeviceConnection
    public DeviceInfo getDeviceInfo() {
        return getServiceProvider().getDeviceInfo();
    }

    @Override // com.microsoft.band.service.device.DeviceConnection
    public DeviceServiceProvider getServiceProvider() {
        return this.mServiceProvider;
    }

    @Override // com.microsoft.band.service.device.DeviceConnection
    public void waitForDeviceToDisconnect() {
        synchronized (this.mConnectedFlag) {
            if (isConnected()) {
                try {
                    this.mConnectedFlag.wait();
                } catch (InterruptedException e) {
                    KDKLog.e(TAG, "Interrupted while waiting for device to disconnect.");
                }
            }
            this.mConnectedFlag.notifyAll();
        }
    }

    @Override // com.microsoft.band.service.device.DeviceConnection
    public void dispose() {
        this.mDisposedFlag.set(true);
        disconnect();
        this.mBluetoothConnection = null;
        KDKLog.d(TAG, "Disposed.");
    }

    @Override // com.microsoft.band.service.device.DeviceConnection
    public void connect() throws IOException {
        connect(false);
    }

    @Override // com.microsoft.band.service.device.DeviceConnection
    public void connect(boolean waitForConnection) throws IOException {
        if (!isDisposed()) {
            synchronized (this.mConnectedFlag) {
                if (!isConnected() && this.mBluetoothConnection != null) {
                    this.mConnectingFlag.set(this.mBluetoothConnection.isConnected() ? false : true);
                    if (this.mConnectingFlag.get()) {
                        this.mBluetoothConnection.connect(getProtocolUUID());
                        if (waitForConnection) {
                            try {
                                this.mConnectedFlag.wait(5000L);
                                this.mConnectingFlag.get();
                                this.mConnectingFlag.set(false);
                            } catch (InterruptedException e) {
                                KDKLog.e(TAG, "Interrupted with waiting for device to connect: " + e.getMessage());
                                this.mConnectingFlag.get();
                                this.mConnectingFlag.set(false);
                            }
                        }
                    } else {
                        this.mConnectedFlag.set(true);
                    }
                    if (this.mConnectedFlag.get() || waitForConnection) {
                        this.mConnectedFlag.notify();
                    }
                }
            }
        }
    }

    @Override // com.microsoft.band.service.device.DeviceConnection
    public void disconnect() {
        disconnectDevice(false);
    }

    @Override // com.microsoft.band.service.device.DeviceConnection
    public void reset() {
        disconnectDevice(true);
    }

    public String toString() {
        return String.format(Locale.getDefault(), "Bluetooth Device: %s (%s)", getDeviceName(), getDeviceAddress());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void reconnectDevice() {
        BluetoothDeviceConnection bluetoothConnection = this.mBluetoothConnection;
        if (!isDisposed() && bluetoothConnection != null && bluetoothConnection.isIdle()) {
            bluetoothConnection.reconnect();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public InputStream getInputStream() {
        BluetoothDeviceConnection bluetoothConnection = this.mBluetoothConnection;
        if (bluetoothConnection == null) {
            return null;
        }
        return bluetoothConnection.getInputStream();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public OutputStream getOutputStream() {
        BluetoothDeviceConnection bluetoothConnection = this.mBluetoothConnection;
        if (bluetoothConnection == null) {
            return null;
        }
        return bluetoothConnection.getOutputStream();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void disconnectDevice(boolean reconnect) {
        synchronized (this.mConnectedFlag) {
            this.mConnectedFlag.set(false);
            this.mConnectingFlag.set(false);
            BluetoothDeviceConnection bluetoothConnection = this.mBluetoothConnection;
            if (bluetoothConnection != null && !bluetoothConnection.isIdle()) {
                bluetoothConnection.disconnect(false);
            }
            if (reconnect && !isDisposed() && bluetoothConnection != null) {
                bluetoothConnection.reconnect();
                int retries = 0;
                while (true) {
                    if (retries > 0) {
                        KDKLog.d(TAG, "Waiting for device to reconnect %d of %d times.", Integer.valueOf(retries), 5);
                    }
                    try {
                        this.mConnectedFlag.wait(10000L);
                    } catch (InterruptedException e) {
                        KDKLog.e(TAG, "Interrupted while waiting for device to reconnect.");
                    }
                    if (isConnected()) {
                        break;
                    }
                    int retries2 = retries + 1;
                    if (retries >= 5) {
                        break;
                    }
                    retries = retries2;
                }
            }
            this.mConnectedFlag.notifyAll();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void readWithTimeout(InputStream in, byte[] outBytes, int offset, int bytesToRead) throws IOException, TimeoutException {
        int readOffset = offset;
        int remaining = bytesToRead;
        long duration = 0;
        while (remaining > 0) {
            long readStartTime = System.currentTimeMillis();
            int bytesRead = in.read(outBytes, readOffset, remaining);
            long readDuration = System.currentTimeMillis() - readStartTime;
            remaining -= bytesRead;
            readOffset += bytesRead;
            duration += readDuration;
            if (remaining > 0 && duration >= 30000) {
                throw new TimeoutException(String.format("Read a total of %d/%d bytes, but timed out after %d ms.", Integer.valueOf(bytesToRead - remaining), Integer.valueOf(bytesToRead), Long.valueOf(duration)));
            }
        }
    }

    @Override // com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnection.BluetoothDeviceConnectionListener
    public boolean onBluetoothDeviceBound(BluetoothDeviceConnection connection) {
        boolean onDeviceBound;
        KDKLog.i(getClass().getSimpleName(), "Bound to " + connection);
        synchronized (this.mConnectedFlag) {
            setBluetoothConnection(connection);
            onDeviceBound = onDeviceBound();
        }
        return onDeviceBound;
    }

    @Override // com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnection.BluetoothDeviceConnectionListener
    public boolean onBluetoothDeviceConnected(BluetoothDeviceConnection connection) {
        boolean startReaderThread = false;
        synchronized (this.mConnectedFlag) {
            setBluetoothConnection(connection);
            this.mConnectingFlag.set(false);
            if (performHandshake()) {
                this.mConnectedFlag.set(true);
                startReaderThread = onDeviceConnected();
            }
            this.mConnectedFlag.notifyAll();
        }
        return startReaderThread;
    }

    @Override // com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnection.BluetoothDeviceConnectionListener
    public void onBluetoothDeviceDisconnected(BluetoothDeviceConnection connection) {
        synchronized (this.mConnectedFlag) {
            this.mConnectingFlag.set(false);
            this.mConnectedFlag.set(false);
            onDeviceDisconnected();
            this.mConnectedFlag.notifyAll();
        }
    }

    @Override // com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnection.BluetoothDeviceConnectionListener
    public void onBluetoothDeviceError(BluetoothDeviceConnection connection, Exception socketException) {
        synchronized (this.mConnectedFlag) {
            this.mConnectingFlag.set(false);
            this.mConnectedFlag.set(connection.isConnected());
            onDeviceError(socketException);
            this.mConnectedFlag.notifyAll();
        }
    }

    @Override // com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnection.BluetoothDeviceConnectionListener
    public void onBluetoothDeviceDataReceived(BluetoothDeviceConnection connection, byte[] buffer, int length) {
        synchronized (this.mConnectedFlag) {
            onDeviceDataReceived(buffer, length);
        }
    }

    private void setBluetoothConnection(BluetoothDeviceConnection connection) {
        if (connection != this.mBluetoothConnection) {
            if (this.mBluetoothConnection != null) {
                this.mBluetoothConnection.disconnect();
            }
            this.mBluetoothConnection = connection;
        }
    }
}

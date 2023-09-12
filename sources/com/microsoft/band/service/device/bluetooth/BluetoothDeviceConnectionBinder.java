package com.microsoft.band.service.device.bluetooth;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.ParcelUuid;
import com.microsoft.band.internal.EventHandlerThread;
import com.microsoft.band.internal.util.BluetoothAdapterHelper;
import com.microsoft.band.service.device.bluetooth.BluetoothDeviceConnection;
import java.util.UUID;
/* loaded from: classes.dex */
final class BluetoothDeviceConnectionBinder {
    private final String mBluetoothAddress;
    private final int mDeviceClass;
    private volatile BluetoothDeviceConnection mDeviceConnection;
    private final int mDeviceMajorClass;
    private final EventHandlerThread mEventHandlerThread;
    private final BluetoothDeviceConnection.BluetoothDeviceConnectionListener mListener;
    private final int mServiceClass;
    private final UUID[] mUuids;

    /* JADX INFO: Access modifiers changed from: protected */
    public BluetoothDeviceConnectionBinder(EventHandlerThread eventHandlerThread, BluetoothDeviceConnection.BluetoothDeviceConnectionListener listener, String bluetoothAddress, UUID serviceUUID) {
        if (bluetoothAddress == null) {
            throw new NullPointerException("bluetoothAddress");
        }
        this.mEventHandlerThread = eventHandlerThread;
        this.mListener = listener;
        this.mDeviceMajorClass = 0;
        this.mDeviceClass = 0;
        this.mServiceClass = 0;
        if (serviceUUID == null) {
            this.mUuids = null;
        } else {
            this.mUuids = new UUID[]{serviceUUID};
        }
        this.mBluetoothAddress = bluetoothAddress;
    }

    public synchronized BluetoothDeviceConnection getConnection() {
        return this.mDeviceConnection;
    }

    public synchronized boolean isConnectionIdle() {
        boolean z;
        if (isBound()) {
            z = this.mDeviceConnection.isIdle();
        }
        return z;
    }

    public synchronized boolean isConnecting() {
        boolean z;
        if (isBound()) {
            z = this.mDeviceConnection.isConnecting();
        }
        return z;
    }

    public synchronized boolean isBound() {
        return this.mDeviceConnection != null;
    }

    public boolean bindAndConnect(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice == null) {
            throw new NullPointerException("bluetoothDevice argument is null");
        }
        if (!isBound() || isConnectionIdle()) {
            return bind(bluetoothDevice);
        }
        return false;
    }

    public boolean isConnected() {
        return isBound() && !isConnectionIdle();
    }

    public synchronized void disconnect() {
        if (isBound()) {
            this.mDeviceConnection.disconnect();
        }
    }

    public BluetoothDeviceConnection.BluetoothDeviceConnectionListener getListener() {
        return this.mListener;
    }

    public String getBluetoothAddress() {
        return this.mBluetoothAddress;
    }

    public UUID getPrimaryUuid() {
        if (this.mUuids == null || this.mUuids.length == 0) {
            UUID uuid = BluetoothAdapterHelper.SERIAL_PORT;
            return uuid;
        }
        UUID uuid2 = this.mUuids[0];
        return uuid2;
    }

    public synchronized void unbind() {
        disconnect();
        this.mDeviceConnection = null;
    }

    public synchronized boolean isBoundTo(BluetoothDevice bluetoothDevice) {
        boolean bound;
        bound = false;
        if (bluetoothDevice != null) {
            if (isBound()) {
                bound = this.mDeviceConnection.getDevice().getAddress().equals(bluetoothDevice.getAddress());
            }
        }
        return bound;
    }

    public synchronized boolean isBoundTo(BluetoothDeviceConnection bluetoothConnection) {
        boolean bound;
        bound = false;
        if (bluetoothConnection != null) {
            if (this.mDeviceConnection == null) {
                bound = this.mListener == bluetoothConnection.getListener();
            } else {
                bound = this.mDeviceConnection == bluetoothConnection;
            }
        }
        return bound;
    }

    public boolean isSupportedDevice(BluetoothDevice bluetoothDevice) {
        return bluetoothDevice != null && (this.mBluetoothAddress != null ? this.mBluetoothAddress.equals(bluetoothDevice.getAddress()) : isMatch(bluetoothDevice.getBluetoothClass()) && isMatch(BluetoothAdapterHelper.getDeviceUuids(bluetoothDevice)));
    }

    public boolean equals(Object obj) {
        boolean isEqual = obj == this;
        if (!isEqual && (obj instanceof BluetoothDeviceConnectionBinder)) {
            BluetoothDeviceConnectionBinder other = (BluetoothDeviceConnectionBinder) obj;
            return other.getListener() == this.mListener;
        }
        return isEqual;
    }

    public int hashCode() {
        return this.mListener.hashCode();
    }

    protected boolean isMatch(ParcelUuid[] parcelUuids) {
        boolean isMatch = this.mUuids == null || this.mUuids.length == 0;
        if (!isMatch && parcelUuids != null && parcelUuids.length >= this.mUuids.length) {
            for (int parcelIndex = 0; !isMatch && parcelIndex < parcelUuids.length; parcelIndex++) {
                for (int index = 0; !isMatch && index < this.mUuids.length; index++) {
                    isMatch = this.mUuids[index].equals(parcelUuids[parcelIndex].getUuid());
                }
            }
        }
        return isMatch;
    }

    protected synchronized boolean bind(BluetoothDevice bluetoothDevice) {
        boolean isMatch;
        isMatch = isSupportedDevice(bluetoothDevice);
        if (isMatch) {
            if (isConnectionIdle() && !isBoundTo(bluetoothDevice)) {
                unbind();
            }
            if (!isBound()) {
                this.mDeviceConnection = new BluetoothDeviceConnection(getEventHandler(), bluetoothDevice, getPrimaryUuid(), this.mListener);
            } else {
                this.mDeviceConnection.connect();
            }
        }
        return isMatch;
    }

    protected boolean isMatch(BluetoothClass bluetoothClass) {
        boolean isMatch = this.mDeviceMajorClass == 0 || this.mDeviceMajorClass == bluetoothClass.getMajorDeviceClass();
        if (isMatch) {
            boolean isMatch2 = this.mDeviceClass == 0 || this.mDeviceClass == bluetoothClass.getDeviceClass();
            if (isMatch2) {
                return this.mServiceClass == 0 || bluetoothClass.hasService(this.mServiceClass);
            }
            return isMatch2;
        }
        return isMatch;
    }

    protected Handler getEventHandler() {
        return this.mEventHandlerThread.getHandler();
    }
}

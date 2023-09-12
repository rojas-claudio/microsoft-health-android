package com.microsoft.band.internal.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.ParcelUuid;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
/* loaded from: classes.dex */
public abstract class BluetoothAdapterHelper {
    private static final String TAG = BluetoothAdapterHelper.class.getSimpleName();
    public static final UUID SERIAL_PORT = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static final Intent ENABLE_BLUETOOTH_INTENT = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");

    static {
        ENABLE_BLUETOOTH_INTENT.addFlags(67108864);
    }

    public static boolean isBluetoothAdapterAvailable() {
        return BluetoothAdapter.getDefaultAdapter() != null;
    }

    public static boolean isBluetoothEnabled() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public static boolean startDiscovery() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            return false;
        }
        try {
            boolean success = bluetoothAdapter.startDiscovery();
            return success;
        } catch (Exception e) {
            KDKLog.i(TAG, e.getMessage());
            return false;
        }
    }

    public static boolean cancelDiscovery() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            return false;
        }
        try {
            boolean success = bluetoothAdapter.cancelDiscovery();
            return success;
        } catch (Exception e) {
            KDKLog.i(TAG, e.getMessage());
            return false;
        }
    }

    public static boolean isDiscovering() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            return false;
        }
        try {
            boolean discovering = bluetoothAdapter.isDiscovering();
            return discovering;
        } catch (Exception e) {
            KDKLog.i(TAG, e.getMessage());
            return false;
        }
    }

    public static void enableBluetooth(Context context) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            if (context == null) {
                throw new NullPointerException("Context cannot be null");
            }
            context.startActivity(ENABLE_BLUETOOTH_INTENT);
        }
    }

    public static void enableBluetooth(Activity activity, int requestCode) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            if (activity == null) {
                throw new NullPointerException("Activity cannot be null");
            }
            activity.startActivityForResult(ENABLE_BLUETOOTH_INTENT, requestCode);
        }
    }

    public static Set<BluetoothDevice> getPairedDevices() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return null;
        }
        try {
            Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
            return devices;
        } catch (Exception e) {
            Set<BluetoothDevice> devices2 = new HashSet<>();
            KDKLog.i(TAG, e.getMessage());
            return devices2;
        }
    }

    public static Set<BluetoothDevice> getPairedDevicesWithPossibleException() {
        Set<BluetoothDevice> devices = null;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            devices = bluetoothAdapter.getBondedDevices();
        }
        if (devices == null) {
            Set<BluetoothDevice> devices2 = new HashSet<>();
            return devices2;
        }
        return devices;
    }

    public static ParcelUuid[] getDeviceUuids(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice == null) {
            throw new IllegalArgumentException("BluetoothDevice cannot be null");
        }
        ParcelUuid[] parcelUuids = bluetoothDevice.getUuids();
        if (parcelUuids == null) {
            KDKLog.i(TAG, "No UUIDs for '" + bluetoothDevice.getName() + "' using SerialPort as fallback.");
            return new ParcelUuid[]{new ParcelUuid(SERIAL_PORT)};
        }
        return parcelUuids;
    }

    public static boolean isSupportedUuid(BluetoothDevice bluetoothDevice, ParcelUuid parcelUuid) {
        if (parcelUuid == null) {
            return false;
        }
        ParcelUuid[] parcelUuids = getDeviceUuids(bluetoothDevice);
        for (ParcelUuid uuid : parcelUuids) {
            if (uuid.equals(parcelUuid)) {
                return true;
            }
        }
        return false;
    }

    public static BluetoothSocket createSocket(BluetoothDevice bluetoothDevice, UUID serviceUUID) throws IOException {
        if (serviceUUID == null) {
            throw new IllegalArgumentException("Service UUID cannot be null");
        }
        if (bluetoothDevice == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= 17) {
            BluetoothSocket socket = bluetoothDevice.createRfcommSocketToServiceRecord(serviceUUID);
            return socket;
        }
        BluetoothSocket socket2 = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(serviceUUID);
        return socket2;
    }
}

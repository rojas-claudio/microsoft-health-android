package com.microsoft.band;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import android.support.v4.content.LocalBroadcastManager;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.band.internal.util.BluetoothAdapterHelper;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.service.device.CargoBluetoothProtocolConnection;
import com.microsoft.band.service.logger.LoggerEvent;
import com.microsoft.band.service.logger.LoggerFactory;
import com.microsoft.band.util.StreamUtils;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public final class CargoKit {
    private static volatile BroadcastReceiver broadcastReceiver;
    public static final ParcelUuid PARCEL_UUID_CARGO_BLUETOOTH_PROTOCOL = ParcelUuid.fromString(BandDeviceConstants.GUID_CARGO_BLUETOOTH_PROTOCOL);
    private static final Object LOCK = new Object();

    /* loaded from: classes.dex */
    public interface LoggerListener {
        void onLogEvent(LoggerEvent loggerEvent);
    }

    private CargoKit() throws IllegalAccessException {
        throw new IllegalAccessException("You can't create instance of " + CargoKit.class.getSimpleName());
    }

    public static boolean isBluetoothAvailable() {
        return BluetoothAdapterHelper.isBluetoothAdapterAvailable();
    }

    public static boolean isBluetoothEnabled() {
        return BluetoothAdapterHelper.isBluetoothEnabled();
    }

    public static boolean isCargoDevice(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice == null) {
            return false;
        }
        boolean isDeviceBound = bluetoothDevice.getBondState() == 12;
        if (isDeviceBound) {
            boolean isCargoDevice = BluetoothAdapterHelper.isSupportedUuid(bluetoothDevice, PARCEL_UUID_CARGO_BLUETOOTH_PROTOCOL);
            return isCargoDevice;
        }
        BluetoothClass bluetoothClass = bluetoothDevice.getBluetoothClass();
        return bluetoothClass != null && bluetoothClass.getDeviceClass() == 1796 && bluetoothClass.hasService(2097152);
    }

    public static void pairWithBand(BluetoothDevice bluetoothDevice) {
        BluetoothSocket socket = null;
        try {
            KDKLog.w(CargoConstants.DISCOVERY_TAG, "Attempting to pair with possible Cargo Device: Name = %s, MAC = %s", bluetoothDevice.getName(), bluetoothDevice.getAddress());
            socket = BluetoothAdapterHelper.createSocket(bluetoothDevice, CargoBluetoothProtocolConnection.NORMAL_PROTOCOL_UUID);
            socket.connect();
        } catch (Exception e) {
            KDKLog.w(CargoConstants.DISCOVERY_TAG, "Failed pairing attempt with possible Cargo Device: " + e.getMessage());
        }
        if (socket != null) {
            StreamUtils.closeQuietly(socket);
        }
    }

    public static List<DeviceInfo> getPairedDevices() {
        List<DeviceInfo> devices = new ArrayList<>();
        if (isBluetoothAvailable()) {
            for (BluetoothDevice bluetoothDevice : BluetoothAdapterHelper.getPairedDevices()) {
                if (BluetoothAdapterHelper.isSupportedUuid(bluetoothDevice, PARCEL_UUID_CARGO_BLUETOOTH_PROTOCOL)) {
                    devices.add(new DeviceInfo(bluetoothDevice.getName(), bluetoothDevice.getAddress()));
                }
            }
        }
        return devices;
    }

    public static void registerLogListener(Context context, final LoggerListener listener) {
        if (context == null || listener == null) {
            throw new NullPointerException("context or log listener are null");
        }
        if (broadcastReceiver == null) {
            synchronized (LOCK) {
                if (broadcastReceiver == null) {
                    BroadcastReceiver receiver = new BroadcastReceiver() { // from class: com.microsoft.band.CargoKit.1
                        @Override // android.content.BroadcastReceiver
                        public void onReceive(Context c, Intent intent) {
                            if (LoggerFactory.ACTION_TELEMETRY_MESSAGE.equals(intent.getAction())) {
                                LoggerListener.this.onLogEvent((LoggerEvent) intent.getParcelableExtra("event"));
                            }
                        }
                    };
                    IntentFilter filter = new IntentFilter(LoggerFactory.ACTION_TELEMETRY_MESSAGE);
                    LocalBroadcastManager.getInstance(context).registerReceiver(receiver, filter);
                    broadcastReceiver = receiver;
                }
            }
        }
    }

    public static void unregisterLogListener(Context context) {
        if (broadcastReceiver != null && context != null) {
            synchronized (LOCK) {
                if (broadcastReceiver != null) {
                    LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
                    broadcastReceiver = null;
                }
            }
        }
    }

    public static boolean isLogListenerRegistered() {
        return broadcastReceiver != null;
    }
}

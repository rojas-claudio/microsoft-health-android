package com.microsoft.band;

import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.band.internal.util.BluetoothAdapterHelper;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
final class BluetoothUtils {
    private static final ParcelUuid PARCEL_UUID_CARGO_BLUETOOTH_PROTOCOL = ParcelUuid.fromString(BandDeviceConstants.GUID_CARGO_BLUETOOTH_PROTOCOL);

    private BluetoothUtils() {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BandInfo[] getPairedBands() {
        List<DeviceInfo> devices = new ArrayList<>();
        if (BluetoothAdapterHelper.isBluetoothAdapterAvailable()) {
            for (BluetoothDevice bluetoothDevice : BluetoothAdapterHelper.getPairedDevicesWithPossibleException()) {
                if (BluetoothAdapterHelper.isSupportedUuid(bluetoothDevice, PARCEL_UUID_CARGO_BLUETOOTH_PROTOCOL)) {
                    devices.add(new DeviceInfo(bluetoothDevice.getName(), bluetoothDevice.getAddress()));
                }
            }
        }
        BandInfo[] pairedBands = new BandInfo[devices.size()];
        for (int i = 0; i < devices.size(); i++) {
            DeviceInfo deviceInfo = devices.get(i);
            pairedBands[i] = new BandInfoImpl(deviceInfo);
        }
        return pairedBands;
    }
}

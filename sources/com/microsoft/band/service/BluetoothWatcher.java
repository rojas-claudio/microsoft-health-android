package com.microsoft.band.service;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.microsoft.band.CargoKit;
import com.microsoft.band.internal.util.KDKLog;
/* loaded from: classes.dex */
public class BluetoothWatcher extends BroadcastReceiver {
    private static final String TAG = BluetoothWatcher.class.getSimpleName();

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if ("android.bluetooth.device.action.ACL_CONNECTED".equals(intent.getAction())) {
            BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            KDKLog.d(TAG, "ACL Connect Received For Device %s", bluetoothDevice);
            if (CargoKit.isCargoDevice(bluetoothDevice)) {
                Intent startServiceIntent = new Intent(context, BandService.class);
                context.startService(startServiceIntent);
            }
        }
    }
}

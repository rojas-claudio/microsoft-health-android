package com.microsoft.band;

import android.content.Context;
import com.microsoft.band.internal.util.KDKLog;
/* loaded from: classes.dex */
public final class BandClientManager {
    private static final String TAG = BandClientManager.class.getSimpleName();
    private static final BandClientManager INSTANCE = new BandClientManager();

    public static BandClientManager getInstance() {
        return INSTANCE;
    }

    private BandClientManager() {
    }

    public BandInfo[] getPairedBands() {
        return BluetoothUtils.getPairedBands();
    }

    public BandClient create(Context context, BandInfo device) {
        KDKLog.i(TAG, "BandClient create with deviceName=%s deviceAddress=%s", device.getName(), device.getMacAddress());
        return new BandClientImpl(context, device);
    }
}

package com.google.android.gms.ads.mediation.admob;

import android.os.Bundle;
import com.google.ads.mediation.NetworkExtras;
/* loaded from: classes.dex */
public final class AdMobExtras implements NetworkExtras {
    private final Bundle im;

    public AdMobExtras(Bundle extras) {
        this.im = extras != null ? new Bundle(extras) : null;
    }

    public Bundle getExtras() {
        return this.im;
    }
}

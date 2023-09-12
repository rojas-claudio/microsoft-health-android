package com.google.android.gms.internal;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.internal.ab;
/* loaded from: classes.dex */
public final class t extends ab.a {
    private final AdListener dT;

    public t(AdListener adListener) {
        this.dT = adListener;
    }

    @Override // com.google.android.gms.internal.ab
    public void onAdClosed() {
        this.dT.onAdClosed();
    }

    @Override // com.google.android.gms.internal.ab
    public void onAdFailedToLoad(int errorCode) {
        this.dT.onAdFailedToLoad(errorCode);
    }

    @Override // com.google.android.gms.internal.ab
    public void onAdLeftApplication() {
        this.dT.onAdLeftApplication();
    }

    @Override // com.google.android.gms.internal.ab
    public void onAdLoaded() {
        this.dT.onAdLoaded();
    }

    @Override // com.google.android.gms.internal.ab
    public void onAdOpened() {
        this.dT.onAdOpened();
    }
}

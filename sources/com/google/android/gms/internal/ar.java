package com.google.android.gms.internal;

import com.google.android.gms.internal.at;
import com.google.android.gms.internal.ay;
/* loaded from: classes.dex */
public final class ar extends ay.a {
    private final Object eJ = new Object();
    private at.a fb;
    private aq fc;

    public void a(aq aqVar) {
        synchronized (this.eJ) {
            this.fc = aqVar;
        }
    }

    public void a(at.a aVar) {
        synchronized (this.eJ) {
            this.fb = aVar;
        }
    }

    @Override // com.google.android.gms.internal.ay
    public void onAdClosed() {
        synchronized (this.eJ) {
            if (this.fc != null) {
                this.fc.E();
            }
        }
    }

    @Override // com.google.android.gms.internal.ay
    public void onAdFailedToLoad(int error) {
        synchronized (this.eJ) {
            if (this.fb != null) {
                this.fb.d(error == 3 ? 1 : 2);
                this.fb = null;
            }
        }
    }

    @Override // com.google.android.gms.internal.ay
    public void onAdLeftApplication() {
        synchronized (this.eJ) {
            if (this.fc != null) {
                this.fc.F();
            }
        }
    }

    @Override // com.google.android.gms.internal.ay
    public void onAdLoaded() {
        synchronized (this.eJ) {
            if (this.fb != null) {
                this.fb.d(0);
                this.fb = null;
                return;
            }
            if (this.fc != null) {
                this.fc.H();
            }
        }
    }

    @Override // com.google.android.gms.internal.ay
    public void onAdOpened() {
        synchronized (this.eJ) {
            if (this.fc != null) {
                this.fc.G();
            }
        }
    }

    @Override // com.google.android.gms.internal.ay
    public void y() {
        synchronized (this.eJ) {
            if (this.fc != null) {
                this.fc.D();
            }
        }
    }
}

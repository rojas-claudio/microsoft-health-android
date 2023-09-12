package com.google.android.gms.ads;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.internal.ac;
import com.google.android.gms.internal.av;
import com.google.android.gms.internal.cn;
import com.google.android.gms.internal.t;
import com.google.android.gms.internal.u;
import com.google.android.gms.internal.v;
import com.google.android.gms.internal.x;
import com.google.android.gms.internal.z;
/* loaded from: classes.dex */
public final class InterstitialAd {
    private final av dS = new av();
    private AdListener dT;
    private ac dU;
    private String dV;
    private a dW;
    private final Context mContext;

    public InterstitialAd(Context context) {
        this.mContext = context;
    }

    private void c(String str) throws RemoteException {
        if (this.dV == null) {
            d(str);
        }
        this.dU = u.a(this.mContext, new x(), this.dV, this.dS);
        if (this.dT != null) {
            this.dU.a(new t(this.dT));
        }
        if (this.dW != null) {
            this.dU.a(new z(this.dW));
        }
    }

    private void d(String str) {
        if (this.dU == null) {
            throw new IllegalStateException("The ad unit ID must be set on InterstitialAd before " + str + " is called.");
        }
    }

    public AdListener getAdListener() {
        return this.dT;
    }

    public String getAdUnitId() {
        return this.dV;
    }

    public boolean isLoaded() {
        try {
            if (this.dU == null) {
                return false;
            }
            return this.dU.isReady();
        } catch (RemoteException e) {
            cn.b("Failed to check if ad is ready.", e);
            return false;
        }
    }

    public void loadAd(AdRequest adRequest) {
        try {
            if (this.dU == null) {
                c("loadAd");
            }
            if (this.dU.a(new v(this.mContext, adRequest))) {
                this.dS.c(adRequest.v());
            }
        } catch (RemoteException e) {
            cn.b("Failed to load ad.", e);
        }
    }

    public void setAdListener(AdListener adListener) {
        try {
            this.dT = adListener;
            if (this.dU != null) {
                this.dU.a(adListener != null ? new t(adListener) : null);
            }
        } catch (RemoteException e) {
            cn.b("Failed to set the AdListener.", e);
        }
    }

    public void setAdUnitId(String adUnitId) {
        if (this.dV != null) {
            throw new IllegalStateException("The ad unit ID can only be set once on InterstitialAd.");
        }
        this.dV = adUnitId;
    }

    public void show() {
        try {
            d("show");
            this.dU.showInterstitial();
        } catch (RemoteException e) {
            cn.b("Failed to show interstitial.", e);
        }
    }
}

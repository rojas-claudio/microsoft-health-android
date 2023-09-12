package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.ads.AdRequest;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationBannerListener;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationInterstitialListener;
import com.google.ads.mediation.MediationServerParameters;
import com.google.ads.mediation.NetworkExtras;
/* loaded from: classes.dex */
public final class ba<NETWORK_EXTRAS extends NetworkExtras, SERVER_PARAMETERS extends MediationServerParameters> implements MediationBannerListener, MediationInterstitialListener {
    private final ay ft;

    public ba(ay ayVar) {
        this.ft = ayVar;
    }

    @Override // com.google.ads.mediation.MediationBannerListener
    public void onClick(MediationBannerAdapter<?, ?> adapter) {
        cn.m("Adapter called onClick.");
        if (!cm.ar()) {
            cn.q("onClick must be called on the main UI thread.");
            cm.hO.post(new Runnable() { // from class: com.google.android.gms.internal.ba.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        ba.this.ft.y();
                    } catch (RemoteException e) {
                        cn.b("Could not call onAdClicked.", e);
                    }
                }
            });
            return;
        }
        try {
            this.ft.y();
        } catch (RemoteException e) {
            cn.b("Could not call onAdClicked.", e);
        }
    }

    @Override // com.google.ads.mediation.MediationBannerListener
    public void onDismissScreen(MediationBannerAdapter<?, ?> adapter) {
        cn.m("Adapter called onDismissScreen.");
        if (!cm.ar()) {
            cn.q("onDismissScreen must be called on the main UI thread.");
            cm.hO.post(new Runnable() { // from class: com.google.android.gms.internal.ba.4
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        ba.this.ft.onAdClosed();
                    } catch (RemoteException e) {
                        cn.b("Could not call onAdClosed.", e);
                    }
                }
            });
            return;
        }
        try {
            this.ft.onAdClosed();
        } catch (RemoteException e) {
            cn.b("Could not call onAdClosed.", e);
        }
    }

    @Override // com.google.ads.mediation.MediationInterstitialListener
    public void onDismissScreen(MediationInterstitialAdapter<?, ?> adapter) {
        cn.m("Adapter called onDismissScreen.");
        if (!cm.ar()) {
            cn.q("onDismissScreen must be called on the main UI thread.");
            cm.hO.post(new Runnable() { // from class: com.google.android.gms.internal.ba.9
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        ba.this.ft.onAdClosed();
                    } catch (RemoteException e) {
                        cn.b("Could not call onAdClosed.", e);
                    }
                }
            });
            return;
        }
        try {
            this.ft.onAdClosed();
        } catch (RemoteException e) {
            cn.b("Could not call onAdClosed.", e);
        }
    }

    @Override // com.google.ads.mediation.MediationBannerListener
    public void onFailedToReceiveAd(MediationBannerAdapter<?, ?> adapter, final AdRequest.ErrorCode errorCode) {
        cn.m("Adapter called onFailedToReceiveAd with error. " + errorCode);
        if (!cm.ar()) {
            cn.q("onFailedToReceiveAd must be called on the main UI thread.");
            cm.hO.post(new Runnable() { // from class: com.google.android.gms.internal.ba.5
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        ba.this.ft.onAdFailedToLoad(bb.a(errorCode));
                    } catch (RemoteException e) {
                        cn.b("Could not call onAdFailedToLoad.", e);
                    }
                }
            });
            return;
        }
        try {
            this.ft.onAdFailedToLoad(bb.a(errorCode));
        } catch (RemoteException e) {
            cn.b("Could not call onAdFailedToLoad.", e);
        }
    }

    @Override // com.google.ads.mediation.MediationInterstitialListener
    public void onFailedToReceiveAd(MediationInterstitialAdapter<?, ?> adapter, final AdRequest.ErrorCode errorCode) {
        cn.m("Adapter called onFailedToReceiveAd with error " + errorCode + ".");
        if (!cm.ar()) {
            cn.q("onFailedToReceiveAd must be called on the main UI thread.");
            cm.hO.post(new Runnable() { // from class: com.google.android.gms.internal.ba.10
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        ba.this.ft.onAdFailedToLoad(bb.a(errorCode));
                    } catch (RemoteException e) {
                        cn.b("Could not call onAdFailedToLoad.", e);
                    }
                }
            });
            return;
        }
        try {
            this.ft.onAdFailedToLoad(bb.a(errorCode));
        } catch (RemoteException e) {
            cn.b("Could not call onAdFailedToLoad.", e);
        }
    }

    @Override // com.google.ads.mediation.MediationBannerListener
    public void onLeaveApplication(MediationBannerAdapter<?, ?> adapter) {
        cn.m("Adapter called onLeaveApplication.");
        if (!cm.ar()) {
            cn.q("onLeaveApplication must be called on the main UI thread.");
            cm.hO.post(new Runnable() { // from class: com.google.android.gms.internal.ba.6
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        ba.this.ft.onAdLeftApplication();
                    } catch (RemoteException e) {
                        cn.b("Could not call onAdLeftApplication.", e);
                    }
                }
            });
            return;
        }
        try {
            this.ft.onAdLeftApplication();
        } catch (RemoteException e) {
            cn.b("Could not call onAdLeftApplication.", e);
        }
    }

    @Override // com.google.ads.mediation.MediationInterstitialListener
    public void onLeaveApplication(MediationInterstitialAdapter<?, ?> adapter) {
        cn.m("Adapter called onLeaveApplication.");
        if (!cm.ar()) {
            cn.q("onLeaveApplication must be called on the main UI thread.");
            cm.hO.post(new Runnable() { // from class: com.google.android.gms.internal.ba.11
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        ba.this.ft.onAdLeftApplication();
                    } catch (RemoteException e) {
                        cn.b("Could not call onAdLeftApplication.", e);
                    }
                }
            });
            return;
        }
        try {
            this.ft.onAdLeftApplication();
        } catch (RemoteException e) {
            cn.b("Could not call onAdLeftApplication.", e);
        }
    }

    @Override // com.google.ads.mediation.MediationBannerListener
    public void onPresentScreen(MediationBannerAdapter<?, ?> adapter) {
        cn.m("Adapter called onPresentScreen.");
        if (!cm.ar()) {
            cn.q("onPresentScreen must be called on the main UI thread.");
            cm.hO.post(new Runnable() { // from class: com.google.android.gms.internal.ba.7
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        ba.this.ft.onAdOpened();
                    } catch (RemoteException e) {
                        cn.b("Could not call onAdOpened.", e);
                    }
                }
            });
            return;
        }
        try {
            this.ft.onAdOpened();
        } catch (RemoteException e) {
            cn.b("Could not call onAdOpened.", e);
        }
    }

    @Override // com.google.ads.mediation.MediationInterstitialListener
    public void onPresentScreen(MediationInterstitialAdapter<?, ?> adapter) {
        cn.m("Adapter called onPresentScreen.");
        if (!cm.ar()) {
            cn.q("onPresentScreen must be called on the main UI thread.");
            cm.hO.post(new Runnable() { // from class: com.google.android.gms.internal.ba.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        ba.this.ft.onAdOpened();
                    } catch (RemoteException e) {
                        cn.b("Could not call onAdOpened.", e);
                    }
                }
            });
            return;
        }
        try {
            this.ft.onAdOpened();
        } catch (RemoteException e) {
            cn.b("Could not call onAdOpened.", e);
        }
    }

    @Override // com.google.ads.mediation.MediationBannerListener
    public void onReceivedAd(MediationBannerAdapter<?, ?> adapter) {
        cn.m("Adapter called onReceivedAd.");
        if (!cm.ar()) {
            cn.q("onReceivedAd must be called on the main UI thread.");
            cm.hO.post(new Runnable() { // from class: com.google.android.gms.internal.ba.8
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        ba.this.ft.onAdLoaded();
                    } catch (RemoteException e) {
                        cn.b("Could not call onAdLoaded.", e);
                    }
                }
            });
            return;
        }
        try {
            this.ft.onAdLoaded();
        } catch (RemoteException e) {
            cn.b("Could not call onAdLoaded.", e);
        }
    }

    @Override // com.google.ads.mediation.MediationInterstitialListener
    public void onReceivedAd(MediationInterstitialAdapter<?, ?> adapter) {
        cn.m("Adapter called onReceivedAd.");
        if (!cm.ar()) {
            cn.q("onReceivedAd must be called on the main UI thread.");
            cm.hO.post(new Runnable() { // from class: com.google.android.gms.internal.ba.3
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        ba.this.ft.onAdLoaded();
                    } catch (RemoteException e) {
                        cn.b("Could not call onAdLoaded.", e);
                    }
                }
            });
            return;
        }
        try {
            this.ft.onAdLoaded();
        } catch (RemoteException e) {
            cn.b("Could not call onAdLoaded.", e);
        }
    }
}

package com.google.ads.mediation.customevent;

import android.app.Activity;
import android.view.View;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationBannerListener;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationInterstitialListener;
import com.google.android.gms.ads.mediation.customevent.CustomEventExtras;
import com.google.android.gms.internal.cn;
/* loaded from: classes.dex */
public final class CustomEventAdapter implements MediationBannerAdapter<CustomEventExtras, CustomEventServerParameters>, MediationInterstitialAdapter<CustomEventExtras, CustomEventServerParameters> {
    private View m;
    private CustomEventBanner n;
    private CustomEventInterstitial o;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class a implements CustomEventBannerListener {
        private final MediationBannerListener k;
        private final CustomEventAdapter p;

        public a(CustomEventAdapter customEventAdapter, MediationBannerListener mediationBannerListener) {
            this.p = customEventAdapter;
            this.k = mediationBannerListener;
        }

        @Override // com.google.ads.mediation.customevent.CustomEventBannerListener
        public void onClick() {
            cn.m("Custom event adapter called onFailedToReceiveAd.");
            this.k.onClick(this.p);
        }

        @Override // com.google.ads.mediation.customevent.CustomEventListener
        public void onDismissScreen() {
            cn.m("Custom event adapter called onFailedToReceiveAd.");
            this.k.onDismissScreen(this.p);
        }

        @Override // com.google.ads.mediation.customevent.CustomEventListener
        public void onFailedToReceiveAd() {
            cn.m("Custom event adapter called onFailedToReceiveAd.");
            this.k.onFailedToReceiveAd(this.p, AdRequest.ErrorCode.NO_FILL);
        }

        @Override // com.google.ads.mediation.customevent.CustomEventListener
        public void onLeaveApplication() {
            cn.m("Custom event adapter called onFailedToReceiveAd.");
            this.k.onLeaveApplication(this.p);
        }

        @Override // com.google.ads.mediation.customevent.CustomEventListener
        public void onPresentScreen() {
            cn.m("Custom event adapter called onFailedToReceiveAd.");
            this.k.onPresentScreen(this.p);
        }

        @Override // com.google.ads.mediation.customevent.CustomEventBannerListener
        public void onReceivedAd(View view) {
            cn.m("Custom event adapter called onReceivedAd.");
            this.p.a(view);
            this.k.onReceivedAd(this.p);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class b implements CustomEventInterstitialListener {
        private final MediationInterstitialListener l;
        private final CustomEventAdapter p;

        public b(CustomEventAdapter customEventAdapter, MediationInterstitialListener mediationInterstitialListener) {
            this.p = customEventAdapter;
            this.l = mediationInterstitialListener;
        }

        @Override // com.google.ads.mediation.customevent.CustomEventListener
        public void onDismissScreen() {
            cn.m("Custom event adapter called onDismissScreen.");
            this.l.onDismissScreen(this.p);
        }

        @Override // com.google.ads.mediation.customevent.CustomEventListener
        public void onFailedToReceiveAd() {
            cn.m("Custom event adapter called onFailedToReceiveAd.");
            this.l.onFailedToReceiveAd(this.p, AdRequest.ErrorCode.NO_FILL);
        }

        @Override // com.google.ads.mediation.customevent.CustomEventListener
        public void onLeaveApplication() {
            cn.m("Custom event adapter called onLeaveApplication.");
            this.l.onLeaveApplication(this.p);
        }

        @Override // com.google.ads.mediation.customevent.CustomEventListener
        public void onPresentScreen() {
            cn.m("Custom event adapter called onPresentScreen.");
            this.l.onPresentScreen(this.p);
        }

        @Override // com.google.ads.mediation.customevent.CustomEventInterstitialListener
        public void onReceivedAd() {
            cn.m("Custom event adapter called onReceivedAd.");
            this.l.onReceivedAd(CustomEventAdapter.this);
        }
    }

    private static <T> T a(String str) {
        try {
            return (T) Class.forName(str).newInstance();
        } catch (Throwable th) {
            cn.q("Could not instantiate custom event adapter: " + str + ". " + th.getMessage());
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(View view) {
        this.m = view;
    }

    @Override // com.google.ads.mediation.MediationAdapter
    public void destroy() {
        if (this.n != null) {
            this.n.destroy();
        }
        if (this.o != null) {
            this.o.destroy();
        }
    }

    @Override // com.google.ads.mediation.MediationAdapter
    public Class<CustomEventExtras> getAdditionalParametersType() {
        return CustomEventExtras.class;
    }

    @Override // com.google.ads.mediation.MediationBannerAdapter
    public View getBannerView() {
        return this.m;
    }

    @Override // com.google.ads.mediation.MediationAdapter
    public Class<CustomEventServerParameters> getServerParametersType() {
        return CustomEventServerParameters.class;
    }

    @Override // com.google.ads.mediation.MediationBannerAdapter
    public void requestBannerAd(MediationBannerListener listener, Activity activity, CustomEventServerParameters serverParameters, AdSize adSize, MediationAdRequest mediationAdRequest, CustomEventExtras customEventExtras) {
        this.n = (CustomEventBanner) a(serverParameters.className);
        if (this.n == null) {
            listener.onFailedToReceiveAd(this, AdRequest.ErrorCode.INTERNAL_ERROR);
        } else {
            this.n.requestBannerAd(new a(this, listener), activity, serverParameters.label, serverParameters.parameter, adSize, mediationAdRequest, customEventExtras == null ? null : customEventExtras.getExtra(serverParameters.label));
        }
    }

    @Override // com.google.ads.mediation.MediationInterstitialAdapter
    public void requestInterstitialAd(MediationInterstitialListener listener, Activity activity, CustomEventServerParameters serverParameters, MediationAdRequest mediationAdRequest, CustomEventExtras customEventExtras) {
        this.o = (CustomEventInterstitial) a(serverParameters.className);
        if (this.o == null) {
            listener.onFailedToReceiveAd(this, AdRequest.ErrorCode.INTERNAL_ERROR);
        } else {
            this.o.requestInterstitialAd(new b(this, listener), activity, serverParameters.label, serverParameters.parameter, mediationAdRequest, customEventExtras == null ? null : customEventExtras.getExtra(serverParameters.label));
        }
    }

    @Override // com.google.ads.mediation.MediationInterstitialAdapter
    public void showInterstitial() {
        this.o.showInterstitial();
    }
}

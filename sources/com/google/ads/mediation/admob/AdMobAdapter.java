package com.google.ads.mediation.admob;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationBannerListener;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationInterstitialListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.mediation.admob.AdMobExtras;
import com.google.android.gms.internal.bb;
import com.google.android.gms.internal.cm;
import java.util.Date;
import java.util.Set;
/* loaded from: classes.dex */
public final class AdMobAdapter implements MediationBannerAdapter<AdMobExtras, AdMobServerParameters>, MediationInterstitialAdapter<AdMobExtras, AdMobServerParameters> {
    private AdView h;
    private InterstitialAd i;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class a extends AdListener {
        private final AdMobAdapter j;
        private final MediationBannerListener k;

        public a(AdMobAdapter adMobAdapter, MediationBannerListener mediationBannerListener) {
            this.j = adMobAdapter;
            this.k = mediationBannerListener;
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdClosed() {
            this.k.onDismissScreen(this.j);
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdFailedToLoad(int errorCode) {
            this.k.onFailedToReceiveAd(this.j, bb.f(errorCode));
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdLeftApplication() {
            this.k.onLeaveApplication(this.j);
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdLoaded() {
            this.k.onReceivedAd(this.j);
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdOpened() {
            this.k.onClick(this.j);
            this.k.onPresentScreen(this.j);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class b extends AdListener {
        private final AdMobAdapter j;
        private final MediationInterstitialListener l;

        public b(AdMobAdapter adMobAdapter, MediationInterstitialListener mediationInterstitialListener) {
            this.j = adMobAdapter;
            this.l = mediationInterstitialListener;
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdClosed() {
            this.l.onDismissScreen(this.j);
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdFailedToLoad(int errorCode) {
            this.l.onFailedToReceiveAd(this.j, bb.f(errorCode));
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdLeftApplication() {
            this.l.onLeaveApplication(this.j);
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdLoaded() {
            this.l.onReceivedAd(this.j);
        }

        @Override // com.google.android.gms.ads.AdListener
        public void onAdOpened() {
            this.l.onPresentScreen(this.j);
        }
    }

    private static AdRequest a(Context context, MediationAdRequest mediationAdRequest, AdMobExtras adMobExtras, AdMobServerParameters adMobServerParameters) {
        AdRequest.Builder builder = new AdRequest.Builder();
        Date birthday = mediationAdRequest.getBirthday();
        if (birthday != null) {
            builder.setBirthday(birthday);
        }
        AdRequest.Gender gender = mediationAdRequest.getGender();
        if (gender != null) {
            builder.setGender(bb.a(gender));
        }
        Set<String> keywords = mediationAdRequest.getKeywords();
        if (keywords != null) {
            for (String str : keywords) {
                builder.addKeyword(str);
            }
        }
        if (mediationAdRequest.isTesting()) {
            builder.addTestDevice(cm.l(context));
        }
        if (adMobServerParameters.tagForChildDirectedTreatment != -1) {
            builder.tagForChildDirectedTreatment(adMobServerParameters.tagForChildDirectedTreatment == 1);
        }
        if (adMobExtras == null) {
            adMobExtras = new AdMobExtras(new Bundle());
        }
        Bundle extras = adMobExtras.getExtras();
        extras.putInt("gw", 1);
        extras.putString("mad_hac", adMobServerParameters.allowHouseAds);
        extras.putBoolean("_noRefresh", true);
        builder.addNetworkExtras(adMobExtras);
        return builder.build();
    }

    @Override // com.google.ads.mediation.MediationAdapter
    public void destroy() {
        if (this.h != null) {
            this.h.destroy();
            this.h = null;
        }
        if (this.i != null) {
            this.i = null;
        }
    }

    @Override // com.google.ads.mediation.MediationAdapter
    public Class<AdMobExtras> getAdditionalParametersType() {
        return AdMobExtras.class;
    }

    @Override // com.google.ads.mediation.MediationBannerAdapter
    public View getBannerView() {
        return this.h;
    }

    @Override // com.google.ads.mediation.MediationAdapter
    public Class<AdMobServerParameters> getServerParametersType() {
        return AdMobServerParameters.class;
    }

    @Override // com.google.ads.mediation.MediationBannerAdapter
    public void requestBannerAd(MediationBannerListener bannerListener, Activity activity, AdMobServerParameters serverParameters, AdSize adSize, MediationAdRequest mediationAdRequest, AdMobExtras extras) {
        this.h = new AdView(activity);
        this.h.setAdSize(new com.google.android.gms.ads.AdSize(adSize.getWidth(), adSize.getHeight()));
        this.h.setAdUnitId(serverParameters.adUnitId);
        this.h.setAdListener(new a(this, bannerListener));
        this.h.loadAd(a(activity, mediationAdRequest, extras, serverParameters));
    }

    @Override // com.google.ads.mediation.MediationInterstitialAdapter
    public void requestInterstitialAd(MediationInterstitialListener interstitialListener, Activity activity, AdMobServerParameters serverParameters, MediationAdRequest mediationAdRequest, AdMobExtras extras) {
        this.i = new InterstitialAd(activity);
        this.i.setAdUnitId(serverParameters.adUnitId);
        this.i.setAdListener(new b(this, interstitialListener));
        this.i.loadAd(a(activity, mediationAdRequest, extras, serverParameters));
    }

    @Override // com.google.ads.mediation.MediationInterstitialAdapter
    public void showInterstitial() {
        this.i.show();
    }
}

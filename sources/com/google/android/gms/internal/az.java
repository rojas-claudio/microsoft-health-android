package com.google.android.gms.internal;

import android.app.Activity;
import android.os.RemoteException;
import com.google.ads.mediation.MediationAdapter;
import com.google.ads.mediation.MediationBannerAdapter;
import com.google.ads.mediation.MediationInterstitialAdapter;
import com.google.ads.mediation.MediationServerParameters;
import com.google.ads.mediation.NetworkExtras;
import com.google.ads.mediation.admob.AdMobServerParameters;
import com.google.android.gms.internal.ax;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONObject;
/* loaded from: classes.dex */
public final class az<NETWORK_EXTRAS extends NetworkExtras, SERVER_PARAMETERS extends MediationServerParameters> extends ax.a {
    private final MediationAdapter<NETWORK_EXTRAS, SERVER_PARAMETERS> fr;
    private final NETWORK_EXTRAS fs;

    public az(MediationAdapter<NETWORK_EXTRAS, SERVER_PARAMETERS> mediationAdapter, NETWORK_EXTRAS network_extras) {
        this.fr = mediationAdapter;
        this.fs = network_extras;
    }

    private SERVER_PARAMETERS a(String str, int i) throws RemoteException {
        HashMap hashMap;
        try {
            if (str != null) {
                JSONObject jSONObject = new JSONObject(str);
                HashMap hashMap2 = new HashMap(jSONObject.length());
                Iterator<String> keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    hashMap2.put(next, jSONObject.getString(next));
                }
                hashMap = hashMap2;
            } else {
                hashMap = new HashMap(0);
            }
            Class<SERVER_PARAMETERS> serverParametersType = this.fr.getServerParametersType();
            AdMobServerParameters adMobServerParameters = null;
            if (serverParametersType != null) {
                SERVER_PARAMETERS newInstance = serverParametersType.newInstance();
                newInstance.load(hashMap);
                adMobServerParameters = newInstance;
            }
            if (adMobServerParameters instanceof AdMobServerParameters) {
                adMobServerParameters.tagForChildDirectedTreatment = i;
            }
            return adMobServerParameters;
        } catch (Throwable th) {
            cn.b("Could not get MediationServerParameters.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.ax
    public void a(com.google.android.gms.dynamic.b bVar, v vVar, String str, ay ayVar) throws RemoteException {
        if (!(this.fr instanceof MediationInterstitialAdapter)) {
            cn.q("MediationAdapter is not a MediationInterstitialAdapter: " + this.fr.getClass().getCanonicalName());
            throw new RemoteException();
        }
        cn.m("Requesting interstitial ad from adapter.");
        try {
            ((MediationInterstitialAdapter) this.fr).requestInterstitialAd(new ba(ayVar), (Activity) com.google.android.gms.dynamic.c.b(bVar), a(str, vVar.tagForChildDirectedTreatment), bb.e(vVar), this.fs);
        } catch (Throwable th) {
            cn.b("Could not request interstitial ad from adapter.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.ax
    public void a(com.google.android.gms.dynamic.b bVar, x xVar, v vVar, String str, ay ayVar) throws RemoteException {
        if (!(this.fr instanceof MediationBannerAdapter)) {
            cn.q("MediationAdapter is not a MediationBannerAdapter: " + this.fr.getClass().getCanonicalName());
            throw new RemoteException();
        }
        cn.m("Requesting banner ad from adapter.");
        try {
            ((MediationBannerAdapter) this.fr).requestBannerAd(new ba(ayVar), (Activity) com.google.android.gms.dynamic.c.b(bVar), a(str, vVar.tagForChildDirectedTreatment), bb.a(xVar), bb.e(vVar), this.fs);
        } catch (Throwable th) {
            cn.b("Could not request banner ad from adapter.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.ax
    public void destroy() throws RemoteException {
        try {
            this.fr.destroy();
        } catch (Throwable th) {
            cn.b("Could not destroy adapter.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.ax
    public com.google.android.gms.dynamic.b getView() throws RemoteException {
        if (!(this.fr instanceof MediationBannerAdapter)) {
            cn.q("MediationAdapter is not a MediationBannerAdapter: " + this.fr.getClass().getCanonicalName());
            throw new RemoteException();
        }
        try {
            return com.google.android.gms.dynamic.c.g(((MediationBannerAdapter) this.fr).getBannerView());
        } catch (Throwable th) {
            cn.b("Could not get banner view from adapter.", th);
            throw new RemoteException();
        }
    }

    @Override // com.google.android.gms.internal.ax
    public void showInterstitial() throws RemoteException {
        if (!(this.fr instanceof MediationInterstitialAdapter)) {
            cn.q("MediationAdapter is not a MediationInterstitialAdapter: " + this.fr.getClass().getCanonicalName());
            throw new RemoteException();
        }
        cn.m("Showing interstitial from adapter.");
        try {
            ((MediationInterstitialAdapter) this.fr).showInterstitial();
        } catch (Throwable th) {
            cn.b("Could not show interstitial from adapter.", th);
            throw new RemoteException();
        }
    }
}

package com.google.android.gms.internal;

import android.os.RemoteException;
import com.google.ads.mediation.MediationAdapter;
import com.google.ads.mediation.MediationServerParameters;
import com.google.android.gms.ads.mediation.NetworkExtras;
import com.google.android.gms.internal.aw;
import java.util.Map;
/* loaded from: classes.dex */
public final class av extends aw.a {
    private Map<Class<? extends NetworkExtras>, NetworkExtras> fq;

    private <NETWORK_EXTRAS extends com.google.ads.mediation.NetworkExtras, SERVER_PARAMETERS extends MediationServerParameters> ax h(String str) throws RemoteException {
        try {
            MediationAdapter mediationAdapter = (MediationAdapter) Class.forName(str).newInstance();
            return new az(mediationAdapter, (com.google.ads.mediation.NetworkExtras) this.fq.get(mediationAdapter.getAdditionalParametersType()));
        } catch (Throwable th) {
            cn.q("Could not instantiate mediation adapter: " + str + ". " + th.getMessage());
            throw new RemoteException();
        }
    }

    public void c(Map<Class<? extends NetworkExtras>, NetworkExtras> map) {
        this.fq = map;
    }

    @Override // com.google.android.gms.internal.aw
    public ax g(String str) throws RemoteException {
        return h(str);
    }
}

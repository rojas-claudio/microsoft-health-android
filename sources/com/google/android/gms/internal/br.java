package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.bs;
/* loaded from: classes.dex */
public final class br {

    /* loaded from: classes.dex */
    public interface a {
        void a(bw bwVar);
    }

    public static cg a(Context context, bu buVar, a aVar) {
        return buVar.eg.hS ? b(context, buVar, aVar) : c(context, buVar, aVar);
    }

    private static cg b(Context context, bu buVar, a aVar) {
        cn.m("Fetching ad response from local ad request service.");
        bs.a aVar2 = new bs.a(context, buVar, aVar);
        aVar2.start();
        return aVar2;
    }

    private static cg c(Context context, bu buVar, a aVar) {
        cn.m("Fetching ad response from remote ad request service.");
        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context) != 0) {
            cn.q("Failed to connect to remote ad request service.");
            return null;
        }
        return new bs.b(context, buVar, aVar);
    }
}

package com.google.android.gms.internal;

import android.content.Context;
import com.google.android.gms.internal.bu;
/* loaded from: classes.dex */
public final class bp {

    /* loaded from: classes.dex */
    public interface a {
        void a(ce ceVar);
    }

    public static cg a(Context context, bu.a aVar, h hVar, cq cqVar, aw awVar, a aVar2) {
        bq bqVar = new bq(context, aVar, hVar, cqVar, awVar, aVar2);
        bqVar.start();
        return bqVar;
    }
}

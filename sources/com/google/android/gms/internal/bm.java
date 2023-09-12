package com.google.android.gms.internal;

import android.app.Activity;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.dynamic.e;
import com.google.android.gms.internal.bn;
import com.google.android.gms.internal.bo;
/* loaded from: classes.dex */
public final class bm extends com.google.android.gms.dynamic.e<bo> {
    private static final bm gl = new bm();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class a extends Exception {
        public a(String str) {
            super(str);
        }
    }

    private bm() {
        super("com.google.android.gms.ads.AdOverlayCreatorImpl");
    }

    public static bn a(Activity activity) {
        bn c;
        try {
            if (b(activity)) {
                cn.m("Using AdOverlay from the client jar.");
                c = new bf(activity);
            } else {
                c = gl.c(activity);
            }
            return c;
        } catch (a e) {
            cn.q(e.getMessage());
            return null;
        }
    }

    private static boolean b(Activity activity) throws a {
        Intent intent = activity.getIntent();
        if (intent.hasExtra("com.google.android.gms.ads.internal.overlay.useClientJar")) {
            return intent.getBooleanExtra("com.google.android.gms.ads.internal.overlay.useClientJar", false);
        }
        throw new a("Ad overlay requires the useClientJar flag in intent extras.");
    }

    private bn c(Activity activity) {
        try {
            return bn.a.m(t(activity).a(com.google.android.gms.dynamic.c.g(activity)));
        } catch (RemoteException e) {
            cn.b("Could not create remote AdOverlay.", e);
            return null;
        } catch (e.a e2) {
            cn.b("Could not create remote AdOverlay.", e2);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.dynamic.e
    /* renamed from: l */
    public bo d(IBinder iBinder) {
        return bo.a.n(iBinder);
    }
}

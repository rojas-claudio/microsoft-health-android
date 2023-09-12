package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.view.View;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.fi;
import com.google.android.gms.plus.PlusOneDummyView;
/* loaded from: classes.dex */
public final class fm {
    private static Context pW;
    private static fi rx;

    /* loaded from: classes.dex */
    public static class a extends Exception {
        public a(String str) {
            super(str);
        }
    }

    public static View a(Context context, int i, int i2, String str, int i3) {
        try {
            if (str == null) {
                throw new NullPointerException();
            }
            return (View) com.google.android.gms.dynamic.c.b(x(context).a(com.google.android.gms.dynamic.c.g(context), i, i2, str, i3));
        } catch (Exception e) {
            return new PlusOneDummyView(context, i);
        }
    }

    public static View a(Context context, int i, int i2, String str, String str2) {
        try {
            if (str == null) {
                throw new NullPointerException();
            }
            return (View) com.google.android.gms.dynamic.c.b(x(context).a(com.google.android.gms.dynamic.c.g(context), i, i2, str, str2));
        } catch (Exception e) {
            return new PlusOneDummyView(context, i);
        }
    }

    private static fi x(Context context) throws a {
        dm.e(context);
        if (rx == null) {
            if (pW == null) {
                pW = GooglePlayServicesUtil.getRemoteContext(context);
                if (pW == null) {
                    throw new a("Could not get remote context.");
                }
            }
            try {
                rx = fi.a.ao((IBinder) pW.getClassLoader().loadClass("com.google.android.gms.plus.plusone.PlusOneButtonCreatorImpl").newInstance());
            } catch (ClassNotFoundException e) {
                throw new a("Could not load creator class.");
            } catch (IllegalAccessException e2) {
                throw new a("Could not access creator.");
            } catch (InstantiationException e3) {
                throw new a("Could not instantiate creator.");
            }
        }
        return rx;
    }
}

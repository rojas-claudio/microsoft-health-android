package com.google.android.gms.internal;

import android.util.Log;
import com.google.ads.AdRequest;
/* loaded from: classes.dex */
public final class cn {
    public static void a(String str, Throwable th) {
        if (k(3)) {
            Log.d(AdRequest.LOGTAG, str, th);
        }
    }

    public static void b(String str, Throwable th) {
        if (k(5)) {
            Log.w(AdRequest.LOGTAG, str, th);
        }
    }

    public static boolean k(int i) {
        return (i >= 5 || Log.isLoggable(AdRequest.LOGTAG, i)) && i != 2;
    }

    public static void m(String str) {
        if (k(3)) {
            Log.d(AdRequest.LOGTAG, str);
        }
    }

    public static void n(String str) {
        if (k(6)) {
            Log.e(AdRequest.LOGTAG, str);
        }
    }

    public static void o(String str) {
        if (k(4)) {
            Log.i(AdRequest.LOGTAG, str);
        }
    }

    public static void p(String str) {
        if (k(2)) {
            Log.v(AdRequest.LOGTAG, str);
        }
    }

    public static void q(String str) {
        if (k(5)) {
            Log.w(AdRequest.LOGTAG, str);
        }
    }
}

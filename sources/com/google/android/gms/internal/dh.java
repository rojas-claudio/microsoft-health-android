package com.google.android.gms.internal;

import android.util.Log;
/* loaded from: classes.dex */
public final class dh {
    private final String li;

    public dh(String str) {
        this.li = (String) dm.e(str);
    }

    public void a(String str, String str2, Throwable th) {
        if (x(6)) {
            Log.e(str, str2, th);
        }
    }

    public void b(String str, String str2) {
        if (x(3)) {
            Log.d(str, str2);
        }
    }

    public void c(String str, String str2) {
        if (x(5)) {
            Log.w(str, str2);
        }
    }

    public void d(String str, String str2) {
        if (x(6)) {
            Log.e(str, str2);
        }
    }

    public void e(String str, String str2) {
        if (x(4)) {
        }
    }

    public boolean x(int i) {
        return Log.isLoggable(this.li, i);
    }
}

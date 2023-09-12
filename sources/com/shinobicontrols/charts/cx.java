package com.shinobicontrols.charts;

import android.util.Log;
/* loaded from: classes.dex */
class cx {
    static void a(Class<?> cls, String str, Throwable th) {
        if (cls != null) {
            str = String.format("%s: %s", cls.getName(), str);
        }
        if (th == null) {
            Log.d("ShinobiCharts", str);
        } else {
            Log.d("ShinobiCharts", str, th);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(String str) {
        a(null, str, null);
    }

    static void b(Class<?> cls, String str, Throwable th) {
        if (cls != null) {
            str = String.format("%s: %s", cls.getName(), str);
        }
        if (th == null) {
            Log.w("ShinobiCharts", str);
        } else {
            Log.w("ShinobiCharts", str, th);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(String str) {
        b(null, str, null);
    }

    static void c(Class<?> cls, String str, Throwable th) {
        if (cls != null) {
            str = String.format("%s: %s", cls.getName(), str);
        }
        if (th == null) {
            Log.e("ShinobiCharts", str);
        } else {
            Log.e("ShinobiCharts", str, th);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void c(String str) {
        c(null, str, null);
    }
}

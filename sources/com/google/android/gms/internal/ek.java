package com.google.android.gms.internal;

import android.os.Build;
/* loaded from: classes.dex */
public final class ek {
    private static boolean I(int i) {
        return Build.VERSION.SDK_INT >= i;
    }

    public static boolean bJ() {
        return I(11);
    }

    public static boolean bK() {
        return I(12);
    }

    public static boolean bL() {
        return I(13);
    }

    public static boolean bM() {
        return I(14);
    }

    public static boolean bN() {
        return I(16);
    }

    public static boolean bO() {
        return I(17);
    }
}

package com.google.android.gms.internal;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
/* loaded from: classes.dex */
public abstract class dd implements SafeParcelable {
    private boolean kC = false;
    private static final Object kz = new Object();
    private static ClassLoader kA = null;
    private static Integer kB = null;

    private static boolean a(Class<?> cls) {
        try {
            return SafeParcelable.NULL.equals(cls.getField("NULL").get(null));
        } catch (IllegalAccessException e) {
            return false;
        } catch (NoSuchFieldException e2) {
            return false;
        }
    }

    protected static ClassLoader aV() {
        ClassLoader classLoader;
        synchronized (kz) {
            classLoader = kA;
        }
        return classLoader;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static Integer aW() {
        Integer num;
        synchronized (kz) {
            num = kB;
        }
        return num;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static boolean y(String str) {
        ClassLoader aV = aV();
        if (aV == null) {
            return true;
        }
        try {
            return a(aV.loadClass(str));
        } catch (Exception e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean aX() {
        return this.kC;
    }
}

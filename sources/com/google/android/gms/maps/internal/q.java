package com.google.android.gms.maps.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.dm;
import com.google.android.gms.maps.internal.c;
import com.google.android.gms.maps.model.RuntimeRemoteException;
/* loaded from: classes.dex */
public class q {
    private static Context pW;
    private static c pX;

    private static <T> T a(ClassLoader classLoader, String str) {
        try {
            return (T) c(((ClassLoader) dm.e(classLoader)).loadClass(str));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Unable to find dynamic class " + str);
        }
    }

    private static <T> T c(Class<?> cls) {
        try {
            return (T) cls.newInstance();
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unable to call the default constructor of " + cls.getName());
        } catch (InstantiationException e2) {
            throw new IllegalStateException("Unable to instantiate the dynamic class " + cls.getName());
        }
    }

    private static boolean cI() {
        return cJ() != null;
    }

    private static Class<?> cJ() {
        try {
            return Class.forName("com.google.android.gms.maps.internal.CreatorImpl");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static Context getRemoteContext(Context context) {
        if (pW == null) {
            if (cI()) {
                pW = context;
            } else {
                pW = GooglePlayServicesUtil.getRemoteContext(context);
            }
        }
        return pW;
    }

    public static c u(Context context) throws GooglePlayServicesNotAvailableException {
        dm.e(context);
        if (pX != null) {
            return pX;
        }
        v(context);
        pX = w(context);
        try {
            pX.a(com.google.android.gms.dynamic.c.g(getRemoteContext(context).getResources()), GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE);
            return pX;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    private static void v(Context context) throws GooglePlayServicesNotAvailableException {
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        switch (isGooglePlayServicesAvailable) {
            case 0:
                return;
            default:
                throw new GooglePlayServicesNotAvailableException(isGooglePlayServicesAvailable);
        }
    }

    private static c w(Context context) {
        if (cI()) {
            Log.i(q.class.getSimpleName(), "Making Creator statically");
            return (c) c(cJ());
        }
        return c.a.J((IBinder) a(getRemoteContext(context).getClassLoader(), "com.google.android.gms.maps.internal.CreatorImpl"));
    }
}

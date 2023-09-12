package com.google.android.gms.ads.identifier;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.a;
import com.google.android.gms.internal.dm;
import com.google.android.gms.internal.p;
import java.io.IOException;
/* loaded from: classes.dex */
public final class AdvertisingIdClient {

    /* loaded from: classes.dex */
    public static final class Info {
        private final String dX;
        private final boolean dY;

        Info(String advertisingId, boolean limitAdTrackingEnabled) {
            this.dX = advertisingId;
            this.dY = limitAdTrackingEnabled;
        }

        public String getId() {
            return this.dX;
        }

        public boolean isLimitAdTrackingEnabled() {
            return this.dY;
        }
    }

    private static a g(Context context) throws IOException, GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        try {
            context.getPackageManager().getPackageInfo(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE, 0);
            try {
                GooglePlayServicesUtil.m(context);
                a aVar = new a();
                Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
                intent.setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE);
                if (context.bindService(intent, aVar, 1)) {
                    return aVar;
                }
                throw new IOException("Connection failure");
            } catch (GooglePlayServicesNotAvailableException e) {
                throw new IOException(e);
            }
        } catch (PackageManager.NameNotFoundException e2) {
            throw new GooglePlayServicesNotAvailableException(9);
        }
    }

    public static Info getAdvertisingIdInfo(Context context) throws IOException, IllegalStateException, GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        dm.x("Calling this from your main thread can lead to deadlock");
        a g = g(context);
        try {
            try {
                try {
                    p b = p.a.b(g.aG());
                    return new Info(b.getId(), b.a(true));
                } catch (InterruptedException e) {
                    throw new IOException("Interrupted exception");
                }
            } catch (RemoteException e2) {
                Log.i("AdvertisingIdClient", "GMS remote exception ", e2);
                throw new IOException("Remote exception");
            }
        } finally {
            context.unbindService(g);
        }
    }
}

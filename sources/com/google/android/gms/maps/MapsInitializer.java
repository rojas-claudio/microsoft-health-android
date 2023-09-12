package com.google.android.gms.maps;

import android.content.Context;
import android.os.RemoteException;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.internal.dm;
import com.google.android.gms.maps.internal.c;
import com.google.android.gms.maps.internal.q;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.RuntimeRemoteException;
/* loaded from: classes.dex */
public final class MapsInitializer {
    private MapsInitializer() {
    }

    public static void initialize(Context context) throws GooglePlayServicesNotAvailableException {
        dm.e(context);
        c u = q.u(context);
        try {
            CameraUpdateFactory.a(u.cG());
            BitmapDescriptorFactory.a(u.cH());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}

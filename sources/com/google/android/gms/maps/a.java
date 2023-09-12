package com.google.android.gms.maps;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.b;
/* loaded from: classes.dex */
public class a {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(GoogleMapOptions googleMapOptions, Parcel parcel, int i) {
        int k = b.k(parcel);
        b.c(parcel, 1, googleMapOptions.getVersionCode());
        b.a(parcel, 2, googleMapOptions.cv());
        b.a(parcel, 3, googleMapOptions.cw());
        b.c(parcel, 4, googleMapOptions.getMapType());
        b.a(parcel, 5, (Parcelable) googleMapOptions.getCamera(), i, false);
        b.a(parcel, 6, googleMapOptions.cx());
        b.a(parcel, 7, googleMapOptions.cy());
        b.a(parcel, 8, googleMapOptions.cz());
        b.a(parcel, 9, googleMapOptions.cA());
        b.a(parcel, 10, googleMapOptions.cB());
        b.a(parcel, 11, googleMapOptions.cC());
        b.C(parcel, k);
    }
}

package com.google.android.gms.maps.model;

import android.os.Parcel;
/* loaded from: classes.dex */
public class e {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(LatLng latLng, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, latLng.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, latLng.latitude);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, latLng.longitude);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }
}

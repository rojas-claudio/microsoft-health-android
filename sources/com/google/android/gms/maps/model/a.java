package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class a {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(CameraPosition cameraPosition, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, cameraPosition.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, (Parcelable) cameraPosition.target, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, cameraPosition.zoom);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, cameraPosition.tilt);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, cameraPosition.bearing);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }
}

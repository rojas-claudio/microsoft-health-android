package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class f {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(MarkerOptions markerOptions, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, markerOptions.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, (Parcelable) markerOptions.getPosition(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, markerOptions.getTitle(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, markerOptions.getSnippet(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, markerOptions.cN(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, markerOptions.getAnchorU());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, markerOptions.getAnchorV());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, markerOptions.isDraggable());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, markerOptions.isVisible());
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }
}

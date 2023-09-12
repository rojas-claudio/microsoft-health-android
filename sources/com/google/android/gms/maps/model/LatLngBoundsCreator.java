package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class LatLngBoundsCreator implements Parcelable.Creator<LatLngBounds> {
    public static final int CONTENT_DESCRIPTION = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(LatLngBounds latLngBounds, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, latLngBounds.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, (Parcelable) latLngBounds.southwest, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, (Parcelable) latLngBounds.northeast, i, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public LatLngBounds createFromParcel(Parcel parcel) {
        LatLng latLng;
        LatLng latLng2;
        int i;
        LatLng latLng3 = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i2 = 0;
        LatLng latLng4 = null;
        while (parcel.dataPosition() < j) {
            int i3 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i3)) {
                case 1:
                    LatLng latLng5 = latLng3;
                    latLng2 = latLng4;
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i3);
                    latLng = latLng5;
                    break;
                case 2:
                    LatLng latLng6 = (LatLng) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i3, LatLng.CREATOR);
                    i = i2;
                    latLng = latLng3;
                    latLng2 = latLng6;
                    break;
                case 3:
                    latLng = (LatLng) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i3, LatLng.CREATOR);
                    latLng2 = latLng4;
                    i = i2;
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i3);
                    latLng = latLng3;
                    latLng2 = latLng4;
                    i = i2;
                    break;
            }
            i2 = i;
            latLng4 = latLng2;
            latLng3 = latLng;
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new LatLngBounds(i2, latLng4, latLng3);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public LatLngBounds[] newArray(int size) {
        return new LatLngBounds[size];
    }
}

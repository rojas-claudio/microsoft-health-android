package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class PolylineOptionsCreator implements Parcelable.Creator<PolylineOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(PolylineOptions polylineOptions, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, polylineOptions.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.b(parcel, 2, polylineOptions.getPoints(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, polylineOptions.getWidth());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 4, polylineOptions.getColor());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, polylineOptions.getZIndex());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, polylineOptions.isVisible());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, polylineOptions.isGeodesic());
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public PolylineOptions createFromParcel(Parcel parcel) {
        float f = 0.0f;
        boolean z = false;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        ArrayList arrayList = null;
        boolean z2 = false;
        int i = 0;
        float f2 = 0.0f;
        int i2 = 0;
        while (parcel.dataPosition() < j) {
            int i3 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i3)) {
                case 1:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i3);
                    break;
                case 2:
                    arrayList = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i3, LatLng.CREATOR);
                    break;
                case 3:
                    f2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i3);
                    break;
                case 4:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i3);
                    break;
                case 5:
                    f = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i3);
                    break;
                case 6:
                    z2 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i3);
                    break;
                case 7:
                    z = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i3);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i3);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new PolylineOptions(i2, arrayList, f2, i, f, z2, z);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public PolylineOptions[] newArray(int size) {
        return new PolylineOptions[size];
    }
}

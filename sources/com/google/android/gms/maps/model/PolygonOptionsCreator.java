package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class PolygonOptionsCreator implements Parcelable.Creator<PolygonOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(PolygonOptions polygonOptions, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, polygonOptions.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.b(parcel, 2, polygonOptions.getPoints(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 3, polygonOptions.cO(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, polygonOptions.getStrokeWidth());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 5, polygonOptions.getStrokeColor());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 6, polygonOptions.getFillColor());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, polygonOptions.getZIndex());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, polygonOptions.isVisible());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, polygonOptions.isGeodesic());
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public PolygonOptions createFromParcel(Parcel parcel) {
        float f = 0.0f;
        boolean z = false;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        ArrayList arrayList = null;
        ArrayList arrayList2 = new ArrayList();
        boolean z2 = false;
        int i = 0;
        int i2 = 0;
        float f2 = 0.0f;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i4)) {
                case 1:
                    i3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 2:
                    arrayList = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i4, LatLng.CREATOR);
                    break;
                case 3:
                    com.google.android.gms.common.internal.safeparcel.a.a(parcel, i4, arrayList2, getClass().getClassLoader());
                    break;
                case 4:
                    f2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i4);
                    break;
                case 5:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 6:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 7:
                    f = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i4);
                    break;
                case 8:
                    z2 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i4);
                    break;
                case 9:
                    z = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i4);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i4);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new PolygonOptions(i3, arrayList, arrayList2, f2, i2, i, f, z2, z);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public PolygonOptions[] newArray(int size) {
        return new PolygonOptions[size];
    }
}

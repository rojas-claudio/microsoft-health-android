package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class MarkerOptionsCreator implements Parcelable.Creator<MarkerOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

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
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 10, markerOptions.isFlat());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 11, markerOptions.getRotation());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 12, markerOptions.getInfoWindowAnchorU());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 13, markerOptions.getInfoWindowAnchorV());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 14, markerOptions.getAlpha());
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public MarkerOptions createFromParcel(Parcel parcel) {
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        LatLng latLng = null;
        String str = null;
        String str2 = null;
        IBinder iBinder = null;
        float f = 0.0f;
        float f2 = 0.0f;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        float f3 = 0.0f;
        float f4 = 0.5f;
        float f5 = 0.0f;
        float f6 = 1.0f;
        while (parcel.dataPosition() < j) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i2);
                    break;
                case 2:
                    latLng = (LatLng) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i2, LatLng.CREATOR);
                    break;
                case 3:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 4:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 5:
                    iBinder = com.google.android.gms.common.internal.safeparcel.a.m(parcel, i2);
                    break;
                case 6:
                    f = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i2);
                    break;
                case 7:
                    f2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i2);
                    break;
                case 8:
                    z = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i2);
                    break;
                case 9:
                    z2 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i2);
                    break;
                case 10:
                    z3 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i2);
                    break;
                case 11:
                    f3 = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i2);
                    break;
                case 12:
                    f4 = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i2);
                    break;
                case 13:
                    f5 = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i2);
                    break;
                case 14:
                    f6 = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i2);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new MarkerOptions(i, latLng, str, str2, iBinder, f, f2, z, z2, z3, f3, f4, f5, f6);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public MarkerOptions[] newArray(int size) {
        return new MarkerOptions[size];
    }
}

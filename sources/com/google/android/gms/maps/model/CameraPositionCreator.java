package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class CameraPositionCreator implements Parcelable.Creator<CameraPosition> {
    public static final int CONTENT_DESCRIPTION = 0;

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

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public CameraPosition createFromParcel(Parcel parcel) {
        float f = 0.0f;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        LatLng latLng = null;
        float f2 = 0.0f;
        float f3 = 0.0f;
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
                    f3 = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i2);
                    break;
                case 4:
                    f2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i2);
                    break;
                case 5:
                    f = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i2);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new CameraPosition(i, latLng, f3, f2, f);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public CameraPosition[] newArray(int size) {
        return new CameraPosition[size];
    }
}

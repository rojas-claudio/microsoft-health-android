package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public class LatLngCreator implements Parcelable.Creator<LatLng> {
    public static final int CONTENT_DESCRIPTION = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(LatLng latLng, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, latLng.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, latLng.latitude);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, latLng.longitude);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public LatLng createFromParcel(Parcel parcel) {
        double d = Constants.SPLITS_ACCURACY;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        double d2 = 0.0d;
        while (parcel.dataPosition() < j) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i2);
                    break;
                case 2:
                    d2 = com.google.android.gms.common.internal.safeparcel.a.j(parcel, i2);
                    break;
                case 3:
                    d = com.google.android.gms.common.internal.safeparcel.a.j(parcel, i2);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new LatLng(i, d2, d);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public LatLng[] newArray(int size) {
        return new LatLng[size];
    }
}

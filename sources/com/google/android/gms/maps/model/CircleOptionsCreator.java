package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public class CircleOptionsCreator implements Parcelable.Creator<CircleOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(CircleOptions circleOptions, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, circleOptions.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, (Parcelable) circleOptions.getCenter(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, circleOptions.getRadius());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, circleOptions.getStrokeWidth());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 5, circleOptions.getStrokeColor());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 6, circleOptions.getFillColor());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, circleOptions.getZIndex());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, circleOptions.isVisible());
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public CircleOptions createFromParcel(Parcel parcel) {
        float f = 0.0f;
        boolean z = false;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        LatLng latLng = null;
        double d = Constants.SPLITS_ACCURACY;
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
                    latLng = (LatLng) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i4, LatLng.CREATOR);
                    break;
                case 3:
                    d = com.google.android.gms.common.internal.safeparcel.a.j(parcel, i4);
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
        return new CircleOptions(i3, latLng, d, f2, i2, i, f, z);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public CircleOptions[] newArray(int size) {
        return new CircleOptions[size];
    }
}

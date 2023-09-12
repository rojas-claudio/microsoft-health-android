package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class TileOverlayOptionsCreator implements Parcelable.Creator<TileOverlayOptions> {
    public static final int CONTENT_DESCRIPTION = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(TileOverlayOptions tileOverlayOptions, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, tileOverlayOptions.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, tileOverlayOptions.cP(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, tileOverlayOptions.isVisible());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, tileOverlayOptions.getZIndex());
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public TileOverlayOptions createFromParcel(Parcel parcel) {
        boolean z = false;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        IBinder iBinder = null;
        float f = 0.0f;
        int i = 0;
        while (parcel.dataPosition() < j) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i2);
                    break;
                case 2:
                    iBinder = com.google.android.gms.common.internal.safeparcel.a.m(parcel, i2);
                    break;
                case 3:
                    z = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i2);
                    break;
                case 4:
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
        return new TileOverlayOptions(i, iBinder, z, f);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public TileOverlayOptions[] newArray(int size) {
        return new TileOverlayOptions[size];
    }
}

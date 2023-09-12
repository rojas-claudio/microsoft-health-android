package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class TileCreator implements Parcelable.Creator<Tile> {
    public static final int CONTENT_DESCRIPTION = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(Tile tile, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, tile.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 2, tile.width);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 3, tile.height);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, tile.data, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public Tile createFromParcel(Parcel parcel) {
        int i = 0;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        byte[] bArr = null;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i4)) {
                case 1:
                    i3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 2:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 3:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 4:
                    bArr = com.google.android.gms.common.internal.safeparcel.a.o(parcel, i4);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i4);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new Tile(i3, i2, i, bArr);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public Tile[] newArray(int size) {
        return new Tile[size];
    }
}

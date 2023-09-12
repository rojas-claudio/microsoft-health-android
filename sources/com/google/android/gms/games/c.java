package com.google.android.gms.games;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class c implements Parcelable.Creator<PlayerEntity> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(PlayerEntity playerEntity, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 1, playerEntity.getPlayerId(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1000, playerEntity.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, playerEntity.getDisplayName(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, (Parcelable) playerEntity.getIconImageUri(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, (Parcelable) playerEntity.getHiResImageUri(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, playerEntity.getRetrievedTimestamp());
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: L */
    public PlayerEntity[] newArray(int i) {
        return new PlayerEntity[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: u */
    public PlayerEntity createFromParcel(Parcel parcel) {
        Uri uri = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        long j2 = 0;
        Uri uri2 = null;
        String str = null;
        String str2 = null;
        while (parcel.dataPosition() < j) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 2:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 3:
                    uri2 = (Uri) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i2, Uri.CREATOR);
                    break;
                case 4:
                    uri = (Uri) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i2, Uri.CREATOR);
                    break;
                case 5:
                    j2 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i2);
                    break;
                case 1000:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i2);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new PlayerEntity(i, str2, str, uri2, uri, j2);
    }
}

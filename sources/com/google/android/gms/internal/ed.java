package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class ed implements Parcelable.Creator<ec> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(ec ecVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, ecVar.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, ecVar.bH(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, (Parcelable) ecVar.bI(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: G */
    public ec[] newArray(int i) {
        return new ec[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: s */
    public ec createFromParcel(Parcel parcel) {
        dz dzVar = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        Parcel parcel2 = null;
        while (parcel.dataPosition() < j) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i2);
                    break;
                case 2:
                    parcel2 = com.google.android.gms.common.internal.safeparcel.a.y(parcel, i2);
                    break;
                case 3:
                    dzVar = (dz) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i2, dz.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new ec(i, parcel2, dzVar);
    }
}

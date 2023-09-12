package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.dw;
import com.google.android.gms.internal.dz;
/* loaded from: classes.dex */
public class dy implements Parcelable.Creator<dz.b> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(dz.b bVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, bVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, bVar.lN, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, (Parcelable) bVar.lO, i, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: D */
    public dz.b[] newArray(int i) {
        return new dz.b[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: p */
    public dz.b createFromParcel(Parcel parcel) {
        dw.a aVar = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        String str = null;
        while (parcel.dataPosition() < j) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i2);
                    break;
                case 2:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 3:
                    aVar = (dw.a) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i2, dw.a.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new dz.b(i, str, aVar);
    }
}

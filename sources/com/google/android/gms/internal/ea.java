package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.dz;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class ea implements Parcelable.Creator<dz> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(dz dzVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, dzVar.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.b(parcel, 2, dzVar.bE(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, dzVar.bF(), false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: E */
    public dz[] newArray(int i) {
        return new dz[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: q */
    public dz createFromParcel(Parcel parcel) {
        String str = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        ArrayList arrayList = null;
        while (parcel.dataPosition() < j) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i2);
                    break;
                case 2:
                    arrayList = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i2, dz.a.CREATOR);
                    break;
                case 3:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new dz(i, arrayList, str);
    }
}

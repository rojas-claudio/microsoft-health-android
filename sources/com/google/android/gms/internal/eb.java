package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.dz;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class eb implements Parcelable.Creator<dz.a> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(dz.a aVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, aVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, aVar.className, false);
        com.google.android.gms.common.internal.safeparcel.b.b(parcel, 3, aVar.lM, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: F */
    public dz.a[] newArray(int i) {
        return new dz.a[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: r */
    public dz.a createFromParcel(Parcel parcel) {
        ArrayList arrayList = null;
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
                    arrayList = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i2, dz.b.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new dz.a(i, str, arrayList);
    }
}

package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.dt;
/* loaded from: classes.dex */
public class dv implements Parcelable.Creator<dt.a> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(dt.a aVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, aVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, aVar.lx, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 3, aVar.ly);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: B */
    public dt.a[] newArray(int i) {
        return new dt.a[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: n */
    public dt.a createFromParcel(Parcel parcel) {
        int i = 0;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        String str = null;
        int i2 = 0;
        while (parcel.dataPosition() < j) {
            int i3 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i3)) {
                case 1:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i3);
                    break;
                case 2:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    break;
                case 3:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i3);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i3);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new dt.a(i2, str, i);
    }
}

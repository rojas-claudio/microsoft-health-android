package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.dw;
/* loaded from: classes.dex */
public class dx implements Parcelable.Creator<dw.a> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(dw.a aVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, aVar.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 2, aVar.bn());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, aVar.bt());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 4, aVar.bo());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, aVar.bu());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, aVar.bv(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 7, aVar.bw());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, aVar.by(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, (Parcelable) aVar.bA(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: C */
    public dw.a[] newArray(int i) {
        return new dw.a[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: o */
    public dw.a createFromParcel(Parcel parcel) {
        dr drVar = null;
        int i = 0;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        String str = null;
        String str2 = null;
        boolean z = false;
        int i2 = 0;
        boolean z2 = false;
        int i3 = 0;
        int i4 = 0;
        while (parcel.dataPosition() < j) {
            int i5 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i5)) {
                case 1:
                    i4 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i5);
                    break;
                case 2:
                    i3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i5);
                    break;
                case 3:
                    z2 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i5);
                    break;
                case 4:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i5);
                    break;
                case 5:
                    z = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i5);
                    break;
                case 6:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i5);
                    break;
                case 7:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i5);
                    break;
                case 8:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i5);
                    break;
                case 9:
                    drVar = (dr) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i5, dr.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i5);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new dw.a(i4, i3, z2, i2, z, str2, i, str, drVar);
    }
}

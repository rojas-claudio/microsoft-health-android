package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class y implements Parcelable.Creator<x> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(x xVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, xVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, xVar.ew, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 3, xVar.height);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 4, xVar.heightPixels);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, xVar.ex);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 6, xVar.width);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 7, xVar.widthPixels);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: b */
    public x createFromParcel(Parcel parcel) {
        int i = 0;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        String str = null;
        int i2 = 0;
        boolean z = false;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (parcel.dataPosition() < j) {
            int i6 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i6)) {
                case 1:
                    i5 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i6);
                    break;
                case 2:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i6);
                    break;
                case 3:
                    i4 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i6);
                    break;
                case 4:
                    i3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i6);
                    break;
                case 5:
                    z = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i6);
                    break;
                case 6:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i6);
                    break;
                case 7:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i6);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i6);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new x(i5, str, i4, i3, z, i2, i);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: c */
    public x[] newArray(int i) {
        return new x[i];
    }
}

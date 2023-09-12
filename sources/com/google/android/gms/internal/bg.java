package com.google.android.gms.internal;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class bg implements Parcelable.Creator<bh> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(bh bhVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, bhVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, (Parcelable) bhVar.fR, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, bhVar.U(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, bhVar.V(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, bhVar.W(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, bhVar.X(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, bhVar.fW, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, bhVar.fX);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, bhVar.fY, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 10, bhVar.Y(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 11, bhVar.orientation);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 12, bhVar.ga);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 13, bhVar.fz, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 14, (Parcelable) bhVar.eg, i, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: d */
    public bh createFromParcel(Parcel parcel) {
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        be beVar = null;
        IBinder iBinder = null;
        IBinder iBinder2 = null;
        IBinder iBinder3 = null;
        IBinder iBinder4 = null;
        String str = null;
        boolean z = false;
        String str2 = null;
        IBinder iBinder5 = null;
        int i2 = 0;
        int i3 = 0;
        String str3 = null;
        co coVar = null;
        while (parcel.dataPosition() < j) {
            int i4 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i4)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 2:
                    beVar = (be) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i4, be.CREATOR);
                    break;
                case 3:
                    iBinder = com.google.android.gms.common.internal.safeparcel.a.m(parcel, i4);
                    break;
                case 4:
                    iBinder2 = com.google.android.gms.common.internal.safeparcel.a.m(parcel, i4);
                    break;
                case 5:
                    iBinder3 = com.google.android.gms.common.internal.safeparcel.a.m(parcel, i4);
                    break;
                case 6:
                    iBinder4 = com.google.android.gms.common.internal.safeparcel.a.m(parcel, i4);
                    break;
                case 7:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i4);
                    break;
                case 8:
                    z = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i4);
                    break;
                case 9:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i4);
                    break;
                case 10:
                    iBinder5 = com.google.android.gms.common.internal.safeparcel.a.m(parcel, i4);
                    break;
                case 11:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 12:
                    i3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 13:
                    str3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i4);
                    break;
                case 14:
                    coVar = (co) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i4, co.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i4);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new bh(i, beVar, iBinder, iBinder2, iBinder3, iBinder4, str, z, str2, iBinder5, i2, i3, str3, coVar);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: h */
    public bh[] newArray(int i) {
        return new bh[i];
    }
}

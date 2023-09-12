package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class bd implements Parcelable.Creator<be> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(be beVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, beVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, beVar.fy, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, beVar.fz, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, beVar.mimeType, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, beVar.packageName, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, beVar.fA, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, beVar.fB, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, beVar.fC, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: c */
    public be createFromParcel(Parcel parcel) {
        String str = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        String str7 = null;
        while (parcel.dataPosition() < j) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i2);
                    break;
                case 2:
                    str7 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 3:
                    str6 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 4:
                    str5 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 5:
                    str4 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 6:
                    str3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 7:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 8:
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
        return new be(i, str7, str6, str5, str4, str3, str2, str);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: g */
    public be[] newArray(int i) {
        return new be[i];
    }
}

package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class fp implements Parcelable.Creator<fn> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(fn fnVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 1, fnVar.getAccountName(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1000, fnVar.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, fnVar.cZ(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, fnVar.da(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, fnVar.db(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, fnVar.dc(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, fnVar.dd(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, fnVar.de(), false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, fnVar.df(), false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: A */
    public fn createFromParcel(Parcel parcel) {
        String str = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String[] strArr = null;
        String[] strArr2 = null;
        String[] strArr3 = null;
        String str5 = null;
        while (parcel.dataPosition() < j) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    str5 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 2:
                    strArr3 = com.google.android.gms.common.internal.safeparcel.a.w(parcel, i2);
                    break;
                case 3:
                    strArr2 = com.google.android.gms.common.internal.safeparcel.a.w(parcel, i2);
                    break;
                case 4:
                    strArr = com.google.android.gms.common.internal.safeparcel.a.w(parcel, i2);
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
        return new fn(i, str5, strArr3, strArr2, strArr, str4, str3, str2, str);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: af */
    public fn[] newArray(int i) {
        return new fn[i];
    }
}

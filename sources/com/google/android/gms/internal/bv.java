package com.google.android.gms.internal;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class bv implements Parcelable.Creator<bu> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(bu buVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, buVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, buVar.gA, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, (Parcelable) buVar.gB, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, (Parcelable) buVar.ed, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, buVar.adUnitId, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, (Parcelable) buVar.applicationInfo, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, (Parcelable) buVar.gC, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, buVar.gD, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, buVar.gE, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 10, buVar.gF, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 11, (Parcelable) buVar.eg, i, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: e */
    public bu createFromParcel(Parcel parcel) {
        co coVar = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        String str = null;
        String str2 = null;
        String str3 = null;
        PackageInfo packageInfo = null;
        ApplicationInfo applicationInfo = null;
        String str4 = null;
        x xVar = null;
        v vVar = null;
        Bundle bundle = null;
        while (parcel.dataPosition() < j) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i2);
                    break;
                case 2:
                    bundle = com.google.android.gms.common.internal.safeparcel.a.n(parcel, i2);
                    break;
                case 3:
                    vVar = (v) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i2, v.CREATOR);
                    break;
                case 4:
                    xVar = (x) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i2, x.CREATOR);
                    break;
                case 5:
                    str4 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 6:
                    applicationInfo = (ApplicationInfo) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i2, ApplicationInfo.CREATOR);
                    break;
                case 7:
                    packageInfo = (PackageInfo) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i2, PackageInfo.CREATOR);
                    break;
                case 8:
                    str3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 9:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 10:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 11:
                    coVar = (co) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i2, co.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new bu(i, bundle, vVar, xVar, str4, applicationInfo, packageInfo, str3, str2, str, coVar);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: i */
    public bu[] newArray(int i) {
        return new bu[i];
    }
}

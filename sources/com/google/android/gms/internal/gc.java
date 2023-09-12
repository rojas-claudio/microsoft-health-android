package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.fv;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes.dex */
public class gc implements Parcelable.Creator<fv.d> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(fv.d dVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        Set<Integer> di = dVar.di();
        if (di.contains(1)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, dVar.getVersionCode());
        }
        if (di.contains(2)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, dVar.getFamilyName(), true);
        }
        if (di.contains(3)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, dVar.getFormatted(), true);
        }
        if (di.contains(4)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, dVar.getGivenName(), true);
        }
        if (di.contains(5)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, dVar.getHonorificPrefix(), true);
        }
        if (di.contains(6)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, dVar.getHonorificSuffix(), true);
        }
        if (di.contains(7)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, dVar.getMiddleName(), true);
        }
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: J */
    public fv.d createFromParcel(Parcel parcel) {
        String str = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        HashSet hashSet = new HashSet();
        int i = 0;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        while (parcel.dataPosition() < j) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i2);
                    hashSet.add(1);
                    break;
                case 2:
                    str6 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    hashSet.add(2);
                    break;
                case 3:
                    str5 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    hashSet.add(3);
                    break;
                case 4:
                    str4 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    hashSet.add(4);
                    break;
                case 5:
                    str3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    hashSet.add(5);
                    break;
                case 6:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    hashSet.add(6);
                    break;
                case 7:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    hashSet.add(7);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new fv.d(hashSet, i, str6, str5, str4, str3, str2, str);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: ao */
    public fv.d[] newArray(int i) {
        return new fv.d[i];
    }
}

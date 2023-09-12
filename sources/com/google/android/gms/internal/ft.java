package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes.dex */
public class ft implements Parcelable.Creator<fs> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(fs fsVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        Set<Integer> di = fsVar.di();
        if (di.contains(1)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, fsVar.getVersionCode());
        }
        if (di.contains(2)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, fsVar.getId(), true);
        }
        if (di.contains(4)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, (Parcelable) fsVar.dz(), i, true);
        }
        if (di.contains(5)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, fsVar.getStartDate(), true);
        }
        if (di.contains(6)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, (Parcelable) fsVar.dA(), i, true);
        }
        if (di.contains(7)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, fsVar.getType(), true);
        }
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: C */
    public fs createFromParcel(Parcel parcel) {
        String str = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        HashSet hashSet = new HashSet();
        int i = 0;
        fq fqVar = null;
        String str2 = null;
        fq fqVar2 = null;
        String str3 = null;
        while (parcel.dataPosition() < j) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i2);
                    hashSet.add(1);
                    break;
                case 2:
                    str3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    hashSet.add(2);
                    break;
                case 3:
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    break;
                case 4:
                    hashSet.add(4);
                    fqVar2 = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i2, fq.CREATOR);
                    break;
                case 5:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    hashSet.add(5);
                    break;
                case 6:
                    hashSet.add(6);
                    fqVar = (fq) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i2, fq.CREATOR);
                    break;
                case 7:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    hashSet.add(7);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new fs(hashSet, i, str3, fqVar2, str2, fqVar, str);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: ah */
    public fs[] newArray(int i) {
        return new fs[i];
    }
}

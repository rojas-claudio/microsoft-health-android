package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.fv;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes.dex */
public class gd implements Parcelable.Creator<fv.f> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(fv.f fVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        Set<Integer> di = fVar.di();
        if (di.contains(1)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, fVar.getVersionCode());
        }
        if (di.contains(2)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, fVar.getDepartment(), true);
        }
        if (di.contains(3)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, fVar.getDescription(), true);
        }
        if (di.contains(4)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, fVar.getEndDate(), true);
        }
        if (di.contains(5)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, fVar.getLocation(), true);
        }
        if (di.contains(6)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, fVar.getName(), true);
        }
        if (di.contains(7)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, fVar.isPrimary());
        }
        if (di.contains(8)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, fVar.getStartDate(), true);
        }
        if (di.contains(9)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, fVar.getTitle(), true);
        }
        if (di.contains(10)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 10, fVar.getType());
        }
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: K */
    public fv.f createFromParcel(Parcel parcel) {
        int i = 0;
        String str = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        HashSet hashSet = new HashSet();
        String str2 = null;
        boolean z = false;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        String str7 = null;
        int i2 = 0;
        while (parcel.dataPosition() < j) {
            int i3 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i3)) {
                case 1:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i3);
                    hashSet.add(1);
                    break;
                case 2:
                    str7 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(2);
                    break;
                case 3:
                    str6 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(3);
                    break;
                case 4:
                    str5 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(4);
                    break;
                case 5:
                    str4 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(5);
                    break;
                case 6:
                    str3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(6);
                    break;
                case 7:
                    z = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i3);
                    hashSet.add(7);
                    break;
                case 8:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(8);
                    break;
                case 9:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    hashSet.add(9);
                    break;
                case 10:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i3);
                    hashSet.add(10);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i3);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new fv.f(hashSet, i2, str7, str6, str5, str4, str3, z, str2, str, i);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: ap */
    public fv.f[] newArray(int i) {
        return new fv.f[i];
    }
}

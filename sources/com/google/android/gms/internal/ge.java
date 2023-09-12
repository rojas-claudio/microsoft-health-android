package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.fv;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes.dex */
public class ge implements Parcelable.Creator<fv.g> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(fv.g gVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        Set<Integer> di = gVar.di();
        if (di.contains(1)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, gVar.getVersionCode());
        }
        if (di.contains(2)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, gVar.isPrimary());
        }
        if (di.contains(3)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, gVar.getValue(), true);
        }
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: L */
    public fv.g createFromParcel(Parcel parcel) {
        boolean z = false;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        HashSet hashSet = new HashSet();
        String str = null;
        int i = 0;
        while (parcel.dataPosition() < j) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i2);
                    hashSet.add(1);
                    break;
                case 2:
                    z = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i2);
                    hashSet.add(2);
                    break;
                case 3:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    hashSet.add(3);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new fv.g(hashSet, i, z, str);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: aq */
    public fv.g[] newArray(int i) {
        return new fv.g[i];
    }
}

package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.fv;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes.dex */
public class gf implements Parcelable.Creator<fv.h> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(fv.h hVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        Set<Integer> di = hVar.di();
        if (di.contains(1)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, hVar.getVersionCode());
        }
        if (di.contains(3)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 3, hVar.dV());
        }
        if (di.contains(4)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, hVar.getValue(), true);
        }
        if (di.contains(5)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, hVar.getLabel(), true);
        }
        if (di.contains(6)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 6, hVar.getType());
        }
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: M */
    public fv.h createFromParcel(Parcel parcel) {
        String str = null;
        int i = 0;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        HashSet hashSet = new HashSet();
        int i2 = 0;
        String str2 = null;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i4)) {
                case 1:
                    i3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    hashSet.add(1);
                    break;
                case 2:
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i4);
                    break;
                case 3:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    hashSet.add(3);
                    break;
                case 4:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i4);
                    hashSet.add(4);
                    break;
                case 5:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i4);
                    hashSet.add(5);
                    break;
                case 6:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    hashSet.add(6);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new fv.h(hashSet, i3, str2, i2, str, i);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: ar */
    public fv.h[] newArray(int i) {
        return new fv.h[i];
    }
}

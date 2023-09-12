package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.fv;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes.dex */
public class fy implements Parcelable.Creator<fv.b> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(fv.b bVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        Set<Integer> di = bVar.di();
        if (di.contains(1)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, bVar.getVersionCode());
        }
        if (di.contains(2)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, (Parcelable) bVar.dM(), i, true);
        }
        if (di.contains(3)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, (Parcelable) bVar.dN(), i, true);
        }
        if (di.contains(4)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 4, bVar.getLayout());
        }
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: F */
    public fv.b createFromParcel(Parcel parcel) {
        fv.b.C0033b c0033b = null;
        int i = 0;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        HashSet hashSet = new HashSet();
        fv.b.a aVar = null;
        int i2 = 0;
        while (parcel.dataPosition() < j) {
            int i3 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i3)) {
                case 1:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i3);
                    hashSet.add(1);
                    break;
                case 2:
                    hashSet.add(2);
                    aVar = (fv.b.a) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i3, fv.b.a.CREATOR);
                    break;
                case 3:
                    hashSet.add(3);
                    c0033b = (fv.b.C0033b) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i3, fv.b.C0033b.CREATOR);
                    break;
                case 4:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i3);
                    hashSet.add(4);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i3);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new fv.b(hashSet, i2, aVar, c0033b, i);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: ak */
    public fv.b[] newArray(int i) {
        return new fv.b[i];
    }
}

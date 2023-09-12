package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.fv;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes.dex */
public class ga implements Parcelable.Creator<fv.b.C0033b> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(fv.b.C0033b c0033b, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        Set<Integer> di = c0033b.di();
        if (di.contains(1)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, c0033b.getVersionCode());
        }
        if (di.contains(2)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 2, c0033b.getHeight());
        }
        if (di.contains(3)) {
            com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, c0033b.getUrl(), true);
        }
        if (di.contains(4)) {
            com.google.android.gms.common.internal.safeparcel.b.c(parcel, 4, c0033b.getWidth());
        }
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: H */
    public fv.b.C0033b createFromParcel(Parcel parcel) {
        int i = 0;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        HashSet hashSet = new HashSet();
        String str = null;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i4)) {
                case 1:
                    i3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    hashSet.add(1);
                    break;
                case 2:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    hashSet.add(2);
                    break;
                case 3:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i4);
                    hashSet.add(3);
                    break;
                case 4:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    hashSet.add(4);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i4);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new fv.b.C0033b(hashSet, i3, i2, str, i);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: am */
    public fv.b.C0033b[] newArray(int i) {
        return new fv.b.C0033b[i];
    }
}

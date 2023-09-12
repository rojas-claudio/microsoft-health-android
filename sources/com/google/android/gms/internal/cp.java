package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class cp implements Parcelable.Creator<co> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(co coVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, coVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, coVar.hP, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 3, coVar.hQ);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 4, coVar.hR);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, coVar.hS);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: g */
    public co createFromParcel(Parcel parcel) {
        boolean z = false;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        String str = null;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i4)) {
                case 1:
                    i3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 2:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i4);
                    break;
                case 3:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 4:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 5:
                    z = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i4);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i4);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new co(i3, str, i2, i, z);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: l */
    public co[] newArray(int i) {
        return new co[i];
    }
}

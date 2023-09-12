package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.internal.dt;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class du implements Parcelable.Creator<dt> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(dt dtVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, dtVar.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.b(parcel, 2, dtVar.bm(), false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: A */
    public dt[] newArray(int i) {
        return new dt[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: m */
    public dt createFromParcel(Parcel parcel) {
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        ArrayList arrayList = null;
        while (parcel.dataPosition() < j) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i2);
                    break;
                case 2:
                    arrayList = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i2, dt.a.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new dt(i, arrayList);
    }
}

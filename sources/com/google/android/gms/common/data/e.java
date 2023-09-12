package com.google.android.gms.common.data;

import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class e implements Parcelable.Creator<d> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(d dVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 1, dVar.aK(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1000, dVar.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, (Parcelable[]) dVar.aL(), i, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 3, dVar.getStatusCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, dVar.aM(), false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: h */
    public d createFromParcel(Parcel parcel) {
        int i = 0;
        Bundle bundle = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        CursorWindow[] cursorWindowArr = null;
        String[] strArr = null;
        int i2 = 0;
        while (parcel.dataPosition() < j) {
            int i3 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i3)) {
                case 1:
                    strArr = com.google.android.gms.common.internal.safeparcel.a.w(parcel, i3);
                    break;
                case 2:
                    cursorWindowArr = (CursorWindow[]) com.google.android.gms.common.internal.safeparcel.a.b(parcel, i3, CursorWindow.CREATOR);
                    break;
                case 3:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i3);
                    break;
                case 4:
                    bundle = com.google.android.gms.common.internal.safeparcel.a.n(parcel, i3);
                    break;
                case 1000:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i3);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i3);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        d dVar = new d(i2, strArr, cursorWindowArr, i, bundle);
        dVar.aJ();
        return dVar;
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: s */
    public d[] newArray(int i) {
        return new d[i];
    }
}

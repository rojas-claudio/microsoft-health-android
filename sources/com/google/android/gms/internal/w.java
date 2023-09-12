package com.google.android.gms.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class w implements Parcelable.Creator<v> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(v vVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, vVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, vVar.es);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, vVar.extras, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 4, vVar.et);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, vVar.eu, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, vVar.ev);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 7, vVar.tagForChildDirectedTreatment);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a */
    public v createFromParcel(Parcel parcel) {
        ArrayList<String> arrayList = null;
        int i = 0;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        long j2 = 0;
        boolean z = false;
        int i2 = 0;
        Bundle bundle = null;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i4)) {
                case 1:
                    i3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 2:
                    j2 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i4);
                    break;
                case 3:
                    bundle = com.google.android.gms.common.internal.safeparcel.a.n(parcel, i4);
                    break;
                case 4:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 5:
                    arrayList = com.google.android.gms.common.internal.safeparcel.a.x(parcel, i4);
                    break;
                case 6:
                    z = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i4);
                    break;
                case 7:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i4);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new v(i3, j2, bundle, i2, arrayList, z, i);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: b */
    public v[] newArray(int i) {
        return new v[i];
    }
}

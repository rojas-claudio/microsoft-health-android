package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class bx implements Parcelable.Creator<bw> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(bw bwVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, bwVar.versionCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, bwVar.fW, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, bwVar.gG, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, bwVar.eW, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 5, bwVar.errorCode);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, bwVar.eX, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, bwVar.gH);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, bwVar.gI);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, bwVar.gJ);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 10, bwVar.gK, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 11, bwVar.fa);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 12, bwVar.orientation);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: f */
    public bw createFromParcel(Parcel parcel) {
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        String str = null;
        String str2 = null;
        ArrayList<String> arrayList = null;
        int i2 = 0;
        ArrayList<String> arrayList2 = null;
        long j2 = 0;
        boolean z = false;
        long j3 = 0;
        ArrayList<String> arrayList3 = null;
        long j4 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i4)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 2:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i4);
                    break;
                case 3:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i4);
                    break;
                case 4:
                    arrayList = com.google.android.gms.common.internal.safeparcel.a.x(parcel, i4);
                    break;
                case 5:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 6:
                    arrayList2 = com.google.android.gms.common.internal.safeparcel.a.x(parcel, i4);
                    break;
                case 7:
                    j2 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i4);
                    break;
                case 8:
                    z = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i4);
                    break;
                case 9:
                    j3 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i4);
                    break;
                case 10:
                    arrayList3 = com.google.android.gms.common.internal.safeparcel.a.x(parcel, i4);
                    break;
                case 11:
                    j4 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i4);
                    break;
                case 12:
                    i3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i4);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new bw(i, str, str2, arrayList, i2, arrayList2, j2, z, j3, arrayList3, j4, i3);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: j */
    public bw[] newArray(int i) {
        return new bw[i];
    }
}

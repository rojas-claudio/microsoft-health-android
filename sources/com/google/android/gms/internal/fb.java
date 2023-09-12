package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public class fb implements Parcelable.Creator<fa> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(fa faVar, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 1, faVar.getRequestId(), false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1000, faVar.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, faVar.getExpirationTime());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, faVar.co());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, faVar.getLatitude());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, faVar.getLongitude());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, faVar.cp());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 7, faVar.cq());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 8, faVar.getNotificationResponsiveness());
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 9, faVar.cr());
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: ac */
    public fa[] newArray(int i) {
        return new fa[i];
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: z */
    public fa createFromParcel(Parcel parcel) {
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        String str = null;
        int i2 = 0;
        short s = 0;
        double d = Constants.SPLITS_ACCURACY;
        double d2 = Constants.SPLITS_ACCURACY;
        float f = 0.0f;
        long j2 = 0;
        int i3 = 0;
        int i4 = -1;
        while (parcel.dataPosition() < j) {
            int i5 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i5)) {
                case 1:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i5);
                    break;
                case 2:
                    j2 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i5);
                    break;
                case 3:
                    s = com.google.android.gms.common.internal.safeparcel.a.e(parcel, i5);
                    break;
                case 4:
                    d = com.google.android.gms.common.internal.safeparcel.a.j(parcel, i5);
                    break;
                case 5:
                    d2 = com.google.android.gms.common.internal.safeparcel.a.j(parcel, i5);
                    break;
                case 6:
                    f = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i5);
                    break;
                case 7:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i5);
                    break;
                case 8:
                    i3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i5);
                    break;
                case 9:
                    i4 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i5);
                    break;
                case 1000:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i5);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i5);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new fa(i, str, i2, s, d, d2, f, j2, i3, i4);
    }
}

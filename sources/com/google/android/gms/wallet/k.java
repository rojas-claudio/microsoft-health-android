package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class k implements Parcelable.Creator<ProxyCard> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(ProxyCard proxyCard, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, proxyCard.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, proxyCard.um, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, proxyCard.un, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 4, proxyCard.uo);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 5, proxyCard.up);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: X */
    public ProxyCard createFromParcel(Parcel parcel) {
        String str = null;
        int i = 0;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i2 = 0;
        String str2 = null;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i4)) {
                case 1:
                    i3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 2:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i4);
                    break;
                case 3:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i4);
                    break;
                case 4:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 5:
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
        return new ProxyCard(i3, str2, str, i2, i);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: aC */
    public ProxyCard[] newArray(int i) {
        return new ProxyCard[i];
    }
}

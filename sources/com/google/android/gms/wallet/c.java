package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class c implements Parcelable.Creator<FullWallet> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(FullWallet fullWallet, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, fullWallet.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, fullWallet.tH, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, fullWallet.tI, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, (Parcelable) fullWallet.tJ, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, fullWallet.tK, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, (Parcelable) fullWallet.tL, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, (Parcelable) fullWallet.tM, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, fullWallet.tN, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: P */
    public FullWallet createFromParcel(Parcel parcel) {
        String[] strArr = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        Address address = null;
        Address address2 = null;
        String str = null;
        ProxyCard proxyCard = null;
        String str2 = null;
        String str3 = null;
        while (parcel.dataPosition() < j) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i2);
                    break;
                case 2:
                    str3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 3:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 4:
                    proxyCard = (ProxyCard) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i2, ProxyCard.CREATOR);
                    break;
                case 5:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 6:
                    address2 = (Address) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i2, Address.CREATOR);
                    break;
                case 7:
                    address = (Address) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i2, Address.CREATOR);
                    break;
                case 8:
                    strArr = com.google.android.gms.common.internal.safeparcel.a.w(parcel, i2);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new FullWallet(i, str3, str2, proxyCard, str, address2, address, strArr);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: au */
    public FullWallet[] newArray(int i) {
        return new FullWallet[i];
    }
}

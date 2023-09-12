package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class g implements Parcelable.Creator<MaskedWallet> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(MaskedWallet maskedWallet, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, maskedWallet.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, maskedWallet.tH, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, maskedWallet.tI, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, maskedWallet.tN, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, maskedWallet.tK, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, (Parcelable) maskedWallet.tL, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, (Parcelable) maskedWallet.tM, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, (Parcelable[]) maskedWallet.tZ, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, (Parcelable[]) maskedWallet.ua, i, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: T */
    public MaskedWallet createFromParcel(Parcel parcel) {
        OfferWalletObject[] offerWalletObjectArr = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        LoyaltyWalletObject[] loyaltyWalletObjectArr = null;
        Address address = null;
        Address address2 = null;
        String str = null;
        String[] strArr = null;
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
                    strArr = com.google.android.gms.common.internal.safeparcel.a.w(parcel, i2);
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
                    loyaltyWalletObjectArr = (LoyaltyWalletObject[]) com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2, LoyaltyWalletObject.CREATOR);
                    break;
                case 9:
                    offerWalletObjectArr = (OfferWalletObject[]) com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2, OfferWalletObject.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new MaskedWallet(i, str3, str2, strArr, str, address2, address, loyaltyWalletObjectArr, offerWalletObjectArr);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: ay */
    public MaskedWallet[] newArray(int i) {
        return new MaskedWallet[i];
    }
}

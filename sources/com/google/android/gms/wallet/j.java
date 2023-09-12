package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class j implements Parcelable.Creator<OfferWalletObject> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(OfferWalletObject offerWalletObject, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, offerWalletObject.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, offerWalletObject.tU, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, offerWalletObject.ul, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: W */
    public OfferWalletObject createFromParcel(Parcel parcel) {
        String str = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        String str2 = null;
        while (parcel.dataPosition() < j) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i2);
                    break;
                case 2:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 3:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new OfferWalletObject(i, str2, str);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: aB */
    public OfferWalletObject[] newArray(int i) {
        return new OfferWalletObject[i];
    }
}

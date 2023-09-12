package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class f implements Parcelable.Creator<LoyaltyWalletObject> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(LoyaltyWalletObject loyaltyWalletObject, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, loyaltyWalletObject.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, loyaltyWalletObject.tU, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, loyaltyWalletObject.tV, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, loyaltyWalletObject.tW, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, loyaltyWalletObject.tX, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, loyaltyWalletObject.tY, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: S */
    public LoyaltyWalletObject createFromParcel(Parcel parcel) {
        String str = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        while (parcel.dataPosition() < j) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i2);
                    break;
                case 2:
                    str5 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 3:
                    str4 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 4:
                    str3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 5:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 6:
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
        return new LoyaltyWalletObject(i, str5, str4, str3, str2, str);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: ax */
    public LoyaltyWalletObject[] newArray(int i) {
        return new LoyaltyWalletObject[i];
    }
}

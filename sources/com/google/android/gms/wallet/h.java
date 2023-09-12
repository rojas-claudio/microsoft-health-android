package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class h implements Parcelable.Creator<MaskedWalletRequest> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(MaskedWalletRequest maskedWalletRequest, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, maskedWalletRequest.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, maskedWalletRequest.tI, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, maskedWalletRequest.ub);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, maskedWalletRequest.uc);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, maskedWalletRequest.ud);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, maskedWalletRequest.ue, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, maskedWalletRequest.tE, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, maskedWalletRequest.uf, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, (Parcelable) maskedWalletRequest.tO, i, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 10, maskedWalletRequest.ug);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 11, maskedWalletRequest.uh);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: U */
    public MaskedWalletRequest createFromParcel(Parcel parcel) {
        Cart cart = null;
        boolean z = false;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        boolean z2 = false;
        String str = null;
        String str2 = null;
        String str3 = null;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = false;
        String str4 = null;
        int i = 0;
        while (parcel.dataPosition() < j) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i2);
                    break;
                case 2:
                    str4 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 3:
                    z5 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i2);
                    break;
                case 4:
                    z4 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i2);
                    break;
                case 5:
                    z3 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i2);
                    break;
                case 6:
                    str3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 7:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 8:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 9:
                    cart = (Cart) com.google.android.gms.common.internal.safeparcel.a.a(parcel, i2, Cart.CREATOR);
                    break;
                case 10:
                    z2 = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i2);
                    break;
                case 11:
                    z = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i2);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new MaskedWalletRequest(i, str4, z5, z4, z3, str3, str2, str, cart, z2, z);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: az */
    public MaskedWalletRequest[] newArray(int i) {
        return new MaskedWalletRequest[i];
    }
}

package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class b implements Parcelable.Creator<Cart> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(Cart cart, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, cart.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, cart.tD, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, cart.tE, false);
        com.google.android.gms.common.internal.safeparcel.b.b(parcel, 4, cart.tF, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: O */
    public Cart createFromParcel(Parcel parcel) {
        String str = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        ArrayList arrayList = new ArrayList();
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
                case 4:
                    arrayList = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i2, LineItem.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new Cart(i, str2, str, arrayList);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: at */
    public Cart[] newArray(int i) {
        return new Cart[i];
    }
}

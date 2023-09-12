package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class a implements Parcelable.Creator<Address> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(Address address, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, address.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, address.name, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, address.tu, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, address.tv, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, address.tw, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 6, address.hl, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, address.tx, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 8, address.ty, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 9, address.tz, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 10, address.tA, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 11, address.tB);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 12, address.tC, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: N */
    public Address createFromParcel(Parcel parcel) {
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        String str = null;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        String str7 = null;
        String str8 = null;
        String str9 = null;
        boolean z = false;
        String str10 = null;
        while (parcel.dataPosition() < j) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i2);
                    break;
                case 2:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 3:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 4:
                    str3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 5:
                    str4 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 6:
                    str5 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 7:
                    str6 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 8:
                    str7 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 9:
                    str8 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 10:
                    str9 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                case 11:
                    z = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i2);
                    break;
                case 12:
                    str10 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i2);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new Address(i, str, str2, str3, str4, str5, str6, str7, str8, str9, z, str10);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: as */
    public Address[] newArray(int i) {
        return new Address[i];
    }
}

package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class e implements Parcelable.Creator<LineItem> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(LineItem lineItem, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, lineItem.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, lineItem.description, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 3, lineItem.tQ, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, lineItem.tR, false);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 5, lineItem.tD, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 6, lineItem.tS);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 7, lineItem.tE, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: R */
    public LineItem createFromParcel(Parcel parcel) {
        int i = 0;
        String str = null;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        int i2 = 0;
        while (parcel.dataPosition() < j) {
            int i3 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i3)) {
                case 1:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i3);
                    break;
                case 2:
                    str5 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    break;
                case 3:
                    str4 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    break;
                case 4:
                    str3 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    break;
                case 5:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    break;
                case 6:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i3);
                    break;
                case 7:
                    str = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i3);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new LineItem(i2, str5, str4, str3, str2, i, str);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: aw */
    public LineItem[] newArray(int i) {
        return new LineItem[i];
    }
}

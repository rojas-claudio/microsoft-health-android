package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
/* loaded from: classes.dex */
public class i implements Parcelable.Creator<NotifyTransactionStatusRequest> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(NotifyTransactionStatusRequest notifyTransactionStatusRequest, Parcel parcel, int i) {
        int k = com.google.android.gms.common.internal.safeparcel.b.k(parcel);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 1, notifyTransactionStatusRequest.iM);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 2, notifyTransactionStatusRequest.tH, false);
        com.google.android.gms.common.internal.safeparcel.b.c(parcel, 3, notifyTransactionStatusRequest.status);
        com.google.android.gms.common.internal.safeparcel.b.a(parcel, 4, notifyTransactionStatusRequest.uj, false);
        com.google.android.gms.common.internal.safeparcel.b.C(parcel, k);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: V */
    public NotifyTransactionStatusRequest createFromParcel(Parcel parcel) {
        String str = null;
        int i = 0;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        String str2 = null;
        int i2 = 0;
        while (parcel.dataPosition() < j) {
            int i3 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i3)) {
                case 1:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i3);
                    break;
                case 2:
                    str2 = com.google.android.gms.common.internal.safeparcel.a.l(parcel, i3);
                    break;
                case 3:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i3);
                    break;
                case 4:
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
        return new NotifyTransactionStatusRequest(i2, str2, i, str);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: aA */
    public NotifyTransactionStatusRequest[] newArray(int i) {
        return new NotifyTransactionStatusRequest[i];
    }
}

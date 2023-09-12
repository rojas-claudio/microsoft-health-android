package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
/* loaded from: classes.dex */
public class LocationRequestCreator implements Parcelable.Creator<LocationRequest> {
    public static final int CONTENT_DESCRIPTION = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(LocationRequest locationRequest, Parcel parcel, int i) {
        int k = b.k(parcel);
        b.c(parcel, 1, locationRequest.mPriority);
        b.c(parcel, 1000, locationRequest.getVersionCode());
        b.a(parcel, 2, locationRequest.oJ);
        b.a(parcel, 3, locationRequest.oK);
        b.a(parcel, 4, locationRequest.oL);
        b.a(parcel, 5, locationRequest.oC);
        b.c(parcel, 6, locationRequest.oM);
        b.a(parcel, 7, locationRequest.oN);
        b.C(parcel, k);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public LocationRequest createFromParcel(Parcel parcel) {
        boolean z = false;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 102;
        long j2 = 3600000;
        long j3 = 600000;
        long j4 = Long.MAX_VALUE;
        int i2 = Integer.MAX_VALUE;
        float f = 0.0f;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i4)) {
                case 1:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 2:
                    j2 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i4);
                    break;
                case 3:
                    j3 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i4);
                    break;
                case 4:
                    z = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i4);
                    break;
                case 5:
                    j4 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i4);
                    break;
                case 6:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 7:
                    f = com.google.android.gms.common.internal.safeparcel.a.i(parcel, i4);
                    break;
                case 1000:
                    i3 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i4);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new LocationRequest(i3, i, j2, j3, z, j4, i2, f);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public LocationRequest[] newArray(int size) {
        return new LocationRequest[size];
    }
}

package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
/* loaded from: classes.dex */
public class DetectedActivityCreator implements Parcelable.Creator<DetectedActivity> {
    public static final int CONTENT_DESCRIPTION = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(DetectedActivity detectedActivity, Parcel parcel, int i) {
        int k = b.k(parcel);
        b.c(parcel, 1, detectedActivity.oy);
        b.c(parcel, 1000, detectedActivity.getVersionCode());
        b.c(parcel, 2, detectedActivity.oz);
        b.C(parcel, k);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public DetectedActivity createFromParcel(Parcel parcel) {
        int i = 0;
        int j = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < j) {
            int i4 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i4)) {
                case 1:
                    i2 = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
                    break;
                case 2:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i4);
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
        return new DetectedActivity(i3, i2, i);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public DetectedActivity[] newArray(int size) {
        return new DetectedActivity[size];
    }
}

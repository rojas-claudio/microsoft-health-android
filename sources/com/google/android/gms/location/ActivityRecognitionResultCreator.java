package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class ActivityRecognitionResultCreator implements Parcelable.Creator<ActivityRecognitionResult> {
    public static final int CONTENT_DESCRIPTION = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(ActivityRecognitionResult activityRecognitionResult, Parcel parcel, int i) {
        int k = b.k(parcel);
        b.b(parcel, 1, activityRecognitionResult.ov, false);
        b.c(parcel, 1000, activityRecognitionResult.getVersionCode());
        b.a(parcel, 2, activityRecognitionResult.ow);
        b.a(parcel, 3, activityRecognitionResult.ox);
        b.C(parcel, k);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public ActivityRecognitionResult createFromParcel(Parcel parcel) {
        long j = 0;
        int j2 = com.google.android.gms.common.internal.safeparcel.a.j(parcel);
        int i = 0;
        ArrayList arrayList = null;
        long j3 = 0;
        while (parcel.dataPosition() < j2) {
            int i2 = com.google.android.gms.common.internal.safeparcel.a.i(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.a.y(i2)) {
                case 1:
                    arrayList = com.google.android.gms.common.internal.safeparcel.a.c(parcel, i2, DetectedActivity.CREATOR);
                    break;
                case 2:
                    j3 = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i2);
                    break;
                case 3:
                    j = com.google.android.gms.common.internal.safeparcel.a.g(parcel, i2);
                    break;
                case 1000:
                    i = com.google.android.gms.common.internal.safeparcel.a.f(parcel, i2);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.a.b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() != j2) {
            throw new a.C0002a("Overread allowed size end=" + j2, parcel);
        }
        return new ActivityRecognitionResult(i, arrayList, j3, j);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public ActivityRecognitionResult[] newArray(int size) {
        return new ActivityRecognitionResult[size];
    }
}

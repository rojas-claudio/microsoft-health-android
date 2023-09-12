package com.google.android.gms.auth;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
/* loaded from: classes.dex */
public class RecoveryDecisionCreator implements Parcelable.Creator<RecoveryDecision> {
    public static final int CONTENT_DESCRIPTION = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(RecoveryDecision recoveryDecision, Parcel parcel, int i) {
        int k = b.k(parcel);
        b.c(parcel, 1, recoveryDecision.iM);
        b.a(parcel, 2, (Parcelable) recoveryDecision.recoveryIntent, i, false);
        b.a(parcel, 3, recoveryDecision.showRecoveryInterstitial);
        b.a(parcel, 4, recoveryDecision.isRecoveryInfoNeeded);
        b.a(parcel, 5, recoveryDecision.isRecoveryInterstitialAllowed);
        b.a(parcel, 6, (Parcelable) recoveryDecision.recoveryIntentWithoutIntro, i, false);
        b.C(parcel, k);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public RecoveryDecision createFromParcel(Parcel parcel) {
        PendingIntent pendingIntent = null;
        boolean z = false;
        int j = a.j(parcel);
        boolean z2 = false;
        boolean z3 = false;
        PendingIntent pendingIntent2 = null;
        int i = 0;
        while (parcel.dataPosition() < j) {
            int i2 = a.i(parcel);
            switch (a.y(i2)) {
                case 1:
                    i = a.f(parcel, i2);
                    break;
                case 2:
                    pendingIntent2 = (PendingIntent) a.a(parcel, i2, PendingIntent.CREATOR);
                    break;
                case 3:
                    z3 = a.c(parcel, i2);
                    break;
                case 4:
                    z2 = a.c(parcel, i2);
                    break;
                case 5:
                    z = a.c(parcel, i2);
                    break;
                case 6:
                    pendingIntent = (PendingIntent) a.a(parcel, i2, PendingIntent.CREATOR);
                    break;
                default:
                    a.b(parcel, i2);
                    break;
            }
        }
        if (parcel.dataPosition() != j) {
            throw new a.C0002a("Overread allowed size end=" + j, parcel);
        }
        return new RecoveryDecision(i, pendingIntent2, z3, z2, z, pendingIntent);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.Parcelable.Creator
    public RecoveryDecision[] newArray(int size) {
        return new RecoveryDecision[size];
    }
}

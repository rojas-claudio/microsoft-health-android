package com.google.android.gms.auth;

import android.app.PendingIntent;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
/* loaded from: classes.dex */
public class RecoveryDecision implements SafeParcelable {
    public static final RecoveryDecisionCreator CREATOR = new RecoveryDecisionCreator();
    final int iM;
    public boolean isRecoveryInfoNeeded;
    public boolean isRecoveryInterstitialAllowed;
    public PendingIntent recoveryIntent;
    public PendingIntent recoveryIntentWithoutIntro;
    public boolean showRecoveryInterstitial;

    public RecoveryDecision() {
        this.iM = 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RecoveryDecision(int versionCode, PendingIntent recoveryIntent, boolean showRecoveryInterstitial, boolean isRecoveryInfoNeeded, boolean isRecoveryInterstitialAllowed, PendingIntent recoveryIntentWithoutIntro) {
        this.iM = versionCode;
        this.recoveryIntent = recoveryIntent;
        this.showRecoveryInterstitial = showRecoveryInterstitial;
        this.isRecoveryInfoNeeded = isRecoveryInfoNeeded;
        this.isRecoveryInterstitialAllowed = isRecoveryInterstitialAllowed;
        this.recoveryIntentWithoutIntro = recoveryIntentWithoutIntro;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getVersionCode() {
        return this.iM;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        RecoveryDecisionCreator.a(this, out, flags);
    }
}

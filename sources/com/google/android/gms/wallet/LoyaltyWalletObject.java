package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
/* loaded from: classes.dex */
public class LoyaltyWalletObject implements SafeParcelable {
    public static final Parcelable.Creator<LoyaltyWalletObject> CREATOR = new f();
    private final int iM;
    String tU;
    String tV;
    String tW;
    String tX;
    String tY;

    public LoyaltyWalletObject() {
        this.iM = 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LoyaltyWalletObject(int versionCode, String id, String accountId, String issuerName, String programName, String accountName) {
        this.iM = versionCode;
        this.tU = id;
        this.tV = accountId;
        this.tW = issuerName;
        this.tX = programName;
        this.tY = accountName;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getAccountId() {
        return this.tV;
    }

    public String getAccountName() {
        return this.tY;
    }

    public String getId() {
        return this.tU;
    }

    public String getIssuerName() {
        return this.tW;
    }

    public String getProgramName() {
        return this.tX;
    }

    public int getVersionCode() {
        return this.iM;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        f.a(this, dest, flags);
    }
}

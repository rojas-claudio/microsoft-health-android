package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
/* loaded from: classes.dex */
public class OfferWalletObject implements SafeParcelable {
    public static final Parcelable.Creator<OfferWalletObject> CREATOR = new j();
    private final int iM;
    String tU;
    String ul;

    public OfferWalletObject() {
        this.iM = 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OfferWalletObject(int versionCode, String id, String redemptionCode) {
        this.iM = versionCode;
        this.tU = id;
        this.ul = redemptionCode;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return this.tU;
    }

    public String getRedemptionCode() {
        return this.ul;
    }

    public int getVersionCode() {
        return this.iM;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        j.a(this, dest, flags);
    }
}

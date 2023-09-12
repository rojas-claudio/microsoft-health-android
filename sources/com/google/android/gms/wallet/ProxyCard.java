package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
/* loaded from: classes.dex */
public final class ProxyCard implements SafeParcelable {
    public static final Parcelable.Creator<ProxyCard> CREATOR = new k();
    private final int iM;
    String um;
    String un;
    int uo;
    int up;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProxyCard(int versionCode, String pan, String cvn, int expirationMonth, int expirationYear) {
        this.iM = versionCode;
        this.um = pan;
        this.un = cvn;
        this.uo = expirationMonth;
        this.up = expirationYear;
    }

    public ProxyCard(String pan, String cvn, int expirationMonth, int expirationYear) {
        this.iM = 1;
        this.um = pan;
        this.un = cvn;
        this.uo = expirationMonth;
        this.up = expirationYear;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getCvn() {
        return this.un;
    }

    public int getExpirationMonth() {
        return this.uo;
    }

    public int getExpirationYear() {
        return this.up;
    }

    public String getPan() {
        return this.um;
    }

    public int getVersionCode() {
        return this.iM;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        k.a(this, out, flags);
    }
}

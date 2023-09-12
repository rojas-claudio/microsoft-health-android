package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
/* loaded from: classes.dex */
public final class Address implements SafeParcelable {
    public static final Parcelable.Creator<Address> CREATOR = new a();
    String hl;
    private final int iM;
    String name;
    String tA;
    boolean tB;
    String tC;
    String tu;
    String tv;
    String tw;
    String tx;
    String ty;
    String tz;

    public Address() {
        this.iM = 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Address(int versionCode, String name, String address1, String address2, String address3, String countryCode, String city, String state, String postalCode, String phoneNumber, boolean isPostBox, String companyName) {
        this.iM = versionCode;
        this.name = name;
        this.tu = address1;
        this.tv = address2;
        this.tw = address3;
        this.hl = countryCode;
        this.tx = city;
        this.ty = state;
        this.tz = postalCode;
        this.tA = phoneNumber;
        this.tB = isPostBox;
        this.tC = companyName;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getAddress1() {
        return this.tu;
    }

    public String getAddress2() {
        return this.tv;
    }

    public String getAddress3() {
        return this.tw;
    }

    public String getCity() {
        return this.tx;
    }

    public String getCompanyName() {
        return this.tC;
    }

    public String getCountryCode() {
        return this.hl;
    }

    public String getName() {
        return this.name;
    }

    public String getPhoneNumber() {
        return this.tA;
    }

    public String getPostalCode() {
        return this.tz;
    }

    public String getState() {
        return this.ty;
    }

    public int getVersionCode() {
        return this.iM;
    }

    public boolean isPostBox() {
        return this.tB;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        a.a(this, out, flags);
    }
}

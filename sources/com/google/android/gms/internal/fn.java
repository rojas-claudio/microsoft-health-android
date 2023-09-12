package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
/* loaded from: classes.dex */
public class fn implements SafeParcelable {
    public static final fp CREATOR = new fp();
    private final int iM;
    private final String it;
    private final String[] rA;
    private final String rB;
    private final String rC;
    private final String rD;
    private final String rE;
    private final String[] ry;
    private final String[] rz;

    /* JADX INFO: Access modifiers changed from: package-private */
    public fn(int i, String str, String[] strArr, String[] strArr2, String[] strArr3, String str2, String str3, String str4, String str5) {
        this.iM = i;
        this.it = str;
        this.ry = strArr;
        this.rz = strArr2;
        this.rA = strArr3;
        this.rB = str2;
        this.rC = str3;
        this.rD = str4;
        this.rE = str5;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public fn(String str, String[] strArr, String[] strArr2, String[] strArr3, String str2, String str3, String str4, String str5) {
        this.iM = 1;
        this.it = str;
        this.ry = strArr;
        this.rz = strArr2;
        this.rA = strArr3;
        this.rB = str2;
        this.rC = str3;
        this.rD = str4;
        this.rE = str5;
    }

    public String[] cZ() {
        return this.ry;
    }

    public String[] da() {
        return this.rz;
    }

    public String[] db() {
        return this.rA;
    }

    public String dc() {
        return this.rB;
    }

    public String dd() {
        return this.rC;
    }

    public String de() {
        return this.rD;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String df() {
        return this.rE;
    }

    public boolean equals(Object obj) {
        if (obj instanceof fn) {
            fn fnVar = (fn) obj;
            return this.iM == fnVar.iM && dl.equal(this.it, fnVar.it) && dl.equal(this.ry, fnVar.ry) && dl.equal(this.rz, fnVar.rz) && dl.equal(this.rA, fnVar.rA) && dl.equal(this.rB, fnVar.rB) && dl.equal(this.rC, fnVar.rC) && dl.equal(this.rD, fnVar.rD) && dl.equal(this.rE, fnVar.rE);
        }
        return false;
    }

    public String getAccountName() {
        return this.it;
    }

    public int getVersionCode() {
        return this.iM;
    }

    public int hashCode() {
        return dl.hashCode(Integer.valueOf(this.iM), this.it, this.ry, this.rz, this.rA, this.rB, this.rC, this.rD, this.rE);
    }

    public String toString() {
        return dl.d(this).a("versionCode", Integer.valueOf(this.iM)).a("accountName", this.it).a("requestedScopes", this.ry).a("visibleActivities", this.rz).a("requiredFeatures", this.rA).a("packageNameForAuth", this.rB).a("callingPackageName", this.rC).a("applicationName", this.rD).a("clientId", this.rE).toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        fp.a(this, out, flags);
    }
}

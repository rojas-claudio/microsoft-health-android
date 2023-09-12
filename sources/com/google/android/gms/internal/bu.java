package com.google.android.gms.internal;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
/* loaded from: classes.dex */
public final class bu implements SafeParcelable {
    public static final bv CREATOR = new bv();
    public final String adUnitId;
    public final ApplicationInfo applicationInfo;
    public final x ed;
    public final co eg;
    public final Bundle gA;
    public final v gB;
    public final PackageInfo gC;
    public final String gD;
    public final String gE;
    public final String gF;
    public final int versionCode;

    /* loaded from: classes.dex */
    public static final class a {
        public final String adUnitId;
        public final ApplicationInfo applicationInfo;
        public final x ed;
        public final co eg;
        public final Bundle gA;
        public final v gB;
        public final PackageInfo gC;
        public final String gE;
        public final String gF;

        public a(Bundle bundle, v vVar, x xVar, String str, ApplicationInfo applicationInfo, PackageInfo packageInfo, String str2, String str3, co coVar) {
            this.gA = bundle;
            this.gB = vVar;
            this.ed = xVar;
            this.adUnitId = str;
            this.applicationInfo = applicationInfo;
            this.gC = packageInfo;
            this.gE = str2;
            this.gF = str3;
            this.eg = coVar;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public bu(int i, Bundle bundle, v vVar, x xVar, String str, ApplicationInfo applicationInfo, PackageInfo packageInfo, String str2, String str3, String str4, co coVar) {
        this.versionCode = i;
        this.gA = bundle;
        this.gB = vVar;
        this.ed = xVar;
        this.adUnitId = str;
        this.applicationInfo = applicationInfo;
        this.gC = packageInfo;
        this.gD = str2;
        this.gE = str3;
        this.gF = str4;
        this.eg = coVar;
    }

    public bu(Bundle bundle, v vVar, x xVar, String str, ApplicationInfo applicationInfo, PackageInfo packageInfo, String str2, String str3, String str4, co coVar) {
        this(1, bundle, vVar, xVar, str, applicationInfo, packageInfo, str2, str3, str4, coVar);
    }

    public bu(a aVar, String str) {
        this(aVar.gA, aVar.gB, aVar.ed, aVar.adUnitId, aVar.applicationInfo, aVar.gC, str, aVar.gE, aVar.gF, aVar.eg);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        bv.a(this, out, flags);
    }
}

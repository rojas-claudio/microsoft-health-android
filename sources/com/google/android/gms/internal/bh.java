package com.google.android.gms.internal;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.dynamic.b;
/* loaded from: classes.dex */
public final class bh implements SafeParcelable {
    public static final bg CREATOR = new bg();
    public final co eg;
    public final be fR;
    public final q fS;
    public final bi fT;
    public final cq fU;
    public final ag fV;
    public final String fW;
    public final boolean fX;
    public final String fY;
    public final bl fZ;
    public final String fz;
    public final int ga;
    public final int orientation;
    public final int versionCode;

    /* JADX INFO: Access modifiers changed from: package-private */
    public bh(int i, be beVar, IBinder iBinder, IBinder iBinder2, IBinder iBinder3, IBinder iBinder4, String str, boolean z, String str2, IBinder iBinder5, int i2, int i3, String str3, co coVar) {
        this.versionCode = i;
        this.fR = beVar;
        this.fS = (q) com.google.android.gms.dynamic.c.b(b.a.z(iBinder));
        this.fT = (bi) com.google.android.gms.dynamic.c.b(b.a.z(iBinder2));
        this.fU = (cq) com.google.android.gms.dynamic.c.b(b.a.z(iBinder3));
        this.fV = (ag) com.google.android.gms.dynamic.c.b(b.a.z(iBinder4));
        this.fW = str;
        this.fX = z;
        this.fY = str2;
        this.fZ = (bl) com.google.android.gms.dynamic.c.b(b.a.z(iBinder5));
        this.orientation = i2;
        this.ga = i3;
        this.fz = str3;
        this.eg = coVar;
    }

    public bh(be beVar, q qVar, bi biVar, bl blVar, co coVar) {
        this.versionCode = 1;
        this.fR = beVar;
        this.fS = qVar;
        this.fT = biVar;
        this.fU = null;
        this.fV = null;
        this.fW = null;
        this.fX = false;
        this.fY = null;
        this.fZ = blVar;
        this.orientation = -1;
        this.ga = 4;
        this.fz = null;
        this.eg = coVar;
    }

    public bh(q qVar, bi biVar, ag agVar, bl blVar, cq cqVar, boolean z, int i, String str, co coVar) {
        this.versionCode = 1;
        this.fR = null;
        this.fS = qVar;
        this.fT = biVar;
        this.fU = cqVar;
        this.fV = agVar;
        this.fW = null;
        this.fX = z;
        this.fY = null;
        this.fZ = blVar;
        this.orientation = i;
        this.ga = 3;
        this.fz = str;
        this.eg = coVar;
    }

    public bh(q qVar, bi biVar, ag agVar, bl blVar, cq cqVar, boolean z, int i, String str, String str2, co coVar) {
        this.versionCode = 1;
        this.fR = null;
        this.fS = qVar;
        this.fT = biVar;
        this.fU = cqVar;
        this.fV = agVar;
        this.fW = str2;
        this.fX = z;
        this.fY = str;
        this.fZ = blVar;
        this.orientation = i;
        this.ga = 3;
        this.fz = null;
        this.eg = coVar;
    }

    public bh(q qVar, bi biVar, bl blVar, cq cqVar, int i, co coVar) {
        this.versionCode = 1;
        this.fR = null;
        this.fS = qVar;
        this.fT = biVar;
        this.fU = cqVar;
        this.fV = null;
        this.fW = null;
        this.fX = false;
        this.fY = null;
        this.fZ = blVar;
        this.orientation = i;
        this.ga = 1;
        this.fz = null;
        this.eg = coVar;
    }

    public bh(q qVar, bi biVar, bl blVar, cq cqVar, boolean z, int i, co coVar) {
        this.versionCode = 1;
        this.fR = null;
        this.fS = qVar;
        this.fT = biVar;
        this.fU = cqVar;
        this.fV = null;
        this.fW = null;
        this.fX = z;
        this.fY = null;
        this.fZ = blVar;
        this.orientation = i;
        this.ga = 2;
        this.fz = null;
        this.eg = coVar;
    }

    public static bh a(Intent intent) {
        try {
            Bundle bundleExtra = intent.getBundleExtra("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo");
            bundleExtra.setClassLoader(bh.class.getClassLoader());
            return (bh) bundleExtra.getParcelable("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo");
        } catch (Exception e) {
            return null;
        }
    }

    public static void a(Intent intent, bh bhVar) {
        Bundle bundle = new Bundle(1);
        bundle.putParcelable("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo", bhVar);
        intent.putExtra("com.google.android.gms.ads.inernal.overlay.AdOverlayInfo", bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder U() {
        return com.google.android.gms.dynamic.c.g(this.fS).asBinder();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder V() {
        return com.google.android.gms.dynamic.c.g(this.fT).asBinder();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder W() {
        return com.google.android.gms.dynamic.c.g(this.fU).asBinder();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder X() {
        return com.google.android.gms.dynamic.c.g(this.fV).asBinder();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder Y() {
        return com.google.android.gms.dynamic.c.g(this.fZ).asBinder();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        bg.a(this, out, flags);
    }
}

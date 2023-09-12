package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.admob.AdMobExtras;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public final class v implements SafeParcelable {
    public static final w CREATOR = new w();
    public final long es;
    public final int et;
    public final List<String> eu;
    public final boolean ev;
    public final Bundle extras;
    public final int tagForChildDirectedTreatment;
    public final int versionCode;

    /* JADX INFO: Access modifiers changed from: package-private */
    public v(int i, long j, Bundle bundle, int i2, List<String> list, boolean z, int i3) {
        this.versionCode = i;
        this.es = j;
        this.extras = bundle;
        this.et = i2;
        this.eu = list;
        this.ev = z;
        this.tagForChildDirectedTreatment = i3;
    }

    public v(Context context, AdRequest adRequest) {
        this.versionCode = 1;
        Date birthday = adRequest.getBirthday();
        this.es = birthday != null ? birthday.getTime() : -1L;
        this.et = adRequest.getGender();
        Set<String> keywords = adRequest.getKeywords();
        this.eu = !keywords.isEmpty() ? Collections.unmodifiableList(new ArrayList(keywords)) : null;
        this.ev = adRequest.isTestDevice(context);
        this.tagForChildDirectedTreatment = adRequest.w();
        AdMobExtras adMobExtras = (AdMobExtras) adRequest.getNetworkExtras(AdMobExtras.class);
        this.extras = adMobExtras != null ? adMobExtras.getExtras() : null;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        w.a(this, out, flags);
    }
}

package com.google.android.gms.internal;

import android.content.Intent;
import android.net.Uri;
import com.google.android.gms.common.GooglePlayServicesUtil;
/* loaded from: classes.dex */
public class dg {
    private static final Uri lg = Uri.parse("http://plus.google.com/");
    private static final Uri lh = lg.buildUpon().appendPath("circles").appendPath("find").build();

    private static Uri A(String str) {
        return Uri.parse("market://details").buildUpon().appendQueryParameter("id", str).build();
    }

    public static Intent B(String str) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(A(str));
        intent.setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE);
        intent.addFlags(524288);
        return intent;
    }

    public static Intent C(String str) {
        Uri parse = Uri.parse("bazaar://search?q=pname:" + str);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(parse);
        intent.setFlags(524288);
        return intent;
    }

    public static Intent bj() {
        return new Intent("android.settings.DATE_SETTINGS");
    }

    public static Intent z(String str) {
        Uri fromParts = Uri.fromParts("package", str, null);
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(fromParts);
        return intent;
    }
}

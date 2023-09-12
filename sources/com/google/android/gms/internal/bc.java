package com.google.android.gms.internal;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
/* loaded from: classes.dex */
public final class bc {
    public static boolean a(Context context, be beVar, bl blVar) {
        if (beVar == null) {
            cn.q("No intent data for launcher overlay.");
            return false;
        }
        Intent intent = new Intent();
        if (TextUtils.isEmpty(beVar.fz)) {
            cn.q("Open GMSG did not contain a URL.");
            return false;
        }
        if (TextUtils.isEmpty(beVar.mimeType)) {
            intent.setData(Uri.parse(beVar.fz));
        } else {
            intent.setDataAndType(Uri.parse(beVar.fz), beVar.mimeType);
        }
        intent.setAction("android.intent.action.VIEW");
        if (!TextUtils.isEmpty(beVar.packageName)) {
            intent.setPackage(beVar.packageName);
        }
        if (!TextUtils.isEmpty(beVar.fA)) {
            String[] split = beVar.fA.split("/", 2);
            if (split.length < 2) {
                cn.q("Could not parse component name from open GMSG: " + beVar.fA);
                return false;
            }
            intent.setClassName(split[0], split[1]);
        }
        try {
            cn.p("Launching an intent: " + intent);
            context.startActivity(intent);
            blVar.A();
            return true;
        } catch (ActivityNotFoundException e) {
            cn.q(e.getMessage());
            return false;
        }
    }
}

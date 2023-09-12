package com.google.android.gms.internal;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.facebook.AppEventsConstants;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public final class ah {
    public static final ai ez = new ai() { // from class: com.google.android.gms.internal.ah.1
        @Override // com.google.android.gms.internal.ai
        public void a(cq cqVar, Map<String, String> map) {
            String str = map.get("urls");
            if (str == null) {
                cn.q("URLs missing in canOpenURLs GMSG.");
                return;
            }
            String[] split = str.split(",");
            HashMap hashMap = new HashMap();
            PackageManager packageManager = cqVar.getContext().getPackageManager();
            for (String str2 : split) {
                String[] split2 = str2.split(";", 2);
                hashMap.put(str2, Boolean.valueOf(packageManager.resolveActivity(new Intent(split2.length > 1 ? split2[1].trim() : "android.intent.action.VIEW", Uri.parse(split2[0].trim())), 65536) != null));
            }
            cqVar.a("openableURLs", hashMap);
        }
    };
    public static final ai eA = new ai() { // from class: com.google.android.gms.internal.ah.2
        @Override // com.google.android.gms.internal.ai
        public void a(cq cqVar, Map<String, String> map) {
            Uri uri;
            h ax;
            String str = map.get("u");
            if (str == null) {
                cn.q("URL missing from click GMSG.");
                return;
            }
            Uri parse = Uri.parse(str);
            try {
                ax = cqVar.ax();
            } catch (i e) {
                cn.q("Unable to append parameter to URL: " + str);
            }
            if (ax != null && ax.a(parse)) {
                uri = ax.a(parse, cqVar.getContext());
                new cl(cqVar.getContext(), cqVar.ay().hP, uri.toString()).start();
            }
            uri = parse;
            new cl(cqVar.getContext(), cqVar.ay().hP, uri.toString()).start();
        }
    };
    public static final ai eB = new ai() { // from class: com.google.android.gms.internal.ah.3
        @Override // com.google.android.gms.internal.ai
        public void a(cq cqVar, Map<String, String> map) {
            bf au = cqVar.au();
            if (au == null) {
                cn.q("A GMSG tried to close something that wasn't an overlay.");
            } else {
                au.close();
            }
        }
    };
    public static final ai eC = new ai() { // from class: com.google.android.gms.internal.ah.4
        @Override // com.google.android.gms.internal.ai
        public void a(cq cqVar, Map<String, String> map) {
            bf au = cqVar.au();
            if (au == null) {
                cn.q("A GMSG tried to use a custom close button on something that wasn't an overlay.");
            } else {
                au.d(AppEventsConstants.EVENT_PARAM_VALUE_YES.equals(map.get("custom_close")));
            }
        }
    };
    public static final ai eD = new ai() { // from class: com.google.android.gms.internal.ah.5
        @Override // com.google.android.gms.internal.ai
        public void a(cq cqVar, Map<String, String> map) {
            String str = map.get("u");
            if (str == null) {
                cn.q("URL missing from httpTrack GMSG.");
            } else {
                new cl(cqVar.getContext(), cqVar.ay().hP, str).start();
            }
        }
    };
    public static final ai eE = new ai() { // from class: com.google.android.gms.internal.ah.6
        @Override // com.google.android.gms.internal.ai
        public void a(cq cqVar, Map<String, String> map) {
            cn.o("Received log message: " + map.get("string"));
        }
    };
    public static final ai eF = new aj();
    public static final ai eG = new ai() { // from class: com.google.android.gms.internal.ah.7
        @Override // com.google.android.gms.internal.ai
        public void a(cq cqVar, Map<String, String> map) {
            String str = map.get("tx");
            String str2 = map.get("ty");
            String str3 = map.get("td");
            try {
                int parseInt = Integer.parseInt(str);
                int parseInt2 = Integer.parseInt(str2);
                int parseInt3 = Integer.parseInt(str3);
                h ax = cqVar.ax();
                if (ax != null) {
                    ax.g().a(parseInt, parseInt2, parseInt3);
                }
            } catch (NumberFormatException e) {
                cn.q("Could not parse touch parameters from gmsg.");
            }
        }
    };
    public static final ai eH = new ak();
}

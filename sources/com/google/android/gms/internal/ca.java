package com.google.android.gms.internal;

import android.location.Location;
import android.text.TextUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.json.JSONException;
/* loaded from: classes.dex */
public final class ca {
    private static final SimpleDateFormat gS = new SimpleDateFormat("yyyyMMdd");

    public static String a(bu buVar, cd cdVar, Location location) {
        try {
            HashMap hashMap = new HashMap();
            if (buVar.gA != null) {
                hashMap.put("ad_pos", buVar.gA);
            }
            a(hashMap, buVar.gB);
            hashMap.put("format", buVar.ed.ew);
            if (buVar.ed.width == -1) {
                hashMap.put("smart_w", "full");
            }
            if (buVar.ed.height == -2) {
                hashMap.put("smart_h", "auto");
            }
            hashMap.put("slotname", buVar.adUnitId);
            hashMap.put("pn", buVar.applicationInfo.packageName);
            if (buVar.gC != null) {
                hashMap.put("vc", Integer.valueOf(buVar.gC.versionCode));
            }
            hashMap.put("ms", buVar.gD);
            hashMap.put("seq_num", buVar.gE);
            hashMap.put("session_id", buVar.gF);
            hashMap.put("js", buVar.eg.hP);
            a(hashMap, cdVar);
            a(hashMap, location);
            if (cn.k(2)) {
                cn.p("Ad Request JSON: " + ci.l(hashMap).toString(2));
            }
            return ci.l(hashMap).toString();
        } catch (JSONException e) {
            cn.q("Problem serializing ad request to JSON: " + e.getMessage());
            return null;
        }
    }

    private static void a(HashMap<String, Object> hashMap, Location location) {
        if (location == null) {
            return;
        }
        HashMap hashMap2 = new HashMap();
        Float valueOf = Float.valueOf(location.getAccuracy() * 1000.0f);
        Long valueOf2 = Long.valueOf(location.getTime() * 1000);
        Long valueOf3 = Long.valueOf((long) (location.getLatitude() * 1.0E7d));
        Long valueOf4 = Long.valueOf((long) (location.getLongitude() * 1.0E7d));
        hashMap2.put("radius", valueOf);
        hashMap2.put("lat", valueOf3);
        hashMap2.put("long", valueOf4);
        hashMap2.put("time", valueOf2);
        hashMap.put("loc", hashMap2);
    }

    private static void a(HashMap<String, Object> hashMap, cd cdVar) {
        hashMap.put("am", Integer.valueOf(cdVar.hh));
        hashMap.put("cog", g(cdVar.hi));
        hashMap.put("coh", g(cdVar.hj));
        if (!TextUtils.isEmpty(cdVar.hk)) {
            hashMap.put("carrier", cdVar.hk);
        }
        hashMap.put("gl", cdVar.hl);
        if (cdVar.hm) {
            hashMap.put("simulator", 1);
        }
        hashMap.put("ma", g(cdVar.hn));
        hashMap.put("sp", g(cdVar.ho));
        hashMap.put("hl", cdVar.hp);
        if (!TextUtils.isEmpty(cdVar.hq)) {
            hashMap.put("mv", cdVar.hq);
        }
        hashMap.put("muv", Integer.valueOf(cdVar.hr));
        if (cdVar.hs != -2) {
            hashMap.put("cnt", Integer.valueOf(cdVar.hs));
        }
        hashMap.put("gnt", Integer.valueOf(cdVar.ht));
        hashMap.put("pt", Integer.valueOf(cdVar.hu));
        hashMap.put("rm", Integer.valueOf(cdVar.hv));
        hashMap.put("riv", Integer.valueOf(cdVar.hw));
        hashMap.put("u_sd", Float.valueOf(cdVar.hx));
        hashMap.put("sh", Integer.valueOf(cdVar.hz));
        hashMap.put("sw", Integer.valueOf(cdVar.hy));
    }

    private static void a(HashMap<String, Object> hashMap, v vVar) {
        if (vVar.es != -1) {
            hashMap.put("cust_age", gS.format(new Date(vVar.es)));
        }
        if (vVar.extras != null) {
            hashMap.put("extras", vVar.extras);
        }
        if (vVar.et != -1) {
            hashMap.put("cust_gender", Integer.valueOf(vVar.et));
        }
        if (vVar.eu != null) {
            hashMap.put("kw", vVar.eu);
        }
        if (vVar.tagForChildDirectedTreatment != -1) {
            hashMap.put("tag_for_child_directed_treatment", Integer.valueOf(vVar.tagForChildDirectedTreatment));
        }
        if (vVar.ev) {
            hashMap.put("adtest", "on");
        }
    }

    private static Integer g(boolean z) {
        return Integer.valueOf(z ? 1 : 0);
    }
}

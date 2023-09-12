package com.google.android.gms.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public final class ap {
    public final List<ao> eU;
    public final long eV;
    public final List<String> eW;
    public final List<String> eX;
    public final List<String> eY;
    public final String eZ;
    public final long fa;

    public ap(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject(str);
        if (cn.k(2)) {
            cn.p("Mediation Response JSON: " + jSONObject.toString(2));
        }
        JSONArray jSONArray = jSONObject.getJSONArray("ad_networks");
        ArrayList arrayList = new ArrayList(jSONArray.length());
        for (int i = 0; i < jSONArray.length(); i++) {
            arrayList.add(new ao(jSONArray.getJSONObject(i)));
        }
        this.eU = Collections.unmodifiableList(arrayList);
        this.eZ = jSONObject.getString("qdata");
        JSONObject optJSONObject = jSONObject.optJSONObject("settings");
        if (optJSONObject == null) {
            this.eV = -1L;
            this.eW = null;
            this.eX = null;
            this.eY = null;
            this.fa = -1L;
            return;
        }
        this.eV = optJSONObject.optLong("ad_network_timeout_millis", -1L);
        this.eW = au.a(optJSONObject, "click_urls");
        this.eX = au.a(optJSONObject, "imp_urls");
        this.eY = au.a(optJSONObject, "nofill_urls");
        long optLong = optJSONObject.optLong("refresh", -1L);
        this.fa = optLong > 0 ? 1000 * optLong : -1L;
    }
}

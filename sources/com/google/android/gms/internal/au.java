package com.google.android.gms.internal;

import android.content.Context;
import com.facebook.AppEventsConstants;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public final class au {
    public static List<String> a(JSONObject jSONObject, String str) throws JSONException {
        JSONArray optJSONArray = jSONObject.optJSONArray(str);
        if (optJSONArray != null) {
            ArrayList arrayList = new ArrayList(optJSONArray.length());
            for (int i = 0; i < optJSONArray.length(); i++) {
                arrayList.add(optJSONArray.getString(i));
            }
            return Collections.unmodifiableList(arrayList);
        }
        return null;
    }

    public static void a(Context context, String str, ce ceVar, String str2, boolean z, List<String> list) {
        String str3 = z ? AppEventsConstants.EVENT_PARAM_VALUE_YES : AppEventsConstants.EVENT_PARAM_VALUE_NO;
        for (String str4 : list) {
            String replaceAll = str4.replaceAll("@gw_adlocid@", str2).replaceAll("@gw_adnetrefresh@", str3).replaceAll("@gw_qdata@", ceVar.hA.eZ).replaceAll("@gw_sdkver@", str).replaceAll("@gw_sessid@", cf.hB).replaceAll("@gw_seqnum@", ceVar.gE);
            if (ceVar.fm != null) {
                replaceAll = replaceAll.replaceAll("@gw_adnetid@", ceVar.fm.eP).replaceAll("@gw_allocid@", ceVar.fm.eR);
            }
            new cl(context, str, replaceAll).start();
        }
    }
}

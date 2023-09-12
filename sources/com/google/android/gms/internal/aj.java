package com.google.android.gms.internal;

import com.facebook.AppEventsConstants;
import java.util.Map;
/* loaded from: classes.dex */
public final class aj implements ai {
    private static boolean a(Map<String, String> map) {
        return AppEventsConstants.EVENT_PARAM_VALUE_YES.equals(map.get("custom_close"));
    }

    private static int b(Map<String, String> map) {
        String str = map.get("o");
        if (str != null) {
            if ("p".equalsIgnoreCase(str)) {
                return ci.ao();
            }
            if ("l".equalsIgnoreCase(str)) {
                return ci.an();
            }
        }
        return -1;
    }

    @Override // com.google.android.gms.internal.ai
    public void a(cq cqVar, Map<String, String> map) {
        String str = map.get("a");
        if (str == null) {
            cn.q("Action missing from an open GMSG.");
            return;
        }
        cr aw = cqVar.aw();
        if ("expand".equalsIgnoreCase(str)) {
            if (cqVar.az()) {
                cn.q("Cannot expand WebView that is already expanded.");
            } else {
                aw.a(a(map), b(map));
            }
        } else if (!"webapp".equalsIgnoreCase(str)) {
            aw.a(new be(map.get("i"), map.get("u"), map.get("m"), map.get("p"), map.get("c"), map.get("f"), map.get("e")));
        } else {
            String str2 = map.get("u");
            if (str2 != null) {
                aw.a(a(map), b(map), str2);
            } else {
                aw.a(a(map), b(map), map.get("html"), map.get("baseurl"));
            }
        }
    }
}

package com.google.android.gms.internal;

import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import java.util.Map;
/* loaded from: classes.dex */
public final class ak implements ai {
    private static int a(DisplayMetrics displayMetrics, Map<String, String> map, String str, int i) {
        String str2 = map.get(str);
        if (str2 != null) {
            try {
                return cm.a(displayMetrics, Integer.parseInt(str2));
            } catch (NumberFormatException e) {
                cn.q("Could not parse " + str + " in a video GMSG: " + str2);
                return i;
            }
        }
        return i;
    }

    @Override // com.google.android.gms.internal.ai
    public void a(cq cqVar, Map<String, String> map) {
        String str = map.get("action");
        if (str == null) {
            cn.q("Action missing from video GMSG.");
            return;
        }
        bf au = cqVar.au();
        if (au == null) {
            cn.q("Could not get ad overlay for a video GMSG.");
            return;
        }
        boolean equalsIgnoreCase = "new".equalsIgnoreCase(str);
        boolean equalsIgnoreCase2 = "position".equalsIgnoreCase(str);
        if (equalsIgnoreCase || equalsIgnoreCase2) {
            DisplayMetrics displayMetrics = cqVar.getContext().getResources().getDisplayMetrics();
            int a = a(displayMetrics, map, "x", 0);
            int a2 = a(displayMetrics, map, "y", 0);
            int a3 = a(displayMetrics, map, "w", -1);
            int a4 = a(displayMetrics, map, "h", -1);
            if (equalsIgnoreCase && au.Q() == null) {
                au.c(a, a2, a3, a4);
                return;
            } else {
                au.b(a, a2, a3, a4);
                return;
            }
        }
        bj Q = au.Q();
        if (Q == null) {
            bj.a(cqVar, "no_video_view", (String) null);
        } else if ("click".equalsIgnoreCase(str)) {
            DisplayMetrics displayMetrics2 = cqVar.getContext().getResources().getDisplayMetrics();
            int a5 = a(displayMetrics2, map, "x", 0);
            int a6 = a(displayMetrics2, map, "y", 0);
            long uptimeMillis = SystemClock.uptimeMillis();
            MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 0, a5, a6, 0);
            Q.b(obtain);
            obtain.recycle();
        } else if ("controls".equalsIgnoreCase(str)) {
            String str2 = map.get("enabled");
            if (str2 == null) {
                cn.q("Enabled parameter missing from controls video GMSG.");
            } else {
                Q.f(Boolean.parseBoolean(str2));
            }
        } else if ("currentTime".equalsIgnoreCase(str)) {
            String str3 = map.get("time");
            if (str3 == null) {
                cn.q("Time parameter missing from currentTime video GMSG.");
                return;
            }
            try {
                Q.seekTo((int) (Float.parseFloat(str3) * 1000.0f));
            } catch (NumberFormatException e) {
                cn.q("Could not parse time parameter from currentTime video GMSG: " + str3);
            }
        } else if ("hide".equalsIgnoreCase(str)) {
            Q.setVisibility(4);
        } else if ("load".equalsIgnoreCase(str)) {
            Q.Z();
        } else if ("pause".equalsIgnoreCase(str)) {
            Q.pause();
        } else if ("play".equalsIgnoreCase(str)) {
            Q.play();
        } else if ("show".equalsIgnoreCase(str)) {
            Q.setVisibility(0);
        } else if ("src".equalsIgnoreCase(str)) {
            Q.i(map.get("src"));
        } else {
            cn.q("Unknown video action: " + str);
        }
    }
}

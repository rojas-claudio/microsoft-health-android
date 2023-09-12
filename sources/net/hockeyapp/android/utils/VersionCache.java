package net.hockeyapp.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
/* loaded from: classes.dex */
public class VersionCache {
    private static String VERSION_INFO_KEY = "versionInfo";

    public static void setVersionInfo(Context context, String json) {
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences("HockeyApp", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(VERSION_INFO_KEY, json);
            PrefsUtil.applyChanges(editor);
        }
    }

    public static String getVersionInfo(Context context) {
        if (context != null) {
            SharedPreferences preferences = context.getSharedPreferences("HockeyApp", 0);
            return preferences.getString(VERSION_INFO_KEY, "[]");
        }
        return "[]";
    }
}

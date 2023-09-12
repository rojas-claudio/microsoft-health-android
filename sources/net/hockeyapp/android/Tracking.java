package net.hockeyapp.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import net.hockeyapp.android.utils.PrefsUtil;
/* loaded from: classes.dex */
public class Tracking {
    private static final String START_TIME_KEY = "startTime";
    private static final String USAGE_TIME_KEY = "usageTime";

    public static void startUsage(Activity activity) {
        long now = System.currentTimeMillis();
        if (activity != null) {
            SharedPreferences.Editor editor = getPreferences(activity).edit();
            editor.putLong(START_TIME_KEY + activity.hashCode(), now);
            PrefsUtil.applyChanges(editor);
        }
    }

    public static void stopUsage(Activity activity) {
        long now = System.currentTimeMillis();
        if (activity != null && checkVersion(activity)) {
            SharedPreferences preferences = getPreferences(activity);
            long start = preferences.getLong(START_TIME_KEY + activity.hashCode(), 0L);
            long sum = preferences.getLong(USAGE_TIME_KEY + Constants.APP_VERSION, 0L);
            if (start > 0) {
                long duration = now - start;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong(USAGE_TIME_KEY + Constants.APP_VERSION, sum + duration);
                PrefsUtil.applyChanges(editor);
            }
        }
    }

    public static long getUsageTime(Context context) {
        if (checkVersion(context)) {
            SharedPreferences preferences = getPreferences(context);
            long sum = preferences.getLong(USAGE_TIME_KEY + Constants.APP_VERSION, 0L);
            return sum / 1000;
        }
        return 0L;
    }

    private static boolean checkVersion(Context context) {
        if (Constants.APP_VERSION == null) {
            Constants.loadFromContext(context);
            if (Constants.APP_VERSION == null) {
                return false;
            }
        }
        return true;
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("HockeyApp", 0);
    }
}

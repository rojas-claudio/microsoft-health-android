package com.microsoft.kapp.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.widget.EditText;
import com.microsoft.kapp.R;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
/* loaded from: classes.dex */
public class SettingsUtils {
    public static String getVersionText(Context context, String callTag) {
        try {
            String version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            return context.getResources().getString(R.string.app_name) + MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE + String.format(context.getString(R.string.settings_my_band_version), version);
        } catch (PackageManager.NameNotFoundException exception) {
            KLog.e(callTag, "Exception in getVersionText", exception);
            return null;
        }
    }

    public static void throwExceptionIfOnMainThread() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new UnsupportedOperationException();
        }
    }

    public static boolean validateDeviceName(EditText deviceName, int minLength) {
        if (deviceName.getText().toString().trim().length() >= minLength) {
            deviceName.setBackgroundResource(R.color.device_name_background);
            return true;
        }
        deviceName.setBackgroundResource(R.color.oobe_edit_text_background_error);
        return false;
    }

    public static TreeMap<CharSequence, ApplicationInfo> getAllInstalledApps(Context context) {
        TreeMap<CharSequence, ApplicationInfo> allApps = new TreeMap<>();
        PackageManager pm = context.getPackageManager();
        ArrayList<ApplicationInfo> allAppsUnsorted = new ArrayList<>(pm.getInstalledApplications(0));
        Iterator i$ = allAppsUnsorted.iterator();
        while (i$.hasNext()) {
            ApplicationInfo app = i$.next();
            if (isNotificationEnabledPackage(app.packageName) && pm.getLaunchIntentForPackage(app.packageName) != null) {
                CharSequence appName = app.loadLabel(pm);
                allApps.put(appName, app);
            }
        }
        return allApps;
    }

    private static boolean isNotificationEnabledPackage(String packageName) {
        return (StrappConstants.isMailClient(packageName) || packageName.equals(StrappConstants.NOTIFICATION_SERVICE_FACEBOOK) || packageName.equals(StrappConstants.NOTIFICATION_SERVICE_FACEBOOK_MESSAGER) || packageName.equals(StrappConstants.NOTIFICATION_SERVICE_TWITTER) || packageName.equals(StrappConstants.NOTIFICATION_SERVICE_MICROSOFT_HEALTH)) ? false : true;
    }
}

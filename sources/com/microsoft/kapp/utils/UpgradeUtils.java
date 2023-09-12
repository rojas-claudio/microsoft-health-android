package com.microsoft.kapp.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.SettingsProvider;
/* loaded from: classes.dex */
public class UpgradeUtils {
    private static String TAG = UpgradeUtils.class.getSimpleName();

    public static boolean isAppJustUpgraded(SettingsProvider settingsProvider, Context context) {
        String currentVersion = getAppVersion(context);
        String storedVersion = settingsProvider.getAppVersion();
        return (TextUtils.isEmpty(storedVersion) || currentVersion.equals(storedVersion)) ? false : true;
    }

    public static void updateAppJustUpgraded(SettingsProvider settingsProvider, Context context) {
        String currentVersion = getAppVersion(context);
        settingsProvider.setAppVersion(currentVersion);
    }

    private static String getAppVersion(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String currentVersion = pInfo.versionName;
            return currentVersion;
        } catch (PackageManager.NameNotFoundException e) {
            KLog.d(TAG, "unable to getPackageInfo", e);
            return "";
        }
    }
}

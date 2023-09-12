package com.microsoft.kapp.diagnostics;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import com.microsoft.band.build.BranchInfo;
import com.microsoft.kapp.debug.KappConfig;
import com.microsoft.kapp.logging.KLog;
import org.apache.commons.lang3.StringUtils;
/* loaded from: classes.dex */
public final class Compatibility {
    private static final String MANUFACTURE_NAME_SAMSUNG = "samsung";
    private static boolean inEmulator;
    private static boolean inEmulatorResolved;
    private static final String TAG = Compatibility.class.getSimpleName();
    private static final int API_LEVEL = Build.VERSION.SDK_INT;

    public static int getApiLevel() {
        return API_LEVEL;
    }

    public static boolean supportsRegisterActivityLifecycleCallbacks() {
        return getApiLevel() >= 14;
    }

    public static boolean inEmulator() {
        if (!inEmulatorResolved) {
            inEmulatorResolved = true;
            inEmulator = Build.HARDWARE.contains("goldfish");
        }
        return inEmulator;
    }

    public static boolean isPublicRelease() {
        return !KappConfig.isDebbuging;
    }

    public static boolean shouldLogHTTPToFiddler() {
        return false;
    }

    public static boolean isPublicReleaseBranch() {
        return true;
    }

    public static boolean shouldIntegrateHockeyApp(Context context) {
        return (!BranchInfo.BranchName.equalsIgnoreCase("Release") || KappConfig.isDebbuging || isDeveloperVersion(context)) ? false : true;
    }

    public static boolean supportsSmsMmsConversation() {
        String manufacture = Build.MANUFACTURER;
        return !StringUtils.equalsIgnoreCase(MANUFACTURE_NAME_SAMSUNG, manufacture);
    }

    public static boolean isDeveloperVersion(Context context) {
        String currentVersion = getCurrentVersion(context);
        if (currentVersion != null) {
            return currentVersion.equals("1.0.0.0");
        }
        return true;
    }

    public static String getCurrentVersion(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            KLog.e(TAG, "unable to get package version");
            return null;
        }
    }
}

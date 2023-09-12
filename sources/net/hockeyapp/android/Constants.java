package net.hockeyapp.android;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import com.unnamed.b.atv.model.TreeNode;
import java.io.File;
import java.security.MessageDigest;
/* loaded from: classes.dex */
public class Constants {
    public static final String BASE_URL = "https://sdk.hockeyapp.net/";
    public static final String SDK_NAME = "HockeySDK";
    public static final String SDK_VERSION = "3.5.0";
    public static final String TAG = "HockeyApp";
    public static String FILES_PATH = null;
    public static String APP_VERSION = null;
    public static String APP_VERSION_NAME = null;
    public static String APP_PACKAGE = null;
    public static String ANDROID_VERSION = null;
    public static String PHONE_MODEL = null;
    public static String PHONE_MANUFACTURER = null;
    public static String CRASH_IDENTIFIER = null;

    public static void loadFromContext(Context context) {
        ANDROID_VERSION = Build.VERSION.RELEASE;
        PHONE_MODEL = Build.MODEL;
        PHONE_MANUFACTURER = Build.MANUFACTURER;
        loadFilesPath(context);
        loadPackageData(context);
        loadCrashIdentifier(context);
    }

    public static File getHockeyAppStorageDir() {
        File externalStorage = Environment.getExternalStorageDirectory();
        File dir = new File(externalStorage.getAbsolutePath() + "/HockeyApp");
        dir.mkdirs();
        return dir;
    }

    private static void loadFilesPath(Context context) {
        if (context != null) {
            try {
                File file = context.getFilesDir();
                if (file != null) {
                    FILES_PATH = file.getAbsolutePath();
                }
            } catch (Exception e) {
                Log.e("HockeyApp", "Exception thrown when accessing the files dir:");
                e.printStackTrace();
            }
        }
    }

    private static void loadPackageData(Context context) {
        if (context != null) {
            try {
                PackageManager packageManager = context.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                APP_PACKAGE = packageInfo.packageName;
                APP_VERSION = "" + packageInfo.versionCode;
                APP_VERSION_NAME = packageInfo.versionName;
                int buildNumber = loadBuildNumber(context, packageManager);
                if (buildNumber != 0 && buildNumber > packageInfo.versionCode) {
                    APP_VERSION = "" + buildNumber;
                }
            } catch (Exception e) {
                Log.e("HockeyApp", "Exception thrown when accessing the package info:");
                e.printStackTrace();
            }
        }
    }

    private static int loadBuildNumber(Context context, PackageManager packageManager) {
        try {
            ApplicationInfo appInfo = packageManager.getApplicationInfo(context.getPackageName(), 128);
            Bundle metaData = appInfo.metaData;
            if (metaData != null) {
                return metaData.getInt("buildNumber", 0);
            }
            return 0;
        } catch (Exception e) {
            Log.e("HockeyApp", "Exception thrown when accessing the application info:");
            e.printStackTrace();
            return 0;
        }
    }

    private static void loadCrashIdentifier(Context context) {
        String deviceIdentifier = Settings.Secure.getString(context.getContentResolver(), "android_id");
        if (APP_PACKAGE != null && deviceIdentifier != null) {
            String combined = APP_PACKAGE + TreeNode.NODES_ID_SEPARATOR + deviceIdentifier + TreeNode.NODES_ID_SEPARATOR + createSalt(context);
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-1");
                byte[] bytes = combined.getBytes("UTF-8");
                digest.update(bytes, 0, bytes.length);
                CRASH_IDENTIFIER = bytesToHex(digest.digest());
            } catch (Throwable th) {
            }
        }
    }

    private static String createSalt(Context context) {
        String fingerprint = "HA" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.PRODUCT.length() % 10);
        String serial = "";
        if (Build.VERSION.SDK_INT >= 9) {
            try {
                serial = Build.class.getField("SERIAL").get(null).toString();
            } catch (Throwable th) {
            }
        }
        return fingerprint + TreeNode.NODES_ID_SEPARATOR + serial;
    }

    private static String bytesToHex(byte[] bytes) {
        char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hex = new char[bytes.length * 2];
        for (int index = 0; index < bytes.length; index++) {
            int value = bytes[index] & 255;
            hex[index * 2] = HEX_ARRAY[value >>> 4];
            hex[(index * 2) + 1] = HEX_ARRAY[value & 15];
        }
        String result = new String(hex);
        return result.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
    }
}

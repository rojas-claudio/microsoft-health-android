package org.acra;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import org.acra.annotation.ReportsCrashes;
import org.acra.log.ACRALog;
import org.acra.log.AndroidLogDelegate;
/* loaded from: classes.dex */
public class ACRA {
    public static final boolean DEV_LOGGING = false;
    public static final String PREF_ALWAYS_ACCEPT = "acra.alwaysaccept";
    public static final String PREF_DISABLE_ACRA = "acra.disable";
    public static final String PREF_ENABLE_ACRA = "acra.enable";
    public static final String PREF_ENABLE_DEVICE_ID = "acra.deviceid.enable";
    public static final String PREF_ENABLE_SYSTEM_LOGS = "acra.syslog.enable";
    public static final String PREF_LAST_VERSION_NR = "acra.lastVersionNr";
    public static final String PREF_USER_EMAIL_ADDRESS = "acra.user.email";
    private static ACRAConfiguration configProxy;
    private static ErrorReporter errorReporterSingleton;
    private static Application mApplication;
    private static SharedPreferences.OnSharedPreferenceChangeListener mPrefListener;
    private static ReportsCrashes mReportsCrashes;
    public static final String LOG_TAG = ACRA.class.getSimpleName();
    public static ACRALog log = new AndroidLogDelegate();

    public static void init(Application app) {
        if (mApplication != null) {
            log.w(LOG_TAG, "ACRA#init called more than once. Won't do anything more.");
            return;
        }
        mApplication = app;
        mReportsCrashes = (ReportsCrashes) mApplication.getClass().getAnnotation(ReportsCrashes.class);
        if (mReportsCrashes == null) {
            log.e(LOG_TAG, "ACRA#init called but no ReportsCrashes annotation on Application " + mApplication.getPackageName());
            return;
        }
        SharedPreferences prefs = getACRASharedPreferences();
        try {
            checkCrashResources();
            log.d(LOG_TAG, "ACRA is enabled for " + mApplication.getPackageName() + ", intializing...");
            boolean enableAcra = !shouldDisableACRA(prefs);
            ErrorReporter errorReporter = new ErrorReporter(mApplication, prefs, enableAcra);
            errorReporter.setDefaultReportSenders();
            errorReporterSingleton = errorReporter;
        } catch (ACRAConfigurationException e) {
            log.w(LOG_TAG, "Error : ", e);
        }
        mPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener() { // from class: org.acra.ACRA.1
            @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (ACRA.PREF_DISABLE_ACRA.equals(key) || ACRA.PREF_ENABLE_ACRA.equals(key)) {
                    boolean enableAcra2 = !ACRA.shouldDisableACRA(sharedPreferences);
                    ACRA.getErrorReporter().setEnabled(enableAcra2);
                }
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(mPrefListener);
    }

    public static ErrorReporter getErrorReporter() {
        if (errorReporterSingleton == null) {
            throw new IllegalStateException("Cannot access ErrorReporter before ACRA#init");
        }
        return errorReporterSingleton;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean shouldDisableACRA(SharedPreferences prefs) {
        try {
            boolean enableAcra = prefs.getBoolean(PREF_ENABLE_ACRA, true);
            boolean disableAcra = prefs.getBoolean(PREF_DISABLE_ACRA, enableAcra ? false : true);
            return disableAcra;
        } catch (Exception e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void checkCrashResources() throws ACRAConfigurationException {
        ReportsCrashes conf = getConfig();
        switch (conf.mode()) {
            case TOAST:
                if (conf.resToastText() == 0) {
                    throw new ACRAConfigurationException("TOAST mode: you have to define the resToastText parameter in your application @ReportsCrashes() annotation.");
                }
                return;
            case NOTIFICATION:
                if (conf.resNotifTickerText() == 0 || conf.resNotifTitle() == 0 || conf.resNotifText() == 0 || conf.resDialogText() == 0) {
                    throw new ACRAConfigurationException("NOTIFICATION mode: you have to define at least the resNotifTickerText, resNotifTitle, resNotifText, resDialogText parameters in your application @ReportsCrashes() annotation.");
                }
                return;
            case DIALOG:
                if (conf.resDialogText() == 0) {
                    throw new ACRAConfigurationException("DIALOG mode: you have to define at least the resDialogText parameters in your application @ReportsCrashes() annotation.");
                }
                return;
            default:
                return;
        }
    }

    public static SharedPreferences getACRASharedPreferences() {
        ReportsCrashes conf = getConfig();
        return !"".equals(conf.sharedPreferencesName()) ? mApplication.getSharedPreferences(conf.sharedPreferencesName(), conf.sharedPreferencesMode()) : PreferenceManager.getDefaultSharedPreferences(mApplication);
    }

    public static ACRAConfiguration getConfig() {
        if (configProxy == null) {
            if (mApplication == null) {
                log.w(LOG_TAG, "Calling ACRA.getConfig() before ACRA.init() gives you an empty configuration instance. You might prefer calling ACRA.getNewDefaultConfig(Application) to get an instance with default values taken from a @ReportsCrashes annotation.");
            }
            configProxy = getNewDefaultConfig(mApplication);
        }
        return configProxy;
    }

    public static void setConfig(ACRAConfiguration conf) {
        configProxy = conf;
    }

    public static ACRAConfiguration getNewDefaultConfig(Application app) {
        return app != null ? new ACRAConfiguration((ReportsCrashes) app.getClass().getAnnotation(ReportsCrashes.class)) : new ACRAConfiguration(null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isDebuggable() {
        PackageManager pm = mApplication.getPackageManager();
        try {
            return (pm.getApplicationInfo(mApplication.getPackageName(), 0).flags & 2) > 0;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Application getApplication() {
        return mApplication;
    }

    public static void setLog(ACRALog log2) {
        log = log2;
    }
}

package org.acra.collector;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import com.facebook.internal.ServerProtocol;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.acra.ACRA;
import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.annotation.ReportsCrashes;
import org.acra.util.Installation;
import org.acra.util.PackageManagerWrapper;
import org.acra.util.ReportUtils;
/* loaded from: classes.dex */
public final class CrashReportDataFactory {
    private final Time appStartDate;
    private final Context context;
    private final Map<String, String> customParameters = new HashMap();
    private final String initialConfiguration;
    private final SharedPreferences prefs;

    public CrashReportDataFactory(Context context, SharedPreferences prefs, Time appStartDate, String initialConfiguration) {
        this.context = context;
        this.prefs = prefs;
        this.appStartDate = appStartDate;
        this.initialConfiguration = initialConfiguration;
    }

    public String putCustomData(String key, String value) {
        return this.customParameters.put(key, value);
    }

    public String removeCustomData(String key) {
        return this.customParameters.remove(key);
    }

    public String getCustomData(String key) {
        return this.customParameters.get(key);
    }

    public CrashReportData createCrashData(Throwable th, boolean isSilentReport, Thread brokenThread) {
        String deviceId;
        CrashReportData crashReportData = new CrashReportData();
        try {
            List<ReportField> crashReportFields = getReportFields();
            crashReportData.put((CrashReportData) ReportField.STACK_TRACE, (ReportField) getStackTrace(th));
            crashReportData.put((CrashReportData) ReportField.USER_APP_START_DATE, (ReportField) this.appStartDate.format3339(false));
            if (isSilentReport) {
                crashReportData.put((CrashReportData) ReportField.IS_SILENT, (ReportField) ServerProtocol.DIALOG_RETURN_SCOPES_TRUE);
            }
            if (crashReportFields.contains(ReportField.REPORT_ID)) {
                crashReportData.put((CrashReportData) ReportField.REPORT_ID, (ReportField) UUID.randomUUID().toString());
            }
            if (crashReportFields.contains(ReportField.INSTALLATION_ID)) {
                crashReportData.put((CrashReportData) ReportField.INSTALLATION_ID, (ReportField) Installation.id(this.context));
            }
            if (crashReportFields.contains(ReportField.INITIAL_CONFIGURATION)) {
                crashReportData.put((CrashReportData) ReportField.INITIAL_CONFIGURATION, (ReportField) this.initialConfiguration);
            }
            if (crashReportFields.contains(ReportField.CRASH_CONFIGURATION)) {
                crashReportData.put((CrashReportData) ReportField.CRASH_CONFIGURATION, (ReportField) ConfigurationCollector.collectConfiguration(this.context));
            }
            if (!(th instanceof OutOfMemoryError) && crashReportFields.contains(ReportField.DUMPSYS_MEMINFO)) {
                crashReportData.put((CrashReportData) ReportField.DUMPSYS_MEMINFO, (ReportField) DumpSysCollector.collectMemInfo());
            }
            if (crashReportFields.contains(ReportField.PACKAGE_NAME)) {
                crashReportData.put((CrashReportData) ReportField.PACKAGE_NAME, (ReportField) this.context.getPackageName());
            }
            if (crashReportFields.contains(ReportField.BUILD)) {
                crashReportData.put((CrashReportData) ReportField.BUILD, (ReportField) (ReflectionCollector.collectConstants(Build.class) + ReflectionCollector.collectConstants(Build.VERSION.class, "VERSION")));
            }
            if (crashReportFields.contains(ReportField.PHONE_MODEL)) {
                crashReportData.put((CrashReportData) ReportField.PHONE_MODEL, (ReportField) Build.MODEL);
            }
            if (crashReportFields.contains(ReportField.ANDROID_VERSION)) {
                crashReportData.put((CrashReportData) ReportField.ANDROID_VERSION, (ReportField) Build.VERSION.RELEASE);
            }
            if (crashReportFields.contains(ReportField.BRAND)) {
                crashReportData.put((CrashReportData) ReportField.BRAND, (ReportField) Build.BRAND);
            }
            if (crashReportFields.contains(ReportField.PRODUCT)) {
                crashReportData.put((CrashReportData) ReportField.PRODUCT, (ReportField) Build.PRODUCT);
            }
            if (crashReportFields.contains(ReportField.TOTAL_MEM_SIZE)) {
                crashReportData.put((CrashReportData) ReportField.TOTAL_MEM_SIZE, (ReportField) Long.toString(ReportUtils.getTotalInternalMemorySize()));
            }
            if (crashReportFields.contains(ReportField.AVAILABLE_MEM_SIZE)) {
                crashReportData.put((CrashReportData) ReportField.AVAILABLE_MEM_SIZE, (ReportField) Long.toString(ReportUtils.getAvailableInternalMemorySize()));
            }
            if (crashReportFields.contains(ReportField.FILE_PATH)) {
                crashReportData.put((CrashReportData) ReportField.FILE_PATH, (ReportField) ReportUtils.getApplicationFilePath(this.context));
            }
            if (crashReportFields.contains(ReportField.DISPLAY)) {
                crashReportData.put((CrashReportData) ReportField.DISPLAY, (ReportField) DisplayManagerCollector.collectDisplays(this.context));
            }
            if (crashReportFields.contains(ReportField.USER_CRASH_DATE)) {
                Time curDate = new Time();
                curDate.setToNow();
                crashReportData.put((CrashReportData) ReportField.USER_CRASH_DATE, (ReportField) curDate.format3339(false));
            }
            if (crashReportFields.contains(ReportField.CUSTOM_DATA)) {
                crashReportData.put((CrashReportData) ReportField.CUSTOM_DATA, (ReportField) createCustomInfoString());
            }
            if (crashReportFields.contains(ReportField.USER_EMAIL)) {
                crashReportData.put((CrashReportData) ReportField.USER_EMAIL, (ReportField) this.prefs.getString(ACRA.PREF_USER_EMAIL_ADDRESS, "N/A"));
            }
            if (crashReportFields.contains(ReportField.DEVICE_FEATURES)) {
                crashReportData.put((CrashReportData) ReportField.DEVICE_FEATURES, (ReportField) DeviceFeaturesCollector.getFeatures(this.context));
            }
            if (crashReportFields.contains(ReportField.ENVIRONMENT)) {
                crashReportData.put((CrashReportData) ReportField.ENVIRONMENT, (ReportField) ReflectionCollector.collectStaticGettersResults(Environment.class));
            }
            if (crashReportFields.contains(ReportField.SETTINGS_SYSTEM)) {
                crashReportData.put((CrashReportData) ReportField.SETTINGS_SYSTEM, (ReportField) SettingsCollector.collectSystemSettings(this.context));
            }
            if (crashReportFields.contains(ReportField.SETTINGS_SECURE)) {
                crashReportData.put((CrashReportData) ReportField.SETTINGS_SECURE, (ReportField) SettingsCollector.collectSecureSettings(this.context));
            }
            if (crashReportFields.contains(ReportField.SETTINGS_GLOBAL)) {
                crashReportData.put((CrashReportData) ReportField.SETTINGS_GLOBAL, (ReportField) SettingsCollector.collectGlobalSettings(this.context));
            }
            if (crashReportFields.contains(ReportField.SHARED_PREFERENCES)) {
                crashReportData.put((CrashReportData) ReportField.SHARED_PREFERENCES, (ReportField) SharedPreferencesCollector.collect(this.context));
            }
            PackageManagerWrapper pm = new PackageManagerWrapper(this.context);
            PackageInfo pi = pm.getPackageInfo();
            if (pi != null) {
                if (crashReportFields.contains(ReportField.APP_VERSION_CODE)) {
                    crashReportData.put((CrashReportData) ReportField.APP_VERSION_CODE, (ReportField) Integer.toString(pi.versionCode));
                }
                if (crashReportFields.contains(ReportField.APP_VERSION_NAME)) {
                    crashReportData.put((CrashReportData) ReportField.APP_VERSION_NAME, (ReportField) (pi.versionName != null ? pi.versionName : "not set"));
                }
            } else {
                crashReportData.put((CrashReportData) ReportField.APP_VERSION_NAME, (ReportField) "Package info unavailable");
            }
            if (crashReportFields.contains(ReportField.DEVICE_ID) && this.prefs.getBoolean(ACRA.PREF_ENABLE_DEVICE_ID, true) && pm.hasPermission("android.permission.READ_PHONE_STATE") && (deviceId = ReportUtils.getDeviceId(this.context)) != null) {
                crashReportData.put((CrashReportData) ReportField.DEVICE_ID, (ReportField) deviceId);
            }
            if ((this.prefs.getBoolean(ACRA.PREF_ENABLE_SYSTEM_LOGS, true) && pm.hasPermission("android.permission.READ_LOGS")) || Compatibility.getAPILevel() >= 16) {
                Log.i(ACRA.LOG_TAG, "READ_LOGS granted! ACRA can include LogCat and DropBox data.");
                if (crashReportFields.contains(ReportField.LOGCAT)) {
                    crashReportData.put((CrashReportData) ReportField.LOGCAT, (ReportField) LogCatCollector.collectLogCat(null));
                }
                if (crashReportFields.contains(ReportField.EVENTSLOG)) {
                    crashReportData.put((CrashReportData) ReportField.EVENTSLOG, (ReportField) LogCatCollector.collectLogCat("events"));
                }
                if (crashReportFields.contains(ReportField.RADIOLOG)) {
                    crashReportData.put((CrashReportData) ReportField.RADIOLOG, (ReportField) LogCatCollector.collectLogCat("radio"));
                }
                if (crashReportFields.contains(ReportField.DROPBOX)) {
                    crashReportData.put((CrashReportData) ReportField.DROPBOX, (ReportField) DropBoxCollector.read(this.context, ACRA.getConfig().additionalDropBoxTags()));
                }
            } else {
                Log.i(ACRA.LOG_TAG, "READ_LOGS not allowed. ACRA will not include LogCat and DropBox data.");
            }
            if (crashReportFields.contains(ReportField.APPLICATION_LOG)) {
                crashReportData.put((CrashReportData) ReportField.APPLICATION_LOG, (ReportField) LogFileCollector.collectLogFile(this.context, ACRA.getConfig().applicationLogFile(), ACRA.getConfig().applicationLogFileLines()));
            }
            if (crashReportFields.contains(ReportField.MEDIA_CODEC_LIST)) {
                crashReportData.put((CrashReportData) ReportField.MEDIA_CODEC_LIST, (ReportField) MediaCodecListCollector.collecMediaCodecList());
            }
            if (crashReportFields.contains(ReportField.THREAD_DETAILS)) {
                crashReportData.put((CrashReportData) ReportField.THREAD_DETAILS, (ReportField) ThreadCollector.collect(brokenThread));
            }
            if (crashReportFields.contains(ReportField.USER_IP)) {
                crashReportData.put((CrashReportData) ReportField.USER_IP, (ReportField) ReportUtils.getLocalIpAddress());
            }
        } catch (FileNotFoundException e) {
            Log.e(ACRA.LOG_TAG, "Error : application log file " + ACRA.getConfig().applicationLogFile() + " not found.", e);
        } catch (IOException e2) {
            Log.e(ACRA.LOG_TAG, "Error while reading application log file " + ACRA.getConfig().applicationLogFile() + ".", e2);
        } catch (RuntimeException e3) {
            Log.e(ACRA.LOG_TAG, "Error while retrieving crash data", e3);
        }
        return crashReportData;
    }

    private String createCustomInfoString() {
        StringBuilder customInfo = new StringBuilder();
        for (String currentKey : this.customParameters.keySet()) {
            String currentVal = this.customParameters.get(currentKey);
            customInfo.append(currentKey);
            customInfo.append(" = ");
            if (currentVal != null) {
                currentVal = currentVal.replaceAll("\n", "\\\\n");
            }
            customInfo.append(currentVal);
            customInfo.append("\n");
        }
        return customInfo.toString();
    }

    private String getStackTrace(Throwable th) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        for (Throwable cause = th; cause != null; cause = cause.getCause()) {
            cause.printStackTrace(printWriter);
        }
        String stacktraceAsString = result.toString();
        printWriter.close();
        return stacktraceAsString;
    }

    private List<ReportField> getReportFields() {
        ReportField[] fieldsList;
        ReportsCrashes config = ACRA.getConfig();
        ReportField[] customReportFields = config.customReportContent();
        if (customReportFields.length != 0) {
            Log.d(ACRA.LOG_TAG, "Using custom Report Fields");
            fieldsList = customReportFields;
        } else if (config.mailTo() == null || "".equals(config.mailTo())) {
            Log.d(ACRA.LOG_TAG, "Using default Report Fields");
            fieldsList = ACRAConstants.DEFAULT_REPORT_FIELDS;
        } else {
            Log.d(ACRA.LOG_TAG, "Using default Mail Report Fields");
            fieldsList = ACRAConstants.DEFAULT_MAIL_REPORT_FIELDS;
        }
        return Arrays.asList(fieldsList);
    }
}

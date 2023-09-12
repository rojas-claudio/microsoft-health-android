package com.microsoft.applicationinsights.library;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.os.EnvironmentCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import com.microsoft.applicationinsights.contracts.Application;
import com.microsoft.applicationinsights.contracts.Device;
import com.microsoft.applicationinsights.contracts.Internal;
import com.microsoft.applicationinsights.contracts.Operation;
import com.microsoft.applicationinsights.contracts.Session;
import com.microsoft.applicationinsights.contracts.User;
import com.microsoft.applicationinsights.logging.InternalLogging;
import com.microsoft.kapp.utils.Constants;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class TelemetryContext {
    protected static final String SHARED_PREFERENCES_KEY = "APP_INSIGHTS_CONTEXT";
    private static final String TAG = "TelemetryContext";
    protected static final String USER_ACQ_KEY = "USER_ACQ";
    protected static final String USER_ID_KEY = "USER_ID";
    private String appIdForEnvelope;
    private final Application application;
    private Map<String, String> cachedTags;
    private final String instrumentationKey;
    private final Internal internal;
    private final String lastSessionId;
    private final Session session;
    private final SharedPreferences settings;
    private final User user;
    private Operation operation = new Operation();
    private final Device device = new Device();

    public TelemetryContext(Context appContext, String instrumentationKey, String userId) {
        this.settings = appContext.getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
        configDeviceContext(appContext);
        this.session = new Session();
        configSessionContext();
        this.user = new User();
        configUserContext(userId);
        this.internal = new Internal();
        configInternalContext(appContext);
        this.application = new Application();
        configAppContext(appContext);
        this.lastSessionId = null;
        this.instrumentationKey = instrumentationKey;
        this.cachedTags = getCachedTags();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getInstrumentationKey() {
        return this.instrumentationKey;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public User getUser() {
        return this.user;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Device getDevice() {
        return this.device;
    }

    protected Operation getOperation() {
        return this.operation;
    }

    protected Session getSession() {
        return this.session;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Application getApplication() {
        return this.application;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getPackageName() {
        return this.appIdForEnvelope;
    }

    private Map<String, String> getCachedTags() {
        if (this.cachedTags == null) {
            this.cachedTags = new LinkedHashMap();
            this.application.addToHashMap(this.cachedTags);
            this.internal.addToHashMap(this.cachedTags);
            this.operation.addToHashMap(this.cachedTags);
        }
        return this.cachedTags;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Map<String, String> getContextTags() {
        Map<String, String> contextTags = new LinkedHashMap<>();
        contextTags.putAll(getCachedTags());
        this.device.addToHashMap(contextTags);
        this.session.addToHashMap(contextTags);
        this.user.addToHashMap(contextTags);
        return contextTags;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void renewSessionId() {
        String newId = UUID.randomUUID().toString();
        this.session.setId(newId);
    }

    public void renewSessionId(String sessionId) {
        this.session.setId(sessionId);
    }

    protected void configSessionContext() {
        if (this.lastSessionId == null) {
            renewSessionId();
        } else {
            this.session.setId(this.lastSessionId);
        }
    }

    protected void configAppContext(Context appContext) {
        String version = EnvironmentCompat.MEDIA_UNKNOWN;
        this.appIdForEnvelope = "";
        try {
            PackageManager manager = appContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(appContext.getPackageName(), 0);
            if (info.packageName != null) {
                this.appIdForEnvelope = info.packageName;
            }
            String appBuild = Integer.toString(info.versionCode);
            version = String.format("%s (%S)", info.versionName, appBuild);
        } catch (PackageManager.NameNotFoundException e) {
            InternalLogging.warn(TAG, "Could not collect application context");
        } finally {
            this.application.setVer(version);
        }
    }

    public void configUserContext(String userId) {
        String userAcq;
        if (userId == null) {
            userId = this.settings.getString(USER_ID_KEY, null);
            userAcq = this.settings.getString(USER_ACQ_KEY, null);
            if (userId == null || userAcq == null) {
                userId = UUID.randomUUID().toString();
                userAcq = Util.dateToISO8601(new Date());
                saveUserInfo(userId, userAcq);
            }
        } else {
            userAcq = Util.dateToISO8601(new Date());
            saveUserInfo(userId, userAcq);
        }
        this.user.setId(userId);
        this.user.setAccountAcquisitionDate(userAcq);
    }

    private void saveUserInfo(String userId, String acqDateString) {
        SharedPreferences.Editor editor = this.settings.edit();
        editor.putString(USER_ID_KEY, userId);
        editor.putString(USER_ACQ_KEY, acqDateString);
        editor.apply();
    }

    protected void configDeviceContext(Context appContext) {
        String networkString;
        this.device.setOsVersion(Build.VERSION.RELEASE);
        this.device.setOs(Constants.ANDROID_PHONE_IDENTIFIER);
        this.device.setModel(Build.MODEL);
        this.device.setOemName(Build.MANUFACTURER);
        this.device.setLocale(Locale.getDefault().toString());
        updateScreenResolution(appContext);
        ContentResolver resolver = appContext.getContentResolver();
        String deviceIdentifier = Settings.Secure.getString(resolver, "android_id");
        if (deviceIdentifier != null) {
            this.device.setId(Util.tryHashStringSha256(deviceIdentifier));
        }
        TelephonyManager telephonyManager = (TelephonyManager) appContext.getSystemService("phone");
        if (telephonyManager.getPhoneType() != 0) {
            this.device.setType("Phone");
        } else {
            this.device.setType("Tablet");
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) appContext.getSystemService("connectivity");
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            int networkType = activeNetwork.getType();
            switch (networkType) {
                case 0:
                    networkString = "Mobile";
                    break;
                case 1:
                    networkString = "WiFi";
                    break;
                default:
                    networkString = "Unknown";
                    InternalLogging.warn(TAG, "Unknown network type:" + networkType);
                    break;
            }
            this.device.setNetwork(networkString);
        }
        if (Util.isEmulator()) {
            this.device.setModel("[Emulator]" + this.device.getModel());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @SuppressLint({"NewApi"})
    public void updateScreenResolution(Context context) {
        int width;
        int height;
        WindowManager wm = (WindowManager) context.getSystemService("window");
        if (Build.VERSION.SDK_INT >= 17) {
            Point size = new Point();
            wm.getDefaultDisplay().getRealSize(size);
            width = size.x;
            height = size.y;
        } else if (Build.VERSION.SDK_INT >= 13) {
            try {
                Method mGetRawW = Display.class.getMethod("getRawWidth", new Class[0]);
                Method mGetRawH = Display.class.getMethod("getRawHeight", new Class[0]);
                Display display = wm.getDefaultDisplay();
                width = ((Integer) mGetRawW.invoke(display, new Object[0])).intValue();
                height = ((Integer) mGetRawH.invoke(display, new Object[0])).intValue();
            } catch (Exception ex) {
                Point size2 = new Point();
                wm.getDefaultDisplay().getSize(size2);
                width = size2.x;
                height = size2.y;
                InternalLogging.warn(TAG, "Couldn't determine screen resolution: " + ex.toString());
            }
        } else {
            Display d = wm.getDefaultDisplay();
            width = d.getWidth();
            height = d.getHeight();
        }
        String resolutionString = String.valueOf(height) + "x" + String.valueOf(width);
        this.device.setScreenResolution(resolutionString);
    }

    protected void configInternalContext(Context appContext) {
        String sdkVersionString = "";
        if (appContext != null) {
            try {
                Bundle bundle = appContext.getPackageManager().getApplicationInfo(appContext.getPackageName(), 128).metaData;
                if (bundle != null) {
                    sdkVersionString = bundle.getString("com.microsoft.applicationinsights.library.sdkVersion");
                } else {
                    InternalLogging.warn(TAG, "Could not load sdk version from gradle.properties or manifest");
                }
            } catch (PackageManager.NameNotFoundException exception) {
                InternalLogging.warn(TAG, "Error loading SDK version from manifest");
                Log.v(TAG, exception.toString());
            }
        }
        this.internal.setSdkVersion("android:" + sdkVersionString);
    }
}

package com.microsoft.applicationinsights.library;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import com.microsoft.applicationinsights.library.config.ApplicationInsightsConfig;
import com.microsoft.applicationinsights.logging.InternalLogging;
import java.util.Map;
/* loaded from: classes.dex */
public enum ApplicationInsights {
    INSTANCE;
    
    private static boolean DEVELOPER_MODE = false;
    private static final String TAG = "ApplicationInsights";
    private static boolean isRunning;
    private static boolean isSetup;
    private Application application;
    private Map<String, String> commonProperties;
    private Context context;
    private String instrumentationKey;
    private TelemetryContext telemetryContext;
    private String userId;
    private boolean telemetryDisabled = false;
    private boolean exceptionTrackingDisabled = false;
    private boolean autoCollectionDisabled = false;
    private ApplicationInsightsConfig config = new ApplicationInsightsConfig();

    ApplicationInsights() {
        boolean z = false;
        setDeveloperMode((Util.isEmulator() || Util.isDebuggerAttached()) ? true : true);
    }

    public static void setup(Context context) {
        INSTANCE.setupInstance(context, null, null);
    }

    public static void setup(Context context, Application application) {
        INSTANCE.setupInstance(context, application, null);
    }

    public static void setup(Context context, String instrumentationKey) {
        INSTANCE.setupInstance(context, null, instrumentationKey);
    }

    public static void setup(Context context, Application application, String instrumentationKey) {
        INSTANCE.setupInstance(context, application, instrumentationKey);
    }

    public void setupInstance(Context context, Application application, String instrumentationKey) {
        if (!isSetup) {
            if (context != null) {
                this.context = context;
                this.instrumentationKey = instrumentationKey;
                this.application = application;
                isSetup = true;
                InternalLogging.info(TAG, "ApplicationInsights has been setup correctly.", null);
                return;
            }
            InternalLogging.warn(TAG, "ApplicationInsights could not be setup correctly because the given context was null");
        }
    }

    public static void start() {
        INSTANCE.startInstance();
    }

    public void startInstance() {
        if (!isSetup) {
            InternalLogging.warn(TAG, "Could not start ApplicationInsight since it has not been setup correctly.");
        } else if (!isRunning) {
            if (this.instrumentationKey == null) {
                this.instrumentationKey = readInstrumentationKey(this.context);
            }
            this.telemetryContext = new TelemetryContext(this.context, this.instrumentationKey, this.userId);
            EnvelopeFactory.INSTANCE.configure(this.telemetryContext, this.commonProperties);
            Persistence.initialize(this.context);
            Sender.initialize(this.config);
            Channel.initialize(this.config);
            TelemetryClient.initialize(!this.telemetryDisabled);
            LifeCycleTracking.initialize(this.telemetryContext, this.config);
            if (this.application != null && !this.autoCollectionDisabled) {
                LifeCycleTracking.registerPageViewCallbacks(this.application);
                LifeCycleTracking.registerSessionManagementCallbacks(this.application);
            } else {
                InternalLogging.warn(TAG, "Auto collection of page views could not be started, since the given application was null");
            }
            if (!this.exceptionTrackingDisabled) {
                ExceptionTracking.registerExceptionHandler(this.context);
            }
            isRunning = true;
            Sender.getInstance().sendDataOnAppStart();
            InternalLogging.info(TAG, "ApplicationInsights has been started.", null);
        }
    }

    public static void sendPendingData() {
        if (!isRunning) {
            InternalLogging.warn(TAG, "Could not set send pending data, because ApplicationInsights has not been started, yet.");
        } else {
            Channel.getInstance().synchronize();
        }
    }

    public static void enableActivityTracking(Application application) {
        if (!isRunning) {
            InternalLogging.warn(TAG, "Could not set activity tracking, because ApplicationInsights has not been started, yet.");
        } else if (!INSTANCE.telemetryDisabled) {
            LifeCycleTracking.registerActivityLifecycleCallbacks(application);
        }
    }

    public static void enableAutoPageViewTracking() {
        if (!isRunning) {
            InternalLogging.warn(TAG, "Could not set page view tracking, because ApplicationInsights has not been started yet.");
        } else if (INSTANCE.application == null) {
            InternalLogging.warn(TAG, "Could not set page view tracking, because ApplicationInsights has not been setup with an application.");
        } else {
            LifeCycleTracking.registerPageViewCallbacks(INSTANCE.application);
        }
    }

    public static void disableAutoPageViewTracking() {
        if (!isRunning) {
            InternalLogging.warn(TAG, "Could not unset page view tracking, because ApplicationInsights has not been started yet.");
        } else if (INSTANCE.application == null) {
            InternalLogging.warn(TAG, "Could not unset page view tracking, because ApplicationInsights has not been setup with an application.");
        } else {
            LifeCycleTracking.unregisterPageViewCallbacks(INSTANCE.application);
        }
    }

    public static void enableAutoSessionManagement() {
        if (!isRunning) {
            InternalLogging.warn(TAG, "Could not set session management, because ApplicationInsights has not been started yet.");
        } else if (INSTANCE.application == null) {
            InternalLogging.warn(TAG, "Could not set session management, because ApplicationInsights has not been setup with an application.");
        } else {
            LifeCycleTracking.registerSessionManagementCallbacks(INSTANCE.application);
        }
    }

    public static void disableAutoSessionManagement() {
        if (!isRunning) {
            InternalLogging.warn(TAG, "Could not unset session management, because ApplicationInsights has not been started yet.");
        } else if (INSTANCE.application == null) {
            InternalLogging.warn(TAG, "Could not unset session management, because ApplicationInsights has not been setup with an application.");
        } else {
            LifeCycleTracking.unregisterSessionManagementCallbacks(INSTANCE.application);
        }
    }

    public static void setExceptionTrackingDisabled(boolean disabled) {
        if (!isSetup) {
            InternalLogging.warn(TAG, "Could not enable/disable exception tracking, because ApplicationInsights has not been setup correctly.");
        } else if (isRunning) {
            InternalLogging.warn(TAG, "Could not enable/disable exception tracking, because ApplicationInsights has already been started.");
        } else {
            INSTANCE.exceptionTrackingDisabled = disabled;
        }
    }

    public static void setTelemetryDisabled(boolean disabled) {
        if (!isSetup) {
            InternalLogging.warn(TAG, "Could not enable/disable telemetry, because ApplicationInsights has not been setup correctly.");
        } else if (isRunning) {
            InternalLogging.warn(TAG, "Could not enable/disable telemetry, because ApplicationInsights has already been started.");
        } else {
            INSTANCE.telemetryDisabled = disabled;
        }
    }

    public static void setAutoCollectionDisabled(boolean disabled) {
        if (!isSetup) {
            InternalLogging.warn(TAG, "Could not enable/disable auto collection, because ApplicationInsights has not been setup correctly.");
        } else if (isRunning) {
            InternalLogging.warn(TAG, "Could not enable/disable auto collection, because ApplicationInsights has already been started.");
        } else {
            INSTANCE.autoCollectionDisabled = disabled;
        }
    }

    public static Map<String, String> getCommonProperties() {
        return INSTANCE.commonProperties;
    }

    public static void setCommonProperties(Map<String, String> commonProperties) {
        if (!isSetup) {
            InternalLogging.warn(TAG, "Could not set common properties, because ApplicationInsights has not been setup correctly.");
        } else if (isRunning) {
            InternalLogging.warn(TAG, "Could not set common properties, because ApplicationInsights has already been started.");
        } else {
            INSTANCE.commonProperties = commonProperties;
        }
    }

    public static void setDeveloperMode(boolean developerMode) {
        DEVELOPER_MODE = developerMode;
    }

    public static boolean isDeveloperMode() {
        return DEVELOPER_MODE;
    }

    private String readInstrumentationKey(Context context) {
        String iKey = "";
        if (context != null) {
            try {
                Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
                if (bundle != null) {
                    iKey = bundle.getString("com.microsoft.applicationinsights.instrumentationKey");
                } else {
                    logInstrumentationInstructions();
                }
            } catch (PackageManager.NameNotFoundException exception) {
                logInstrumentationInstructions();
                Log.v(TAG, exception.toString());
            }
        }
        return iKey;
    }

    public Context getContext() {
        return this.context;
    }

    private static void logInstrumentationInstructions() {
        InternalLogging.error("MissingInstrumentationkey", "No instrumentation key found.\nSet the instrumentation key in AndroidManifest.xml\n<meta-data\nandroid:name=\"com.microsoft.applicationinsights.instrumentationKey\"\nandroid:value=\"${AI_INSTRUMENTATION_KEY}\" />");
    }

    public static ApplicationInsightsConfig getConfig() {
        return INSTANCE.config;
    }

    public void setConfig(ApplicationInsightsConfig config) {
        if (!isSetup) {
            InternalLogging.warn(TAG, "Could not set telemetry configuration, because ApplicationInsights has not been setup correctly.");
        } else if (isRunning) {
            InternalLogging.warn(TAG, "Could not set telemetry configuration, because ApplicationInsights has already been started.");
        } else {
            INSTANCE.config = config;
        }
    }

    public static void renewSession(String sessionId) {
        if (!INSTANCE.telemetryDisabled && INSTANCE.telemetryContext != null) {
            INSTANCE.telemetryContext.renewSessionId(sessionId);
        }
    }

    public static void setUserId(String userId) {
        if (isRunning) {
            INSTANCE.telemetryContext.configUserContext(userId);
        } else {
            INSTANCE.userId = userId;
        }
    }

    protected static String getInstrumentationKey() {
        return INSTANCE.instrumentationKey;
    }
}

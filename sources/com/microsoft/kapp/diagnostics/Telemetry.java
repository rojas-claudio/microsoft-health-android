package com.microsoft.kapp.diagnostics;

import android.app.Application;
import com.microsoft.applicationinsights.contracts.EventData;
import com.microsoft.applicationinsights.library.ApplicationInsights;
import com.microsoft.applicationinsights.library.TelemetryClient;
import com.microsoft.band.build.BranchInfo;
import com.microsoft.kapp.debug.KappConfig;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public final class Telemetry {
    private static final String DOGFOODING = "1f95adcc-2c18-4c52-bb50-9ccea026c2f2";
    private static final String ENGINEERING = "a72d45ae-fe44-410c-b4dc-ede1d0ac9938";
    private static final String ODSUSERID = "ODSUserId";
    private static final String PUBLIC = "85a5b488-0f6a-4e7e-a68b-bf875eee81bf";
    protected boolean mIsEnabled;

    private Telemetry() {
        this.mIsEnabled = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class TelemetryHolder {
        static Telemetry instance = new Telemetry();

        TelemetryHolder() {
        }
    }

    private static Telemetry getInstance() {
        return TelemetryHolder.instance;
    }

    private static String getApplicationInsightsId() {
        if (BranchInfo.BranchName != "Main" && BranchInfo.BranchName != "Stabilization") {
            if (BranchInfo.BranchName == "Release" || KappConfig.isDebbuging) {
                return DOGFOODING;
            }
            if (Compatibility.isPublicReleaseBranch()) {
                return PUBLIC;
            }
            String message = String.format("Unrecognized branch name %s, could not determine the Application Insights id to use. You want to udpate the code to make sure it is still valid.", BranchInfo.BranchName);
            throw new RuntimeException(message);
        }
        return ENGINEERING;
    }

    private static TelemetryClient getTelemetryClient() {
        return TelemetryClient.getInstance();
    }

    public static void initialize(Application application) {
        if (getInstance().mIsEnabled) {
            ApplicationInsights.setup(application, getApplicationInsightsId());
            ApplicationInsights.start();
            ApplicationInsights.disableAutoPageViewTracking();
            ApplicationInsights.enableAutoSessionManagement();
        }
    }

    public static void setIsEnabled(boolean isEnabled) {
        getInstance().mIsEnabled = isEnabled;
    }

    public static void setUserId(String userId) {
        ApplicationInsights.setUserId(userId);
        Map<String, String> commonProperties = ApplicationInsights.getCommonProperties();
        if (commonProperties == null) {
            commonProperties = new HashMap<>();
        }
        commonProperties.put(ODSUSERID, userId);
        ApplicationInsights.setCommonProperties(commonProperties);
    }

    public static void removeUserId() {
        Map<String, String> commonProperties = ApplicationInsights.getCommonProperties();
        if (commonProperties != null) {
            commonProperties.remove(ODSUSERID);
            ApplicationInsights.setCommonProperties(commonProperties);
        }
    }

    public static void logPage(String name) {
        if (getInstance().mIsEnabled) {
            getTelemetryClient().trackPageView(name);
        }
    }

    public static void logPage(String name, Map<String, String> properties) {
        if (getInstance().mIsEnabled) {
            getTelemetryClient().trackPageView(name, properties);
        }
    }

    public static EventData startTimedEvent(String eventName) {
        TimedEventData timedEvent = new TimedEventData();
        timedEvent.setName(eventName);
        timedEvent.setStartTime(System.currentTimeMillis());
        timedEvent.setProperties(new HashMap());
        timedEvent.setMeasurements(new HashMap());
        return timedEvent;
    }

    public static void endTimedEvent(EventData event) {
        if (getInstance().mIsEnabled && (event instanceof TimedEventData)) {
            long duration = System.currentTimeMillis() - ((TimedEventData) event).getStartTime();
            event.getMeasurements().put(TelemetryConstants.TimedEvents.Cloud.Dimensions.DURATION, Double.valueOf(duration));
            getTelemetryClient().trackEvent(event.getName(), event.getProperties(), event.getMeasurements());
        }
    }

    public static void logEvent(String eventName) {
        if (getInstance().mIsEnabled) {
            getTelemetryClient().trackEvent(eventName);
        }
    }

    public static void logEvent(String eventName, HashMap<String, String> properties, HashMap<String, Double> metrics) {
        if (getInstance().mIsEnabled) {
            getTelemetryClient().trackEvent(eventName, properties, metrics);
        }
    }

    public static Map<String, String> createSingleProperty(String name, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(name, value);
        return map;
    }
}

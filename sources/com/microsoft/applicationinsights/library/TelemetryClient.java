package com.microsoft.applicationinsights.library;

import com.microsoft.applicationinsights.contracts.shared.ITelemetry;
import com.microsoft.applicationinsights.library.CreateDataTask;
import com.microsoft.applicationinsights.logging.InternalLogging;
import java.util.Map;
/* loaded from: classes.dex */
public class TelemetryClient {
    public static final String TAG = "TelemetryClient";
    private static TelemetryClient instance;
    private final boolean telemetryEnabled;
    private static volatile boolean isTelemetryClientLoaded = false;
    private static final Object LOCK = new Object();

    protected TelemetryClient(boolean telemetryEnabled) {
        this.telemetryEnabled = telemetryEnabled;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void initialize(boolean telemetryEnabled) {
        if (!isTelemetryClientLoaded) {
            synchronized (LOCK) {
                if (!isTelemetryClientLoaded) {
                    isTelemetryClientLoaded = true;
                    instance = new TelemetryClient(telemetryEnabled);
                }
            }
        }
    }

    public static TelemetryClient getInstance() {
        if (instance == null) {
            InternalLogging.error(TAG, "getInstance was called before initialization");
        }
        return instance;
    }

    public void trackEvent(String eventName) {
        trackEvent(eventName, null, null);
    }

    public void trackEvent(String eventName, Map<String, String> properties) {
        trackEvent(eventName, properties, null);
    }

    public void track(ITelemetry telemetry) {
        if (isTelemetryEnabled()) {
            new CreateDataTask(telemetry).execute(new Void[0]);
        }
    }

    public void trackEvent(String eventName, Map<String, String> properties, Map<String, Double> measurements) {
        if (isTelemetryEnabled()) {
            new CreateDataTask(CreateDataTask.DataType.EVENT, eventName, properties, measurements).execute(new Void[0]);
        }
    }

    public void trackTrace(String message) {
        trackTrace(message, null);
    }

    public void trackTrace(String message, Map<String, String> properties) {
        if (isTelemetryEnabled()) {
            new CreateDataTask(CreateDataTask.DataType.TRACE, message, properties, null).execute(new Void[0]);
        }
    }

    public void trackMetric(String name, double value) {
        if (isTelemetryEnabled()) {
            new CreateDataTask(CreateDataTask.DataType.METRIC, name, value).execute(new Void[0]);
        }
    }

    public void trackHandledException(Throwable handledException) {
        trackHandledException(handledException, null);
    }

    public void trackHandledException(Throwable handledException, Map<String, String> properties) {
        if (isTelemetryEnabled()) {
            new CreateDataTask(CreateDataTask.DataType.HANDLED_EXCEPTION, handledException, properties).execute(new Void[0]);
        }
    }

    public void trackPageView(String pageName) {
        trackPageView(pageName, null, null);
    }

    public void trackPageView(String pageName, Map<String, String> properties) {
        trackPageView(pageName, properties, null);
    }

    public void trackPageView(String pageName, Map<String, String> properties, Map<String, Double> measurements) {
        if (isTelemetryEnabled()) {
            new CreateDataTask(CreateDataTask.DataType.PAGE_VIEW, pageName, properties, null).execute(new Void[0]);
        }
    }

    public void trackNewSession() {
        if (isTelemetryEnabled()) {
            new CreateDataTask(CreateDataTask.DataType.NEW_SESSION).execute(new Void[0]);
        }
    }

    protected boolean isTelemetryEnabled() {
        if (!this.telemetryEnabled) {
            InternalLogging.warn(TAG, "Could not track telemetry item, because telemetry feature is disabled.");
        }
        return this.telemetryEnabled;
    }
}

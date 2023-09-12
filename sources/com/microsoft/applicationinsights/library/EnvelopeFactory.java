package com.microsoft.applicationinsights.library;

import com.microsoft.applicationinsights.contracts.CrashData;
import com.microsoft.applicationinsights.contracts.CrashDataHeaders;
import com.microsoft.applicationinsights.contracts.CrashDataThread;
import com.microsoft.applicationinsights.contracts.CrashDataThreadFrame;
import com.microsoft.applicationinsights.contracts.Data;
import com.microsoft.applicationinsights.contracts.DataPoint;
import com.microsoft.applicationinsights.contracts.Envelope;
import com.microsoft.applicationinsights.contracts.EventData;
import com.microsoft.applicationinsights.contracts.MessageData;
import com.microsoft.applicationinsights.contracts.MetricData;
import com.microsoft.applicationinsights.contracts.PageViewData;
import com.microsoft.applicationinsights.contracts.SessionStateData;
import com.microsoft.applicationinsights.contracts.shared.ITelemetry;
import com.microsoft.applicationinsights.contracts.shared.ITelemetryData;
import com.microsoft.applicationinsights.logging.InternalLogging;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public enum EnvelopeFactory {
    INSTANCE;
    
    protected static final int CONTRACT_VERSION = 2;
    private static final String TAG = "EnvelopeManager";
    private Map<String, String> commonProperties;
    private boolean configured;
    private TelemetryContext context;

    protected void configure(TelemetryContext context) {
        configure(context, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void configure(TelemetryContext context, Map<String, String> commonProperties) {
        this.context = context;
        this.commonProperties = commonProperties;
        this.configured = true;
    }

    protected Envelope createEnvelope() {
        Envelope envelope = new Envelope();
        this.context.updateScreenResolution(ApplicationInsights.INSTANCE.getContext());
        envelope.setAppId(this.context.getPackageName());
        envelope.setAppVer(this.context.getApplication().getVer());
        envelope.setTime(Util.dateToISO8601(new Date()));
        envelope.setIKey(this.context.getInstrumentationKey());
        envelope.setUserId(this.context.getUser().getId());
        envelope.setDeviceId(this.context.getDevice().getId());
        envelope.setOsVer(this.context.getDevice().getOsVersion());
        envelope.setOs(this.context.getDevice().getOs());
        Map<String, String> tags = this.context.getContextTags();
        if (tags != null) {
            envelope.setTags(tags);
        }
        return envelope;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Envelope createEnvelope(ITelemetry telemetryData) {
        addCommonProperties(telemetryData);
        Data<ITelemetryData> data = new Data<>();
        data.setBaseData(telemetryData);
        data.setBaseType(telemetryData.getBaseType());
        Envelope envelope = createEnvelope();
        envelope.setData(data);
        envelope.setName(telemetryData.getEnvelopeName());
        return envelope;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Envelope createEventEnvelope(String eventName, Map<String, String> properties, Map<String, Double> measurements) {
        if (!isConfigured()) {
            return null;
        }
        EventData telemetry = new EventData();
        telemetry.setName(ensureNotNull(eventName));
        telemetry.setProperties(properties);
        telemetry.setMeasurements(measurements);
        Envelope envelope = createEnvelope(telemetry);
        return envelope;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Envelope createTraceEnvelope(String message, Map<String, String> properties) {
        if (!isConfigured()) {
            return null;
        }
        MessageData telemetry = new MessageData();
        telemetry.setMessage(ensureNotNull(message));
        telemetry.setProperties(properties);
        Envelope envelope = createEnvelope(telemetry);
        return envelope;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Envelope createMetricEnvelope(String name, double value) {
        if (!isConfigured()) {
            return null;
        }
        MetricData telemetry = new MetricData();
        DataPoint data = new DataPoint();
        data.setCount(1);
        data.setKind(0);
        data.setMax(Double.valueOf(value));
        data.setMax(Double.valueOf(value));
        data.setName(ensureNotNull(name));
        data.setValue(value);
        List<DataPoint> metricsList = new ArrayList<>();
        metricsList.add(data);
        telemetry.setMetrics(metricsList);
        Envelope envelope = createEnvelope(telemetry);
        return envelope;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Envelope createExceptionEnvelope(Throwable exception, Map<String, String> properties) {
        if (!isConfigured()) {
            return null;
        }
        CrashData telemetry = getCrashData(exception, properties);
        Envelope envelope = createEnvelope(telemetry);
        return envelope;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Envelope createPageViewEnvelope(String pageName, Map<String, String> properties, Map<String, Double> measurements) {
        if (!isConfigured()) {
            return null;
        }
        PageViewData telemetry = new PageViewData();
        telemetry.setName(ensureNotNull(pageName));
        telemetry.setUrl(null);
        telemetry.setProperties(properties);
        telemetry.setMeasurements(measurements);
        Envelope envelope = createEnvelope(telemetry);
        return envelope;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Envelope createNewSessionEnvelope() {
        if (!isConfigured()) {
            return null;
        }
        SessionStateData telemetry = new SessionStateData();
        telemetry.setState(0);
        Envelope envelope = createEnvelope(telemetry);
        return envelope;
    }

    protected void addCommonProperties(ITelemetry telemetry) {
        telemetry.setVer(2);
        if (this.commonProperties != null) {
            Map<String, String> map = telemetry.getProperties();
            if (map != null) {
                map.putAll(this.commonProperties);
            }
            telemetry.setProperties(map);
        }
    }

    private String ensureNotNull(String input) {
        if (input == null) {
            return "";
        }
        return input;
    }

    protected void setCommonProperties(Map<String, String> commonProperties) {
        this.commonProperties = commonProperties;
    }

    private CrashData getCrashData(Throwable exception, Map<String, String> properties) {
        Throwable localException = exception;
        if (localException == null) {
            localException = new Exception();
        }
        List<CrashDataThreadFrame> stackFrames = new ArrayList<>();
        StackTraceElement[] stack = localException.getStackTrace();
        for (int i = 0; i < stack.length - 1; i++) {
            StackTraceElement rawFrame = stack[i];
            CrashDataThreadFrame frame = new CrashDataThreadFrame();
            frame.setSymbol(rawFrame.toString());
            stackFrames.add(frame);
            frame.setAddress("");
        }
        CrashDataThread crashDataThread = new CrashDataThread();
        crashDataThread.setFrames(stackFrames);
        List<CrashDataThread> threads = new ArrayList<>(1);
        threads.add(crashDataThread);
        CrashDataHeaders crashDataHeaders = new CrashDataHeaders();
        crashDataHeaders.setId(UUID.randomUUID().toString());
        String message = localException.getMessage();
        crashDataHeaders.setExceptionReason(ensureNotNull(message));
        crashDataHeaders.setExceptionType(localException.getClass().getName());
        crashDataHeaders.setApplicationIdentifier(this.context.getPackageName());
        CrashData crashData = new CrashData();
        crashData.setThreads(threads);
        crashData.setHeaders(crashDataHeaders);
        crashData.setProperties(properties);
        return crashData;
    }

    protected boolean isConfigured() {
        if (!this.configured) {
            InternalLogging.warn(TAG, "Could not create telemetry data. You have to setup & start ApplicationInsights first.");
        }
        return this.configured;
    }
}

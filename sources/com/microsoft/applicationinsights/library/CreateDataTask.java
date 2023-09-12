package com.microsoft.applicationinsights.library;

import android.os.AsyncTask;
import com.microsoft.applicationinsights.contracts.Envelope;
import com.microsoft.applicationinsights.contracts.shared.ITelemetry;
import java.util.Map;
/* loaded from: classes.dex */
class CreateDataTask extends AsyncTask<Void, Void, Void> {
    private Throwable exception;
    private Map<String, Double> measurements;
    private double metric;
    private String name;
    private Map<String, String> properties;
    private ITelemetry telemetry;
    private final DataType type;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public enum DataType {
        NONE,
        EVENT,
        TRACE,
        METRIC,
        PAGE_VIEW,
        HANDLED_EXCEPTION,
        UNHANDLED_EXCEPTION,
        NEW_SESSION
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CreateDataTask(ITelemetry telemetry) {
        this.type = DataType.NONE;
        this.telemetry = telemetry;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CreateDataTask(DataType type) {
        this.type = type;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CreateDataTask(DataType type, String metricName, double metric) {
        this.type = type;
        this.name = metricName;
        this.metric = metric;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CreateDataTask(DataType type, String name, Map<String, String> properties, Map<String, Double> measurements) {
        this.type = type;
        this.name = name;
        this.properties = properties;
        this.measurements = measurements;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CreateDataTask(DataType type, Throwable exception, Map<String, String> properties) {
        this.type = type;
        this.exception = exception;
        this.properties = properties;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Void doInBackground(Void... params) {
        Envelope envelope = null;
        switch (this.type) {
            case NONE:
                if (this.telemetry != null) {
                    envelope = EnvelopeFactory.INSTANCE.createEnvelope(this.telemetry);
                    break;
                }
                break;
            case EVENT:
                envelope = EnvelopeFactory.INSTANCE.createEventEnvelope(this.name, this.properties, this.measurements);
                break;
            case PAGE_VIEW:
                envelope = EnvelopeFactory.INSTANCE.createPageViewEnvelope(this.name, this.properties, this.measurements);
                break;
            case TRACE:
                envelope = EnvelopeFactory.INSTANCE.createTraceEnvelope(this.name, this.properties);
                break;
            case METRIC:
                envelope = EnvelopeFactory.INSTANCE.createMetricEnvelope(this.name, this.metric);
                break;
            case NEW_SESSION:
                envelope = EnvelopeFactory.INSTANCE.createNewSessionEnvelope();
                break;
            case HANDLED_EXCEPTION:
            case UNHANDLED_EXCEPTION:
                envelope = EnvelopeFactory.INSTANCE.createExceptionEnvelope(this.exception, this.properties);
                break;
        }
        if (envelope != null) {
            Channel channel = Channel.getInstance();
            if (this.type == DataType.UNHANDLED_EXCEPTION) {
                channel.processUnhandledException(envelope);
                return null;
            }
            channel.enqueue(envelope);
            return null;
        }
        return null;
    }
}

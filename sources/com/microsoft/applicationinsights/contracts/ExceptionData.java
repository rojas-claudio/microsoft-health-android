package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.ITelemetry;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class ExceptionData extends Domain implements ITelemetry {
    private int crashThreadId;
    private List<ExceptionDetails> exceptions;
    private String handledAt;
    private Map<String, Double> measurements;
    private String problemId;
    private Map<String, String> properties;
    private int severityLevel;
    private int ver = 2;

    public ExceptionData() {
        InitializeFields();
    }

    @Override // com.microsoft.applicationinsights.contracts.Domain, com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public String getEnvelopeName() {
        return "Microsoft.ApplicationInsights.Exception";
    }

    @Override // com.microsoft.applicationinsights.contracts.Domain, com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public String getBaseType() {
        return "Microsoft.ApplicationInsights.ExceptionData";
    }

    public int getVer() {
        return this.ver;
    }

    @Override // com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public void setVer(int value) {
        this.ver = value;
    }

    public String getHandledAt() {
        return this.handledAt;
    }

    public void setHandledAt(String value) {
        this.handledAt = value;
    }

    public List<ExceptionDetails> getExceptions() {
        if (this.exceptions == null) {
            this.exceptions = new ArrayList();
        }
        return this.exceptions;
    }

    public void setExceptions(List<ExceptionDetails> value) {
        this.exceptions = value;
    }

    public int getSeverityLevel() {
        return this.severityLevel;
    }

    public void setSeverityLevel(int value) {
        this.severityLevel = value;
    }

    public String getProblemId() {
        return this.problemId;
    }

    public void setProblemId(String value) {
        this.problemId = value;
    }

    public int getCrashThreadId() {
        return this.crashThreadId;
    }

    public void setCrashThreadId(int value) {
        this.crashThreadId = value;
    }

    @Override // com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public Map<String, String> getProperties() {
        if (this.properties == null) {
            this.properties = new LinkedHashMap();
        }
        return this.properties;
    }

    @Override // com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public void setProperties(Map<String, String> value) {
        this.properties = value;
    }

    public Map<String, Double> getMeasurements() {
        if (this.measurements == null) {
            this.measurements = new LinkedHashMap();
        }
        return this.measurements;
    }

    public void setMeasurements(Map<String, Double> value) {
        this.measurements = value;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.applicationinsights.contracts.Domain
    public String serializeContent(Writer writer) throws IOException {
        String prefix = super.serializeContent(writer);
        writer.write(prefix + "\"ver\":");
        writer.write(JsonHelper.convert(Integer.valueOf(this.ver)));
        writer.write(",\"handledAt\":");
        writer.write(JsonHelper.convert(this.handledAt));
        writer.write(",\"exceptions\":");
        JsonHelper.writeList(writer, this.exceptions);
        String prefix2 = ",";
        if (this.severityLevel != 0) {
            writer.write(",\"severityLevel\":");
            writer.write(JsonHelper.convert(Integer.valueOf(this.severityLevel)));
            prefix2 = ",";
        }
        if (this.problemId != null) {
            writer.write(prefix2 + "\"problemId\":");
            writer.write(JsonHelper.convert(this.problemId));
            prefix2 = ",";
        }
        if (this.crashThreadId != 0) {
            writer.write(prefix2 + "\"crashThreadId\":");
            writer.write(JsonHelper.convert(Integer.valueOf(this.crashThreadId)));
            prefix2 = ",";
        }
        if (this.properties != null) {
            writer.write(prefix2 + "\"properties\":");
            JsonHelper.writeDictionary(writer, this.properties);
            prefix2 = ",";
        }
        if (this.measurements != null) {
            writer.write(prefix2 + "\"measurements\":");
            JsonHelper.writeDictionary(writer, this.measurements);
            return ",";
        }
        return prefix2;
    }

    @Override // com.microsoft.applicationinsights.contracts.Domain
    protected void InitializeFields() {
    }
}

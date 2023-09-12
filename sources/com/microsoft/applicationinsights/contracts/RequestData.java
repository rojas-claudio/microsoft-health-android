package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.ITelemetry;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class RequestData extends Domain implements ITelemetry {
    private String duration;
    private String httpMethod;
    private String id;
    private Map<String, Double> measurements;
    private String name;
    private Map<String, String> properties;
    private String responseCode;
    private String startTime;
    private boolean success;
    private String url;
    private int ver = 2;

    public RequestData() {
        InitializeFields();
    }

    @Override // com.microsoft.applicationinsights.contracts.Domain, com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public String getEnvelopeName() {
        return "Microsoft.ApplicationInsights.Request";
    }

    @Override // com.microsoft.applicationinsights.contracts.Domain, com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public String getBaseType() {
        return "Microsoft.ApplicationInsights.RequestData";
    }

    public int getVer() {
        return this.ver;
    }

    @Override // com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public void setVer(int value) {
        this.ver = value;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String value) {
        this.startTime = value;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String value) {
        this.duration = value;
    }

    public String getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(String value) {
        this.responseCode = value;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(boolean value) {
        this.success = value;
    }

    public String getHttpMethod() {
        return this.httpMethod;
    }

    public void setHttpMethod(String value) {
        this.httpMethod = value;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String value) {
        this.url = value;
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
        writer.write(",\"id\":");
        writer.write(JsonHelper.convert(this.id));
        String prefix2 = ",";
        if (this.name != null) {
            writer.write(",\"name\":");
            writer.write(JsonHelper.convert(this.name));
            prefix2 = ",";
        }
        writer.write(prefix2 + "\"startTime\":");
        writer.write(JsonHelper.convert(this.startTime));
        writer.write(",\"duration\":");
        writer.write(JsonHelper.convert(this.duration));
        writer.write(",\"responseCode\":");
        writer.write(JsonHelper.convert(this.responseCode));
        writer.write(",\"success\":");
        writer.write(JsonHelper.convert(this.success));
        String prefix3 = ",";
        if (this.httpMethod != null) {
            writer.write(",\"httpMethod\":");
            writer.write(JsonHelper.convert(this.httpMethod));
            prefix3 = ",";
        }
        if (this.url != null) {
            writer.write(prefix3 + "\"url\":");
            writer.write(JsonHelper.convert(this.url));
            prefix3 = ",";
        }
        if (this.properties != null) {
            writer.write(prefix3 + "\"properties\":");
            JsonHelper.writeDictionary(writer, this.properties);
            prefix3 = ",";
        }
        if (this.measurements != null) {
            writer.write(prefix3 + "\"measurements\":");
            JsonHelper.writeDictionary(writer, this.measurements);
            return ",";
        }
        return prefix3;
    }

    @Override // com.microsoft.applicationinsights.contracts.Domain
    protected void InitializeFields() {
    }
}

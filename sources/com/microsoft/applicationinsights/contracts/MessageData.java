package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.ITelemetry;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class MessageData extends Domain implements ITelemetry {
    private String message;
    private Map<String, String> properties;
    private int severityLevel;
    private int ver = 2;

    public MessageData() {
        InitializeFields();
    }

    @Override // com.microsoft.applicationinsights.contracts.Domain, com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public String getEnvelopeName() {
        return "Microsoft.ApplicationInsights.Message";
    }

    @Override // com.microsoft.applicationinsights.contracts.Domain, com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public String getBaseType() {
        return "Microsoft.ApplicationInsights.MessageData";
    }

    public int getVer() {
        return this.ver;
    }

    @Override // com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public void setVer(int value) {
        this.ver = value;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String value) {
        this.message = value;
    }

    public int getSeverityLevel() {
        return this.severityLevel;
    }

    public void setSeverityLevel(int value) {
        this.severityLevel = value;
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.applicationinsights.contracts.Domain
    public String serializeContent(Writer writer) throws IOException {
        String prefix = super.serializeContent(writer);
        writer.write(prefix + "\"ver\":");
        writer.write(JsonHelper.convert(Integer.valueOf(this.ver)));
        writer.write(",\"message\":");
        writer.write(JsonHelper.convert(this.message));
        String prefix2 = ",";
        if (this.severityLevel != 0) {
            writer.write(",\"severityLevel\":");
            writer.write(JsonHelper.convert(Integer.valueOf(this.severityLevel)));
            prefix2 = ",";
        }
        if (this.properties != null) {
            writer.write(prefix2 + "\"properties\":");
            JsonHelper.writeDictionary(writer, this.properties);
            return ",";
        }
        return prefix2;
    }

    @Override // com.microsoft.applicationinsights.contracts.Domain
    protected void InitializeFields() {
    }
}

package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.ITelemetry;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class EventData extends Domain implements ITelemetry {
    private Map<String, Double> measurements;
    private String name;
    private Map<String, String> properties;
    private int ver = 2;

    public EventData() {
        InitializeFields();
    }

    @Override // com.microsoft.applicationinsights.contracts.Domain, com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public String getEnvelopeName() {
        return "Microsoft.ApplicationInsights.Event";
    }

    @Override // com.microsoft.applicationinsights.contracts.Domain, com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public String getBaseType() {
        return "Microsoft.ApplicationInsights.EventData";
    }

    public int getVer() {
        return this.ver;
    }

    @Override // com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public void setVer(int value) {
        this.ver = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
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
        writer.write(",\"name\":");
        writer.write(JsonHelper.convert(this.name));
        String prefix2 = ",";
        if (this.properties != null) {
            writer.write(",\"properties\":");
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

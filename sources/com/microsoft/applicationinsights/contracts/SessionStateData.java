package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.ITelemetry;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
/* loaded from: classes.dex */
public class SessionStateData extends Domain implements ITelemetry {
    private int ver = 2;
    private int state = 0;

    public SessionStateData() {
        InitializeFields();
    }

    @Override // com.microsoft.applicationinsights.contracts.Domain, com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public String getEnvelopeName() {
        return "Microsoft.ApplicationInsights.SessionState";
    }

    @Override // com.microsoft.applicationinsights.contracts.Domain, com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public String getBaseType() {
        return "Microsoft.ApplicationInsights.SessionStateData";
    }

    public int getVer() {
        return this.ver;
    }

    @Override // com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public void setVer(int value) {
        this.ver = value;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int value) {
        this.state = value;
    }

    @Override // com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public Map<String, String> getProperties() {
        return null;
    }

    @Override // com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public void setProperties(Map<String, String> value) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.applicationinsights.contracts.Domain
    public String serializeContent(Writer writer) throws IOException {
        String prefix = super.serializeContent(writer);
        writer.write(prefix + "\"ver\":");
        writer.write(JsonHelper.convert(Integer.valueOf(this.ver)));
        writer.write(",\"state\":");
        writer.write(JsonHelper.convert(Integer.valueOf(this.state)));
        return ",";
    }

    @Override // com.microsoft.applicationinsights.contracts.Domain
    protected void InitializeFields() {
    }
}

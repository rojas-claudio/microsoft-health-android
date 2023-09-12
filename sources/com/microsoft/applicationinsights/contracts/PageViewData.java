package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.ITelemetry;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
/* loaded from: classes.dex */
public class PageViewData extends EventData implements ITelemetry {
    private String duration;
    private String url;

    public PageViewData() {
        InitializeFields();
    }

    @Override // com.microsoft.applicationinsights.contracts.EventData, com.microsoft.applicationinsights.contracts.Domain, com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public String getEnvelopeName() {
        return "Microsoft.ApplicationInsights.PageView";
    }

    @Override // com.microsoft.applicationinsights.contracts.EventData, com.microsoft.applicationinsights.contracts.Domain, com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public String getBaseType() {
        return "Microsoft.ApplicationInsights.PageViewData";
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String value) {
        this.url = value;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String value) {
        this.duration = value;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.applicationinsights.contracts.EventData, com.microsoft.applicationinsights.contracts.Domain
    public String serializeContent(Writer writer) throws IOException {
        String prefix = super.serializeContent(writer);
        if (this.url != null) {
            writer.write(prefix + "\"url\":");
            writer.write(JsonHelper.convert(this.url));
            prefix = ",";
        }
        if (this.duration != null) {
            writer.write(prefix + "\"duration\":");
            writer.write(JsonHelper.convert(this.duration));
            return ",";
        }
        return prefix;
    }

    @Override // com.microsoft.applicationinsights.contracts.EventData, com.microsoft.applicationinsights.contracts.Domain
    protected void InitializeFields() {
    }
}

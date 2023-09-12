package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.ITelemetry;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
/* loaded from: classes.dex */
public class PageViewPerfData extends PageViewData implements ITelemetry {
    private String domProcessing;
    private String networkConnect;
    private String perfTotal;
    private String receivedResponse;
    private String sentRequest;

    public PageViewPerfData() {
        InitializeFields();
    }

    @Override // com.microsoft.applicationinsights.contracts.PageViewData, com.microsoft.applicationinsights.contracts.EventData, com.microsoft.applicationinsights.contracts.Domain, com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public String getEnvelopeName() {
        return "Microsoft.ApplicationInsights.PageViewPerf";
    }

    @Override // com.microsoft.applicationinsights.contracts.PageViewData, com.microsoft.applicationinsights.contracts.EventData, com.microsoft.applicationinsights.contracts.Domain, com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public String getBaseType() {
        return "Microsoft.ApplicationInsights.PageViewPerfData";
    }

    public String getPerfTotal() {
        return this.perfTotal;
    }

    public void setPerfTotal(String value) {
        this.perfTotal = value;
    }

    public String getNetworkConnect() {
        return this.networkConnect;
    }

    public void setNetworkConnect(String value) {
        this.networkConnect = value;
    }

    public String getSentRequest() {
        return this.sentRequest;
    }

    public void setSentRequest(String value) {
        this.sentRequest = value;
    }

    public String getReceivedResponse() {
        return this.receivedResponse;
    }

    public void setReceivedResponse(String value) {
        this.receivedResponse = value;
    }

    public String getDomProcessing() {
        return this.domProcessing;
    }

    public void setDomProcessing(String value) {
        this.domProcessing = value;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.applicationinsights.contracts.PageViewData, com.microsoft.applicationinsights.contracts.EventData, com.microsoft.applicationinsights.contracts.Domain
    public String serializeContent(Writer writer) throws IOException {
        String prefix = super.serializeContent(writer);
        if (this.perfTotal != null) {
            writer.write(prefix + "\"perfTotal\":");
            writer.write(JsonHelper.convert(this.perfTotal));
            prefix = ",";
        }
        if (this.networkConnect != null) {
            writer.write(prefix + "\"networkConnect\":");
            writer.write(JsonHelper.convert(this.networkConnect));
            prefix = ",";
        }
        if (this.sentRequest != null) {
            writer.write(prefix + "\"sentRequest\":");
            writer.write(JsonHelper.convert(this.sentRequest));
            prefix = ",";
        }
        if (this.receivedResponse != null) {
            writer.write(prefix + "\"receivedResponse\":");
            writer.write(JsonHelper.convert(this.receivedResponse));
            prefix = ",";
        }
        if (this.domProcessing != null) {
            writer.write(prefix + "\"domProcessing\":");
            writer.write(JsonHelper.convert(this.domProcessing));
            return ",";
        }
        return prefix;
    }

    @Override // com.microsoft.applicationinsights.contracts.PageViewData, com.microsoft.applicationinsights.contracts.EventData, com.microsoft.applicationinsights.contracts.Domain
    protected void InitializeFields() {
    }
}

package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.ITelemetry;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class CrashData extends Domain implements ITelemetry {
    private List<CrashDataBinary> binaries;
    private CrashDataHeaders headers;
    private List<CrashDataThread> threads;
    private int ver = 2;

    public CrashData() {
        InitializeFields();
    }

    @Override // com.microsoft.applicationinsights.contracts.Domain, com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public String getEnvelopeName() {
        return "Microsoft.ApplicationInsights.Crash";
    }

    @Override // com.microsoft.applicationinsights.contracts.Domain, com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public String getBaseType() {
        return "Microsoft.ApplicationInsights.CrashData";
    }

    public int getVer() {
        return this.ver;
    }

    @Override // com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public void setVer(int value) {
        this.ver = value;
    }

    public CrashDataHeaders getHeaders() {
        return this.headers;
    }

    public void setHeaders(CrashDataHeaders value) {
        this.headers = value;
    }

    public List<CrashDataThread> getThreads() {
        if (this.threads == null) {
            this.threads = new ArrayList();
        }
        return this.threads;
    }

    public void setThreads(List<CrashDataThread> value) {
        this.threads = value;
    }

    public List<CrashDataBinary> getBinaries() {
        if (this.binaries == null) {
            this.binaries = new ArrayList();
        }
        return this.binaries;
    }

    public void setBinaries(List<CrashDataBinary> value) {
        this.binaries = value;
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
        writer.write(",\"headers\":");
        JsonHelper.writeJsonSerializable(writer, this.headers);
        String prefix2 = ",";
        if (this.threads != null) {
            writer.write(",\"threads\":");
            JsonHelper.writeList(writer, this.threads);
            prefix2 = ",";
        }
        if (this.binaries != null) {
            writer.write(prefix2 + "\"binaries\":");
            JsonHelper.writeList(writer, this.binaries);
            return ",";
        }
        return prefix2;
    }

    @Override // com.microsoft.applicationinsights.contracts.Domain
    protected void InitializeFields() {
    }
}

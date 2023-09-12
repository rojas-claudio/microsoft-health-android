package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.IJsonSerializable;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
/* loaded from: classes.dex */
public class Internal implements IJsonSerializable {
    private String agentVersion;
    private String sdkVersion;

    public Internal() {
        InitializeFields();
    }

    public String getSdkVersion() {
        return this.sdkVersion;
    }

    public void setSdkVersion(String value) {
        this.sdkVersion = value;
    }

    public String getAgentVersion() {
        return this.agentVersion;
    }

    public void setAgentVersion(String value) {
        this.agentVersion = value;
    }

    public void addToHashMap(Map<String, String> map) {
        if (this.sdkVersion != null) {
            map.put("ai.internal.sdkVersion", this.sdkVersion);
        }
        if (this.agentVersion != null) {
            map.put("ai.internal.agentVersion", this.agentVersion);
        }
    }

    @Override // com.microsoft.applicationinsights.contracts.shared.IJsonSerializable
    public void serialize(Writer writer) throws IOException {
        if (writer == null) {
            throw new IllegalArgumentException("writer");
        }
        writer.write(123);
        serializeContent(writer);
        writer.write(125);
    }

    protected String serializeContent(Writer writer) throws IOException {
        String prefix = "";
        if (this.sdkVersion != null) {
            writer.write("\"ai.internal.sdkVersion\":");
            writer.write(JsonHelper.convert(this.sdkVersion));
            prefix = ",";
        }
        if (this.agentVersion != null) {
            writer.write(prefix + "\"ai.internal.agentVersion\":");
            writer.write(JsonHelper.convert(this.agentVersion));
            return ",";
        }
        return prefix;
    }

    protected void InitializeFields() {
    }
}

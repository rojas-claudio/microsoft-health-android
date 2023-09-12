package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.IJsonSerializable;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
/* loaded from: classes.dex */
public class Location implements IJsonSerializable {
    private String ip;

    public Location() {
        InitializeFields();
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String value) {
        this.ip = value;
    }

    public void addToHashMap(Map<String, String> map) {
        if (this.ip != null) {
            map.put("ai.location.ip", this.ip);
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
        if (this.ip != null) {
            writer.write("\"ai.location.ip\":");
            writer.write(JsonHelper.convert(this.ip));
            return ",";
        }
        return "";
    }

    protected void InitializeFields() {
    }
}

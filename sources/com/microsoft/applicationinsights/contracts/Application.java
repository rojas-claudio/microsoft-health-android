package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.IJsonSerializable;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
/* loaded from: classes.dex */
public class Application implements IJsonSerializable {
    private String build;
    private String ver;

    public Application() {
        InitializeFields();
    }

    public String getVer() {
        return this.ver;
    }

    public void setVer(String value) {
        this.ver = value;
    }

    public String getBuild() {
        return this.build;
    }

    public void setBuild(String value) {
        this.build = value;
    }

    public void addToHashMap(Map<String, String> map) {
        if (this.ver != null) {
            map.put("ai.application.ver", this.ver);
        }
        if (this.build != null) {
            map.put("ai.application.build", this.build);
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
        if (this.ver != null) {
            writer.write("\"ai.application.ver\":");
            writer.write(JsonHelper.convert(this.ver));
            prefix = ",";
        }
        if (this.build != null) {
            writer.write(prefix + "\"ai.application.build\":");
            writer.write(JsonHelper.convert(this.build));
            return ",";
        }
        return prefix;
    }

    protected void InitializeFields() {
    }
}

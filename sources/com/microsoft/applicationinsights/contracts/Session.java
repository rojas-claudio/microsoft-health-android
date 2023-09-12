package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.IJsonSerializable;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
/* loaded from: classes.dex */
public class Session implements IJsonSerializable {
    private String id;
    private String isFirst;
    private String isNew;

    public Session() {
        InitializeFields();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getIsFirst() {
        return this.isFirst;
    }

    public void setIsFirst(String value) {
        this.isFirst = value;
    }

    public String getIsNew() {
        return this.isNew;
    }

    public void setIsNew(String value) {
        this.isNew = value;
    }

    public void addToHashMap(Map<String, String> map) {
        if (this.id != null) {
            map.put("ai.session.id", this.id);
        }
        if (this.isFirst != null) {
            map.put("ai.session.isFirst", this.isFirst);
        }
        if (this.isNew != null) {
            map.put("ai.session.isNew", this.isNew);
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
        if (this.id != null) {
            writer.write("\"ai.session.id\":");
            writer.write(JsonHelper.convert(this.id));
            prefix = ",";
        }
        if (this.isFirst != null) {
            writer.write(prefix + "\"ai.session.isFirst\":");
            writer.write(JsonHelper.convert(this.isFirst));
            prefix = ",";
        }
        if (this.isNew != null) {
            writer.write(prefix + "\"ai.session.isNew\":");
            writer.write(JsonHelper.convert(this.isNew));
            return ",";
        }
        return prefix;
    }

    protected void InitializeFields() {
    }
}

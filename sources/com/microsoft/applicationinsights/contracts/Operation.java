package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.IJsonSerializable;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
/* loaded from: classes.dex */
public class Operation implements IJsonSerializable {
    private String id;
    private String name;
    private String parentId;
    private String rootId;
    private String syntheticSource;

    public Operation() {
        InitializeFields();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String value) {
        this.parentId = value;
    }

    public String getRootId() {
        return this.rootId;
    }

    public void setRootId(String value) {
        this.rootId = value;
    }

    public String getSyntheticSource() {
        return this.syntheticSource;
    }

    public void setSyntheticSource(String value) {
        this.syntheticSource = value;
    }

    public void addToHashMap(Map<String, String> map) {
        if (this.id != null) {
            map.put("ai.operation.id", this.id);
        }
        if (this.name != null) {
            map.put("ai.operation.name", this.name);
        }
        if (this.parentId != null) {
            map.put("ai.operation.parentId", this.parentId);
        }
        if (this.rootId != null) {
            map.put("ai.operation.rootId", this.rootId);
        }
        if (this.syntheticSource != null) {
            map.put("ai.operation.syntheticSource", this.syntheticSource);
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
            writer.write("\"ai.operation.id\":");
            writer.write(JsonHelper.convert(this.id));
            prefix = ",";
        }
        if (this.name != null) {
            writer.write(prefix + "\"ai.operation.name\":");
            writer.write(JsonHelper.convert(this.name));
            prefix = ",";
        }
        if (this.parentId != null) {
            writer.write(prefix + "\"ai.operation.parentId\":");
            writer.write(JsonHelper.convert(this.parentId));
            prefix = ",";
        }
        if (this.rootId != null) {
            writer.write(prefix + "\"ai.operation.rootId\":");
            writer.write(JsonHelper.convert(this.rootId));
            prefix = ",";
        }
        if (this.syntheticSource != null) {
            writer.write(prefix + "\"ai.operation.syntheticSource\":");
            writer.write(JsonHelper.convert(this.syntheticSource));
            return ",";
        }
        return prefix;
    }

    protected void InitializeFields() {
    }
}

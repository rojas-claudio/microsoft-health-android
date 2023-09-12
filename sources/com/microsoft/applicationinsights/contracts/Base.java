package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.IJsonSerializable;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
/* loaded from: classes.dex */
public class Base implements IJsonSerializable {
    private String baseType;

    public Base() {
        InitializeFields();
    }

    public String getBaseType() {
        return this.baseType;
    }

    public void setBaseType(String value) {
        this.baseType = value;
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

    /* JADX INFO: Access modifiers changed from: protected */
    public String serializeContent(Writer writer) throws IOException {
        if (this.baseType != null) {
            writer.write("\"baseType\":");
            writer.write(JsonHelper.convert(this.baseType));
            return ",";
        }
        return "";
    }

    protected void InitializeFields() {
    }
}

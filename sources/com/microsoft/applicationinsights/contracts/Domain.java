package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.IJsonSerializable;
import java.io.IOException;
import java.io.Writer;
/* loaded from: classes.dex */
public class Domain implements IJsonSerializable {
    public Domain() {
        InitializeFields();
    }

    public String getEnvelopeName() {
        return "Microsoft.ApplicationInsights.Do";
    }

    public String getBaseType() {
        return "Microsoft.ApplicationInsights.Domain";
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
        return "";
    }

    protected void InitializeFields() {
    }
}

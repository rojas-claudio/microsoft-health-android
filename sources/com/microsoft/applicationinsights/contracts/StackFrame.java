package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.IJsonSerializable;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
/* loaded from: classes.dex */
public class StackFrame implements IJsonSerializable {
    private String assembly;
    private String fileName;
    private int level;
    private int line;
    private String method;

    public StackFrame() {
        InitializeFields();
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int value) {
        this.level = value;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String value) {
        this.method = value;
    }

    public String getAssembly() {
        return this.assembly;
    }

    public void setAssembly(String value) {
        this.assembly = value;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String value) {
        this.fileName = value;
    }

    public int getLine() {
        return this.line;
    }

    public void setLine(int value) {
        this.line = value;
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
        writer.write("\"level\":");
        writer.write(JsonHelper.convert(Integer.valueOf(this.level)));
        writer.write(",\"method\":");
        writer.write(JsonHelper.convert(this.method));
        String prefix = ",";
        if (this.assembly != null) {
            writer.write(",\"assembly\":");
            writer.write(JsonHelper.convert(this.assembly));
            prefix = ",";
        }
        if (this.fileName != null) {
            writer.write(prefix + "\"fileName\":");
            writer.write(JsonHelper.convert(this.fileName));
            prefix = ",";
        }
        if (this.line != 0) {
            writer.write(prefix + "\"line\":");
            writer.write(JsonHelper.convert(Integer.valueOf(this.line)));
            return ",";
        }
        return prefix;
    }

    protected void InitializeFields() {
    }
}

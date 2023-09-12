package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.IJsonSerializable;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class ExceptionDetails implements IJsonSerializable {
    private boolean hasFullStack = true;
    private int id;
    private String message;
    private int outerId;
    private List<StackFrame> parsedStack;
    private String stack;
    private String typeName;

    public ExceptionDetails() {
        InitializeFields();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int value) {
        this.id = value;
    }

    public int getOuterId() {
        return this.outerId;
    }

    public void setOuterId(int value) {
        this.outerId = value;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String value) {
        this.typeName = value;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String value) {
        this.message = value;
    }

    public boolean getHasFullStack() {
        return this.hasFullStack;
    }

    public void setHasFullStack(boolean value) {
        this.hasFullStack = value;
    }

    public String getStack() {
        return this.stack;
    }

    public void setStack(String value) {
        this.stack = value;
    }

    public List<StackFrame> getParsedStack() {
        if (this.parsedStack == null) {
            this.parsedStack = new ArrayList();
        }
        return this.parsedStack;
    }

    public void setParsedStack(List<StackFrame> value) {
        this.parsedStack = value;
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
        if (this.id != 0) {
            writer.write("\"id\":");
            writer.write(JsonHelper.convert(Integer.valueOf(this.id)));
            prefix = ",";
        }
        if (this.outerId != 0) {
            writer.write(prefix + "\"outerId\":");
            writer.write(JsonHelper.convert(Integer.valueOf(this.outerId)));
            prefix = ",";
        }
        writer.write(prefix + "\"typeName\":");
        writer.write(JsonHelper.convert(this.typeName));
        writer.write(",\"message\":");
        writer.write(JsonHelper.convert(this.message));
        String prefix2 = ",";
        if (this.hasFullStack) {
            writer.write(",\"hasFullStack\":");
            writer.write(JsonHelper.convert(this.hasFullStack));
            prefix2 = ",";
        }
        if (this.stack != null) {
            writer.write(prefix2 + "\"stack\":");
            writer.write(JsonHelper.convert(this.stack));
            prefix2 = ",";
        }
        if (this.parsedStack != null) {
            writer.write(prefix2 + "\"parsedStack\":");
            JsonHelper.writeList(writer, this.parsedStack);
            return ",";
        }
        return prefix2;
    }

    protected void InitializeFields() {
    }
}

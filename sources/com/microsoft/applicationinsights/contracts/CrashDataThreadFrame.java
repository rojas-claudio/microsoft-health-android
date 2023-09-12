package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.IJsonSerializable;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class CrashDataThreadFrame implements IJsonSerializable {
    private String address;
    private Map<String, String> registers;
    private String symbol;

    public CrashDataThreadFrame() {
        InitializeFields();
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String value) {
        this.address = value;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String value) {
        this.symbol = value;
    }

    public Map<String, String> getRegisters() {
        if (this.registers == null) {
            this.registers = new LinkedHashMap();
        }
        return this.registers;
    }

    public void setRegisters(Map<String, String> value) {
        this.registers = value;
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
        writer.write("\"address\":");
        writer.write(JsonHelper.convert(this.address));
        String prefix = ",";
        if (this.symbol != null) {
            writer.write(",\"symbol\":");
            writer.write(JsonHelper.convert(this.symbol));
            prefix = ",";
        }
        if (this.registers != null) {
            writer.write(prefix + "\"registers\":");
            JsonHelper.writeDictionary(writer, this.registers);
            return ",";
        }
        return prefix;
    }

    protected void InitializeFields() {
    }
}

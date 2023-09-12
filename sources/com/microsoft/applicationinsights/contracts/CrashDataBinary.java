package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.IJsonSerializable;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
/* loaded from: classes.dex */
public class CrashDataBinary implements IJsonSerializable {
    private long cpuSubType;
    private long cpuType;
    private String endAddress;
    private String name;
    private String path;
    private String startAddress;
    private String uuid;

    public CrashDataBinary() {
        InitializeFields();
    }

    public String getStartAddress() {
        return this.startAddress;
    }

    public void setStartAddress(String value) {
        this.startAddress = value;
    }

    public String getEndAddress() {
        return this.endAddress;
    }

    public void setEndAddress(String value) {
        this.endAddress = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public long getCpuType() {
        return this.cpuType;
    }

    public void setCpuType(long value) {
        this.cpuType = value;
    }

    public long getCpuSubType() {
        return this.cpuSubType;
    }

    public void setCpuSubType(long value) {
        this.cpuSubType = value;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String value) {
        this.uuid = value;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String value) {
        this.path = value;
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
        if (this.startAddress != null) {
            writer.write("\"startAddress\":");
            writer.write(JsonHelper.convert(this.startAddress));
            prefix = ",";
        }
        if (this.endAddress != null) {
            writer.write(prefix + "\"endAddress\":");
            writer.write(JsonHelper.convert(this.endAddress));
            prefix = ",";
        }
        if (this.name != null) {
            writer.write(prefix + "\"name\":");
            writer.write(JsonHelper.convert(this.name));
            prefix = ",";
        }
        if (this.cpuType != 0) {
            writer.write(prefix + "\"cpuType\":");
            writer.write(JsonHelper.convert(Long.valueOf(this.cpuType)));
            prefix = ",";
        }
        if (this.cpuSubType != 0) {
            writer.write(prefix + "\"cpuSubType\":");
            writer.write(JsonHelper.convert(Long.valueOf(this.cpuSubType)));
            prefix = ",";
        }
        if (this.uuid != null) {
            writer.write(prefix + "\"uuid\":");
            writer.write(JsonHelper.convert(this.uuid));
            prefix = ",";
        }
        if (this.path != null) {
            writer.write(prefix + "\"path\":");
            writer.write(JsonHelper.convert(this.path));
            return ",";
        }
        return prefix;
    }

    protected void InitializeFields() {
    }
}

package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.IJsonSerializable;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
/* loaded from: classes.dex */
public class DataPoint implements IJsonSerializable {
    private Integer count;
    private int kind = 0;
    private Double max;
    private Double min;
    private String name;
    private Double stdDev;
    private double value;

    public DataPoint() {
        InitializeFields();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public int getKind() {
        return this.kind;
    }

    public void setKind(int value) {
        this.kind = value;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer value) {
        this.count = value;
    }

    public Double getMin() {
        return this.min;
    }

    public void setMin(Double value) {
        this.min = value;
    }

    public Double getMax() {
        return this.max;
    }

    public void setMax(Double value) {
        this.max = value;
    }

    public Double getStdDev() {
        return this.stdDev;
    }

    public void setStdDev(Double value) {
        this.stdDev = value;
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
        writer.write("\"name\":");
        writer.write(JsonHelper.convert(this.name));
        String prefix = ",";
        if (this.kind != 0) {
            writer.write(",\"kind\":");
            writer.write(JsonHelper.convert(Integer.valueOf(this.kind)));
            prefix = ",";
        }
        writer.write(prefix + "\"value\":");
        writer.write(JsonHelper.convert(Double.valueOf(this.value)));
        String prefix2 = ",";
        if (this.count != null) {
            writer.write(",\"count\":");
            writer.write(JsonHelper.convert(this.count));
            prefix2 = ",";
        }
        if (this.min != null) {
            writer.write(prefix2 + "\"min\":");
            writer.write(JsonHelper.convert(this.min));
            prefix2 = ",";
        }
        if (this.max != null) {
            writer.write(prefix2 + "\"max\":");
            writer.write(JsonHelper.convert(this.max));
            prefix2 = ",";
        }
        if (this.stdDev != null) {
            writer.write(prefix2 + "\"stdDev\":");
            writer.write(JsonHelper.convert(this.stdDev));
            return ",";
        }
        return prefix2;
    }

    protected void InitializeFields() {
    }
}

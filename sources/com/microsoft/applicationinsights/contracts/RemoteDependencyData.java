package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.ITelemetry;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class RemoteDependencyData extends Domain implements ITelemetry {
    private Boolean async;
    private String commandName;
    private Integer count;
    private String dependencyTypeName;
    private Double max;
    private Double min;
    private String name;
    private Map<String, String> properties;
    private Double stdDev;
    private double value;
    private int ver = 2;
    private int kind = 0;
    private int dependencyKind = 2;
    private Boolean success = true;
    private int dependencySource = 0;

    public RemoteDependencyData() {
        InitializeFields();
    }

    @Override // com.microsoft.applicationinsights.contracts.Domain, com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public String getEnvelopeName() {
        return "Microsoft.ApplicationInsights.RemoteDependency";
    }

    @Override // com.microsoft.applicationinsights.contracts.Domain, com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public String getBaseType() {
        return "Microsoft.ApplicationInsights.RemoteDependencyData";
    }

    public int getVer() {
        return this.ver;
    }

    @Override // com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public void setVer(int value) {
        this.ver = value;
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

    public int getDependencyKind() {
        return this.dependencyKind;
    }

    public void setDependencyKind(int value) {
        this.dependencyKind = value;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(Boolean value) {
        this.success = value;
    }

    public Boolean getAsync() {
        return this.async;
    }

    public void setAsync(Boolean value) {
        this.async = value;
    }

    public int getDependencySource() {
        return this.dependencySource;
    }

    public void setDependencySource(int value) {
        this.dependencySource = value;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public void setCommandName(String value) {
        this.commandName = value;
    }

    public String getDependencyTypeName() {
        return this.dependencyTypeName;
    }

    public void setDependencyTypeName(String value) {
        this.dependencyTypeName = value;
    }

    @Override // com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public Map<String, String> getProperties() {
        if (this.properties == null) {
            this.properties = new LinkedHashMap();
        }
        return this.properties;
    }

    @Override // com.microsoft.applicationinsights.contracts.shared.ITelemetry
    public void setProperties(Map<String, String> value) {
        this.properties = value;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.applicationinsights.contracts.Domain
    public String serializeContent(Writer writer) throws IOException {
        String prefix = super.serializeContent(writer);
        writer.write(prefix + "\"ver\":");
        writer.write(JsonHelper.convert(Integer.valueOf(this.ver)));
        writer.write(",\"name\":");
        writer.write(JsonHelper.convert(this.name));
        String prefix2 = ",";
        if (this.kind != 0) {
            writer.write(",\"kind\":");
            writer.write(JsonHelper.convert(Integer.valueOf(this.kind)));
            prefix2 = ",";
        }
        writer.write(prefix2 + "\"value\":");
        writer.write(JsonHelper.convert(Double.valueOf(this.value)));
        String prefix3 = ",";
        if (this.count != null) {
            writer.write(",\"count\":");
            writer.write(JsonHelper.convert(this.count));
            prefix3 = ",";
        }
        if (this.min != null) {
            writer.write(prefix3 + "\"min\":");
            writer.write(JsonHelper.convert(this.min));
            prefix3 = ",";
        }
        if (this.max != null) {
            writer.write(prefix3 + "\"max\":");
            writer.write(JsonHelper.convert(this.max));
            prefix3 = ",";
        }
        if (this.stdDev != null) {
            writer.write(prefix3 + "\"stdDev\":");
            writer.write(JsonHelper.convert(this.stdDev));
            prefix3 = ",";
        }
        writer.write(prefix3 + "\"dependencyKind\":");
        writer.write(JsonHelper.convert(Integer.valueOf(this.dependencyKind)));
        String prefix4 = ",";
        if (this.success != null) {
            writer.write(",\"success\":");
            writer.write(JsonHelper.convert(this.success.booleanValue()));
            prefix4 = ",";
        }
        if (this.async != null) {
            writer.write(prefix4 + "\"async\":");
            writer.write(JsonHelper.convert(this.async.booleanValue()));
            prefix4 = ",";
        }
        if (this.dependencySource != 0) {
            writer.write(prefix4 + "\"dependencySource\":");
            writer.write(JsonHelper.convert(Integer.valueOf(this.dependencySource)));
            prefix4 = ",";
        }
        if (this.commandName != null) {
            writer.write(prefix4 + "\"commandName\":");
            writer.write(JsonHelper.convert(this.commandName));
            prefix4 = ",";
        }
        if (this.dependencyTypeName != null) {
            writer.write(prefix4 + "\"dependencyTypeName\":");
            writer.write(JsonHelper.convert(this.dependencyTypeName));
            prefix4 = ",";
        }
        if (this.properties != null) {
            writer.write(prefix4 + "\"properties\":");
            JsonHelper.writeDictionary(writer, this.properties);
            return ",";
        }
        return prefix4;
    }

    @Override // com.microsoft.applicationinsights.contracts.Domain
    protected void InitializeFields() {
    }
}

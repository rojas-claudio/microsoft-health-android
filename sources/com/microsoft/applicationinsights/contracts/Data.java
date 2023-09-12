package com.microsoft.applicationinsights.contracts;

import com.microsoft.applicationinsights.contracts.shared.ITelemetryData;
import com.microsoft.applicationinsights.contracts.shared.JsonHelper;
import java.io.IOException;
import java.io.Writer;
/* loaded from: classes.dex */
public class Data<TDomain extends ITelemetryData> extends Base implements ITelemetryData {
    private TDomain baseData;

    public Data() {
        InitializeFields();
    }

    public TDomain getBaseData() {
        return this.baseData;
    }

    public void setBaseData(TDomain value) {
        this.baseData = value;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.applicationinsights.contracts.Base
    public String serializeContent(Writer writer) throws IOException {
        String prefix = super.serializeContent(writer);
        writer.write(prefix + "\"baseData\":");
        JsonHelper.writeJsonSerializable(writer, this.baseData);
        return ",";
    }

    @Override // com.microsoft.applicationinsights.contracts.Base
    protected void InitializeFields() {
    }
}

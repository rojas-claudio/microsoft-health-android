package com.microsoft.applicationinsights.contracts.shared;

import java.util.Map;
/* loaded from: classes.dex */
public interface ITelemetry extends ITelemetryData {
    String getBaseType();

    String getEnvelopeName();

    Map<String, String> getProperties();

    void setProperties(Map<String, String> map);

    void setVer(int i);
}

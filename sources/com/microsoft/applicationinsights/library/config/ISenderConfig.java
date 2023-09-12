package com.microsoft.applicationinsights.library.config;
/* loaded from: classes.dex */
public interface ISenderConfig {
    String getEndpointUrl();

    int getSenderConnectTimeout();

    int getSenderReadTimeout();

    void setEndpointUrl(String str);
}

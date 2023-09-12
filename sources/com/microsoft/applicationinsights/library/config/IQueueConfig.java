package com.microsoft.applicationinsights.library.config;
/* loaded from: classes.dex */
public interface IQueueConfig {
    int getMaxBatchCount();

    int getMaxBatchIntervalMs();

    void setMaxBatchCount(int i);

    void setMaxBatchIntervalMs(int i);
}

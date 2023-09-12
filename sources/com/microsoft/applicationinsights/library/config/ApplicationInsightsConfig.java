package com.microsoft.applicationinsights.library.config;

import com.microsoft.applicationinsights.library.ApplicationInsights;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public class ApplicationInsightsConfig implements ISenderConfig, ISessionConfig, IQueueConfig {
    private static final int DEBUG_MAX_BATCH_COUNT = 5;
    private static final int DEBUG_MAX_BATCH_INTERVAL_MS = 3000;
    private static final int DEFAULTSENDER_CONNECT_TIMEOUT = 15000;
    private static final String DEFAULT_ENDPOINT_URL = "https://dc.services.visualstudio.com/v2/track";
    private static final int DEFAULT_MAX_BATCH_COUNT = 100;
    private static final int DEFAULT_MAX_BATCH_INTERVAL_MS = 15000;
    private static final int DEFAULT_SENDER_READ_TIMEOUT = 10000;
    protected static final int DEFAULT_SESSION_INTERVAL = 20000;
    private String endpointUrl;
    protected final Object lock = new Object();
    private int maxBatchCount;
    private int maxBatchIntervalMs;
    private int senderConnectTimeoutMs;
    private int senderReadTimeoutMs;
    private long sessionIntervalMs;

    public ApplicationInsightsConfig() {
        this.maxBatchCount = ApplicationInsights.isDeveloperMode() ? 5 : 100;
        this.maxBatchIntervalMs = ApplicationInsights.isDeveloperMode() ? 3000 : 15000;
        this.endpointUrl = DEFAULT_ENDPOINT_URL;
        this.senderReadTimeoutMs = DEFAULT_SENDER_READ_TIMEOUT;
        this.senderConnectTimeoutMs = Constants.SYNC_HOMEDATA_TIMEOUT;
        this.sessionIntervalMs = 20000L;
    }

    @Override // com.microsoft.applicationinsights.library.config.IQueueConfig
    public int getMaxBatchCount() {
        return this.maxBatchCount;
    }

    @Override // com.microsoft.applicationinsights.library.config.IQueueConfig
    public void setMaxBatchCount(int maxBatchCount) {
        synchronized (this.lock) {
            this.maxBatchCount = maxBatchCount;
        }
    }

    @Override // com.microsoft.applicationinsights.library.config.IQueueConfig
    public int getMaxBatchIntervalMs() {
        return this.maxBatchIntervalMs;
    }

    @Override // com.microsoft.applicationinsights.library.config.IQueueConfig
    public void setMaxBatchIntervalMs(int maxBatchIntervalMs) {
        synchronized (this.lock) {
            this.maxBatchIntervalMs = maxBatchIntervalMs;
        }
    }

    @Override // com.microsoft.applicationinsights.library.config.ISenderConfig
    public String getEndpointUrl() {
        return this.endpointUrl;
    }

    @Override // com.microsoft.applicationinsights.library.config.ISenderConfig
    public void setEndpointUrl(String endpointUrl) {
        synchronized (this.lock) {
            this.endpointUrl = endpointUrl;
        }
    }

    @Override // com.microsoft.applicationinsights.library.config.ISenderConfig
    public int getSenderReadTimeout() {
        return this.senderReadTimeoutMs;
    }

    @Override // com.microsoft.applicationinsights.library.config.ISenderConfig
    public int getSenderConnectTimeout() {
        return this.senderConnectTimeoutMs;
    }

    @Override // com.microsoft.applicationinsights.library.config.ISessionConfig
    public long getSessionIntervalMs() {
        return this.sessionIntervalMs;
    }

    @Override // com.microsoft.applicationinsights.library.config.ISessionConfig
    public void setSessionIntervalMs(long sessionIntervalMs) {
        this.sessionIntervalMs = sessionIntervalMs;
    }
}

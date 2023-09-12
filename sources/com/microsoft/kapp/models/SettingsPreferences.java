package com.microsoft.kapp.models;

import java.io.Serializable;
/* loaded from: classes.dex */
public class SettingsPreferences implements Serializable {
    private static final long serialVersionUID = 7874096721809923029L;
    private String mBandRegion;
    private boolean mIsDistanceHeightMetric;
    private boolean mIsTemperatureMetric;
    private boolean mIsWeightMetric;
    private boolean mShouldSendDataOverWiFiOnly;

    public boolean isWeightMetric() {
        return this.mIsWeightMetric;
    }

    public void setIsWeightMetric(boolean isWeightMetric) {
        this.mIsWeightMetric = isWeightMetric;
    }

    public boolean isTemperatureMetric() {
        return this.mIsTemperatureMetric;
    }

    public void setIsTemperatureMetric(boolean isTemperatureMetric) {
        this.mIsTemperatureMetric = isTemperatureMetric;
    }

    public boolean isDistanceHeightMetric() {
        return this.mIsDistanceHeightMetric;
    }

    public void setIsDistanceHeightMetric(boolean isDistanceHeightMetric) {
        this.mIsDistanceHeightMetric = isDistanceHeightMetric;
    }

    public String getUserSelectedBandRegion() {
        return this.mBandRegion;
    }

    public void setUserSelectedBandRegion(String bandRegion) {
        this.mBandRegion = bandRegion;
    }

    public boolean shouldSendDataOverWiFiOnly() {
        return this.mShouldSendDataOverWiFiOnly;
    }

    public void setShouldSendDataOverWiFiOnly(boolean shouldSendDataOverWiFiOnly) {
        this.mShouldSendDataOverWiFiOnly = shouldSendDataOverWiFiOnly;
    }
}

package com.microsoft.kapp.version;

import org.apache.commons.lang3.StringUtils;
/* loaded from: classes.dex */
public class VersionManagerDebugOverride {
    private long mApplicationUpdateCheckIntervalInSeconds;
    private long mDeviceFirmwareUpdateCheckIntervalInSeconds;
    private String mLatestAppVersion;
    private String mLatestFirmwareVersion;
    private String mMinimumRequiredFirmwareVersion;

    public VersionManagerDebugOverride(String latestAppVersion, String minimumRequiredFirmwareVersion, String latestFirmwareVersion, String applicationUpdateCheckIntervalInSeconds, String deviceFirmwareUpdateCheckIntervalInSeconds) {
        this.mLatestAppVersion = normalizeString(latestAppVersion);
        this.mMinimumRequiredFirmwareVersion = normalizeString(minimumRequiredFirmwareVersion);
        this.mLatestFirmwareVersion = normalizeString(latestFirmwareVersion);
        this.mApplicationUpdateCheckIntervalInSeconds = normalizeAndConvertLong(applicationUpdateCheckIntervalInSeconds);
        this.mDeviceFirmwareUpdateCheckIntervalInSeconds = normalizeAndConvertLong(deviceFirmwareUpdateCheckIntervalInSeconds);
    }

    public String getLatestAppVersion() {
        return this.mLatestAppVersion;
    }

    public String getMinimumRequiredFirmwareVersion() {
        return this.mMinimumRequiredFirmwareVersion;
    }

    public String getLatestFirmwareVersion() {
        return this.mLatestFirmwareVersion;
    }

    public long getApplicationUpdateCheckIntervalInSeconds() {
        return this.mApplicationUpdateCheckIntervalInSeconds;
    }

    public long getDeviceFirmwareUpdateCheckIntervalInSeconds() {
        return this.mDeviceFirmwareUpdateCheckIntervalInSeconds;
    }

    private String normalizeString(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value;
    }

    private long normalizeAndConvertLong(String value) {
        if (StringUtils.isBlank(value)) {
            return 0L;
        }
        return Long.parseLong(value);
    }
}

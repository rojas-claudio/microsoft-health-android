package com.microsoft.kapp.version;

import com.google.gson.annotations.SerializedName;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class CheckedFirmwareUpdateInfo {
    @SerializedName("FirmwareVersion")
    private String mFirmwareVersion;
    @SerializedName("OptionalUpdate")
    private boolean mIsUpdateOptional;
    @SerializedName("LastSkippedOn")
    private DateTime mLastSkipped;
    @SerializedName("UpdateNeedsd")
    private boolean mUpdateNeeded;

    public CheckedFirmwareUpdateInfo(boolean needsUpdate, boolean isUpdateOptional, String firmwareVersion) {
        this(needsUpdate, isUpdateOptional, firmwareVersion, null);
    }

    public CheckedFirmwareUpdateInfo(boolean needsUpdate, boolean isUpdateOptional, String firmwareVersion, DateTime lastSkipped) {
        this.mUpdateNeeded = needsUpdate;
        this.mFirmwareVersion = firmwareVersion;
        this.mLastSkipped = lastSkipped;
        this.mIsUpdateOptional = isUpdateOptional;
    }

    public String getFirmwareVersion() {
        return this.mFirmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.mFirmwareVersion = firmwareVersion;
    }

    public DateTime getLastSkipped() {
        return this.mLastSkipped;
    }

    public void setLastSkipped(DateTime lastSkipped) {
        this.mLastSkipped = lastSkipped;
    }

    public boolean isUpdateNeeded() {
        return this.mUpdateNeeded;
    }

    public void setUpdateNeeded(boolean needsUpdate) {
        this.mUpdateNeeded = needsUpdate;
    }

    public boolean isIsUpdateOptional() {
        return this.mIsUpdateOptional;
    }

    public void setIsUpdateOptional(boolean mIsUpdateOptional) {
        this.mIsUpdateOptional = mIsUpdateOptional;
    }
}

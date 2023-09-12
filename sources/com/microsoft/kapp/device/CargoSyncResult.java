package com.microsoft.kapp.device;

import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.multidevice.KSyncResult;
import org.joda.time.DateTime;
import org.joda.time.Interval;
/* loaded from: classes.dex */
public class CargoSyncResult {
    private Interval mCloudProcessingWaitTime;
    private boolean mIsForgroundSync;
    private boolean mIsPopUpErrorAllowed;
    private KSyncResult mSyncResult;
    private DateTime mSyncTime;

    public CargoSyncResult(boolean isForgroundSync, boolean isPopUpErrorAllowed, KSyncResult syncResult) {
        this.mSyncResult = syncResult;
        this.mIsForgroundSync = isForgroundSync;
        this.mIsPopUpErrorAllowed = isPopUpErrorAllowed;
    }

    public KSyncResult getSyncResult() {
        return this.mSyncResult;
    }

    public void setSyncResult(KSyncResult syncResult) {
        this.mSyncResult = syncResult;
    }

    public Interval getCloudProcessingWaitTime() {
        return this.mCloudProcessingWaitTime;
    }

    public void setCloudProcessingWaitTime(Interval cloudProcessingWaitTime) {
        this.mCloudProcessingWaitTime = cloudProcessingWaitTime;
    }

    public boolean isSyncSuccess() {
        return this.mSyncResult != null && this.mSyncResult.getStatus() == KSyncResult.SyncResultCode.SUCCESS;
    }

    public boolean getIsForgroundSync() {
        return this.mIsForgroundSync;
    }

    public boolean getIsPopUpErrorAllowed() {
        return this.mIsPopUpErrorAllowed;
    }

    public DateTime getSyncTime() {
        return this.mSyncTime;
    }

    public void setSyncTime(DateTime syncTime) {
        Validate.notNull(syncTime, "syncTime");
        this.mSyncTime = syncTime;
    }

    public String toString() {
        String success = String.format("Sync Successful: %s", Boolean.valueOf(isSyncSuccess()));
        String result = "";
        if (this.mSyncResult != null && getCloudProcessingWaitTime() != null) {
            result = String.format("%sCloud Wait Time (ms): %d\n", this.mSyncResult.toString(), Long.valueOf(getCloudProcessingWaitTime().toDuration().getMillis()));
        }
        return result + success;
    }
}

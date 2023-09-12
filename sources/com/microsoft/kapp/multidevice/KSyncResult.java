package com.microsoft.kapp.multidevice;

import com.microsoft.kapp.DeviceErrorState;
/* loaded from: classes.dex */
public class KSyncResult {
    private CloudErrorState mCloudErrorState;
    private DeviceErrorCode mDeviceErrorCode;
    private DeviceErrorState mSDEError;
    private SyncResultCode mStatus;

    /* loaded from: classes.dex */
    public enum CloudErrorState {
        SUCCESS,
        UNKNOWN,
        CLOUD_PROCESSING_FAILURE
    }

    /* loaded from: classes.dex */
    public enum DeviceErrorCode {
        BLUETOOTH_ERROR,
        DEVICE_NOT_CONNECTED,
        DEVICE_NOT_BONDED,
        DEVICE_INCOMPATIBLE_FIRMWARE_VERSION,
        UNKNOWN
    }

    /* loaded from: classes.dex */
    public enum SyncResultCode {
        NOT_ENABLED,
        SUCCESS,
        DEVICE_ERROR,
        CLOUD_ERROR,
        SDE_ERROR,
        UNKNOWN_ERROR
    }

    public KSyncResult() {
    }

    public KSyncResult(SyncResultCode status) {
        this.mStatus = status;
    }

    public SyncResultCode getStatus() {
        return this.mStatus;
    }

    public void setStatus(SyncResultCode status) {
        this.mStatus = status;
    }

    public boolean isSuccess() {
        return getStatus() == SyncResultCode.SUCCESS;
    }

    public void setSDEError(DeviceErrorState state) {
        this.mSDEError = state;
    }

    public DeviceErrorState getSDEError() {
        return this.mSDEError;
    }

    public CloudErrorState getCloudError() {
        return this.mCloudErrorState;
    }

    public void setCloudError(CloudErrorState state) {
        this.mCloudErrorState = state;
    }

    public DeviceErrorCode getDeviceErrorCode() {
        return this.mDeviceErrorCode;
    }

    public void setDeviceErrorCode(DeviceErrorCode deviceError) {
        this.mDeviceErrorCode = deviceError;
    }

    public boolean isFirmwareUpdateError() {
        return this.mStatus != null && this.mStatus == SyncResultCode.DEVICE_ERROR && this.mDeviceErrorCode != null && this.mDeviceErrorCode == DeviceErrorCode.DEVICE_INCOMPATIBLE_FIRMWARE_VERSION;
    }

    public boolean isMultipleBandsPairedError() {
        return this.mStatus != null && this.mStatus == SyncResultCode.SDE_ERROR && this.mSDEError != null && this.mSDEError == DeviceErrorState.MULTIPLE_DEVICES_BONDED;
    }
}

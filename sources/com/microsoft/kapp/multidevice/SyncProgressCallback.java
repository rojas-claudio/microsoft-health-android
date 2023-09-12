package com.microsoft.kapp.multidevice;

import com.microsoft.kapp.multidevice.MultiDeviceConstants;
/* loaded from: classes.dex */
public interface SyncProgressCallback {
    void onSyncComplete(MultiDeviceConstants.DeviceType deviceType, KSyncResult kSyncResult);

    void onSyncProgress(MultiDeviceConstants.DeviceType deviceType, int i);

    void registerForCallbacks(MultiDeviceConstants.DeviceType deviceType);
}

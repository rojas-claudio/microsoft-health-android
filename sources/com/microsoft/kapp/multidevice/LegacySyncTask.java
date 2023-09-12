package com.microsoft.kapp.multidevice;

import com.microsoft.kapp.multidevice.MultiDeviceConstants;
import com.microsoft.kapp.sensor.SensorDataProvider;
import com.microsoft.kapp.sensor.upload.SensorDataUploader;
/* loaded from: classes.dex */
public class LegacySyncTask implements Runnable, LegacySyncProgressCallback {
    private MultiDeviceConstants.DeviceType mDeviceType;
    private boolean mIsForeground;
    private SyncProgressCallback mProgressCallback;
    private SensorDataProvider mProvider;
    private SensorDataUploader mUploader;

    public LegacySyncTask(SyncProgressCallback progressCallback, MultiDeviceConstants.DeviceType deviceType, boolean isForeground, SensorDataProvider provider, SensorDataUploader uploader) {
        this.mProgressCallback = progressCallback;
        this.mIsForeground = isForeground;
        this.mDeviceType = deviceType;
        this.mProvider = provider;
        this.mUploader = uploader;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.mProgressCallback.registerForCallbacks(this.mDeviceType);
        KSyncResult result = this.mUploader.legacyUpload(this.mIsForeground, this);
        this.mProgressCallback.onSyncComplete(this.mDeviceType, result);
    }

    @Override // com.microsoft.kapp.multidevice.LegacySyncProgressCallback
    public void onSyncProgress(int progress) {
        this.mProgressCallback.onSyncProgress(this.mDeviceType, progress);
    }
}

package com.microsoft.band;

import com.microsoft.band.internal.device.DeviceInfo;
/* loaded from: classes.dex */
final class BandInfoImpl implements BandInfo {
    private DeviceInfo mDeviceInfoDelegate;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BandInfoImpl(DeviceInfo info) {
        this.mDeviceInfoDelegate = info;
    }

    @Override // com.microsoft.band.BandInfo
    public String getName() {
        return this.mDeviceInfoDelegate.getName();
    }

    @Override // com.microsoft.band.BandInfo
    public String getMacAddress() {
        return this.mDeviceInfoDelegate.getMacAddress();
    }

    DeviceInfo getDeviceInfo() {
        return this.mDeviceInfoDelegate;
    }
}

package com.microsoft.band.device.command;

import com.microsoft.band.device.DeviceProfileInfo;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
/* loaded from: classes.dex */
public class DeviceProfileSet extends CommandBase {
    private static final long serialVersionUID = 5154272107663264805L;
    private DeviceProfileInfo mDeviceProfileInfo;

    public DeviceProfileSet(DeviceProfileInfo deviceProfileInfo) {
        super(BandDeviceConstants.Command.CargoProfileSet, 0, 0);
        if (deviceProfileInfo == null) {
            throw new NullPointerException("deviceProfileInfo");
        }
        this.mDeviceProfileInfo = deviceProfileInfo;
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return this.mDeviceProfileInfo.toBytes();
    }
}

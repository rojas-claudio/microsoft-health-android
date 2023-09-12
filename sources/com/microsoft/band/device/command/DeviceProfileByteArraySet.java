package com.microsoft.band.device.command;

import com.microsoft.band.device.ProfileByteArray;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
/* loaded from: classes.dex */
public class DeviceProfileByteArraySet extends CommandBase {
    private static final long serialVersionUID = -1289072488156286706L;
    private ProfileByteArray mProfileByteArray;

    public DeviceProfileByteArraySet(ProfileByteArray profileByteArray) {
        super(BandDeviceConstants.Command.CargoProfileByteArraySet, 0, 0);
        if (profileByteArray == null) {
            throw new NullPointerException("profileByteArray");
        }
        this.mProfileByteArray = profileByteArray;
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return this.mProfileByteArray.toBytes();
    }
}

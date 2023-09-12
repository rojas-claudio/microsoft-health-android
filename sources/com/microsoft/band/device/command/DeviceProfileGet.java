package com.microsoft.band.device.command;

import com.microsoft.band.device.DeviceProfileInfo;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class DeviceProfileGet extends CommandBase {
    private static final int ARG_SIZE = 4;
    private static final long serialVersionUID = -7567873569531568462L;
    DeviceProfileInfo mDeviceProfileInfo;
    private int mHardwareVersion;

    public DeviceProfileGet(int hardwareVersion) {
        super(BandDeviceConstants.Command.CargoProfileGet, 4, 128);
        this.mHardwareVersion = 0;
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(4).putInt(BitHelper.longToUnsignedInt(128L));
        setCommandRelatedData(buffer.array());
        this.mHardwareVersion = hardwareVersion;
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return null;
    }

    @Override // com.microsoft.band.internal.CommandBase
    public void loadResult(ByteBuffer record) {
        this.mDeviceProfileInfo = new DPIOverride(record, this.mHardwareVersion);
    }

    public DeviceProfileInfo getDeviceProfileInfo() {
        return this.mDeviceProfileInfo;
    }

    /* loaded from: classes.dex */
    private class DPIOverride extends DeviceProfileInfo {
        private static final long serialVersionUID = -6691670526228344308L;

        protected DPIOverride(ByteBuffer buffer, int hardwareVersion) {
            super(buffer, hardwareVersion);
        }
    }
}

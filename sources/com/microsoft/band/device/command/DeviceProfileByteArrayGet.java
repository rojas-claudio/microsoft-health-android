package com.microsoft.band.device.command;

import com.microsoft.band.device.ProfileByteArray;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class DeviceProfileByteArrayGet extends CommandBase {
    private static final int ARG_SIZE = 4;
    private static final long serialVersionUID = -7762541622356311945L;
    ProfileByteArray mProfileByteArray;

    public DeviceProfileByteArrayGet() {
        super(BandDeviceConstants.Command.CargoProfileByteArrayGet, 4, ProfileByteArray.PROFILE_BYTE_ARRAY_STRUCTURE_LENGTH);
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(4).putInt(BitHelper.longToUnsignedInt(282L));
        setCommandRelatedData(buffer.array());
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return null;
    }

    @Override // com.microsoft.band.internal.CommandBase
    public void loadResult(ByteBuffer record) {
        this.mProfileByteArray = new ProfileByteArray(record);
    }

    public ProfileByteArray getProfileByteArray() {
        return this.mProfileByteArray;
    }
}

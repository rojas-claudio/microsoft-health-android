package com.microsoft.band.device.command;

import com.microsoft.band.device.FileTime;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class GetUTCTime extends CommandBase {
    private static final int ARG_SIZE = 0;
    private static final byte MESSAGE_SIZE = 8;
    private static final long serialVersionUID = 0;
    private FileTime mDeviceUTC;

    public GetUTCTime() {
        super(BandDeviceConstants.Command.CargoTimeGetUtcTime, 0, 8);
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return null;
    }

    @Override // com.microsoft.band.internal.CommandBase
    public void loadResult(ByteBuffer record) {
        this.mDeviceUTC = FileTime.valueOf(record);
    }

    public FileTime getDeviceUTC() {
        return this.mDeviceUTC;
    }
}

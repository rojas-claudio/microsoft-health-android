package com.microsoft.band.device.command;

import com.microsoft.band.device.FileTime;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
import com.microsoft.band.internal.util.BufferUtil;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class SetUTCTime extends CommandBase {
    private static final int ARG_SIZE = 0;
    private static final byte MESSAGE_SIZE = 8;
    private static final long serialVersionUID = 0;
    private FileTime mFileTime;

    public SetUTCTime() {
        super(BandDeviceConstants.Command.CargoTimeSetUtcTime, 0, 8);
        this.mFileTime = new FileTime(System.currentTimeMillis());
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(getMessageSize()).put(this.mFileTime.toBytes());
        return buffer.array();
    }
}

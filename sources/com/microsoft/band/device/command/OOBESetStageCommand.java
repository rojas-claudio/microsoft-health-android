package com.microsoft.band.device.command;

import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
import com.microsoft.band.internal.util.BufferUtil;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class OOBESetStageCommand extends CommandBase {
    private static final int ARG_SIZE = 0;
    private static final byte MESSAGE_SIZE = 2;
    private static final long serialVersionUID = 1;
    private short mIndex;

    public OOBESetStageCommand(short index) {
        super(BandDeviceConstants.Command.CargoOOBESetStage, 0, 2);
        this.mIndex = index;
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(2).putShort(this.mIndex);
        return buffer.array();
    }
}

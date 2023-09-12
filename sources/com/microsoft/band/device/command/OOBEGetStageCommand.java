package com.microsoft.band.device.command;

import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class OOBEGetStageCommand extends CommandBase {
    private static final int ARG_SIZE = 0;
    private static final byte MESSAGE_SIZE = 2;
    private static final long serialVersionUID = 1;
    private short mIndex;

    public OOBEGetStageCommand() {
        super(BandDeviceConstants.Command.CargoOOBEGetStage, 0, 2);
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return null;
    }

    @Override // com.microsoft.band.internal.CommandBase
    public void loadResult(ByteBuffer record) {
        this.mIndex = record.getShort();
    }

    public short getIndex() {
        return this.mIndex;
    }
}

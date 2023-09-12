package com.microsoft.band.device.command;

import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class CommandRead extends CommandBase {
    private static final long serialVersionUID = 1;
    private byte[] mResultByte;

    public CommandRead(BandDeviceConstants.Command commandType, byte[] argBuf, int resultSize) {
        super(commandType, argBuf == null ? 0 : argBuf.length, resultSize);
        if (argBuf != null) {
            setCommandRelatedData(argBuf);
        }
        this.mResultByte = new byte[resultSize];
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return null;
    }

    @Override // com.microsoft.band.internal.CommandBase
    public void loadResult(ByteBuffer record) {
        this.mResultByte = record.array();
    }

    public byte[] getResultByte() {
        return this.mResultByte;
    }
}

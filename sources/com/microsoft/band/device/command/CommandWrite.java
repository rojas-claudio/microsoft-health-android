package com.microsoft.band.device.command;

import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
/* loaded from: classes.dex */
public class CommandWrite extends CommandBase {
    private static final byte MESSAGE_SIZE = 0;
    private static final long serialVersionUID = 1;
    private byte[] mExtendedData;

    public CommandWrite(BandDeviceConstants.Command commandType, byte[] argBuf, byte[] buf) {
        super(commandType, argBuf == null ? 0 : argBuf.length, 0);
        if (argBuf != null) {
            setCommandRelatedData(argBuf);
        }
        this.mExtendedData = buf;
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return this.mExtendedData;
    }
}

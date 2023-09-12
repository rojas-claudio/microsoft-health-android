package com.microsoft.band.device.command;

import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
/* loaded from: classes.dex */
public class FlushLoggerCommand extends CommandBase {
    private static final long serialVersionUID = 0;

    public FlushLoggerCommand() {
        super(BandDeviceConstants.Command.LoggerFlush);
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return null;
    }
}

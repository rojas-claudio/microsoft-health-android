package com.microsoft.band.device.command;

import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
/* loaded from: classes.dex */
public class OOBEFinalizeCommand extends CommandBase {
    private static final long serialVersionUID = 1;

    public OOBEFinalizeCommand() {
        super(BandDeviceConstants.Command.CargoOOBEFinalize);
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return null;
    }
}

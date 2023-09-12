package com.microsoft.band.device.command;

import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
/* loaded from: classes.dex */
public class DisableSleepNotification extends CommandBase {
    private static final long serialVersionUID = 1;

    public DisableSleepNotification() {
        super(BandDeviceConstants.Command.CargoAppDataDisableSleepNotification);
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return null;
    }
}

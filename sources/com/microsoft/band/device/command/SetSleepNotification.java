package com.microsoft.band.device.command;

import com.microsoft.band.device.SleepNotification;
import com.microsoft.band.internal.BandDeviceConstants;
/* loaded from: classes.dex */
public class SetSleepNotification extends CommandWrite {
    private static final long serialVersionUID = 1;

    public SetSleepNotification(SleepNotification sNotification) {
        super(BandDeviceConstants.Command.CargoAppDataSetSleepNotification, null, sNotification.toBytes());
    }
}

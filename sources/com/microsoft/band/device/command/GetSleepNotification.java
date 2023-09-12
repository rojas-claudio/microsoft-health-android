package com.microsoft.band.device.command;

import com.microsoft.band.device.SleepNotification;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class GetSleepNotification extends CommandBase {
    private static final long serialVersionUID = 1;
    SleepNotification mSleepNotification;

    public GetSleepNotification() {
        super(BandDeviceConstants.Command.CargoAppDataGetSleepNotification, 0, SleepNotification.STRUCT_SIZE);
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return null;
    }

    @Override // com.microsoft.band.internal.CommandBase
    public void loadResult(ByteBuffer record) {
        this.mSleepNotification = new SleepNotification(record);
    }

    public SleepNotification getSleepNotification() {
        return this.mSleepNotification;
    }
}

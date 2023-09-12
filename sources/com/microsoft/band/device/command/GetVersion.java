package com.microsoft.band.device.command;

import com.microsoft.band.device.FWVersion;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class GetVersion extends CommandBase {
    private static final int ARG_SIZE = 0;
    private static final byte MESSAGE_SIZE = 57;
    private static final int VERSION_COUNT = 3;
    private static final int VERSION_SIZE = 19;
    private static final long serialVersionUID = 0;
    private FWVersion[] mVersion;

    public GetVersion() {
        super(BandDeviceConstants.Command.CargoCoreModuleGetVersion, 0, 57);
        this.mVersion = new FWVersion[3];
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return null;
    }

    @Override // com.microsoft.band.internal.CommandBase
    public void loadResult(ByteBuffer record) {
        for (int count = 0; count < 3; count++) {
            this.mVersion[count] = new FWVersion(record);
        }
    }

    public FWVersion[] getFWVersion() {
        return this.mVersion;
    }

    public FWVersion getFWVersion(int index) {
        return this.mVersion[index];
    }
}

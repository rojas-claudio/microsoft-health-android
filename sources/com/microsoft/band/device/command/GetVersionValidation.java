package com.microsoft.band.device.command;

import com.microsoft.band.device.FWVersion;
import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/* loaded from: classes.dex */
public class GetVersionValidation extends CommandBase {
    private static final int ARG_SIZE = 0;
    private static final byte MESSAGE_SIZE = 61;
    private static final long serialVersionUID = 0;
    private boolean mIsResourcesValid;
    private FWVersion[] mVersion;

    public GetVersionValidation() {
        super(BandDeviceConstants.Command.CargoSRAMFWUpdateValidateAssets, 0, 61);
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
        this.mIsResourcesValid = record.order(ByteOrder.LITTLE_ENDIAN).getInt() != 0;
    }

    public boolean isResourcesValid() {
        return this.mIsResourcesValid;
    }

    public FWVersion[] getFWVersion() {
        return this.mVersion;
    }
}

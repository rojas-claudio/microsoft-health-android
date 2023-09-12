package com.microsoft.band.device.command;

import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class GetEtchedSerialNumber extends CommandBase {
    private static final int ARG_SIZE = 0;
    private static final byte MESSAGE_SIZE = 32;
    private static String mSerialNumber;
    private static final long serialVersionUID = 0;

    public GetEtchedSerialNumber() {
        super(BandDeviceConstants.Command.CargoConfigurationGetPermanentConfig, 0, 32);
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return null;
    }

    @Override // com.microsoft.band.internal.CommandBase
    public void loadResult(ByteBuffer record) {
        StringBuilder sb = new StringBuilder();
        for (int c = 2; c < 18; c++) {
            sb.append((char) record.get(c));
        }
        mSerialNumber = sb.toString();
    }

    public String getSerialNumber() {
        return mSerialNumber;
    }
}

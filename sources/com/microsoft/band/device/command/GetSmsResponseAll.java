package com.microsoft.band.device.command;

import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
import com.microsoft.band.internal.util.BufferUtil;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class GetSmsResponseAll extends CommandBase {
    private static final int STRUCT_SIZE = 2576;
    private static final long serialVersionUID = -598841430348923529L;
    private final String[] mMessages;

    public GetSmsResponseAll() {
        super(BandDeviceConstants.Command.CargoFireballGetAllSmsResponse, 0, STRUCT_SIZE);
        this.mMessages = new String[8];
    }

    @Override // com.microsoft.band.internal.CommandBase
    public void loadResult(ByteBuffer buffer) {
        for (int i = 0; i < this.mMessages.length; i++) {
            this.mMessages[i] = BufferUtil.getString(buffer, 161);
        }
    }

    public String[] getMessages() {
        return this.mMessages;
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return null;
    }
}

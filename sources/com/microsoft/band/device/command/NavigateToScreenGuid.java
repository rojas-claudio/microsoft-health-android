package com.microsoft.band.device.command;

import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
import com.microsoft.band.internal.util.BufferUtil;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class NavigateToScreenGuid extends CommandBase {
    private static final int ARG_SIZE = 2;
    private static byte MESSAGE_SIZE = 0;
    private static final long serialVersionUID = 1;
    private int mScreenId;

    public NavigateToScreenGuid(byte screenId) {
        super(BandDeviceConstants.Command.CargoFireballUINavigateToScreenGUID, 2, MESSAGE_SIZE);
        this.mScreenId = screenId;
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(2).putShort((short) this.mScreenId);
        return buffer.array();
    }
}

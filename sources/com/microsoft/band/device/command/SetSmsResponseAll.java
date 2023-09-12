package com.microsoft.band.device.command;

import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.util.StringHelper;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class SetSmsResponseAll extends CommandBase {
    private static final int STRUCT_SIZE = 2576;
    private static final long serialVersionUID = 5753042519565947386L;
    private final String[] mMessages;

    public SetSmsResponseAll(String... msgs) {
        super(BandDeviceConstants.Command.CargoFireballSetAllSmsResponse, 0, STRUCT_SIZE);
        this.mMessages = new String[8];
        int maxMessages = Math.min(msgs.length, 8);
        for (int i = 0; i < maxMessages; i++) {
            Validation.validateNullParameter(msgs[i], "SMS Response Message");
            msgs[i] = StringUtil.truncateString(msgs[i], 160);
        }
        System.arraycopy(msgs, 0, this.mMessages, 0, maxMessages);
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        ByteBuffer buffer = ByteBuffer.allocate(STRUCT_SIZE);
        for (int i = 0; i < this.mMessages.length; i++) {
            buffer.position(i * SetSmsResponse.MAX_MESSAGE_LENGTH_BYTES);
            if (this.mMessages[i] != null) {
                buffer.put(StringHelper.getBytes(this.mMessages[i]));
            }
        }
        return buffer.array();
    }
}

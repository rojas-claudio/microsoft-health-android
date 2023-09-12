package com.microsoft.band.device.command;

import com.microsoft.band.internal.BandDeviceConstants;
import com.microsoft.band.internal.CommandBase;
import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.util.StringHelper;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class SetSmsResponse extends CommandBase {
    public static final int MAX_MESSAGE_LENGTH = 160;
    public static final int MAX_MESSAGE_LENGTH_BYTES = 322;
    public static final int NUM_SMS_RESPONSES = 8;
    private static final int STRUCT_SIZE = 323;
    private static final long serialVersionUID = 1511537694189696697L;
    private final int mIndex;
    private final String mMessage;

    public SetSmsResponse(SmsResponseType type, int index, String message) {
        super(BandDeviceConstants.Command.CargoFireballSetSmsResponse, 0, STRUCT_SIZE);
        Validation.validateNullParameter(message, "SMS Response Message");
        this.mMessage = StringUtil.truncateString(message, 160);
        this.mIndex = ((type.mId * 8) / 2) + index;
    }

    private byte[] toBytes() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(STRUCT_SIZE);
        buffer.put((byte) (this.mIndex & 255));
        if (this.mMessage != null) {
            buffer.put(StringHelper.getBytes(this.mMessage));
        }
        buffer.putShort((short) 0);
        return buffer.array();
    }

    @Override // com.microsoft.band.internal.CommandBase
    public byte[] getExtendedData() {
        return toBytes();
    }
}

package com.microsoft.band.device;

import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.internal.util.StringUtil;
import com.microsoft.band.internal.util.Validation;
import com.microsoft.band.util.StringHelper;
import com.microsoft.kapp.utils.Constants;
import java.io.Serializable;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class NotificationInsights implements Serializable {
    private static final long serialVersionUID = 1;
    private String mLine1;
    private String mLine2;
    private String mMessage;
    private boolean mIsTwoLine = false;
    private byte mRsvd1 = 0;
    private byte mRsvd2 = 0;

    public NotificationInsights(String line1, String line2) {
        this.mLine1 = setString(line1, "NotificationInsight: LineOne", 40);
        this.mLine2 = setString(line2, "NotificationInsight: LineTwo", 160);
    }

    public NotificationInsights(String message) {
        this.mMessage = setString(message, "NotificationInsight2: " + Constants.NOTIFICATION_MESSAGE, 160);
    }

    public boolean isTwoLine() {
        return this.mIsTwoLine;
    }

    public byte[] toBytes() {
        ByteBuffer buffer;
        if (this.mIsTwoLine) {
            byte[] line1Bytes = StringHelper.getBytes(this.mLine1);
            byte[] line2Bytes = StringHelper.getBytes(this.mLine2);
            int line1BytesLength = line1Bytes.length;
            int line2BytesLength = line2Bytes.length;
            buffer = BufferUtil.allocateLittleEndian(6 + line1BytesLength + line2BytesLength).putShort((short) line1BytesLength).putShort((short) line2BytesLength).put(this.mRsvd1).put(this.mRsvd2).put(line1Bytes).put(line2Bytes);
        } else {
            byte[] msgBytes = StringHelper.getBytes(this.mMessage);
            int msgBytesLength = msgBytes.length;
            buffer = BufferUtil.allocateLittleEndian(4 + msgBytesLength).putShort((short) msgBytesLength).put(this.mRsvd1).put(this.mRsvd2).put(msgBytes);
        }
        return buffer.array();
    }

    private String setString(String msg, String prompt, int limit) {
        Validation.validateNullParameter(msg, prompt);
        return StringUtil.truncateString(msg, limit);
    }
}

package com.microsoft.band.device.keyboard;

import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.util.KDKLog;
import com.microsoft.band.util.StringHelper;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class KeyboardContextPushMessage {
    private static final short MAX_KBD_CONTEXT_LEN = 51;
    private String mContext;
    private short mContextLength;
    private static final String TAG = KeyboardContextPushMessage.class.getSimpleName();
    private static final String KEYBOARD_TAG = TAG + ": " + InternalBandConstants.KEYBOARD_BASE_TAG;

    public KeyboardContextPushMessage(ByteBuffer buffer) {
        this.mContextLength = buffer.getShort();
        byte[] bytes = new byte[51];
        int length = Math.min((int) this.mContextLength, 51);
        buffer.get(bytes);
        try {
            this.mContext = StringHelper.valueOf(bytes, 0, length);
        } catch (UnsupportedEncodingException e) {
            KDKLog.e(KEYBOARD_TAG, "Unsupported encoding for context setting context to null", e);
            this.mContext = null;
        }
        KDKLog.d(KEYBOARD_TAG, "Got context message (" + ((int) this.mContextLength) + ") [" + this.mContext + "]");
    }

    public short getContextLength() {
        return this.mContextLength;
    }

    public String getContext() {
        return this.mContextLength > 0 ? this.mContext : "";
    }
}

package com.microsoft.band.device.keyboard;

import com.microsoft.band.internal.InternalBandConstants;
import com.microsoft.band.internal.util.KDKLog;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class CompactTouchSample {
    private KTouchType mTouchType;
    private short mXY;
    private byte mZ;
    private static final String TAG = CompactTouchSample.class.getSimpleName();
    private static final String KEYBOARD_TAG = TAG + ": " + InternalBandConstants.KEYBOARD_BASE_TAG;

    public CompactTouchSample(ByteBuffer buffer) {
        this.mXY = buffer.getShort();
        this.mTouchType = KTouchType.valueOf(buffer.get());
        this.mZ = buffer.get();
        KDKLog.d(KEYBOARD_TAG, "Touch point (" + ((int) getX()) + "," + ((int) getY()) + "," + ((int) getZ()) + ") of type " + this.mTouchType);
    }

    public short getX() {
        return (short) ((this.mXY & 65408) >> 7);
    }

    public short getY() {
        return (short) (this.mXY & 127);
    }

    public void setXY(short x, short y) {
        this.mXY = (short) (((x & 511) << 7) | (y & 127));
    }

    public KTouchType getTouchType() {
        return this.mTouchType;
    }

    public short getZ() {
        return this.mZ;
    }
}

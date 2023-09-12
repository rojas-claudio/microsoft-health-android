package com.microsoft.band.service.device;

import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.internal.util.BufferUtil;
import java.io.Serializable;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class SensorLogMetadata implements Serializable {
    private static final long serialVersionUID = 1;
    private long mByteCount;
    private long mEndingSeqNumber;
    private long mStartingSeqNumber;

    public void SensorLogMetaData() {
        this.mStartingSeqNumber = -1L;
        this.mEndingSeqNumber = -1L;
        this.mByteCount = 0L;
    }

    public void initializeMetadata(ByteBuffer data) {
        this.mStartingSeqNumber = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mEndingSeqNumber = BitHelper.unsignedIntegerToLong(data.getInt());
        this.mByteCount = BitHelper.unsignedIntegerToLong(data.getInt());
    }

    public byte[] toBytes() {
        ByteBuffer buffer = BufferUtil.allocateLittleEndian(12);
        buffer.putInt(BitHelper.longToUnsignedInt(this.mStartingSeqNumber));
        buffer.putInt(BitHelper.longToUnsignedInt(this.mEndingSeqNumber));
        buffer.putInt(BitHelper.longToUnsignedInt(this.mByteCount));
        return buffer.array();
    }

    public int getChunkCount() {
        return (int) ((this.mEndingSeqNumber - this.mStartingSeqNumber) + 1);
    }

    public long getStartingSeqNumber() {
        return this.mStartingSeqNumber;
    }

    public long getEndingSeqNumber() {
        return this.mEndingSeqNumber;
    }

    public long getByteCount() {
        return this.mByteCount;
    }

    public String toString() {
        return String.format("SensorLog:%s", System.getProperty("line.separator")) + String.format("     |--startSeqId = %d %s", Long.valueOf(this.mStartingSeqNumber), System.getProperty("line.separator")) + String.format("     |--endSeqId = %d %s", Long.valueOf(this.mEndingSeqNumber), System.getProperty("line.separator")) + String.format("     |--byteCount = %d %s", Long.valueOf(this.mByteCount), System.getProperty("line.separator"));
    }
}

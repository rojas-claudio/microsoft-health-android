package com.microsoft.band.service.device;

import com.microsoft.band.internal.util.BufferUtil;
import com.microsoft.band.service.subscription.SubscriptionDataContract;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/* loaded from: classes.dex */
public class PushServicePayload {
    private static final int REMOTE_SUBSCRIPTION_HEADER_SIZE = 4;
    private byte[] mData;
    private final int mMissedSamples;
    private final SubscriptionDataContract.SensorType mSensorType;
    private final long mTimestamp;

    /* JADX INFO: Access modifiers changed from: protected */
    public PushServicePayload(SubscriptionDataContract.SensorType sensorType, int missedSamples, long timestamp, byte[] data, int offset, int length) {
        if (sensorType == null) {
            throw new IllegalArgumentException("sensorType not specified");
        }
        if (data == null) {
            throw new IllegalArgumentException("data buffer not specified");
        }
        if (length <= 0) {
            throw new IllegalArgumentException("length must be greater than 0");
        }
        this.mSensorType = sensorType;
        this.mMissedSamples = missedSamples;
        this.mTimestamp = timestamp;
        this.mData = BufferUtil.copy(data, offset, length);
    }

    public static PushServicePayload create(ByteBuffer buffer, long timestamp) {
        PushServicePayload payload = null;
        if (buffer.remaining() >= 4) {
            buffer.mark();
            SubscriptionDataContract.SensorType sensorType = SubscriptionDataContract.SensorType.lookup(buffer.get() & 255);
            if (SubscriptionDataContract.SensorType.Unknown != sensorType) {
                int missedSamples = buffer.get() & 255;
                int dataLength = buffer.getShort() & 65535;
                payload = create(buffer, sensorType, missedSamples, dataLength, timestamp);
            }
            if (payload == null) {
                buffer.reset();
            }
        }
        return payload;
    }

    public static PushServicePayload create(ByteBuffer buffer, SubscriptionDataContract.SensorType sensorType, int missedSamples, int dataLength, long timestamp) {
        if (dataLength <= 0 || buffer.remaining() < dataLength) {
            return null;
        }
        PushServicePayload payload = new PushServicePayload(sensorType, missedSamples, timestamp, buffer.array(), buffer.arrayOffset() + buffer.position(), dataLength);
        buffer.position(buffer.position() + dataLength);
        return payload;
    }

    public SubscriptionDataContract.SensorType getSensorType() {
        return this.mSensorType;
    }

    public byte[] getData() {
        return this.mData;
    }

    public int getDataLength() {
        return this.mData.length;
    }

    public int getLength() {
        return getDataLength() + 4;
    }

    public int getMissedSamples() {
        return this.mMissedSamples;
    }

    public long getTimestamp() {
        return this.mTimestamp;
    }

    public ByteBuffer getDataBuffer() {
        return ByteBuffer.wrap(this.mData).order(ByteOrder.LITTLE_ENDIAN);
    }

    public String toString() {
        return String.format("<%s> - [%s]", this.mSensorType, BufferUtil.toHexString(this.mData, 0, getDataLength()), Integer.valueOf(this.mMissedSamples));
    }
}

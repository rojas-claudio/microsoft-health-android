package com.microsoft.band.internal.device.subscription;

import android.os.Parcel;
import com.microsoft.band.internal.device.DeviceDataModel;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public abstract class SubscriptionDataModel extends DeviceDataModel {
    public static final float ACCELERATION_CONVERSION_CONSTANT = 2.4414062E-4f;
    private int mMissedSamples;
    private long mTimeStamp;

    protected abstract void format(StringBuffer stringBuffer);

    /* JADX INFO: Access modifiers changed from: protected */
    public SubscriptionDataModel(Parcel in) {
        super(in);
        this.mMissedSamples = in.readInt();
        this.mTimeStamp = in.readLong();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SubscriptionDataModel(ByteBuffer buffer) {
        super(buffer);
    }

    public final int getMissedSamples() {
        return this.mMissedSamples;
    }

    public final void setMissedSamples(int value) {
        this.mMissedSamples = value;
    }

    public final long getTimestamp() {
        return this.mTimeStamp;
    }

    public final void setTimestamp(long value) {
        this.mTimeStamp = value;
    }

    public final String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getClass().getSimpleName()).append(String.format("Missed Samples = %d\n", Integer.valueOf(this.mMissedSamples))).append(String.format("TimeStamp = %d\n", Long.valueOf(this.mTimeStamp)));
        format(sb);
        return sb.toString();
    }

    @Override // com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mMissedSamples);
        dest.writeLong(this.mTimeStamp);
    }
}

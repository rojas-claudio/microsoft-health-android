package com.microsoft.band.internal.device.subscription;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.sensors.BandDistanceEvent;
import com.microsoft.band.sensors.MotionType;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class DistanceData extends SubscriptionDataModel implements BandDistanceEvent {
    public static final Parcelable.Creator<DistanceData> CREATOR = new Parcelable.Creator<DistanceData>() { // from class: com.microsoft.band.internal.device.subscription.DistanceData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DistanceData createFromParcel(Parcel in) {
            return new DistanceData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DistanceData[] newArray(int size) {
            return new DistanceData[size];
        }
    };
    private final boolean mIsGpsUsed;
    private final int mMode;
    private final long mPace;
    private final long mSpeed;
    private final long mTotalDistance;
    private final long mTotalGpsDistance;
    private final long mTotalPedometerDistance;

    protected DistanceData(Parcel in) {
        super(in);
        long[] longArray = new long[5];
        in.readLongArray(longArray);
        this.mTotalDistance = longArray[0];
        this.mTotalPedometerDistance = longArray[1];
        this.mTotalGpsDistance = longArray[2];
        this.mSpeed = longArray[3];
        this.mPace = longArray[4];
        this.mMode = in.readInt();
        this.mIsGpsUsed = in.readByte() == 1;
    }

    public DistanceData(ByteBuffer buffer) {
        super(buffer);
        this.mTotalDistance = BitHelper.unsignedIntegerToLong(buffer.getInt());
        this.mTotalPedometerDistance = BitHelper.unsignedIntegerToLong(buffer.getInt());
        this.mTotalGpsDistance = BitHelper.unsignedIntegerToLong(buffer.getInt());
        this.mSpeed = BitHelper.unsignedIntegerToLong(buffer.getInt());
        this.mPace = BitHelper.unsignedIntegerToLong(buffer.getInt());
        this.mMode = buffer.get();
        this.mIsGpsUsed = buffer.get() == 1;
    }

    @Override // com.microsoft.band.sensors.BandDistanceEvent
    public final long getTotalDistance() {
        return this.mTotalDistance;
    }

    @Override // com.microsoft.band.sensors.BandDistanceEvent
    public final float getSpeed() {
        return (float) this.mSpeed;
    }

    @Override // com.microsoft.band.sensors.BandDistanceEvent
    public final float getPace() {
        return (float) this.mPace;
    }

    @Override // com.microsoft.band.sensors.BandDistanceEvent
    public final MotionType getMotionType() {
        return MotionType.values()[this.mMode];
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel
    protected final void format(StringBuffer sb) {
        sb.append(String.format("Total Distance = %d cm\n", Long.valueOf(getTotalDistance()))).append(String.format("Speed = %.2f cm/s\n", Float.valueOf(getSpeed()))).append(String.format("Pace = %.2f ms/m\n", Float.valueOf(getPace()))).append(String.format("Motion Type = %s\n", getMotionType()));
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel, com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public final void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLongArray(new long[]{this.mTotalDistance, this.mTotalPedometerDistance, this.mTotalGpsDistance, this.mSpeed, this.mPace});
        dest.writeInt(this.mMode);
        dest.writeByte((byte) (this.mIsGpsUsed ? 0 : 1));
    }
}

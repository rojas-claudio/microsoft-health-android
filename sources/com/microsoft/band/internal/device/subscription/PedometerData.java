package com.microsoft.band.internal.device.subscription;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.sensors.BandPedometerEvent;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class PedometerData extends SubscriptionDataModel implements BandPedometerEvent {
    public static final Parcelable.Creator<PedometerData> CREATOR = new Parcelable.Creator<PedometerData>() { // from class: com.microsoft.band.internal.device.subscription.PedometerData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PedometerData createFromParcel(Parcel in) {
            return new PedometerData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PedometerData[] newArray(int size) {
            return new PedometerData[size];
        }
    };
    private final int mMovementMode;
    private final int mMovementRate;
    private final int mStepRate;
    private final long mTotalMovements;
    private final long mTotalSteps;

    protected PedometerData(Parcel in) {
        super(in);
        long[] longArray = new long[2];
        in.readLongArray(longArray);
        this.mTotalSteps = longArray[0];
        this.mTotalMovements = longArray[1];
        this.mStepRate = in.readInt();
        this.mMovementRate = in.readInt();
        this.mMovementMode = in.readInt();
    }

    public PedometerData(ByteBuffer buffer) {
        super(buffer);
        this.mTotalSteps = buffer.getInt() & 4294967295L;
        this.mStepRate = buffer.getShort() & 65535;
        this.mMovementRate = buffer.getShort() & 65535;
        this.mTotalMovements = buffer.getInt() & 4294967295L;
        this.mMovementMode = buffer.get() & 255;
    }

    @Override // com.microsoft.band.sensors.BandPedometerEvent
    public final long getTotalSteps() {
        return this.mTotalSteps;
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel
    protected final void format(StringBuffer sb) {
        sb.append(String.format("Total Steps = %d\n", Long.valueOf(getTotalSteps())));
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel, com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public final void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLongArray(new long[]{this.mTotalSteps, this.mTotalMovements});
        dest.writeInt(this.mStepRate);
        dest.writeInt(this.mMovementRate);
        dest.writeInt(this.mMovementMode);
    }
}

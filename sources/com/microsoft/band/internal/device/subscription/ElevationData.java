package com.microsoft.band.internal.device.subscription;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.sensors.BandAltimeterEvent;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class ElevationData extends SubscriptionDataModel implements BandAltimeterEvent {
    public static final Parcelable.Creator<ElevationData> CREATOR = new Parcelable.Creator<ElevationData>() { // from class: com.microsoft.band.internal.device.subscription.ElevationData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ElevationData createFromParcel(Parcel in) {
            return new ElevationData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ElevationData[] newArray(int size) {
            return new ElevationData[size];
        }
    };
    private final float mBarometricTemperature;
    private final int mElevationFromMSL;
    private final long mElevationGain;
    private final long mElevationGainElevator;
    private final long mElevationGainStepping;
    private final long mElevationLoss;
    private final long mElevationLossElevator;
    private final long mElevationLossStepping;
    private final int mElevationRate;
    private final long mFloorCountAscended;
    private final long mFloorCountDescended;
    private final long mStepsInElevationGain;
    private final long mStepsInElevationLoss;

    protected ElevationData(Parcel in) {
        super(in);
        this.mElevationFromMSL = in.readInt();
        this.mBarometricTemperature = in.readFloat();
        this.mElevationGain = in.readLong();
        this.mElevationLoss = in.readLong();
        this.mElevationGainStepping = in.readLong();
        this.mElevationLossStepping = in.readLong();
        this.mElevationGainElevator = in.readLong();
        this.mElevationLossElevator = in.readLong();
        this.mStepsInElevationGain = in.readLong();
        this.mStepsInElevationLoss = in.readLong();
        this.mElevationRate = in.readInt();
        this.mFloorCountAscended = in.readLong();
        this.mFloorCountDescended = in.readLong();
    }

    public ElevationData(ByteBuffer buffer) {
        super(buffer);
        this.mElevationFromMSL = buffer.getInt();
        this.mBarometricTemperature = buffer.getFloat();
        this.mElevationGain = BitHelper.unsignedIntegerToLong(buffer.getInt());
        this.mElevationLoss = BitHelper.unsignedIntegerToLong(buffer.getInt());
        this.mElevationGainStepping = BitHelper.unsignedIntegerToLong(buffer.getInt());
        this.mElevationLossStepping = BitHelper.unsignedIntegerToLong(buffer.getInt());
        this.mElevationGainElevator = BitHelper.unsignedIntegerToLong(buffer.getInt());
        this.mElevationLossElevator = BitHelper.unsignedIntegerToLong(buffer.getInt());
        this.mStepsInElevationGain = BitHelper.unsignedIntegerToLong(buffer.getInt());
        this.mStepsInElevationLoss = BitHelper.unsignedIntegerToLong(buffer.getInt());
        this.mElevationRate = buffer.getShort();
        this.mFloorCountAscended = BitHelper.unsignedIntegerToLong(buffer.getInt());
        this.mFloorCountDescended = BitHelper.unsignedIntegerToLong(buffer.getInt());
    }

    @Override // com.microsoft.band.sensors.BandAltimeterEvent
    public final long getTotalGain() {
        return this.mElevationGain;
    }

    @Override // com.microsoft.band.sensors.BandAltimeterEvent
    public final long getTotalLoss() {
        return this.mElevationLoss;
    }

    @Override // com.microsoft.band.sensors.BandAltimeterEvent
    public final long getSteppingGain() {
        return this.mElevationGainStepping;
    }

    @Override // com.microsoft.band.sensors.BandAltimeterEvent
    public final long getSteppingLoss() {
        return this.mElevationLossStepping;
    }

    @Override // com.microsoft.band.sensors.BandAltimeterEvent
    public final long getStepsAscended() {
        return this.mStepsInElevationGain;
    }

    @Override // com.microsoft.band.sensors.BandAltimeterEvent
    public final long getStepsDescended() {
        return this.mStepsInElevationLoss;
    }

    @Override // com.microsoft.band.sensors.BandAltimeterEvent
    public final float getRate() {
        return this.mElevationRate;
    }

    @Override // com.microsoft.band.sensors.BandAltimeterEvent
    public final long getFlightsAscended() {
        return this.mFloorCountAscended;
    }

    @Override // com.microsoft.band.sensors.BandAltimeterEvent
    public final long getFlightsDescended() {
        return this.mFloorCountDescended;
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel
    protected final void format(StringBuffer sb) {
        sb.append(String.format("Total Gain = %d cm\n", Long.valueOf(getTotalGain()))).append(String.format("Total Loss = %d cm\n", Long.valueOf(getTotalLoss()))).append(String.format("Stepping Gain = %d cm\n", Long.valueOf(getSteppingGain()))).append(String.format("Stepping Loss = %d cm\n", Long.valueOf(getSteppingLoss()))).append(String.format("Steps Ascended = %d\n", Long.valueOf(getStepsAscended()))).append(String.format("Steps Descended = %d\n", Long.valueOf(getStepsDescended()))).append(String.format("Rate = %f cm/s\n", Float.valueOf(getRate()))).append(String.format("Flights of Stairs Ascended = %d\n", Long.valueOf(getFlightsAscended()))).append(String.format("Flights of Stairs Descended = %d\n", Long.valueOf(getFlightsDescended())));
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel, com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public final void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mElevationFromMSL);
        dest.writeFloat(this.mBarometricTemperature);
        dest.writeLong(this.mElevationGain);
        dest.writeLong(this.mElevationLoss);
        dest.writeLong(this.mElevationGainStepping);
        dest.writeLong(this.mElevationLossStepping);
        dest.writeLong(this.mElevationGainElevator);
        dest.writeLong(this.mElevationLossElevator);
        dest.writeLong(this.mStepsInElevationGain);
        dest.writeLong(this.mStepsInElevationLoss);
        dest.writeInt(this.mElevationRate);
        dest.writeLong(this.mFloorCountAscended);
        dest.writeLong(this.mFloorCountDescended);
    }
}

package com.microsoft.band.internal.device.subscription;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.sensors.BandRRIntervalEvent;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class RRIntervalData extends SubscriptionDataModel implements BandRRIntervalEvent {
    public static final Parcelable.Creator<RRIntervalData> CREATOR = new Parcelable.Creator<RRIntervalData>() { // from class: com.microsoft.band.internal.device.subscription.RRIntervalData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RRIntervalData createFromParcel(Parcel in) {
            return new RRIntervalData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RRIntervalData[] newArray(int size) {
            return new RRIntervalData[size];
        }
    };
    private final int mIntervalSampleCount;
    private final long mTotalSampleCount;

    protected RRIntervalData(Parcel in) {
        super(in);
        this.mTotalSampleCount = in.readLong();
        this.mIntervalSampleCount = in.readInt();
    }

    public RRIntervalData(ByteBuffer buffer) {
        super(buffer);
        this.mTotalSampleCount = BitHelper.unsignedIntegerToLong(buffer.getInt());
        this.mIntervalSampleCount = BitHelper.unsignedShortToInteger(buffer.getShort());
    }

    @Override // com.microsoft.band.sensors.BandRRIntervalEvent
    public final double getInterval() {
        return this.mIntervalSampleCount * 0.016592d;
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel
    protected final void format(StringBuffer sb) {
        sb.append(String.format("RR Interval = %.3f s\n", Double.valueOf(getInterval())));
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel, com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public final void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.mTotalSampleCount);
        dest.writeInt(this.mIntervalSampleCount);
    }
}

package com.microsoft.band.internal.device.subscription;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.HeartRateQuality;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class HeartRateData extends SubscriptionDataModel implements BandHeartRateEvent {
    public static final Parcelable.Creator<HeartRateData> CREATOR = new Parcelable.Creator<HeartRateData>() { // from class: com.microsoft.band.internal.device.subscription.HeartRateData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HeartRateData createFromParcel(Parcel in) {
            return new HeartRateData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HeartRateData[] newArray(int size) {
            return new HeartRateData[size];
        }
    };
    private static final int QUALITY_THREASHOLD = 6;
    private final int mQuality;
    private final int mRate;

    public HeartRateData(Parcel in) {
        super(in);
        this.mRate = in.readInt();
        this.mQuality = in.readInt();
    }

    public HeartRateData(ByteBuffer buffer) {
        super(buffer);
        this.mRate = buffer.get() & 255;
        this.mQuality = buffer.get() & 255;
    }

    @Override // com.microsoft.band.sensors.BandHeartRateEvent
    public final int getHeartRate() {
        return this.mRate;
    }

    @Override // com.microsoft.band.sensors.BandHeartRateEvent
    public final HeartRateQuality getQuality() {
        return this.mQuality >= 6 ? HeartRateQuality.LOCKED : HeartRateQuality.ACQUIRING;
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel
    protected final void format(StringBuffer sb) {
        sb.append(String.format("Heart Rate = %d beats per minute\n", Integer.valueOf(getHeartRate()))).append(String.format("Quality = %s\n", getQuality()));
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel, com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public final void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mRate);
        dest.writeInt(this.mQuality);
    }
}

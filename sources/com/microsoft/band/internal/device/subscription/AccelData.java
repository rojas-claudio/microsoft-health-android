package com.microsoft.band.internal.device.subscription;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.sensors.BandAccelerometerEvent;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class AccelData extends SubscriptionDataModel implements BandAccelerometerEvent {
    public static final Parcelable.Creator<AccelData> CREATOR = new Parcelable.Creator<AccelData>() { // from class: com.microsoft.band.internal.device.subscription.AccelData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AccelData createFromParcel(Parcel in) {
            return new AccelData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AccelData[] newArray(int size) {
            return new AccelData[size];
        }
    };
    private final int mXAccel;
    private final int mYAccel;
    private final int mZAccel;

    public AccelData(Parcel in) {
        super(in);
        this.mXAccel = in.readInt();
        this.mYAccel = in.readInt();
        this.mZAccel = in.readInt();
    }

    public AccelData(ByteBuffer buffer) {
        super(buffer);
        this.mXAccel = buffer.getShort();
        this.mYAccel = buffer.getShort();
        this.mZAccel = buffer.getShort();
    }

    @Override // com.microsoft.band.sensors.BandAccelerometerEvent
    public final float getAccelerationX() {
        return this.mXAccel * 2.4414062E-4f;
    }

    @Override // com.microsoft.band.sensors.BandAccelerometerEvent
    public final float getAccelerationY() {
        return this.mYAccel * 2.4414062E-4f;
    }

    @Override // com.microsoft.band.sensors.BandAccelerometerEvent
    public final float getAccelerationZ() {
        return this.mZAccel * 2.4414062E-4f;
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel
    protected final void format(StringBuffer sb) {
        sb.append(String.format("AccelX = %.3f g\n", Float.valueOf(getAccelerationX()))).append(String.format("AccelY = %.3f g\n", Float.valueOf(getAccelerationY()))).append(String.format("AccelZ = %.3f g\n", Float.valueOf(getAccelerationZ())));
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel, com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public final void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mXAccel);
        dest.writeInt(this.mYAccel);
        dest.writeInt(this.mZAccel);
    }
}

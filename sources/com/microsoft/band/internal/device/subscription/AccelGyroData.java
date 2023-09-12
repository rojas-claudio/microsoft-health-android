package com.microsoft.band.internal.device.subscription;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.sensors.BandGyroscopeEvent;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class AccelGyroData extends SubscriptionDataModel implements BandGyroscopeEvent {
    public static final Parcelable.Creator<AccelGyroData> CREATOR = new Parcelable.Creator<AccelGyroData>() { // from class: com.microsoft.band.internal.device.subscription.AccelGyroData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AccelGyroData createFromParcel(Parcel in) {
            return new AccelGyroData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AccelGyroData[] newArray(int size) {
            return new AccelGyroData[size];
        }
    };
    public static final float GYRO_CONVERSION_CONSTANT = 0.030487806f;
    private final int mXAccel;
    private final int mXGyro;
    private final int mYAccel;
    private final int mYGyro;
    private final int mZAccel;
    private final int mZGyro;

    public AccelGyroData(Parcel in) {
        super(in);
        this.mXAccel = in.readInt();
        this.mYAccel = in.readInt();
        this.mZAccel = in.readInt();
        this.mXGyro = in.readInt();
        this.mYGyro = in.readInt();
        this.mZGyro = in.readInt();
    }

    public AccelGyroData(ByteBuffer buffer) {
        super(buffer);
        this.mXAccel = buffer.getShort();
        this.mYAccel = buffer.getShort();
        this.mZAccel = buffer.getShort();
        this.mXGyro = buffer.getShort();
        this.mYGyro = buffer.getShort();
        this.mZGyro = buffer.getShort();
    }

    @Override // com.microsoft.band.sensors.BandGyroscopeEvent
    public final float getAccelerationX() {
        return this.mXAccel * 2.4414062E-4f;
    }

    @Override // com.microsoft.band.sensors.BandGyroscopeEvent
    public final float getAccelerationY() {
        return this.mYAccel * 2.4414062E-4f;
    }

    @Override // com.microsoft.band.sensors.BandGyroscopeEvent
    public final float getAccelerationZ() {
        return this.mZAccel * 2.4414062E-4f;
    }

    @Override // com.microsoft.band.sensors.BandGyroscopeEvent
    public final float getAngularVelocityX() {
        return this.mXGyro * 0.030487806f;
    }

    @Override // com.microsoft.band.sensors.BandGyroscopeEvent
    public final float getAngularVelocityY() {
        return this.mYGyro * 0.030487806f;
    }

    @Override // com.microsoft.band.sensors.BandGyroscopeEvent
    public final float getAngularVelocityZ() {
        return this.mZGyro * 0.030487806f;
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel
    protected final void format(StringBuffer sb) {
        sb.append(String.format("AccelX = %.3f g\n", Float.valueOf(getAccelerationX()))).append(String.format("AccelY = %.3f g\n", Float.valueOf(getAccelerationY()))).append(String.format("AccelZ = %.3f g\n", Float.valueOf(getAccelerationZ()))).append(String.format("GyroX = %.3f degrees/second\n", Float.valueOf(getAngularVelocityX()))).append(String.format("GyroY = %.3f degrees/second\n", Float.valueOf(getAngularVelocityY()))).append(String.format("GyroZ = %.3f degrees/second\n", Float.valueOf(getAngularVelocityZ())));
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel, com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public final void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mXAccel);
        dest.writeInt(this.mYAccel);
        dest.writeInt(this.mZAccel);
        dest.writeInt(this.mXGyro);
        dest.writeInt(this.mYGyro);
        dest.writeInt(this.mZGyro);
    }
}

package com.microsoft.band.internal.device.subscription;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.sensors.BandBarometerEvent;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class BarometerData extends SubscriptionDataModel implements BandBarometerEvent {
    public static final Parcelable.Creator<BarometerData> CREATOR = new Parcelable.Creator<BarometerData>() { // from class: com.microsoft.band.internal.device.subscription.BarometerData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BarometerData createFromParcel(Parcel in) {
            return new BarometerData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BarometerData[] newArray(int size) {
            return new BarometerData[size];
        }
    };
    private final int mRawAirPressure;
    private final int mRawTemperature;

    protected BarometerData(Parcel in) {
        super(in);
        this.mRawAirPressure = in.readInt();
        this.mRawTemperature = in.readInt();
    }

    public BarometerData(ByteBuffer buffer) {
        super(buffer);
        this.mRawAirPressure = buffer.getInt();
        this.mRawTemperature = buffer.getShort();
    }

    @Override // com.microsoft.band.sensors.BandBarometerEvent
    public final double getAirPressure() {
        return this.mRawAirPressure / 4096.0d;
    }

    @Override // com.microsoft.band.sensors.BandBarometerEvent
    public final double getTemperature() {
        return 42.5d + (this.mRawTemperature / 480.0d);
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel
    protected final void format(StringBuffer sb) {
        sb.append(String.format("Air Pressure = %.3f hPa\n", Double.valueOf(getAirPressure()))).append(String.format("Temperature = %.2f degrees Celsius\n", Double.valueOf(getTemperature())));
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel, com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public final void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mRawAirPressure);
        dest.writeInt(this.mRawTemperature);
    }
}

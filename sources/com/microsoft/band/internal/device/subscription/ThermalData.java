package com.microsoft.band.internal.device.subscription;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.sensors.BandSkinTemperatureEvent;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class ThermalData extends SubscriptionDataModel implements BandSkinTemperatureEvent {
    public static final Parcelable.Creator<ThermalData> CREATOR = new Parcelable.Creator<ThermalData>() { // from class: com.microsoft.band.internal.device.subscription.ThermalData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ThermalData createFromParcel(Parcel in) {
            return new ThermalData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ThermalData[] newArray(int size) {
            return new ThermalData[size];
        }
    };
    private final boolean mHighAlert;
    private final boolean mLowAlert;
    private final short mTemperature;

    protected ThermalData(Parcel in) {
        super(in);
        this.mTemperature = (short) in.readInt();
        boolean[] boolArray = new boolean[2];
        in.readBooleanArray(boolArray);
        this.mHighAlert = boolArray[0];
        this.mLowAlert = boolArray[1];
    }

    public ThermalData(ByteBuffer buffer) {
        super(buffer);
        this.mTemperature = buffer.getShort();
        this.mHighAlert = buffer.getInt() != 0;
        this.mLowAlert = buffer.getInt() != 0;
    }

    @Override // com.microsoft.band.sensors.BandSkinTemperatureEvent
    public final float getTemperature() {
        return this.mTemperature / 100.0f;
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel
    protected final void format(StringBuffer sb) {
        sb.append(String.format("Temperature = %.2f degrees Celsius\n", Float.valueOf(getTemperature())));
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel, com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public final void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mTemperature);
        dest.writeBooleanArray(new boolean[]{this.mHighAlert, this.mLowAlert});
    }
}

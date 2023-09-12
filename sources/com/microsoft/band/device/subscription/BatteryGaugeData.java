package com.microsoft.band.device.subscription;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.device.subscription.SubscriptionDataModel;
import com.microsoft.band.sensors.BandBatteryLevelEvent;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class BatteryGaugeData extends SubscriptionDataModel implements BandBatteryLevelEvent {
    public static final Parcelable.Creator<BatteryGaugeData> CREATOR = new Parcelable.Creator<BatteryGaugeData>() { // from class: com.microsoft.band.device.subscription.BatteryGaugeData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BatteryGaugeData createFromParcel(Parcel in) {
            return new BatteryGaugeData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BatteryGaugeData[] newArray(int size) {
            return new BatteryGaugeData[size];
        }
    };
    private int mAlerts;
    private int mFilteredVoltage;
    private byte mLevel;

    protected BatteryGaugeData(Parcel in) {
        super(in);
        this.mLevel = in.readByte();
        this.mFilteredVoltage = in.readInt();
        this.mAlerts = in.readInt();
    }

    public BatteryGaugeData(ByteBuffer buffer) {
        super(buffer);
        this.mLevel = buffer.get();
        this.mFilteredVoltage = buffer.getShort() & 255;
        this.mAlerts = buffer.getShort() & 255;
    }

    @Override // com.microsoft.band.sensors.BandBatteryLevelEvent
    public int getBatteryLevel() {
        return this.mLevel;
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel
    protected void format(StringBuffer sb) {
        sb.append(String.format("     |--Level = %d\n", Byte.valueOf(this.mLevel))).append(String.format("     |--FilteredVoltage = %d\n", Integer.valueOf(this.mFilteredVoltage))).append(String.format("     |--Alerts = x%04X\n", Integer.valueOf(this.mAlerts)));
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel, com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte(this.mLevel);
        dest.writeInt(this.mFilteredVoltage);
        dest.writeInt(this.mAlerts);
    }
}

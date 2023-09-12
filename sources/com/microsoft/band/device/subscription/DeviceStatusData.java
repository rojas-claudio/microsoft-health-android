package com.microsoft.band.device.subscription;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.device.subscription.SubscriptionDataModel;
import com.microsoft.band.sensors.BandConnectionStatus;
import com.microsoft.band.sensors.BandConnectionStatusEvent;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class DeviceStatusData extends SubscriptionDataModel implements BandConnectionStatusEvent {
    public static final Parcelable.Creator<DeviceStatusData> CREATOR = new Parcelable.Creator<DeviceStatusData>() { // from class: com.microsoft.band.device.subscription.DeviceStatusData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DeviceStatusData createFromParcel(Parcel in) {
            return new DeviceStatusData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DeviceStatusData[] newArray(int size) {
            return new DeviceStatusData[size];
        }
    };
    private BandConnectionStatus mDeviceState;

    public DeviceStatusData(Parcel in) {
        super(in);
        int stateOrdinal = in.readInt();
        if (stateOrdinal >= 0 && stateOrdinal < BandConnectionStatus.values().length) {
            this.mDeviceState = BandConnectionStatus.values()[stateOrdinal];
        } else {
            this.mDeviceState = BandConnectionStatus.UNKNOWN;
        }
    }

    public DeviceStatusData(ByteBuffer buffer) {
        super(buffer);
        int stateOrdinal = buffer.getInt();
        if (stateOrdinal >= 0 && stateOrdinal < BandConnectionStatus.values().length) {
            this.mDeviceState = BandConnectionStatus.values()[stateOrdinal];
        } else {
            this.mDeviceState = BandConnectionStatus.UNKNOWN;
        }
    }

    @Override // com.microsoft.band.sensors.BandConnectionStatusEvent
    public BandConnectionStatus getConnectionStatus() {
        return this.mDeviceState;
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel
    protected void format(StringBuffer sb) {
        sb.append(String.format("     |--State = %s\n", this.mDeviceState));
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel, com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mDeviceState == null ? 0 : this.mDeviceState.ordinal());
    }
}

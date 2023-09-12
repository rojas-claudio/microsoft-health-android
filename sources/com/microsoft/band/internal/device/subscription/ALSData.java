package com.microsoft.band.internal.device.subscription;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.sensors.BandAmbientLightEvent;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class ALSData extends SubscriptionDataModel implements BandAmbientLightEvent {
    public static final Parcelable.Creator<ALSData> CREATOR = new Parcelable.Creator<ALSData>() { // from class: com.microsoft.band.internal.device.subscription.ALSData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ALSData createFromParcel(Parcel in) {
            return new ALSData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ALSData[] newArray(int size) {
            return new ALSData[size];
        }
    };
    private final int mLuxValue;

    protected ALSData(Parcel in) {
        super(in);
        this.mLuxValue = in.readInt();
    }

    public ALSData(ByteBuffer buffer) {
        super(buffer);
        this.mLuxValue = BitHelper.unsignedShortToInteger(buffer.getShort());
    }

    @Override // com.microsoft.band.sensors.BandAmbientLightEvent
    public final int getBrightness() {
        return this.mLuxValue;
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel
    protected final void format(StringBuffer sb) {
        sb.append(String.format("Brightness = %d lux\n", Integer.valueOf(this.mLuxValue)));
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel, com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public final void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mLuxValue);
    }
}

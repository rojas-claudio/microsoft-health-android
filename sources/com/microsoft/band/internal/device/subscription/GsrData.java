package com.microsoft.band.internal.device.subscription;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.sensors.BandGsrEvent;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class GsrData extends SubscriptionDataModel implements BandGsrEvent {
    public static final Parcelable.Creator<GsrData> CREATOR = new Parcelable.Creator<GsrData>() { // from class: com.microsoft.band.internal.device.subscription.GsrData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GsrData createFromParcel(Parcel in) {
            return new GsrData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GsrData[] newArray(int size) {
            return new GsrData[size];
        }
    };
    private final int mCapSenseCounts;
    private final int mKOhm;
    private final int mStatus;

    protected GsrData(Parcel in) {
        super(in);
        this.mStatus = in.readInt();
        this.mKOhm = in.readInt();
        this.mCapSenseCounts = in.readInt();
    }

    public GsrData(ByteBuffer buffer) {
        super(buffer);
        this.mStatus = BitHelper.unsignedByteToInteger(buffer.get());
        this.mKOhm = buffer.getInt();
        this.mCapSenseCounts = BitHelper.unsignedShortToInteger(buffer.getShort());
    }

    @Override // com.microsoft.band.sensors.BandGsrEvent
    public final int getResistance() {
        return this.mKOhm;
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel
    protected final void format(StringBuffer sb) {
        sb.append(String.format("Resistance = %d kOhms\n", Integer.valueOf(getResistance())));
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel, com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public final void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mStatus);
        dest.writeInt(this.mKOhm);
        dest.writeInt(this.mCapSenseCounts);
    }
}

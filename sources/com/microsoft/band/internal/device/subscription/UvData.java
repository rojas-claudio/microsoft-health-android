package com.microsoft.band.internal.device.subscription;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.internal.util.BitHelper;
import com.microsoft.band.sensors.BandUVEvent;
import com.microsoft.band.sensors.UVIndexLevel;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class UvData extends SubscriptionDataModel implements BandUVEvent {
    public static final Parcelable.Creator<UvData> CREATOR = new Parcelable.Creator<UvData>() { // from class: com.microsoft.band.internal.device.subscription.UvData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UvData createFromParcel(Parcel in) {
            return new UvData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UvData[] newArray(int size) {
            return new UvData[size];
        }
    };
    private final int mIndexX10;
    private final int mIndexX10Peak;
    private final int mPhotodiodeCurrent;
    private final int mUVIIntensityLevel;

    protected UvData(Parcel in) {
        super(in);
        this.mIndexX10 = in.readInt();
        this.mIndexX10Peak = in.readInt();
        this.mUVIIntensityLevel = in.readInt();
        this.mPhotodiodeCurrent = in.readInt();
    }

    public UvData(ByteBuffer buffer) {
        super(buffer);
        this.mIndexX10 = BitHelper.unsignedByteToInteger(buffer.get());
        this.mIndexX10Peak = BitHelper.unsignedByteToInteger(buffer.get());
        this.mUVIIntensityLevel = BitHelper.unsignedShortToInteger(buffer.getShort());
        this.mPhotodiodeCurrent = BitHelper.unsignedShortToInteger(buffer.getShort());
    }

    @Override // com.microsoft.band.sensors.BandUVEvent
    public final UVIndexLevel getUVIndexLevel() {
        return UVIndexLevel.values()[this.mUVIIntensityLevel];
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel
    protected final void format(StringBuffer sb) {
        sb.append(String.format("UV Index Level = %s\n", getUVIndexLevel()));
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel, com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public final void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mIndexX10);
        dest.writeInt(this.mIndexX10Peak);
        dest.writeInt(this.mUVIIntensityLevel);
        dest.writeInt(this.mPhotodiodeCurrent);
    }
}

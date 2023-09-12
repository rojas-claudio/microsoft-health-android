package com.microsoft.band.internal.device.subscription;

import android.os.Parcel;
import android.os.Parcelable;
import com.microsoft.band.sensors.BandContactEvent;
import com.microsoft.band.sensors.BandContactState;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class DeviceContactData extends SubscriptionDataModel implements BandContactEvent {
    public static final Parcelable.Creator<DeviceContactData> CREATOR = new Parcelable.Creator<DeviceContactData>() { // from class: com.microsoft.band.internal.device.subscription.DeviceContactData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DeviceContactData createFromParcel(Parcel in) {
            return new DeviceContactData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DeviceContactData[] newArray(int size) {
            return new DeviceContactData[size];
        }
    };
    private final BandContactState mWornBasedOnGsrState;
    private final BandContactState mWornBasedOnHeartRateLedState;
    private final BandContactState mWornState;

    protected DeviceContactData(Parcel in) {
        super(in);
        this.mWornState = lookupState(in.readByte());
        this.mWornBasedOnGsrState = lookupState(in.readByte());
        this.mWornBasedOnHeartRateLedState = lookupState(in.readByte());
    }

    public DeviceContactData(ByteBuffer buffer) {
        super(buffer);
        this.mWornState = lookupState(buffer.get());
        this.mWornBasedOnGsrState = lookupState(buffer.get());
        this.mWornBasedOnHeartRateLedState = lookupState(buffer.get());
    }

    @Override // com.microsoft.band.sensors.BandContactEvent
    public final BandContactState getContactState() {
        return this.mWornState;
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel
    protected final void format(StringBuffer sb) {
        sb.append(String.format("Contact State = %s\n", getContactState()));
    }

    @Override // com.microsoft.band.internal.device.subscription.SubscriptionDataModel, com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public final void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mWornState == null ? BandContactState.UNKNOWN.ordinal() : this.mWornState.ordinal());
        dest.writeInt(this.mWornBasedOnGsrState == null ? BandContactState.UNKNOWN.ordinal() : this.mWornBasedOnGsrState.ordinal());
        dest.writeInt(this.mWornBasedOnHeartRateLedState == null ? BandContactState.UNKNOWN.ordinal() : this.mWornBasedOnHeartRateLedState.ordinal());
    }

    public static final BandContactState lookupState(int stateID) {
        BandContactState[] arr$ = BandContactState.values();
        for (BandContactState v : arr$) {
            if (v.getId() == stateID) {
                return v;
            }
        }
        return BandContactState.UNKNOWN;
    }
}

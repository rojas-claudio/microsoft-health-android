package com.microsoft.band.device;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
/* loaded from: classes.dex */
public final class DeviceSettings implements Parcelable, Serializable {
    public static final Parcelable.Creator<DeviceSettings> CREATOR = new Parcelable.Creator<DeviceSettings>() { // from class: com.microsoft.band.device.DeviceSettings.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DeviceSettings createFromParcel(Parcel in) {
            return new DeviceSettings(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DeviceSettings[] newArray(int size) {
            return new DeviceSettings[size];
        }
    };
    private static final long serialVersionUID = 1;
    private String mDeviceUuidString;
    private boolean mIsBand;
    private String mName;

    public DeviceSettings(String name, String macAddress, boolean isBand) {
        this.mName = name;
        this.mDeviceUuidString = macAddress;
        this.mIsBand = isBand;
    }

    public DeviceSettings(Parcel in) {
        this.mName = in.readString();
        this.mDeviceUuidString = in.readString();
        this.mIsBand = in.readByte() != 0;
    }

    public String getName() {
        return this.mName;
    }

    public String getDeviceId() {
        return this.mDeviceUuidString;
    }

    public boolean getIsBand() {
        return this.mIsBand;
    }

    public void setIsBand(boolean value) {
        this.mIsBand = value;
    }

    public String toString() {
        return String.format("%s\n     |--Name = %s\n     |--UUID = %s\n     |--Is Band = %s\n", getClass().getSimpleName(), getName(), getDeviceId(), Boolean.valueOf(getIsBand()));
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mDeviceUuidString);
        dest.writeByte((byte) (this.mIsBand ? 1 : 0));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}

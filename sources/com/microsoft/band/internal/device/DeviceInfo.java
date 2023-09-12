package com.microsoft.band.internal.device;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.UUID;
/* loaded from: classes.dex */
public final class DeviceInfo extends DeviceDataModel {
    public static final Parcelable.Creator<DeviceInfo> CREATOR = new Parcelable.Creator<DeviceInfo>() { // from class: com.microsoft.band.internal.device.DeviceInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DeviceInfo createFromParcel(Parcel in) {
            return new DeviceInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DeviceInfo[] newArray(int size) {
            return new DeviceInfo[size];
        }
    };
    private UUID mDeviceUUID;
    private String mFwVersion;
    private int mHardwareVersion;
    private int mLogVersion;
    private String mMacAddress;
    private String mName;
    private String mSerialNumber;

    public DeviceInfo(String name, String macAddress) {
        this.mName = name;
        this.mMacAddress = macAddress;
        this.mDeviceUUID = null;
    }

    public DeviceInfo(Parcel in) {
        super(in);
        this.mName = in.readString();
        this.mMacAddress = in.readString();
        long uuidMostSigBits = in.readLong();
        long uuidLeastSigBits = in.readLong();
        this.mDeviceUUID = new UUID(uuidMostSigBits, uuidLeastSigBits);
        this.mFwVersion = in.readString();
        this.mLogVersion = in.readInt();
        this.mSerialNumber = in.readString();
        if (getVersion() >= 16842752) {
            this.mHardwareVersion = in.readInt();
        }
    }

    public String getName() {
        return this.mName;
    }

    public String getMacAddress() {
        return this.mMacAddress;
    }

    public UUID getDeviceUUID() {
        return this.mDeviceUUID;
    }

    public String getFWVersion() {
        return this.mFwVersion;
    }

    public int getLogVersion() {
        return this.mLogVersion;
    }

    public String getSerialNumber() {
        return this.mSerialNumber;
    }

    public int getHardwareVersion() {
        return this.mHardwareVersion;
    }

    public void setDeviceUUID(UUID value) {
        this.mDeviceUUID = value;
    }

    public void setFWVersion(String fwVersion) {
        this.mFwVersion = fwVersion;
    }

    public void setLogVersion(int logVersion) {
        this.mLogVersion = logVersion;
    }

    public void setSerialNumber(String serialNumber) {
        this.mSerialNumber = serialNumber;
    }

    public void setHardwareVersion(int hardwareVersion) {
        this.mHardwareVersion = hardwareVersion;
    }

    public String toString() {
        return String.format("%s\n     |--Name = %s\n     |--MAC = %s\n     |--UUID = %s\n", getClass().getSimpleName(), getName(), getMacAddress(), getDeviceUUID());
    }

    @Override // com.microsoft.band.internal.device.DeviceDataModel, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mName);
        dest.writeString(this.mMacAddress);
        long uuidMostSigBits = this.mDeviceUUID == null ? 0L : this.mDeviceUUID.getMostSignificantBits();
        long uuidLeastSigBits = this.mDeviceUUID == null ? 0L : this.mDeviceUUID.getLeastSignificantBits();
        dest.writeLong(uuidMostSigBits);
        dest.writeLong(uuidLeastSigBits);
        dest.writeString(this.mFwVersion);
        dest.writeInt(this.mLogVersion);
        dest.writeString(this.mSerialNumber);
        if (getVersion() >= 16842752) {
            dest.writeInt(this.mHardwareVersion);
        }
    }
}

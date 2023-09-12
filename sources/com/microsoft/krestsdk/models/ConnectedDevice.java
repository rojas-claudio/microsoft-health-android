package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.multidevice.MultiDeviceConstants;
import com.microsoft.kapp.utils.Constants;
import java.util.UUID;
/* loaded from: classes.dex */
public class ConnectedDevice implements Parcelable {
    public static final Parcelable.Creator<ConnectedDevice> CREATOR = new Parcelable.Creator<ConnectedDevice>() { // from class: com.microsoft.krestsdk.models.ConnectedDevice.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ConnectedDevice createFromParcel(Parcel in) {
            return new ConnectedDevice(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ConnectedDevice[] newArray(int size) {
            return new ConnectedDevice[size];
        }
    };
    @SerializedName("DeviceMetadataHint")
    private String mName;
    @SerializedName("DeviceId")
    private String mUuidString;

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public MultiDeviceConstants.DeviceType getType() {
        return (this.mName == null || !this.mName.equalsIgnoreCase(Constants.ANDROID_PHONE_IDENTIFIER)) ? MultiDeviceConstants.DeviceType.BAND : MultiDeviceConstants.DeviceType.PHONE;
    }

    public void setUUID(String uuidString) {
        this.mUuidString = uuidString;
    }

    public UUID getUUID() {
        return UUID.fromString(this.mUuidString);
    }

    public ConnectedDevice() {
    }

    public ConnectedDevice(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mUuidString);
    }

    public void readFromParcel(Parcel in) {
        this.mName = in.readString();
        this.mUuidString = in.readString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}

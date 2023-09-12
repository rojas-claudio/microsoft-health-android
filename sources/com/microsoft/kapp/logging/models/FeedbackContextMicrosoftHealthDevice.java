package com.microsoft.kapp.logging.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.band.device.DeviceSettings;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.logging.LogConfiguration;
/* loaded from: classes.dex */
public class FeedbackContextMicrosoftHealthDevice implements Parcelable {
    public static final Parcelable.Creator<FeedbackContextMicrosoftHealthDevice> CREATOR = new Parcelable.Creator<FeedbackContextMicrosoftHealthDevice>() { // from class: com.microsoft.kapp.logging.models.FeedbackContextMicrosoftHealthDevice.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContextMicrosoftHealthDevice createFromParcel(Parcel in) {
            return new FeedbackContextMicrosoftHealthDevice(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContextMicrosoftHealthDevice[] newArray(int size) {
            return new FeedbackContextMicrosoftHealthDevice[size];
        }
    };
    @SerializedName("serialNumber")
    String mSerialNumber;
    @SerializedName("type")
    String mType;
    @SerializedName("uniqueId")
    String mUniqueId;
    @SerializedName("versions")
    FeedbackContextMicrosoftHealthDeviceVersions mVersion;

    public FeedbackContextMicrosoftHealthDevice() {
        this.mType = "Phone";
    }

    public FeedbackContextMicrosoftHealthDevice(DeviceSettings device, CargoConnection cargoConnection, LogConfiguration logConfiguration) {
        this.mType = device.getIsBand() ? "Microsoft Band" : "Phone";
        if (device.getIsBand()) {
            this.mUniqueId = device.getDeviceId();
            try {
                this.mVersion = new FeedbackContextMicrosoftHealthDeviceVersions(cargoConnection);
                if (logConfiguration.getLogMode().equals(LogMode.CAN_LOG_PRIVATE_DATA)) {
                    this.mSerialNumber = cargoConnection.getDeviceSerialNo();
                }
            } catch (Exception e) {
            }
        }
    }

    public FeedbackContextMicrosoftHealthDevice(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mType);
        dest.writeString(this.mUniqueId);
        dest.writeParcelable(this.mVersion, 0);
    }

    public void readFromParcel(Parcel in) {
        this.mType = in.readString();
        this.mUniqueId = in.readString();
        this.mVersion = (FeedbackContextMicrosoftHealthDeviceVersions) in.readParcelable(FeedbackContextMicrosoftHealthDeviceVersions.class.getClassLoader());
    }
}

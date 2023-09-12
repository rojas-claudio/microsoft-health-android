package com.microsoft.kapp.logging.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.band.device.FirmwareVersions;
import com.microsoft.kapp.CargoConnection;
/* loaded from: classes.dex */
public class FeedbackContextMicrosoftHealthDeviceVersions implements Parcelable {
    public static final Parcelable.Creator<FeedbackContextMicrosoftHealthDeviceVersions> CREATOR = new Parcelable.Creator<FeedbackContextMicrosoftHealthDeviceVersions>() { // from class: com.microsoft.kapp.logging.models.FeedbackContextMicrosoftHealthDeviceVersions.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContextMicrosoftHealthDeviceVersions createFromParcel(Parcel in) {
            return new FeedbackContextMicrosoftHealthDeviceVersions(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContextMicrosoftHealthDeviceVersions[] newArray(int size) {
            return new FeedbackContextMicrosoftHealthDeviceVersions[size];
        }
    };
    @SerializedName("application")
    String mApplication;
    @SerializedName("bootloader")
    String mBootloader;
    @SerializedName("pcbId")
    int mPcbId;
    @SerializedName("updater")
    String mUpdater;

    public FeedbackContextMicrosoftHealthDeviceVersions(CargoConnection cargoConnection) {
        try {
            FirmwareVersions connectedDevice = cargoConnection.getDeviceFirmwareVersionsObject();
            if (connectedDevice != null) {
                this.mApplication = cargoConnection.getDeviceFirmwareVersion().toString();
                this.mBootloader = connectedDevice.getBootloaderVersion().getCurrentVersion();
                this.mUpdater = connectedDevice.getUpdaterVersion().getCurrentVersion();
                this.mPcbId = connectedDevice.getBootloaderVersion().getPcbId();
            }
        } catch (Exception e) {
        }
    }

    public FeedbackContextMicrosoftHealthDeviceVersions(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mPcbId);
        dest.writeString(this.mApplication);
        dest.writeString(this.mBootloader);
        dest.writeString(this.mUpdater);
    }

    public void readFromParcel(Parcel in) {
        this.mPcbId = in.readInt();
        this.mApplication = in.readString();
        this.mBootloader = in.readString();
        this.mUpdater = in.readString();
    }
}

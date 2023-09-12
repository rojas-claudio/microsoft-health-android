package com.microsoft.kapp.logging.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.band.device.DeviceSettings;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.logging.LogConfiguration;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import java.util.ArrayList;
import java.util.UUID;
/* loaded from: classes.dex */
public class FeedbackContextMicrosoftHealth implements Parcelable {
    public static final Parcelable.Creator<FeedbackContextMicrosoftHealth> CREATOR = new Parcelable.Creator<FeedbackContextMicrosoftHealth>() { // from class: com.microsoft.kapp.logging.models.FeedbackContextMicrosoftHealth.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContextMicrosoftHealth createFromParcel(Parcel in) {
            return new FeedbackContextMicrosoftHealth(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContextMicrosoftHealth[] newArray(int size) {
            return new FeedbackContextMicrosoftHealth[size];
        }
    };
    @SerializedName("cloud")
    FeedbackContextMicrosoftHealthCloud mCloud;
    @SerializedName("pairedDevices")
    FeedbackContextMicrosoftHealthDevice[] mDevices;

    public FeedbackContextMicrosoftHealth(CargoUserProfile profile, CargoConnection cargoConnection, CredentialsManager credentialsManager, LogConfiguration logConfiguration, SettingsProvider settingsProvider) {
        this.mCloud = new FeedbackContextMicrosoftHealthCloud(credentialsManager, logConfiguration);
        if (profile != null) {
            ArrayList<FeedbackContextMicrosoftHealthDevice> devices = new ArrayList<>();
            if (settingsProvider.isSensorLoggingEnabled()) {
                devices.add(new FeedbackContextMicrosoftHealthDevice());
            }
            int numberOfDevices = profile.getDevices().size();
            UUID deviceId = profile.getDeviceId();
            if (deviceId != null) {
                String deviceIdString = deviceId.toString();
                for (int i = 0; i < numberOfDevices; i++) {
                    DeviceSettings device = profile.getDevices().get(i);
                    if (deviceIdString.equalsIgnoreCase(device.getDeviceId())) {
                        devices.add(new FeedbackContextMicrosoftHealthDevice(device, cargoConnection, logConfiguration));
                    }
                }
            }
            if (devices.size() > 0) {
                this.mDevices = (FeedbackContextMicrosoftHealthDevice[]) devices.toArray(new FeedbackContextMicrosoftHealthDevice[devices.size()]);
            }
        }
    }

    public FeedbackContextMicrosoftHealth(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mCloud, 0);
        dest.writeParcelableArray(this.mDevices, 0);
    }

    public void readFromParcel(Parcel in) {
        this.mCloud = (FeedbackContextMicrosoftHealthCloud) in.readParcelable(FeedbackContextMicrosoftHealthCloud.class.getClassLoader());
        this.mDevices = (FeedbackContextMicrosoftHealthDevice[]) in.readParcelableArray(FeedbackContextMicrosoftHealthDevice.class.getClassLoader());
    }
}

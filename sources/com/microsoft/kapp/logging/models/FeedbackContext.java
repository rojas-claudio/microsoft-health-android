package com.microsoft.kapp.logging.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.logging.LogConfiguration;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
/* loaded from: classes.dex */
public class FeedbackContext implements Parcelable {
    public static final Parcelable.Creator<FeedbackContext> CREATOR = new Parcelable.Creator<FeedbackContext>() { // from class: com.microsoft.kapp.logging.models.FeedbackContext.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContext createFromParcel(Parcel in) {
            return new FeedbackContext(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContext[] newArray(int size) {
            return new FeedbackContext[size];
        }
    };
    @SerializedName("application")
    FeedbackContextApplication mFeedbackContextApplication;
    @SerializedName("currentContext")
    FeedbackContextCurrentContext mFeedbackContextCurrentContext;
    @SerializedName("device")
    FeedbackContextDevice mFeedbackContextDevice;
    @SerializedName("microsoftHealth")
    FeedbackContextMicrosoftHealth mFeedbackContextMicrosoftHealth;
    @SerializedName("operatingSystem")
    FeedbackContextSystem mFeedbackContextSystem;

    public FeedbackContext(Context context, String sender, CargoUserProfile profile, CargoConnection cargoConnection, CredentialsManager credentialsManager, LogConfiguration logConfiguration, SettingsProvider settingsProvider, boolean shouldIncludeLogs) {
        this.mFeedbackContextApplication = new FeedbackContextApplication(context);
        this.mFeedbackContextSystem = new FeedbackContextSystem();
        this.mFeedbackContextDevice = new FeedbackContextDevice(context);
        this.mFeedbackContextCurrentContext = new FeedbackContextCurrentContext(sender);
        this.mFeedbackContextMicrosoftHealth = new FeedbackContextMicrosoftHealth(profile, cargoConnection, credentialsManager, logConfiguration, settingsProvider);
    }

    public FeedbackContext(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mFeedbackContextApplication, 0);
        dest.writeParcelable(this.mFeedbackContextCurrentContext, 0);
        dest.writeParcelable(this.mFeedbackContextDevice, 0);
        dest.writeParcelable(this.mFeedbackContextMicrosoftHealth, 0);
        dest.writeParcelable(this.mFeedbackContextSystem, 0);
    }

    public void readFromParcel(Parcel in) {
        this.mFeedbackContextApplication = (FeedbackContextApplication) in.readParcelable(FeedbackContextApplication.class.getClassLoader());
        this.mFeedbackContextCurrentContext = (FeedbackContextCurrentContext) in.readParcelable(FeedbackContextCurrentContext.class.getClassLoader());
        this.mFeedbackContextDevice = (FeedbackContextDevice) in.readParcelable(FeedbackContextDevice.class.getClassLoader());
        this.mFeedbackContextMicrosoftHealth = (FeedbackContextMicrosoftHealth) in.readParcelable(FeedbackContextMicrosoftHealth.class.getClassLoader());
        this.mFeedbackContextSystem = (FeedbackContextSystem) in.readParcelable(FeedbackContextSystem.class.getClassLoader());
    }
}

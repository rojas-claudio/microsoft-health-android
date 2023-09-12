package com.microsoft.kapp.logging.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.internal.ServerProtocol;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public class FeedbackContextSystem implements Parcelable {
    public static final Parcelable.Creator<FeedbackContextSystem> CREATOR = new Parcelable.Creator<FeedbackContextSystem>() { // from class: com.microsoft.kapp.logging.models.FeedbackContextSystem.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContextSystem createFromParcel(Parcel in) {
            return new FeedbackContextSystem(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContextSystem[] newArray(int size) {
            return new FeedbackContextSystem[size];
        }
    };
    @SerializedName("androidVersions")
    FeedbackContextSystemAndroidVersions mAndroidVersions;
    @SerializedName(WorkoutSummary.NAME)
    String mName;
    @SerializedName(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION)
    String mVersion;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FeedbackContextSystem() {
        this.mName = Constants.ANDROID_PHONE_IDENTIFIER;
        this.mVersion = String.valueOf(Build.VERSION.RELEASE);
        this.mAndroidVersions = new FeedbackContextSystemAndroidVersions();
    }

    public FeedbackContextSystem(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mVersion);
        dest.writeParcelable(this.mAndroidVersions, 0);
    }

    public void readFromParcel(Parcel in) {
        this.mName = in.readString();
        this.mVersion = in.readString();
        this.mAndroidVersions = (FeedbackContextSystemAndroidVersions) in.readParcelable(FeedbackContextSystemAndroidVersions.class.getClassLoader());
    }
}

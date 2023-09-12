package com.microsoft.kapp.logging.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class FeedbackContextSystemAndroidVersions implements Parcelable {
    public static final Parcelable.Creator<FeedbackContextSystemAndroidVersions> CREATOR = new Parcelable.Creator<FeedbackContextSystemAndroidVersions>() { // from class: com.microsoft.kapp.logging.models.FeedbackContextSystemAndroidVersions.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContextSystemAndroidVersions createFromParcel(Parcel in) {
            return new FeedbackContextSystemAndroidVersions(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContextSystemAndroidVersions[] newArray(int size) {
            return new FeedbackContextSystemAndroidVersions[size];
        }
    };
    @SerializedName("basebandVersion")
    String mBasebandVersion;
    @SerializedName("buildNumber")
    String mBuildNumber;
    @SerializedName("kernelVersion")
    String mKernelVersion;
    @SerializedName("systemVersion")
    String mSystemVersion;

    public FeedbackContextSystemAndroidVersions() {
        this.mBasebandVersion = Build.getRadioVersion();
        this.mKernelVersion = System.getProperty("os.version");
        this.mSystemVersion = Build.MODEL;
        this.mBuildNumber = Build.DISPLAY;
    }

    public FeedbackContextSystemAndroidVersions(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mBasebandVersion);
        dest.writeString(this.mBuildNumber);
        dest.writeString(this.mKernelVersion);
        dest.writeString(this.mSystemVersion);
    }

    public void readFromParcel(Parcel in) {
        this.mBasebandVersion = in.readString();
        this.mBuildNumber = in.readString();
        this.mKernelVersion = in.readString();
        this.mSystemVersion = in.readString();
    }
}

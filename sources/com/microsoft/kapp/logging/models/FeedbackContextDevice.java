package com.microsoft.kapp.logging.models;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import com.facebook.internal.ServerProtocol;
import com.google.gson.annotations.SerializedName;
import java.util.Locale;
/* loaded from: classes.dex */
public class FeedbackContextDevice implements Parcelable {
    public static final Parcelable.Creator<FeedbackContextDevice> CREATOR = new Parcelable.Creator<FeedbackContextDevice>() { // from class: com.microsoft.kapp.logging.models.FeedbackContextDevice.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContextDevice createFromParcel(Parcel in) {
            return new FeedbackContextDevice(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContextDevice[] newArray(int size) {
            return new FeedbackContextDevice[size];
        }
    };
    @SerializedName("language")
    String mLanguage;
    @SerializedName("manufacturer")
    String mManufacturer;
    @SerializedName("model")
    String mModel;
    @SerializedName("region")
    String mRegion;
    @SerializedName("screenResolution")
    FeedbackContextDeviceResolution mScreenResolution;
    @SerializedName("type")
    String mType;
    @SerializedName(ServerProtocol.FALLBACK_DIALOG_PARAM_VERSION)
    String mVersion;

    public FeedbackContextDevice(Context context) {
        this.mType = "Phone";
        this.mManufacturer = Build.MANUFACTURER;
        this.mModel = Build.MODEL;
        this.mVersion = Build.ID;
        this.mScreenResolution = new FeedbackContextDeviceResolution(context);
        this.mLanguage = Locale.getDefault().getLanguage();
        this.mRegion = Locale.getDefault().getCountry();
    }

    public FeedbackContextDevice(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mLanguage);
        dest.writeString(this.mManufacturer);
        dest.writeString(this.mModel);
        dest.writeString(this.mRegion);
        dest.writeParcelable(this.mScreenResolution, 0);
        dest.writeString(this.mType);
        dest.writeString(this.mVersion);
    }

    public void readFromParcel(Parcel in) {
        this.mLanguage = in.readString();
        this.mManufacturer = in.readString();
        this.mModel = in.readString();
        this.mRegion = in.readString();
        this.mScreenResolution = (FeedbackContextDeviceResolution) in.readParcelable(FeedbackContextDeviceResolution.class.getClassLoader());
        this.mType = in.readString();
        this.mType = in.readString();
    }
}

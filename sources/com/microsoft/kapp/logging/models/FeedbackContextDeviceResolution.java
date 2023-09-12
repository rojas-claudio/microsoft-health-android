package com.microsoft.kapp.logging.models;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class FeedbackContextDeviceResolution implements Parcelable {
    public static final Parcelable.Creator<FeedbackContextDeviceResolution> CREATOR = new Parcelable.Creator<FeedbackContextDeviceResolution>() { // from class: com.microsoft.kapp.logging.models.FeedbackContextDeviceResolution.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContextDeviceResolution createFromParcel(Parcel in) {
            return new FeedbackContextDeviceResolution(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeedbackContextDeviceResolution[] newArray(int size) {
            return new FeedbackContextDeviceResolution[size];
        }
    };
    @SerializedName("height")
    int mHeight;
    @SerializedName("width")
    int mWidth;

    public FeedbackContextDeviceResolution(Context context) {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        double screenWidthInPixels = config.screenWidthDp * dm.density;
        double screenHeightInPixels = (dm.heightPixels * screenWidthInPixels) / dm.widthPixels;
        this.mWidth = (int) screenWidthInPixels;
        this.mHeight = (int) screenHeightInPixels;
    }

    public FeedbackContextDeviceResolution(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mWidth);
        dest.writeInt(this.mHeight);
    }

    public void readFromParcel(Parcel in) {
        this.mWidth = in.readInt();
        this.mHeight = in.readInt();
    }
}

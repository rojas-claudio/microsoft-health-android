package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class FeatureConfiguration implements Parcelable {
    public static final Parcelable.Creator<FeatureConfiguration> CREATOR = new Parcelable.Creator<FeatureConfiguration>() { // from class: com.microsoft.krestsdk.models.FeatureConfiguration.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeatureConfiguration createFromParcel(Parcel in) {
            return new FeatureConfiguration(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeatureConfiguration[] newArray(int size) {
            return new FeatureConfiguration[size];
        }
    };
    @SerializedName("enabled")
    private boolean mEnabled;

    public boolean IsEnabled() {
        return this.mEnabled;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (this.mEnabled ? 1 : 0));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public FeatureConfiguration(Parcel in) {
        this.mEnabled = in.readByte() != 0;
    }
}

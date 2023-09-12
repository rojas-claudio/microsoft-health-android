package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class FeaturesConfiguration implements Parcelable {
    public static final Parcelable.Creator<FeaturesConfiguration> CREATOR = new Parcelable.Creator<FeaturesConfiguration>() { // from class: com.microsoft.krestsdk.models.FeaturesConfiguration.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeaturesConfiguration createFromParcel(Parcel in) {
            return new FeaturesConfiguration(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FeaturesConfiguration[] newArray(int size) {
            return new FeaturesConfiguration[size];
        }
    };
    @SerializedName("facebook")
    private FeatureConfiguration mFacebookFeatureConfiguration;
    @SerializedName("facebookMessenger")
    private FeatureConfiguration mFacebookMessengerFeatureConfiguration;
    @SerializedName("finance")
    private FinanceFeatureConfiguration mFinanceFeatureConfiguration;
    @SerializedName("starbucks")
    private StarbucksFeatureConfiguration mStarbucksFeatureConfiguration;
    @SerializedName("twitter")
    private FeatureConfiguration mTwitterFeatureConfiguration;

    public StarbucksFeatureConfiguration getStarbucksFeatureConfiguration() {
        return this.mStarbucksFeatureConfiguration;
    }

    public FeatureConfiguration getFacebookFeatureConfiguration() {
        return this.mFacebookFeatureConfiguration;
    }

    public FeatureConfiguration getFacebookMessengerFeatureConfiguration() {
        return this.mFacebookMessengerFeatureConfiguration;
    }

    public FeatureConfiguration getTwitterFeatureConfiguration() {
        return this.mTwitterFeatureConfiguration;
    }

    public FinanceFeatureConfiguration getFinanceFeatureConfiguration() {
        return this.mFinanceFeatureConfiguration;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mStarbucksFeatureConfiguration);
        dest.writeValue(this.mFacebookFeatureConfiguration);
        dest.writeValue(this.mFacebookMessengerFeatureConfiguration);
        dest.writeValue(this.mTwitterFeatureConfiguration);
        dest.writeValue(this.mFinanceFeatureConfiguration);
    }

    protected FeaturesConfiguration(Parcel in) {
        this.mStarbucksFeatureConfiguration = (StarbucksFeatureConfiguration) in.readValue(StarbucksFeatureConfiguration.class.getClassLoader());
        this.mFacebookFeatureConfiguration = (FeatureConfiguration) in.readValue(FeatureConfiguration.class.getClassLoader());
        this.mFacebookMessengerFeatureConfiguration = (FeatureConfiguration) in.readValue(FeatureConfiguration.class.getClassLoader());
        this.mTwitterFeatureConfiguration = (FeatureConfiguration) in.readValue(FeatureConfiguration.class.getClassLoader());
        this.mFinanceFeatureConfiguration = (FinanceFeatureConfiguration) in.readValue(FeatureConfiguration.class.getClassLoader());
    }
}

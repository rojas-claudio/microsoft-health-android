package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class StarbucksFeatureConfiguration extends FeatureConfiguration {
    public static final Parcelable.Creator<StarbucksFeatureConfiguration> CREATOR = new Parcelable.Creator<StarbucksFeatureConfiguration>() { // from class: com.microsoft.krestsdk.models.StarbucksFeatureConfiguration.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StarbucksFeatureConfiguration createFromParcel(Parcel in) {
            return new StarbucksFeatureConfiguration(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StarbucksFeatureConfiguration[] newArray(int size) {
            return new StarbucksFeatureConfiguration[size];
        }
    };
    @SerializedName("cardBackUrl")
    private String mCardBackUrl;
    @SerializedName("cardFrontUrl")
    private String mCardFrontUrl;
    @SerializedName("displayUrl")
    private String mDisplayUrl;
    @SerializedName("displayUrlString")
    private String mDisplayUrlString;

    @Override // com.microsoft.krestsdk.models.FeatureConfiguration, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.microsoft.krestsdk.models.FeatureConfiguration, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(getDisplayUrl());
        dest.writeString(getDisplayUrlString());
        dest.writeString(getCardFrontUrl());
        dest.writeString(getCardBackUrl());
    }

    protected StarbucksFeatureConfiguration(Parcel in) {
        super(in);
        setDisplayUrl(in.readString());
        setDisplayUrlString(in.readString());
        setCardFrontUrl(in.readString());
        setCardBackUrl(in.readString());
    }

    public String getDisplayUrl() {
        return this.mDisplayUrl;
    }

    public void setDisplayUrl(String mDisplayUrl) {
        this.mDisplayUrl = mDisplayUrl;
    }

    public String getDisplayUrlString() {
        return this.mDisplayUrlString;
    }

    public void setDisplayUrlString(String mDisplayUrlString) {
        this.mDisplayUrlString = mDisplayUrlString;
    }

    public String getCardFrontUrl() {
        return this.mCardFrontUrl;
    }

    public void setCardFrontUrl(String mCardFrontUrl) {
        this.mCardFrontUrl = mCardFrontUrl;
    }

    public String getCardBackUrl() {
        return this.mCardBackUrl;
    }

    public void setCardBackUrl(String mCardBackUrl) {
        this.mCardBackUrl = mCardBackUrl;
    }
}

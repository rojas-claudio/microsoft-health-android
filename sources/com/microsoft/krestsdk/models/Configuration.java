package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class Configuration implements Parcelable {
    public static final Parcelable.Creator<Configuration> CREATOR = new Parcelable.Creator<Configuration>() { // from class: com.microsoft.krestsdk.models.Configuration.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Configuration createFromParcel(Parcel in) {
            return new Configuration(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Configuration[] newArray(int size) {
            return new Configuration[size];
        }
    };
    @SerializedName("features")
    private FeaturesConfiguration mFeaturesConfiguration;
    @SerializedName("maps")
    private MapsConfiguration mMaps;
    @SerializedName("oobe")
    private OOBEConfiguration mOOBE;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public OOBEConfiguration getOOBE() {
        return this.mOOBE;
    }

    public void setOOBE(OOBEConfiguration mOOBE) {
        this.mOOBE = mOOBE;
    }

    public MapsConfiguration getMaps() {
        return this.mMaps;
    }

    public void setMaps(MapsConfiguration mMaps) {
        this.mMaps = mMaps;
    }

    public FeaturesConfiguration getFeaturesConfiguration() {
        return this.mFeaturesConfiguration;
    }

    public void setFeaturesConfiguration(FeaturesConfiguration mFeaturesConfiguration) {
        this.mFeaturesConfiguration = mFeaturesConfiguration;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(getOOBE());
        dest.writeValue(getMaps());
        dest.writeValue(getFeaturesConfiguration());
    }

    protected Configuration(Parcel in) {
        setOOBE((OOBEConfiguration) in.readValue(OOBEConfiguration.class.getClassLoader()));
        setMaps((MapsConfiguration) in.readValue(MapsConfiguration.class.getClassLoader()));
        setFeaturesConfiguration((FeaturesConfiguration) in.readValue(FeaturesConfiguration.class.getClassLoader()));
    }
}

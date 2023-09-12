package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class OOBEDefaults implements Parcelable {
    public static final Parcelable.Creator<OOBEDefaults> CREATOR = new Parcelable.Creator<OOBEDefaults>() { // from class: com.microsoft.krestsdk.models.OOBEDefaults.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OOBEDefaults createFromParcel(Parcel in) {
            return new OOBEDefaults(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OOBEDefaults[] newArray(int size) {
            return new OOBEDefaults[size];
        }
    };
    @SerializedName("distanceUnit")
    private UnitType mDistanceUnit;
    @SerializedName("marketingOptIn")
    private boolean mMarketingOptIn;
    @SerializedName("temperatureUnit")
    private TemperatureType mTemperatureUnit;
    @SerializedName("weightUnit")
    private UnitType mWeightUnit;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public UnitType getDistanceUnit() {
        return this.mDistanceUnit;
    }

    public void setDistanceUnit(UnitType mDistanceUnit) {
        this.mDistanceUnit = mDistanceUnit;
    }

    public UnitType getWeightUnit() {
        return this.mWeightUnit;
    }

    public void setWeightUnit(UnitType mWeightUnit) {
        this.mWeightUnit = mWeightUnit;
    }

    public TemperatureType getTemperatureUnit() {
        return this.mTemperatureUnit;
    }

    public void setTemperatureUnit(TemperatureType mTemperatureUnit) {
        this.mTemperatureUnit = mTemperatureUnit;
    }

    public boolean isMarketingOptIn() {
        return this.mMarketingOptIn;
    }

    public void setMarketingOptIn(boolean mMarketingOptIn) {
        this.mMarketingOptIn = mMarketingOptIn;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(getDistanceUnit());
        dest.writeSerializable(getWeightUnit());
        dest.writeSerializable(getTemperatureUnit());
        dest.writeByte((byte) (isMarketingOptIn() ? 1 : 0));
    }

    protected OOBEDefaults(Parcel in) {
        setDistanceUnit((UnitType) in.readSerializable());
        setWeightUnit((UnitType) in.readSerializable());
        setTemperatureUnit((TemperatureType) in.readSerializable());
        setMarketingOptIn(in.readByte() != 0);
    }
}

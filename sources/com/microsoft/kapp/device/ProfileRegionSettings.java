package com.microsoft.kapp.device;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.band.client.CargoDateFormat;
import com.microsoft.band.client.CargoLocation;
import com.microsoft.band.client.CargoTimeFormat;
import com.microsoft.band.client.UnitType;
/* loaded from: classes.dex */
public class ProfileRegionSettings implements Cloneable, Parcelable {
    public static final Parcelable.Creator<ProfileRegionSettings> CREATOR = new Parcelable.Creator<ProfileRegionSettings>() { // from class: com.microsoft.kapp.device.ProfileRegionSettings.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ProfileRegionSettings createFromParcel(Parcel in) {
            return new ProfileRegionSettings(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ProfileRegionSettings[] newArray(int size) {
            return new ProfileRegionSettings[size];
        }
    };
    private static final long serialVersionUID = 0;
    private CargoLocation mCargoLocation;
    @SerializedName("country")
    String mCountry;
    @SerializedName("dateSeparator")
    char mDateSeparator;
    @SerializedName("decimalSeparator")
    char mDecimalSeparator;
    @SerializedName("displayCaloriesUnit")
    UnitType mDisplayCaloriesUnit;
    @SerializedName("displayDate")
    CargoDateFormat mDisplayDate;
    @SerializedName("displaySizeUnit")
    UnitType mDisplaySizeUnit;
    @SerializedName("displayTime")
    CargoTimeFormat mDisplayTime;
    @SerializedName("displayVolumeUnit")
    UnitType mDisplayVolumeUnit;
    @SerializedName("localeName")
    String mLocaleName;
    @SerializedName("numberSeparator")
    char mNumberSeparator;

    public ProfileRegionSettings() {
    }

    public String getLocaleName() {
        return this.mLocaleName;
    }

    public char getDateSeparator() {
        return this.mDateSeparator;
    }

    public char getNumberSeparator() {
        return this.mNumberSeparator;
    }

    public char getDecimalSeparator() {
        return this.mDecimalSeparator;
    }

    public CargoTimeFormat getDisplayTimeFormat() {
        return this.mDisplayTime;
    }

    public CargoDateFormat getDisplayDateFormat() {
        return this.mDisplayDate;
    }

    public UnitType getDisplaySizeUnit() {
        return this.mDisplaySizeUnit;
    }

    public UnitType getDisplayCaloriesUnit() {
        return this.mDisplayCaloriesUnit;
    }

    public UnitType getDisplayVolumeUnit() {
        return this.mDisplayVolumeUnit;
    }

    public CargoLocation getCargoLocation() {
        return this.mCargoLocation;
    }

    public void setCargoLocation(CargoLocation cargoLocation) {
        this.mCargoLocation = cargoLocation;
    }

    protected ProfileRegionSettings(Parcel in) {
        this.mCountry = in.readString();
        this.mLocaleName = in.readString();
        this.mDateSeparator = in.readString().charAt(0);
        this.mNumberSeparator = in.readString().charAt(0);
        this.mDecimalSeparator = in.readString().charAt(0);
        this.mDisplayTime = CargoTimeFormat.valueOf(in.readString());
        this.mDisplayDate = CargoDateFormat.valueOf(in.readString());
        this.mDisplaySizeUnit = UnitType.valueOf(in.readString());
        this.mDisplayVolumeUnit = UnitType.valueOf(in.readString());
        this.mDisplayCaloriesUnit = UnitType.valueOf(in.readString());
        this.mCargoLocation = CargoLocation.valueOf(in.readString());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mCountry);
        dest.writeString(this.mLocaleName);
        dest.writeString(String.valueOf(this.mDateSeparator));
        dest.writeString(String.valueOf(this.mNumberSeparator));
        dest.writeString(String.valueOf(this.mDecimalSeparator));
        dest.writeString(this.mDisplayTime.toString());
        dest.writeString(this.mDisplayDate.toString());
        dest.writeString(this.mDisplaySizeUnit.toString());
        dest.writeString(this.mDisplayVolumeUnit.toString());
        dest.writeString(this.mDisplayCaloriesUnit.toString());
        dest.writeString(this.mCargoLocation.toString());
    }
}

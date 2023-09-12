package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class Location implements Parcelable {
    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() { // from class: com.microsoft.krestsdk.models.Location.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
    private static final double LOG_LAT_UNITS_PER_DEGREE = 1.0E7d;
    @SerializedName("AltitudeFromMSL")
    private int mAltitudeFromMSL;
    @SerializedName("EHPE")
    private int mEHPE;
    @SerializedName("EVPE")
    private int mEVPE;
    @SerializedName("Latitude")
    private int mLatitude;
    @SerializedName("Longitude")
    private int mLongitude;
    @SerializedName("SpeedOverGround")
    private int mSpeedOverGround;

    public Location() {
    }

    public int getAltitudeFromMSL() {
        return this.mAltitudeFromMSL;
    }

    public int getEHPE() {
        return this.mEHPE;
    }

    public int getEVPE() {
        return this.mEVPE;
    }

    public int getLatitude() {
        return this.mLatitude;
    }

    public int getLongitude() {
        return this.mLongitude;
    }

    public double getLatitudeInDegrees() {
        return this.mLatitude / LOG_LAT_UNITS_PER_DEGREE;
    }

    public double getLongitudeInDegrees() {
        return this.mLongitude / LOG_LAT_UNITS_PER_DEGREE;
    }

    public int getSpeedOverGround() {
        return this.mSpeedOverGround;
    }

    public void setAltitudeFromMSL(int mAltitudeFromMSL) {
        this.mAltitudeFromMSL = mAltitudeFromMSL;
    }

    public void setEHPE(int mEHPE) {
        this.mEHPE = mEHPE;
    }

    public void setEVPE(int mEVPE) {
        this.mEVPE = mEVPE;
    }

    public void setLatitude(int mLatitude) {
        this.mLatitude = mLatitude;
    }

    public void setLongitude(int mLongitude) {
        this.mLongitude = mLongitude;
    }

    public void setSpeedOverGround(int mSpeedOverGround) {
        this.mSpeedOverGround = mSpeedOverGround;
    }

    protected Location(Parcel in) {
        this.mAltitudeFromMSL = in.readInt();
        this.mEHPE = in.readInt();
        this.mEVPE = in.readInt();
        this.mLatitude = in.readInt();
        this.mLongitude = in.readInt();
        this.mSpeedOverGround = in.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mAltitudeFromMSL);
        dest.writeInt(this.mEHPE);
        dest.writeInt(this.mEVPE);
        dest.writeInt(this.mLatitude);
        dest.writeInt(this.mLongitude);
        dest.writeInt(this.mSpeedOverGround);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}

package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.StringUtils;
/* loaded from: classes.dex */
public class GolfCourse implements Parcelable, Comparable<GolfCourse> {
    public static final Parcelable.Creator<GolfCourse> CREATOR = new Parcelable.Creator<GolfCourse>() { // from class: com.microsoft.krestsdk.models.GolfCourse.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GolfCourse createFromParcel(Parcel in) {
            return new GolfCourse(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public GolfCourse[] newArray(int size) {
            return new GolfCourse[size];
        }
    };
    @SerializedName("city")
    private String mCity;
    @SerializedName("country")
    private String mCountry;
    @SerializedName("courseId")
    private String mCourseId;
    @SerializedName("courseType")
    private GolfCourseType mCourseType;
    @SerializedName("displayAddress")
    private String mDisplayAddress;
    @SerializedName("displayAddress2")
    private String mDisplayAddress2;
    @SerializedName("displayAddress3")
    private String mDisplayAddress3;
    @SerializedName("distance")
    private double mDistance;
    @SerializedName("holes")
    private GolfHole[] mHoles;
    @SerializedName("latitude")
    private double mLatitude;
    @SerializedName("longitude")
    private double mLongitude;
    @SerializedName(WorkoutSummary.NAME)
    private String mName;
    @SerializedName("numberOfHoles")
    private int mNumberOfHoles;
    @SerializedName("phoneNumber")
    private String mPhoneNumber;
    @SerializedName("regionId")
    private String mRegionId;
    @SerializedName("state")
    private String mState;
    @SerializedName("street")
    private String mStreet;
    @SerializedName("tees")
    private GolfTee[] mTees;
    @SerializedName("website")
    private String mWebsite;
    @SerializedName("zipcode")
    private String mZipcode;

    public GolfCourse() {
    }

    public String getCourseId() {
        return this.mCourseId;
    }

    public void setCourseId(String mCourseId) {
        this.mCourseId = mCourseId;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getStreet() {
        return this.mStreet;
    }

    public void setStreet(String mStreet) {
        this.mStreet = mStreet;
    }

    public String getCity() {
        return this.mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }

    public String getState() {
        return this.mState;
    }

    public void setState(String mState) {
        this.mState = mState;
    }

    public String getZipcode() {
        return this.mZipcode;
    }

    public void setZipcode(String mZipcode) {
        this.mZipcode = mZipcode;
    }

    public String getCountry() {
        return this.mCountry;
    }

    public void setCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getDisplayAddress() {
        return this.mDisplayAddress;
    }

    public void setDisplayAddress(String mDisplayAddress) {
        this.mDisplayAddress = mDisplayAddress;
    }

    public String getDisplayAddress2() {
        return this.mDisplayAddress2;
    }

    public void setDisplayAddress2(String mDisplayAddress2) {
        this.mDisplayAddress2 = mDisplayAddress2;
    }

    public String getDisplayAddress3() {
        return this.mDisplayAddress3;
    }

    public void setDisplayAddress3(String mDisplayAddress3) {
        this.mDisplayAddress3 = mDisplayAddress3;
    }

    public String getPhoneNumber() {
        return this.mPhoneNumber;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public GolfCourseType getCourseType() {
        return this.mCourseType;
    }

    public void setCourseType(GolfCourseType mCourseType) {
        this.mCourseType = mCourseType;
    }

    public double getLatitude() {
        return this.mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return this.mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public int getNumberOfHoles() {
        return this.mNumberOfHoles;
    }

    public void setNumberOfHoles(int mNumberOfHoles) {
        this.mNumberOfHoles = mNumberOfHoles;
    }

    public double getDistance() {
        return this.mDistance;
    }

    public void setDistance(double mDistance) {
        this.mDistance = mDistance;
    }

    public String getWebsite() {
        return this.mWebsite;
    }

    public void setWebsite(String website) {
        this.mWebsite = website;
    }

    public GolfTee[] getTees() {
        return this.mTees;
    }

    public void setTees(GolfTee[] tees) {
        this.mTees = tees;
    }

    public double getDifficultTeeRating() {
        GolfTee defaultTee = getDefaultTee();
        if (defaultTee == null) {
            return Constants.SPLITS_ACCURACY;
        }
        double teeRating = defaultTee.getRating();
        return teeRating;
    }

    public double getDifficultSlope() {
        GolfTee defaultTee = getDefaultTee();
        if (defaultTee == null) {
            return Constants.SPLITS_ACCURACY;
        }
        double slope = defaultTee.getSlope();
        return slope;
    }

    public GolfTee getDefaultTee() {
        GolfTee defaultTee = null;
        if (this.mTees != null && this.mTees.length > 0) {
            defaultTee = this.mTees[0];
            GolfTee[] arr$ = this.mTees;
            for (GolfTee tee : arr$) {
                if (tee.isDefault()) {
                    return tee;
                }
            }
        }
        return defaultTee;
    }

    public String getConcatenatedAddress() {
        return StringUtils.joinWhenItemNotEmpty(System.getProperty("line.separator"), getDisplayAddress(), getDisplayAddress2(), getDisplayAddress3());
    }

    protected GolfCourse(Parcel in) {
        this.mCourseId = in.readString();
        this.mName = in.readString();
        this.mStreet = in.readString();
        this.mCity = in.readString();
        this.mState = in.readString();
        this.mZipcode = in.readString();
        this.mCountry = in.readString();
        this.mDisplayAddress = in.readString();
        this.mDisplayAddress2 = in.readString();
        this.mDisplayAddress3 = in.readString();
        this.mPhoneNumber = in.readString();
        this.mCourseType = (GolfCourseType) in.readValue(GolfCourseType.class.getClassLoader());
        this.mLatitude = in.readDouble();
        this.mLongitude = in.readDouble();
        this.mNumberOfHoles = in.readInt();
        this.mDistance = in.readDouble();
        this.mWebsite = in.readString();
        this.mTees = (GolfTee[]) in.createTypedArray(GolfTee.CREATOR);
        this.mHoles = (GolfHole[]) in.createTypedArray(GolfHole.CREATOR);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mCourseId);
        dest.writeString(this.mName);
        dest.writeString(this.mStreet);
        dest.writeString(this.mCity);
        dest.writeString(this.mState);
        dest.writeString(this.mZipcode);
        dest.writeString(this.mCountry);
        dest.writeString(this.mDisplayAddress);
        dest.writeString(this.mDisplayAddress2);
        dest.writeString(this.mDisplayAddress3);
        dest.writeString(this.mPhoneNumber);
        dest.writeValue(this.mCourseType);
        dest.writeDouble(this.mLatitude);
        dest.writeDouble(this.mLongitude);
        dest.writeInt(this.mNumberOfHoles);
        dest.writeDouble(this.mDistance);
        dest.writeString(this.mWebsite);
        dest.writeTypedArray(this.mTees, flags);
        dest.writeTypedArray(this.mHoles, flags);
    }

    @Override // java.lang.Comparable
    public int compareTo(GolfCourse other) {
        if (other != null) {
            return this.mName.compareTo(other.getName());
        }
        return -1;
    }
}

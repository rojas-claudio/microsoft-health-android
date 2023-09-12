package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class MeasuredEventMapPoint implements Parcelable {
    public static final Parcelable.Creator<MeasuredEventMapPoint> CREATOR = new Parcelable.Creator<MeasuredEventMapPoint>() { // from class: com.microsoft.krestsdk.models.MeasuredEventMapPoint.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MeasuredEventMapPoint createFromParcel(Parcel in) {
            return new MeasuredEventMapPoint(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MeasuredEventMapPoint[] newArray(int size) {
            return new MeasuredEventMapPoint[size];
        }
    };
    @SerializedName("HeartRate")
    private int mHeartRate;
    @SerializedName("IsPaused")
    private boolean mIsPause;
    @SerializedName("IsResume")
    private boolean mIsResume;
    @SerializedName("Location")
    private Location mLocation;
    @SerializedName("MapPointOrdinal")
    private int mMapPointOrdinal;
    @SerializedName("MapPointType")
    private String mMapPointType;
    @SerializedName("Pace")
    private int mPace;
    @SerializedName("ScaledPace")
    private int mScaledPace;
    @SerializedName("SecondsSinceStart")
    private int mSecondsSinceStart;
    @SerializedName("Speed")
    private double mSpeed;
    @SerializedName("SplitOrdinal")
    private int mSplitOrdinal;
    @SerializedName("TotalDistance")
    private int mTotalDistance;

    public boolean getIsResume() {
        return this.mIsResume;
    }

    public void setIsResume(boolean isResume) {
        this.mIsResume = isResume;
    }

    public boolean getIsPause() {
        return this.mIsPause;
    }

    public void setIsPause(boolean isPause) {
        this.mIsPause = isPause;
    }

    public MeasuredEventMapPoint() {
    }

    public int getSecondsSinceStart() {
        return this.mSecondsSinceStart;
    }

    public int getMapPointOrdinal() {
        return this.mMapPointOrdinal;
    }

    public int getTotalDistance() {
        return this.mTotalDistance;
    }

    public int getPace() {
        return this.mPace;
    }

    public int getScaledPace() {
        return this.mScaledPace;
    }

    public double getSpeed() {
        return this.mSpeed;
    }

    public Location getLocation() {
        return this.mLocation;
    }

    public void setSecondsSinceStart(int mSecondsSinceStart) {
        this.mSecondsSinceStart = mSecondsSinceStart;
    }

    public void setMapPointOrdinal(int mMapPointOrdinal) {
        this.mMapPointOrdinal = mMapPointOrdinal;
    }

    public void setTotalDistance(int mTotalDistance) {
        this.mTotalDistance = mTotalDistance;
    }

    public void setPace(int mPace) {
        this.mPace = mPace;
    }

    public void setScaledPace(int mScaledPace) {
        this.mScaledPace = mScaledPace;
    }

    public void setSpeed(double speed) {
        this.mSpeed = speed;
    }

    public void setLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    public int getheartRate() {
        return this.mHeartRate;
    }

    public void setheartRate(int heartRate) {
        this.mHeartRate = heartRate;
    }

    public void setSplitOrdinal(int ordinal) {
        this.mSplitOrdinal = ordinal;
    }

    public int getSplitOrdinal() {
        return this.mSplitOrdinal;
    }

    public String getMapPointType() {
        return this.mMapPointType;
    }

    public void setMapPointType(String mapPointType) {
        this.mMapPointType = mapPointType;
    }

    protected MeasuredEventMapPoint(Parcel in) {
        this.mSecondsSinceStart = in.readInt();
        this.mMapPointOrdinal = in.readInt();
        this.mTotalDistance = in.readInt();
        this.mPace = in.readInt();
        this.mScaledPace = in.readInt();
        this.mLocation = (Location) in.readParcelable(Location.class.getClassLoader());
        this.mIsPause = in.readByte() != 0;
        this.mIsResume = in.readByte() != 0;
        this.mHeartRate = in.readInt();
        this.mSplitOrdinal = in.readInt();
        this.mMapPointType = in.readString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mSecondsSinceStart);
        dest.writeInt(this.mMapPointOrdinal);
        dest.writeInt(this.mTotalDistance);
        dest.writeInt(this.mPace);
        dest.writeInt(this.mScaledPace);
        dest.writeParcelable(this.mLocation, flags);
        dest.writeByte((byte) (this.mIsPause ? 1 : 0));
        dest.writeByte((byte) (this.mIsResume ? 1 : 0));
        dest.writeInt(this.mHeartRate);
        dest.writeInt(this.mSplitOrdinal);
        dest.writeString(this.mMapPointType);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}

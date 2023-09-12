package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class UserDailySummary implements Serializable, Parcelable {
    public static final Parcelable.Creator<UserDailySummary> CREATOR = new Parcelable.Creator<UserDailySummary>() { // from class: com.microsoft.krestsdk.models.UserDailySummary.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserDailySummary createFromParcel(Parcel in) {
            return new UserDailySummary(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserDailySummary[] newArray(int size) {
            return new UserDailySummary[size];
        }
    };
    private static final long serialVersionUID = 7558060200585304090L;
    @SerializedName("AverageHeartRate")
    private int mAverageHeartRate;
    @SerializedName("CaloriesBurned")
    private int mCaloriesBurned;
    @SerializedName("DailyHeartRateZones")
    private HRZones mDailyHeartRateZones;
    @SerializedName("FloorsClimbed")
    private int mFloorsClimbed;
    @SerializedName("Location")
    private String mLocation;
    @SerializedName("LowestHeartRate")
    private int mLowestHeartRate;
    @SerializedName("NumberOfActiveHours")
    private int mNumberOfActiveHours;
    @SerializedName("PeakHeartRate")
    private int mPeakHeartRate;
    @SerializedName("StepsTaken")
    private int mStepsTaken;
    @SerializedName("TimeOfDay")
    private DateTime mTimeOfDay;
    @SerializedName("TotalDistance")
    private int mTotalDistance;
    @SerializedName("TotalDistanceOnFoot")
    private int mTotalDistanceOnFoot;
    @SerializedName("UvExposure")
    private int mUvExposure;

    public UserDailySummary() {
    }

    public DateTime getTimeOfDay() {
        return this.mTimeOfDay;
    }

    public void setTimeOfDay(DateTime time) {
        this.mTimeOfDay = time;
    }

    public int getUvExposure() {
        return this.mUvExposure;
    }

    public void setUvExposure(int uvExposure) {
        this.mUvExposure = uvExposure;
    }

    public String getLocation() {
        return this.mLocation;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public int getCaloriesBurned() {
        return this.mCaloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.mCaloriesBurned = caloriesBurned;
    }

    public int getStepsTaken() {
        return this.mStepsTaken;
    }

    public void setStepsTaken(int stepsTaken) {
        this.mStepsTaken = stepsTaken;
    }

    public int getPeakHeartRate() {
        return this.mPeakHeartRate;
    }

    public void setPeakHeartRate(int peakHeartRate) {
        this.mPeakHeartRate = peakHeartRate;
    }

    public int getLowestHeartRate() {
        return this.mLowestHeartRate;
    }

    public void setLowestHeartRate(int lowestHeartRate) {
        this.mLowestHeartRate = lowestHeartRate;
    }

    public int getAverageHeartRate() {
        return this.mAverageHeartRate;
    }

    public void setAverageHeartRate(int averageHeartRate) {
        this.mAverageHeartRate = averageHeartRate;
    }

    public HRZones getDailyHRZones() {
        return this.mDailyHeartRateZones;
    }

    public void setDailyHRZones(HRZones dailyHRZones) {
        this.mDailyHeartRateZones = dailyHRZones;
    }

    public int getTotalDistance() {
        return this.mTotalDistance;
    }

    public void setTotalDistance(int totalDistance) {
        this.mTotalDistance = totalDistance;
    }

    public int getTotalDistanceOnFoot() {
        return this.mTotalDistanceOnFoot;
    }

    public int getFloorsClimbed() {
        return this.mFloorsClimbed;
    }

    public void setTotalDistanceOnFoot(int totalDistanceOnFoot) {
        this.mTotalDistanceOnFoot = totalDistanceOnFoot;
    }

    public int getNumberOfActiveHours() {
        return this.mNumberOfActiveHours;
    }

    public void setNumberOfActiveHours(int numberOfActiveHours) {
        this.mNumberOfActiveHours = numberOfActiveHours;
    }

    protected UserDailySummary(Parcel in) {
        this.mTimeOfDay = new DateTime(in.readLong());
        this.mStepsTaken = in.readInt();
        this.mCaloriesBurned = in.readInt();
        this.mUvExposure = in.readInt();
        this.mLocation = in.readString();
        this.mPeakHeartRate = in.readInt();
        this.mLowestHeartRate = in.readInt();
        this.mAverageHeartRate = in.readInt();
        this.mDailyHeartRateZones = (HRZones) in.readParcelable(HRZones.class.getClassLoader());
        this.mTotalDistance = in.readInt();
        this.mFloorsClimbed = in.readInt();
        this.mTotalDistanceOnFoot = in.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mTimeOfDay.getMillis());
        dest.writeInt(this.mStepsTaken);
        dest.writeInt(this.mCaloriesBurned);
        dest.writeInt(this.mUvExposure);
        dest.writeString(this.mLocation);
        dest.writeInt(this.mPeakHeartRate);
        dest.writeInt(this.mLowestHeartRate);
        dest.writeInt(this.mAverageHeartRate);
        dest.writeParcelable(this.mDailyHeartRateZones, flags);
        dest.writeInt(this.mTotalDistance);
        dest.writeInt(this.mFloorsClimbed);
        dest.writeInt(this.mTotalDistanceOnFoot);
    }
}

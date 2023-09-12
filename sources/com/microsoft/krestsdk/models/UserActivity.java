package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class UserActivity implements Serializable, Parcelable {
    public static final Parcelable.Creator<UserActivity> CREATOR = new Parcelable.Creator<UserActivity>() { // from class: com.microsoft.krestsdk.models.UserActivity.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserActivity createFromParcel(Parcel in) {
            return new UserActivity(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserActivity[] newArray(int size) {
            return new UserActivity[size];
        }
    };
    private static final long serialVersionUID = 7558060200585304090L;
    @SerializedName("AverageHeartRate")
    private int mAverageHeartRate;
    @SerializedName("CaloriesBurned")
    private int mCaloriesBurned;
    @SerializedName("FloorsClimbed")
    private int mFloorsClimbed;
    private boolean mIsPaused;
    @SerializedName("Location")
    private String mLocation;
    @SerializedName("LowestHeartRate")
    private int mLowestHeartRate;
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

    public UserActivity() {
    }

    public DateTime getTimeOfDay() {
        return this.mTimeOfDay;
    }

    public void setTimeOfDay(DateTime timeOfDay) {
        this.mTimeOfDay = timeOfDay;
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

    public boolean isPaused() {
        return this.mIsPaused;
    }

    public void setPaused(boolean isPaused) {
        this.mIsPaused = isPaused;
    }

    protected UserActivity(Parcel in) {
        this.mTimeOfDay = new DateTime(in.readLong());
        this.mStepsTaken = in.readInt();
        this.mCaloriesBurned = in.readInt();
        this.mUvExposure = in.readInt();
        this.mLocation = in.readString();
        this.mPeakHeartRate = in.readInt();
        this.mLowestHeartRate = in.readInt();
        this.mAverageHeartRate = in.readInt();
        this.mTotalDistance = in.readInt();
        this.mTotalDistanceOnFoot = in.readInt();
        this.mFloorsClimbed = in.readInt();
        this.mIsPaused = in.readInt() == 1;
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
        dest.writeInt(this.mTotalDistance);
        dest.writeInt(this.mTotalDistanceOnFoot);
        dest.writeInt(this.mFloorsClimbed);
        dest.writeInt(this.mIsPaused ? 1 : 0);
    }
}

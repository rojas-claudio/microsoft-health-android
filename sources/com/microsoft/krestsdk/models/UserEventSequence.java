package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.UUID;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class UserEventSequence implements Parcelable {
    public static final Parcelable.Creator<UserEventSequence> CREATOR = new Parcelable.Creator<UserEventSequence>() { // from class: com.microsoft.krestsdk.models.UserEventSequence.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserEventSequence createFromParcel(Parcel in) {
            return new UserEventSequence(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserEventSequence[] newArray(int size) {
            return new UserEventSequence[size];
        }
    };
    @SerializedName("AverageHeartRate")
    private int mAverageHeartRate;
    @SerializedName("CaloriesBurned")
    private int mCaloriesBurned;
    @SerializedName("Duration")
    private int mDuration;
    @SerializedName("LocationBlob")
    private UUID mLocationBlob;
    @SerializedName("LowestHeartRate")
    private int mLowestHeartRate;
    @SerializedName("PeakHeartRate")
    private int mPeakHeartRate;
    @SerializedName("SequenceId")
    private long mSequenceId;
    @SerializedName("StartTime")
    private DateTime mStartTime;

    public UserEventSequence() {
    }

    public long getSequenceId() {
        return this.mSequenceId;
    }

    public void setSequenceId(long sequenceId) {
        this.mSequenceId = sequenceId;
    }

    public DateTime getStartTime() {
        return this.mStartTime;
    }

    public void setStartTime(DateTime startTime) {
        this.mStartTime = startTime;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public int getCaloriesBurned() {
        return this.mCaloriesBurned;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.mCaloriesBurned = caloriesBurned;
    }

    public UUID getLocationBlob() {
        return this.mLocationBlob;
    }

    public void setLocationBlob(UUID locationBlob) {
        this.mLocationBlob = locationBlob;
    }

    public void setAverageHeartRate(int heartRate) {
        this.mAverageHeartRate = heartRate;
    }

    public int getAverageHeartRate() {
        return this.mAverageHeartRate;
    }

    public int getLowestHeartRate() {
        return this.mLowestHeartRate;
    }

    public void setLowestHeartRate(int lowestHeartRate) {
        this.mLowestHeartRate = lowestHeartRate;
    }

    public int getPeakHeartRate() {
        return this.mPeakHeartRate;
    }

    public void setPeakHeartRate(int peakHeartRate) {
        this.mPeakHeartRate = peakHeartRate;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public UserEventSequence(Parcel in) {
        this.mSequenceId = in.readLong();
        this.mStartTime = new DateTime(in.readLong());
        this.mDuration = in.readInt();
        this.mCaloriesBurned = in.readInt();
        this.mAverageHeartRate = in.readInt();
        this.mLowestHeartRate = in.readInt();
        this.mPeakHeartRate = in.readInt();
        this.mLocationBlob = (UUID) UUID.class.cast(in.readSerializable());
        this.mLowestHeartRate = in.readInt();
        this.mPeakHeartRate = in.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mSequenceId);
        dest.writeLong(this.mStartTime.getMillis());
        dest.writeInt(this.mDuration);
        dest.writeInt(this.mCaloriesBurned);
        dest.writeInt(this.mAverageHeartRate);
        dest.writeInt(this.mLowestHeartRate);
        dest.writeInt(this.mPeakHeartRate);
        dest.writeSerializable(this.mLocationBlob);
        dest.writeInt(this.mLowestHeartRate);
        dest.writeInt(this.mPeakHeartRate);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}

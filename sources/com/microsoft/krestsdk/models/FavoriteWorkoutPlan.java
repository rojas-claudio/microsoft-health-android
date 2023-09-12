package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class FavoriteWorkoutPlan implements Parcelable {
    public static final Parcelable.Creator<FavoriteWorkoutPlan> CREATOR = new Parcelable.Creator<FavoriteWorkoutPlan>() { // from class: com.microsoft.krestsdk.models.FavoriteWorkoutPlan.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FavoriteWorkoutPlan createFromParcel(Parcel in) {
            return new FavoriteWorkoutPlan(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FavoriteWorkoutPlan[] newArray(int size) {
            return new FavoriteWorkoutPlan[size];
        }
    };
    @SerializedName("ETag")
    private String mETag;
    @SerializedName("$id")
    private String mId;
    @SerializedName("CurrentInstanceId")
    private int mInstanceId;
    @SerializedName("IsSubscribed")
    private boolean mIsSubscribed;
    @SerializedName("LasUpdateTime")
    private DateTime mLastUpdateTime;
    @SerializedName("NumberOfTimesCompleted")
    private int mNumberOfTimesCompleted;
    @SerializedName("PartitionKey")
    private String mPartitionKey;
    @SerializedName("RowKey")
    private String mRowKey;
    @SerializedName("Status")
    private int mStatus;
    @SerializedName("TimeFavorited")
    private DateTime mTimeFavorited;
    @SerializedName("TimeSubscribed")
    private DateTime mTimeSubscribed;
    @SerializedName("Timestamp")
    private DateTime mTimestamp;
    @SerializedName("UserId")
    private String mUserId;
    @SerializedName("WorkoutPlanBrowseDetails")
    private WorkoutPlanBrowseDetails mWorkoutPlanBrowseDetails;
    @SerializedName(GuidedWorkoutNotificationHandler.KEY_WORKOUT_PLAN_ID)
    private String mWorkoutPlanId;

    public String getId() {
        return this.mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public int getInstanceId() {
        return this.mInstanceId;
    }

    public void setInstanceId(int mInstanceId) {
        this.mInstanceId = mInstanceId;
    }

    public String getETag() {
        return this.mETag;
    }

    public void setETag(String mETag) {
        this.mETag = mETag;
    }

    public boolean isSubscribed() {
        return this.mIsSubscribed;
    }

    public void setIsSubscribed(boolean mIsSubscribed) {
        this.mIsSubscribed = mIsSubscribed;
    }

    public DateTime getLastUpdateTime() {
        return this.mLastUpdateTime;
    }

    public void setLastUpdateTime(DateTime mLastUpdateTime) {
        this.mLastUpdateTime = mLastUpdateTime;
    }

    public int getNumberOfTimesCompleted() {
        return this.mNumberOfTimesCompleted;
    }

    public void setNumberOfTimesCompleted(int mNumberOfTimesCompleted) {
        this.mNumberOfTimesCompleted = mNumberOfTimesCompleted;
    }

    public String getPartitionKey() {
        return this.mPartitionKey;
    }

    public void setPartitionKey(String mPartitionKey) {
        this.mPartitionKey = mPartitionKey;
    }

    public String getRowKey() {
        return this.mRowKey;
    }

    public void setRowKey(String mRowKey) {
        this.mRowKey = mRowKey;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public void setStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public DateTime getTimeFavorited() {
        return this.mTimeFavorited;
    }

    public void setTimeFavorited(DateTime mTimeFavorited) {
        this.mTimeFavorited = mTimeFavorited;
    }

    public DateTime getTimestamp() {
        return this.mTimestamp;
    }

    public void setTimestamp(DateTime mTimestamp) {
        this.mTimestamp = mTimestamp;
    }

    public DateTime getTimeSubscribed() {
        return this.mTimeSubscribed;
    }

    public void setTimeSubscribed(DateTime mTimeSubscribed) {
        this.mTimeSubscribed = mTimeSubscribed;
    }

    public String getUserId() {
        return this.mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getWorkoutPlanId() {
        return this.mWorkoutPlanId;
    }

    public void setWorkoutPlanId(String mWorkoutPlanId) {
        this.mWorkoutPlanId = mWorkoutPlanId;
    }

    public WorkoutPlanBrowseDetails getWorkoutPlanBrowseDetails() {
        return this.mWorkoutPlanBrowseDetails;
    }

    public void setWorkoutPlanBrowseDetails(WorkoutPlanBrowseDetails workoutPlanBrowseDetails) {
        this.mWorkoutPlanBrowseDetails = workoutPlanBrowseDetails;
    }

    public FavoriteWorkoutPlan() {
    }

    public FavoriteWorkoutPlan(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mETag);
        dest.writeInt(!this.mIsSubscribed ? 0 : 1);
        dest.writeInt(this.mInstanceId);
        dest.writeLong(this.mLastUpdateTime.getMillis());
        dest.writeInt(this.mNumberOfTimesCompleted);
        dest.writeString(this.mPartitionKey);
        dest.writeString(this.mRowKey);
        dest.writeInt(this.mStatus);
        dest.writeLong(this.mTimeFavorited.getMillis());
        dest.writeLong(this.mTimestamp.getMillis());
        dest.writeLong(this.mTimeSubscribed.getMillis());
        dest.writeString(this.mUserId);
        dest.writeString(this.mWorkoutPlanId);
        dest.writeParcelable(this.mWorkoutPlanBrowseDetails, flags);
    }

    public void readFromParcel(Parcel in) {
        this.mId = in.readString();
        this.mETag = in.readString();
        this.mIsSubscribed = in.readInt() == 1;
        this.mInstanceId = in.readInt();
        this.mLastUpdateTime = new DateTime(in.readLong());
        this.mNumberOfTimesCompleted = in.readInt();
        this.mPartitionKey = in.readString();
        this.mRowKey = in.readString();
        this.mStatus = in.readInt();
        this.mTimeFavorited = new DateTime(in.readLong());
        this.mTimestamp = new DateTime(in.readLong());
        this.mTimeSubscribed = new DateTime(in.readLong());
        this.mUserId = in.readString();
        this.mWorkoutPlanId = in.readString();
        this.mWorkoutPlanBrowseDetails = (WorkoutPlanBrowseDetails) in.readParcelable(WorkoutPlanBrowseDetails.class.getClassLoader());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}

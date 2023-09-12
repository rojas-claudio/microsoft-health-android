package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.models.SyncedWorkoutInfo;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class ScheduledWorkout implements Parcelable {
    public static final Parcelable.Creator<ScheduledWorkout> CREATOR = new Parcelable.Creator<ScheduledWorkout>() { // from class: com.microsoft.krestsdk.models.ScheduledWorkout.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ScheduledWorkout createFromParcel(Parcel in) {
            return new ScheduledWorkout(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ScheduledWorkout[] newArray(int size) {
            return new ScheduledWorkout[size];
        }
    };
    @SerializedName("Day")
    private int mDay;
    @SerializedName("DeviceWorkoutId")
    private int mDeviceWorkoutId;
    @SerializedName("$id")
    private String mId;
    @SerializedName("IsUserDeleted")
    private boolean mIsUserDeleted;
    @SerializedName("LastUpdateTime")
    private DateTime mLastUpdateTime;
    @SerializedName("UserId")
    private String mUserId;
    @SerializedName("WeekId")
    private int mWeekId;
    @SerializedName("WorkoutIndex")
    private int mWorkoutIndex;
    @SerializedName(GuidedWorkoutNotificationHandler.KEY_WORKOUT_PLAN_ID)
    private String mWorkoutPlanId;
    @SerializedName("WorkoutPlanInstanceId")
    private int mWorkoutPlanInstanceId;
    @SerializedName("WorkoutStatus")
    private int mWorkoutStatus;

    public ScheduledWorkout() {
    }

    public ScheduledWorkout(SyncedWorkoutInfo syncedWorkoutInfo) {
        if (syncedWorkoutInfo != null) {
            this.mWorkoutPlanId = syncedWorkoutInfo.getWorkoutPlanId();
            this.mWeekId = syncedWorkoutInfo.getWeek();
            this.mDay = syncedWorkoutInfo.getDay();
            this.mWorkoutIndex = syncedWorkoutInfo.getWorkoutIndex();
        }
    }

    public String getId() {
        return this.mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getWorkoutPlanId() {
        return this.mWorkoutPlanId;
    }

    public void setWorkoutPlanId(String workoutPlanId) {
        this.mWorkoutPlanId = workoutPlanId;
    }

    public int getWorkoutIndex() {
        return this.mWorkoutIndex;
    }

    public void setWorkoutIndex(int workoutIndex) {
        this.mWorkoutIndex = workoutIndex;
    }

    public String getUserId() {
        return this.mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public boolean getIsUserDeleted() {
        return this.mIsUserDeleted;
    }

    public void setIsUserDeleted(boolean isUserDeleted) {
        this.mIsUserDeleted = isUserDeleted;
    }

    public DateTime getLastUpdateTime() {
        return this.mLastUpdateTime;
    }

    public void setLastUpdateTime(DateTime lastUpdateTime) {
        this.mLastUpdateTime = lastUpdateTime;
    }

    public int getWeekId() {
        return this.mWeekId;
    }

    public void setWeekId(int weekId) {
        this.mWeekId = weekId;
    }

    public int getDay() {
        return this.mDay;
    }

    public void setDay(int day) {
        this.mDay = day;
    }

    public int getWorkoutPlanInstanceId() {
        return this.mWorkoutPlanInstanceId;
    }

    public UserWorkoutStatus getTypedWorkoutStatus() {
        UserWorkoutStatus[] values = UserWorkoutStatus.values();
        for (UserWorkoutStatus value : values) {
            if (value.getNumericType() == this.mWorkoutStatus) {
                return value;
            }
        }
        return UserWorkoutStatus.NOT_STARTED;
    }

    public int getDeviceWorkoutId() {
        return this.mDeviceWorkoutId;
    }

    public void setDeviceWorkoutId(int deviceWorkoutId) {
        this.mDeviceWorkoutId = deviceWorkoutId;
    }

    protected ScheduledWorkout(Parcel in) {
        this.mId = in.readString();
        this.mWorkoutPlanId = in.readString();
        this.mWorkoutIndex = in.readInt();
        this.mUserId = in.readString();
        this.mIsUserDeleted = in.readInt() == 1;
        long lastUpdateTime = in.readLong();
        this.mLastUpdateTime = lastUpdateTime >= 0 ? new DateTime(lastUpdateTime) : null;
        this.mWeekId = in.readInt();
        this.mDay = in.readInt();
        this.mWorkoutStatus = in.readInt();
        this.mDeviceWorkoutId = in.readInt();
        this.mWorkoutPlanInstanceId = in.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.mWorkoutPlanId);
        dest.writeInt(this.mWorkoutIndex);
        dest.writeString(this.mUserId);
        dest.writeInt(this.mIsUserDeleted ? 1 : 0);
        dest.writeLong(this.mLastUpdateTime == null ? -1L : this.mLastUpdateTime.getMillis());
        dest.writeInt(this.mWeekId);
        dest.writeInt(this.mDay);
        dest.writeInt(this.mWorkoutStatus);
        dest.writeInt(this.mDeviceWorkoutId);
        dest.writeInt(this.mWorkoutPlanInstanceId);
    }
}

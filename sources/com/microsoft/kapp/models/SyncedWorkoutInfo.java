package com.microsoft.kapp.models;

import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class SyncedWorkoutInfo {
    @SerializedName("Day")
    private int mDay;
    @SerializedName("$id")
    private String mId;
    @SerializedName("TimeSynced")
    private DateTime mTimeSynced;
    @SerializedName("WeekId")
    private int mWeek;
    @SerializedName("WorkoutIndex")
    private int mWorkoutIndex;
    @SerializedName(GuidedWorkoutNotificationHandler.KEY_WORKOUT_PLAN_ID)
    private String mWorkoutPlanId;
    @SerializedName("WorkoutPlanInstanceId")
    private int mWorkoutPlanInstanceId;

    public SyncedWorkoutInfo() {
    }

    public SyncedWorkoutInfo(String workoutPlanId, int day, int week, int workoutIndex) {
        this.mWorkoutPlanId = workoutPlanId;
        this.mDay = day;
        this.mWeek = week;
        this.mWorkoutIndex = workoutIndex;
    }

    public SyncedWorkoutInfo(String workoutPlanId, int workoutPlanInstanceId, int day, int week, int workoutIndex, DateTime timeSynced) {
        this.mId = "";
        this.mWorkoutPlanId = workoutPlanId;
        this.mWorkoutPlanInstanceId = workoutPlanInstanceId;
        this.mDay = day;
        this.mWeek = week;
        this.mWorkoutIndex = workoutIndex;
        this.mTimeSynced = timeSynced;
    }

    public String getId() {
        return this.mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getWorkoutPlanId() {
        return this.mWorkoutPlanId;
    }

    public void setWorkoutPlanId(String workoutPlanId) {
        this.mWorkoutPlanId = workoutPlanId;
    }

    public int getWorkoutPlanInstanceId() {
        return this.mWorkoutPlanInstanceId;
    }

    public void setWorkoutPlanInstanceId(int workoutPlanInstanceId) {
        this.mWorkoutPlanInstanceId = workoutPlanInstanceId;
    }

    public int getDay() {
        return this.mDay;
    }

    public void setDay(int day) {
        this.mDay = day;
    }

    public int getWeek() {
        return this.mWeek;
    }

    public void setWeek(int week) {
        this.mWeek = week;
    }

    public int getWorkoutIndex() {
        return this.mWorkoutIndex;
    }

    public void setWorkoutIndex(int workoutIndex) {
        this.mWorkoutIndex = workoutIndex;
    }

    public DateTime getTimeSynced() {
        return this.mTimeSynced;
    }

    public void setTimeSynced(DateTime timeSynced) {
        this.mTimeSynced = timeSynced;
    }
}

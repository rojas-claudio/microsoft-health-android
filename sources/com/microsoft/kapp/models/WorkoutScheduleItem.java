package com.microsoft.kapp.models;

import com.microsoft.kapp.services.healthandfitness.models.WorkoutPlan;
import com.microsoft.krestsdk.models.ScheduledWorkout;
import com.microsoft.krestsdk.models.UserWorkoutStatus;
/* loaded from: classes.dex */
public class WorkoutScheduleItem {
    private UserWorkoutStatus mCompletionStatus;
    private boolean mIsEnabled = true;
    private boolean mIsRest;
    private boolean mIsSkipPending;
    private Boolean mSyncStatus;
    private ScheduledWorkout mWorkout;
    private OnWorkoutClickedListener mWorkoutClickedListener;
    private WorkoutPlan mWorkoutPlan;
    private OnWorkoutToggledListener mWorkoutToggledListener;

    /* loaded from: classes.dex */
    public interface OnWorkoutClickedListener {
        void onWorkoutClicked(WorkoutScheduleItem workoutScheduleItem);
    }

    /* loaded from: classes.dex */
    public interface OnWorkoutToggledListener {
        void onWorkoutToggled(WorkoutScheduleItem workoutScheduleItem, boolean z);
    }

    private WorkoutScheduleItem(ScheduledWorkout workout, WorkoutPlan workoutPlan, boolean isRest, UserWorkoutStatus completionStatus, OnWorkoutToggledListener workoutToggledListener, OnWorkoutClickedListener workoutClickListener) {
        this.mWorkout = workout;
        this.mWorkoutPlan = workoutPlan;
        this.mIsRest = isRest;
        this.mCompletionStatus = completionStatus;
        this.mWorkoutToggledListener = workoutToggledListener;
        this.mWorkoutClickedListener = workoutClickListener;
    }

    public static WorkoutScheduleItem createNormal(ScheduledWorkout workout, WorkoutPlan workoutPlan, UserWorkoutStatus completionStatus, OnWorkoutToggledListener workoutToggledListener, OnWorkoutClickedListener workoutClickListener) {
        return new WorkoutScheduleItem(workout, workoutPlan, false, completionStatus, workoutToggledListener, workoutClickListener);
    }

    public static WorkoutScheduleItem createRest() {
        return new WorkoutScheduleItem(null, null, true, UserWorkoutStatus.NOT_STARTED, null, null);
    }

    public ScheduledWorkout getWorkout() {
        return this.mWorkout;
    }

    public String getName() {
        int workoutIndex = this.mWorkout.getWorkoutIndex();
        return this.mWorkoutPlan.getSteps()[workoutIndex].getName();
    }

    public int getDay() {
        return this.mWorkout.getDay();
    }

    public int getWeek() {
        return this.mWorkout.getWeekId();
    }

    public boolean isRest() {
        return this.mIsRest;
    }

    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    public void setIsEnabled(boolean value) {
        this.mIsEnabled = value;
    }

    public Boolean getSyncStatus() {
        return this.mSyncStatus;
    }

    public void setSyncStatus(Boolean value) {
        this.mSyncStatus = value;
    }

    public UserWorkoutStatus getCompletionStatus() {
        return this.mCompletionStatus;
    }

    public void setCompletionState(UserWorkoutStatus value) {
        this.mCompletionStatus = value;
    }

    public OnWorkoutToggledListener getWorkoutToggledListener() {
        return this.mWorkoutToggledListener;
    }

    public boolean isSkipPending() {
        return this.mIsSkipPending;
    }

    public void setIsSkipPending(boolean value) {
        this.mIsSkipPending = value;
    }

    public OnWorkoutClickedListener getWorkoutClickedListener() {
        return this.mWorkoutClickedListener;
    }

    public void setWorkoutClickedListener(OnWorkoutClickedListener mWorkoutClickedListener) {
        this.mWorkoutClickedListener = mWorkoutClickedListener;
    }
}

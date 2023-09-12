package com.microsoft.krestsdk.models;

import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler;
/* loaded from: classes.dex */
public class FeaturedWorkout {
    @SerializedName(TelemetryConstants.Events.OobeComplete.Dimensions.GENDER)
    private String mGender;
    @SerializedName("$id")
    private Integer mId;
    @SerializedName("MaximumAge")
    private Integer mMaximumAge;
    @SerializedName("MinimumAge")
    private Integer mMinimumAge;
    @SerializedName("Region")
    private String mRegion;
    @SerializedName("WorkoutPlanBrowseDetails")
    private WorkoutPlanBrowseDetails mWorkoutPlanBrowseDetails;
    @SerializedName(GuidedWorkoutNotificationHandler.KEY_WORKOUT_PLAN_ID)
    private String mWorkoutPlanId;

    public Integer getId() {
        return this.mId;
    }

    public void setId(Integer id) {
        this.mId = id;
    }

    public Integer getMinimumAge() {
        return this.mMinimumAge;
    }

    public void setMinimumAge(Integer minimumAge) {
        this.mMinimumAge = minimumAge;
    }

    public Integer getMaximumAge() {
        return this.mMaximumAge;
    }

    public void setMaximumAge(Integer maximumAge) {
        this.mMaximumAge = maximumAge;
    }

    public String getGender() {
        return this.mGender;
    }

    public void setGender(String gender) {
        this.mGender = gender;
    }

    public String getRegion() {
        return this.mRegion;
    }

    public void setRegion(String region) {
        this.mRegion = region;
    }

    public String getWorkoutPlanId() {
        return this.mWorkoutPlanId;
    }

    public void setWorkoutPlanId(String workoutPlanId) {
        this.mWorkoutPlanId = workoutPlanId;
    }

    public WorkoutPlanBrowseDetails getWorkoutPlanBrowseDetails() {
        return this.mWorkoutPlanBrowseDetails;
    }

    public void setWorkoutPlanBrowseDetails(WorkoutPlanBrowseDetails workoutPlanBrowseDetails) {
        this.mWorkoutPlanBrowseDetails = workoutPlanBrowseDetails;
    }
}

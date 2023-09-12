package com.microsoft.krestsdk.models;

import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class UserEventSummaryDTO {
    @SerializedName("LastGuidedWorkoutEvent")
    private GuidedWorkoutEvent mLastGuidedWorkoutEvent;

    public GuidedWorkoutEvent getLastGuidedWorkout() {
        return this.mLastGuidedWorkoutEvent;
    }

    public void setLastGuidedWorkoutEvent(GuidedWorkoutEvent lastGuidedWorkoutEvent) {
        this.mLastGuidedWorkoutEvent = lastGuidedWorkoutEvent;
    }
}

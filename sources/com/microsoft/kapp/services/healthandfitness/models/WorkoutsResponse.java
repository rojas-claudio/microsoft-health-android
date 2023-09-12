package com.microsoft.kapp.services.healthandfitness.models;

import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class WorkoutsResponse {
    @SerializedName("count")
    private int mCount;
    @SerializedName("results")
    private WorkoutSummary[] mWorkoutSummaries;

    public int getCount() {
        return this.mCount;
    }

    public void setCount(int mCount) {
        this.mCount = mCount;
    }

    public WorkoutSummary[] getWorkoutSummaries() {
        return this.mWorkoutSummaries;
    }

    public void setWorkoutSummaries(WorkoutSummary[] mWorkoutSummaries) {
        this.mWorkoutSummaries = mWorkoutSummaries;
    }
}

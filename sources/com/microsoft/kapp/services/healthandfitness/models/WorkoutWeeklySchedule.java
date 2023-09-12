package com.microsoft.kapp.services.healthandfitness.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
/* loaded from: classes.dex */
public class WorkoutWeeklySchedule implements Serializable {
    private static final long serialVersionUID = 5791777923406715661L;
    @SerializedName("id")
    private String mPeriod;
    @SerializedName("daylist")
    private WorkoutDay[] mWorkoutDays;

    public WorkoutDay[] getWorkoutDays() {
        return this.mWorkoutDays;
    }

    public void setWorkoutDays(WorkoutDay[] mWorkoutDays) {
        this.mWorkoutDays = mWorkoutDays;
    }

    public String getPeriod() {
        return this.mPeriod;
    }

    public void setPeriod(String mPeriod) {
        this.mPeriod = mPeriod;
    }
}

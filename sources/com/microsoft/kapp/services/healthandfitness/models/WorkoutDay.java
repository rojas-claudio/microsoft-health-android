package com.microsoft.kapp.services.healthandfitness.models;

import com.google.gson.annotations.SerializedName;
import com.microsoft.krestsdk.services.KCloudConstants;
import java.io.Serializable;
/* loaded from: classes.dex */
public class WorkoutDay implements Serializable {
    private static final long serialVersionUID = -1584137798094885291L;
    @SerializedName(KCloudConstants.WORKOUT_DAY)
    private int mDay;
    @SerializedName("workouts")
    private String[] mWorkouts;

    public int getDay() {
        return this.mDay;
    }

    public void setDay(int mDay) {
        this.mDay = mDay;
    }

    public String[] getWorkouts() {
        return this.mWorkouts;
    }

    public void setWorkouts(String[] mWorkouts) {
        this.mWorkouts = mWorkouts;
    }
}

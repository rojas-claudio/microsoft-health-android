package com.microsoft.kapp.activities;

import com.microsoft.kapp.services.healthandfitness.models.WorkoutPlan;
import com.microsoft.krestsdk.models.ScheduledWorkout;
import java.util.List;
/* loaded from: classes.dex */
public interface WorkoutActivityListener {
    boolean getFavoriteStatus();

    List<ScheduledWorkout> getPlanSchedule();

    boolean getSubscribedStatus();

    String getSubscribedWorkoutPlan();

    WorkoutPlan getWorkoutPlan();

    void updateFavoriteStatus();

    void updateSubscribedStatus(boolean z);
}

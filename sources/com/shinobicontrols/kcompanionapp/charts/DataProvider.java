package com.shinobicontrols.kcompanionapp.charts;

import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.models.ExerciseEventBase;
import com.microsoft.krestsdk.models.GolfEvent;
import com.microsoft.krestsdk.models.RunEvent;
import com.microsoft.krestsdk.models.SleepEvent;
import com.microsoft.krestsdk.models.UserActivity;
import com.microsoft.krestsdk.models.UserDailySummary;
/* loaded from: classes.dex */
public interface DataProvider {
    BikeEvent getBikeEvent();

    UserDailySummary[] getDailySummariesForWeek();

    ExerciseEventBase getExerciseEvent();

    GolfEvent getGolfEvent();

    UserActivity[] getHourlyUserActivitiesForDay();

    RunEvent getRunEvent();

    SleepEvent getSleepEvent();
}

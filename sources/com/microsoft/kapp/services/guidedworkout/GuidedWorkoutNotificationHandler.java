package com.microsoft.kapp.services.guidedworkout;

import android.content.Intent;
import com.microsoft.krestsdk.models.ScheduledWorkout;
/* loaded from: classes.dex */
public interface GuidedWorkoutNotificationHandler {
    public static final String KEY_ACTION_STATUS = "ActionStatus";
    public static final String KEY_ERROR_ID = "ErrorId";
    public static final String KEY_IS_FIRST_WORKOUT = "isFirstWorkout";
    public static final String KEY_IS_USER_INITIATED = "isUserInitiated";
    public static final String KEY_NEXT_WORKOUT_NAME = "NextWorkoutName";
    public static final String KEY_NEXT_WORKOUT_PLAN_NAME = "NextWorkoutPlanName";
    public static final String KEY_SCHEDULED_WORKOUT = "ScheduledWorkout";
    public static final String KEY_WORKOUT_PLAN_ID = "WorkoutPlanId";
    public static final String OPERATION_FAVORITE = "com.microsoft.KApp.GuidedWorkout.Favorite";
    public static final String OPERATION_NEXT_CALENDAR_WORKOUT = "com.microsoft.KApp.GuidedWorkout.NextCalendarWorkout";
    public static final String OPERATION_SUBSCRIBE = "com.microsoft.KApp.GuidedWorkout.Subscribe";
    public static final String OPERATION_SYNC = "com.microsoft.KApp.GuidedWorkout.Sync";
    public static final String OPERATION_UNFAVORITE = "com.microsoft.KApp.GuidedWorkout.Unfavorite";
    public static final String OPERATION_UNSUBSCRIBE = "com.microsoft.KApp.GuidedWorkout.Unsubscribe";

    void broadcastGuidedWorkoutEvent(String str, int i, Intent intent);

    void notifyNextCalendarWorkoutUpdated();

    void notifyWorkoutPlanFavoriteError(String str, int i);

    void notifyWorkoutPlanFavoriteStarted(String str);

    void notifyWorkoutPlanFavorited(String str);

    void notifyWorkoutPlanSubscribeError(String str);

    void notifyWorkoutPlanSubscribeStarted(String str);

    void notifyWorkoutPlanSubscribed(String str);

    void notifyWorkoutPlanUnfavoriteError(String str);

    void notifyWorkoutPlanUnfavoriteStarted(String str);

    void notifyWorkoutPlanUnfavorited(String str);

    void notifyWorkoutPlanUnsubscribeError(String str);

    void notifyWorkoutPlanUnsubscribeStarted(String str);

    void notifyWorkoutPlanUnsubscribed(String str);

    void notifyWorkoutSyncError(ScheduledWorkout scheduledWorkout, int i, boolean z, boolean z2);

    void notifyWorkoutSyncStarted(ScheduledWorkout scheduledWorkout);

    void notifyWorkoutSyncSuccess(String str, String str2, ScheduledWorkout scheduledWorkout);
}

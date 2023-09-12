package com.microsoft.kapp.services.guidedworkout;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.models.SyncedWorkoutInfo;
import com.microsoft.kapp.services.healthandfitness.WorkoutProviderType;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutPlan;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutsResponseEnvelope;
import com.microsoft.krestsdk.models.FavoriteWorkoutPlan;
import com.microsoft.krestsdk.models.FeaturedWorkout;
import com.microsoft.krestsdk.models.GuidedWorkoutEvent;
import com.microsoft.krestsdk.models.ScheduledWorkout;
import java.util.List;
/* loaded from: classes.dex */
public interface GuidedWorkoutService {
    public static final int WORKOUT_PULL_NETWORK_ERROR = 3;
    public static final int WORKOUT_PUSH_FAILED = 0;
    public static final int WORKOUT_PUSH_FAILED_TILE_OPEN = 1;
    public static final int WORKOUT_PUSH_SUCCEEDED = 2;

    void clearAllData();

    void deleteLastGuidedWorkoutEventLocally();

    void favoriteWorkoutPlan(String str);

    void fetchGuidedWorkoutData(Callback<Void> callback, boolean z);

    ScheduledWorkout getCurrentlySyncingWorkout();

    void getFavoriteWorkoutPlans(Callback<List<FavoriteWorkoutPlan>> callback);

    void getFeaturedWorkouts(int i, String str, Callback<List<FeaturedWorkout>> callback);

    void getGuidedWorkoutEventById(String str, boolean z, Callback<GuidedWorkoutEvent> callback);

    GuidedWorkoutEvent getGuidedWorkoutevent();

    void getHnFStrengthWorkoutPlans(WorkoutProviderType workoutProviderType, Callback<WorkoutsResponseEnvelope> callback);

    void getHnFStrengthWorkoutPlans(String str, WorkoutProviderType workoutProviderType, Callback<WorkoutsResponseEnvelope> callback);

    void getHnFStrengthWorkoutPlans(String str, String str2, WorkoutProviderType workoutProviderType, Callback<WorkoutsResponseEnvelope> callback);

    void getHnFWorkoutPlanDetails(String str, Callback<WorkoutPlan> callback);

    SyncedWorkoutInfo getLastSyncedWorkout();

    void getLastSyncedWorkout(Callback<SyncedWorkoutInfo> callback);

    String getNextGuidedWorkoutStepName();

    String getNextGuidedWorkoutStepPlanName();

    ScheduledWorkout getNextGuidedWorkoutStepSchedule();

    String getSubscribedWorkoutPlanId();

    void getWorkoutPlanSchedules(String str, Callback<List<ScheduledWorkout>> callback);

    boolean isRestDay();

    void pushGuidedWorkoutToDevice(ScheduledWorkout scheduledWorkout);

    void pushGuidedWorkoutToDevice(ScheduledWorkout scheduledWorkout, boolean z, boolean z2);

    void setLastGuidedWorkoutEvent(GuidedWorkoutEvent guidedWorkoutEvent);

    void setLastSyncedWorkout(SyncedWorkoutInfo syncedWorkoutInfo);

    void setNextGuidedWorkoutInfo(String str, String str2, ScheduledWorkout scheduledWorkout);

    void setRestDay(boolean z);

    void setSubscribedWorkoutPlanId(String str);

    void skipWorkout(String str, int i, int i2, int i3, Callback<Void> callback);

    void subscribeToWorkoutPlan(String str);

    void unfavoriteWorkoutPlan(String str);

    void unsubscribeFromWorkoutPlan(String str);

    void updateManualSync(ScheduledWorkout scheduledWorkout);
}

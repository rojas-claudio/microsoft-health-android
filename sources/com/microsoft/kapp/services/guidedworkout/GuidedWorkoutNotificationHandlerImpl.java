package com.microsoft.kapp.services.guidedworkout;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.tasks.GuidedWorkout.CalculateNextWorkoutOperation;
import com.microsoft.krestsdk.models.ScheduledWorkout;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class GuidedWorkoutNotificationHandlerImpl implements GuidedWorkoutNotificationHandler {
    @Inject
    Context mApplicationContext;
    GuidedWorkoutService mGuidedWorkoutService;

    public GuidedWorkoutNotificationHandlerImpl(GuidedWorkoutService guidedWorkoutService) {
        KApplicationGraph.getApplicationGraph().inject(this);
        this.mGuidedWorkoutService = guidedWorkoutService;
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler
    public void notifyWorkoutSyncStarted(ScheduledWorkout syncingScheduledWorkout) {
        Intent data = new Intent();
        data.putExtra(GuidedWorkoutNotificationHandler.KEY_SCHEDULED_WORKOUT, syncingScheduledWorkout);
        broadcastGuidedWorkoutEvent(GuidedWorkoutNotificationHandler.OPERATION_SYNC, 0, data);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler
    public void notifyWorkoutSyncSuccess(String syncedWorkoutPlanName, String syncedWorkoutName, ScheduledWorkout syncedScheduledWorkout) {
        this.mGuidedWorkoutService.setNextGuidedWorkoutInfo(syncedWorkoutPlanName, syncedWorkoutName, syncedScheduledWorkout);
        this.mGuidedWorkoutService.setRestDay(false);
        notifyNextCalendarWorkoutUpdated();
        Intent data = new Intent();
        data.putExtra(GuidedWorkoutNotificationHandler.KEY_SCHEDULED_WORKOUT, syncedScheduledWorkout);
        broadcastGuidedWorkoutEvent(GuidedWorkoutNotificationHandler.OPERATION_SYNC, 1, data);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler
    public void notifyWorkoutSyncError(ScheduledWorkout syncingScheduledWorkout, int errorId, boolean isFirstWorkout, boolean isUserInitiated) {
        Intent data = new Intent();
        data.putExtra(GuidedWorkoutNotificationHandler.KEY_SCHEDULED_WORKOUT, syncingScheduledWorkout);
        data.putExtra("ErrorId", errorId);
        data.putExtra(GuidedWorkoutNotificationHandler.KEY_IS_FIRST_WORKOUT, isFirstWorkout);
        data.putExtra(GuidedWorkoutNotificationHandler.KEY_IS_USER_INITIATED, isUserInitiated);
        broadcastGuidedWorkoutEvent(GuidedWorkoutNotificationHandler.OPERATION_SYNC, 2, data);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler
    public void notifyWorkoutPlanSubscribeStarted(String workoutPlanId) {
        Intent data = new Intent();
        data.putExtra(GuidedWorkoutNotificationHandler.KEY_WORKOUT_PLAN_ID, workoutPlanId);
        broadcastGuidedWorkoutEvent(GuidedWorkoutNotificationHandler.OPERATION_SUBSCRIBE, 0, data);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler
    public void notifyWorkoutPlanSubscribed(String newSubscribedWorkoutPlanId) {
        this.mGuidedWorkoutService.setSubscribedWorkoutPlanId(newSubscribedWorkoutPlanId);
        Intent data = new Intent();
        data.putExtra(GuidedWorkoutNotificationHandler.KEY_WORKOUT_PLAN_ID, newSubscribedWorkoutPlanId);
        broadcastGuidedWorkoutEvent(GuidedWorkoutNotificationHandler.OPERATION_SUBSCRIBE, 1, data);
        new CalculateNextWorkoutOperation(null, this, null, true).execute();
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler
    public void notifyWorkoutPlanSubscribeError(String newSubscribedWorkoutPlanId) {
        Intent data = new Intent();
        data.putExtra(GuidedWorkoutNotificationHandler.KEY_WORKOUT_PLAN_ID, newSubscribedWorkoutPlanId);
        data.putExtra("ErrorId", -1);
        broadcastGuidedWorkoutEvent(GuidedWorkoutNotificationHandler.OPERATION_SUBSCRIBE, 2, data);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler
    public void notifyWorkoutPlanUnsubscribeStarted(String workoutPlanId) {
        Intent data = new Intent();
        data.putExtra(GuidedWorkoutNotificationHandler.KEY_WORKOUT_PLAN_ID, workoutPlanId);
        broadcastGuidedWorkoutEvent(GuidedWorkoutNotificationHandler.OPERATION_UNSUBSCRIBE, 0, data);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler
    public void notifyWorkoutPlanUnsubscribed(String unSubscribedWorkoutPlanId) {
        this.mGuidedWorkoutService.setSubscribedWorkoutPlanId(null);
        this.mGuidedWorkoutService.setRestDay(false);
        Intent data = new Intent();
        data.putExtra(GuidedWorkoutNotificationHandler.KEY_WORKOUT_PLAN_ID, unSubscribedWorkoutPlanId);
        broadcastGuidedWorkoutEvent(GuidedWorkoutNotificationHandler.OPERATION_UNSUBSCRIBE, 1, data);
        new CalculateNextWorkoutOperation(null, this, null, true).execute();
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler
    public void notifyWorkoutPlanUnsubscribeError(String unSubscribedWorkoutPlanId) {
        Intent data = new Intent();
        data.putExtra(GuidedWorkoutNotificationHandler.KEY_WORKOUT_PLAN_ID, unSubscribedWorkoutPlanId);
        data.putExtra("ErrorId", -1);
        broadcastGuidedWorkoutEvent(GuidedWorkoutNotificationHandler.OPERATION_UNSUBSCRIBE, 2, data);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler
    public void notifyWorkoutPlanFavoriteStarted(String workoutPlanId) {
        Intent data = new Intent();
        data.putExtra(GuidedWorkoutNotificationHandler.KEY_WORKOUT_PLAN_ID, workoutPlanId);
        broadcastGuidedWorkoutEvent(GuidedWorkoutNotificationHandler.OPERATION_FAVORITE, 0, data);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler
    public void notifyWorkoutPlanFavorited(String favoritedWorkoutPlanId) {
        Intent data = new Intent();
        data.putExtra(GuidedWorkoutNotificationHandler.KEY_WORKOUT_PLAN_ID, favoritedWorkoutPlanId);
        broadcastGuidedWorkoutEvent(GuidedWorkoutNotificationHandler.OPERATION_FAVORITE, 1, data);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler
    public void notifyWorkoutPlanFavoriteError(String favoritedWorkoutPlanId, int errorId) {
        Intent data = new Intent();
        data.putExtra(GuidedWorkoutNotificationHandler.KEY_WORKOUT_PLAN_ID, favoritedWorkoutPlanId);
        data.putExtra("ErrorId", errorId);
        broadcastGuidedWorkoutEvent(GuidedWorkoutNotificationHandler.OPERATION_FAVORITE, 2, data);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler
    public void notifyWorkoutPlanUnfavoriteStarted(String workoutPlanId) {
        Intent data = new Intent();
        data.putExtra(GuidedWorkoutNotificationHandler.KEY_WORKOUT_PLAN_ID, workoutPlanId);
        broadcastGuidedWorkoutEvent(GuidedWorkoutNotificationHandler.OPERATION_UNFAVORITE, 0, data);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler
    public void notifyWorkoutPlanUnfavorited(String unfavoritedWorkoutPlanId) {
        Intent data = new Intent();
        data.putExtra(GuidedWorkoutNotificationHandler.KEY_WORKOUT_PLAN_ID, unfavoritedWorkoutPlanId);
        broadcastGuidedWorkoutEvent(GuidedWorkoutNotificationHandler.OPERATION_UNFAVORITE, 1, data);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler
    public void notifyWorkoutPlanUnfavoriteError(String unfavoritedWorkoutPlanId) {
        Intent data = new Intent();
        data.putExtra(GuidedWorkoutNotificationHandler.KEY_WORKOUT_PLAN_ID, unfavoritedWorkoutPlanId);
        data.putExtra("ErrorId", -1);
        broadcastGuidedWorkoutEvent(GuidedWorkoutNotificationHandler.OPERATION_UNFAVORITE, 2, data);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler
    public void broadcastGuidedWorkoutEvent(String operation, int operationStatus, Intent data) {
        data.setAction(operation);
        data.putExtra(GuidedWorkoutNotificationHandler.KEY_ACTION_STATUS, operationStatus);
        LocalBroadcastManager.getInstance(this.mApplicationContext).sendBroadcast(data);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler
    public void notifyNextCalendarWorkoutUpdated() {
        Intent data = new Intent();
        broadcastGuidedWorkoutEvent(GuidedWorkoutNotificationHandler.OPERATION_NEXT_CALENDAR_WORKOUT, 1, data);
    }
}

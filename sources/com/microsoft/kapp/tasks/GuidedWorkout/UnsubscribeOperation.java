package com.microsoft.kapp.tasks.GuidedWorkout;

import android.os.AsyncTask;
import android.text.TextUtils;
import com.microsoft.kapp.AsyncTaskOperation;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.utils.SerialAsyncOperation;
import com.microsoft.krestsdk.models.FavoriteWorkoutPlan;
import com.microsoft.krestsdk.services.KRestException;
import com.microsoft.krestsdk.services.RestService;
import java.util.HashMap;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class UnsubscribeOperation implements SerialAsyncOperation {
    private static final String TAG = UnsubscribeOperation.class.getSimpleName();
    @Inject
    protected GuidedWorkoutService mGuidedWorkoutService;
    private SerialAsyncOperation mNextAsyncOperation;
    private GuidedWorkoutNotificationHandler mNotificationCenter;
    @Inject
    protected RestService mRestService;
    private String mUnsubscribedWorkoutPlanId;
    private String mWorkoutPlanId;

    public UnsubscribeOperation(String workoutPlanId, GuidedWorkoutNotificationHandler notificationCenter, SerialAsyncOperation nextOperation) {
        KApplicationGraph.getApplicationGraph().inject(this);
        this.mWorkoutPlanId = workoutPlanId;
        this.mNextAsyncOperation = nextOperation;
        this.mNotificationCenter = notificationCenter;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.microsoft.kapp.tasks.GuidedWorkout.UnsubscribeOperation$1] */
    @Override // com.microsoft.kapp.utils.SerialAsyncOperation
    public void execute() {
        new AsyncTaskOperation(this.mNextAsyncOperation) { // from class: com.microsoft.kapp.tasks.GuidedWorkout.UnsubscribeOperation.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Void doInBackground(Void... params) {
                try {
                    FavoriteWorkoutPlan oldSubscribedWorkoutPlan = UnsubscribeOperation.this.mRestService.getSubscribedWorkoutPlan();
                    UnsubscribeOperation.this.mUnsubscribedWorkoutPlanId = oldSubscribedWorkoutPlan != null ? oldSubscribedWorkoutPlan.getWorkoutPlanId() : null;
                    if (UnsubscribeOperation.this.mUnsubscribedWorkoutPlanId != null) {
                        try {
                            UnsubscribeOperation.this.mRestService.unsubscribeFromWorkoutPlan(UnsubscribeOperation.this.mUnsubscribedWorkoutPlanId);
                            setOperationResultStatus(1, 0);
                        } catch (KRestException ex) {
                            KLog.e(UnsubscribeOperation.TAG, "Error when unsubscribing from a workout plan!", ex);
                            setOperationResultStatus(2, 1);
                        }
                    } else {
                        UnsubscribeOperation.this.mUnsubscribedWorkoutPlanId = UnsubscribeOperation.this.mWorkoutPlanId;
                        setOperationResultStatus(1, 0);
                    }
                } catch (Exception ex2) {
                    KLog.e(UnsubscribeOperation.TAG, "getting subscribed workout plan failed!", ex2);
                    setOperationResultStatus(2, 1);
                }
                return null;
            }

            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationSuccess() {
                if (UnsubscribeOperation.this.mUnsubscribedWorkoutPlanId != null) {
                    UnsubscribeOperation.this.mNotificationCenter.notifyWorkoutPlanUnsubscribed(UnsubscribeOperation.this.mUnsubscribedWorkoutPlanId);
                }
                if (!TextUtils.equals(UnsubscribeOperation.this.mWorkoutPlanId, UnsubscribeOperation.this.mUnsubscribedWorkoutPlanId) && UnsubscribeOperation.this.mWorkoutPlanId != null) {
                    UnsubscribeOperation.this.mNotificationCenter.notifyWorkoutPlanUnsubscribed(UnsubscribeOperation.this.mWorkoutPlanId);
                }
            }

            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationError() {
                if (UnsubscribeOperation.this.mUnsubscribedWorkoutPlanId != null) {
                    UnsubscribeOperation.this.mNotificationCenter.notifyWorkoutPlanUnsubscribeError(UnsubscribeOperation.this.mUnsubscribedWorkoutPlanId);
                }
                if (!TextUtils.equals(UnsubscribeOperation.this.mWorkoutPlanId, UnsubscribeOperation.this.mUnsubscribedWorkoutPlanId) && UnsubscribeOperation.this.mWorkoutPlanId != null) {
                    UnsubscribeOperation.this.mNotificationCenter.notifyWorkoutPlanUnsubscribeError(UnsubscribeOperation.this.mWorkoutPlanId);
                }
                HashMap<String, String> telemetryProperties = new HashMap<>();
                telemetryProperties.put(TelemetryConstants.Events.GuidedWorkoutSubscribe.Dimensions.IS_SUBSCRIBED, String.valueOf(false));
                telemetryProperties.put("Workout Plan ID", UnsubscribeOperation.this.mWorkoutPlanId);
                Telemetry.logEvent(TelemetryConstants.Events.GuidedWorkoutSubscribe.EVENT_NAME, telemetryProperties, null);
            }

            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationStarted() {
                if (UnsubscribeOperation.this.mWorkoutPlanId != null) {
                    UnsubscribeOperation.this.mNotificationCenter.notifyWorkoutPlanUnsubscribeStarted(UnsubscribeOperation.this.mWorkoutPlanId);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }
}

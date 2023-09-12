package com.microsoft.kapp.tasks.GuidedWorkout;

import android.os.AsyncTask;
import com.microsoft.kapp.AsyncTaskOperation;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.utils.SerialAsyncOperation;
import com.microsoft.krestsdk.services.KRestException;
import com.microsoft.krestsdk.services.RestService;
import java.util.HashMap;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class SubscribeOperation implements SerialAsyncOperation {
    private static final String TAG = SubscribeOperation.class.getSimpleName();
    @Inject
    protected GuidedWorkoutService mGuidedWorkoutService;
    private SerialAsyncOperation mNextAsyncOperation;
    private GuidedWorkoutNotificationHandler mNotificationCenter;
    @Inject
    protected RestService mRestService;
    private String mWorkoutPlanId;

    public SubscribeOperation(String workoutPlanId, GuidedWorkoutNotificationHandler notificationCenter, SerialAsyncOperation nextOperation) {
        KApplicationGraph.getApplicationGraph().inject(this);
        this.mNextAsyncOperation = nextOperation;
        this.mWorkoutPlanId = workoutPlanId;
        this.mNotificationCenter = notificationCenter;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.microsoft.kapp.tasks.GuidedWorkout.SubscribeOperation$1] */
    @Override // com.microsoft.kapp.utils.SerialAsyncOperation
    public void execute() {
        new AsyncTaskOperation(this.mNextAsyncOperation) { // from class: com.microsoft.kapp.tasks.GuidedWorkout.SubscribeOperation.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Void doInBackground(Void... params) {
                if (SubscribeOperation.this.mWorkoutPlanId != null) {
                    try {
                        SubscribeOperation.this.mRestService.subscribeToWorkoutPlan(SubscribeOperation.this.mWorkoutPlanId);
                        setOperationResultStatus(1, 0);
                        return null;
                    } catch (KRestException ex) {
                        KLog.e(SubscribeOperation.TAG, "Error when subscribing to a workout plan!", ex);
                        setOperationResultStatus(2, 1);
                        return null;
                    }
                }
                KLog.e(SubscribeOperation.TAG, "WorkoutPlanId cannot be null!");
                setOperationResultStatus(2, 1);
                return null;
            }

            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationSuccess() {
                SubscribeOperation.this.mNotificationCenter.notifyWorkoutPlanSubscribed(SubscribeOperation.this.mWorkoutPlanId);
                new SyncFirstWorkoutOperation(SubscribeOperation.this.mWorkoutPlanId, null, true).execute();
                HashMap<String, String> telemetryProperties = new HashMap<>();
                telemetryProperties.put(TelemetryConstants.Events.GuidedWorkoutSubscribe.Dimensions.IS_SUBSCRIBED, String.valueOf(true));
                telemetryProperties.put("Workout Plan ID", SubscribeOperation.this.mWorkoutPlanId);
                Telemetry.logEvent(TelemetryConstants.Events.GuidedWorkoutSubscribe.EVENT_NAME, telemetryProperties, null);
            }

            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationError() {
                SubscribeOperation.this.mNotificationCenter.notifyWorkoutPlanSubscribeError(SubscribeOperation.this.mWorkoutPlanId);
            }

            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationStarted() {
                SubscribeOperation.this.mNotificationCenter.notifyWorkoutPlanSubscribeStarted(SubscribeOperation.this.mWorkoutPlanId);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }
}

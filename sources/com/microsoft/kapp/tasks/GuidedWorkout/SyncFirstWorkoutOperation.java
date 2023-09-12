package com.microsoft.kapp.tasks.GuidedWorkout;

import android.content.Context;
import android.os.AsyncTask;
import com.microsoft.kapp.AsyncTaskOperation;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.utils.LocaleProvider;
import com.microsoft.kapp.utils.SerialAsyncOperation;
import com.microsoft.krestsdk.models.ScheduledWorkout;
import com.microsoft.krestsdk.services.KRestException;
import com.microsoft.krestsdk.services.RestService;
import java.util.List;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class SyncFirstWorkoutOperation implements SerialAsyncOperation {
    private static final String TAG = SyncFirstWorkoutOperation.class.getSimpleName();
    @Inject
    protected Context mContext;
    @Inject
    protected GuidedWorkoutService mGuidedWorkoutService;
    private boolean mIsUserInitiated;
    private SerialAsyncOperation mNextAsyncOperation;
    @Inject
    protected RestService mRestService;
    private ScheduledWorkout mScheduledWorkout;
    private String mWorkoutPlanId;

    public SyncFirstWorkoutOperation(String workoutPlanId, SerialAsyncOperation nextOperation, boolean isUserInitiated) {
        KApplicationGraph.getApplicationGraph().inject(this);
        this.mNextAsyncOperation = nextOperation;
        this.mWorkoutPlanId = workoutPlanId;
        this.mIsUserInitiated = isUserInitiated;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.microsoft.kapp.tasks.GuidedWorkout.SyncFirstWorkoutOperation$1] */
    @Override // com.microsoft.kapp.utils.SerialAsyncOperation
    public void execute() {
        new AsyncTaskOperation(this.mNextAsyncOperation) { // from class: com.microsoft.kapp.tasks.GuidedWorkout.SyncFirstWorkoutOperation.1
            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationSuccess() {
                SyncFirstWorkoutOperation.this.mGuidedWorkoutService.pushGuidedWorkoutToDevice(SyncFirstWorkoutOperation.this.mScheduledWorkout, true, SyncFirstWorkoutOperation.this.mIsUserInitiated);
            }

            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationError() {
            }

            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationStarted() {
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Void doInBackground(Void... params) {
                if (SyncFirstWorkoutOperation.this.mWorkoutPlanId != null) {
                    try {
                        List<ScheduledWorkout> scheduledWorkouts = SyncFirstWorkoutOperation.this.mRestService.getWorkoutPlanSchedules(SyncFirstWorkoutOperation.this.mWorkoutPlanId, LocaleProvider.getLocaleSettings(SyncFirstWorkoutOperation.this.mContext).getLocaleName());
                        SyncFirstWorkoutOperation.this.mScheduledWorkout = scheduledWorkouts.get(0);
                        setOperationResultStatus(1, 0);
                        return null;
                    } catch (KRestException exception) {
                        KLog.e(SyncFirstWorkoutOperation.TAG, "Could not sync the first workout", exception);
                        setOperationResultStatus(2, 1);
                        return null;
                    }
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }
}

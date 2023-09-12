package com.microsoft.kapp.tasks.GuidedWorkout;

import android.os.AsyncTask;
import com.microsoft.kapp.AsyncTaskOperation;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.SyncedWorkoutInfo;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService;
import com.microsoft.kapp.utils.SerialAsyncOperation;
import com.microsoft.krestsdk.models.FavoriteWorkoutPlan;
import com.microsoft.krestsdk.models.GuidedWorkoutEvent;
import com.microsoft.krestsdk.services.RestService;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class FetchGuidedWorkoutDataOperation implements SerialAsyncOperation {
    private static final String TAG = FetchGuidedWorkoutDataOperation.class.getSimpleName();
    private Callback<Void> mCallback;
    @Inject
    protected GuidedWorkoutService mGuidedWorkoutService;
    @Inject
    protected HealthAndFitnessService mHnFService;
    private SerialAsyncOperation mNextAsyncOperation;
    @Inject
    protected RestService mRestService;

    public FetchGuidedWorkoutDataOperation(Callback<Void> callback, SerialAsyncOperation nextAsyncOperation) {
        KApplicationGraph.getApplicationGraph().inject(this);
        this.mNextAsyncOperation = nextAsyncOperation;
        this.mCallback = callback;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.microsoft.kapp.tasks.GuidedWorkout.FetchGuidedWorkoutDataOperation$1] */
    @Override // com.microsoft.kapp.utils.SerialAsyncOperation
    public void execute() {
        new AsyncTaskOperation(this.mNextAsyncOperation) { // from class: com.microsoft.kapp.tasks.GuidedWorkout.FetchGuidedWorkoutDataOperation.1
            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationStarted() {
            }

            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationSuccess() {
                if (FetchGuidedWorkoutDataOperation.this.mCallback != null) {
                    FetchGuidedWorkoutDataOperation.this.mCallback.callback(null);
                }
            }

            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationError() {
                if (FetchGuidedWorkoutDataOperation.this.mCallback != null) {
                    FetchGuidedWorkoutDataOperation.this.mCallback.onError(null);
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Void doInBackground(Void... params) {
                KLog.i(FetchGuidedWorkoutDataOperation.TAG, "GuidedWorkout DataFetcher: Starting");
                boolean resultOk = true;
                try {
                    KLog.i(FetchGuidedWorkoutDataOperation.TAG, "GuidedWorkout DataFetcher: Fetching Subscribed Plan started");
                    FavoriteWorkoutPlan mSubscribedFavoriteWorkoutPlan = FetchGuidedWorkoutDataOperation.this.mRestService.getSubscribedWorkoutPlan();
                    KLog.i(FetchGuidedWorkoutDataOperation.TAG, "GuidedWorkout DataFetcher: Fetching Subscribed Plan completed.");
                    String mSubscribedWorkoutPlanId = mSubscribedFavoriteWorkoutPlan == null ? null : mSubscribedFavoriteWorkoutPlan.getWorkoutPlanId();
                    FetchGuidedWorkoutDataOperation.this.mGuidedWorkoutService.setSubscribedWorkoutPlanId(mSubscribedWorkoutPlanId);
                } catch (Exception ex) {
                    KLog.e(FetchGuidedWorkoutDataOperation.TAG, "GuidedWorkout DataFetcher: Error during SubscribedPlan data fetching!", ex);
                    resultOk = false;
                }
                try {
                    KLog.i(FetchGuidedWorkoutDataOperation.TAG, "GuidedWorkout DataFetcher: Fetching Post GuidedWorkout Details started");
                    GuidedWorkoutEvent mLastGuidedWorkoutEvent = FetchGuidedWorkoutDataOperation.this.mRestService.getPostGuidedWorkoutDetails();
                    FetchGuidedWorkoutDataOperation.this.mGuidedWorkoutService.setLastGuidedWorkoutEvent(mLastGuidedWorkoutEvent);
                    KLog.i(FetchGuidedWorkoutDataOperation.TAG, "GuidedWorkout DataFetcher: Fetching Post GuidedWorkout Details completed");
                } catch (Exception ex2) {
                    KLog.e(FetchGuidedWorkoutDataOperation.TAG, "GuidedWorkout DataFetcher: Error during Post GuidedWorkout Details fetching!", ex2);
                    resultOk = false;
                }
                try {
                    KLog.i(FetchGuidedWorkoutDataOperation.TAG, "GuidedWorkout DataFetcher: Fetching last syncedWorkout started");
                    SyncedWorkoutInfo syncedWorkoutInfo = FetchGuidedWorkoutDataOperation.this.mRestService.getLastSyncedWorkout();
                    FetchGuidedWorkoutDataOperation.this.mGuidedWorkoutService.setLastSyncedWorkout(syncedWorkoutInfo);
                    KLog.i(FetchGuidedWorkoutDataOperation.TAG, "GuidedWorkout DataFetcher: Fetching last syncedWorkout completed.");
                } catch (Exception ex3) {
                    KLog.e(FetchGuidedWorkoutDataOperation.TAG, "GuidedWorkout DataFetcher: Error during last syncedWorkout data fetching!", ex3);
                    resultOk = false;
                }
                KLog.i(FetchGuidedWorkoutDataOperation.TAG, "GuidedWorkout DataFetcher: Completed");
                if (resultOk) {
                    setOperationResultStatus(1, 0);
                } else {
                    setOperationResultStatus(2, 1);
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }
}

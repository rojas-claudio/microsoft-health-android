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
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class AddFavoritesOperation implements SerialAsyncOperation {
    private static final String TAG = AddFavoritesOperation.class.getSimpleName();
    @Inject
    protected GuidedWorkoutService mGuidedWorkoutService;
    private SerialAsyncOperation mNextAsyncOperation;
    private GuidedWorkoutNotificationHandler mNotificationCenter;
    @Inject
    protected RestService mRestService;
    private String mWorkoutPlanId;

    public AddFavoritesOperation(String workoutPlanId, GuidedWorkoutNotificationHandler notificationCenter, SerialAsyncOperation nextOperation) {
        KApplicationGraph.getApplicationGraph().inject(this);
        this.mNextAsyncOperation = nextOperation;
        this.mWorkoutPlanId = workoutPlanId;
        this.mNotificationCenter = notificationCenter;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.microsoft.kapp.tasks.GuidedWorkout.AddFavoritesOperation$1] */
    @Override // com.microsoft.kapp.utils.SerialAsyncOperation
    public void execute() {
        new AsyncTaskOperation(this.mNextAsyncOperation) { // from class: com.microsoft.kapp.tasks.GuidedWorkout.AddFavoritesOperation.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Void doInBackground(Void... params) {
                if (AddFavoritesOperation.this.mWorkoutPlanId != null) {
                    boolean isFavorite = false;
                    try {
                        List<FavoriteWorkoutPlan> favoriteWorkoutPlansList = AddFavoritesOperation.this.mRestService.getFavoriteWorkoutPlans();
                        if (favoriteWorkoutPlansList != null) {
                            Iterator i$ = favoriteWorkoutPlansList.iterator();
                            while (true) {
                                if (!i$.hasNext()) {
                                    break;
                                }
                                FavoriteWorkoutPlan favoriteWorkoutPlan = i$.next();
                                if (favoriteWorkoutPlan != null && TextUtils.equals(favoriteWorkoutPlan.getWorkoutPlanId(), AddFavoritesOperation.this.mWorkoutPlanId)) {
                                    isFavorite = true;
                                    break;
                                }
                            }
                        }
                    } catch (KRestException ex) {
                        KLog.e(AddFavoritesOperation.TAG, "Error when getting favorite workout plans!", ex);
                    }
                    if (!isFavorite) {
                        try {
                            AddFavoritesOperation.this.mRestService.addFavoriteWorkoutPlan(AddFavoritesOperation.this.mWorkoutPlanId);
                            setOperationResultStatus(1, 0);
                            return null;
                        } catch (KRestException ex2) {
                            KLog.e(AddFavoritesOperation.TAG, "Error when favoriting a workout plan!", ex2);
                            setOperationResultStatus(2, 1);
                            return null;
                        }
                    }
                    setOperationResultStatus(1, 0);
                    return null;
                }
                return null;
            }

            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationSuccess() {
                AddFavoritesOperation.this.mNotificationCenter.notifyWorkoutPlanFavorited(AddFavoritesOperation.this.mWorkoutPlanId);
                HashMap<String, String> telemetryProperties = new HashMap<>();
                telemetryProperties.put("Is Favorite", String.valueOf(true));
                telemetryProperties.put("Workout Plan ID", AddFavoritesOperation.this.mWorkoutPlanId);
                Telemetry.logEvent(TelemetryConstants.Events.GuidedWorkoutFavorite.EVENT_NAME, telemetryProperties, null);
            }

            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationError() {
                AddFavoritesOperation.this.mNotificationCenter.notifyWorkoutPlanFavoriteError(AddFavoritesOperation.this.mWorkoutPlanId, getOperationResultCodeId());
            }

            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationStarted() {
                AddFavoritesOperation.this.mNotificationCenter.notifyWorkoutPlanFavoriteStarted(AddFavoritesOperation.this.mWorkoutPlanId);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }
}

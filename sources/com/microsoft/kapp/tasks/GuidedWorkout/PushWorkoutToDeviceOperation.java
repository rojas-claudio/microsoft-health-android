package com.microsoft.kapp.tasks.GuidedWorkout;

import android.content.Context;
import android.os.AsyncTask;
import com.microsoft.kapp.AsyncTaskOperation;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.SyncedWorkoutInfo;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutPlan;
import com.microsoft.kapp.utils.LocaleProvider;
import com.microsoft.kapp.utils.SerialAsyncOperation;
import com.microsoft.krestsdk.models.FavoriteWorkoutPlan;
import com.microsoft.krestsdk.models.ScheduledWorkout;
import com.microsoft.krestsdk.services.KRestException;
import com.microsoft.krestsdk.services.RestService;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class PushWorkoutToDeviceOperation implements SerialAsyncOperation {
    private static final String TAG = PushWorkoutToDeviceOperation.class.getSimpleName();
    @Inject
    protected CargoConnection mCargoConnection;
    @Inject
    protected Context mContext;
    private SyncedWorkoutInfo mCurrentlySyncingWorkoutInfo;
    private Callback<Void> mEndOfOperationCallback;
    @Inject
    protected GuidedWorkoutService mGuidedWorkoutService;
    @Inject
    protected HealthAndFitnessService mHnFService;
    private boolean mIsFirstWorkout;
    private boolean mIsUserInitiated;
    private SerialAsyncOperation mNextAsyncOperation;
    private GuidedWorkoutNotificationHandler mNotificationCenter;
    @Inject
    protected RestService mRestService;
    private ScheduledWorkout mScheduledWorkout;
    @Inject
    protected SettingsProvider mSettingsProvider;
    private int mSyncResult = 0;
    private String mWorkoutName;
    private String mWorkoutPlanName;

    public PushWorkoutToDeviceOperation(ScheduledWorkout scheduledWorkout, GuidedWorkoutNotificationHandler notificationCenter, boolean isFirstWorkout, Callback<Void> endOfOperationCallback, SerialAsyncOperation nextAsyncOperation, boolean isUserInitiated) {
        KApplicationGraph.getApplicationGraph().inject(this);
        this.mScheduledWorkout = scheduledWorkout;
        this.mNotificationCenter = notificationCenter;
        this.mIsFirstWorkout = isFirstWorkout;
        this.mEndOfOperationCallback = endOfOperationCallback;
        this.mNextAsyncOperation = nextAsyncOperation;
        this.mIsUserInitiated = isUserInitiated;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.microsoft.kapp.tasks.GuidedWorkout.PushWorkoutToDeviceOperation$1] */
    @Override // com.microsoft.kapp.utils.SerialAsyncOperation
    public void execute() {
        new AsyncTaskOperation(this.mNextAsyncOperation) { // from class: com.microsoft.kapp.tasks.GuidedWorkout.PushWorkoutToDeviceOperation.1
            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationStarted() {
                PushWorkoutToDeviceOperation.this.mNotificationCenter.notifyWorkoutSyncStarted(PushWorkoutToDeviceOperation.this.mScheduledWorkout);
            }

            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationSuccess() {
                PushWorkoutToDeviceOperation.this.mEndOfOperationCallback.callback(null);
                PushWorkoutToDeviceOperation.this.mNotificationCenter.notifyWorkoutSyncSuccess(PushWorkoutToDeviceOperation.this.mWorkoutPlanName, PushWorkoutToDeviceOperation.this.mWorkoutName, PushWorkoutToDeviceOperation.this.mScheduledWorkout);
            }

            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationError() {
                int errorId;
                PushWorkoutToDeviceOperation.this.mEndOfOperationCallback.onError(null);
                switch (PushWorkoutToDeviceOperation.this.mSyncResult) {
                    case 1:
                        errorId = 3;
                        break;
                    case 2:
                    default:
                        errorId = 0;
                        break;
                    case 3:
                        errorId = 4;
                        break;
                }
                PushWorkoutToDeviceOperation.this.mNotificationCenter.notifyWorkoutSyncError(PushWorkoutToDeviceOperation.this.mScheduledWorkout, errorId, PushWorkoutToDeviceOperation.this.mIsFirstWorkout, PushWorkoutToDeviceOperation.this.mIsUserInitiated);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Void doInBackground(Void... params) {
                String currentSubscribedWorkoutPlanId = PushWorkoutToDeviceOperation.this.mGuidedWorkoutService.getSubscribedWorkoutPlanId();
                String workoutPlanId = PushWorkoutToDeviceOperation.this.mScheduledWorkout.getWorkoutPlanId();
                if (workoutPlanId == null) {
                    PushWorkoutToDeviceOperation.this.mSyncResult = 0;
                    return null;
                }
                if (currentSubscribedWorkoutPlanId != null) {
                    boolean isSubscribed = currentSubscribedWorkoutPlanId.equals(workoutPlanId);
                    if (!isSubscribed) {
                        PushWorkoutToDeviceOperation.this.mGuidedWorkoutService.updateManualSync(PushWorkoutToDeviceOperation.this.mScheduledWorkout);
                    }
                }
                int workoutPlanInstanceId = 0;
                boolean isFavorite = false;
                try {
                    List<FavoriteWorkoutPlan> favoritesList = PushWorkoutToDeviceOperation.this.mRestService.getFavoriteWorkoutPlans();
                    if (favoritesList != null) {
                        Iterator i$ = favoritesList.iterator();
                        while (true) {
                            if (!i$.hasNext()) {
                                break;
                            }
                            FavoriteWorkoutPlan favoriteWorkoutPlan = i$.next();
                            if (favoriteWorkoutPlan != null && workoutPlanId.equals(favoriteWorkoutPlan.getWorkoutPlanId())) {
                                isFavorite = true;
                                workoutPlanInstanceId = favoriteWorkoutPlan.getInstanceId();
                                break;
                            }
                        }
                    }
                    if (!isFavorite) {
                        KLog.e(PushWorkoutToDeviceOperation.TAG, "Abort workout sync to the band: workout %s is not favorite!", workoutPlanId);
                        PushWorkoutToDeviceOperation.this.mSyncResult = 3;
                        return null;
                    }
                    int workoutIndex = PushWorkoutToDeviceOperation.this.mScheduledWorkout.getWorkoutIndex();
                    int day = PushWorkoutToDeviceOperation.this.mScheduledWorkout.getDay();
                    int week = PushWorkoutToDeviceOperation.this.mScheduledWorkout.getWeekId();
                    String locale = LocaleProvider.getLocaleSettings(PushWorkoutToDeviceOperation.this.mContext).getLocaleName();
                    PushWorkoutToDeviceOperation.this.mCurrentlySyncingWorkoutInfo = new SyncedWorkoutInfo(workoutPlanId, workoutPlanInstanceId, day, week, workoutIndex, DateTime.now());
                    byte[] workout = PushWorkoutToDeviceOperation.this.mRestService.getDeviceWorkout(workoutPlanId, workoutPlanInstanceId, workoutIndex, day, week, locale);
                    PushWorkoutToDeviceOperation.this.mSyncResult = PushWorkoutToDeviceOperation.this.mCargoConnection.pushWorkoutData(workout);
                    if (PushWorkoutToDeviceOperation.this.mSyncResult == 2) {
                        PushWorkoutToDeviceOperation.this.mRestService.updateLastSyncedWorkout(workoutPlanId, workoutPlanInstanceId, workoutIndex, day, week);
                        PushWorkoutToDeviceOperation.this.mCurrentlySyncingWorkoutInfo.setTimeSynced(DateTime.now());
                        PushWorkoutToDeviceOperation.this.mGuidedWorkoutService.setLastSyncedWorkout(PushWorkoutToDeviceOperation.this.mCurrentlySyncingWorkoutInfo);
                        PushWorkoutToDeviceOperation.this.mSettingsProvider.setResyncLastWorkoutNextSync(false);
                        try {
                            WorkoutPlan syncedWorkoutPlan = PushWorkoutToDeviceOperation.this.mHnFService.getHnFWorkoutPlanDetails(workoutPlanId);
                            PushWorkoutToDeviceOperation.this.mWorkoutPlanName = syncedWorkoutPlan.getName();
                            PushWorkoutToDeviceOperation.this.mWorkoutName = syncedWorkoutPlan.getSteps()[0].getName();
                        } catch (Exception exception) {
                            KLog.e(PushWorkoutToDeviceOperation.TAG, "Error during the fetching of the name of the synced plan and workout!", exception);
                            setOperationResultStatus(2, 1);
                        }
                        setOperationResultStatus(1, 0);
                        HashMap<String, String> telemetryProperties = new HashMap<>();
                        telemetryProperties.put("Is Favorite", String.valueOf(isFavorite));
                        telemetryProperties.put("Workout Plan ID", workoutPlanId);
                        Telemetry.logEvent(TelemetryConstants.Events.GuidedWorkoutSync.EVENT_NAME, telemetryProperties, null);
                    }
                    return null;
                } catch (KRestException exception2) {
                    KLog.e(PushWorkoutToDeviceOperation.TAG, "Unknown Error during Workout Sync!", exception2);
                    PushWorkoutToDeviceOperation.this.mSyncResult = 3;
                    setOperationResultStatus(2, 1);
                    return null;
                } catch (Exception exception3) {
                    KLog.e(PushWorkoutToDeviceOperation.TAG, "Unknown Error during Workout Sync!", exception3);
                    PushWorkoutToDeviceOperation.this.mSyncResult = 0;
                    setOperationResultStatus(2, 1);
                    return null;
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }
}

package com.microsoft.kapp.services.guidedworkout;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.LoadStatus;
import com.microsoft.kapp.models.SyncedWorkoutInfo;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService;
import com.microsoft.kapp.services.healthandfitness.WorkoutProviderType;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutPlan;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutsResponseEnvelope;
import com.microsoft.kapp.tasks.GuidedWorkout.AddFavoritesOperation;
import com.microsoft.kapp.tasks.GuidedWorkout.CalculateNextWorkoutOperation;
import com.microsoft.kapp.tasks.GuidedWorkout.FetchGuidedWorkoutDataOperation;
import com.microsoft.kapp.tasks.GuidedWorkout.PushWorkoutToDeviceOperation;
import com.microsoft.kapp.tasks.GuidedWorkout.RemoveFavoritesOperation;
import com.microsoft.kapp.tasks.GuidedWorkout.SubscribeOperation;
import com.microsoft.kapp.tasks.GuidedWorkout.UnsubscribeOperation;
import com.microsoft.kapp.utils.MultipleRequestManager;
import com.microsoft.krestsdk.models.FavoriteWorkoutPlan;
import com.microsoft.krestsdk.models.FeaturedWorkout;
import com.microsoft.krestsdk.models.GuidedWorkoutEvent;
import com.microsoft.krestsdk.models.ScheduledWorkout;
import com.microsoft.krestsdk.services.RestService;
import java.util.List;
import java.util.UUID;
/* loaded from: classes.dex */
public class GuidedWorkoutServiceImpl implements GuidedWorkoutService {
    private static final String TAG = GuidedWorkoutServiceImpl.class.getSimpleName();
    private volatile ScheduledWorkout mCurrentlySyncingWorkout;
    private HealthAndFitnessService mHnFService;
    private volatile boolean mIsRestDay;
    private volatile GuidedWorkoutEvent mLastGuidedWorkoutEvent;
    private volatile SyncedWorkoutInfo mLastSyncedWorkoutInfo;
    private volatile String mNextGuidedWorkoutStepName;
    private volatile String mNextGuidedWorkoutStepPlanName;
    private volatile ScheduledWorkout mNextGuidedWorkoutStepSchedule;
    private RestService mRestService;
    private SettingsProvider mSettingsProvider;
    private volatile String mSubscribedWorkoutPlanId;
    private volatile Boolean mIsSyncingWorkout = false;
    private final Callback<Void> mUnlockCallback = new Callback<Void>() { // from class: com.microsoft.kapp.services.guidedworkout.GuidedWorkoutServiceImpl.1
        @Override // com.microsoft.kapp.Callback
        public void onError(Exception ex) {
            GuidedWorkoutServiceImpl.this.setCurrentlySyncingWorkout(null);
            GuidedWorkoutServiceImpl.this.unlockGWSyncLock();
        }

        @Override // com.microsoft.kapp.Callback
        public void callback(Void result) {
            GuidedWorkoutServiceImpl.this.setCurrentlySyncingWorkout(null);
            GuidedWorkoutServiceImpl.this.unlockGWSyncLock();
        }
    };
    private GuidedWorkoutNotificationHandlerImpl mNotificationCenter = new GuidedWorkoutNotificationHandlerImpl(this);

    public GuidedWorkoutServiceImpl(SettingsProvider settingsProvider, RestService restService, HealthAndFitnessService healthAndFitnessService) {
        this.mSettingsProvider = settingsProvider;
        this.mRestService = restService;
        this.mHnFService = healthAndFitnessService;
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void pushGuidedWorkoutToDevice(ScheduledWorkout scheduledWorkout) {
        startPushWorkoutTask(scheduledWorkout, false, true);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void pushGuidedWorkoutToDevice(ScheduledWorkout scheduledWorkout, boolean isFirstWorkout, boolean isUserInitiated) {
        startPushWorkoutTask(scheduledWorkout, isFirstWorkout, isUserInitiated);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void updateManualSync(ScheduledWorkout scheduledWorkout) {
        try {
            this.mRestService.updateLastSyncedWorkoutManual(scheduledWorkout.getWorkoutPlanId(), scheduledWorkout.getWorkoutIndex(), scheduledWorkout.getDay(), scheduledWorkout.getWeekId());
        } catch (Exception ex) {
            KLog.w(TAG, "Error when updating the last sync for a one-off guided workout!", ex);
        }
    }

    public boolean startPushWorkoutTask(ScheduledWorkout scheduledWorkout, boolean isFirstWorkout, boolean isUserInitiated) {
        boolean z;
        if (scheduledWorkout == null || scheduledWorkout.getWorkoutPlanId() == null) {
            KLog.w(TAG, "cannot push a null scheduledWorkout to the device!");
            return false;
        }
        String workoutPlanId = scheduledWorkout.getWorkoutPlanId();
        if (workoutPlanId == null) {
            KLog.w(TAG, "cannot push a scheduledWorkout with a null workoutPlanId to the device!");
            return false;
        }
        synchronized (this.mIsSyncingWorkout) {
            if (this.mIsSyncingWorkout.booleanValue()) {
                z = false;
            } else {
                this.mIsSyncingWorkout = true;
                List<UUID> uuid = this.mSettingsProvider.getUUIDsOnDevice();
                boolean isWorkoutStrappDisabled = uuid == null || !uuid.contains(DefaultStrappUUID.STRAPP_GUIDED_WORKOUTS);
                if (isWorkoutStrappDisabled) {
                    this.mNotificationCenter.notifyWorkoutSyncError(scheduledWorkout, 1, isFirstWorkout, isUserInitiated);
                    this.mIsSyncingWorkout = false;
                    z = false;
                } else {
                    boolean isInNoDevicePairedMode = this.mSettingsProvider.isInNoDevicePairedMode();
                    if (isInNoDevicePairedMode) {
                        this.mNotificationCenter.notifyWorkoutSyncError(scheduledWorkout, 2, isFirstWorkout, isUserInitiated);
                        this.mIsSyncingWorkout = false;
                        z = false;
                    } else {
                        setCurrentlySyncingWorkout(scheduledWorkout);
                        new AddFavoritesOperation(workoutPlanId, this.mNotificationCenter, new PushWorkoutToDeviceOperation(scheduledWorkout, this.mNotificationCenter, isFirstWorkout, this.mUnlockCallback, null, isUserInitiated)).execute();
                        z = true;
                    }
                }
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unlockGWSyncLock() {
        synchronized (this.mIsSyncingWorkout) {
            if (this.mIsSyncingWorkout.booleanValue()) {
                this.mIsSyncingWorkout = false;
            }
        }
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public ScheduledWorkout getCurrentlySyncingWorkout() {
        return this.mCurrentlySyncingWorkout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCurrentlySyncingWorkout(ScheduledWorkout scheduledWorkout) {
        this.mCurrentlySyncingWorkout = scheduledWorkout;
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public SyncedWorkoutInfo getLastSyncedWorkout() {
        return this.mLastSyncedWorkoutInfo;
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void setLastSyncedWorkout(SyncedWorkoutInfo lastSyncedWorkoutInfo) {
        this.mLastSyncedWorkoutInfo = lastSyncedWorkoutInfo;
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void subscribeToWorkoutPlan(String workoutPlanId) {
        if (workoutPlanId == null) {
            KLog.e(TAG, "cannot subscribe to a null workoutPlanId!");
            return;
        }
        KLog.i(TAG, "Trying to subscribe to GW plan %s", workoutPlanId);
        this.mNotificationCenter.notifyWorkoutPlanSubscribeStarted(workoutPlanId);
        new UnsubscribeOperation(null, this.mNotificationCenter, new AddFavoritesOperation(workoutPlanId, this.mNotificationCenter, new SubscribeOperation(workoutPlanId, this.mNotificationCenter, null))).execute();
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void unsubscribeFromWorkoutPlan(String workoutPlanId) {
        KLog.i(TAG, "Trying to unsubscribe to GW plan %s");
        new UnsubscribeOperation(workoutPlanId, this.mNotificationCenter, null).execute();
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void favoriteWorkoutPlan(String workoutPlanId) {
        KLog.i(TAG, "Trying to favorite the GW plan %s", workoutPlanId);
        new AddFavoritesOperation(workoutPlanId, this.mNotificationCenter, null).execute();
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void unfavoriteWorkoutPlan(String workoutPlanId) {
        KLog.i(TAG, "Trying to unfavorite the GW plan %s", workoutPlanId);
        new RemoveFavoritesOperation(workoutPlanId, this.mNotificationCenter, null).execute();
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void fetchGuidedWorkoutData(final Callback<Void> callback, boolean isUserInitiated) {
        final MultipleRequestManager multipleRequestManager = new MultipleRequestManager(2, new MultipleRequestManager.OnRequestCompleteListener() { // from class: com.microsoft.kapp.services.guidedworkout.GuidedWorkoutServiceImpl.2
            @Override // com.microsoft.kapp.utils.MultipleRequestManager.OnRequestCompleteListener
            public void requestComplete(LoadStatus status) {
                if (status == LoadStatus.LOADED) {
                    callback.callback(null);
                } else {
                    callback.onError(null);
                }
            }
        });
        Callback<Void> endOfOperationsCallBack = new Callback<Void>() { // from class: com.microsoft.kapp.services.guidedworkout.GuidedWorkoutServiceImpl.3
            @Override // com.microsoft.kapp.Callback
            public void callback(Void result) {
                multipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                multipleRequestManager.notifyRequestFailed();
            }
        };
        new FetchGuidedWorkoutDataOperation(endOfOperationsCallBack, new CalculateNextWorkoutOperation(endOfOperationsCallBack, this.mNotificationCenter, null, isUserInitiated)).execute();
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void setNextGuidedWorkoutInfo(String planName, String workoutName, ScheduledWorkout scheduledWorkout) {
        this.mNextGuidedWorkoutStepPlanName = planName;
        this.mNextGuidedWorkoutStepName = workoutName;
        this.mNextGuidedWorkoutStepSchedule = scheduledWorkout;
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public ScheduledWorkout getNextGuidedWorkoutStepSchedule() {
        return this.mNextGuidedWorkoutStepSchedule;
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public boolean isRestDay() {
        return this.mIsRestDay;
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void setRestDay(boolean isRestDay) {
        this.mIsRestDay = isRestDay;
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public String getSubscribedWorkoutPlanId() {
        return this.mSubscribedWorkoutPlanId;
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void setSubscribedWorkoutPlanId(String subscribedWorkoutPlanId) {
        this.mSubscribedWorkoutPlanId = subscribedWorkoutPlanId;
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public GuidedWorkoutEvent getGuidedWorkoutevent() {
        return this.mLastGuidedWorkoutEvent;
    }

    public void setGuidedWorkoutevent(GuidedWorkoutEvent guidedWorkoutevent) {
        this.mLastGuidedWorkoutEvent = guidedWorkoutevent;
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void getFavoriteWorkoutPlans(Callback<List<FavoriteWorkoutPlan>> callback) {
        this.mRestService.getFavoriteWorkoutPlans(callback);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void getLastSyncedWorkout(Callback<SyncedWorkoutInfo> callback) {
        this.mRestService.getLastSyncedWorkout(callback);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void skipWorkout(String workoutPlanId, int workoutIndex, int day, int week, Callback<Void> callback) {
        this.mRestService.skipWorkout(workoutPlanId, workoutIndex, day, week, callback);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void getFeaturedWorkouts(int age, String gender, Callback<List<FeaturedWorkout>> callback) {
        this.mRestService.getFeaturedWorkouts(age, gender, callback);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void getGuidedWorkoutEventById(String eventId, boolean hasSequences, Callback<GuidedWorkoutEvent> callback) {
        this.mRestService.getGuidedWorkoutEventById(eventId, hasSequences, callback);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void getHnFStrengthWorkoutPlans(WorkoutProviderType providerType, Callback<WorkoutsResponseEnvelope> callback) {
        this.mHnFService.getHnFStrengthWorkoutPlans(providerType, callback);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void getHnFStrengthWorkoutPlans(String filterString, WorkoutProviderType providerType, Callback<WorkoutsResponseEnvelope> callback) {
        this.mHnFService.getHnFStrengthWorkoutPlans(filterString, providerType, callback);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void getHnFWorkoutPlanDetails(String workoutId, Callback<WorkoutPlan> callback) {
        this.mHnFService.getHnFWorkoutPlanDetails(workoutId, callback);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void getWorkoutPlanSchedules(String workoutPlanId, Callback<List<ScheduledWorkout>> callback) {
        this.mRestService.getWorkoutPlanSchedules(workoutPlanId, callback);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void getHnFStrengthWorkoutPlans(String workoutName, String filterString, WorkoutProviderType providerType, Callback<WorkoutsResponseEnvelope> callback) {
        this.mHnFService.getHnFStrengthWorkoutPlans(workoutName, filterString, providerType, callback);
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public String getNextGuidedWorkoutStepName() {
        return this.mNextGuidedWorkoutStepName;
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public String getNextGuidedWorkoutStepPlanName() {
        return this.mNextGuidedWorkoutStepPlanName;
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void deleteLastGuidedWorkoutEventLocally() {
        this.mLastGuidedWorkoutEvent = null;
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void setLastGuidedWorkoutEvent(GuidedWorkoutEvent lastGuidedWorkoutEvent) {
        this.mLastGuidedWorkoutEvent = lastGuidedWorkoutEvent;
    }

    @Override // com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService
    public void clearAllData() {
        this.mCurrentlySyncingWorkout = null;
        this.mLastSyncedWorkoutInfo = null;
        this.mIsSyncingWorkout = false;
        this.mSubscribedWorkoutPlanId = null;
        this.mLastGuidedWorkoutEvent = null;
        this.mNextGuidedWorkoutStepName = null;
        this.mNextGuidedWorkoutStepPlanName = null;
        this.mNextGuidedWorkoutStepSchedule = null;
        this.mIsRestDay = false;
    }
}

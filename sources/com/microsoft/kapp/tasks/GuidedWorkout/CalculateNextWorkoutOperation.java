package com.microsoft.kapp.tasks.GuidedWorkout;

import android.os.AsyncTask;
import com.microsoft.kapp.AsyncTaskOperation;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutPlan;
import com.microsoft.kapp.utils.SerialAsyncOperation;
import com.microsoft.krestsdk.models.CurrentGuidedWorkout;
import com.microsoft.krestsdk.models.ScheduledWorkout;
import com.microsoft.krestsdk.models.WorkoutState;
import com.microsoft.krestsdk.services.RestService;
import java.util.List;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class CalculateNextWorkoutOperation implements SerialAsyncOperation {
    private static final String TAG = CalculateNextWorkoutOperation.class.getSimpleName();
    private Callback<Void> mCallback;
    @Inject
    protected GuidedWorkoutService mGuidedWorkoutService;
    @Inject
    protected HealthAndFitnessService mHnFService;
    private boolean mIsUserInitiated;
    @Inject
    protected MultiDeviceManager mMultiDeviceManager;
    private SerialAsyncOperation mNextAsyncOperation;
    private GuidedWorkoutNotificationHandler mNotificationCenter;
    @Inject
    protected RestService mRestService;
    @Inject
    protected SettingsProvider mSettingsProvider;

    public CalculateNextWorkoutOperation(Callback<Void> callback, GuidedWorkoutNotificationHandler notificationCenter, SerialAsyncOperation nextOperation, boolean isUserInitiated) {
        KApplicationGraph.getApplicationGraph().inject(this);
        this.mNextAsyncOperation = nextOperation;
        this.mCallback = callback;
        this.mNotificationCenter = notificationCenter;
        this.mIsUserInitiated = isUserInitiated;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.microsoft.kapp.tasks.GuidedWorkout.CalculateNextWorkoutOperation$1] */
    @Override // com.microsoft.kapp.utils.SerialAsyncOperation
    public void execute() {
        new AsyncTaskOperation(this.mNextAsyncOperation) { // from class: com.microsoft.kapp.tasks.GuidedWorkout.CalculateNextWorkoutOperation.1
            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationStarted() {
            }

            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationSuccess() {
                if (CalculateNextWorkoutOperation.this.mCallback != null) {
                    CalculateNextWorkoutOperation.this.mCallback.callback(null);
                }
                if (CalculateNextWorkoutOperation.this.mNotificationCenter != null) {
                    CalculateNextWorkoutOperation.this.mNotificationCenter.notifyNextCalendarWorkoutUpdated();
                }
            }

            @Override // com.microsoft.kapp.AsyncTaskOperation
            protected void onOperationError() {
                if (CalculateNextWorkoutOperation.this.mCallback != null) {
                    CalculateNextWorkoutOperation.this.mCallback.onError(null);
                }
            }

            private void updateNextWorkout(ScheduledWorkout scheduledWorkout, boolean isRestDay) {
                try {
                    int day = scheduledWorkout.getWorkoutIndex();
                    WorkoutPlan subscribedWorkoutPlan = CalculateNextWorkoutOperation.this.mHnFService.getHnFWorkoutPlanDetails(scheduledWorkout.getWorkoutPlanId());
                    if (day >= subscribedWorkoutPlan.getSteps().length) {
                        KLog.e(CalculateNextWorkoutOperation.TAG, "Error when calculating the next workout in GW calendar! incorrect workout index, out of steps bounds");
                        setOperationResultStatus(2, 1);
                    } else {
                        String nextWorkoutStepName = subscribedWorkoutPlan.getSteps()[day].getName();
                        List<ScheduledWorkout> subscribedPlanSchedule = CalculateNextWorkoutOperation.this.mRestService.getWorkoutPlanSchedules(scheduledWorkout.getWorkoutPlanId());
                        CalculateNextWorkoutOperation.this.mGuidedWorkoutService.setNextGuidedWorkoutInfo(subscribedWorkoutPlan.getName(), nextWorkoutStepName, subscribedPlanSchedule.get(day));
                        CalculateNextWorkoutOperation.this.mGuidedWorkoutService.setRestDay(isRestDay);
                        setOperationResultStatus(1, 0);
                    }
                } catch (Exception ex) {
                    KLog.e(CalculateNextWorkoutOperation.TAG, "Error when calculating the next workout in GW calendar!", ex);
                    setOperationResultStatus(2, 1);
                }
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public Void doInBackground(Void... params) {
                try {
                    CurrentGuidedWorkout currentGuidedWorkout = CalculateNextWorkoutOperation.this.mRestService.getCurrentGuidedWorkout();
                    WorkoutState stateEnum = currentGuidedWorkout.getState();
                    switch (AnonymousClass2.$SwitchMap$com$microsoft$krestsdk$models$WorkoutState[stateEnum.ordinal()]) {
                        case 1:
                            if (!CalculateNextWorkoutOperation.this.mSettingsProvider.getResyncLastWorkoutNextSync()) {
                                updateNextWorkout(currentGuidedWorkout.getWorkoutInfo(), false);
                                break;
                            }
                        case 2:
                            if (CalculateNextWorkoutOperation.this.mMultiDeviceManager.hasBand()) {
                                int day = currentGuidedWorkout.getWorkoutInfo().getWorkoutIndex();
                                if (day == 0) {
                                    CalculateNextWorkoutOperation.this.mGuidedWorkoutService.pushGuidedWorkoutToDevice(currentGuidedWorkout.getWorkoutInfo(), true, CalculateNextWorkoutOperation.this.mIsUserInitiated);
                                } else {
                                    CalculateNextWorkoutOperation.this.mGuidedWorkoutService.pushGuidedWorkoutToDevice(currentGuidedWorkout.getWorkoutInfo());
                                }
                            }
                            updateNextWorkout(currentGuidedWorkout.getWorkoutInfo(), false);
                            break;
                        case 3:
                            updateNextWorkout(currentGuidedWorkout.getWorkoutInfo(), true);
                            break;
                        case 4:
                            CalculateNextWorkoutOperation.this.mGuidedWorkoutService.setNextGuidedWorkoutInfo(null, null, null);
                            CalculateNextWorkoutOperation.this.mGuidedWorkoutService.setRestDay(false);
                            setOperationResultStatus(1, 0);
                            break;
                        case 5:
                            CalculateNextWorkoutOperation.this.mGuidedWorkoutService.setNextGuidedWorkoutInfo(null, null, null);
                            CalculateNextWorkoutOperation.this.mGuidedWorkoutService.setRestDay(false);
                            KLog.e(CalculateNextWorkoutOperation.TAG, "Error when calculating the next workout in GW calendar! Invalid state response");
                            setOperationResultStatus(2, 1);
                            break;
                    }
                } catch (Exception ex) {
                    KLog.e(CalculateNextWorkoutOperation.TAG, "Error when calculating the next workout in GW calendar!", ex);
                    setOperationResultStatus(2, 1);
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.microsoft.kapp.tasks.GuidedWorkout.CalculateNextWorkoutOperation$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$microsoft$krestsdk$models$WorkoutState = new int[WorkoutState.values().length];

        static {
            try {
                $SwitchMap$com$microsoft$krestsdk$models$WorkoutState[WorkoutState.OnBand.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$microsoft$krestsdk$models$WorkoutState[WorkoutState.SyncRequired.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$microsoft$krestsdk$models$WorkoutState[WorkoutState.RestDay.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$microsoft$krestsdk$models$WorkoutState[WorkoutState.NoWorkout.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$microsoft$krestsdk$models$WorkoutState[WorkoutState.Unknown.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }
}

package com.microsoft.kapp.fragments.guidedworkout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.activities.HomeActivity;
import com.microsoft.kapp.activities.WorkoutDetailActivity;
import com.microsoft.kapp.adapters.WorkoutScheduleItemAdapter;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport;
import com.microsoft.kapp.fragments.ManageTilesFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.SyncedWorkoutInfo;
import com.microsoft.kapp.models.WorkoutScheduleItem;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutPlan;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.GuidedWorkoutUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.krestsdk.models.ScheduledWorkout;
import com.microsoft.krestsdk.models.UserWorkoutStatus;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class WorkoutPlanScheduleFragmentV1 extends BaseFragmentWithOfflineSupport implements GuidedWorkoutBroadcastListener {
    private static final String MODE_HOME_TILE = "home_tile_mode";
    private static final String STATE_WORKOUT_PLAN = "workout_plan";
    private static final String STATE_WORKOUT_SCHEDULE = "workout_schedule";
    private WorkoutScheduleItemAdapter mAdapter;
    private List<WorkoutScheduleItem> mAllWorkoutItems;
    private String mCurrentWorkoutPlanId;
    private GuidedWorkoutBroadcastReceiver mGuidedWorkoutBroadcastReceiver;
    @Inject
    GuidedWorkoutService mGuidedWorkoutService;
    private boolean mIsHomeTileMode;
    private boolean mIsPlanSingleWorkout;
    private boolean mIsPlanSubscriptionDisabled;
    private ViewGroup mMakeThisYourPlanLayout;
    private List<ScheduledWorkout> mPlanSchedule;
    private ViewGroup mPlanWorkoutSubscriptionBanner;
    private ExpandableListView mScheduleList;
    @Inject
    SettingsProvider mSettingsProvider;
    private int mTotalWeeks;
    private ProgressBar mWorkoutOperationProgress;
    private WorkoutPlan mWorkoutPlan;
    private TextView mWorkoutSubscribeDownload;
    private ViewGroup mYourCurrentPlanLayout;

    public static BaseFragment newInstance(WorkoutPlan workoutPlan, List<ScheduledWorkout> planSchedule, boolean isHomeTileMode, boolean isPlanSubscriptionDisabled) {
        Validate.notNull(workoutPlan, "workoutPlan");
        Validate.notNull(planSchedule, "planSchedule");
        WorkoutPlanScheduleFragmentV1 fragment = new WorkoutPlanScheduleFragmentV1();
        Bundle bundle = new Bundle();
        bundle.putSerializable(STATE_WORKOUT_PLAN, workoutPlan);
        bundle.putParcelableArrayList(STATE_WORKOUT_SCHEDULE, new ArrayList<>(planSchedule));
        bundle.putBoolean(MODE_HOME_TILE, isHomeTileMode);
        bundle.putBoolean(Constants.KEY_GUIDED_WORKOUT_PLAN_SUBSCRIPTION_STATE, isPlanSubscriptionDisabled);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle savedBundle = savedInstanceState == null ? getArguments() : savedInstanceState;
        Validate.notNull(savedBundle, "savedBundle");
        this.mWorkoutPlan = (WorkoutPlan) savedBundle.getSerializable(STATE_WORKOUT_PLAN);
        this.mPlanSchedule = savedBundle.getParcelableArrayList(STATE_WORKOUT_SCHEDULE);
        this.mIsHomeTileMode = savedBundle.getBoolean(MODE_HOME_TILE);
        this.mIsPlanSubscriptionDisabled = savedBundle.getBoolean(Constants.KEY_GUIDED_WORKOUT_PLAN_SUBSCRIPTION_STATE, false);
        Validate.notNull(this.mWorkoutPlan, "mWorkoutPlan");
        Validate.notNull(this.mPlanSchedule, "mPlanSchedule");
        this.mCurrentWorkoutPlanId = this.mWorkoutPlan.getId();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_GUIDED_WORKOUTS_PLAN_SCHEDULE);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_WORKOUT_PLAN, this.mWorkoutPlan);
        outState.putBoolean(MODE_HOME_TILE, this.mIsHomeTileMode);
        outState.putParcelableArrayList(STATE_WORKOUT_SCHEDULE, (ArrayList) this.mPlanSchedule);
        outState.putBoolean(Constants.KEY_GUIDED_WORKOUT_PLAN_SUBSCRIPTION_STATE, this.mIsPlanSubscriptionDisabled);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.workout_plan_schedule_fragment_v1, container, false);
        this.mScheduleList = (ExpandableListView) ViewUtils.getValidView(rootView, R.id.workout_schedule_list, ExpandableListView.class);
        this.mPlanWorkoutSubscriptionBanner = (ViewGroup) ViewUtils.getValidView(rootView, R.id.this_is_current_plan, ViewGroup.class);
        this.mYourCurrentPlanLayout = (ViewGroup) ViewUtils.getValidView(this.mPlanWorkoutSubscriptionBanner, R.id.your_current_plan_layout, ViewGroup.class);
        this.mMakeThisYourPlanLayout = (ViewGroup) ViewUtils.getValidView(this.mPlanWorkoutSubscriptionBanner, R.id.make_this_your_plan_layout, ViewGroup.class);
        this.mWorkoutSubscribeDownload = (TextView) ViewUtils.getValidView(this.mMakeThisYourPlanLayout, R.id.glyph_download_circle, TextView.class);
        this.mWorkoutOperationProgress = (ProgressBar) ViewUtils.getValidView(this.mMakeThisYourPlanLayout, R.id.workout_subscribe_progress, ProgressBar.class);
        if (this.mIsPlanSubscriptionDisabled) {
            this.mPlanWorkoutSubscriptionBanner.setVisibility(8);
        }
        if (this.mIsPlanSingleWorkout) {
            setOnClickDownloadThisSingleWorkoutBanner(this.mPlanSchedule.get(0));
        } else {
            setOnClickSubscribeToThisPlanBanner();
        }
        updateSubscribedStatusLocally();
        loadInitialSchedule();
        this.mGuidedWorkoutBroadcastReceiver = new GuidedWorkoutBroadcastReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(GuidedWorkoutNotificationHandler.OPERATION_SUBSCRIBE);
        filter.addAction(GuidedWorkoutNotificationHandler.OPERATION_UNSUBSCRIBE);
        filter.addAction(GuidedWorkoutNotificationHandler.OPERATION_SYNC);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.mGuidedWorkoutBroadcastReceiver, filter);
        return rootView;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this.mGuidedWorkoutBroadcastReceiver);
    }

    private void loadInitialSchedule() {
        if (this.mWorkoutPlan != null && this.mPlanSchedule != null && this.mAdapter == null) {
            populateScheduleItems(this.mWorkoutPlan, this.mPlanSchedule);
        }
        updateCurrentSyncingWorkout();
    }

    private void updateCurrentSyncingWorkout() {
        ScheduledWorkout currentScheduledWorkout;
        ScheduledWorkout currentlySyncingWorkout = this.mGuidedWorkoutService.getCurrentlySyncingWorkout();
        if (currentlySyncingWorkout != null && this.mCurrentWorkoutPlanId != null && this.mCurrentWorkoutPlanId.equals(currentlySyncingWorkout.getWorkoutPlanId())) {
            Iterator i$ = this.mAllWorkoutItems.iterator();
            while (true) {
                if (!i$.hasNext()) {
                    break;
                }
                WorkoutScheduleItem currentWorkout = i$.next();
                if (currentWorkout != null && (currentScheduledWorkout = currentWorkout.getWorkout()) != null && currentScheduledWorkout.getWeekId() == currentlySyncingWorkout.getWeekId() && currentScheduledWorkout.getDay() == currentlySyncingWorkout.getDay() && currentScheduledWorkout.getWorkoutIndex() == currentlySyncingWorkout.getWorkoutIndex()) {
                    currentWorkout.setSyncStatus(null);
                    break;
                }
            }
            this.mAdapter.notifyDataSetChanged();
        }
    }

    private void populateScheduleItems(WorkoutPlan workoutPlan, List<ScheduledWorkout> workouts) {
        ScheduledWorkout lastScheduledWorkout;
        if (workoutPlan != null && workouts != null) {
            this.mWorkoutPlan = workoutPlan;
            String subscribedWorkoutPlanId = this.mGuidedWorkoutService.getSubscribedWorkoutPlanId();
            this.mAllWorkoutItems = new ArrayList();
            int dayIndex = 0;
            int workoutIndex = 0;
            while (workoutIndex < workouts.size()) {
                ScheduledWorkout workout = workouts.get(workoutIndex);
                if (workout == null) {
                    KLog.w(this.TAG, "ScheduledWorkout shouldn't be null!");
                    workoutIndex++;
                } else {
                    final int currentDayIndex = dayIndex;
                    int currentWorkoutDayIndex = (((workout.getWeekId() - 1) * 7) + workout.getDay()) - 1;
                    if (currentWorkoutDayIndex <= dayIndex) {
                        WorkoutScheduleItem.OnWorkoutToggledListener toggledListener = new WorkoutScheduleItem.OnWorkoutToggledListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanScheduleFragmentV1.1
                            @Override // com.microsoft.kapp.models.WorkoutScheduleItem.OnWorkoutToggledListener
                            public void onWorkoutToggled(final WorkoutScheduleItem toggledWorkout, boolean checked) {
                                if (toggledWorkout != null) {
                                    if (WorkoutPlanScheduleFragmentV1.this.mGuidedWorkoutService.getCurrentlySyncingWorkout() != null) {
                                        WorkoutPlanScheduleFragmentV1.this.getDialogManager().showDialog(WorkoutPlanScheduleFragmentV1.this.getActivity(), Integer.valueOf((int) R.string.app_name), Integer.valueOf((int) R.string.guided_workout_error_currently_syncing), DialogPriority.LOW);
                                        toggledWorkout.setSyncStatus(false);
                                    } else if (!WorkoutPlanScheduleFragmentV1.this.mSettingsProvider.getUUIDsOnDevice().contains(DefaultStrappUUID.STRAPP_GUIDED_WORKOUTS)) {
                                        WorkoutPlanScheduleFragmentV1.this.getDialogManager().showDialog(WorkoutPlanScheduleFragmentV1.this.getActivity(), Integer.valueOf((int) R.string.connection_with_device_error_title), Integer.valueOf((int) R.string.guided_workout_sync_tile_disabled_error), R.string.guided_workout_sync_error_manage_tiles_text, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanScheduleFragmentV1.1.1
                                            @Override // android.content.DialogInterface.OnClickListener
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(WorkoutPlanScheduleFragmentV1.this.getActivity(), HomeActivity.class);
                                                intent.putExtra(HomeActivity.STARTING_PAGE, ManageTilesFragment.class.getSimpleName());
                                                WorkoutPlanScheduleFragmentV1.this.getActivity().startActivity(intent);
                                            }
                                        }, (DialogInterface.OnClickListener) null, DialogPriority.LOW);
                                    } else {
                                        boolean isSubscribed = false;
                                        String currentSubscribedWorkoutPlanId = WorkoutPlanScheduleFragmentV1.this.mGuidedWorkoutService.getSubscribedWorkoutPlanId();
                                        if (currentSubscribedWorkoutPlanId != null && toggledWorkout.getWorkout() != null) {
                                            isSubscribed = currentSubscribedWorkoutPlanId.equals(toggledWorkout.getWorkout().getWorkoutPlanId());
                                        }
                                        if (checked) {
                                            if (WorkoutPlanScheduleFragmentV1.this.mSettingsProvider.isInNoDevicePairedMode()) {
                                                WorkoutPlanScheduleFragmentV1.this.getDialogManager().showDialog(WorkoutPlanScheduleFragmentV1.this.getActivity(), Integer.valueOf((int) R.string.connection_with_device_error_title), Integer.valueOf((int) R.string.guided_workout_sync_no_band_paired_mode_error), DialogPriority.LOW);
                                            } else if (isSubscribed) {
                                                WorkoutPlanScheduleFragmentV1.this.sendWorkoutToDevice(toggledWorkout, currentDayIndex, true);
                                            } else {
                                                WorkoutPlanScheduleFragmentV1.this.getDialogManager().showDialog(WorkoutPlanScheduleFragmentV1.this.getActivity(), Integer.valueOf((int) R.string.app_name), Integer.valueOf((int) R.string.guided_workout_sync_not_subscribed), new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanScheduleFragmentV1.1.2
                                                    @Override // android.content.DialogInterface.OnClickListener
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        WorkoutPlanScheduleFragmentV1.this.sendWorkoutToDevice(toggledWorkout, currentDayIndex, false);
                                                    }
                                                }, (DialogInterface.OnClickListener) null, DialogPriority.HIGH);
                                            }
                                        }
                                    }
                                }
                            }
                        };
                        this.mTotalWeeks = Math.max(this.mTotalWeeks, workout.getWeekId());
                        WorkoutScheduleItem.OnWorkoutClickedListener workoutClickListener = new WorkoutScheduleItem.OnWorkoutClickedListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanScheduleFragmentV1.2
                            @Override // com.microsoft.kapp.models.WorkoutScheduleItem.OnWorkoutClickedListener
                            public void onWorkoutClicked(WorkoutScheduleItem workoutItem) {
                                Intent intent = new Intent(WorkoutPlanScheduleFragmentV1.this.getActivity(), WorkoutDetailActivity.class);
                                ScheduledWorkout scheduledWorkout = workoutItem.getWorkout();
                                if (scheduledWorkout == null) {
                                    KLog.w(WorkoutPlanScheduleFragmentV1.this.TAG, "scheduledWorkout should not be null!");
                                    return;
                                }
                                intent.putExtra(Constants.KEY_WORKOUT_HEADER_DETAIL, true);
                                intent.putExtra(Constants.KEY_GUIDED_WORKOUT_SCHEDULED_WORKOUT, scheduledWorkout);
                                intent.putExtra(Constants.KEY_MODE, WorkoutPlanScheduleFragmentV1.this.mIsHomeTileMode);
                                Fragment parent = WorkoutPlanScheduleFragmentV1.this.getParentFragment();
                                if (parent != null) {
                                    parent.startActivityForResult(intent, 10005);
                                } else {
                                    WorkoutPlanScheduleFragmentV1.this.startActivityForResult(intent, 10005);
                                }
                            }
                        };
                        WorkoutScheduleItem workoutScheduleItem = WorkoutScheduleItem.createNormal(workout, this.mWorkoutPlan, workout.getWorkoutPlanId().equals(subscribedWorkoutPlanId) ? workout.getTypedWorkoutStatus() : UserWorkoutStatus.NOT_STARTED, toggledListener, workoutClickListener);
                        this.mAllWorkoutItems.add(workoutScheduleItem);
                        workoutIndex++;
                    } else {
                        this.mAllWorkoutItems.add(WorkoutScheduleItem.createRest());
                    }
                }
                dayIndex++;
            }
            int lastScheduledWorkoutIndex = workouts.size() - 1;
            if (lastScheduledWorkoutIndex >= 0 && (lastScheduledWorkout = workouts.get(lastScheduledWorkoutIndex)) != null) {
                int totalRemainingRestDaysinLastWeek = 7 - lastScheduledWorkout.getDay();
                for (int i = 0; i < totalRemainingRestDaysinLastWeek; i++) {
                    this.mAllWorkoutItems.add(WorkoutScheduleItem.createRest());
                }
            }
            updateSyncStatus(null);
            updateScheduleAdapter();
            setState(1234);
        }
    }

    protected void sendWorkoutToDevice(WorkoutScheduleItem toggledWorkout, int currentDayIndex, boolean isSubscribed) {
        if (toggledWorkout != null) {
            toggledWorkout.setSyncStatus(null);
            this.mAdapter.notifyDataSetChanged();
            this.mGuidedWorkoutService.pushGuidedWorkoutToDevice(toggledWorkout.getWorkout());
            if (isSubscribed) {
                skipWorkoutsUntilIndex(currentDayIndex - 1);
            }
        }
    }

    private void updateScheduleAdapter() {
        if (this.mAdapter != null) {
            this.mAdapter.notifyDataSetChanged();
            return;
        }
        this.mAdapter = new WorkoutScheduleItemAdapter(getActivity(), this.mAllWorkoutItems, this.mTotalWeeks, !this.mIsPlanSubscriptionDisabled);
        this.mScheduleList.setAdapter(this.mAdapter);
        this.mScheduleList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanScheduleFragmentV1.3
            @Override // android.widget.ExpandableListView.OnGroupClickListener
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                    return true;
                }
                parent.expandGroup(groupPosition);
                return true;
            }
        });
        if (this.mAdapter.getGroupCount() > 0) {
            for (int position = 0; position < this.mAdapter.getGroupCount(); position++) {
                this.mScheduleList.expandGroup(position);
            }
        }
    }

    private void skipWorkoutsUntilIndex(int lastSkippedIndex) {
        for (int i = 0; i <= lastSkippedIndex; i++) {
            if (!this.mAllWorkoutItems.get(i).isRest() && this.mAllWorkoutItems.get(i).getCompletionStatus().equals(UserWorkoutStatus.NOT_STARTED)) {
                skipWorkout(this.mAllWorkoutItems.get(i));
            }
        }
    }

    private void skipWorkout(final WorkoutScheduleItem scheduleItem) {
        scheduleItem.setIsSkipPending(true);
        ScheduledWorkout workout = scheduleItem.getWorkout();
        if (workout != null) {
            setState(1233);
            this.mGuidedWorkoutService.skipWorkout(workout.getWorkoutPlanId(), workout.getWorkoutIndex(), workout.getDay(), workout.getWeekId(), new ActivityScopedCallback(this, new Callback() { // from class: com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanScheduleFragmentV1.4
                @Override // com.microsoft.kapp.Callback
                public void callback(Object result) {
                    WorkoutPlanScheduleFragmentV1.this.setState(1234);
                    scheduleItem.setIsSkipPending(false);
                    scheduleItem.setCompletionState(UserWorkoutStatus.SKIPPED);
                }

                @Override // com.microsoft.kapp.Callback
                public void onError(Exception ex) {
                    WorkoutPlanScheduleFragmentV1.this.setState(1235);
                    scheduleItem.setIsSkipPending(false);
                }
            }));
        }
    }

    private void updateSyncStatus(WorkoutScheduleItem inProgressItem) {
        SyncedWorkoutInfo lastSyncedWorkoutInfo = this.mGuidedWorkoutService.getLastSyncedWorkout();
        String lastSyncedWorkoutPlanId = lastSyncedWorkoutInfo == null ? null : lastSyncedWorkoutInfo.getWorkoutPlanId();
        int lastSyncedWorkoutDay = lastSyncedWorkoutInfo == null ? 0 : lastSyncedWorkoutInfo.getDay();
        int lastSyncedWorkoutWeek = lastSyncedWorkoutInfo == null ? 0 : lastSyncedWorkoutInfo.getWeek();
        for (WorkoutScheduleItem scheduleItem : this.mAllWorkoutItems) {
            if (scheduleItem == inProgressItem) {
                scheduleItem.setSyncStatus(null);
            } else if (!scheduleItem.isRest()) {
                ScheduledWorkout workout = scheduleItem.getWorkout();
                boolean isSynced = lastSyncedWorkoutInfo != null && lastSyncedWorkoutPlanId != null && lastSyncedWorkoutPlanId.equals(workout.getWorkoutPlanId()) && workout.getDay() == lastSyncedWorkoutDay && workout.getWeekId() == lastSyncedWorkoutWeek;
                scheduleItem.setSyncStatus(Boolean.valueOf(isSynced));
            }
        }
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutBroadcastListener
    public void onGWBroadcastReceived(Context context, Intent data) {
        ScheduledWorkout currentScheduledWorkoutItem;
        if (isAdded() && data != null) {
            String operation = data.getAction();
            int operationStatus = data.getIntExtra(GuidedWorkoutNotificationHandler.KEY_ACTION_STATUS, 2);
            String workoutPlanId = data.getStringExtra(GuidedWorkoutNotificationHandler.KEY_WORKOUT_PLAN_ID);
            if (GuidedWorkoutNotificationHandler.OPERATION_SUBSCRIBE.equals(operation)) {
                if (!this.mIsPlanSingleWorkout) {
                    if (operationStatus == 0) {
                        if (TextUtils.equals(this.mCurrentWorkoutPlanId, workoutPlanId)) {
                            setSubscribeAction(2);
                        }
                    } else if (operationStatus == 1) {
                        if (TextUtils.equals(this.mCurrentWorkoutPlanId, workoutPlanId)) {
                            setSubscribeAction(3);
                        }
                    } else if (operationStatus == 2 && TextUtils.equals(this.mCurrentWorkoutPlanId, workoutPlanId)) {
                        setSubscribeAction(1);
                    }
                }
            } else if (GuidedWorkoutNotificationHandler.OPERATION_UNSUBSCRIBE.equals(operation)) {
                if (!this.mIsPlanSingleWorkout) {
                    if (operationStatus == 0) {
                        if (TextUtils.equals(this.mCurrentWorkoutPlanId, workoutPlanId)) {
                            setSubscribeAction(2);
                        }
                    } else if (operationStatus == 1) {
                        if (TextUtils.equals(this.mCurrentWorkoutPlanId, workoutPlanId)) {
                            setSubscribeAction(1);
                        }
                    } else if (operationStatus == 2 && TextUtils.equals(this.mCurrentWorkoutPlanId, workoutPlanId)) {
                        setSubscribeAction(3);
                    }
                }
            } else if (GuidedWorkoutNotificationHandler.OPERATION_SYNC.equals(operation)) {
                ScheduledWorkout scheduledWorkout = (ScheduledWorkout) data.getParcelableExtra(GuidedWorkoutNotificationHandler.KEY_SCHEDULED_WORKOUT);
                if (operationStatus == 0) {
                    if (scheduledWorkout != null && this.mCurrentWorkoutPlanId != null && this.mCurrentWorkoutPlanId.equals(scheduledWorkout.getWorkoutPlanId())) {
                        Iterator i$ = this.mAllWorkoutItems.iterator();
                        while (true) {
                            if (!i$.hasNext()) {
                                break;
                            }
                            WorkoutScheduleItem workoutItem = i$.next();
                            if (workoutItem != null && (currentScheduledWorkoutItem = workoutItem.getWorkout()) != null && GuidedWorkoutUtils.isSameScheduledWorkout(scheduledWorkout, currentScheduledWorkoutItem)) {
                                workoutItem.setSyncStatus(null);
                                break;
                            }
                        }
                        this.mAdapter.notifyDataSetChanged();
                    }
                } else if (operationStatus == 1) {
                    updateSyncStatus(null);
                    updateScheduleAdapter();
                } else if (operationStatus == 2) {
                    boolean isFirstWorkout = data.getBooleanExtra(GuidedWorkoutNotificationHandler.KEY_IS_FIRST_WORKOUT, true);
                    int errorId = data.getIntExtra("ErrorId", 0);
                    updateSyncStatus(null);
                    updateScheduleAdapter();
                    if (!isFirstWorkout) {
                        getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.app_name), Integer.valueOf(GuidedWorkoutUtils.getWorkoutSyncErrorText(errorId)), DialogPriority.LOW);
                    }
                }
            }
        }
    }

    private void setSubscribeAction(int status) {
        this.mWorkoutSubscribeDownload.setVisibility(4);
        this.mWorkoutOperationProgress.setVisibility(8);
        switch (status) {
            case 0:
            default:
                return;
            case 1:
                this.mWorkoutSubscribeDownload.setVisibility(0);
                this.mYourCurrentPlanLayout.setVisibility(8);
                this.mMakeThisYourPlanLayout.setVisibility(0);
                return;
            case 2:
                this.mWorkoutOperationProgress.setVisibility(0);
                return;
            case 3:
                this.mYourCurrentPlanLayout.setVisibility(0);
                this.mMakeThisYourPlanLayout.setVisibility(8);
                return;
        }
    }

    private void setOnClickSubscribeToThisPlanBanner() {
        this.mWorkoutSubscribeDownload.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanScheduleFragmentV1.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (!WorkoutPlanScheduleFragmentV1.this.mSettingsProvider.isInNoDevicePairedMode()) {
                    List<UUID> uuid = WorkoutPlanScheduleFragmentV1.this.mSettingsProvider.getUUIDsOnDevice();
                    if (uuid != null && uuid.contains(DefaultStrappUUID.STRAPP_GUIDED_WORKOUTS)) {
                        if (WorkoutPlanScheduleFragmentV1.this.mGuidedWorkoutService.getSubscribedWorkoutPlanId() == null) {
                            WorkoutPlanScheduleFragmentV1.this.subscribeToWorkoutPlan();
                            return;
                        } else {
                            WorkoutPlanScheduleFragmentV1.this.getDialogManager().showDialog(WorkoutPlanScheduleFragmentV1.this.getActivity(), Integer.valueOf((int) R.string.guided_workout_stop_previous_plan_title), Integer.valueOf((int) R.string.guided_workout_stop_previous_plan_body), new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanScheduleFragmentV1.5.1
                                @Override // android.content.DialogInterface.OnClickListener
                                public void onClick(DialogInterface dialog, int which) {
                                    WorkoutPlanScheduleFragmentV1.this.subscribeToWorkoutPlan();
                                }
                            }, (DialogInterface.OnClickListener) null, DialogPriority.HIGH);
                            return;
                        }
                    }
                    GuidedWorkoutUtils.showGuidedWorkoutTileDisabledDialog(WorkoutPlanScheduleFragmentV1.this.getActivity());
                    return;
                }
                WorkoutPlanScheduleFragmentV1.this.getDialogManager().showDialog(WorkoutPlanScheduleFragmentV1.this.getActivity(), Integer.valueOf((int) R.string.connection_with_device_error_title), Integer.valueOf((int) R.string.guided_workout_subscribe_no_band_paired_mode_error), DialogPriority.LOW);
            }
        });
    }

    private void setOnClickDownloadThisSingleWorkoutBanner(final ScheduledWorkout scheduledWorkout) {
        this.mWorkoutSubscribeDownload.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanScheduleFragmentV1.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (scheduledWorkout != null) {
                    if (!WorkoutPlanScheduleFragmentV1.this.mSettingsProvider.getUUIDsOnDevice().contains(DefaultStrappUUID.STRAPP_GUIDED_WORKOUTS)) {
                        GuidedWorkoutUtils.showGuidedWorkoutTileDisabledDialog(WorkoutPlanScheduleFragmentV1.this.getActivity());
                    } else {
                        WorkoutPlanScheduleFragmentV1.this.getDialogManager().showDialog(WorkoutPlanScheduleFragmentV1.this.getActivity(), Integer.valueOf((int) R.string.app_name), Integer.valueOf((int) R.string.guided_workout_sync_not_subscribed), new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanScheduleFragmentV1.6.1
                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialog, int which) {
                                WorkoutPlanScheduleFragmentV1.this.mGuidedWorkoutService.pushGuidedWorkoutToDevice(scheduledWorkout);
                            }
                        }, (DialogInterface.OnClickListener) null, DialogPriority.HIGH);
                    }
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void subscribeToWorkoutPlan() {
        this.mGuidedWorkoutService.subscribeToWorkoutPlan(this.mCurrentWorkoutPlanId);
    }

    private void updateSubscribedStatusLocally() {
        if (getSubscribedStatus()) {
            setSubscribeAction(3);
        } else {
            setSubscribeAction(1);
        }
    }

    private boolean getSubscribedStatus() {
        String currentlySubscribedPlanId = this.mGuidedWorkoutService.getSubscribedWorkoutPlanId();
        return TextUtils.equals(currentlySubscribedPlanId, this.mCurrentWorkoutPlanId);
    }
}

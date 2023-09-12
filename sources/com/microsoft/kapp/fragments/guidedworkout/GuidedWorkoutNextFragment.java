package com.microsoft.kapp.fragments.guidedworkout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.activities.HomeActivity;
import com.microsoft.kapp.adapters.WorkoutStepListAdapterV1;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.GuidedWorkoutCircuitList;
import com.microsoft.kapp.models.SyncedWorkoutInfo;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.services.healthandfitness.models.CircuitGroupType;
import com.microsoft.kapp.services.healthandfitness.models.CircuitType;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutCircuit;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutExercise;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutPlan;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutStep;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.kapp.utils.GuidedWorkoutUtils;
import com.microsoft.kapp.utils.StringUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.GuidedWorkouts.CircuitTypeHeader;
import com.microsoft.kapp.views.GuidedWorkouts.ExerciseView;
import com.microsoft.kapp.views.GuidedWorkouts.FooterHolder;
import com.microsoft.kapp.views.GuidedWorkouts.GroupDescriptionHeader;
import com.microsoft.kapp.views.GuidedWorkouts.GroupTypeHeader;
import com.microsoft.kapp.views.GuidedWorkouts.RestView;
import com.microsoft.krestsdk.models.CompletionType;
import com.microsoft.krestsdk.models.GuidedWorkoutEvent;
import com.microsoft.krestsdk.models.ScheduledWorkout;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import java.util.Iterator;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class GuidedWorkoutNextFragment extends BaseFragmentWithOfflineSupport implements GuidedWorkoutBroadcastListener {
    private TextView mBackButton;
    private TextView mDownloadWorkoutIcon;
    private TextView mFindOtherWorkouts;
    private View.OnClickListener mFindOtherWorkoutsClickListener;
    private GuidedWorkoutBroadcastReceiver mGuidedWorkoutBroadcastReceiver;
    private FrameLayout mGuidedWorkoutCircuitListContainer;
    @Inject
    GuidedWorkoutService mGuidedWorkoutService;
    private View mHeaderArrow;
    private LinearLayout mHeaderContainer;
    private TextView mHeaderSubTitle;
    private boolean mIsGWCalendarTile;
    private boolean mIsHomeTileMode;
    private boolean mIsPendingFindOtherWorkoutsRequest;
    private boolean mIsRestDay;
    private boolean mIsWorkoutOnBand;
    private TextView mPlanOverView;
    private View.OnClickListener mPlanOverViewClickListener;
    private TextView mRestDayFindOtherWorkouts;
    private ScrollView mRestDayImagePanel;
    private View mRestDayPageFooter;
    private View mRestDayPageFooterDivider;
    private LinearLayout mRestDayPanelLayout;
    private TextView mRestDayPlanOverView;
    private View mRootView;
    private ScheduledWorkout mScheduledWorkout;
    @Inject
    SettingsProvider mSettingsProvider;
    private boolean mShowHeader;
    private ProgressBar mSyncProgress;
    private ViewGroup mSyncWorkoutToBandLayout;
    private LinearLayout mThisIsOnYourBandHeader;
    private ViewGroup mThisWorkoutOnBandLayout;
    private int mWorkoutIndex;
    private TextView mWorkoutName;
    private String mWorkoutPlanId;
    private String mWorkoutPlanName;
    private WorkoutStep mWorkoutStep;
    private WorkoutStepListAdapterV1 mWorkoutStepListAdapterV1;
    private TextView mWorkoutStepName;

    public static BaseFragment newInstance(ScheduledWorkout scheduledWorkout, boolean showHeaderTitle, boolean isHomeTileMode, boolean isGWCalendarTile) {
        GuidedWorkoutNextFragment fragment = new GuidedWorkoutNextFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.KEY_GUIDED_WORKOUT_SCHEDULED_WORKOUT, scheduledWorkout);
        bundle.putBoolean(Constants.KEY_WORKOUT_HEADER_DETAIL, showHeaderTitle);
        bundle.putBoolean(Constants.KEY_MODE, isHomeTileMode);
        bundle.putBoolean(Constants.KEY_GW_CALENDAR_TILE, isGWCalendarTile);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle savedBundle = savedInstanceState == null ? getArguments() : savedInstanceState;
        Validate.notNull(savedBundle, "savedBundle");
        this.mScheduledWorkout = (ScheduledWorkout) savedBundle.getParcelable(Constants.KEY_GUIDED_WORKOUT_SCHEDULED_WORKOUT);
        this.mShowHeader = savedBundle.getBoolean(Constants.KEY_WORKOUT_HEADER_DETAIL);
        this.mIsHomeTileMode = savedBundle.getBoolean(Constants.KEY_MODE);
        this.mIsGWCalendarTile = savedBundle.getBoolean(Constants.KEY_GW_CALENDAR_TILE);
        this.mIsRestDay = this.mGuidedWorkoutService.isRestDay();
        if (!this.mIsGWCalendarTile) {
            Validate.notNull(this.mScheduledWorkout, "scheduledWorkout");
        }
        this.mWorkoutPlanId = this.mScheduledWorkout != null ? this.mScheduledWorkout.getWorkoutPlanId() : null;
        this.mWorkoutIndex = this.mScheduledWorkout != null ? this.mScheduledWorkout.getWorkoutIndex() : -1;
    }

    private void addHeaderTileToView(boolean showHeader) {
        if (showHeader) {
            this.mWorkoutName.setText(this.mWorkoutPlanName);
            if (this.mWorkoutStep != null) {
                this.mWorkoutStepName.setText(this.mWorkoutStep.getName());
            }
            if (this.mIsGWCalendarTile) {
                if (this.mIsRestDay) {
                    this.mHeaderSubTitle.setText(R.string.guided_workout_rest_day_subTitle);
                } else {
                    this.mHeaderSubTitle.setText(R.string.guided_workout_next);
                }
                this.mHeaderSubTitle.setVisibility(0);
                this.mHeaderContainer.setBackgroundColor(getResources().getColor(R.color.top_menu_color));
                this.mBackButton.setVisibility(8);
                this.mHeaderArrow.setVisibility(8);
                this.mWorkoutName.setVisibility(0);
                this.mWorkoutStepName.setVisibility(0);
                return;
            }
            return;
        }
        this.mHeaderContainer.setVisibility(8);
        this.mHeaderArrow.setVisibility(8);
    }

    public WorkoutStepListAdapterV1 getAdapter() {
        return this.mWorkoutStepListAdapterV1;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.KEY_GUIDED_WORKOUT_SCHEDULED_WORKOUT, this.mScheduledWorkout);
        outState.putBoolean(Constants.KEY_WORKOUT_HEADER_DETAIL, this.mShowHeader);
        outState.putBoolean(Constants.KEY_MODE, this.mIsHomeTileMode);
        outState.putBoolean(Constants.KEY_GW_CALENDAR_TILE, this.mIsGWCalendarTile);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.guided_workout_next_fragment, container, false);
        this.mGuidedWorkoutCircuitListContainer = (FrameLayout) ViewUtils.getValidView(this.mRootView, R.id.circuit_list, FrameLayout.class);
        this.mHeaderContainer = (LinearLayout) ViewUtils.getValidView(this.mRootView, R.id.header_container, LinearLayout.class);
        this.mWorkoutName = (TextView) ViewUtils.getValidView(this.mRootView, R.id.workout_name, TextView.class);
        this.mWorkoutStepName = (TextView) ViewUtils.getValidView(this.mRootView, R.id.workoutStep_name, TextView.class);
        this.mBackButton = (TextView) ViewUtils.getValidView(this.mRootView, R.id.back, TextView.class);
        this.mHeaderSubTitle = (TextView) ViewUtils.getValidView(this.mRootView, R.id.header_sub_title, TextView.class);
        this.mThisIsOnYourBandHeader = (LinearLayout) ViewUtils.getValidView(this.mRootView, R.id.this_workout_is_on_your_band_header, LinearLayout.class);
        this.mThisWorkoutOnBandLayout = (ViewGroup) ViewUtils.getValidView(this.mRootView, R.id.workout_on_your_band_layout, ViewGroup.class);
        this.mDownloadWorkoutIcon = (TextView) ViewUtils.getValidView(this.mRootView, R.id.glyph_download_circle, TextView.class);
        this.mSyncWorkoutToBandLayout = (ViewGroup) ViewUtils.getValidView(this.mRootView, R.id.sync_workout_to_band_layout, ViewGroup.class);
        this.mSyncProgress = (ProgressBar) ViewUtils.getValidView(this.mRootView, R.id.sync_progress, ProgressBar.class);
        this.mHeaderArrow = (View) ViewUtils.getValidView(this.mRootView, R.id.header_title_arrow, View.class);
        this.mRestDayImagePanel = (ScrollView) ViewUtils.getValidView(this.mRootView, R.id.rest_day_image_panel, ScrollView.class);
        this.mRestDayPanelLayout = (LinearLayout) ViewUtils.getValidView(this.mRestDayImagePanel, R.id.rest_day_image_panel_layout, LinearLayout.class);
        this.mRestDayPageFooter = inflater.inflate(R.layout.guided_workout_next_fragment_bottom_view, (ViewGroup) this.mRestDayPanelLayout, false);
        this.mRestDayPageFooterDivider = inflater.inflate(R.layout.guided_workout_next_fragment_bottom_divider_view, (ViewGroup) this.mRestDayPanelLayout, false);
        this.mBackButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutNextFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GuidedWorkoutNextFragment.this.getActivity().finish();
            }
        });
        this.mDownloadWorkoutIcon.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutNextFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (GuidedWorkoutNextFragment.this.mSettingsProvider.getUUIDsOnDevice().contains(DefaultStrappUUID.STRAPP_GUIDED_WORKOUTS)) {
                    if (GuidedWorkoutNextFragment.this.mGuidedWorkoutService != null && GuidedWorkoutNextFragment.this.mScheduledWorkout != null) {
                        boolean isSubscribed = false;
                        String currentSubscribedWorkoutPlanId = GuidedWorkoutNextFragment.this.mGuidedWorkoutService.getSubscribedWorkoutPlanId();
                        if (currentSubscribedWorkoutPlanId != null) {
                            isSubscribed = currentSubscribedWorkoutPlanId.equals(GuidedWorkoutNextFragment.this.mWorkoutPlanId);
                        }
                        if (isSubscribed) {
                            GuidedWorkoutNextFragment.this.updateSyncStatus(true);
                            GuidedWorkoutNextFragment.this.mGuidedWorkoutService.pushGuidedWorkoutToDevice(GuidedWorkoutNextFragment.this.mScheduledWorkout);
                            return;
                        }
                        GuidedWorkoutNextFragment.this.getDialogManager().showDialog(GuidedWorkoutNextFragment.this.getActivity(), Integer.valueOf((int) R.string.app_name), Integer.valueOf((int) R.string.guided_workout_sync_not_subscribed), new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutNextFragment.2.1
                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialog, int which) {
                                GuidedWorkoutNextFragment.this.updateSyncStatus(true);
                                GuidedWorkoutNextFragment.this.mGuidedWorkoutService.pushGuidedWorkoutToDevice(GuidedWorkoutNextFragment.this.mScheduledWorkout);
                            }
                        }, (DialogInterface.OnClickListener) null, DialogPriority.HIGH);
                        return;
                    }
                    return;
                }
                GuidedWorkoutUtils.showGuidedWorkoutTileDisabledDialog(GuidedWorkoutNextFragment.this.getActivity());
            }
        });
        this.mPlanOverViewClickListener = new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutNextFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (GuidedWorkoutNextFragment.this.mWorkoutPlanId != null) {
                    GuidedWorkoutNextFragment.this.startWorkoutPlanActivity(GuidedWorkoutNextFragment.this.mWorkoutPlanId);
                }
            }
        };
        this.mFindOtherWorkoutsClickListener = new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutNextFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GuidedWorkoutNextFragment.this.startFindOtherWorkoutsPage();
            }
        };
        fetchWorkoutPlan(this.mWorkoutPlanId);
        this.mGuidedWorkoutBroadcastReceiver = new GuidedWorkoutBroadcastReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(GuidedWorkoutNotificationHandler.OPERATION_SYNC);
        if (this.mIsGWCalendarTile) {
            filter.addAction(GuidedWorkoutNotificationHandler.OPERATION_NEXT_CALENDAR_WORKOUT);
        }
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.mGuidedWorkoutBroadcastReceiver, filter);
        return this.mRootView;
    }

    private void fetchWorkoutPlan(String workoutPlanId) {
        if (workoutPlanId == null) {
            KLog.w(this.TAG, "WorkoutPlanId cannot be null!");
            return;
        }
        setState(1233);
        this.mGuidedWorkoutService.getHnFWorkoutPlanDetails(workoutPlanId, new ActivityScopedCallback(this, new Callback<WorkoutPlan>() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutNextFragment.5
            @Override // com.microsoft.kapp.Callback
            public void callback(WorkoutPlan result) {
                if (result == null) {
                    KLog.w(GuidedWorkoutNextFragment.this.TAG, "WorkoutPlan should not be null!");
                    GuidedWorkoutNextFragment.this.setState(1235);
                    return;
                }
                GuidedWorkoutNextFragment.this.getAndShowWorkoutStep(result);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.w(GuidedWorkoutNextFragment.this.TAG, "Error while fetching WorkoutPlan!");
                GuidedWorkoutNextFragment.this.setState(1235);
            }
        }));
    }

    protected void getAndShowWorkoutStep(WorkoutPlan workoutPlan) {
        if (workoutPlan != null) {
            try {
                this.mWorkoutPlanName = workoutPlan.getName();
                if (this.mIsRestDay && this.mIsGWCalendarTile) {
                    this.mWorkoutStepName.setText(R.string.guided_workout_rest_day_subTitle);
                } else {
                    this.mWorkoutStep = workoutPlan.getSteps()[this.mWorkoutIndex];
                }
                loadInitialPage();
            } catch (Exception ex) {
                KLog.e(this.TAG, "Error during fetching WorkoutStep %s", ex);
                setState(1235);
            }
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this.mGuidedWorkoutBroadcastReceiver);
    }

    private void loadInitialPage() {
        setState(1234);
        if (this.mIsGWCalendarTile) {
            if (this.mIsRestDay) {
                showRestDay();
            } else if (this.mWorkoutStep != null) {
                showWorkoutDay();
            } else {
                setState(1235);
            }
        } else if (this.mWorkoutStep != null) {
            showWorkoutDay();
        } else {
            setState(1235);
        }
    }

    private void showWorkoutDay() {
        this.mRestDayImagePanel.setVisibility(8);
        this.mGuidedWorkoutCircuitListContainer.setVisibility(0);
        this.mHeaderContainer.setVisibility(0);
        this.mThisIsOnYourBandHeader.setVisibility(this.mMultiDeviceManager.hasBand() ? 0 : 8);
        updateSyncStatus(isCurrentWorkout(this.mGuidedWorkoutService.getCurrentlySyncingWorkout()));
        populateWorkoutList(this.mWorkoutStep);
    }

    private void showRestDay() {
        this.mRestDayImagePanel.setVisibility(0);
        this.mGuidedWorkoutCircuitListContainer.setVisibility(8);
        this.mHeaderContainer.setVisibility(0);
        this.mBackButton.setVisibility(8);
        this.mThisIsOnYourBandHeader.setVisibility(8);
        this.mRestDayPanelLayout.removeView(this.mRestDayPageFooterDivider);
        this.mRestDayPanelLayout.removeView(this.mRestDayPageFooter);
        this.mRestDayPanelLayout.addView(this.mRestDayPageFooterDivider);
        this.mRestDayPanelLayout.addView(this.mRestDayPageFooter);
        this.mRestDayFindOtherWorkouts = (TextView) ViewUtils.getValidView(this.mRestDayPageFooter, R.id.workout_find_other_workouts, TextView.class);
        this.mRestDayPlanOverView = (TextView) ViewUtils.getValidView(this.mRestDayPageFooter, R.id.plan_overview, TextView.class);
        this.mRestDayFindOtherWorkouts.setOnClickListener(this.mFindOtherWorkoutsClickListener);
        this.mRestDayPlanOverView.setOnClickListener(this.mPlanOverViewClickListener);
        addHeaderTileToView(this.mShowHeader);
        setState(1234);
    }

    private boolean isCurrentWorkout(ScheduledWorkout scheduledWorkout) {
        return scheduledWorkout != null && this.mScheduledWorkout != null && scheduledWorkout.getWorkoutPlanId() != null && scheduledWorkout.getWorkoutPlanId().equals(this.mScheduledWorkout.getWorkoutPlanId()) && scheduledWorkout.getDay() == this.mScheduledWorkout.getDay() && scheduledWorkout.getWeekId() == this.mScheduledWorkout.getWeekId();
    }

    private void addCircuitListToTreeView(TreeNode root, CircuitType type, GuidedWorkoutCircuitList list) {
        if (list.size() > 0) {
            TreeNode circuitGroupNode = new TreeNode(type).setViewHolder(new CircuitTypeHeader(getActivity()));
            root.addChild(circuitGroupNode);
            Iterator i$ = list.iterator();
            while (i$.hasNext()) {
                WorkoutCircuit circuit = i$.next();
                CircuitGroupType groupType = circuit.getGroupType();
                TreeNode typeNode = new TreeNode(circuit).setViewHolder(new GroupTypeHeader(getActivity(), list.getGroupTypeCount(circuit.getGroupType())));
                circuitGroupNode.addChild(typeNode);
                if (groupType != CircuitGroupType.Rest && groupType != CircuitGroupType.FreePlay) {
                    TreeNode descriptionNode = new TreeNode(circuit).setViewHolder(new GroupDescriptionHeader(getActivity()));
                    typeNode.addChild(descriptionNode);
                    int i = 0;
                    while (i < circuit.getExerciseList().length) {
                        WorkoutExercise exercise = circuit.getExerciseList()[i];
                        if (!exercise.getId().equalsIgnoreCase(Constants.REST_EXERCISE_ID)) {
                            TreeNode exerciseNode = new TreeNode(exercise).setViewHolder(new ExerciseView(getActivity(), groupType));
                            typeNode.addChild(exerciseNode);
                        }
                        String restDurationString = GuidedWorkoutUtils.getRestDurationString(getActivity(), exercise, groupType);
                        boolean showRestForList = groupType != CircuitGroupType.List || GuidedWorkoutUtils.getMaxIntegerfromString(exercise.getSets()) <= 1;
                        if (!restDurationString.isEmpty() && showRestForList) {
                            boolean skip = i == circuit.getExerciseList().length + (-1) && circuit.getDropLastRest();
                            TreeNode restNode = new TreeNode(restDurationString).setViewHolder(new RestView(getActivity(), skip));
                            typeNode.addChild(restNode);
                        }
                        i++;
                    }
                }
            }
        }
    }

    private void populateWorkoutList(WorkoutStep workoutStep) {
        if (workoutStep == null) {
            KLog.w(this.TAG, "WorkoutStep should not be null!");
            return;
        }
        ViewGroup containerView = (ViewGroup) ViewUtils.getValidView(this.mRootView, R.id.circuit_holder, ViewGroup.class);
        containerView.removeAllViews();
        WorkoutCircuit[] circuitList = workoutStep.getCircuitList();
        if (circuitList != null && isAdded()) {
            GuidedWorkoutCircuitList warmups = new GuidedWorkoutCircuitList(circuitList.length);
            GuidedWorkoutCircuitList workouts = new GuidedWorkoutCircuitList(circuitList.length);
            GuidedWorkoutCircuitList cooldowns = new GuidedWorkoutCircuitList(circuitList.length);
            for (WorkoutCircuit circuit : circuitList) {
                switch (circuit.getCircuitType()) {
                    case CoolDown:
                        cooldowns.add(circuit);
                        break;
                    case WarmUp:
                        warmups.add(circuit);
                        break;
                    default:
                        workouts.add(circuit);
                        break;
                }
            }
            TreeNode root = TreeNode.root();
            addCircuitListToTreeView(root, CircuitType.WarmUp, warmups);
            addCircuitListToTreeView(root, CircuitType.Regular, workouts);
            addCircuitListToTreeView(root, CircuitType.CoolDown, cooldowns);
            if (this.mIsGWCalendarTile) {
                TreeNode footerNode = new TreeNode(1).setViewHolder(new FooterHolder(getActivity()));
                root.addChild(footerNode);
                View footer = footerNode.getViewHolder().getView(null);
                this.mFindOtherWorkouts = (TextView) ViewUtils.getValidView(footer, R.id.workout_find_other_workouts, TextView.class);
                this.mPlanOverView = (TextView) ViewUtils.getValidView(footer, R.id.plan_overview, TextView.class);
                this.mPlanOverView.setOnClickListener(this.mPlanOverViewClickListener);
                this.mFindOtherWorkouts.setOnClickListener(this.mFindOtherWorkoutsClickListener);
            }
            AndroidTreeView treeView = new AndroidTreeView(getActivity(), root);
            containerView.addView(treeView.getView());
            treeView.expandAll();
            treeView.setDefaultAnimation(true);
        }
        addHeaderTileToView(this.mShowHeader);
        setState(1234);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSyncStatus(boolean isSyncing) {
        if (isAdded() && this.mWorkoutStep != null && this.mMultiDeviceManager.hasBand()) {
            if (isSyncing) {
                this.mThisWorkoutOnBandLayout.setVisibility(8);
                this.mSyncWorkoutToBandLayout.setVisibility(0);
                this.mDownloadWorkoutIcon.setVisibility(4);
                this.mSyncProgress.setVisibility(0);
                return;
            }
            this.mIsWorkoutOnBand = GuidedWorkoutUtils.isWorkoutSynced(this.mGuidedWorkoutService, this.mScheduledWorkout);
            this.mSyncProgress.setVisibility(4);
            if (this.mIsWorkoutOnBand) {
                this.mThisWorkoutOnBandLayout.setVisibility(0);
                this.mSyncWorkoutToBandLayout.setVisibility(8);
                return;
            }
            this.mThisWorkoutOnBandLayout.setVisibility(8);
            this.mSyncWorkoutToBandLayout.setVisibility(0);
            this.mDownloadWorkoutIcon.setVisibility(0);
            this.mSyncProgress.setVisibility(4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startWorkoutPlanActivity(String workoutPlanId) {
        Bundle bundle = new Bundle();
        bundle.putString("workoutPlanId", workoutPlanId);
        if (this.mIsGWCalendarTile) {
            ActivityUtils.launchLevelTwoActivityForResult(getActivity(), GuidedWorkoutsBrowsePlanOverViewFragment.class, bundle, this, 10002);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startFindOtherWorkoutsPage() throws ClassCastException {
        try {
            ((HomeActivity) HomeActivity.class.cast(getActivity())).navigateToFragment(BrowseGuidedWorkoutsFragment.class, null, true, false);
        } catch (ClassCastException ex) {
            KLog.e(this.TAG, "Error casting the current activity into HomeActivity.class: %s", ex);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mIsPendingFindOtherWorkoutsRequest) {
            startFindOtherWorkoutsPage();
            this.mIsPendingFindOtherWorkoutsRequest = false;
            return;
        }
        String subscribedWorkoutPlanId = this.mGuidedWorkoutService.getSubscribedWorkoutPlanId();
        if (subscribedWorkoutPlanId == null && this.mIsGWCalendarTile) {
            SyncedWorkoutInfo syncedWorkoutInfo = this.mGuidedWorkoutService.getLastSyncedWorkout();
            GuidedWorkoutEvent guidedWorkoutEvent = this.mGuidedWorkoutService.getGuidedWorkoutevent();
            if (syncedWorkoutInfo != null && guidedWorkoutEvent != null && GuidedWorkoutUtils.isSameCompletedWorkout(syncedWorkoutInfo, guidedWorkoutEvent)) {
                ActivityUtils.performBackButton(getActivity());
            }
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10002 && resultCode == 6) {
            this.mIsPendingFindOtherWorkoutsRequest = true;
        }
    }

    private String getWorkoutRepsDescriptionFromWorkout(CompletionType completionType, int completionValue) {
        switch (completionType) {
            case SECONDS:
                return completionValue >= 120 ? getString(R.string.workout_reps_name_gr_120_seconds, Formatter.formatDurationSecondsToHrMin(getActivity(), completionValue).toString()) : getString(R.string.workout_reps_name, StringUtils.unitSeconds(completionValue, getResources()));
            case REPETITIONS:
                return getString(R.string.workout_reps_name, StringUtils.unitRepetitions(completionValue, getResources()));
            case METERS:
                return getString(R.string.workout_reps_name, StringUtils.unitMeter(completionValue, getResources()));
            case CALORIES:
                return getString(R.string.workout_reps_name, StringUtils.unitCalories(completionValue, getResources()));
            case JOULES:
                return getString(R.string.workout_reps_name, StringUtils.unitJoule(completionValue, getResources()));
            case HEART_RATE:
                return getString(R.string.workout_reps_name_HR, Integer.valueOf(completionValue));
            default:
                return getString(R.string.workout_reps_name_default, Integer.valueOf(completionValue));
        }
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutBroadcastListener
    public void onGWBroadcastReceived(Context context, Intent data) {
        if (isAdded() && data != null) {
            String operation = data.getAction();
            int operationStatus = data.getIntExtra(GuidedWorkoutNotificationHandler.KEY_ACTION_STATUS, 2);
            ScheduledWorkout scheduledWorkout = (ScheduledWorkout) data.getParcelableExtra(GuidedWorkoutNotificationHandler.KEY_SCHEDULED_WORKOUT);
            if (GuidedWorkoutNotificationHandler.OPERATION_NEXT_CALENDAR_WORKOUT.equals(operation) && this.mIsGWCalendarTile) {
                if (operationStatus == 1) {
                    this.mIsRestDay = this.mGuidedWorkoutService.isRestDay();
                    if (this.mIsRestDay) {
                        showRestDay();
                        return;
                    }
                    ScheduledWorkout nextGuidedWorkoutStepSchedule = this.mGuidedWorkoutService.getNextGuidedWorkoutStepSchedule();
                    this.mScheduledWorkout = nextGuidedWorkoutStepSchedule;
                    if (nextGuidedWorkoutStepSchedule != null) {
                        this.mWorkoutPlanId = this.mScheduledWorkout.getWorkoutPlanId();
                        this.mWorkoutIndex = this.mScheduledWorkout.getWorkoutIndex();
                        if (this.mWorkoutPlanId != null) {
                            fetchWorkoutPlan(this.mWorkoutPlanId);
                        }
                    }
                }
            } else if (GuidedWorkoutNotificationHandler.OPERATION_SYNC.equals(operation)) {
                if (operationStatus == 0) {
                    updateSyncStatus(isCurrentWorkout(scheduledWorkout));
                } else if (operationStatus == 1) {
                    updateSyncStatus(false);
                } else if (operationStatus == 2) {
                    int errorId = data.getIntExtra("ErrorId", 0);
                    updateSyncStatus(false);
                    getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.app_name), Integer.valueOf(GuidedWorkoutUtils.getWorkoutSyncErrorText(errorId)), DialogPriority.LOW);
                }
            }
        }
    }
}

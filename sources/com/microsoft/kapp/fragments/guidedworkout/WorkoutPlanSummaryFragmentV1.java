package com.microsoft.kapp.fragments.guidedworkout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.activities.HomeActivity;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.SyncedWorkoutInfo;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.picasso.utils.PicassoWrapper;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.bedrock.BedrockImageServiceUtils;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutPlan;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.GuidedWorkoutUtils;
import com.microsoft.kapp.utils.LockedStringUtils;
import com.microsoft.kapp.utils.SerialAsyncOperation;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.AspectImageView;
import com.microsoft.krestsdk.models.FavoriteWorkoutPlan;
import com.microsoft.krestsdk.models.ScheduledWorkout;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
/* loaded from: classes.dex */
public class WorkoutPlanSummaryFragmentV1 extends BaseFragmentWithOfflineSupport implements GuidedWorkoutBroadcastListener {
    private static final String TAG = WorkoutPlanSummaryFragmentV1.class.getSimpleName();
    private static final int mGlyphBand = 2131166359;
    private static int mWorkoutImageHeight;
    private int mFavoriteWorkoutColor;
    private TextView mFindOtherWorkouts;
    private TextView mGlyphCheckSquare;
    private GuidedWorkoutBroadcastReceiver mGuidedWorkoutBroadcastReceiver;
    @Inject
    GuidedWorkoutService mGuidedWorkoutService;
    private boolean mIsFavorited;
    private boolean mIsHomeTileMode;
    private boolean mIsPlanSingleWorkout;
    private boolean mIsPlanSubscriptionDisabled;
    private ViewGroup mMakeThisYourPlanLayout;
    private ViewGroup mPlanWorkoutSubscriptionBanner;
    private ScheduledWorkout mScheduledWorkout;
    @Inject
    SettingsProvider mSettingsProvider;
    private TextView mSyncThisWorkoutTextView;
    private TextView mThisWorkoutIsOnYourBandTextView;
    private int mUnfavoriteWorkoutColor;
    private TextView mWorkoutFavorite;
    private RelativeLayout mWorkoutFavoriteContainer;
    private ProgressBar mWorkoutFavoriteProgress;
    private ProgressBar mWorkoutOperationProgress;
    private TextView mWorkoutOverview;
    private WorkoutPlan mWorkoutPlan;
    private String mWorkoutPlanId;
    private LinearLayout mWorkoutStats;
    private TextView mWorkoutSubscribeDownload;
    private AspectImageView mWorkoutSummaryImage;
    private TextView mWorkoutUnsubscribe;
    private ViewGroup mYourCurrentPlanLayout;

    public static BaseFragment newInstance(WorkoutPlan workoutPlan) {
        return newInstance(workoutPlan, false, false);
    }

    public static BaseFragment newInstance(WorkoutPlan workoutPlan, boolean isHomeTileMode, boolean isPlanSubscriptionDisabled) {
        Validate.notNull(workoutPlan, "workoutPlan");
        WorkoutPlanSummaryFragmentV1 fragment = new WorkoutPlanSummaryFragmentV1();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_GUIDED_WORKOUT_PLAN, workoutPlan);
        bundle.putBoolean(Constants.KEY_MODE, isHomeTileMode);
        bundle.putBoolean(Constants.KEY_GUIDED_WORKOUT_PLAN_SUBSCRIPTION_STATE, isPlanSubscriptionDisabled);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static BaseFragment newInstance(WorkoutPlan workoutPlan, ScheduledWorkout scheduledWorkout, boolean isHomeTileMode, boolean isPlanSubscriptionDisabled) {
        Validate.notNull(workoutPlan, "workoutPlan");
        Validate.notNull(scheduledWorkout, "scheduledWorkout");
        WorkoutPlanSummaryFragmentV1 fragment = new WorkoutPlanSummaryFragmentV1();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_GUIDED_WORKOUT_PLAN, workoutPlan);
        bundle.putParcelable(Constants.KEY_GUIDED_WORKOUT_SCHEDULED_WORKOUT, scheduledWorkout);
        bundle.putBoolean(Constants.KEY_GUIDED_WORKOUT_PLAN_SUBSCRIPTION_STATE, isPlanSubscriptionDisabled);
        bundle.putBoolean(Constants.KEY_MODE, isHomeTileMode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle savedBundle = savedInstanceState == null ? getArguments() : savedInstanceState;
        Validate.notNull(savedBundle, "savedBundle");
        this.mWorkoutPlan = (WorkoutPlan) savedBundle.getSerializable(Constants.KEY_GUIDED_WORKOUT_PLAN);
        this.mWorkoutPlanId = this.mWorkoutPlan == null ? null : this.mWorkoutPlan.getId();
        this.mIsHomeTileMode = savedBundle.getBoolean(Constants.KEY_MODE, false);
        this.mIsPlanSubscriptionDisabled = savedBundle.getBoolean(Constants.KEY_GUIDED_WORKOUT_PLAN_SUBSCRIPTION_STATE, false);
        this.mScheduledWorkout = (ScheduledWorkout) savedBundle.getParcelable(Constants.KEY_GUIDED_WORKOUT_SCHEDULED_WORKOUT);
        this.mIsPlanSingleWorkout = this.mScheduledWorkout != null;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Constants.KEY_GUIDED_WORKOUT_PLAN, this.mWorkoutPlan);
        outState.putBoolean(Constants.KEY_MODE, this.mIsHomeTileMode);
        outState.putParcelable(Constants.KEY_GUIDED_WORKOUT_SCHEDULED_WORKOUT, this.mScheduledWorkout);
        outState.putBoolean(Constants.KEY_GUIDED_WORKOUT_PLAN_SUBSCRIPTION_STATE, this.mIsPlanSubscriptionDisabled);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.workout_plan_summary_fragment_v1, container, false);
        mWorkoutImageHeight = getResources().getDimensionPixelSize(R.dimen.guided_workout_overview_image_height);
        this.mWorkoutSummaryImage = (AspectImageView) ViewUtils.getValidView(rootView, R.id.workout_summary_image, AspectImageView.class);
        this.mWorkoutOverview = (TextView) ViewUtils.getValidView(rootView, R.id.workout_overview, TextView.class);
        this.mWorkoutStats = (LinearLayout) ViewUtils.getValidView(rootView, R.id.workout_plan_stats, LinearLayout.class);
        this.mWorkoutUnsubscribe = (TextView) ViewUtils.getValidView(rootView, R.id.remove_workout_plan, TextView.class);
        this.mWorkoutFavorite = (TextView) ViewUtils.getValidView(rootView, R.id.favorite_icon, TextView.class);
        this.mWorkoutFavoriteContainer = (RelativeLayout) ViewUtils.getValidView(rootView, R.id.favorite_unfavorite_button_container, RelativeLayout.class);
        this.mWorkoutFavoriteProgress = (ProgressBar) ViewUtils.getValidView(rootView, R.id.workout_favorite_progress, ProgressBar.class);
        this.mFindOtherWorkouts = (TextView) ViewUtils.getValidView(rootView, R.id.workout_find_other_workouts, TextView.class);
        this.mPlanWorkoutSubscriptionBanner = (ViewGroup) ViewUtils.getValidView(rootView, R.id.this_is_current_plan, ViewGroup.class);
        this.mYourCurrentPlanLayout = (ViewGroup) ViewUtils.getValidView(this.mPlanWorkoutSubscriptionBanner, R.id.your_current_plan_layout, ViewGroup.class);
        this.mGlyphCheckSquare = (TextView) ViewUtils.getValidView(this.mYourCurrentPlanLayout, R.id.glyph_check_square, TextView.class);
        this.mThisWorkoutIsOnYourBandTextView = (TextView) ViewUtils.getValidView(this.mYourCurrentPlanLayout, R.id.this_is_your_current_plan, TextView.class);
        this.mMakeThisYourPlanLayout = (ViewGroup) ViewUtils.getValidView(this.mPlanWorkoutSubscriptionBanner, R.id.make_this_your_plan_layout, ViewGroup.class);
        this.mSyncThisWorkoutTextView = (TextView) ViewUtils.getValidView(this.mMakeThisYourPlanLayout, R.id.make_this_your_workout_plan, TextView.class);
        this.mWorkoutSubscribeDownload = (TextView) ViewUtils.getValidView(this.mMakeThisYourPlanLayout, R.id.glyph_download_circle, TextView.class);
        this.mWorkoutOperationProgress = (ProgressBar) ViewUtils.getValidView(this.mMakeThisYourPlanLayout, R.id.workout_subscribe_progress, ProgressBar.class);
        Resources resources = getResources();
        this.mFavoriteWorkoutColor = resources.getColor(R.color.guided_workout_favorite);
        this.mUnfavoriteWorkoutColor = resources.getColor(R.color.guided_workout_unfavorite);
        if (this.mIsPlanSubscriptionDisabled) {
            this.mPlanWorkoutSubscriptionBanner.setVisibility(8);
        }
        if (this.mIsPlanSingleWorkout) {
            setOnClickDownloadThisSingleWorkoutBanner();
        } else {
            setOnClickSubscribeToThisPlanBanner();
        }
        this.mWorkoutFavoriteContainer.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanSummaryFragmentV1.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (WorkoutPlanSummaryFragmentV1.this.getFavoriteStatus()) {
                    WorkoutPlanSummaryFragmentV1.this.removePlanFromFavorites();
                } else {
                    WorkoutPlanSummaryFragmentV1.this.addPlanToFavorites();
                }
            }
        });
        this.mFindOtherWorkouts.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanSummaryFragmentV1.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Intent intent = new Intent(WorkoutPlanSummaryFragmentV1.this.getActivity(), HomeActivity.class);
                intent.putExtra(HomeActivity.STARTING_PAGE, BrowseGuidedWorkoutsFragment.class.getSimpleName());
                WorkoutPlanSummaryFragmentV1.this.getActivity().startActivity(intent);
            }
        });
        this.mGuidedWorkoutBroadcastReceiver = new GuidedWorkoutBroadcastReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(GuidedWorkoutNotificationHandler.OPERATION_SUBSCRIBE);
        filter.addAction(GuidedWorkoutNotificationHandler.OPERATION_UNSUBSCRIBE);
        filter.addAction(GuidedWorkoutNotificationHandler.OPERATION_FAVORITE);
        filter.addAction(GuidedWorkoutNotificationHandler.OPERATION_UNFAVORITE);
        filter.addAction(GuidedWorkoutNotificationHandler.OPERATION_SYNC);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.mGuidedWorkoutBroadcastReceiver, filter);
        loadData();
        return rootView;
    }

    private void setOnClickSubscribeToThisPlanBanner() {
        this.mWorkoutSubscribeDownload.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanSummaryFragmentV1.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (!WorkoutPlanSummaryFragmentV1.this.mSettingsProvider.isInNoDevicePairedMode()) {
                    List<UUID> uuid = WorkoutPlanSummaryFragmentV1.this.mSettingsProvider.getUUIDsOnDevice();
                    if (uuid != null && uuid.contains(DefaultStrappUUID.STRAPP_GUIDED_WORKOUTS)) {
                        if (WorkoutPlanSummaryFragmentV1.this.mGuidedWorkoutService.getSubscribedWorkoutPlanId() == null) {
                            WorkoutPlanSummaryFragmentV1.this.subscribeToWorkoutPlan();
                            return;
                        } else {
                            WorkoutPlanSummaryFragmentV1.this.getDialogManager().showDialog(WorkoutPlanSummaryFragmentV1.this.getActivity(), Integer.valueOf((int) R.string.guided_workout_stop_previous_plan_title), Integer.valueOf((int) R.string.guided_workout_stop_previous_plan_body), new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanSummaryFragmentV1.3.1
                                @Override // android.content.DialogInterface.OnClickListener
                                public void onClick(DialogInterface dialog, int which) {
                                    WorkoutPlanSummaryFragmentV1.this.subscribeToWorkoutPlan();
                                }
                            }, (DialogInterface.OnClickListener) null, DialogPriority.HIGH);
                            return;
                        }
                    }
                    GuidedWorkoutUtils.showGuidedWorkoutTileDisabledDialog(WorkoutPlanSummaryFragmentV1.this.getActivity());
                    return;
                }
                WorkoutPlanSummaryFragmentV1.this.getDialogManager().showDialog(WorkoutPlanSummaryFragmentV1.this.getActivity(), Integer.valueOf((int) R.string.connection_with_device_error_title), Integer.valueOf((int) R.string.guided_workout_subscribe_no_band_paired_mode_error), DialogPriority.LOW);
            }
        });
        this.mWorkoutUnsubscribe.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanSummaryFragmentV1.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                WorkoutPlanSummaryFragmentV1.this.unsubscribeFromWorkoutPlan();
            }
        });
    }

    private void setOnClickDownloadThisSingleWorkoutBanner() {
        this.mWorkoutUnsubscribe.setVisibility(8);
        this.mWorkoutSubscribeDownload.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanSummaryFragmentV1.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (WorkoutPlanSummaryFragmentV1.this.mScheduledWorkout != null) {
                    if (!WorkoutPlanSummaryFragmentV1.this.mSettingsProvider.getUUIDsOnDevice().contains(DefaultStrappUUID.STRAPP_GUIDED_WORKOUTS)) {
                        GuidedWorkoutUtils.showGuidedWorkoutTileDisabledDialog(WorkoutPlanSummaryFragmentV1.this.getActivity());
                    } else if (WorkoutPlanSummaryFragmentV1.this.mGuidedWorkoutService.getSubscribedWorkoutPlanId() == null) {
                        WorkoutPlanSummaryFragmentV1.this.mGuidedWorkoutService.pushGuidedWorkoutToDevice(WorkoutPlanSummaryFragmentV1.this.mScheduledWorkout);
                    } else {
                        WorkoutPlanSummaryFragmentV1.this.getDialogManager().showDialog(WorkoutPlanSummaryFragmentV1.this.getActivity(), Integer.valueOf((int) R.string.app_name), Integer.valueOf((int) R.string.guided_workout_sync_not_subscribed), new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanSummaryFragmentV1.5.1
                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialog, int which) {
                                WorkoutPlanSummaryFragmentV1.this.mGuidedWorkoutService.pushGuidedWorkoutToDevice(WorkoutPlanSummaryFragmentV1.this.mScheduledWorkout);
                            }
                        }, (DialogInterface.OnClickListener) null, DialogPriority.HIGH);
                    }
                }
            }
        });
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_GUIDED_WORKOUTS_PLAN_SUMMARY);
    }

    private void loadData() {
        if (this.mIsPlanSingleWorkout) {
            updateSyncStatus(isCurrentWorkout(this.mGuidedWorkoutService.getCurrentlySyncingWorkout()));
        } else {
            updateSubscribedStatusLocally();
        }
        this.mWorkoutStats.removeAllViews();
        addSingleTracker(R.string.workout_stat_goal, StringUtils.join(this.mWorkoutPlan.getGoal(), "\n"));
        addSingleTracker(R.string.workout_stat_duration, LockedStringUtils.unitMins(com.microsoft.kapp.utils.StringUtils.parseToInt(this.mWorkoutPlan.getDuration()), getActivity().getResources()));
        addSingleTracker(R.string.workout_stat_body_part, StringUtils.join(this.mWorkoutPlan.getBodyPart(), "\n"));
        addSingleTracker(R.string.workout_stat_difficulty_level, this.mWorkoutPlan.getDifficultyLevel());
        populateWorkoutSummary(this.mWorkoutPlan);
        updateFavoriteStatus(1);
        new CheckFavoritesOperation(null).execute();
    }

    private void populateWorkoutSummary(WorkoutPlan workout) {
        if (workout == null) {
            setState(1235);
            return;
        }
        int width = this.mWorkoutSummaryImage.getEstimatedWidth(getActivity());
        int height = this.mWorkoutSummaryImage.getEstimatedHeight(getActivity());
        String workoutImage = workout.getHeroImageURL();
        PicassoWrapper.with(getActivity()).load(BedrockImageServiceUtils.createSizedImageUrl(workoutImage, width, height)).fit().centerCrop().placeholder(R.drawable.barbell_bg).into(this.mWorkoutSummaryImage);
        String workoutOverview = workout.getHTMLDescription();
        if (!TextUtils.isEmpty(workoutOverview)) {
            Spanned workoutOverviewText = Html.fromHtml(workoutOverview);
            this.mWorkoutOverview.setText(workoutOverviewText);
        }
    }

    private void addSingleTracker(int titleResourceId, CharSequence value) {
        if (isAdded()) {
            View statView = LayoutInflater.from(getActivity()).inflate(R.layout.guided_workout_plan_overview_stat, (ViewGroup) this.mWorkoutStats, false);
            ((TextView) statView.findViewById(R.id.overview_stat_name)).setText(getString(titleResourceId));
            ((TextView) statView.findViewById(R.id.overview_stat_value)).setText(value);
            this.mWorkoutStats.addView(statView);
        }
    }

    private void updateSubscribedStatusLocally() {
        if (getSubscribedStatus()) {
            setSubscribeAction(3);
            updateFavoriteStatus(0);
            return;
        }
        setSubscribeAction(1);
    }

    private void setSubscribeAction(int status) {
        this.mWorkoutSubscribeDownload.setVisibility(4);
        this.mWorkoutUnsubscribe.setVisibility(8);
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
                this.mWorkoutUnsubscribe.setVisibility(0);
                this.mYourCurrentPlanLayout.setVisibility(0);
                this.mMakeThisYourPlanLayout.setVisibility(8);
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void subscribeToWorkoutPlan() {
        this.mGuidedWorkoutService.subscribeToWorkoutPlan(this.mWorkoutPlanId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unsubscribeFromWorkoutPlan() {
        this.mGuidedWorkoutService.unsubscribeFromWorkoutPlan(this.mWorkoutPlanId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addPlanToFavorites() {
        this.mGuidedWorkoutService.favoriteWorkoutPlan(this.mWorkoutPlanId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removePlanFromFavorites() {
        this.mGuidedWorkoutService.unfavoriteWorkoutPlan(this.mWorkoutPlanId);
    }

    public void updateFavoriteStatus(int isFavorited) {
        this.mWorkoutFavorite.setVisibility(4);
        this.mWorkoutFavoriteProgress.setVisibility(4);
        switch (isFavorited) {
            case 0:
                this.mIsFavorited = true;
                this.mWorkoutFavorite.setText(R.string.glyph_star_fill);
                this.mWorkoutFavorite.setTextColor(this.mFavoriteWorkoutColor);
                this.mWorkoutFavorite.setVisibility(0);
                return;
            case 1:
                this.mIsFavorited = false;
                this.mWorkoutFavorite.setText(R.string.glyph_star);
                this.mWorkoutFavorite.setTextColor(this.mUnfavoriteWorkoutColor);
                this.mWorkoutFavorite.setVisibility(0);
                return;
            case 2:
                this.mWorkoutFavoriteProgress.setVisibility(0);
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getFavoriteStatus() {
        return this.mIsFavorited;
    }

    private boolean getSubscribedStatus() {
        String currentlySubscribedPlanId = this.mGuidedWorkoutService.getSubscribedWorkoutPlanId();
        return TextUtils.equals(currentlySubscribedPlanId, this.mWorkoutPlanId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class CheckFavoritesOperation implements SerialAsyncOperation {
        private SerialAsyncOperation mNextAsyncOperation;

        public CheckFavoritesOperation(SerialAsyncOperation nextOperation) {
            this.mNextAsyncOperation = nextOperation;
        }

        @Override // com.microsoft.kapp.utils.SerialAsyncOperation
        public void execute() {
            WorkoutPlanSummaryFragmentV1.this.mGuidedWorkoutService.getFavoriteWorkoutPlans(new ActivityScopedCallback(WorkoutPlanSummaryFragmentV1.this, new Callback<List<FavoriteWorkoutPlan>>() { // from class: com.microsoft.kapp.fragments.guidedworkout.WorkoutPlanSummaryFragmentV1.CheckFavoritesOperation.1
                @Override // com.microsoft.kapp.Callback
                public void callback(List<FavoriteWorkoutPlan> favoriteWorkoutPlans) {
                    WorkoutPlanSummaryFragmentV1.this.setState(1234);
                    if (WorkoutPlanSummaryFragmentV1.this.mWorkoutPlanId != null && favoriteWorkoutPlans != null) {
                        Iterator i$ = favoriteWorkoutPlans.iterator();
                        while (true) {
                            if (!i$.hasNext()) {
                                break;
                            }
                            FavoriteWorkoutPlan favoriteWorkoutPlan = i$.next();
                            if (favoriteWorkoutPlan != null && TextUtils.equals(favoriteWorkoutPlan.getWorkoutPlanId(), WorkoutPlanSummaryFragmentV1.this.mWorkoutPlanId)) {
                                WorkoutPlanSummaryFragmentV1.this.updateFavoriteStatus(0);
                                break;
                            }
                        }
                    }
                    if (CheckFavoritesOperation.this.mNextAsyncOperation != null) {
                        CheckFavoritesOperation.this.mNextAsyncOperation.execute();
                    }
                }

                @Override // com.microsoft.kapp.Callback
                public void onError(Exception ex) {
                    KLog.d(WorkoutPlanSummaryFragmentV1.TAG, "unable to get favorite workouts", ex);
                    WorkoutPlanSummaryFragmentV1.this.setState(1235);
                }
            }));
        }
    }

    private void updateSyncStatus(boolean isSyncing) {
        if (isAdded() && this.mIsPlanSingleWorkout && !this.mIsPlanSubscriptionDisabled) {
            if (isSyncing) {
                this.mYourCurrentPlanLayout.setVisibility(8);
                this.mMakeThisYourPlanLayout.setVisibility(0);
                this.mSyncThisWorkoutTextView.setText(R.string.guided_workout_sync_this_workout_to_your_band);
                this.mWorkoutSubscribeDownload.setVisibility(4);
                this.mWorkoutOperationProgress.setVisibility(0);
                return;
            }
            boolean isWorkoutOnBand = isWorkoutSynced(this.mScheduledWorkout);
            this.mWorkoutOperationProgress.setVisibility(4);
            if (isWorkoutOnBand) {
                this.mYourCurrentPlanLayout.setVisibility(0);
                this.mThisWorkoutIsOnYourBandTextView.setText(R.string.guided_workout_this_workout_is_on_your_band);
                this.mGlyphCheckSquare.setText(R.string.glyph_band);
                this.mMakeThisYourPlanLayout.setVisibility(8);
                return;
            }
            this.mYourCurrentPlanLayout.setVisibility(8);
            this.mMakeThisYourPlanLayout.setVisibility(0);
            this.mSyncThisWorkoutTextView.setText(R.string.guided_workout_sync_this_workout_to_your_band);
            this.mWorkoutSubscribeDownload.setVisibility(0);
            this.mWorkoutOperationProgress.setVisibility(4);
        }
    }

    private boolean isWorkoutSynced(ScheduledWorkout scheduledWorkout) {
        String lastSyncedWorkoutPlan;
        SyncedWorkoutInfo lastSyncedWorkout = this.mGuidedWorkoutService.getLastSyncedWorkout();
        return lastSyncedWorkout != null && scheduledWorkout != null && (lastSyncedWorkoutPlan = lastSyncedWorkout.getWorkoutPlanId()) != null && lastSyncedWorkoutPlan.equals(scheduledWorkout.getWorkoutPlanId()) && lastSyncedWorkout.getWeek() == scheduledWorkout.getWeekId() && lastSyncedWorkout.getDay() == scheduledWorkout.getDay();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this.mGuidedWorkoutBroadcastReceiver);
    }

    private boolean isCurrentWorkout(ScheduledWorkout scheduledWorkout) {
        return scheduledWorkout != null && this.mScheduledWorkout != null && scheduledWorkout.getWorkoutPlanId() != null && scheduledWorkout.getWorkoutPlanId().equals(this.mScheduledWorkout.getWorkoutPlanId()) && scheduledWorkout.getDay() == this.mScheduledWorkout.getDay() && scheduledWorkout.getWeekId() == this.mScheduledWorkout.getWeekId();
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutBroadcastListener
    public void onGWBroadcastReceived(Context context, Intent data) {
        if (isAdded() && data != null) {
            String operation = data.getAction();
            int operationStatus = data.getIntExtra(GuidedWorkoutNotificationHandler.KEY_ACTION_STATUS, 2);
            String workoutPlanId = data.getStringExtra(GuidedWorkoutNotificationHandler.KEY_WORKOUT_PLAN_ID);
            if (GuidedWorkoutNotificationHandler.OPERATION_SUBSCRIBE.equals(operation)) {
                if (!this.mIsPlanSingleWorkout) {
                    if (operationStatus == 0) {
                        if (TextUtils.equals(this.mWorkoutPlanId, workoutPlanId)) {
                            setSubscribeAction(2);
                        }
                    } else if (operationStatus == 1) {
                        if (TextUtils.equals(this.mWorkoutPlanId, workoutPlanId)) {
                            setSubscribeAction(3);
                        }
                    } else if (operationStatus == 2 && TextUtils.equals(this.mWorkoutPlanId, workoutPlanId)) {
                        setSubscribeAction(1);
                    }
                }
            } else if (GuidedWorkoutNotificationHandler.OPERATION_UNSUBSCRIBE.equals(operation)) {
                if (!this.mIsPlanSingleWorkout) {
                    if (operationStatus == 0) {
                        if (TextUtils.equals(this.mWorkoutPlanId, workoutPlanId)) {
                            setSubscribeAction(2);
                        }
                    } else if (operationStatus == 1) {
                        if (TextUtils.equals(this.mWorkoutPlanId, workoutPlanId)) {
                            setSubscribeAction(1);
                        }
                    } else if (operationStatus == 2 && TextUtils.equals(this.mWorkoutPlanId, workoutPlanId)) {
                        setSubscribeAction(3);
                        getDialogManager().showNetworkErrorDialog(getActivity());
                    }
                }
            } else if (GuidedWorkoutNotificationHandler.OPERATION_FAVORITE.equals(operation)) {
                if (operationStatus == 0) {
                    if (TextUtils.equals(this.mWorkoutPlanId, workoutPlanId) && !getFavoriteStatus()) {
                        updateFavoriteStatus(2);
                    }
                } else if (operationStatus == 1) {
                    if (TextUtils.equals(this.mWorkoutPlanId, workoutPlanId)) {
                        updateFavoriteStatus(0);
                    }
                } else if (operationStatus == 2 && TextUtils.equals(this.mWorkoutPlanId, workoutPlanId)) {
                    updateFavoriteStatus(1);
                    getDialogManager().showNetworkErrorDialog(getActivity());
                }
            } else if (GuidedWorkoutNotificationHandler.OPERATION_UNFAVORITE.equals(operation)) {
                if (operationStatus == 0) {
                    if (TextUtils.equals(this.mWorkoutPlanId, workoutPlanId)) {
                        updateFavoriteStatus(2);
                    }
                } else if (operationStatus == 1) {
                    if (TextUtils.equals(this.mWorkoutPlanId, workoutPlanId)) {
                        updateFavoriteStatus(1);
                    }
                } else if (operationStatus == 2 && TextUtils.equals(this.mWorkoutPlanId, workoutPlanId)) {
                    updateFavoriteStatus(0);
                    getDialogManager().showNetworkErrorDialog(getActivity());
                }
            } else if (GuidedWorkoutNotificationHandler.OPERATION_SYNC.equals(operation)) {
                ScheduledWorkout scheduledWorkout = (ScheduledWorkout) data.getParcelableExtra(GuidedWorkoutNotificationHandler.KEY_SCHEDULED_WORKOUT);
                if (operationStatus == 0) {
                    if (this.mIsPlanSingleWorkout) {
                        updateSyncStatus(isCurrentWorkout(scheduledWorkout));
                    }
                } else if (operationStatus == 1) {
                    if (this.mIsPlanSingleWorkout) {
                        updateSyncStatus(false);
                    }
                } else if (operationStatus == 2) {
                    boolean isFirstWorkout = data.getBooleanExtra(GuidedWorkoutNotificationHandler.KEY_IS_FIRST_WORKOUT, true);
                    int errorId = data.getIntExtra("ErrorId", 0);
                    if (this.mIsPlanSingleWorkout) {
                        updateSyncStatus(false);
                        getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.app_name), Integer.valueOf(GuidedWorkoutUtils.getWorkoutSyncErrorText(errorId)), DialogPriority.LOW);
                    } else if (isFirstWorkout) {
                        getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.app_name), Integer.valueOf(GuidedWorkoutUtils.getWorkoutSubscribeSyncErrorText(errorId)), DialogPriority.LOW);
                    }
                }
            }
        }
    }
}

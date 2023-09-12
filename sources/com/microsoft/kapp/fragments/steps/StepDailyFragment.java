package com.microsoft.kapp.fragments.steps;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.ObservableGoalsManager;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.BaseTrackerDailyFragment;
import com.microsoft.kapp.models.UserActivitySummary;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.kapp.utils.GoalsUtils;
import com.microsoft.kapp.utils.ProfileUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomFontTextView;
import com.microsoft.kapp.views.CustomGlyphView;
import com.microsoft.kapp.views.LinearListView;
import com.microsoft.kapp.views.ScrollLoadIndicatorView;
import com.microsoft.kapp.views.UserActivitySummaryListView;
import com.microsoft.kapp.widgets.Interstitial;
import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.GoalType;
import com.microsoft.krestsdk.models.GolfEvent;
import com.microsoft.krestsdk.models.RaisedInsight;
import com.microsoft.krestsdk.models.RunEvent;
import com.microsoft.krestsdk.models.UserActivity;
import com.microsoft.krestsdk.models.UserDailySummary;
import com.shinobicontrols.kcompanionapp.charts.StepsDailyChartFragment;
import com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.LocalDate;
/* loaded from: classes.dex */
public class StepDailyFragment extends BaseTrackerDailyFragment {
    public static final String NUMBER_OF_ACTIVE_HOURS = "number_of_active_hours";
    private static final String STEPS_DAILY_CHART_TAG = "steps_daily_chart_tag";
    private StepsDailyChartFragment mChartFragment;
    private LinearLayout mDisclaimerContainer;
    private int mNumberOfActiveHours;
    protected int mStatStyleResourceId;
    private View mStepsDisclaimer;
    private int mStepsProgressTextResId = R.string.chart_steps_footer_step_goal;
    private View mUVDisclaimer;

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Validate.notNull(this.mSettingsProvider, "mSettingsProvider");
        Bundle fragmentInstantiationData = savedInstanceState == null ? getArguments() : savedInstanceState;
        this.mHomeData = HomeData.getInstance();
        if (fragmentInstantiationData != null) {
            this.mIsL2View = fragmentInstantiationData.getBoolean("stepsAndCaloriesLevelTwoView");
        }
        if (this.mIsL2View && fragmentInstantiationData != null) {
            this.mStartDate = (LocalDate) fragmentInstantiationData.getSerializable("stepsAndCaloriesStartdate");
            this.mEndDate = (LocalDate) fragmentInstantiationData.getSerializable("stepsAndCaloriesEnddate");
            this.mHeaderValue = fragmentInstantiationData.getInt("stepsAndCaloriesHeaderValue");
            this.mNumberOfActiveHours = fragmentInstantiationData.getInt(NUMBER_OF_ACTIVE_HOURS);
            return;
        }
        this.mEndDate = this.mHomeData.getTargetDate();
        if (this.mEndDate == null) {
            this.mEndDate = LocalDate.now();
        }
        this.mStartDate = this.mEndDate.plusDays(-1);
        UserDailySummary userDailySumamry = CommonUtils.getUserDailySummary(this.mHomeData);
        if (userDailySumamry != null) {
            this.mNumberOfActiveHours = userDailySumamry.getNumberOfActiveHours();
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_STEPS_DAILY);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_tracker_daily_fragment, container, false);
        this.mUserActivitySummaryListView = (UserActivitySummaryListView) ViewUtils.getValidView(view, R.id.recent_activities, UserActivitySummaryListView.class);
        this.mHeaderlayout = (RelativeLayout) ViewUtils.getValidView(view, R.id.activity_header_layout, RelativeLayout.class);
        this.mDateTextView = (CustomFontTextView) ViewUtils.getValidView(view, R.id.date_text_view, CustomFontTextView.class);
        this.mGlyph = (CustomGlyphView) ViewUtils.getValidView(view, R.id.activity_glyph, CustomGlyphView.class);
        this.mValueTextView = (CustomFontTextView) ViewUtils.getValidView(view, R.id.value_text_view, CustomFontTextView.class);
        this.mBackButton = (CustomGlyphView) ViewUtils.getValidView(view, R.id.back_button, CustomGlyphView.class);
        this.mDisclaimerContainer = (LinearLayout) ViewUtils.getValidView(view, R.id.disclaimerContainer, LinearLayout.class);
        this.mUVDisclaimer = (View) ViewUtils.getValidView(view, R.id.txtUVDisclaimer, View.class);
        this.mStepsDisclaimer = (View) ViewUtils.getValidView(view, R.id.txtStepDisclaimer, View.class);
        this.mBackButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.steps.StepDailyFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                StepDailyFragment.this.getActivity().finish();
            }
        });
        this.mInterstitial = (Interstitial) ViewUtils.getValidView(view, R.id.base_tracker_daily_interstitial, Interstitial.class);
        this.mScrollLoadIndicator = (ScrollLoadIndicatorView) ViewUtils.getValidView(view, R.id.scroll_load_indicator, ScrollLoadIndicatorView.class);
        getObservableDataManager().addOnGoalsChangedListenerWeafRef(GoalType.STEP, this);
        return view;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment, com.microsoft.kapp.fragments.BaseHomeTileFragment
    public void fetchData() {
        super.fetchData();
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment, com.microsoft.kapp.fragments.BaseTrackerFragmentV1, com.microsoft.kapp.fragments.BaseHomeTileFragment, android.support.v4.app.Fragment
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        this.mUserActivitySummaryListView.setOnItemClickListener(new LinearListView.OnItemClickListener() { // from class: com.microsoft.kapp.fragments.steps.StepDailyFragment.2
            @Override // com.microsoft.kapp.views.LinearListView.OnItemClickListener
            public void onItemClick(LinearListView adapterView, View view, int i, long l) {
                UserActivitySummary userActivitySummary = (UserActivitySummary) adapterView.getAdapter().getItem(i);
                if (userActivitySummary != null) {
                    Activity activity = StepDailyFragment.this.getActivity();
                    if (Validate.isActivityAlive(activity)) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("stepsAndCaloriesStartdate", userActivitySummary.getStartDate());
                        bundle.putSerializable("stepsAndCaloriesEnddate", userActivitySummary.getEndDate());
                        bundle.putBoolean("stepsAndCaloriesLevelTwoView", true);
                        bundle.putInt("stepsAndCaloriesHeaderValue", userActivitySummary.getValue());
                        bundle.putInt(StepDailyFragment.NUMBER_OF_ACTIVE_HOURS, userActivitySummary.getNumberOfActiveHours());
                        ActivityUtils.launchLevelTwoActivity(activity, StepDailyFragment.class, bundle);
                    }
                }
            }
        });
        this.mGlyph.setText(R.string.glyph_steps);
        this.mScrollView.setContentDescription(getResources().getString(R.string.trackable_scrollview_content_desc_steps_day));
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    protected GoalType getGoalType() {
        return GoalType.STEP;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public int getStatsViewResourceId() {
        return R.id.stats;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public int getScrollViewResourceId() {
        return R.id.trackerScrollView;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment
    protected int getAchievementForTargetDate() {
        if (this.mIsL2View) {
            return this.mHeaderValue;
        }
        UserDailySummary userDailySummary = CommonUtils.getUserDailySummary(this.mHomeData);
        if (userDailySummary != null) {
            return userDailySummary.getStepsTaken();
        }
        return 0;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment
    protected int getGoalTextViewResourceId() {
        return R.id.goal_text;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment
    protected int getGoalCommentViewResourceId() {
        return R.id.goal_comment;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment
    protected int getGoalProgressBarViewResourceId() {
        return R.id.goal_progress_bar;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment
    protected int getGoalEditViewResourceId() {
        return R.id.edit_goal_link;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment
    protected int getProgressTextResId() {
        return this.mStepsProgressTextResId;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment
    protected void populateTrackers(List<UserActivity> activityList) {
        int totalDistanceCM = 0;
        int totaluVExposure = 0;
        int totalFloorsClimbed = 0;
        Resources resources = getResources();
        Context context = getActivity();
        clearTrackerStats();
        addSingleTracker(R.string.tracker_header_steps_distance, R.string.glyph_distance, "");
        addSingleTracker(R.string.tracker_floors_climbed, R.string.glyph_stairs, "");
        addSingleTracker(R.string.tracker_header_steps_active_hours, R.string.glyph_duration, "");
        addSingleTracker(R.string.tracker_uv_exposure, R.string.glyph_UV, "");
        Spannable distanceText = new SpannableString(resources.getString(R.string.no_value));
        CharSequence floorsClimbedText = resources.getString(R.string.no_value);
        CharSequence timeText = resources.getString(R.string.no_value);
        CharSequence uvText = resources.getString(R.string.no_value);
        if (!isListNullOrEmpty(activityList)) {
            for (UserActivity userActivity : activityList) {
                totalDistanceCM += userActivity.getTotalDistanceOnFoot();
                totalFloorsClimbed += userActivity.getFloorsClimbed();
                totaluVExposure += userActivity.getUvExposure();
            }
            distanceText = Formatter.formatDistanceStat(context, R.array.MerticSmallUnitFormat, totalDistanceCM, this.mSettingsProvider.isDistanceHeightMetric());
            floorsClimbedText = Formatter.formatFloorsClimbed(context, totalFloorsClimbed);
            timeText = MetricSpannerUtils.formatHourShort(context, this.mFontManager, this.mNumberOfActiveHours);
            if (totaluVExposure > 0) {
                uvText = Formatter.formatUV(context, R.array.MerticSmallUnitFormat, totaluVExposure);
            }
        }
        this.mDisclaimerContainer.setVisibility((totalFloorsClimbed <= 0 || totaluVExposure <= 0) ? 0 : 0);
        this.mUVDisclaimer.setVisibility(totaluVExposure > 0 ? 0 : 0);
        this.mStepsDisclaimer.setVisibility(totalFloorsClimbed > 0 ? 0 : 0);
        setStatValues(distanceText, floorsClimbedText, timeText, uvText);
        if (this.mIsL2View) {
            updateActivityHeader(Formatter.formatSteps(context, this.mHeaderValue));
            return;
        }
        UserDailySummary userDailySummary = CommonUtils.getUserDailySummary(this.mHomeData);
        if (userDailySummary != null) {
            updateActivityHeader(Formatter.formatSteps(context, userDailySummary.getStepsTaken()));
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment
    protected int getValueFromUserActivity(UserActivity userActivity) {
        return userActivity.getStepsTaken();
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment
    public GoalDto getCurrentGoalDto() {
        return this.mHomeData.getGoal(getGoalType());
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public GoalDto getCurrentCaloriesGoalDto() {
        return this.mHomeData.getGoal(GoalType.CALORIE);
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public GoalDto getCurrentStepsGoalDto() {
        return this.mHomeData.getGoal(GoalType.STEP);
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment
    public ObservableGoalsManager getObservableDataManager() {
        return HomeData.getInstance();
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment, com.microsoft.kapp.fragments.BaseTrackerFragmentV1, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("stepsAndCaloriesStartdate", this.mStartDate);
        outState.putSerializable("stepsAndCaloriesEnddate", this.mEndDate);
        outState.putBoolean("stepsAndCaloriesLevelTwoView", this.mIsL2View);
        outState.putInt("stepsAndCaloriesHeaderValue", this.mHeaderValue);
        outState.putInt(NUMBER_OF_ACTIVE_HOURS, this.mNumberOfActiveHours);
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public int getChartViewResourceId() {
        return R.id.chart_daily_fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public BaseChartFragment getDailyChartFragment(List<UserActivity> userActivityList) {
        if (this.mChartFragment == null) {
            this.mChartFragment = StepsDailyChartFragment.newInstance(GoalsUtils.getGoalValue(getCurrentStepsGoalDto()), 600.0d);
        } else {
            this.mChartFragment.setDailyGoal(GoalsUtils.getGoalValue(getCurrentStepsGoalDto()));
            this.mChartFragment.setHighActivityThreshold(600.0d);
        }
        this.mChartFragment.setDataProvider(this);
        return this.mChartFragment;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
    public RunEvent getRunEvent() {
        return null;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public ArrayList<UserActivitySummary> getUserActivitySummaries(LocalDate date, LocalDate profileCreatedDate) {
        return ProfileUtils.getUserActivitySummaries(UserActivitySummary.DurationType.DAILY, date, profileCreatedDate, UserActivitySummary.ActivityType.STEPS);
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public ArrayList<UserActivitySummary> getUserActivitySummaries(List<UserDailySummary> userDailySummaryList) {
        return ProfileUtils.getUserActivitySummaries(UserActivitySummary.DurationType.DAILY, userDailySummaryList, UserActivitySummary.ActivityType.STEPS);
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    protected RaisedInsight.Pivot getDataUsedPivot() {
        return RaisedInsight.Pivot.STEPS;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    protected RaisedInsight.Pivot getTimespanPivot() {
        return RaisedInsight.Pivot.DAY;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment
    public String getChartFragmentTag() {
        return STEPS_DAILY_CHART_TAG;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
    public BikeEvent getBikeEvent() {
        return null;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
    public GolfEvent getGolfEvent() {
        return null;
    }
}

package com.microsoft.kapp.fragments.calories;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.microsoft.kapp.utils.stat.CaloriesDailyStat;
import com.microsoft.kapp.utils.stat.StatUtils;
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
import com.shinobicontrols.kcompanionapp.charts.CaloriesDailyChartFragment;
import com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import org.joda.time.LocalDate;
/* loaded from: classes.dex */
public class CaloriesDailyFragment extends BaseTrackerDailyFragment {
    private static final String CALORIES_DAILY_CHART_TAG = "calories_daily_chart_tag";
    private int mCaloriesProgressTextResId = R.string.chart_calories_footer_calorie_goal;
    private CaloriesDailyChartFragment mChartFragment;
    private Bundle mFragmentInstantiationData;
    @Inject
    StatUtils mStatUtils;

    public CaloriesDailyFragment() {
    }

    public CaloriesDailyFragment(HomeData homeData) {
        this.mHomeData = homeData;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Validate.notNull(this.mSettingsProvider, "mSettingsProvider");
        if (savedInstanceState == null) {
            savedInstanceState = getArguments();
        }
        this.mFragmentInstantiationData = savedInstanceState;
        if (this.mFragmentInstantiationData != null) {
            this.mHomeData = HomeData.getInstance();
            this.mIsL2View = this.mFragmentInstantiationData.getBoolean("stepsAndCaloriesLevelTwoView");
            if (this.mIsL2View) {
                this.mStartDate = (LocalDate) this.mFragmentInstantiationData.getSerializable("stepsAndCaloriesStartdate");
                this.mEndDate = (LocalDate) this.mFragmentInstantiationData.getSerializable("stepsAndCaloriesEnddate");
                this.mHeaderValue = this.mFragmentInstantiationData.getInt("stepsAndCaloriesHeaderValue");
                return;
            }
            this.mEndDate = this.mHomeData.getTargetDate();
            if (this.mEndDate == null) {
                this.mEndDate = LocalDate.now();
            }
            this.mStartDate = this.mEndDate.plusDays(-1);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_CALORIES_DAILY);
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
        this.mBackButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.calories.CaloriesDailyFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                CaloriesDailyFragment.this.getActivity().finish();
            }
        });
        this.mInterstitial = (Interstitial) ViewUtils.getValidView(view, R.id.base_tracker_daily_interstitial, Interstitial.class);
        this.mScrollLoadIndicator = (ScrollLoadIndicatorView) ViewUtils.getValidView(view, R.id.scroll_load_indicator, ScrollLoadIndicatorView.class);
        getObservableDataManager().addOnGoalsChangedListenerWeafRef(GoalType.CALORIE, this);
        return view;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment, com.microsoft.kapp.fragments.BaseTrackerFragmentV1, com.microsoft.kapp.fragments.BaseHomeTileFragment, android.support.v4.app.Fragment
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        this.mUserActivitySummaryListView.setOnItemClickListener(new LinearListView.OnItemClickListener() { // from class: com.microsoft.kapp.fragments.calories.CaloriesDailyFragment.2
            @Override // com.microsoft.kapp.views.LinearListView.OnItemClickListener
            public void onItemClick(LinearListView adapterView, View view, int i, long l) {
                UserActivitySummary userActivitySummary = (UserActivitySummary) adapterView.getAdapter().getItem(i);
                if (userActivitySummary != null) {
                    Activity activity = CaloriesDailyFragment.this.getActivity();
                    if (Validate.isActivityAlive(activity)) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("stepsAndCaloriesStartdate", userActivitySummary.getStartDate());
                        bundle.putSerializable("stepsAndCaloriesEnddate", userActivitySummary.getEndDate());
                        bundle.putBoolean("stepsAndCaloriesLevelTwoView", true);
                        bundle.putInt("stepsAndCaloriesHeaderValue", userActivitySummary.getValue());
                        ActivityUtils.launchLevelTwoActivity(activity, CaloriesDailyFragment.class, bundle);
                    }
                }
            }
        });
        this.mGlyph.setText(R.string.glyph_calories);
        this.mScrollView.setContentDescription(getResources().getString(R.string.trackable_scrollview_content_desc_calories_day));
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    protected GoalType getGoalType() {
        return GoalType.CALORIE;
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
    public int getGoalTextViewResourceId() {
        return R.id.goal_text;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment
    public int getGoalCommentViewResourceId() {
        return R.id.goal_comment;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment
    public int getGoalProgressBarViewResourceId() {
        return R.id.goal_progress_bar;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment
    public int getGoalEditViewResourceId() {
        return R.id.edit_goal_link;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment
    protected int getProgressTextResId() {
        return this.mCaloriesProgressTextResId;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment
    protected int getAchievementForTargetDate() {
        if (this.mIsL2View) {
            return this.mHeaderValue;
        }
        UserDailySummary userDailySummary = CommonUtils.getUserDailySummary(this.mHomeData);
        if (userDailySummary != null) {
            return userDailySummary.getCaloriesBurned();
        }
        return 0;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment
    protected void populateTrackers(List<UserActivity> activityList) {
        CaloriesDailyStat mCaloriesDailyStat = this.mStatUtils.getCaloriesDailyStat(activityList);
        String noValue = getResources().getString(R.string.no_value);
        CharSequence highestBurnValue = noValue;
        CharSequence lowestBurnValue = noValue;
        if (!isListNullOrEmpty(activityList)) {
            if (mCaloriesDailyStat.getHighestBurnActivity().getCaloriesBurned() != Integer.MIN_VALUE) {
                highestBurnValue = MetricSpannerUtils.formatTimeForLocale(getActivity().getApplicationContext(), this.mFontManager, mCaloriesDailyStat.getHighestBurnActivity().getTimeOfDay(), Locale.getDefault());
            }
            if (mCaloriesDailyStat.getLowestBurnActivity().getCaloriesBurned() != Integer.MAX_VALUE) {
                lowestBurnValue = MetricSpannerUtils.formatTimeForLocale(getActivity().getApplicationContext(), this.mFontManager, mCaloriesDailyStat.getLowestBurnActivity().getTimeOfDay(), Locale.getDefault());
            }
        }
        clearTrackerStats();
        setStatLabels();
        setStatValues(highestBurnValue, lowestBurnValue);
        if (this.mIsL2View) {
            updateActivityHeader(Formatter.formatCalories(getActivity(), this.mHeaderValue));
            return;
        }
        UserDailySummary userDailySummary = CommonUtils.getUserDailySummary(this.mHomeData);
        if (userDailySummary != null) {
            updateActivityHeader(Formatter.formatCalories(getActivity(), userDailySummary.getCaloriesBurned()));
        }
    }

    private void setStatLabels() {
        addSingleTracker(R.string.tracker_header_calories_highest_burn, R.string.glyph_arrow_up, "");
        addSingleTracker(R.string.tracker_header_calories_lowest_burn, R.string.glyph_arrow_down, "");
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment
    protected int getValueFromUserActivity(UserActivity userActivity) {
        return userActivity.getCaloriesBurned();
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
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public int getChartViewResourceId() {
        return R.id.chart_daily_fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public BaseChartFragment getDailyChartFragment(List<UserActivity> userActivityList) {
        double moderateCalsBurnPerHour = ProfileUtils.getModerateExerciseCaloriesBurnPerHour(this.mSettingsProvider);
        if (this.mChartFragment == null) {
            this.mChartFragment = CaloriesDailyChartFragment.newInstance(GoalsUtils.getGoalValue(getCurrentCaloriesGoalDto()), moderateCalsBurnPerHour);
        } else {
            this.mChartFragment.setDailyGoal(GoalsUtils.getGoalValue(getCurrentCaloriesGoalDto()));
            this.mChartFragment.setHighActivityThreshold(moderateCalsBurnPerHour);
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
        return ProfileUtils.getUserActivitySummaries(UserActivitySummary.DurationType.DAILY, date, profileCreatedDate, UserActivitySummary.ActivityType.CALORIES);
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public ArrayList<UserActivitySummary> getUserActivitySummaries(List<UserDailySummary> userDailySummaryList) {
        return ProfileUtils.getUserActivitySummaries(UserActivitySummary.DurationType.DAILY, userDailySummaryList, UserActivitySummary.ActivityType.CALORIES);
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    protected RaisedInsight.Pivot getDataUsedPivot() {
        return RaisedInsight.Pivot.CALORIES;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    protected RaisedInsight.Pivot getTimespanPivot() {
        return RaisedInsight.Pivot.DAY;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerDailyFragment
    public String getChartFragmentTag() {
        return CALORIES_DAILY_CHART_TAG;
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

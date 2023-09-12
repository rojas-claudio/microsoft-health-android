package com.microsoft.kapp.fragments.steps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.view.View;
import com.microsoft.band.cloud.UserProfileInfo;
import com.microsoft.kapp.FontManager;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.BaseTrackerWeeklyFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.UserActivitySummary;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.kapp.utils.GoalsUtils;
import com.microsoft.kapp.utils.ProfileUtils;
import com.microsoft.kapp.views.LinearListView;
import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.models.ExerciseEvent;
import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.GoalType;
import com.microsoft.krestsdk.models.GolfEvent;
import com.microsoft.krestsdk.models.RaisedInsight;
import com.microsoft.krestsdk.models.RunEvent;
import com.microsoft.krestsdk.models.SleepEvent;
import com.microsoft.krestsdk.models.UserActivity;
import com.microsoft.krestsdk.models.UserDailySummary;
import com.shinobicontrols.kcompanionapp.charts.DataProvider;
import com.shinobicontrols.kcompanionapp.charts.StepsWeeklyChartFragment;
import com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
@SuppressLint({"ValidFragment"})
/* loaded from: classes.dex */
public class StepWeeklyFragment extends BaseTrackerWeeklyFragment implements DataProvider {
    private HashMap<DateTime, Double> goalValues;
    private UserDailySummary[] mUserDailySummaryList;

    public StepWeeklyFragment() {
    }

    public StepWeeklyFragment(HomeData homeData) {
        this.mHomeData = homeData;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerWeeklyFragment, com.microsoft.kapp.fragments.BaseTrackerFragmentV1, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Validate.notNull(this.mSettingsProvider, "mSettingsProvider");
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerWeeklyFragment, com.microsoft.kapp.fragments.BaseTrackerFragmentV1, com.microsoft.kapp.fragments.BaseHomeTileFragment, android.support.v4.app.Fragment
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        this.mUserActivitySummaryListView.setOnItemClickListener(new LinearListView.OnItemClickListener() { // from class: com.microsoft.kapp.fragments.steps.StepWeeklyFragment.1
            @Override // com.microsoft.kapp.views.LinearListView.OnItemClickListener
            public void onItemClick(LinearListView adapterView, View view, int i, long l) {
                UserActivitySummary userActivitySummary = (UserActivitySummary) adapterView.getAdapter().getItem(i);
                if (userActivitySummary != null) {
                    Activity activity = StepWeeklyFragment.this.getActivity();
                    if (Validate.isActivityAlive(activity)) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("mSavedUserDailySummary", StepWeeklyFragment.this.mSavedUserDailySummary);
                        bundle.putSerializable("stepsAndCaloriesStartdate", userActivitySummary.getStartDate());
                        bundle.putSerializable("stepsAndCaloriesEnddate", userActivitySummary.getEndDate());
                        bundle.putBoolean("stepsAndCaloriesLevelTwoView", true);
                        ActivityUtils.launchLevelTwoActivity(activity, StepWeeklyFragment.class, bundle);
                    }
                }
            }
        });
        this.mGlyph.setText(R.string.glyph_steps);
        this.mScrollView.setContentDescription(getResources().getString(R.string.trackable_scrollview_content_desc_steps_week));
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerWeeklyFragment, com.microsoft.kapp.fragments.BaseHomeTileFragment
    public void fetchData() {
        super.fetchData();
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_STEPS_WEEKLY);
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerWeeklyFragment
    public void onUserSummaryUpdated(List<UserDailySummary> summary, LocalDate startDate, LocalDate endDate) {
        Activity activity = getActivity();
        this.mSavedUserDailySummary = new ArrayList<>(summary);
        setIsWaitingForUserDailySummaryUpdate(false);
        if (!Validate.isActivityAlive(activity)) {
            this.mInterstitial.setVisibility(8);
            return;
        }
        if (!isListNullOrEmpty(summary)) {
            int totalSteps = 0;
            int totalDistanceCM = 0;
            int totaluVExposure = 0;
            int totalFloorsClimbed = 0;
            UserDailySummary maxStepsDay = null;
            for (UserDailySummary day : summary) {
                totalSteps += day.getStepsTaken();
                totalDistanceCM += day.getTotalDistanceOnFoot();
                totalFloorsClimbed += day.getFloorsClimbed();
                totaluVExposure += day.getUvExposure();
                if (maxStepsDay == null || day.getStepsTaken() > maxStepsDay.getStepsTaken()) {
                    if (day.getStepsTaken() > 0) {
                        maxStepsDay = day;
                    }
                }
            }
            populateChart(summary);
            clearTrackerStats();
            addSingleTracker(R.string.tracker_header_steps_distance, R.string.glyph_distance, "");
            addSingleTracker(R.string.tracker_total_floors, R.string.glyph_stairs, "");
            addSingleTracker(R.string.tracker_header_steps_total_steps, R.string.glyph_steps, "");
            addSingleTracker(R.string.tracker_header_steps_most_active, R.string.glyph_arrow_up, "");
            addSingleTracker(R.string.tracker_uv_exposure, R.string.glyph_UV, "");
            CharSequence noValue = getString(R.string.no_value);
            Spannable distanceText = Formatter.formatDistance(getActivity(), R.array.MerticSmallUnitFormat, totalDistanceCM, this.mSettingsProvider.isDistanceHeightMetric(), true);
            CharSequence[] charSequenceArr = new CharSequence[5];
            charSequenceArr[0] = distanceText;
            charSequenceArr[1] = Formatter.formatFloorsClimbed(getActivity(), totalFloorsClimbed);
            charSequenceArr[2] = Formatter.formatIntegerValue(getActivity(), totalSteps);
            charSequenceArr[3] = maxStepsDay == null ? noValue : maxStepsDay.getTimeOfDay().dayOfWeek().getAsShortText();
            if (totaluVExposure > 0) {
                noValue = Formatter.formatUV(getActivity(), R.array.MerticSmallUnitFormat, totaluVExposure);
            }
            charSequenceArr[4] = noValue;
            setStatValues(charSequenceArr);
            updateActivityHeader(Formatter.formatSteps(getActivity(), totalSteps));
        }
        this.mInterstitial.setVisibility(8);
    }

    private static CharSequence getWalkTimeString(Context context, FontManager fontManager, int totalSteps, UserProfileInfo.Gender gender) {
        int stepRate = gender.equals(UserProfileInfo.Gender.female) ? context.getResources().getInteger(R.integer.step_rate_walking_female) : context.getResources().getInteger(R.integer.step_rate_walking_male);
        int walkTimeMinutes = ((totalSteps * 100) / stepRate) / 60;
        if (walkTimeMinutes >= 60) {
            int walkTimeHours = walkTimeMinutes / 60;
            CharSequence walkTimeText = MetricSpannerUtils.formatTimeShort(context, fontManager, walkTimeHours, walkTimeMinutes - (walkTimeHours * 60), -1);
            return walkTimeText;
        }
        CharSequence walkTimeText2 = MetricSpannerUtils.formatMinuteShort(context, fontManager, walkTimeMinutes);
        return walkTimeText2;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public int getStatsViewResourceId() {
        return R.id.stats;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public int getScrollViewResourceId() {
        return R.id.trackerScrollView;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    protected HomeData getHomeData() {
        return this.mHomeData;
    }

    protected void populateChart(List<UserDailySummary> activityList) {
        this.mUserDailySummaryList = (UserDailySummary[]) activityList.toArray(new UserDailySummary[activityList.size()]);
        updateChartGoals();
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public int getChartViewResourceId() {
        return R.id.weekly_chart_fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public BaseChartFragment getWeeklyChartFragment(List<UserDailySummary> userActivityList) {
        BaseChartFragment chartFragment = StepsWeeklyChartFragment.newInstance(this.goalValues);
        chartFragment.setDataProvider(this);
        return chartFragment;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
    public UserActivity[] getHourlyUserActivitiesForDay() {
        return null;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
    public UserDailySummary[] getDailySummariesForWeek() {
        return this.mUserDailySummaryList;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerWeeklyFragment, com.microsoft.kapp.fragments.BaseTrackerFragmentV1, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
    public RunEvent getRunEvent() {
        return null;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public ArrayList<UserActivitySummary> getUserActivitySummaries(LocalDate date, LocalDate profileCreatedDate) {
        return ProfileUtils.getUserActivitySummaries(UserActivitySummary.DurationType.WEEKLY, date, profileCreatedDate, UserActivitySummary.ActivityType.STEPS);
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public ArrayList<UserActivitySummary> getUserActivitySummaries(List<UserDailySummary> userDailySummaryList) {
        return ProfileUtils.getUserActivitySummaries(UserActivitySummary.DurationType.WEEKLY, userDailySummaryList, UserActivitySummary.ActivityType.STEPS);
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    protected RaisedInsight.Pivot getDataUsedPivot() {
        return RaisedInsight.Pivot.STEPS;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    protected RaisedInsight.Pivot getTimespanPivot() {
        return RaisedInsight.Pivot.WEEK;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
    public SleepEvent getSleepEvent() {
        return null;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
    public ExerciseEvent getExerciseEvent() {
        return null;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    protected GoalType getGoalType() {
        return GoalType.STEP;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    protected void updateChartGoals() {
        double goalValue;
        try {
            if (!isWaitingForUserDailySummaryUpdate() && this.mUserDailySummaryList != null) {
                if (this.goalValues != null) {
                    this.goalValues.clear();
                } else {
                    this.goalValues = new HashMap<>();
                }
                UserDailySummary[] arr$ = this.mUserDailySummaryList;
                for (UserDailySummary summary : arr$) {
                    DateTime date = summary.getTimeOfDay();
                    if (CommonUtils.isDateToday(date)) {
                        goalValue = GoalsUtils.getGoalValue(getCurrentStepsGoalDto());
                    } else {
                        goalValue = getGoalValueForDate(date);
                    }
                    this.goalValues.put(date, Double.valueOf(goalValue));
                }
                addChartFragment(getChartViewResourceId(), getWeeklyChartFragment(null), "StepsWeeklyFragmentTag");
            }
        } catch (Exception ex) {
            KLog.d(this.TAG, "unable to load calories weekly chart", ex);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public GoalDto getCurrentCaloriesGoalDto() {
        return this.mHomeData.getGoal(GoalType.CALORIE);
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public GoalDto getCurrentStepsGoalDto() {
        return this.mHomeData.getGoal(GoalType.STEP);
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

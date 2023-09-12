package com.microsoft.kapp.fragments.calories;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.BaseTrackerWeeklyFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.UserActivitySummary;
import com.microsoft.kapp.models.home.HomeData;
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
import com.shinobicontrols.kcompanionapp.charts.CaloriesWeeklyChartFragment;
import com.shinobicontrols.kcompanionapp.charts.DataProvider;
import com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
@SuppressLint({"ValidFragment"})
/* loaded from: classes.dex */
public class CaloriesWeeklyFragment extends BaseTrackerWeeklyFragment implements DataProvider {
    private HashMap<DateTime, Double> goalValues;
    private UserDailySummary[] mUserDailySummaryList;

    public CaloriesWeeklyFragment() {
    }

    public CaloriesWeeklyFragment(HomeData homeData) {
        this.mHomeData = homeData;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerWeeklyFragment, com.microsoft.kapp.fragments.BaseTrackerFragmentV1, com.microsoft.kapp.fragments.BaseHomeTileFragment, android.support.v4.app.Fragment
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        this.mUserActivitySummaryListView.setOnItemClickListener(new LinearListView.OnItemClickListener() { // from class: com.microsoft.kapp.fragments.calories.CaloriesWeeklyFragment.1
            @Override // com.microsoft.kapp.views.LinearListView.OnItemClickListener
            public void onItemClick(LinearListView adapterView, View view, int i, long l) {
                UserActivitySummary userActivitySummary = (UserActivitySummary) adapterView.getAdapter().getItem(i);
                if (userActivitySummary != null) {
                    Activity activity = CaloriesWeeklyFragment.this.getActivity();
                    if (Validate.isActivityAlive(activity)) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("mSavedUserDailySummary", CaloriesWeeklyFragment.this.mSavedUserDailySummary);
                        bundle.putSerializable("stepsAndCaloriesStartdate", userActivitySummary.getStartDate());
                        bundle.putSerializable("stepsAndCaloriesEnddate", userActivitySummary.getEndDate());
                        bundle.putBoolean("stepsAndCaloriesLevelTwoView", true);
                        ActivityUtils.launchLevelTwoActivity(activity, CaloriesWeeklyFragment.class, bundle);
                    }
                }
            }
        });
        this.mGlyph.setText(R.string.glyph_calories);
        this.mScrollView.setContentDescription(getResources().getString(R.string.trackable_scrollview_content_desc_calories_week));
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_CALORIES_WEEKLY);
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
            int totalCalories = 0;
            UserDailySummary mostBurned = new UserDailySummary();
            mostBurned.setCaloriesBurned(Integer.MIN_VALUE);
            for (UserDailySummary day : summary) {
                totalCalories += day.getCaloriesBurned();
                if (mostBurned.getCaloriesBurned() < day.getCaloriesBurned()) {
                    mostBurned = day;
                }
            }
            populateChart(summary);
            clearTrackerStats();
            setStatLabels();
            DateTimeFormatter builder = DateTimeFormat.forPattern(getHostActivity().getResources().getString(R.string.tracker_value_format_calories_most_burned));
            String mostBurnedDay = getResources().getString(R.string.no_value);
            if (totalCalories != 0) {
                mostBurnedDay = builder.print(mostBurned.getTimeOfDay());
            }
            setStatValues(Formatter.formatIntegerValue(getActivity(), totalCalories), mostBurnedDay);
            updateActivityHeader(Formatter.formatCalories(getActivity(), totalCalories));
        }
        this.mInterstitial.setVisibility(8);
    }

    private void setStatLabels() {
        addSingleTracker(R.string.tracker_header_calories_total_burn, R.string.glyph_calories, "");
        addSingleTracker(R.string.tracker_header_calories_most_burned, R.string.glyph_arrow_up, "");
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
    protected Activity getHostActivity() {
        return getActivity();
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
        BaseChartFragment chartFragment = CaloriesWeeklyChartFragment.newInstance(this.goalValues);
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
        return ProfileUtils.getUserActivitySummaries(UserActivitySummary.DurationType.WEEKLY, date, profileCreatedDate, UserActivitySummary.ActivityType.CALORIES);
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public ArrayList<UserActivitySummary> getUserActivitySummaries(List<UserDailySummary> userDailySummaryList) {
        return ProfileUtils.getUserActivitySummaries(UserActivitySummary.DurationType.WEEKLY, userDailySummaryList, UserActivitySummary.ActivityType.CALORIES);
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    protected RaisedInsight.Pivot getDataUsedPivot() {
        return RaisedInsight.Pivot.CALORIES;
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
        return GoalType.CALORIE;
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
                        goalValue = GoalsUtils.getGoalValue(getCurrentCaloriesGoalDto());
                    } else {
                        goalValue = getGoalValueForDate(date);
                    }
                    this.goalValues.put(date, Double.valueOf(goalValue));
                }
                addChartFragment(getChartViewResourceId(), getWeeklyChartFragment(null), "CaloriesWeeklyFragmentTag");
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

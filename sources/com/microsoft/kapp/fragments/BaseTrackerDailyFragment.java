package com.microsoft.kapp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.activities.CaloriesEditGoalActivity;
import com.microsoft.kapp.activities.ObservableGoalsManager;
import com.microsoft.kapp.activities.StepsEditGoalActivity;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.ScrollLoadStatus;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.GoalsUtils;
import com.microsoft.kapp.utils.KAppDateFormatter;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.TrackableScrollView;
import com.microsoft.kapp.widgets.Interstitial;
import com.microsoft.krestsdk.models.ExerciseEvent;
import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.GoalType;
import com.microsoft.krestsdk.models.SleepEvent;
import com.microsoft.krestsdk.models.UserActivity;
import com.microsoft.krestsdk.models.UserDailySummary;
import com.shinobicontrols.kcompanionapp.charts.DataProvider;
import com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
/* loaded from: classes.dex */
public abstract class BaseTrackerDailyFragment extends BaseTrackerFragmentV1 implements DataProvider {
    private static final String EDIT_GOAL_INTENT_REQUEST = "mEditGoalIntentRequest";
    private static final String SAVED_TARGET_DATE_USER_ACTIVITY = "mSavedTargetDateUserActivity";
    private static final String WAITING_GOAL_HISTORY_DATA = "mWaitingGoalHistoryData";
    private int mEditGoalIntentRequest;
    protected LocalDate mEndDate;
    private TextView mGoalComment;
    private TextView mGoalEdit;
    private ProgressBar mGoalProgressBar;
    private int mGoalProgressTextResId;
    private TextView mGoalText;
    protected Interstitial mInterstitial;
    private ArrayList<UserActivity> mSavedTargetDateUserActivity;
    protected LocalDate mStartDate;
    private UserActivity[] mUserActivityList;

    protected abstract int getAchievementForTargetDate();

    public abstract String getChartFragmentTag();

    public abstract GoalDto getCurrentGoalDto();

    protected abstract int getGoalCommentViewResourceId();

    protected abstract int getGoalEditViewResourceId();

    protected abstract int getGoalProgressBarViewResourceId();

    protected abstract int getGoalTextViewResourceId();

    public abstract ObservableGoalsManager getObservableDataManager();

    protected abstract int getProgressTextResId();

    protected abstract int getValueFromUserActivity(UserActivity userActivity);

    protected abstract void populateTrackers(List<UserActivity> list);

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1, com.microsoft.kapp.fragments.BaseHomeTileFragment, android.support.v4.app.Fragment
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        if (savedInstanceState != null) {
            this.mIsRecoveringSavedState = true;
            this.mSavedTargetDateUserActivity = savedInstanceState.getParcelableArrayList(SAVED_TARGET_DATE_USER_ACTIVITY);
            this.mWaitingGoalHistoryData = savedInstanceState.getBoolean(WAITING_GOAL_HISTORY_DATA, true);
            this.mEditGoalIntentRequest = savedInstanceState.getInt(EDIT_GOAL_INTENT_REQUEST);
        }
        this.mGoalProgressBar = (ProgressBar) ViewUtils.getValidView(v, getGoalProgressBarViewResourceId(), ProgressBar.class);
        this.mGoalText = (TextView) ViewUtils.getValidView(v, getGoalTextViewResourceId(), TextView.class);
        this.mGoalComment = (TextView) ViewUtils.getValidView(v, getGoalCommentViewResourceId(), TextView.class);
        this.mGoalEdit = (TextView) ViewUtils.getValidView(v, getGoalEditViewResourceId(), TextView.class);
        this.mGoalProgressTextResId = getProgressTextResId();
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment
    public void fetchData() {
        setState(1233);
        try {
            this.mGoalHistory = HomeData.getInstance().getGoalsHistory().get(getGoalType());
            if (this.mGoalHistory == null) {
                fetchGoals();
            } else {
                this.mWaitingGoalHistoryData = false;
                updateChartGoals();
            }
            this.mScrollView.setOnHitBottomListener(new TrackableScrollView.OnHitBottomListener() { // from class: com.microsoft.kapp.fragments.BaseTrackerDailyFragment.1
                @Override // com.microsoft.kapp.views.TrackableScrollView.OnHitBottomListener
                public void onHitBottom() {
                    if (!BaseTrackerDailyFragment.this.mIsL2View && BaseTrackerDailyFragment.this.mTotalLoadStatus == ScrollLoadStatus.PARTIAL && BaseTrackerDailyFragment.this.mUserActivitySummaryListView != null && BaseTrackerDailyFragment.this.mUserActivitySummaryListView.getLastItem() != null) {
                        LocalDate nextStartDate = BaseTrackerDailyFragment.this.mUserActivitySummaryListView.getLastItem().getStartDate().plusDays(-1);
                        if (nextStartDate != null && !nextStartDate.isBefore(BaseTrackerDailyFragment.this.mProfileCreatedDate)) {
                            LocalDate endDate = nextStartDate.plusDays(-10);
                            if (endDate.isBefore(BaseTrackerDailyFragment.this.mProfileCreatedDate)) {
                                endDate = new LocalDate(BaseTrackerDailyFragment.this.mProfileCreatedDate);
                            }
                            BaseTrackerDailyFragment.this.fetchMoreStepsAndCaloriesDailySummaries(endDate, nextStartDate);
                            return;
                        }
                        BaseTrackerDailyFragment.this.setTotalLoadStatus(ScrollLoadStatus.COMPLETE);
                    }
                }
            });
            hideGoalPanel();
            hideTrackerStats();
            LocalDate startDate = this.mEndDate == null ? LocalDate.now() : this.mEndDate;
            setTargetDate(startDate.toDateTime(new LocalTime()));
            hideTrackerStats();
            HomeData homeData = getHomeData();
            if (homeData != null && startDate.equals(homeData.getTargetDate())) {
                this.mIsRecoveringSavedState = false;
                retriveDatafromHomedata(homeData, startDate);
                loadInsight();
                if (!this.mIsL2View) {
                    fetchTopStepsAndCaloriesDailySummaries(startDate);
                }
            } else if (this.mIsRecoveringSavedState) {
                this.mIsRecoveringSavedState = false;
                if (this.mSavedTargetDateUserActivity != null && this.mSavedTargetDateUserActivity.size() > 0) {
                    onUserActivityUpdated(this.mSavedTargetDateUserActivity);
                    showTrackerStats();
                    if (!this.mIsL2View) {
                        fetchTopStepsAndCaloriesDailySummaries(startDate);
                    }
                } else {
                    fetchUserActivity(startDate);
                    if (!this.mIsL2View) {
                        fetchTopStepsAndCaloriesDailySummaries(startDate);
                    }
                }
            } else {
                fetchUserActivity(startDate);
                if (!this.mIsL2View) {
                    fetchTopStepsAndCaloriesDailySummaries(startDate);
                }
            }
            setState(1234);
        } catch (Exception ex) {
            KLog.e(this.TAG, "Exception in basetrackerfragment", ex);
            setState(1235);
        }
    }

    protected void fetchUserActivity(final LocalDate date) {
        this.mService.getUserActivitiesByHour(date, new ActivityScopedCallback(this, new Callback<List<UserActivity>>() { // from class: com.microsoft.kapp.fragments.BaseTrackerDailyFragment.2
            @Override // com.microsoft.kapp.Callback
            public void callback(List<UserActivity> response) {
                BaseTrackerDailyFragment.this.showTrackerStats();
                if (response != null && response.size() > 0) {
                    BaseTrackerDailyFragment.this.onUserActivityUpdated(response);
                } else {
                    BaseTrackerDailyFragment.this.onUserActivityUpdated(CommonUtils.getEmptyUserActivity(date.toDateTimeAtStartOfDay()));
                }
                BaseTrackerDailyFragment.this.setState(1234);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                BaseTrackerDailyFragment.this.setState(1235);
            }
        }));
    }

    public int getCurrentGoal() {
        if (this.mGoalHistory == null || this.mGoalHistory.size() == 0) {
            return -1;
        }
        return this.mGoalHistory.valueAt(this.mGoalHistory.size() - 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startEditGoalActivity() {
        GoalDto CurrentgoalDto;
        Intent intent;
        Bundle data = new Bundle();
        GoalDto caloriesGoalDto = getCurrentCaloriesGoalDto();
        GoalDto stepsGoalDto = getCurrentStepsGoalDto();
        switch (getGoalType()) {
            case CALORIE:
                CurrentgoalDto = caloriesGoalDto;
                intent = new Intent(getActivity(), CaloriesEditGoalActivity.class);
                this.mEditGoalIntentRequest = 105;
                break;
            case STEP:
                CurrentgoalDto = stepsGoalDto;
                intent = new Intent(getActivity(), StepsEditGoalActivity.class);
                this.mEditGoalIntentRequest = 106;
                break;
            default:
                KLog.d(this.TAG, "GoalType unknown! EditGoalActivity is not started!");
                return;
        }
        data.putString(Constants.GOAL_ID, CurrentgoalDto.getId());
        data.putString(Constants.GOAL_NAME, GoalsUtils.getGoalName(CurrentgoalDto));
        data.putInt(Constants.GOAL_VALUE, GoalsUtils.getGoalValue(CurrentgoalDto));
        data.putInt(Constants.CALORIES_GOAL_VALUE, GoalsUtils.getGoalValue(caloriesGoalDto));
        data.putInt(Constants.STEPS_GOAL_VALUE, GoalsUtils.getGoalValue(stepsGoalDto));
        intent.putExtras(data);
        getParentFragment().startActivityForResult(intent, this.mEditGoalIntentRequest);
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public void onGoalValueUpdated(GoalType dataType, HomeData homeData) {
        if (isTargetDateToday()) {
            GoalDto goal = homeData.getGoal(getGoalType());
            if (goal != null) {
                int todayGoalValue = GoalsUtils.getGoalValue(goal);
                showGoals(todayGoalValue, getAchievementForTargetDate());
                return;
            }
            showGoals(-1, -1);
        }
    }

    protected void showGoals(int goalValue, int progress) {
        if (goalValue == 0 || goalValue == -1) {
            goalValue = 1;
            progress = 0;
        }
        showGoalPanel();
        this.mGoalProgressBar.setMax(goalValue);
        this.mGoalProgressBar.setProgress(progress);
        CharSequence goalText = GoalsUtils.formatGoalText(this.mFontManager, Integer.toString((progress * 100) / goalValue).concat("% "), getHostActivity().getResources().getString(this.mGoalProgressTextResId));
        this.mGoalText.setText(goalText);
    }

    protected void showGoalPanel() {
        this.mGoalText.setVisibility(0);
        this.mGoalComment.setVisibility(0);
        this.mGoalProgressBar.setVisibility(0);
        this.mGoalEdit.setVisibility(0);
        if (isTargetDateToday()) {
            this.mGoalEdit.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.BaseTrackerDailyFragment.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    BaseTrackerDailyFragment.this.startEditGoalActivity();
                }
            });
            this.mGoalEdit.setAlpha(1.0f);
            return;
        }
        this.mGoalEdit.setVisibility(8);
    }

    protected void hideGoalPanel() {
        this.mGoalText.setVisibility(8);
        this.mGoalComment.setVisibility(8);
        this.mGoalProgressBar.setVisibility(8);
        this.mGoalEdit.setVisibility(8);
    }

    public void onUserActivityUpdated(List<UserActivity> activityList) {
        Activity activity = getActivity();
        this.mSavedTargetDateUserActivity = new ArrayList<>(activityList);
        if (!Validate.isActivityAlive(activity)) {
            if (this.mInterstitial != null) {
                this.mInterstitial.setVisibility(8);
                return;
            }
            return;
        }
        if (!isListNullOrEmpty(activityList)) {
            if (DateTimeComparator.getDateOnlyInstance().compare(activityList.get(0).getTimeOfDay(), this.mTargetDate) != 0) {
                if (this.mInterstitial != null) {
                    this.mInterstitial.setVisibility(8);
                    return;
                }
                return;
            }
            populateChart(activityList);
            populateTrackers(activityList);
            showTrackerStats();
            updateChartGoals();
        }
        if (this.mInterstitial != null) {
            this.mInterstitial.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public void updateChartGoals() {
        DateTime historyDay = this.mStartDate != null ? this.mStartDate.toDateTimeAtStartOfDay() : this.mTargetDate;
        if (!this.mWaitingGoalHistoryData && !CommonUtils.isDateToday(historyDay)) {
            if (historyDay == null) {
                KLog.e(this.TAG, "An error happened...TargetDate cannot be null");
                return;
            }
            int goalValue = getGoalValueForDate(historyDay);
            if (goalValue == -1) {
                KLog.e(this.TAG, "An error happened...unable to retrieve goal history for date %s", this.mTargetDate.toString());
            }
            showGoals(goalValue, getAchievementForTargetDate());
        }
    }

    protected void populateChart(List<UserActivity> activityList) {
        this.mUserActivityList = new UserActivity[24];
        for (UserActivity userActivity : activityList) {
            DateTime dateTime = userActivity.getTimeOfDay();
            this.mUserActivityList[dateTime.getHourOfDay()] = userActivity;
        }
        for (int i = 0; i < 24; i++) {
            if (this.mUserActivityList[i] == null) {
                this.mUserActivityList[i] = new UserActivity();
            }
        }
        try {
            BaseChartFragment chartFragment = getDailyChartFragment(activityList);
            if (chartFragment != null) {
                addChartFragment(getChartViewResourceId(), chartFragment, getChartFragmentTag());
            }
        } catch (Exception ex) {
            KLog.d(this.TAG, "unable to load daily chart", ex);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(WAITING_GOAL_HISTORY_DATA, this.mWaitingGoalHistoryData);
        outState.putParcelableArrayList(SAVED_TARGET_DATE_USER_ACTIVITY, this.mSavedTargetDateUserActivity);
        outState.putInt(EDIT_GOAL_INTENT_REQUEST, this.mEditGoalIntentRequest);
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
    public UserActivity[] getHourlyUserActivitiesForDay() {
        return this.mUserActivityList;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
    public UserDailySummary[] getDailySummariesForWeek() {
        return null;
    }

    public void updateActivityHeader(Spannable totalSteps) {
        if (this.mValueTextView != null) {
            this.mValueTextView.setText(totalSteps);
        }
        if (this.mDateTextView != null && this.mEndDate != null) {
            String endDateFormatted = KAppDateFormatter.formatToMonthWithDay(getActivity().getResources(), this.mEndDate);
            this.mDateTextView.setText(endDateFormatted);
        }
    }

    private void fetchTopStepsAndCaloriesDailySummaries(LocalDate startDate) {
        setTotalLoadStatus(ScrollLoadStatus.LOADING);
        if (startDate != null && !startDate.isBefore(this.mProfileCreatedDate)) {
            LocalDate endDate = startDate.plusDays(-10);
            if (endDate.isBefore(this.mProfileCreatedDate)) {
                endDate = new LocalDate(this.mProfileCreatedDate);
            }
            this.mService.getDailySummaries(endDate, startDate, new ActivityScopedCallback(this, new Callback<List<UserDailySummary>>() { // from class: com.microsoft.kapp.fragments.BaseTrackerDailyFragment.4
                @Override // com.microsoft.kapp.Callback
                public void callback(List<UserDailySummary> response) {
                    if (BaseTrackerDailyFragment.this.mUserActivitySummaryListView != null) {
                        BaseTrackerDailyFragment.this.mUserActivitySummaryListView.setItems(BaseTrackerDailyFragment.this.getUserActivitySummaries(response));
                        BaseTrackerDailyFragment.this.setScrollViewPosition(BaseTrackerDailyFragment.this.mScrollPosition);
                    }
                    BaseTrackerDailyFragment.this.setState(1234);
                    BaseTrackerDailyFragment.this.setTotalLoadStatus(ScrollLoadStatus.PARTIAL);
                }

                @Override // com.microsoft.kapp.Callback
                public void onError(Exception ex) {
                    BaseTrackerDailyFragment.this.setState(1235);
                    BaseTrackerDailyFragment.this.setTotalLoadStatus(ScrollLoadStatus.PARTIAL);
                    KLog.w(BaseTrackerDailyFragment.this.TAG, "Error while fetching UserDailySummary from the cloud", ex);
                }
            }));
            return;
        }
        setTotalLoadStatus(ScrollLoadStatus.COMPLETE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchMoreStepsAndCaloriesDailySummaries(LocalDate startDate, LocalDate endDate) {
        setTotalLoadStatus(ScrollLoadStatus.LOADING);
        this.mService.getDailySummaries(startDate, endDate.plusDays(1), new ActivityScopedCallback(this, new Callback<List<UserDailySummary>>() { // from class: com.microsoft.kapp.fragments.BaseTrackerDailyFragment.5
            @Override // com.microsoft.kapp.Callback
            public void callback(List<UserDailySummary> response) {
                if (BaseTrackerDailyFragment.this.mUserActivitySummaryListView != null && response != null && !response.isEmpty()) {
                    BaseTrackerDailyFragment.this.mUserActivitySummaryListView.addItems(BaseTrackerDailyFragment.this.getUserActivitySummaries(response));
                    BaseTrackerDailyFragment.this.setTotalLoadStatus(ScrollLoadStatus.PARTIAL);
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.w(BaseTrackerDailyFragment.this.TAG, "Error while fetching UserDailySummary from the cloud", ex);
                BaseTrackerDailyFragment.this.setTotalLoadStatus(ScrollLoadStatus.PARTIAL);
            }
        }));
    }

    private void retriveDatafromHomedata(HomeData homeData, LocalDate startDate) {
        List<UserActivity> userActivityList = homeData.getUserActivities();
        if (userActivityList != null && userActivityList.size() > 0) {
            onUserActivityUpdated(userActivityList);
        } else {
            onUserActivityUpdated(CommonUtils.getEmptyUserActivity(startDate.toDateTimeAtStartOfDay()));
        }
        GoalDto goal = homeData.getGoal(getGoalType());
        if (goal != null) {
            int todayGoalValue = GoalsUtils.getGoalValue(goal);
            showGoals(todayGoalValue, getAchievementForTargetDate());
            return;
        }
        showGoals(-1, -1);
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
    public BaseChartFragment getWeeklyChartFragment(List<UserDailySummary> activityList) {
        return null;
    }
}

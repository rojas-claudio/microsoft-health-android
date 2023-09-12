package com.microsoft.kapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.RelativeLayout;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.OnGoalsChangedListener;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.ScrollLoadStatus;
import com.microsoft.kapp.models.UserActivitySummary;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.parsers.BasicRaisedInsightFilters;
import com.microsoft.kapp.parsers.InsightsDisplayFilter;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.tasks.GoalsGetTask;
import com.microsoft.kapp.tasks.StateListenerTask;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomFontTextView;
import com.microsoft.kapp.views.CustomGlyphView;
import com.microsoft.kapp.views.InsightsWidget;
import com.microsoft.kapp.views.ScrollLoadIndicatorView;
import com.microsoft.kapp.views.SingleTrackerStat;
import com.microsoft.kapp.views.TrackableScrollView;
import com.microsoft.kapp.views.TrackerStatsWidget;
import com.microsoft.kapp.views.UserActivitySummaryListView;
import com.microsoft.krestsdk.auth.credentials.AccountMetadata;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.models.CategoryType;
import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.GoalType;
import com.microsoft.krestsdk.models.GoalValueHistoryDto;
import com.microsoft.krestsdk.models.GoalValueRecordDto;
import com.microsoft.krestsdk.models.RaisedInsight;
import com.microsoft.krestsdk.models.UserActivity;
import com.microsoft.krestsdk.models.UserDailySummary;
import com.microsoft.krestsdk.services.RaisedInsightQuery;
import com.microsoft.krestsdk.services.RestService;
import com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
/* loaded from: classes.dex */
public abstract class BaseTrackerFragmentV1 extends BaseHomeTileFragment implements GoalsGetTask.OnGoalsRetrieveTaskListener, OnGoalsChangedListener {
    protected static final String CALORIES_WEEKLY_FRAGMENT_TAG = "CaloriesWeeklyFragmentTag";
    private static final int DAYS_PER_YEAR = 366;
    private static final boolean IS_EXPAND = true;
    private static final String KEY_SCROLL_POSITION = "scroll_position";
    protected static final String STEPS_AND_CALORIES_END_DATE = "stepsAndCaloriesEnddate";
    protected static final String STEPS_AND_CALORIES_HEADER_VALUE = "stepsAndCaloriesHeaderValue";
    protected static final String STEPS_AND_CALORIES_L2_VIEW = "stepsAndCaloriesLevelTwoView";
    protected static final String STEPS_AND_CALORIES_START_DATE = "stepsAndCaloriesStartdate";
    protected static final String STEPS_WEEKLY_FRAGMENT_TAG = "StepsWeeklyFragmentTag";
    private static final String TARGET_DATE = "mTargetDate";
    private static boolean isGoalsUpdated;
    protected CustomGlyphView mBackButton;
    @Inject
    CredentialsManager mCredentials;
    protected CustomFontTextView mDateTextView;
    protected CustomGlyphView mGlyph;
    protected SparseIntArray mGoalHistory;
    protected GoalsGetTask mGoalsGetTask;
    protected int mHeaderValue;
    protected RelativeLayout mHeaderlayout;
    protected HomeData mHomeData;
    protected InsightsWidget mInsightWidget;
    protected boolean mIsL2View;
    protected boolean mIsRecoveringSavedState;
    protected LocalDate mProfileCreatedDate;
    protected ScrollLoadIndicatorView mScrollLoadIndicator;
    protected int mScrollPosition;
    protected TrackableScrollView mScrollView;
    @Inject
    RestService mService;
    @Inject
    protected SettingsProvider mSettingsProvider;
    private int mStatStyleResourceId;
    protected ScrollLoadStatus mTotalLoadStatus;
    protected TrackerStatsWidget mTrackerStatsWidget;
    protected UserActivitySummaryListView mUserActivitySummaryListView;
    protected CustomFontTextView mValueTextView;
    SparseArray<Float> mValues;
    protected boolean mWaitingGoalHistoryData = true;
    protected DateTime mTargetDate = DateTime.now();

    public abstract int getChartViewResourceId();

    public abstract GoalDto getCurrentCaloriesGoalDto();

    public abstract GoalDto getCurrentStepsGoalDto();

    public abstract BaseChartFragment getDailyChartFragment(List<UserActivity> list);

    protected abstract RaisedInsight.Pivot getDataUsedPivot();

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract GoalType getGoalType();

    public abstract int getScrollViewResourceId();

    public abstract int getStatsViewResourceId();

    protected abstract RaisedInsight.Pivot getTimespanPivot();

    public abstract ArrayList<UserActivitySummary> getUserActivitySummaries(List<UserDailySummary> list);

    public abstract ArrayList<UserActivitySummary> getUserActivitySummaries(LocalDate localDate, LocalDate localDate2);

    public abstract BaseChartFragment getWeeklyChartFragment(List<UserDailySummary> list);

    protected abstract void onGoalValueUpdated(GoalType goalType, HomeData homeData);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void updateChartGoals();

    protected int getInsightWidgetResourceId() {
        return R.id.insight_widget;
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Validate.notNull(this.mCredentials, "mCredentials");
        AccountMetadata accountMetadata = this.mCredentials.getAccountMetada();
        if (accountMetadata == null || accountMetadata.getProfileCreationDate() == null) {
            this.mProfileCreatedDate = LocalDate.now().plusWeeks(-2);
            KLog.w(this.TAG, "Unable to retrieve ProfileCreatedDate locally. Added a default date - two weeks before current date");
            return;
        }
        this.mProfileCreatedDate = accountMetadata.getProfileCreationDate();
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, android.support.v4.app.Fragment
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        if (this.mHeaderlayout != null && this.mUserActivitySummaryListView != null) {
            if (this.mIsL2View) {
                this.mHeaderlayout.setVisibility(0);
                this.mUserActivitySummaryListView.setVisibility(8);
            } else {
                this.mHeaderlayout.setVisibility(8);
                this.mUserActivitySummaryListView.setVisibility(0);
            }
        }
        this.mTrackerStatsWidget = (TrackerStatsWidget) ViewUtils.getValidView(v, getStatsViewResourceId(), TrackerStatsWidget.class);
        this.mStatStyleResourceId = this.mTrackerStatsWidget.getStatStyleResourceId();
        this.mInsightWidget = (InsightsWidget) ViewUtils.getValidView(v, getInsightWidgetResourceId(), InsightsWidget.class);
        this.mScrollView = (TrackableScrollView) ViewUtils.getValidView(v, getScrollViewResourceId(), TrackableScrollView.class);
        if (savedInstanceState != null) {
            this.mScrollPosition = savedInstanceState.getInt(KEY_SCROLL_POSITION, 0);
            this.mTargetDate = new DateTime(savedInstanceState.getLong(TARGET_DATE));
        } else {
            this.mScrollPosition = 0;
        }
        setScrollViewPosition(this.mScrollPosition);
        this.mGoalsGetTask = new GoalsGetTask.Builder().forParentFragment(this).usingRestService(this.mService).withListener(this).build(true, CategoryType.PERSONALGOAL, getGoalType());
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCROLL_POSITION, getScrollViewPosition());
        if (this.mTargetDate != null) {
            outState.putLong(TARGET_DATE, this.mTargetDate.getMillis());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setStatValues(CharSequence... values) {
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                SingleTrackerStat stat = (SingleTrackerStat) this.mTrackerStatsWidget.getStat(i);
                if (stat != null) {
                    stat.setValue(values[i]);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void hideTrackerStats() {
        this.mTrackerStatsWidget.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void showTrackerStats() {
        this.mTrackerStatsWidget.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void clearTrackerStats() {
        this.mTrackerStatsWidget.clearStats();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setScrollViewPosition(final int position) {
        this.mScrollView.post(new Runnable() { // from class: com.microsoft.kapp.fragments.BaseTrackerFragmentV1.1
            @Override // java.lang.Runnable
            public void run() {
                BaseTrackerFragmentV1.this.mScrollView.setScrollY(position);
            }
        });
    }

    private int getScrollViewPosition() {
        if (this.mScrollView != null) {
            return this.mScrollView.getScrollY();
        }
        return 0;
    }

    protected int getDayOfCentury(DateTime dateTime) {
        Validate.notNull(dateTime, "DateTime");
        return (dateTime.getYearOfCentury() * DAYS_PER_YEAR) + dateTime.getDayOfYear();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setTargetDate(DateTime targetDate) {
        this.mTargetDate = targetDate;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Activity getHostActivity() {
        return getActivity();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public <T> boolean isListNullOrEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }

    protected void addSingleTracker(int titleResourceId, CharSequence value) {
        addSingleTracker(titleResourceId, -1, value);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void addSingleTracker(int titleResourceId, int titleSymbolResourceId, CharSequence value) {
        SingleTrackerStat tracker = new SingleTrackerStat(getHostActivity(), getString(titleResourceId), titleSymbolResourceId);
        tracker.setValue(value);
        this.mTrackerStatsWidget.addStat(tracker);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isTargetDateToday() {
        return CommonUtils.isDateToday(this.mTargetDate);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public HomeData getHomeData() {
        return this.mHomeData;
    }

    public void setTotalLoadStatus(ScrollLoadStatus status) {
        this.mTotalLoadStatus = status;
        if (this.mScrollLoadIndicator != null) {
            this.mScrollLoadIndicator.setStatus(status);
        }
    }

    public void loadInsight() {
        if (this.mSettingsProvider.isRaisedInsightsEnabled()) {
            RaisedInsightQuery query = new RaisedInsightQuery();
            RaisedInsight.Pivot dataUsedPivot = getDataUsedPivot();
            RaisedInsight.Pivot timespanPivot = getTimespanPivot();
            query.setDataUsed(dataUsedPivot.toString());
            query.setTimeSpan(timespanPivot.toString());
            query.setScope(RaisedInsight.Pivot.GLOBAL.toString());
            this.mService.getRaisedInsights(new Callback<List<RaisedInsight>>() { // from class: com.microsoft.kapp.fragments.BaseTrackerFragmentV1.2
                @Override // com.microsoft.kapp.Callback
                public void callback(List<RaisedInsight> result) {
                    RaisedInsight insight;
                    if (result != null && !result.isEmpty() && (insight = BaseTrackerFragmentV1.this.getInsight(result)) != null) {
                        BaseTrackerFragmentV1.this.showInsight(insight);
                    }
                }

                @Override // com.microsoft.kapp.Callback
                public void onError(Exception ex) {
                    KLog.d(BaseTrackerFragmentV1.this.TAG, "RaisedInsightsDataFetcher failed.", ex);
                }
            }, query);
        }
    }

    protected RaisedInsight getInsight(List<RaisedInsight> raisedInsights) {
        Validate.notNull(raisedInsights, "raisedInsights");
        RaisedInsight insight = InsightsDisplayFilter.getRaisedInsight(raisedInsights, BasicRaisedInsightFilters.TONE_CAUTION, BasicRaisedInsightFilters.COMPARISION_SELF, BasicRaisedInsightFilters.COMPARISION_PEOPLE_LIKE_YOU);
        return insight;
    }

    protected void showInsight(RaisedInsight insight) {
        Validate.notNull(insight, "RaisedInsight");
        String insightMessage = insight.getIMMsg();
        String insightActionMessage = insight.getIMActionMsg();
        if (!TextUtils.isEmpty(insightMessage)) {
            this.mInsightWidget.setMessage(insightMessage);
            if (!TextUtils.isEmpty(insightActionMessage)) {
                this.mInsightWidget.setActionMessage(insightActionMessage);
            }
            this.mInsightWidget.setVisibility(0);
        }
    }

    @Override // com.microsoft.kapp.tasks.GoalsGetTask.OnGoalsRetrieveTaskListener
    public void onGoalsRetrieved(List<GoalDto> goalsList) {
        if (goalsList != null) {
            for (GoalDto goal : goalsList) {
                if (goal != null && goal.getType() == getGoalType()) {
                    onGoalRetrieved(goal);
                    return;
                }
            }
        }
    }

    @Override // com.microsoft.kapp.tasks.OnTaskStateChangedListener
    public void onTaskFailed(StateListenerTask task, Exception ex) {
        if (task == this.mGoalsGetTask) {
            KLog.e(this.TAG, getText(R.string.error_message_cannot_get_goals).toString(), ex);
        }
    }

    protected void onGoalRetrieved(GoalDto goal) {
        populateGoalHistory(goal);
        this.mWaitingGoalHistoryData = false;
        updateChartGoals();
    }

    protected void populateGoalHistory(GoalDto goalExpanded) {
        if (goalExpanded == null || goalExpanded.getValueHistory() == null) {
            KLog.e(this.TAG, "Goal History should not be null! populateGoalHistory aborted!");
            return;
        }
        GoalValueHistoryDto goalValueHistoryDto = goalExpanded.getValueHistory().get(0);
        if (goalValueHistoryDto == null) {
            KLog.d(this.TAG, "GoalValueHistoryDto should not be null.");
            return;
        }
        List<GoalValueRecordDto> historyThresholds = goalValueHistoryDto.getHistoryThresholds();
        if (historyThresholds == null) {
            KLog.d(this.TAG, "GoalValueRecordDto should not be null");
            return;
        }
        int historyLength = historyThresholds.size();
        this.mGoalHistory = new SparseIntArray(historyLength);
        for (GoalValueRecordDto goalRecord : historyThresholds) {
            if (goalRecord != null) {
                DateTime updateTime = goalRecord.getUpdateTime();
                Object goalValueObject = goalRecord.getValue();
                if (updateTime != null && goalValueObject != null) {
                    try {
                        Integer goalValue = Integer.valueOf(((Double) goalValueObject).intValue());
                        this.mGoalHistory.put(getDayOfCentury(updateTime), goalValue.intValue());
                    } catch (ClassCastException ex) {
                        KLog.d(this.TAG, "Unable to cast goalRecord.getValue() to Double", ex);
                    }
                }
            }
        }
        HomeData.getInstance().getGoalsHistory().put(getGoalType(), this.mGoalHistory);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void fetchGoals() {
        if (this.mGoalsGetTask != null) {
            this.mGoalsGetTask.execute();
        }
    }

    @Override // com.microsoft.kapp.activities.OnGoalsChangedListener
    public boolean isValid() {
        return Validate.isActivityAlive(getHostActivity());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getGoalValueForDate(DateTime dateTime) {
        if (this.mGoalHistory == null || this.mGoalHistory.size() == 0 || dateTime == null) {
            return -1;
        }
        int goalHistoryLength = this.mGoalHistory.size();
        int goalHistoryLastIndex = goalHistoryLength - 1;
        int targetDayOfCentury = getDayOfCentury(dateTime);
        int goalIndex = -1;
        if (targetDayOfCentury >= this.mGoalHistory.keyAt(0)) {
            int currentIndexInHistory = goalHistoryLastIndex;
            while (true) {
                if (currentIndexInHistory < 0) {
                    break;
                }
                int currentDateInHistory = this.mGoalHistory.keyAt(currentIndexInHistory);
                if (targetDayOfCentury < currentDateInHistory) {
                    currentIndexInHistory--;
                } else {
                    goalIndex = currentIndexInHistory;
                    break;
                }
            }
            return this.mGoalHistory.valueAt(goalIndex);
        }
        return -1;
    }

    @Override // com.microsoft.kapp.activities.OnGoalsChangedListener
    public void onGoalsUpdated(GoalType dataType, HomeData homeData) {
        setGoalUpdated(true);
        onGoalValueUpdated(dataType, homeData);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setGoalUpdated(boolean flag) {
        isGoalsUpdated = flag;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isGoalUpdated() {
        return isGoalsUpdated;
    }
}

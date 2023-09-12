package com.microsoft.kapp.fragments;

import android.os.Bundle;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.ScrollLoadStatus;
import com.microsoft.kapp.models.UserActivitySummary;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.KAppDateFormatter;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomFontTextView;
import com.microsoft.kapp.views.CustomGlyphView;
import com.microsoft.kapp.views.ScrollLoadIndicatorView;
import com.microsoft.kapp.views.TrackableScrollView;
import com.microsoft.kapp.views.UserActivitySummaryListView;
import com.microsoft.kapp.widgets.Interstitial;
import com.microsoft.krestsdk.models.GoalType;
import com.microsoft.krestsdk.models.UserActivity;
import com.microsoft.krestsdk.models.UserDailySummary;
import com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
/* loaded from: classes.dex */
public abstract class BaseTrackerWeeklyFragment extends BaseTrackerFragmentV1 {
    protected static final String SAVED_USER_DAILY_SUMMARY = "mSavedUserDailySummary";
    protected LocalDate mEndDate;
    private Bundle mFragmentInstantiationData;
    protected Interstitial mInterstitial;
    private boolean mPendingUserDailySummaryUpdate;
    protected ArrayList<UserDailySummary> mSavedUserDailySummary;
    protected LocalDate mStartDate;

    public abstract void onUserSummaryUpdated(List<UserDailySummary> list, LocalDate localDate, LocalDate localDate2);

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            savedInstanceState = getArguments();
        }
        this.mFragmentInstantiationData = savedInstanceState;
        if (this.mFragmentInstantiationData != null) {
            this.mHomeData = HomeData.getInstance();
            this.mSavedUserDailySummary = this.mFragmentInstantiationData.getParcelableArrayList(SAVED_USER_DAILY_SUMMARY);
            this.mIsL2View = this.mFragmentInstantiationData.getBoolean("stepsAndCaloriesLevelTwoView");
            if (this.mIsL2View) {
                this.mStartDate = (LocalDate) this.mFragmentInstantiationData.getSerializable("stepsAndCaloriesStartdate");
                this.mEndDate = (LocalDate) this.mFragmentInstantiationData.getSerializable("stepsAndCaloriesEnddate");
                return;
            }
            this.mEndDate = this.mHomeData.getTargetDate();
            if (this.mEndDate == null) {
                this.mEndDate = LocalDate.now();
            }
            this.mStartDate = this.mEndDate.plusDays(-1);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_tracker_weekly_fragment, container, false);
        this.mUserActivitySummaryListView = (UserActivitySummaryListView) ViewUtils.getValidView(view, R.id.recent_activities, UserActivitySummaryListView.class);
        this.mHeaderlayout = (RelativeLayout) ViewUtils.getValidView(view, R.id.activity_header_layout, RelativeLayout.class);
        this.mDateTextView = (CustomFontTextView) ViewUtils.getValidView(view, R.id.date_text_view, CustomFontTextView.class);
        this.mGlyph = (CustomGlyphView) ViewUtils.getValidView(view, R.id.activity_glyph, CustomGlyphView.class);
        this.mValueTextView = (CustomFontTextView) ViewUtils.getValidView(view, R.id.value_text_view, CustomFontTextView.class);
        this.mBackButton = (CustomGlyphView) ViewUtils.getValidView(view, R.id.back_button, CustomGlyphView.class);
        this.mBackButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.BaseTrackerWeeklyFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                BaseTrackerWeeklyFragment.this.getActivity().finish();
            }
        });
        this.mInterstitial = (Interstitial) ViewUtils.getValidView(view, R.id.base_tracker_weekly_interstitial, Interstitial.class);
        this.mScrollLoadIndicator = (ScrollLoadIndicatorView) ViewUtils.getValidView(view, R.id.scroll_load_indicator, ScrollLoadIndicatorView.class);
        return view;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1, com.microsoft.kapp.fragments.BaseHomeTileFragment, android.support.v4.app.Fragment
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        if (savedInstanceState != null) {
            this.mIsRecoveringSavedState = true;
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        setIsWaitingForUserDailySummaryUpdate(true);
    }

    protected void fetchDateRangeSummary(final LocalDate startDate, final LocalDate endDate) {
        this.mService.getDailySummaries(startDate, endDate.plusDays(1), new ActivityScopedCallback(this, new Callback<List<UserDailySummary>>() { // from class: com.microsoft.kapp.fragments.BaseTrackerWeeklyFragment.2
            @Override // com.microsoft.kapp.Callback
            public void callback(List<UserDailySummary> response) {
                BaseTrackerWeeklyFragment.this.showTrackerStats();
                if (!BaseTrackerWeeklyFragment.this.isListNullOrEmpty(response)) {
                    BaseTrackerWeeklyFragment.this.onUserSummaryUpdated(response, startDate, endDate);
                } else {
                    BaseTrackerWeeklyFragment.this.onUserSummaryUpdated(CommonUtils.getEmptyUserDailySummaries(startDate), startDate, endDate);
                }
                BaseTrackerWeeklyFragment.this.setState(1234);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.e(BaseTrackerWeeklyFragment.this.TAG, "Error while fetching data from the cloud", ex);
                BaseTrackerWeeklyFragment.this.setState(1235);
            }
        }));
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_USER_DAILY_SUMMARY, this.mSavedUserDailySummary);
        outState.putSerializable("stepsAndCaloriesStartdate", this.mStartDate);
        outState.putSerializable("stepsAndCaloriesEnddate", this.mEndDate);
        outState.putBoolean("stepsAndCaloriesLevelTwoView", this.mIsL2View);
    }

    public void updateActivityHeader(Spannable totalValue) {
        if (this.mValueTextView != null) {
            this.mValueTextView.setText(totalValue);
        }
        if (this.mDateTextView != null && this.mStartDate != null && this.mEndDate != null) {
            String startDate = KAppDateFormatter.formatToMonthDay(this.mStartDate);
            String endDate = KAppDateFormatter.formatToMonthDay(this.mEndDate);
            String date = String.format(getActivity().getResources().getString(R.string.chart_date_format_long), startDate, endDate);
            this.mDateTextView.setText(date);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment
    public void fetchData() {
        setState(1233);
        try {
            this.mGoalHistory = HomeData.getInstance().getGoalsHistory().get(getGoalType());
            if (this.mGoalHistory == null || isGoalUpdated()) {
                fetchGoals();
                setGoalUpdated(false);
            } else {
                this.mWaitingGoalHistoryData = false;
                updateChartGoals();
            }
            LocalDate startDateOfLastWeek = LocalDate.now().plusWeeks(-1);
            if (!startDateOfLastWeek.isBefore(this.mProfileCreatedDate)) {
                this.mUserActivitySummaryListView.setItems(getUserActivitySummaries(startDateOfLastWeek, this.mProfileCreatedDate));
                setTotalLoadStatus(ScrollLoadStatus.PARTIAL);
            }
            this.mScrollView.setOnHitBottomListener(new TrackableScrollView.OnHitBottomListener() { // from class: com.microsoft.kapp.fragments.BaseTrackerWeeklyFragment.3
                @Override // com.microsoft.kapp.views.TrackableScrollView.OnHitBottomListener
                public void onHitBottom() {
                    if (BaseTrackerWeeklyFragment.this.mTotalLoadStatus == ScrollLoadStatus.PARTIAL && BaseTrackerWeeklyFragment.this.mUserActivitySummaryListView != null && BaseTrackerWeeklyFragment.this.mUserActivitySummaryListView.getLastItem() != null) {
                        ArrayList<UserActivitySummary> list = BaseTrackerWeeklyFragment.this.getUserActivitySummaries(BaseTrackerWeeklyFragment.this.mUserActivitySummaryListView.getLastItem().getStartDate().plusDays(-1), BaseTrackerWeeklyFragment.this.mProfileCreatedDate);
                        if (list != null && !list.isEmpty()) {
                            BaseTrackerWeeklyFragment.this.mUserActivitySummaryListView.addItems(list);
                            if (list.size() < 10) {
                                BaseTrackerWeeklyFragment.this.setTotalLoadStatus(ScrollLoadStatus.COMPLETE);
                                return;
                            }
                            return;
                        }
                        BaseTrackerWeeklyFragment.this.setTotalLoadStatus(ScrollLoadStatus.COMPLETE);
                    }
                }
            });
            LocalDate endDate = this.mEndDate == null ? LocalDate.now() : this.mEndDate;
            LocalDate startDate = this.mStartDate == null ? endDate.plusWeeks(-1) : this.mStartDate;
            setTargetDate(endDate.toDateTime(new LocalTime()));
            hideTrackerStats();
            if (this.mHomeData != null && endDate.equals(this.mHomeData.getTargetDate())) {
                this.mIsRecoveringSavedState = false;
                List<UserDailySummary> summaries = this.mHomeData.getUserDailySummaries();
                if (summaries != null && summaries.size() > 0) {
                    showTrackerStats();
                    onUserSummaryUpdated(summaries, startDate, endDate);
                } else {
                    onUserSummaryUpdated(CommonUtils.getEmptyUserDailySummaries(startDate), startDate, endDate);
                }
                loadInsight();
            } else if (this.mIsRecoveringSavedState) {
                this.mIsRecoveringSavedState = false;
                if (this.mSavedUserDailySummary != null && this.mSavedUserDailySummary.size() > 0) {
                    showTrackerStats();
                    onUserSummaryUpdated(this.mSavedUserDailySummary, startDate, endDate);
                } else {
                    fetchDateRangeSummary(startDate, endDate);
                }
            } else {
                fetchDateRangeSummary(startDate, endDate);
            }
            setState(1234);
        } catch (Exception ex) {
            KLog.w(this.TAG, "error in basetrackerweeklyfragment", ex);
            setState(1235);
            setTotalLoadStatus(ScrollLoadStatus.PARTIAL);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public void onGoalValueUpdated(GoalType dataType, HomeData homeData) {
    }

    public boolean isWaitingForUserDailySummaryUpdate() {
        return this.mPendingUserDailySummaryUpdate;
    }

    public void setIsWaitingForUserDailySummaryUpdate(boolean flag) {
        this.mPendingUserDailySummaryUpdate = flag;
    }

    @Override // com.microsoft.kapp.fragments.BaseTrackerFragmentV1
    public BaseChartFragment getDailyChartFragment(List<UserActivity> userActivityList) {
        return null;
    }
}

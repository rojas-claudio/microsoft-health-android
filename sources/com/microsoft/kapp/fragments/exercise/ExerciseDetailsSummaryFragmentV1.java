package com.microsoft.kapp.fragments.exercise;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.BaseEventSummaryFragment;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.TimeSpan;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.DataProviderUtils;
import com.microsoft.kapp.utils.EventUtils;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.kapp.utils.KAppDateFormatter;
import com.microsoft.kapp.utils.TrackerStatUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.BaseTrackerStat;
import com.microsoft.kapp.views.CustomFontTextView;
import com.microsoft.kapp.views.TrackerStatsWidget;
import com.microsoft.krestsdk.models.ExerciseEvent;
import com.microsoft.krestsdk.models.RaisedInsight;
import com.microsoft.krestsdk.models.UserEvent;
import com.microsoft.krestsdk.services.RestService;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.kcompanionapp.charts.ChartFragment;
import com.shinobicontrols.kcompanionapp.charts.HeartRateChart;
import com.shinobicontrols.kcompanionapp.charts.IChartView;
import com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment;
import com.shinobicontrols.kcompanionapp.charts.internal.TypedValueGetter;
import com.shinobicontrols.kcompanionapp.properties.ChartConfig;
import com.shinobicontrols.kcompanionapp.properties.ChartDataProperties;
import com.shinobicontrols.kcompanionapp.properties.ChartDataPropertiesUtils;
import com.shinobicontrols.kcompanionapp.providers.XAxisProvider;
import com.shinobicontrols.kcompanionapp.providers.XAxisStrategyProvider;
import com.shinobicontrols.kcompanionapp.providers.YAxisStrategyProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/* loaded from: classes.dex */
public class ExerciseDetailsSummaryFragmentV1 extends BaseEventSummaryFragment implements ShinobiChart.OnSnapshotDoneListener {
    private static final String EXERCISE_CHART_TAG = "exercise_chart_tag";
    private ChartFragment mChartFragment;
    private ExerciseEvent mExerciseEvent;
    private CustomFontTextView mPersonalBestBanner;
    protected TrackerStatsWidget mTrackerStats;
    private View mUVDisclaimer;

    public static BaseFragment newInstance(String eventID, Boolean isL2View) {
        ExerciseDetailsSummaryFragmentV1 fragment = new ExerciseDetailsSummaryFragmentV1();
        Bundle bundle = new Bundle();
        bundle.putString("user_event_id", eventID);
        bundle.putBoolean(Constants.EVENT_L2_VIEW, isL2View.booleanValue());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    public View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.base_exercise_details_fragment_v1, container, false);
        this.mTrackerStats = (TrackerStatsWidget) ViewUtils.getValidView(childView, R.id.exercise_details_stats, TrackerStatsWidget.class);
        this.mPersonalBestBanner = (CustomFontTextView) ViewUtils.getValidView(childView, R.id.execiseBestBanner, CustomFontTextView.class);
        this.mUVDisclaimer = (View) ViewUtils.getValidView(childView, R.id.txtUVDisclaimer, View.class);
        return childView;
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment
    public void fetchData() {
        setState(1233);
        ArrayList<RestService.ExpandType> expandTypes = new ArrayList<>();
        expandTypes.add(RestService.ExpandType.INFO);
        expandTypes.add(RestService.ExpandType.SEQUENCES);
        if (this.mSettings.isRaisedInsightsEnabled()) {
            expandTypes.add(RestService.ExpandType.EVIDENCE);
        }
        this.mRestService.getExerciseEventById(this.mUserEventId, expandTypes, new ActivityScopedCallback(this, new Callback<ExerciseEvent>() { // from class: com.microsoft.kapp.fragments.exercise.ExerciseDetailsSummaryFragmentV1.1
            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(ExerciseDetailsSummaryFragmentV1.this.TAG, "getting exerciseEvent failed.", ex);
                ExerciseDetailsSummaryFragmentV1.this.setState(1235);
            }

            @Override // com.microsoft.kapp.Callback
            public void callback(ExerciseEvent result) {
                ExerciseDetailsSummaryFragmentV1.this.mExerciseEvent = result;
                if (ExerciseDetailsSummaryFragmentV1.this.mExerciseEvent != null) {
                    try {
                        ExerciseDetailsSummaryFragmentV1.this.setCurrentEventName(ExerciseDetailsSummaryFragmentV1.this.mExerciseEvent.getName());
                        CommonUtils.updatePersonalBests(HomeData.getInstance().getPersonalBests(), ExerciseDetailsSummaryFragmentV1.this.mExerciseEvent, ExerciseDetailsSummaryFragmentV1.this.getActivity());
                        ExerciseDetailsSummaryFragmentV1.this.populateTrackers(ExerciseDetailsSummaryFragmentV1.this.getHostActivity(), ExerciseDetailsSummaryFragmentV1.this.mExerciseEvent);
                        ExerciseDetailsSummaryFragmentV1.this.getUserActivities(ExerciseDetailsSummaryFragmentV1.this.mExerciseEvent);
                        ExerciseDetailsSummaryFragmentV1.this.mUVDisclaimer.setVisibility(EventUtils.getUVDisclaimerViewVisibility(ExerciseDetailsSummaryFragmentV1.this.mExerciseEvent));
                        if (ExerciseDetailsSummaryFragmentV1.this.mExerciseEvent.getPersonalBests() != null && !ExerciseDetailsSummaryFragmentV1.this.mExerciseEvent.getPersonalBests().isEmpty()) {
                            ExerciseDetailsSummaryFragmentV1.this.mPersonalBestBanner.setText(Formatter.formatPersonalBestBanner(ExerciseDetailsSummaryFragmentV1.this.mExerciseEvent.getPersonalBests(), ExerciseDetailsSummaryFragmentV1.this.getHostActivity().getApplicationContext()));
                            ExerciseDetailsSummaryFragmentV1.this.mPersonalBestBanner.setVisibility(0);
                        }
                        ExerciseDetailsSummaryFragmentV1.this.loadInsight();
                        ExerciseDetailsSummaryFragmentV1.this.setState(1234);
                        return;
                    } catch (Exception ex) {
                        KLog.e(ExerciseDetailsSummaryFragmentV1.this.TAG, "error loading exercise data", ex);
                        ExerciseDetailsSummaryFragmentV1.this.setState(1235);
                        return;
                    }
                }
                KLog.e(ExerciseDetailsSummaryFragmentV1.this.TAG, "onCreateView(): mExerciseEvent cannot be null");
                ExerciseDetailsSummaryFragmentV1.this.setState(1235);
            }
        }));
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_EXERCISE_SUMMARY);
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected boolean hasHistoryView() {
        return true;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected int getFilterType() {
        return Constants.FILTER_TYPE_EXERCISES;
    }

    protected void getUserActivities(ExerciseEvent exerciseEvent) {
        if (exerciseEvent == null) {
            KLog.e(this.TAG, "getUserActivities(): exerciseEvent cannot be null");
        } else {
            handleChartData();
        }
    }

    private void handleChartData() {
        try {
            int age = getUserAge(this.mSettings);
            int hrMax = this.mExerciseEvent.getHRZones().getMaxHr();
            if (this.mChartFragment == null) {
                List<IChartView> charts = new ArrayList<>();
                ChartConfig config = new ChartConfig(getActivity(), age, hrMax, false);
                ChartDataProperties properties = ChartDataPropertiesUtils.getExerciseEventDataProperties(this.mExerciseEvent);
                this.mExerciseEvent.setInfo(this.mExerciseEvent.getUpdatedInfoSequenceWithPauseTime());
                charts.add(new HeartRateChart(config, YAxisStrategyProvider.HEART_RATE, ChartDataPropertiesUtils.getHeartRateDataProperties(this.mExerciseEvent, new TypedValueGetter(getActivity().getResources()), age, hrMax)));
                this.mChartFragment = ChartFragment.newInstance(charts, XAxisProvider.EXERCISE, XAxisStrategyProvider.EXERCISE, properties, false);
            }
            this.mChartFragment.setDataProvider(DataProviderUtils.createExerciseEventDataProvider(this.mExerciseEvent));
            addChartFragment(R.id.chart_fragment, this.mChartFragment, EXERCISE_CHART_TAG);
        } catch (Exception ex) {
            KLog.d(this.TAG, "unable to load chart", ex);
        }
    }

    protected FragmentActivity getHostActivity() {
        return getActivity();
    }

    protected Fragment getHostFragment() {
        return getParentFragment();
    }

    protected void populateTrackers(Context context, ExerciseEvent exerciseEvent) {
        List<BaseTrackerStat> stats = new ArrayList<>();
        Resources res = getResources();
        BaseTrackerStat burnedCalories = TrackerStatUtils.getBurnedCaloriesStat(exerciseEvent, res, context);
        BaseTrackerStat avgBpm = TrackerStatUtils.getAvgBpmStat(exerciseEvent.getAverageHeartRate(), exerciseEvent.getPeakHeartRate(), exerciseEvent.getLowestHeartRate(), res, context);
        BaseTrackerStat duration = TrackerStatUtils.getDurationStat(exerciseEvent.getDuration(), exerciseEvent.getStartTime(), res, context, false, R.array.MerticSmallUnitFormat);
        BaseTrackerStat endHr = TrackerStatUtils.getEndHRStat(exerciseEvent, res, context);
        BaseTrackerStat recoveryTime = TrackerStatUtils.getRecoveryTimeStat(exerciseEvent, res, context);
        BaseTrackerStat trainingEffect = TrackerStatUtils.getTrainingEffectStat(exerciseEvent, context);
        BaseTrackerStat uVExposure = TrackerStatUtils.getUvExposure(exerciseEvent, res, context);
        stats.add(duration);
        stats.add(burnedCalories);
        stats.add(avgBpm);
        stats.add(endHr);
        stats.add(recoveryTime);
        stats.add(trainingEffect);
        stats.add(uVExposure);
        this.mTrackerStats.setStats(stats);
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected UserEvent getUserEvent() {
        return this.mExerciseEvent;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected int getEventType() {
        return 1;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected void onShareRequested() {
        Activity activity = getActivity();
        if (!Validate.isActivityAlive(activity)) {
            shareEvent(null);
        }
        FragmentManager fm = getFragmentManager();
        try {
            BaseChartFragment fragment = (BaseChartFragment) fm.findFragmentById(R.id.chart_fragment);
            if (fragment != null) {
                fragment.takeSnapShot(this);
            } else {
                shareEvent(null);
                KLog.i(this.TAG, "Chart is null");
            }
        } catch (ClassCastException e) {
            KLog.w(this.TAG, "Chart is not able to take snapshot");
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected List<RaisedInsight> getInsights() {
        return this.mExerciseEvent.getPrimaryRaisedInsights();
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected String getShareTitle() {
        if (this.mExerciseEvent == null) {
            return null;
        }
        String eventStartDate = KAppDateFormatter.formatToMonthDay(this.mExerciseEvent.getStartTime());
        String title = String.format(Locale.getDefault(), getString(R.string.share_exercise_title), eventStartDate, this.mExerciseEvent.getMainMetric(getActivity(), this.mSettings.isDistanceHeightMetric()).toString(), TimeSpan.fromSeconds(this.mExerciseEvent.getDuration()).formatToHrsMinsSecs(getActivity()));
        return title;
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport, com.microsoft.kapp.fragments.BaseFragment
    protected int getTopMenuDividerVisibility() {
        return 0;
    }
}

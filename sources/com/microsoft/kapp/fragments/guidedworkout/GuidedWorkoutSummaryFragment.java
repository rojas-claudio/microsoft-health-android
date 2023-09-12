package com.microsoft.kapp.fragments.guidedworkout;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.factories.ShareObject;
import com.microsoft.kapp.fragments.BaseEventSummaryFragment;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.TimeSpan;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.DataProviderUtils;
import com.microsoft.kapp.utils.EventUtils;
import com.microsoft.kapp.utils.FileUtils;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.kapp.utils.KAppDateFormatter;
import com.microsoft.kapp.utils.TrackerStatUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.BaseTrackerStat;
import com.microsoft.kapp.views.CustomFontTextView;
import com.microsoft.kapp.views.TrackerStatsWidget;
import com.microsoft.krestsdk.models.GuidedWorkoutEvent;
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
/* loaded from: classes.dex */
public class GuidedWorkoutSummaryFragment extends BaseEventSummaryFragment implements ShinobiChart.OnSnapshotDoneListener {
    private static final String GUIDEDWORKOUT_CHART_TAG = "guidedworkout_chart_tag";
    private ChartFragment mChartFragment;
    private GuidedWorkoutEvent mGuidedWorkoutEvent;
    private CustomFontTextView mPersonalBestBanner;
    protected int mStatStyleResourceId;
    protected TrackerStatsWidget mTrackerStats;
    private View mUVDisclaimer;

    public static BaseFragment newInstance(String eventID, Boolean isL2View) {
        GuidedWorkoutSummaryFragment fragment = new GuidedWorkoutSummaryFragment();
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

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_GUIDED_WORKOUTS_SUMMARY);
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    public View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.guided_workout_summary_fragment, container, false);
        this.mTrackerStats = (TrackerStatsWidget) ViewUtils.getValidView(childView, R.id.guidedworkout_details_stats, TrackerStatsWidget.class);
        this.mPersonalBestBanner = (CustomFontTextView) ViewUtils.getValidView(childView, R.id.guidedworkoutBestBanner, CustomFontTextView.class);
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
        this.mRestService.getGuidedWorkoutEventById(this.mUserEventId, expandTypes, new ActivityScopedCallback(this, new Callback<GuidedWorkoutEvent>() { // from class: com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutSummaryFragment.1
            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(GuidedWorkoutSummaryFragment.this.TAG, "getting guidedworkoutevent failed.", ex);
                GuidedWorkoutSummaryFragment.this.setState(1235);
            }

            @Override // com.microsoft.kapp.Callback
            public void callback(GuidedWorkoutEvent result) {
                GuidedWorkoutSummaryFragment.this.mGuidedWorkoutEvent = result;
                if (GuidedWorkoutSummaryFragment.this.mGuidedWorkoutEvent != null) {
                    try {
                        GuidedWorkoutSummaryFragment.this.setCurrentEventName(GuidedWorkoutSummaryFragment.this.mGuidedWorkoutEvent.getName());
                        CommonUtils.updatePersonalBests(HomeData.getInstance().getPersonalBests(), GuidedWorkoutSummaryFragment.this.mGuidedWorkoutEvent, GuidedWorkoutSummaryFragment.this.getActivity());
                        GuidedWorkoutSummaryFragment.this.clearAndPopulateTrackers(GuidedWorkoutSummaryFragment.this.getHostActivity(), GuidedWorkoutSummaryFragment.this.mGuidedWorkoutEvent);
                        GuidedWorkoutSummaryFragment.this.getUserActivities(GuidedWorkoutSummaryFragment.this.mGuidedWorkoutEvent);
                        GuidedWorkoutSummaryFragment.this.mUVDisclaimer.setVisibility(EventUtils.getUVDisclaimerViewVisibility(GuidedWorkoutSummaryFragment.this.mGuidedWorkoutEvent));
                        if (GuidedWorkoutSummaryFragment.this.mGuidedWorkoutEvent.getPersonalBests() != null && !GuidedWorkoutSummaryFragment.this.mGuidedWorkoutEvent.getPersonalBests().isEmpty()) {
                            GuidedWorkoutSummaryFragment.this.mPersonalBestBanner.setText(Formatter.formatPersonalBestBanner(GuidedWorkoutSummaryFragment.this.mGuidedWorkoutEvent.getPersonalBests(), GuidedWorkoutSummaryFragment.this.getHostActivity().getApplicationContext()));
                            GuidedWorkoutSummaryFragment.this.mPersonalBestBanner.setVisibility(0);
                        }
                        GuidedWorkoutSummaryFragment.this.loadInsight();
                        GuidedWorkoutSummaryFragment.this.setState(1234);
                        return;
                    } catch (Exception ex) {
                        KLog.e(GuidedWorkoutSummaryFragment.this.TAG, "exception loading guided workout data", ex);
                        GuidedWorkoutSummaryFragment.this.setState(1235);
                        return;
                    }
                }
                GuidedWorkoutSummaryFragment.this.setState(1235);
                KLog.e(GuidedWorkoutSummaryFragment.this.TAG, "onCreateView(): mGuidedWorkoutEvent cannot be null");
            }
        }));
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected boolean hasHistoryView() {
        return true;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected int getFilterType() {
        return 255;
    }

    protected void getUserActivities(GuidedWorkoutEvent guidedWorkoutEvent) {
        if (guidedWorkoutEvent == null) {
            KLog.e(this.TAG, "getUserActivities(): guidedWorkoutEvent cannot be null");
        } else {
            handleChartData();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearAndPopulateTrackers(Context context, GuidedWorkoutEvent guidedWorkoutEvent) {
        this.mTrackerStats.clearStats();
        this.mStatStyleResourceId = this.mTrackerStats.getStatStyleResourceId();
        populateTrackers(context, guidedWorkoutEvent);
    }

    private void handleChartData() {
        try {
            int age = getUserAge(this.mSettings);
            int hrMax = this.mGuidedWorkoutEvent.getHRZones().getMaxHr();
            if (this.mChartFragment == null) {
                List<IChartView> charts = new ArrayList<>();
                ChartConfig config = new ChartConfig(getActivity(), age, hrMax, false);
                ChartDataProperties properties = ChartDataPropertiesUtils.getExerciseEventDataProperties(this.mGuidedWorkoutEvent);
                this.mGuidedWorkoutEvent.setInfo(this.mGuidedWorkoutEvent.getUpdatedInfoSequenceWithPauseTime());
                charts.add(new HeartRateChart(config, YAxisStrategyProvider.HEART_RATE, ChartDataPropertiesUtils.getHeartRateDataProperties(this.mGuidedWorkoutEvent, new TypedValueGetter(getActivity().getResources()), age, hrMax)));
                this.mChartFragment = ChartFragment.newInstance(charts, XAxisProvider.EXERCISE, XAxisStrategyProvider.EXERCISE, properties, false);
            }
            this.mChartFragment.setDataProvider(DataProviderUtils.createExerciseEventDataProvider(this.mGuidedWorkoutEvent));
            addChartFragment(R.id.chart_fragment, this.mChartFragment, GUIDEDWORKOUT_CHART_TAG);
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

    protected void populateTrackers(Context context, GuidedWorkoutEvent guidedWorkoutEvent) {
        List<BaseTrackerStat> stats = new ArrayList<>();
        Resources res = getResources();
        BaseTrackerStat burnedCalories = TrackerStatUtils.getBurnedCaloriesStat(guidedWorkoutEvent, res, context);
        BaseTrackerStat avgBpm = TrackerStatUtils.getAvgBpmStat(guidedWorkoutEvent.getAverageHeartRate(), guidedWorkoutEvent.getPeakHeartRate(), guidedWorkoutEvent.getLowestHeartRate(), res, context);
        BaseTrackerStat duration = TrackerStatUtils.getDurationStat(guidedWorkoutEvent.getDuration(), guidedWorkoutEvent.getStartTime(), res, context, false, R.array.MerticSmallUnitFormat);
        BaseTrackerStat endHr = TrackerStatUtils.getEndHRStat(guidedWorkoutEvent, res, context);
        BaseTrackerStat recoveryTime = TrackerStatUtils.getRecoveryTimeStat(guidedWorkoutEvent, res, context);
        BaseTrackerStat trainingEffect = TrackerStatUtils.getTrainingEffectStat(guidedWorkoutEvent, context);
        BaseTrackerStat uVExposure = TrackerStatUtils.getUvExposure(guidedWorkoutEvent, res, context);
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
        return this.mGuidedWorkoutEvent;
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
            fragment.takeSnapShot(this);
        } catch (ClassCastException e) {
            KLog.w(this.TAG, "Chart is not able to take snapshot");
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment, com.shinobicontrols.charts.ShinobiChart.OnSnapshotDoneListener
    public void onSnapshotDone(Bitmap bitmap) {
        Activity activity = getActivity();
        if (!Validate.isActivityAlive(activity)) {
            shareEvent(null);
        }
        ShareObject.Builder builder = new ShareObject.Builder();
        builder.setChooserTitle(getString(R.string.chooser_share_exercise));
        String eventStartTime = KAppDateFormatter.formatToMonthDay(this.mGuidedWorkoutEvent.getStartTime());
        String title = String.format(Locale.getDefault(), getString(R.string.share_workout_title), eventStartTime, this.mGuidedWorkoutEvent.getMainMetric(getActivity(), this.mSettings.isDistanceHeightMetric()).toString(), TimeSpan.fromSeconds(this.mGuidedWorkoutEvent.getDuration()).formatToHrsMinsSecs(getActivity()));
        builder.setTitle(title);
        if (bitmap != null) {
            Bitmap bitmap2 = CommonUtils.drawTextOnBitmap(bitmap, title);
            DateTimeFormatter fmt = DateTimeFormat.forPattern(getString(R.string.time_format_temp_files));
            try {
                File tmpChart = FileUtils.saveBitmapToDisk(bitmap2, "exercise_hr_chart_" + fmt.print(this.mGuidedWorkoutEvent.getStartTime()) + ".jpg");
                builder.addImage(Uri.fromFile(tmpChart));
            } catch (IOException e) {
                Log.e(this.TAG, "The Bitmap failed to be saved to disk");
            }
        }
        shareEvent(builder.build(getActivity()));
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected int getEventType() {
        return 3;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected List<RaisedInsight> getInsights() {
        return this.mGuidedWorkoutEvent.getPrimaryRaisedInsights();
    }
}

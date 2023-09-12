package com.microsoft.kapp.fragments.bike;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.BingMapActivity;
import com.microsoft.kapp.activities.NetworkConnectivityChangedReceiver;
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
import com.microsoft.kapp.utils.ProfileUtils;
import com.microsoft.kapp.utils.TrackerStatUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.BaseTrackerStat;
import com.microsoft.kapp.views.BingMapView;
import com.microsoft.kapp.views.CustomFontTextView;
import com.microsoft.kapp.views.TrackerStatsWidget;
import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.models.EventType;
import com.microsoft.krestsdk.models.MeasuredEventMapPoint;
import com.microsoft.krestsdk.models.RaisedInsight;
import com.microsoft.krestsdk.models.UserEvent;
import com.microsoft.krestsdk.services.RestService;
import com.shinobicontrols.kcompanionapp.charts.ChartFragment;
import com.shinobicontrols.kcompanionapp.charts.DataProvider;
import com.shinobicontrols.kcompanionapp.charts.HeartRateChart;
import com.shinobicontrols.kcompanionapp.charts.IChartView;
import com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment;
import com.shinobicontrols.kcompanionapp.charts.internal.TypedValueGetter;
import com.shinobicontrols.kcompanionapp.properties.ChartConfig;
import com.shinobicontrols.kcompanionapp.properties.ChartDataPropertiesUtils;
import com.shinobicontrols.kcompanionapp.providers.XAxisProvider;
import com.shinobicontrols.kcompanionapp.providers.XAxisStrategyProvider;
import com.shinobicontrols.kcompanionapp.providers.YAxisStrategyProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
/* loaded from: classes.dex */
public class BikeDetailsSummaryFragment extends BaseEventSummaryFragment implements NetworkConnectivityChangedReceiver.OnNetworkConnectivityChanged, BingMapView.BingMapListener {
    private FrameLayout mBikeContainer;
    private BikeEvent mBikeEvent;
    private final View.OnClickListener mBingMapClickListener = new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.bike.BikeDetailsSummaryFragment.1
        @Override // android.view.View.OnClickListener
        public void onClick(View bingMapView) {
            BikeDetailsSummaryFragment.this.openFullScreenMap();
        }
    };
    private View mBingMapLegend;
    private BingMapView mBingMapView;
    private Context mContext;
    private boolean mHasMapData;
    private List<MeasuredEventMapPoint> mMapPointsWithLocation;
    private CustomFontTextView mPersonalBestBanner;
    private TrackerStatsWidget mStatsWidget;
    private View mUVDisclaimer;

    public static BaseFragment newInstance(String eventID, Boolean isL2View) {
        BikeDetailsSummaryFragment fragment = new BikeDetailsSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user_event_id", eventID);
        bundle.putBoolean(Constants.EVENT_L2_VIEW, isL2View.booleanValue());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getNestedFragmentHostActivity().getApplicationContext();
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    public View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.bike_details_summary_fragment, container, false);
        this.mBikeContainer = (FrameLayout) ViewUtils.getValidView(childView, R.id.bike_overview_container, FrameLayout.class);
        this.mStatsWidget = (TrackerStatsWidget) ViewUtils.getValidView(childView, R.id.run_details_tracker_widget, TrackerStatsWidget.class);
        this.mPersonalBestBanner = (CustomFontTextView) ViewUtils.getValidView(childView, R.id.runBestBanner, CustomFontTextView.class);
        this.mUVDisclaimer = (View) ViewUtils.getValidView(childView, R.id.txtUVDisclaimer, View.class);
        return childView;
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment
    public void fetchData() {
        setState(1233);
        ArrayList<RestService.ExpandType> expandTypes = new ArrayList<>();
        expandTypes.add(RestService.ExpandType.INFO);
        expandTypes.add(RestService.ExpandType.SEQUENCES);
        expandTypes.add(RestService.ExpandType.MAPPOINTS);
        if (this.mSettings.isRaisedInsightsEnabled()) {
            expandTypes.add(RestService.ExpandType.EVIDENCE);
        }
        this.mRestService.getBikeEventById(this.mSettings.isDistanceHeightMetric(), this.mUserEventId, expandTypes, new ActivityScopedCallback(this, new Callback<BikeEvent>() { // from class: com.microsoft.kapp.fragments.bike.BikeDetailsSummaryFragment.2
            @Override // com.microsoft.kapp.Callback
            public void callback(BikeEvent result) {
                boolean z = false;
                BikeDetailsSummaryFragment.this.mBikeEvent = result;
                if (BikeDetailsSummaryFragment.this.mBikeEvent != null) {
                    try {
                        BikeDetailsSummaryFragment.this.setCurrentEventName(BikeDetailsSummaryFragment.this.mBikeEvent.getName());
                        CommonUtils.updatePersonalBests(HomeData.getInstance().getPersonalBests(), BikeDetailsSummaryFragment.this.mBikeEvent, BikeDetailsSummaryFragment.this.getActivity());
                        BikeDetailsSummaryFragment.this.mMapPointsWithLocation = EventUtils.getMapPointsWithLocation(BikeDetailsSummaryFragment.this.mBikeEvent);
                        BikeDetailsSummaryFragment bikeDetailsSummaryFragment = BikeDetailsSummaryFragment.this;
                        if (BikeDetailsSummaryFragment.this.mBikeEvent.hasGpsData() && !BikeDetailsSummaryFragment.this.mMapPointsWithLocation.isEmpty()) {
                            z = true;
                        }
                        bikeDetailsSummaryFragment.mHasMapData = z;
                        BikeDetailsSummaryFragment.this.loadMapChartData();
                        BikeDetailsSummaryFragment.this.mStatsWidget.setStats(BikeDetailsSummaryFragment.this.getStats(BikeDetailsSummaryFragment.this.mBikeEvent));
                        BikeDetailsSummaryFragment.this.mUVDisclaimer.setVisibility(EventUtils.getUVDisclaimerViewVisibility(BikeDetailsSummaryFragment.this.mBikeEvent));
                        if (BikeDetailsSummaryFragment.this.mBikeEvent.getPersonalBests() != null && !BikeDetailsSummaryFragment.this.mBikeEvent.getPersonalBests().isEmpty()) {
                            BikeDetailsSummaryFragment.this.mPersonalBestBanner.setText(Formatter.formatPersonalBestBanner(BikeDetailsSummaryFragment.this.mBikeEvent.getPersonalBests(), BikeDetailsSummaryFragment.this.mContext));
                            BikeDetailsSummaryFragment.this.mPersonalBestBanner.setVisibility(0);
                        }
                        BikeDetailsSummaryFragment.this.loadInsight();
                        BikeDetailsSummaryFragment.this.setState(1234);
                        return;
                    } catch (Exception ex) {
                        KLog.e(BikeDetailsSummaryFragment.this.TAG, "exception loading bike data", ex);
                        BikeDetailsSummaryFragment.this.setState(1235);
                        return;
                    }
                }
                BikeDetailsSummaryFragment.this.setState(1235);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(BikeDetailsSummaryFragment.this.TAG, "getting bikeEvent failed.", ex);
                BikeDetailsSummaryFragment.this.setState(1235);
            }
        }));
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected boolean hasHistoryView() {
        return true;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected int getFilterType() {
        return 256;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<BaseTrackerStat> getStats(BikeEvent event) {
        List<BaseTrackerStat> stats = new ArrayList<>();
        Resources res = getResources();
        boolean isMetric = this.mSettings.isDistanceHeightMetric();
        BaseTrackerStat duration = TrackerStatUtils.getDurationStat(event.getDuration(), event.getStartTime(), res, this.mContext, false, R.array.MerticSmallUnitFormat);
        BaseTrackerStat burnedCalories = TrackerStatUtils.getBurnedCaloriesStat(event, res, this.mContext);
        BaseTrackerStat recoveryTime = TrackerStatUtils.getRecoveryTimeStat(event, res, this.mContext);
        BaseTrackerStat avgBpm = TrackerStatUtils.getAvgBpmStat(event.getAverageHeartRate(), event.getPeakHeartRate(), event.getLowestHeartRate(), res, this.mContext);
        BaseTrackerStat endHr = TrackerStatUtils.getEndHRStat(event, res, this.mContext);
        BaseTrackerStat trainingEffect = TrackerStatUtils.getTrainingEffectStat(event, this.mContext);
        BaseTrackerStat uVExposure = TrackerStatUtils.getUvExposure(event, res, this.mContext);
        stats.add(duration);
        stats.add(burnedCalories);
        if (event.hasGpsData()) {
            double bestSplitValue = EventUtils.calculateBestSplitBike(event, this.mSettings.isDistanceHeightMetric());
            BaseTrackerStat bestSplit = TrackerStatUtils.getBestSplitStat(bestSplitValue, res, this.mContext);
            BaseTrackerStat pace = TrackerStatUtils.getPaceStat(event, res, this.mContext, isMetric);
            BaseTrackerStat averageSpeed = TrackerStatUtils.getAverageSpeedStat(event, res, this.mContext, isMetric);
            BaseTrackerStat topSpeed = TrackerStatUtils.getTopSpeedStat(event, res, this.mContext, isMetric);
            BaseTrackerStat elevationGain = TrackerStatUtils.getElevationGainStat(event, res, this.mContext, isMetric);
            BaseTrackerStat elevationLoss = TrackerStatUtils.getElevationLossStat(event, res, this.mContext, isMetric);
            stats.add(averageSpeed);
            stats.add(topSpeed);
            stats.add(elevationGain);
            stats.add(elevationLoss);
            stats.add(bestSplit);
            stats.add(pace);
        }
        stats.add(avgBpm);
        stats.add(endHr);
        stats.add(recoveryTime);
        stats.add(trainingEffect);
        stats.add(uVExposure);
        return stats;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_BIKE_SUMMARY);
    }

    public void loadMapChartData() {
        View view = getView();
        if (view != null) {
            this.mBikeContainer.removeAllViews();
            if (this.mHasMapData) {
                LayoutInflater.from(this.mContext).inflate(R.layout.bike_details_map_layout, this.mBikeContainer);
                this.mBingMapView = (BingMapView) ViewUtils.getValidView(view, R.id.run_map_view, BingMapView.class);
                this.mBingMapLegend = (View) ViewUtils.getValidView(view, R.id.bing_map_legends, View.class);
                this.mBingMapView.setOnClickListener(this.mBingMapClickListener);
                this.mBingMapView.loadData(this, this.mBikeEvent, true, this.mBikeEvent.getSplitGroupSize(), false, false);
                return;
            }
            int age = ProfileUtils.getUserAge(this.mSettings);
            int hrMax = this.mBikeEvent.getHRZones().getMaxHr();
            List<IChartView> charts = new ArrayList<>();
            ChartConfig config = new ChartConfig(getActivity(), age, hrMax, false);
            this.mBikeEvent.setInfo(this.mBikeEvent.getUpdatedInfoSequenceWithPauseTime());
            charts.add(new HeartRateChart(config, YAxisStrategyProvider.HEART_RATE, ChartDataPropertiesUtils.getHeartRateDataProperties(this.mBikeEvent, new TypedValueGetter(getActivity().getResources()), age, hrMax), getString(R.string.shinobicharts_enable_gps)));
            ChartFragment chartFragment = ChartFragment.newInstance(charts, XAxisProvider.EXERCISE, XAxisStrategyProvider.EXERCISE, ChartDataPropertiesUtils.getExerciseEventDataProperties(this.mBikeEvent), false);
            DataProvider provider = DataProviderUtils.createExerciseEventDataProvider(this.mBikeEvent);
            chartFragment.setDataProvider(provider);
            FragmentTransaction fragTransaction = getFragmentManager().beginTransaction();
            fragTransaction.replace(R.id.bike_overview_container, chartFragment);
            fragTransaction.commit();
            chartFragment.resumeChartView();
        }
    }

    public FragmentActivity getNestedFragmentHostActivity() {
        return getParentFragment().getActivity();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openFullScreenMap() {
        Intent intent = new Intent(getActivity(), BingMapActivity.class);
        intent.putExtra(BingMapActivity.ARG_IN_EVENT_TYPE, EventType.Biking.ordinal());
        intent.putExtra(BingMapActivity.ARG_IN_EVENT, this.mUserEventId);
        startActivity(intent);
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected UserEvent getUserEvent() {
        return this.mBikeEvent;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected void onShareRequested() {
        Activity activity = getActivity();
        if (!Validate.isActivityAlive(activity)) {
            shareEvent(null);
        }
        if (this.mHasMapData) {
            shareEventWithMap(getString(R.string.chooser_share_bike), this.mBikeEvent, getString(R.string.share_bike_title), "bike", this.mBingMapView);
            return;
        }
        FragmentManager fm = getFragmentManager();
        try {
            BaseChartFragment fragment = (BaseChartFragment) fm.findFragmentById(R.id.bike_overview_container);
            if (fragment != null) {
                fragment.takeSnapShot(this);
            } else {
                shareEvent(null);
                KLog.e(this.TAG, "Chart is null");
            }
        } catch (ClassCastException e) {
            KLog.w(this.TAG, "Chart is not able to take snapshot");
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected int getEventType() {
        return 4;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment, com.microsoft.kapp.activities.NetworkConnectivityChangedReceiver.OnNetworkConnectivityChanged
    public void onNetworkDisconnected() {
        super.onNetworkDisconnected();
        if (this.mBingMapView != null) {
            this.mBingMapView.setOnClickListener(null);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment, com.microsoft.kapp.activities.NetworkConnectivityChangedReceiver.OnNetworkConnectivityChanged
    public void onNetworkConnected() {
        super.onNetworkConnected();
        if (this.mBingMapView != null) {
            this.mBingMapView.setOnClickListener(this.mBingMapClickListener);
        }
    }

    @Override // com.microsoft.kapp.views.BingMapView.BingMapListener
    public void onMapLoaded() {
        runOnUiThread(new Runnable() { // from class: com.microsoft.kapp.fragments.bike.BikeDetailsSummaryFragment.3
            @Override // java.lang.Runnable
            public void run() {
                BikeDetailsSummaryFragment.this.mBikeContainer.setVisibility(0);
                BikeDetailsSummaryFragment.this.mBingMapView.setVisibility(0);
                BikeDetailsSummaryFragment.this.mBingMapLegend.setVisibility(0);
            }
        });
    }

    @Override // com.microsoft.kapp.views.BingMapView.BingMapListener
    public void onMapEmpty() {
        runOnUiThread(new Runnable() { // from class: com.microsoft.kapp.fragments.bike.BikeDetailsSummaryFragment.4
            @Override // java.lang.Runnable
            public void run() {
                BikeDetailsSummaryFragment.this.mBikeContainer.setVisibility(0);
                BikeDetailsSummaryFragment.this.mBingMapView.setVisibility(8);
                BikeDetailsSummaryFragment.this.mBingMapLegend.setVisibility(8);
            }
        });
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected List<RaisedInsight> getInsights() {
        return this.mBikeEvent.getPrimaryRaisedInsights();
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected String getShareTitle() {
        if (this.mBikeEvent == null) {
            return null;
        }
        String eventStartDate = KAppDateFormatter.formatToMonthDay(this.mBikeEvent.getStartTime());
        String title = String.format(Locale.getDefault(), getString(R.string.share_bike_title), eventStartDate, TimeSpan.fromSeconds(this.mBikeEvent.getDuration()).formatToHrsMinsSecs(getActivity()));
        return title;
    }
}

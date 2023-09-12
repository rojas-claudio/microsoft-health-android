package com.microsoft.kapp.fragments.run;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.microsoft.kapp.models.Length;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.EventUtils;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.kapp.utils.MathUtils;
import com.microsoft.kapp.utils.TrackerStatUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.BaseTrackerStat;
import com.microsoft.kapp.views.BingMapView;
import com.microsoft.kapp.views.CustomFontTextView;
import com.microsoft.kapp.views.TrackerStatsWidget;
import com.microsoft.krestsdk.models.EventType;
import com.microsoft.krestsdk.models.MeasuredEventMapPoint;
import com.microsoft.krestsdk.models.RaisedInsight;
import com.microsoft.krestsdk.models.RunEvent;
import com.microsoft.krestsdk.models.UserEvent;
import com.microsoft.krestsdk.services.RestService;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class RunDetailsSummaryFragmentV1 extends BaseEventSummaryFragment implements NetworkConnectivityChangedReceiver.OnNetworkConnectivityChanged, BingMapView.BingMapListener {
    private final View.OnClickListener mBingMapClickListener = new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.run.RunDetailsSummaryFragmentV1.1
        @Override // android.view.View.OnClickListener
        public void onClick(View bingMapView) {
            RunDetailsSummaryFragmentV1.this.openFullScreenMap();
        }
    };
    private View mBingMapLegend;
    private BingMapView mBingMapView;
    private Context mContext;
    private boolean mHasMapData;
    private List<MeasuredEventMapPoint> mMapPointsWithLocation;
    private ImageView mNoMapImageView;
    private ViewGroup mNoMapText;
    private CustomFontTextView mPersonalBestBanner;
    private View mRunContainer;
    private RunEvent mRunEvent;
    private TrackerStatsWidget mStatsWidget;
    private View mUVDisclaimer;

    public static BaseFragment newInstance(String eventID, Boolean isL2View) {
        RunDetailsSummaryFragmentV1 fragment = new RunDetailsSummaryFragmentV1();
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
        View childView = inflater.inflate(R.layout.run_details_summary_fragment_v1, container, false);
        this.mRunContainer = (View) ViewUtils.getValidView(childView, R.id.run_map_container, View.class);
        this.mBingMapView = (BingMapView) ViewUtils.getValidView(childView, R.id.run_map_view, BingMapView.class);
        this.mBingMapLegend = (View) ViewUtils.getValidView(childView, R.id.bing_map_legends, View.class);
        this.mStatsWidget = (TrackerStatsWidget) ViewUtils.getValidView(childView, R.id.run_details_tracker_widget, TrackerStatsWidget.class);
        this.mNoMapImageView = (ImageView) ViewUtils.getValidView(childView, R.id.run_no_map, ImageView.class);
        this.mNoMapText = (ViewGroup) ViewUtils.getValidView(childView, R.id.run_no_map_string, ViewGroup.class);
        this.mPersonalBestBanner = (CustomFontTextView) ViewUtils.getValidView(childView, R.id.runBestBanner, CustomFontTextView.class);
        this.mUVDisclaimer = (View) ViewUtils.getValidView(childView, R.id.txtUVDisclaimer, View.class);
        this.mBingMapView.setOnClickListener(this.mBingMapClickListener);
        return childView;
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        this.mRestService.getRunEventById(this.mSettings.isDistanceHeightMetric(), this.mUserEventId, expandTypes, new ActivityScopedCallback(this, new Callback<RunEvent>() { // from class: com.microsoft.kapp.fragments.run.RunDetailsSummaryFragmentV1.2
            @Override // com.microsoft.kapp.Callback
            public void callback(RunEvent result) {
                RunDetailsSummaryFragmentV1.this.mRunEvent = result;
                if (RunDetailsSummaryFragmentV1.this.mRunEvent != null) {
                    try {
                        RunDetailsSummaryFragmentV1.this.setCurrentEventName(RunDetailsSummaryFragmentV1.this.mRunEvent.getName());
                        CommonUtils.updatePersonalBests(HomeData.getInstance().getPersonalBests(), RunDetailsSummaryFragmentV1.this.mRunEvent, RunDetailsSummaryFragmentV1.this.getActivity());
                        RunDetailsSummaryFragmentV1.this.mMapPointsWithLocation = EventUtils.getMapPointsWithLocation(RunDetailsSummaryFragmentV1.this.mRunEvent);
                        RunDetailsSummaryFragmentV1.this.mHasMapData = RunDetailsSummaryFragmentV1.this.mMapPointsWithLocation.isEmpty() ? false : true;
                        RunDetailsSummaryFragmentV1.this.loadMapChartData();
                        RunDetailsSummaryFragmentV1.this.mStatsWidget.setStats(RunDetailsSummaryFragmentV1.this.getStats(RunDetailsSummaryFragmentV1.this.mRunEvent));
                        RunDetailsSummaryFragmentV1.this.mUVDisclaimer.setVisibility(EventUtils.getUVDisclaimerViewVisibility(RunDetailsSummaryFragmentV1.this.mRunEvent));
                        if (RunDetailsSummaryFragmentV1.this.mRunEvent.getPersonalBests() != null && !RunDetailsSummaryFragmentV1.this.mRunEvent.getPersonalBests().isEmpty()) {
                            RunDetailsSummaryFragmentV1.this.mPersonalBestBanner.setText(Formatter.formatPersonalBestBanner(RunDetailsSummaryFragmentV1.this.mRunEvent.getPersonalBests(), RunDetailsSummaryFragmentV1.this.mContext));
                            RunDetailsSummaryFragmentV1.this.mPersonalBestBanner.setVisibility(0);
                        }
                        RunDetailsSummaryFragmentV1.this.loadInsight();
                        RunDetailsSummaryFragmentV1.this.setState(1234);
                        return;
                    } catch (Exception ex) {
                        KLog.e(RunDetailsSummaryFragmentV1.this.TAG, "exception loading run data", ex);
                        RunDetailsSummaryFragmentV1.this.setState(1235);
                        return;
                    }
                }
                RunDetailsSummaryFragmentV1.this.setState(1235);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(RunDetailsSummaryFragmentV1.this.TAG, "getting runEvent failed.", ex);
                RunDetailsSummaryFragmentV1.this.setState(1235);
            }
        }));
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected boolean hasHistoryView() {
        return true;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected int getFilterType() {
        return Constants.FILTER_TYPE_RUNS;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<BaseTrackerStat> getStats(RunEvent event) {
        List<BaseTrackerStat> stats = new ArrayList<>();
        Resources res = getResources();
        boolean isMetric = this.mSettings.isDistanceHeightMetric();
        BaseTrackerStat bestSplit = TrackerStatUtils.gestBestSplitStat(event, res, this.mContext, this.mSettings);
        BaseTrackerStat pace = TrackerStatUtils.getPaceStat(event, res, this.mContext, isMetric);
        BaseTrackerStat burnedCalories = TrackerStatUtils.getBurnedCaloriesStat(event, res, this.mContext);
        BaseTrackerStat avgBpm = TrackerStatUtils.getAvgBpmStat(event.getAverageHeartRate(), event.getPeakHeartRate(), event.getLowestHeartRate(), res, this.mContext);
        BaseTrackerStat duration = TrackerStatUtils.getDurationStat(event.getDuration(), event.getStartTime(), res, this.mContext, false, R.array.MerticSmallUnitFormat);
        BaseTrackerStat endHr = TrackerStatUtils.getEndHRStat(event, res, this.mContext);
        BaseTrackerStat recoveryTime = TrackerStatUtils.getRecoveryTimeStat(event, res, this.mContext);
        BaseTrackerStat trainingEffect = TrackerStatUtils.getTrainingEffectStat(event, this.mContext);
        BaseTrackerStat elevationGain = TrackerStatUtils.getElevationGainStat(event, res, this.mContext, isMetric);
        BaseTrackerStat elevationLoss = TrackerStatUtils.getElevationLossStat(event, res, this.mContext, isMetric);
        BaseTrackerStat uVExposure = TrackerStatUtils.getUvExposure(event, res, this.mContext);
        stats.add(duration);
        stats.add(burnedCalories);
        stats.add(elevationGain);
        stats.add(elevationLoss);
        stats.add(bestSplit);
        stats.add(pace);
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
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_RUN_SUMMARY);
    }

    public void loadMapChartData() {
        View view = getView();
        if (view != null) {
            if (this.mHasMapData) {
                this.mBingMapView.loadData(this, this.mRunEvent, true, 1, false, true);
            } else {
                onMapEmpty();
            }
        }
    }

    public FragmentActivity getNestedFragmentHostActivity() {
        return getParentFragment().getActivity();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openFullScreenMap() {
        Intent intent = new Intent(getActivity(), BingMapActivity.class);
        intent.putExtra(BingMapActivity.ARG_IN_EVENT_TYPE, EventType.Running.ordinal());
        intent.putExtra(BingMapActivity.ARG_IN_EVENT, this.mUserEventId);
        startActivity(intent);
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected UserEvent getUserEvent() {
        return this.mRunEvent;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected void onShareRequested() {
        Activity activity = getActivity();
        if (!Validate.isActivityAlive(activity)) {
            shareEvent(null);
        }
        if (this.mHasMapData) {
            shareEventWithMap(getString(R.string.chooser_share_run), this.mRunEvent, getString(R.string.share_run_title), "run", this.mBingMapView);
            return;
        }
        Length distance = Length.fromCentimeters(this.mRunEvent.getTotalDistance());
        boolean isMetric = this.mSettings.isDistanceHeightMetric();
        double rundistance = isMetric ? distance.getTotalKilometers() : distance.getTotalMiles();
        shareEventWithoutMap(this.mRunEvent, getString(R.string.chooser_share_run), getString(R.string.share_run_title), String.valueOf(MathUtils.truncate(rundistance, 2)), isMetric ? Constants.fb_kilometers : Constants.fb_miles, "runs", "ran");
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected int getEventType() {
        return 0;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment, com.microsoft.kapp.activities.NetworkConnectivityChangedReceiver.OnNetworkConnectivityChanged
    public void onNetworkDisconnected() {
        super.onNetworkDisconnected();
        this.mBingMapView.setOnClickListener(null);
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment, com.microsoft.kapp.activities.NetworkConnectivityChangedReceiver.OnNetworkConnectivityChanged
    public void onNetworkConnected() {
        super.onNetworkConnected();
        this.mBingMapView.setOnClickListener(this.mBingMapClickListener);
    }

    @Override // com.microsoft.kapp.views.BingMapView.BingMapListener
    public void onMapLoaded() {
        runOnUiThread(new Runnable() { // from class: com.microsoft.kapp.fragments.run.RunDetailsSummaryFragmentV1.3
            @Override // java.lang.Runnable
            public void run() {
                RunDetailsSummaryFragmentV1.this.mRunContainer.setVisibility(0);
                RunDetailsSummaryFragmentV1.this.mBingMapView.setVisibility(0);
                RunDetailsSummaryFragmentV1.this.mBingMapLegend.setVisibility(0);
                RunDetailsSummaryFragmentV1.this.mNoMapImageView.setVisibility(8);
                RunDetailsSummaryFragmentV1.this.mNoMapText.setVisibility(8);
            }
        });
    }

    @Override // com.microsoft.kapp.views.BingMapView.BingMapListener
    public void onMapEmpty() {
        runOnUiThread(new Runnable() { // from class: com.microsoft.kapp.fragments.run.RunDetailsSummaryFragmentV1.4
            @Override // java.lang.Runnable
            public void run() {
                RunDetailsSummaryFragmentV1.this.mRunContainer.setVisibility(0);
                RunDetailsSummaryFragmentV1.this.mBingMapView.setVisibility(8);
                RunDetailsSummaryFragmentV1.this.mBingMapLegend.setVisibility(8);
                RunDetailsSummaryFragmentV1.this.mNoMapImageView.setVisibility(0);
                RunDetailsSummaryFragmentV1.this.mNoMapText.setVisibility(0);
            }
        });
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected List<RaisedInsight> getInsights() {
        return this.mRunEvent.getPrimaryRaisedInsights();
    }
}

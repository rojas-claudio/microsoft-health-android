package com.microsoft.kapp.fragments.run;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.adapters.RunSplitsAdapter;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.fragments.BaseHomeTileFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.RunSplitSummary;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.ConversionUtils;
import com.microsoft.kapp.utils.DataProviderUtils;
import com.microsoft.kapp.utils.EventUtils;
import com.microsoft.kapp.utils.ProfileUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.krestsdk.models.RunEvent;
import com.microsoft.krestsdk.models.RunEventSequence;
import com.microsoft.krestsdk.services.RestService;
import com.shinobicontrols.kcompanionapp.charts.ChartFragment;
import com.shinobicontrols.kcompanionapp.charts.DataProvider;
import com.shinobicontrols.kcompanionapp.charts.ElevationChart;
import com.shinobicontrols.kcompanionapp.charts.HeartRateChart;
import com.shinobicontrols.kcompanionapp.charts.IChartView;
import com.shinobicontrols.kcompanionapp.charts.PaceChart;
import com.shinobicontrols.kcompanionapp.charts.internal.TypedValueGetter;
import com.shinobicontrols.kcompanionapp.properties.ChartConfig;
import com.shinobicontrols.kcompanionapp.properties.ChartDataProperties;
import com.shinobicontrols.kcompanionapp.properties.ChartDataPropertiesUtils;
import com.shinobicontrols.kcompanionapp.providers.XAxisProvider;
import com.shinobicontrols.kcompanionapp.providers.XAxisStrategyProvider;
import com.shinobicontrols.kcompanionapp.providers.YAxisStrategyProvider;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class RunDetailsSplitFragmentV1 extends BaseHomeTileFragment {
    private static final String RUN_CHART_TAG = "run_chart_tag";
    private static final String RUN_EVENT_ID = "run_event_id";
    private static final int SPLIT_INDEX_INVALID = -1;
    private ChartFragment mChartFragment;
    @Inject
    RestService mRestService;
    private RunEvent mRunEvent;
    private String mRunEventId;
    @Inject
    SettingsProvider mSettings;
    private ListView mSplitsList;

    public static BaseFragment newInstance(String eventID, Boolean isL2View) {
        RunDetailsSplitFragmentV1 fragment = new RunDetailsSplitFragmentV1();
        Bundle bundle = new Bundle();
        bundle.putString(RUN_EVENT_ID, eventID);
        bundle.putBoolean(Constants.EVENT_L2_VIEW, isL2View.booleanValue());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle savedBundle = savedInstanceState == null ? getArguments() : savedInstanceState;
        this.mRunEventId = savedBundle.getString(RUN_EVENT_ID);
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_RUN_SPLITS);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.run_details_splits_fragment_v1, container, false);
        this.mSplitsList = (ListView) ViewUtils.getValidView(view, R.id.run_splits_list, ListView.class);
        View header = inflater.inflate(R.layout.run_splits_list_header, (ViewGroup) this.mSplitsList, false);
        this.mSplitsList.addHeaderView(header);
        return view;
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
        this.mRestService.getRunEventById(this.mSettings.isDistanceHeightMetric(), this.mRunEventId, expandTypes, new ActivityScopedCallback(this, new Callback<RunEvent>() { // from class: com.microsoft.kapp.fragments.run.RunDetailsSplitFragmentV1.1
            @Override // com.microsoft.kapp.Callback
            public void callback(RunEvent result) {
                RunDetailsSplitFragmentV1.this.mRunEvent = result;
                if (RunDetailsSplitFragmentV1.this.mRunEvent != null) {
                    try {
                        RunDetailsSplitFragmentV1.this.loadMapChartData();
                        RunDetailsSplitFragmentV1.this.ParseRunSplits(RunDetailsSplitFragmentV1.this.mRunEvent.getSequences());
                        RunDetailsSplitFragmentV1.this.setState(1234);
                        return;
                    } catch (Exception ex) {
                        KLog.e(RunDetailsSplitFragmentV1.this.TAG, "exception loading run data", ex);
                        RunDetailsSplitFragmentV1.this.setState(1235);
                        return;
                    }
                }
                RunDetailsSplitFragmentV1.this.setState(1235);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(RunDetailsSplitFragmentV1.this.TAG, "getting runEvent failed.", ex);
                RunDetailsSplitFragmentV1.this.setState(1235);
            }
        }));
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(RUN_EVENT_ID, this.mRunEventId);
    }

    public void loadMapChartData() {
        View view = getView();
        if (view != null) {
            try {
                int age = ProfileUtils.getUserAge(this.mSettings);
                int hrMax = this.mRunEvent.getHRZones().getMaxHr();
                boolean isDistanceHeightMetric = this.mSettings.isDistanceHeightMetric();
                if (this.mChartFragment == null) {
                    List<IChartView> charts = new ArrayList<>();
                    ChartConfig config = new ChartConfig(getActivity(), age, hrMax, isDistanceHeightMetric);
                    ChartDataProperties properties = ChartDataPropertiesUtils.getRunEventDataProperties(this.mRunEvent, isDistanceHeightMetric);
                    TypedValueGetter valueGetter = new TypedValueGetter(getActivity().getResources());
                    this.mRunEvent.setInfo(this.mRunEvent.getUpdatedInfoSequenceWithPauseTime());
                    charts.add(new PaceChart(config, YAxisStrategyProvider.PACE, ChartDataPropertiesUtils.getPaceDataProperties(this.mRunEvent, isDistanceHeightMetric, getActivity())));
                    if (EventUtils.hasGPSTurnedOn(this.mRunEvent)) {
                        charts.add(new HeartRateChart(config, YAxisStrategyProvider.HEART_RATE, ChartDataPropertiesUtils.getHeartRateDataPropertiesForDistance(this.mRunEvent, valueGetter, isDistanceHeightMetric)));
                        charts.add(new ElevationChart(config, YAxisStrategyProvider.ELEVATION, ChartDataPropertiesUtils.getElevationDataProperties(this.mRunEvent, isDistanceHeightMetric)));
                    } else {
                        charts.add(new HeartRateChart(config, YAxisStrategyProvider.HEART_RATE, ChartDataPropertiesUtils.getHeartRateDataProperties(this.mRunEvent, valueGetter, age, hrMax)));
                    }
                    this.mChartFragment = ChartFragment.newInstance(charts, XAxisProvider.MEASURED, XAxisStrategyProvider.RUN, properties, isDistanceHeightMetric);
                }
                DataProvider runProvider = DataProviderUtils.createRunEventDataProvider(this.mRunEvent);
                this.mChartFragment.setDataProvider(runProvider);
                addChartFragment(R.id.run_chart, this.mChartFragment, RUN_CHART_TAG);
            } catch (Exception ex) {
                KLog.d(this.TAG, "unable to load chart", ex);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ParseRunSplits(RunEventSequence[] runEventSequencesArray) {
        int i;
        List<RunSplitSummary> runSummaryList = new ArrayList<>();
        boolean isDistanceMetric = this.mSettings.isDistanceHeightMetric();
        int fastestDuration = Integer.MAX_VALUE;
        int slowestDuration = 0;
        int fastestIndex = -1;
        int slowestIndex = -1;
        while (i < runEventSequencesArray.length) {
            RunEventSequence current = runEventSequencesArray[i];
            if (i == runEventSequencesArray.length - 1) {
                i = ConversionUtils.CmToDistance(current.getSplitDistance(), isDistanceMetric) < Constants.SPLITS_ACCURACY ? i + 1 : 0;
            } else {
                double distance = ConversionUtils.DistanceToCm(current.getOrder(), isDistanceMetric);
                current.setTotalDistance((int) distance);
            }
            RunSplitSummary split = new RunSplitSummary(getActivity(), current, isDistanceMetric);
            runSummaryList.add(split);
            boolean isFullSplit = i == runEventSequencesArray.length + (-1) ? split.isFullSplit(isDistanceMetric) : true;
            if (isFullSplit) {
                if (fastestDuration > current.getDuration()) {
                    fastestDuration = current.getDuration();
                    fastestIndex = runSummaryList.size() - 1;
                }
                if (slowestDuration < current.getDuration()) {
                    slowestDuration = current.getDuration();
                    slowestIndex = runSummaryList.size() - 1;
                }
            }
        }
        if (fastestIndex > -1 && slowestIndex > -1 && fastestIndex != slowestIndex) {
            runSummaryList.get(slowestIndex).setSplitType(RunSplitSummary.SplitType.SLOWEST);
            runSummaryList.get(fastestIndex).setSplitType(RunSplitSummary.SplitType.FASTEST);
        }
        this.mSplitsList.setAdapter((ListAdapter) new RunSplitsAdapter(getActivity(), runSummaryList));
    }

    public FragmentActivity getNestedFragmentHostActivity() {
        return getParentFragment().getActivity();
    }
}

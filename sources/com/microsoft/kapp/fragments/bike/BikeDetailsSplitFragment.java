package com.microsoft.kapp.fragments.bike;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.adapters.BikeSplitsAdapter;
import com.microsoft.kapp.adapters.BikeSplitsGroupedAdapter;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.fragments.BaseHomeTileFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.BikeSplitSummary;
import com.microsoft.kapp.models.BikeSplitSummaryGroup;
import com.microsoft.kapp.models.RunSplitSummary;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.ConversionUtils;
import com.microsoft.kapp.utils.DataProviderUtils;
import com.microsoft.kapp.utils.ProfileUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.models.BikeEventSequence;
import com.microsoft.krestsdk.services.RestService;
import com.shinobicontrols.kcompanionapp.charts.ChartFragment;
import com.shinobicontrols.kcompanionapp.charts.DataProvider;
import com.shinobicontrols.kcompanionapp.charts.ElevationChart;
import com.shinobicontrols.kcompanionapp.charts.HeartRateChart;
import com.shinobicontrols.kcompanionapp.charts.IChartView;
import com.shinobicontrols.kcompanionapp.charts.SpeedChart;
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
public class BikeDetailsSplitFragment extends BaseHomeTileFragment {
    private static final String CHART_TAG = "chart_tag";
    private static final String EVENT_ID = "event_id";
    private static final int SPLIT_INDEX_INVALID = -1;
    private BikeEvent mBikeEvent;
    private ChartFragment mChartFragment;
    private String mEventId;
    private ExpandableListView mGroupedList;
    @Inject
    RestService mRestService;
    @Inject
    SettingsProvider mSettings;
    private ListView mSplitsList;
    private ListView mUngroupedList;

    public static BaseFragment newInstance(String eventID, Boolean isL2View) {
        BikeDetailsSplitFragment fragment = new BikeDetailsSplitFragment();
        Bundle bundle = new Bundle();
        bundle.putString("event_id", eventID);
        bundle.putBoolean(Constants.EVENT_L2_VIEW, isL2View.booleanValue());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle savedBundle = savedInstanceState == null ? getArguments() : savedInstanceState;
        this.mEventId = savedBundle.getString("event_id");
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_BIKE_SPLITS);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bike_details_splits_fragment, container, false);
        this.mUngroupedList = (ListView) ViewUtils.getValidView(view, R.id.bike_splits_list, ListView.class);
        this.mUngroupedList.addHeaderView(getActivity().getLayoutInflater().inflate(R.layout.bike_splits_list_header, (ViewGroup) this.mUngroupedList, false));
        this.mGroupedList = (ExpandableListView) ViewUtils.getValidView(view, R.id.bike_splits_grouped_list, ExpandableListView.class);
        this.mGroupedList.addHeaderView(getActivity().getLayoutInflater().inflate(R.layout.bike_splits_list_grouped_header, (ViewGroup) this.mGroupedList, false));
        return view;
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment
    public void fetchData() {
        setState(1233);
        ArrayList<RestService.ExpandType> expandTypes = new ArrayList<>();
        expandTypes.add(RestService.ExpandType.INFO);
        expandTypes.add(RestService.ExpandType.SEQUENCES);
        expandTypes.add(RestService.ExpandType.MAPPOINTS);
        this.mRestService.getBikeEventById(this.mSettings.isDistanceHeightMetric(), this.mEventId, expandTypes, new ActivityScopedCallback(this, new Callback<BikeEvent>() { // from class: com.microsoft.kapp.fragments.bike.BikeDetailsSplitFragment.1
            @Override // com.microsoft.kapp.Callback
            public void callback(BikeEvent result) {
                BikeDetailsSplitFragment.this.mBikeEvent = result;
                if (BikeDetailsSplitFragment.this.mBikeEvent != null && BikeDetailsSplitFragment.this.isAdded()) {
                    boolean isGroupedView = BikeDetailsSplitFragment.this.isGroupedView();
                    BikeDetailsSplitFragment.this.mSplitsList = isGroupedView ? BikeDetailsSplitFragment.this.mGroupedList : BikeDetailsSplitFragment.this.mUngroupedList;
                    BikeDetailsSplitFragment.this.mGroupedList.setVisibility(isGroupedView ? 0 : 8);
                    BikeDetailsSplitFragment.this.mUngroupedList.setVisibility(isGroupedView ? 8 : 0);
                    try {
                        BikeDetailsSplitFragment.this.loadMapChartData();
                        BikeDetailsSplitFragment.this.parseSplits(BikeDetailsSplitFragment.this.mBikeEvent.getSequences());
                        BikeDetailsSplitFragment.this.setState(1234);
                        return;
                    } catch (Exception ex) {
                        KLog.e(BikeDetailsSplitFragment.this.TAG, "exception loading bike event data", ex);
                        BikeDetailsSplitFragment.this.setState(1235);
                        return;
                    }
                }
                BikeDetailsSplitFragment.this.setState(1235);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(BikeDetailsSplitFragment.this.TAG, "getting bikeEvent failed.", ex);
                BikeDetailsSplitFragment.this.setState(1235);
            }
        }));
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("event_id", this.mEventId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isGroupedView() {
        return this.mBikeEvent == null || this.mBikeEvent.getSplitGroupSize() > 1;
    }

    public void loadMapChartData() {
        if (getView() != null) {
            try {
                int age = ProfileUtils.getUserAge(this.mSettings);
                boolean isDistanceHeightMetric = this.mSettings.isDistanceHeightMetric();
                if (this.mChartFragment == null) {
                    List<IChartView> charts = new ArrayList<>();
                    ChartConfig config = new ChartConfig(getActivity(), age, this.mBikeEvent.getHRZones().getMaxHr(), isDistanceHeightMetric);
                    ChartDataProperties properties = ChartDataPropertiesUtils.getBikeEventDataProperties(this.mBikeEvent, isDistanceHeightMetric);
                    this.mBikeEvent.setInfo(this.mBikeEvent.getUpdatedInfoSequenceWithPauseTime());
                    charts.add(new SpeedChart(config, YAxisStrategyProvider.SPEED, ChartDataPropertiesUtils.getSpeedDataProperties(this.mBikeEvent, isDistanceHeightMetric)));
                    charts.add(new HeartRateChart(config, YAxisStrategyProvider.HEART_RATE, ChartDataPropertiesUtils.getHeartRateDataPropertiesForDistance(this.mBikeEvent, new TypedValueGetter(getActivity().getResources()), isDistanceHeightMetric)));
                    charts.add(new ElevationChart(config, YAxisStrategyProvider.ELEVATION, ChartDataPropertiesUtils.getElevationDataProperties(this.mBikeEvent, isDistanceHeightMetric)));
                    this.mChartFragment = ChartFragment.newInstance(charts, XAxisProvider.MEASURED, XAxisStrategyProvider.BIKE, properties, isDistanceHeightMetric);
                }
                DataProvider bikeProvider = DataProviderUtils.createBikeEventDataProvider(this.mBikeEvent);
                this.mChartFragment.setDataProvider(bikeProvider);
                addChartFragment(isGroupedView() ? R.id.bike_chart_grouped : R.id.bike_chart, this.mChartFragment, CHART_TAG);
            } catch (Exception ex) {
                KLog.d(this.TAG, "unable to load chart", ex);
            }
        }
    }

    private List<BikeSplitSummaryGroup> getSplitGroups(List<BikeSplitSummary> splits) {
        boolean isDistanceMetric = this.mSettings.isDistanceHeightMetric();
        int customSplitSize = this.mBikeEvent.getSplitGroupSize();
        ArrayList<BikeSplitSummaryGroup> results = new ArrayList<>();
        ArrayList<BikeSplitSummary> currentSet = new ArrayList<>();
        double fastestSpeed = Constants.SPLITS_ACCURACY;
        double slowestSpeed = Double.MAX_VALUE;
        int fastestIndex = -1;
        int slowestIndex = -1;
        for (int i = 0; i < splits.size(); i++) {
            BikeSplitSummary current = splits.get(i);
            currentSet.add(current);
            if (results.size() > 0 && currentSet.size() == 1 && current.isEmpty()) {
                results.get(results.size() - 1).setIsEstimation(getActivity());
            } else if (currentSet.size() == customSplitSize || i == splits.size() - 1) {
                BikeSplitSummaryGroup group = new BikeSplitSummaryGroup(getActivity(), currentSet, isDistanceMetric);
                results.add(group);
                if (currentSet.size() == customSplitSize) {
                    if (fastestSpeed < group.getSpeedValue()) {
                        fastestSpeed = group.getSpeedValue();
                        fastestIndex = results.size() - 1;
                    }
                    if (group.getSpeedValue() > Constants.SPLITS_ACCURACY && slowestSpeed > group.getSpeedValue()) {
                        slowestSpeed = group.getSpeedValue();
                        slowestIndex = results.size() - 1;
                    }
                }
                currentSet.clear();
            }
        }
        if (fastestIndex > -1 && slowestIndex > -1 && fastestIndex != slowestIndex) {
            results.get(slowestIndex).setSplitType(RunSplitSummary.SplitType.SLOWEST);
            results.get(fastestIndex).setSplitType(RunSplitSummary.SplitType.FASTEST);
        }
        return results;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void parseSplits(BikeEventSequence[] bikeEventSequencesArray) {
        boolean isDistanceMetric = this.mSettings.isDistanceHeightMetric();
        int sequenceCount = bikeEventSequencesArray[bikeEventSequencesArray.length - 1].getOrder();
        ArrayList<BikeSplitSummary> filteredSplits = new ArrayList<>(sequenceCount);
        int fastestSpeed = Integer.MIN_VALUE;
        int slowestSpeed = Integer.MAX_VALUE;
        int fastestIndex = -1;
        int slowestIndex = -1;
        int missingSequenceCount = 0;
        int lastOrder = 0;
        int splitCount = bikeEventSequencesArray[bikeEventSequencesArray.length - 1].getOrder();
        for (int i = 0; i < bikeEventSequencesArray.length; i++) {
            BikeEventSequence current = bikeEventSequencesArray[i];
            int splitNumber = current.getOrder();
            if (i != bikeEventSequencesArray.length - 1) {
                double distance = ConversionUtils.DistanceToCm(current.getOrder(), isDistanceMetric);
                current.setTotalDistance((int) distance);
            }
            BikeSplitSummary summary = new BikeSplitSummary(getActivity(), current, isDistanceMetric);
            for (int c = lastOrder + 1; c < current.getOrder(); c++) {
                BikeEventSequence missing = new BikeEventSequence();
                missing.setOrder(c);
                missing.setTotalDistance((int) ConversionUtils.DistanceToCm(c, isDistanceMetric));
                filteredSplits.add(new BikeSplitSummary(getActivity(), missing, true, isDistanceMetric));
                missingSequenceCount++;
                if (c == lastOrder + 1 && i != 0) {
                    filteredSplits.get(i - 1).setIsEstimation(getActivity());
                }
                summary.setIsEstimation(getActivity());
            }
            filteredSplits.add(summary);
            lastOrder = current.getOrder();
            if ((splitNumber != splitCount || summary.isFullSplit()) && (splitNumber != splitCount || current.getSplitDistance() == this.mBikeEvent.getSplitDistance())) {
                int currentSpeed = current.getSplitSpeed();
                if (currentSpeed > fastestSpeed) {
                    fastestSpeed = currentSpeed;
                    fastestIndex = filteredSplits.size() - 1;
                }
                if (currentSpeed < slowestSpeed) {
                    slowestSpeed = currentSpeed;
                    slowestIndex = filteredSplits.size() - 1;
                }
            }
        }
        if (fastestIndex > -1 && slowestIndex > -1 && fastestIndex != slowestIndex) {
            filteredSplits.get(slowestIndex).setSplitType(RunSplitSummary.SplitType.SLOWEST);
            filteredSplits.get(fastestIndex).setSplitType(RunSplitSummary.SplitType.FASTEST);
        }
        if (isGroupedView()) {
            ((ExpandableListView) this.mSplitsList).setAdapter(new BikeSplitsGroupedAdapter(getActivity(), getSplitGroups(filteredSplits)));
            if (missingSequenceCount > 0 && this.mSplitsList.getFooterViewsCount() == 0) {
                this.mSplitsList.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.bike_details_splits_footer, (ViewGroup) null));
                return;
            }
            return;
        }
        this.mSplitsList.setAdapter((ListAdapter) new BikeSplitsAdapter(getActivity(), filteredSplits));
    }

    public FragmentActivity getNestedFragmentHostActivity() {
        return getParentFragment().getActivity();
    }
}

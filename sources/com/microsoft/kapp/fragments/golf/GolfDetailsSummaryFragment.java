package com.microsoft.kapp.fragments.golf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.FeedbackActivity;
import com.microsoft.kapp.activities.HomeActivity;
import com.microsoft.kapp.activities.NetworkConnectivityChangedReceiver;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.feedback.FeedbackUtilsV1;
import com.microsoft.kapp.fragments.BaseEventSummaryFragment;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.logging.models.FeedbackDescription;
import com.microsoft.kapp.services.golf.GolfService;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.DataProviderUtils;
import com.microsoft.kapp.utils.EventUtils;
import com.microsoft.kapp.utils.GolfUtils;
import com.microsoft.kapp.utils.ProfileUtils;
import com.microsoft.kapp.utils.ShareUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.TrackerStatsWidget;
import com.microsoft.krestsdk.models.GolfEvent;
import com.microsoft.krestsdk.models.GolfEventHoleSequence;
import com.microsoft.krestsdk.models.UserEvent;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.kcompanionapp.charts.ChartFragment;
import com.shinobicontrols.kcompanionapp.charts.HeartRatePeakDisplayChart;
import com.shinobicontrols.kcompanionapp.charts.IChartView;
import com.shinobicontrols.kcompanionapp.charts.ParChart;
import com.shinobicontrols.kcompanionapp.charts.ViewDataHelper.HRPeakChartViewDataHelper;
import com.shinobicontrols.kcompanionapp.charts.internal.TypedValueGetter;
import com.shinobicontrols.kcompanionapp.properties.ChartConfig;
import com.shinobicontrols.kcompanionapp.properties.ChartDataProperties;
import com.shinobicontrols.kcompanionapp.properties.ChartDataPropertiesUtils;
import com.shinobicontrols.kcompanionapp.providers.XAxisProvider;
import com.shinobicontrols.kcompanionapp.providers.XAxisStrategyProvider;
import com.shinobicontrols.kcompanionapp.providers.YAxisStrategyProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class GolfDetailsSummaryFragment extends BaseEventSummaryFragment implements NetworkConnectivityChangedReceiver.OnNetworkConnectivityChanged {
    private static final String GOLF_CHART_TAG = "golf_chart_tag";
    private ChartFragment mChartFragment;
    private GolfEvent mGolfEvent;
    @Inject
    GolfService mGolfService;
    @Inject
    GolfUtils mGolfUtils;
    private TrackerStatsWidget mStatsWidget;
    private View mUVDisclaimer;

    public static BaseFragment newInstance(String eventID, Boolean isL2View) {
        GolfDetailsSummaryFragment fragment = new GolfDetailsSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user_event_id", eventID);
        bundle.putBoolean(Constants.EVENT_L2_VIEW, isL2View.booleanValue());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    public View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.golf_details_summary_fragment, container, false);
        this.mStatsWidget = (TrackerStatsWidget) ViewUtils.getValidView(childView, R.id.golf_details_tracker_widget, TrackerStatsWidget.class);
        this.mUVDisclaimer = (View) ViewUtils.getValidView(childView, R.id.txtUVDisclaimer, View.class);
        return childView;
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment, android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.mUserEventId = savedInstanceState.getString("user_event_id");
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("user_event_id", this.mUserEventId);
    }

    @Override // com.microsoft.kapp.fragments.BaseHomeTileFragment
    public void fetchData() {
        setState(1233);
        this.mGolfService.getGolfEventById(this.mUserEventId, true, new ActivityScopedCallback(this, new Callback<GolfEvent>() { // from class: com.microsoft.kapp.fragments.golf.GolfDetailsSummaryFragment.1
            @Override // com.microsoft.kapp.Callback
            public void callback(GolfEvent result) {
                GolfDetailsSummaryFragment.this.mGolfEvent = result;
                if (GolfDetailsSummaryFragment.this.mGolfEvent != null) {
                    try {
                        GolfDetailsSummaryFragment.this.setCurrentEventName(GolfDetailsSummaryFragment.this.mGolfEvent.getName());
                        GolfDetailsSummaryFragment.this.addTrackerStats();
                        GolfDetailsSummaryFragment.this.loadChartData();
                        GolfDetailsSummaryFragment.this.mUVDisclaimer.setVisibility(EventUtils.getUVDisclaimerViewVisibility(GolfDetailsSummaryFragment.this.mGolfEvent));
                        TextView reportAProblem = (TextView) ViewUtils.getValidView(GolfDetailsSummaryFragment.this.getView(), R.id.event_report_a_problem, TextView.class);
                        reportAProblem.setText(R.string.feedback_send_feedback_on_round);
                        reportAProblem.setVisibility(0);
                        reportAProblem.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfDetailsSummaryFragment.1.1
                            @Override // android.view.View.OnClickListener
                            public void onClick(View v) {
                                GolfDetailsSummaryFragment.this.reportAProblem();
                            }
                        });
                        TextView findACourse = (TextView) ViewUtils.getValidView(GolfDetailsSummaryFragment.this.getView(), R.id.event_find_a_course, TextView.class);
                        findACourse.setVisibility(0);
                        findACourse.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.golf.GolfDetailsSummaryFragment.1.2
                            @Override // android.view.View.OnClickListener
                            public void onClick(View v) {
                                GolfDetailsSummaryFragment.this.navigateToGolfLandingPage();
                            }
                        });
                        GolfDetailsSummaryFragment.this.setState(1234);
                        return;
                    } catch (Exception ex) {
                        KLog.e(GolfDetailsSummaryFragment.this.TAG, "exception loading golf data", ex);
                        GolfDetailsSummaryFragment.this.setState(1235);
                        return;
                    }
                }
                GolfDetailsSummaryFragment.this.setState(1235);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(GolfDetailsSummaryFragment.this.TAG, "getting GolfEvent failed.", ex);
                GolfDetailsSummaryFragment.this.setState(1235);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addTrackerStats() {
        this.mStatsWidget.setStats(this.mGolfUtils.getGolfTrackerStats(getActivity(), this.mSettings, this.mGolfEvent));
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected boolean hasHistoryView() {
        return true;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected int getFilterType() {
        return 257;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_GOLF_SUMMARY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadChartData() {
        if (getView() != null) {
            try {
                int age = ProfileUtils.getUserAge(this.mSettings);
                boolean isDistanceHeightMetric = this.mSettings.isDistanceHeightMetric();
                if (this.mChartFragment == null) {
                    List<IChartView> charts = new ArrayList<>();
                    ChartConfig config = new ChartConfig(getActivity(), age, 0, isDistanceHeightMetric);
                    TypedValueGetter valueGetter = new TypedValueGetter(getResources());
                    ChartDataProperties properties = ChartDataPropertiesUtils.getParChartDataProperties(this.mGolfEvent, valueGetter);
                    GolfEventHoleSequence[] sortedSequences = this.mGolfEvent.getSortedSequences();
                    double maxHolePlayed = 18.0d;
                    if (sortedSequences != null && sortedSequences.length > 0) {
                        maxHolePlayed = sortedSequences[sortedSequences.length - 1].getHoleNumber();
                    }
                    ChartDataProperties xDataProperties = new ChartDataProperties(Constants.SPLITS_ACCURACY, maxHolePlayed, 0, null);
                    HRPeakChartViewDataHelper hearRateChartDataHelper = new HRPeakChartViewDataHelper(this.mGolfEvent);
                    charts.add(new ParChart(config, null, properties, ""));
                    charts.add(new HeartRatePeakDisplayChart(config, YAxisStrategyProvider.GOLF, hearRateChartDataHelper));
                    this.mChartFragment = ChartFragment.newInstance(charts, XAxisProvider.GOLF, XAxisStrategyProvider.GOLF, xDataProperties, isDistanceHeightMetric);
                    this.mChartFragment.setDataProvider(DataProviderUtils.createGolfEventDataProvider(this.mGolfEvent));
                    addChartFragment(R.id.golf_chart, this.mChartFragment, GOLF_CHART_TAG);
                }
            } catch (Exception ex) {
                KLog.d(this.TAG, "unable to load chart", ex);
            }
        }
    }

    public FragmentActivity getNestedFragmentHostActivity() {
        return getParentFragment().getActivity();
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected UserEvent getUserEvent() {
        return this.mGolfEvent;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected void onShareRequested() {
        Activity activity = getActivity();
        if (this.mChartFragment == null || !Validate.isActivityAlive(activity)) {
            shareEvent(null);
        } else {
            requestSnapShotFromChartFragment(this.mChartFragment, new ShinobiChart.OnSnapshotDoneListener() { // from class: com.microsoft.kapp.fragments.golf.GolfDetailsSummaryFragment.2
                @Override // com.shinobicontrols.charts.ShinobiChart.OnSnapshotDoneListener
                public void onSnapshotDone(Bitmap bitmap) {
                    GolfDetailsSummaryFragment.this.shareEvent(ShareUtils.createGolfShareEvent(GolfDetailsSummaryFragment.this.getString(R.string.chooser_share_golf), GolfDetailsSummaryFragment.this.mGolfEvent, GolfDetailsSummaryFragment.this.getString(R.string.share_golf_title), GolfDetailsSummaryFragment.this.getString(R.string.golf_share_tag), bitmap, GolfDetailsSummaryFragment.this.getActivity()), GolfDetailsSummaryFragment.this.getString(R.string.golf_share_tag));
                }
            });
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected int getEventType() {
        return 5;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment, com.microsoft.kapp.activities.NetworkConnectivityChangedReceiver.OnNetworkConnectivityChanged
    public void onNetworkDisconnected() {
        super.onNetworkDisconnected();
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment, com.microsoft.kapp.activities.NetworkConnectivityChangedReceiver.OnNetworkConnectivityChanged
    public void onNetworkConnected() {
        super.onNetworkConnected();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void navigateToGolfLandingPage() {
        Activity activity = getActivity();
        if (activity instanceof HomeActivity) {
            ((HomeActivity) HomeActivity.class.cast(getActivity())).navigateToFragment(GolfLandingPageFragment.class, null, true, false);
            return;
        }
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.putExtra(HomeActivity.STARTING_PAGE, GolfLandingPageFragment.class.getSimpleName());
        startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reportAProblem() {
        FeedbackUtilsV1.captureScreenshotsAsync(getActivity(), new ActivityScopedCallback(getActivity(), new Callback<ArrayList<String>>() { // from class: com.microsoft.kapp.fragments.golf.GolfDetailsSummaryFragment.3
            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
            }

            @Override // com.microsoft.kapp.Callback
            public void callback(ArrayList<String> result) {
                Intent intent = new Intent(GolfDetailsSummaryFragment.this.getActivity(), FeedbackActivity.class);
                intent.putExtra(FeedbackActivity.KEY_SCREENSHOT, result);
                intent.putExtra(FeedbackActivity.KEY_SENDER, GolfDetailsSummaryFragment.class.getSimpleName());
                intent.putExtra(FeedbackActivity.KEY_CATEGORY, FeedbackDescription.FeedbackCategory.Golf);
                intent.putExtra(FeedbackActivity.KEY_SUBCATEGORY, FeedbackDescription.FeedbackSubcategory.EventProblem);
                HashMap<String, String> feedbackDescription = new HashMap<>();
                feedbackDescription.put("eventId", String.valueOf(GolfDetailsSummaryFragment.this.mUserEventId));
                intent.putExtra(FeedbackActivity.KEY_HEADER_TEXT, GolfDetailsSummaryFragment.this.getResources().getString(R.string.feedback_send_feedback_on_round));
                intent.putExtra(FeedbackActivity.KEY_SKIP_INITIAL_PAGE, true);
                intent.putExtra(FeedbackActivity.KEY_DESCRIPTION_PROPERTIES, feedbackDescription);
                GolfDetailsSummaryFragment.this.getActivity().startActivity(intent);
            }
        }));
    }
}

package com.microsoft.kapp.fragments.sleep;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.R;
import com.microsoft.kapp.ScopedAsyncTask;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.diagnostics.Compatibility;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.feedback.FeedbackUtilsV1;
import com.microsoft.kapp.fragments.BaseEventSummaryFragment;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.kapp.utils.TrackerStatUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.BaseTrackerStat;
import com.microsoft.kapp.views.SingleTrackerStat;
import com.microsoft.kapp.views.TrackerStatsWidget;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.models.ExerciseEvent;
import com.microsoft.krestsdk.models.GolfEvent;
import com.microsoft.krestsdk.models.RaisedInsight;
import com.microsoft.krestsdk.models.RunEvent;
import com.microsoft.krestsdk.models.SleepEvent;
import com.microsoft.krestsdk.models.UserActivity;
import com.microsoft.krestsdk.models.UserDailySummary;
import com.microsoft.krestsdk.models.UserEvent;
import com.microsoft.krestsdk.services.RestService;
import com.shinobicontrols.kcompanionapp.charts.DataProvider;
import com.shinobicontrols.kcompanionapp.charts.SleepChartFragment;
import com.shinobicontrols.kcompanionapp.charts.internal.BaseChartFragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class SleepDetailsSummaryFragment extends BaseEventSummaryFragment implements DataProvider {
    private static final String SLEEP_CHART_TAG = "sleep_chart_tag";
    private BaseChartFragment mChartFragment;
    private Context mContext;
    @Inject
    CredentialsManager mCredentialsManager;
    private SleepEvent mSleepEvent;
    private TrackerStatsWidget mTrackerStats;

    public static BaseFragment newInstance(String eventID, Boolean isL2View) {
        SleepDetailsSummaryFragment fragment = new SleepDetailsSummaryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user_event_id", eventID);
        bundle.putBoolean(Constants.EVENT_L2_VIEW, isL2View.booleanValue());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("user_event_id", this.mUserEventId);
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getHostActivity().getApplicationContext();
        disableEventOption(6);
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    public View onCreateEventView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View childView = inflater.inflate(R.layout.sleep_details_summary_fragment_v1, container, false);
        this.mTrackerStats = (TrackerStatsWidget) ViewUtils.getValidView(childView, R.id.sleep_details_stats, TrackerStatsWidget.class);
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
        if (this.mSettings.isRaisedInsightsEnabled()) {
            expandTypes.add(RestService.ExpandType.EVIDENCE);
        }
        this.mRestService.getSleepEventById(this.mUserEventId, expandTypes, new ActivityScopedCallback(this, new Callback<SleepEvent>() { // from class: com.microsoft.kapp.fragments.sleep.SleepDetailsSummaryFragment.1
            @Override // com.microsoft.kapp.Callback
            public void callback(SleepEvent result) {
                SleepDetailsSummaryFragment.this.mSleepEvent = result;
                if (SleepDetailsSummaryFragment.this.mSleepEvent != null) {
                    try {
                        SleepDetailsSummaryFragment.this.loadChartData();
                        SleepDetailsSummaryFragment.this.mTrackerStats.setStats(SleepDetailsSummaryFragment.this.getStats());
                        if (!Compatibility.isPublicRelease() && result.getIsAutoSleep()) {
                            TextView autoSleepIncorrectFlag = (TextView) ViewUtils.getValidView(SleepDetailsSummaryFragment.this.getView(), R.id.event_report_a_problem, TextView.class);
                            autoSleepIncorrectFlag.setVisibility(0);
                            autoSleepIncorrectFlag.setText(SleepDetailsSummaryFragment.this.getActivity().getResources().getString(R.string.sleep_feedback_report_a_problem));
                            autoSleepIncorrectFlag.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.sleep.SleepDetailsSummaryFragment.1.1
                                @Override // android.view.View.OnClickListener
                                public void onClick(View v) {
                                    SleepDetailsSummaryFragment.this.reportIncorrectSleep();
                                }
                            });
                        }
                        SleepDetailsSummaryFragment.this.loadInsight();
                        SleepDetailsSummaryFragment.this.setState(1234);
                        return;
                    } catch (Exception ex) {
                        KLog.e(SleepDetailsSummaryFragment.this.TAG, "exception loading sleep data", ex);
                        SleepDetailsSummaryFragment.this.setState(1235);
                        return;
                    }
                }
                SleepDetailsSummaryFragment.this.setState(1235);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(SleepDetailsSummaryFragment.this.TAG, "getting sleepEvent failed.", ex);
                SleepDetailsSummaryFragment.this.setState(1235);
            }
        }));
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment, com.microsoft.kapp.fragments.BaseHomeTileFragment, com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_SLEEP_SUMMARY);
    }

    public void reportIncorrectSleep() {
        getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.app_name), Integer.valueOf((int) R.string.sleep_details_report_incorrect_autosleep), new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.sleep.SleepDetailsSummaryFragment.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (SleepDetailsSummaryFragment.this.isAdded()) {
                    String currentDescription = SleepDetailsSummaryFragment.this.getResources().getString(R.string.report_incorrect_sleep);
                    if (SleepDetailsSummaryFragment.this.mSleepEvent != null) {
                        currentDescription = currentDescription + SleepDetailsSummaryFragment.this.getResources().getString(R.string.report_incorrect_sleep_event_id, SleepDetailsSummaryFragment.this.mSleepEvent.getEventId());
                    }
                    FeedbackUtilsV1.sendFeedbackAsync(SleepDetailsSummaryFragment.this.getActivity().getWindow().getDecorView(), SleepDetailsSummaryFragment.this.getActivity(), SleepDetailsSummaryFragment.this.mCargoConnection, currentDescription);
                    ReportAutoSleepIncorrectTelemetryTask task = new ReportAutoSleepIncorrectTelemetryTask(SleepDetailsSummaryFragment.this);
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                }
            }
        }, (DialogInterface.OnClickListener) null, DialogPriority.LOW);
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected boolean hasHistoryView() {
        return true;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected int getFilterType() {
        return Constants.FILTER_TYPE_SLEEP;
    }

    public void loadChartData() {
        View view = getView();
        if (view != null) {
            try {
                if (this.mChartFragment == null) {
                    this.mChartFragment = SleepChartFragment.newInstance();
                    this.mChartFragment.setRetainInstance(true);
                }
                this.mChartFragment.setDataProvider(this);
                addChartFragment(R.id.chart_fragment, this.mChartFragment, SLEEP_CHART_TAG);
            } catch (Exception ex) {
                KLog.d(this.TAG, "unable to load chart", ex);
            }
        }
    }

    protected FragmentActivity getHostActivity() {
        return getActivity();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<BaseTrackerStat> getStats() {
        List<BaseTrackerStat> stats = new ArrayList<>();
        SingleTrackerStat deepSleep = new SingleTrackerStat(this.mContext, getResources().getString(R.string.tracker_header_restful_sleep), R.string.glyph_signal);
        SingleTrackerStat lightSleep = new SingleTrackerStat(this.mContext, getResources().getString(R.string.tracker_header_light_sleep), R.string.glyph_signal);
        SingleTrackerStat numberOfWakeups = new SingleTrackerStat(this.mContext, getResources().getString(R.string.tracker_header_number_of_wakeups), R.string.glyph_awake);
        SingleTrackerStat timeToFallAsleep = new SingleTrackerStat(this.mContext, getResources().getString(R.string.tracker_header_time_to_fall_asleep), R.string.glyph_Fall_Asleep);
        SingleTrackerStat sleepEfficiency = new SingleTrackerStat(this.mContext, getResources().getString(R.string.tracker_header_sleep_efficiency), R.string.glyph_sleep_2);
        SingleTrackerStat sleepDuration = new SingleTrackerStat(this.mContext, getResources().getString(R.string.tracker_header_duration), R.string.glyph_duration);
        SingleTrackerStat caloriesBurned = new SingleTrackerStat(this.mContext, getResources().getString(R.string.tracker_header_sleep_calories_burned), R.string.glyph_calories);
        SingleTrackerStat restingHR = new SingleTrackerStat(this.mContext, getResources().getString(R.string.tracker_header_sleep_resting_hearth_rate), R.string.glyph_heart_fill);
        deepSleep.setValue(Formatter.formatTimeInHoursAndMin(this.mContext, R.array.MerticSmallUnitFormat, this.mSleepEvent.getTotalRestfulSleep()));
        lightSleep.setValue(Formatter.formatTimeInHoursAndMin(this.mContext, R.array.MerticSmallUnitFormat, this.mSleepEvent.getTotalRestlessSleep()));
        timeToFallAsleep.setValue(this.mSleepEvent.getTimeToFallAsleep() < 1 ? Formatter.getSubtextSpannable(this.mContext, this.mContext.getString(R.string.no_value), 0) : Formatter.formatTimeInHoursAndMin(this.mContext, R.array.MerticSmallUnitFormat, this.mSleepEvent.getTimeToFallAsleep()));
        sleepEfficiency.setValue(Formatter.formatToPercentage(this.mContext, this.mSleepEvent.getSleepEfficiencyPercentage()));
        sleepDuration.setValue(Formatter.formatTimeInHoursAndMin(this.mContext, R.array.MerticSmallUnitFormat, this.mSleepEvent.getDuration()));
        caloriesBurned.setValue(String.valueOf(this.mSleepEvent.getCaloriesBurned()));
        numberOfWakeups.setValue(Formatter.formatNumberOfTimes(this.mContext, this.mSleepEvent.getNumberOfWakeups()));
        restingHR.setValue(getRestingHR());
        BaseTrackerStat restoration = TrackerStatUtils.getSleepRestorationStat(this.mSleepEvent, this.mContext);
        stats.add(deepSleep);
        stats.add(lightSleep);
        stats.add(numberOfWakeups);
        stats.add(timeToFallAsleep);
        stats.add(sleepEfficiency);
        stats.add(sleepDuration);
        stats.add(restingHR);
        stats.add(caloriesBurned);
        stats.add(restoration);
        return stats;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
    public UserActivity[] getHourlyUserActivitiesForDay() {
        return null;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
    public UserDailySummary[] getDailySummariesForWeek() {
        return null;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
    public RunEvent getRunEvent() {
        return null;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
    public SleepEvent getSleepEvent() {
        return this.mSleepEvent;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
    public ExerciseEvent getExerciseEvent() {
        return null;
    }

    private CharSequence getRestingHR() {
        return this.mSleepEvent.getRestingHR() <= 0 ? getString(R.string.no_value) : String.valueOf(this.mSleepEvent.getRestingHR());
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected UserEvent getUserEvent() {
        return this.mSleepEvent;
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected int getEventType() {
        return 2;
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
    public BikeEvent getBikeEvent() {
        return null;
    }

    /* loaded from: classes.dex */
    private class ReportAutoSleepIncorrectTelemetryTask extends ScopedAsyncTask<Void, Void, Void> {
        public ReportAutoSleepIncorrectTelemetryTask(OnTaskListener onTaskListener) {
            super(onTaskListener);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public Void doInBackground(Void... params) {
            try {
                HashMap<String, String> telemetryProperties = new HashMap<>();
                telemetryProperties.put(TelemetryConstants.Events.SleepEvent.AutoDetectReport.Dimensions.USER_ID, SleepDetailsSummaryFragment.this.mCredentialsManager.getCredentials().getKdsCredential().getUserId());
                telemetryProperties.put(TelemetryConstants.Events.SleepEvent.AutoDetectReport.Dimensions.SLEEP_ID, String.valueOf(SleepDetailsSummaryFragment.this.mSleepEvent.getEventId()));
                Telemetry.logEvent(TelemetryConstants.Events.SleepEvent.AutoDetectReport.FITNESS_SLEEP_AUTO_DETECT_REPORT, telemetryProperties, null);
            } catch (Exception ex) {
                KLog.e(this.TAG, "Could not post telemetry for sleep event!", ex);
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.ScopedAsyncTask
        public void onPostExecute(Void result) {
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseEventSummaryFragment
    protected List<RaisedInsight> getInsights() {
        return this.mSleepEvent.getPrimaryRaisedInsights();
    }

    @Override // com.shinobicontrols.kcompanionapp.charts.DataProvider
    public GolfEvent getGolfEvent() {
        return null;
    }
}

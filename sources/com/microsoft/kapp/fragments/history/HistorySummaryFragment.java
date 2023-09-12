package com.microsoft.kapp.fragments.history;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.HistoryFilterActivity;
import com.microsoft.kapp.activities.UserEventDetailsActivity;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport;
import com.microsoft.kapp.fragments.EventHistorySummaryFragment;
import com.microsoft.kapp.models.EventData;
import com.microsoft.kapp.models.PersonalBest;
import com.microsoft.kapp.models.TimeSpan;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.EventUtils;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.kapp.utils.HistoryUtils;
import com.microsoft.kapp.utils.KAppDateFormatter;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.CustomGlyphView;
import com.microsoft.kapp.views.PersonalBestSingleTrackerStat;
import com.microsoft.kapp.views.TrackableScrollView;
import com.microsoft.kapp.views.TrackerStatsWidget;
import com.microsoft.kapp.widgets.NoHistoryWidget;
import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.models.ExerciseEvent;
import com.microsoft.krestsdk.models.GuidedWorkoutEvent;
import com.microsoft.krestsdk.models.RunEvent;
import com.microsoft.krestsdk.models.UserEvent;
import com.microsoft.krestsdk.services.RestService;
import java.util.ArrayList;
import java.util.Iterator;
import javax.inject.Inject;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class HistorySummaryFragment extends BaseFragmentWithOfflineSupport implements EventHistorySummaryFragment.NotificationsReceiver {
    protected static final String FILTER_HISTORY_EVENT_DATA = "filter_history_event_data";
    private Context mContext;
    private int mCurrentFilterType;
    private EventData mEventData;
    private EventHistorySummaryFragment mEventHistoryFragment;
    private RelativeLayout mEventListHeader;
    private TextView mEventListHeaderText;
    private CustomGlyphView mFilterGlyph;
    private boolean mIsFiltering;
    private NoHistoryWidget mNoHistoryWidget;
    private final View.OnClickListener mOnFilterButtonPressed = new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.history.HistorySummaryFragment.2
        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            if (CommonUtils.isNetworkAvailable(v.getContext())) {
                HistorySummaryFragment.this.mIsFiltering = true;
                HistorySummaryFragment.this.startHistoryFilterActivity();
                return;
            }
            HistorySummaryFragment.this.setState(1235);
        }
    };
    private TrackableScrollView mScrollView;
    @Inject
    RestService mService;
    @Inject
    SettingsProvider mSettingsProvider;
    private TrackerStatsWidget mTrackerStats;

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity().getApplicationContext();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.INTENT_HISTORY_FILTER_TYPE, this.mCurrentFilterType);
        outState.putBoolean(FILTER_HISTORY_EVENT_DATA, this.mIsFiltering);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onStop() {
        super.onStop();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragmentWithOfflineSupport
    @SuppressLint({"resourceAsColor"})
    public View onCreateChildView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.mCurrentFilterType = savedInstanceState.getInt(Constants.INTENT_HISTORY_FILTER_TYPE);
            this.mIsFiltering = savedInstanceState.getBoolean(FILTER_HISTORY_EVENT_DATA);
        } else {
            this.mCurrentFilterType = Constants.FILTER_TYPE_ALL;
            this.mIsFiltering = false;
        }
        View rootView = inflater.inflate(R.layout.history_summary_fragment, container, false);
        this.mScrollView = (TrackableScrollView) ViewUtils.getValidView(rootView, R.id.history_scroller, TrackableScrollView.class);
        setHistoryView();
        this.mNoHistoryWidget = (NoHistoryWidget) ViewUtils.getValidView(rootView, R.id.no_history_widget, NoHistoryWidget.class);
        this.mEventListHeader = (RelativeLayout) ViewUtils.getValidView(rootView, R.id.event_list_header_layout, RelativeLayout.class);
        this.mEventListHeaderText = (TextView) ViewUtils.getValidView(rootView, R.id.event_list_header, TextView.class);
        this.mFilterGlyph = (CustomGlyphView) ViewUtils.getValidView(rootView, R.id.filter_glyph, CustomGlyphView.class);
        this.mEventListHeaderText.setOnClickListener(this.mOnFilterButtonPressed);
        this.mFilterGlyph.setOnClickListener(this.mOnFilterButtonPressed);
        this.mTrackerStats = (TrackerStatsWidget) ViewUtils.getValidView(rootView, R.id.personal_bests_stats, TrackerStatsWidget.class);
        this.mEventData = new EventData();
        return rootView;
    }

    @Override // android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clearHistoryData();
        updateListHeaderAndPersonalBestStats();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        logPageViewForTelemetry();
        setState(1234);
    }

    @Override // com.microsoft.kapp.fragments.EventHistorySummaryFragment.NotificationsReceiver
    public void OnItemsLoaded(EventData eventData) {
        this.mEventData = eventData;
        if (this.mEventHistoryFragment != null) {
            if (this.mEventHistoryFragment.getHistoryItemsSize() == 0) {
                showNoHistoryMessage(this.mCurrentFilterType != 250);
                return;
            }
            updateListHeaderAndPersonalBestStats();
            this.mNoHistoryWidget.setVisibility(8);
            this.mEventListHeader.setVisibility(0);
        }
    }

    @Override // com.microsoft.kapp.fragments.EventHistorySummaryFragment.NotificationsReceiver
    public void OnLoadError() {
        if (this.mEventHistoryFragment != null) {
            setState(1235);
        }
    }

    private void setHistoryView() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        this.mEventHistoryFragment = EventHistorySummaryFragment.newInstance(this.mCurrentFilterType, this.mIsFiltering, this);
        ft.replace(R.id.history_summary_container, this.mEventHistoryFragment);
        ft.commit();
        this.mScrollView.setOnHitBottomListener(new TrackableScrollView.OnHitBottomListener() { // from class: com.microsoft.kapp.fragments.history.HistorySummaryFragment.1
            @Override // com.microsoft.kapp.views.TrackableScrollView.OnHitBottomListener
            public void onHitBottom() {
                HistorySummaryFragment.this.mEventHistoryFragment.fetchMoreEvents();
            }
        });
    }

    private void logPageViewForTelemetry() {
        switch (this.mCurrentFilterType) {
            case Constants.FILTER_TYPE_ALL /* 250 */:
                Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_HISTORY_ALL);
                return;
            case Constants.FILTER_TYPE_BESTS /* 251 */:
                Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_HISTORY_BESTS);
                return;
            case Constants.FILTER_TYPE_SLEEP /* 252 */:
                Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_HISTORY_SLEEP);
                return;
            case Constants.FILTER_TYPE_RUNS /* 253 */:
                Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_HISTORY_RUNS);
                return;
            case Constants.FILTER_TYPE_EXERCISES /* 254 */:
                Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_HISTORY_EXERCISES);
                return;
            case 255:
                Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_HISTORY_GUIDED_WORKOUTS);
                return;
            case 256:
            default:
                return;
            case 257:
                Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_HISTORY_GOLF);
                return;
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int actionCode, Intent data) {
        super.onActivityResult(requestCode, actionCode, data);
        if (data != null) {
            if (requestCode == 107) {
                if (actionCode == -1) {
                    int type = ((Integer) data.getSerializableExtra(Constants.INTENT_HISTORY_FILTER_TYPE)).intValue();
                    this.mCurrentFilterType = type;
                    updateListHeaderText();
                    this.mEventHistoryFragment.downloadHistoryData(this.mCurrentFilterType);
                } else if (actionCode == 5) {
                    updateListHeaderText();
                    this.mEventHistoryFragment.downloadHistoryData(this.mCurrentFilterType);
                }
                this.mIsFiltering = false;
                this.mEventHistoryFragment.setIsFiltering(false);
            }
            if (requestCode == 109 && actionCode == 0) {
                boolean isUserEventUpdate = data.getBooleanExtra(Constants.INTENT_USER_EVENT_UPDATE, false);
                if (isUserEventUpdate) {
                    this.mEventHistoryFragment.downloadHistoryData();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startHistoryFilterActivity() {
        Activity activity = getActivity();
        if (Validate.isActivityAlive(activity)) {
            Intent intent = new Intent(activity, HistoryFilterActivity.class);
            startActivityForResult(intent, Constants.HISTORY_SUMMARY_FRAGMENT_FILTER_LIST_REQUEST);
        }
    }

    private void showNoHistoryMessage(boolean isFilteredHistory) {
        if (isAdded()) {
            String filterType = HistoryUtils.getFilterTypeString(this.mCurrentFilterType, this.mContext);
            if (TextUtils.isEmpty(filterType)) {
                filterType = this.mContext.getResources().getString(R.string.label_history_filter_all);
            }
            if (isFilteredHistory) {
                this.mEventListHeaderText.setText(Formatter.formatFilteredEventListheader(filterType));
                this.mEventListHeader.setVisibility(0);
                this.mTrackerStats.clearStats();
            } else {
                this.mEventListHeader.setVisibility(4);
            }
            this.mNoHistoryWidget.showNoHistoryMessage(isFilteredHistory, filterType.toLowerCase());
        }
    }

    private void updateListHeaderText() {
        String filterType = HistoryUtils.getFilterTypeString(this.mCurrentFilterType, this.mContext);
        this.mEventListHeaderText.setText(Formatter.formatFilteredEventListheader(filterType));
    }

    private void updateListHeaderAndPersonalBestStats() {
        updateListHeaderText();
        this.mTrackerStats.clearStats();
        if (this.mCurrentFilterType == 253) {
            setRunStatlabels();
            updateRunStats();
            setStatValues(this.mEventData.getRunPersonalBestStatValues(), 101);
        } else if (this.mCurrentFilterType == 254 || this.mCurrentFilterType == 255) {
            setExerciseStatlabels();
            updateExerciseStats();
            setStatValues(this.mEventData.getExercisePersonalBestStatValues(), 102);
        } else if (this.mCurrentFilterType == 256) {
            setBikeStatlabels();
            updateBikeStats();
            setStatValues(this.mEventData.getBikePersonalBestStatValues(), 110);
        }
    }

    private void updateRunStats() {
        if (this.mEventData.getPersonalBestEvents() != null) {
            SpannableString spannableString = new SpannableString(getString(R.string.no_value));
            boolean isMetric = this.mSettingsProvider.isDistanceHeightMetric();
            ArrayList<PersonalBest> runBestStatValues = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                runBestStatValues.add(new PersonalBest("", spannableString, false, ""));
            }
            Iterator<UserEvent> it = this.mEventData.getPersonalBestEvents().iterator();
            while (it.hasNext()) {
                UserEvent event = it.next();
                if (event instanceof RunEvent) {
                    RunEvent runEvent = (RunEvent) RunEvent.class.cast(event);
                    String dateText = runEvent.getStartTime() == null ? "" : KAppDateFormatter.formatToMonthDay(runEvent.getStartTime());
                    Iterator i$ = runEvent.getPersonalBests().iterator();
                    while (i$.hasNext()) {
                        String name = i$.next();
                        if (name.equals(Constants.BEST_RUN_DISTANCE_ID)) {
                            runBestStatValues.set(0, new PersonalBest(dateText, runEvent.getMainMetric(getActivity(), isMetric), runEvent.getEventId()));
                        } else if (name.equals(Constants.BEST_RUN_PACE_ID)) {
                            double paceValue = isMetric ? runEvent.getPace() : runEvent.getPace() * 1.60934d;
                            runBestStatValues.set(1, new PersonalBest(dateText, paceValue > Constants.SPLITS_ACCURACY ? new SpannableString(TimeSpan.fromMilliseconds(paceValue).formatTimePrimeNotation(this.mContext)) : spannableString, runEvent.getEventId()));
                        } else if (name.equals(Constants.BEST_RUN_CALORIES_ID)) {
                            runBestStatValues.set(2, new PersonalBest(dateText, Formatter.formatCalories(this.mContext, runEvent.getCaloriesBurned()), runEvent.getEventId()));
                        } else if (name.equals(Constants.BEST_RUN_SPLIT_ID)) {
                            int splitDuration = EventUtils.calculateBestSplit(runEvent.getSequences(), isMetric);
                            runBestStatValues.set(3, new PersonalBest(dateText, splitDuration > 0 ? new SpannableString(TimeSpan.fromSeconds(splitDuration).formatTimePrimeNotation(this.mContext)) : spannableString, runEvent.getEventId()));
                        }
                    }
                }
            }
            this.mEventData.setRunPersonalBestStatValues(runBestStatValues);
        }
    }

    private void updateExerciseStats() {
        if (this.mEventData.getPersonalBestEvents() != null) {
            Spannable notAvailable = new SpannableString(getString(R.string.no_value));
            ArrayList<PersonalBest> exerciseBestStatValues = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                exerciseBestStatValues.add(new PersonalBest("", notAvailable, false, ""));
            }
            Iterator<UserEvent> it = this.mEventData.getPersonalBestEvents().iterator();
            while (it.hasNext()) {
                UserEvent event = it.next();
                if ((event instanceof ExerciseEvent) || (event instanceof GuidedWorkoutEvent)) {
                    String dateText = event.getStartTime() == null ? "" : KAppDateFormatter.formatToMonthDay(event.getStartTime());
                    Iterator i$ = event.getPersonalBests().iterator();
                    while (i$.hasNext()) {
                        String name = i$.next();
                        if (name.equals(Constants.BEST_EXERCISE_DURATION_ID)) {
                            exerciseBestStatValues.set(1, new PersonalBest(dateText, new SpannableString(Formatter.formatDurationSecondsToSpannableColon(event.getDuration()).toString()), event.getEventId()));
                        } else if (name.equals(Constants.BEST_EXERCISE_CALORIES_ID)) {
                            exerciseBestStatValues.set(0, new PersonalBest(dateText, Formatter.formatCalories(this.mContext, event.getCaloriesBurned()), event.getEventId()));
                        }
                    }
                }
            }
            this.mEventData.setExercisePersonalBestStatValues(exerciseBestStatValues);
        }
    }

    private void updateBikeStats() {
        if (this.mEventData.getPersonalBestEvents() != null) {
            SpannableString notAvailable = new SpannableString(getString(R.string.no_value));
            boolean isMetric = this.mSettingsProvider.isDistanceHeightMetric();
            ArrayList<PersonalBest> bikeBestStatValues = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                bikeBestStatValues.add(new PersonalBest("", notAvailable, false, ""));
            }
            Iterator<UserEvent> it = this.mEventData.getPersonalBestEvents().iterator();
            while (it.hasNext()) {
                UserEvent event = it.next();
                if (event instanceof BikeEvent) {
                    BikeEvent bikeEvent = (BikeEvent) BikeEvent.class.cast(event);
                    String dateText = bikeEvent.getStartTime() == null ? "" : KAppDateFormatter.formatToMonthDay(bikeEvent.getStartTime());
                    Iterator i$ = bikeEvent.getPersonalBests().iterator();
                    while (i$.hasNext()) {
                        String name = i$.next();
                        if (name.equals(Constants.BEST_BIKE_DISTANCE_ID)) {
                            bikeBestStatValues.set(0, new PersonalBest(dateText, bikeEvent.getMainMetric(getActivity(), isMetric), bikeEvent.getEventId()));
                        } else if (name.equals(Constants.BEST_BIKE_SPEED_ID)) {
                            int averageSpeed = bikeEvent.getAverageSpeed();
                            bikeBestStatValues.set(1, new PersonalBest(dateText, averageSpeed > 0 ? Formatter.formatSpeedStat(getActivity(), averageSpeed, isMetric) : notAvailable, bikeEvent.getEventId()));
                        } else if (name.equals(Constants.BEST_BIKE_CALORIES_ID)) {
                            bikeBestStatValues.set(2, new PersonalBest(dateText, Formatter.formatCalories(this.mContext, bikeEvent.getCaloriesBurned()), bikeEvent.getEventId()));
                        } else if (name.equals(Constants.BEST_BIKE_GAIN_ID)) {
                            double altitudeValue = isMetric ? bikeEvent.getTotalAltitudeGain() * 0.01d : bikeEvent.getTotalAltitudeGain() * 0.0328084d;
                            bikeBestStatValues.set(3, new PersonalBest(dateText, altitudeValue > Constants.SPLITS_ACCURACY ? Formatter.formatElevation(this.mContext, bikeEvent.getTotalAltitudeGain(), isMetric) : notAvailable, bikeEvent.getEventId()));
                        }
                    }
                }
            }
            this.mEventData.setBikePersonalBestStatValues(bikeBestStatValues);
        }
    }

    private void addSingleTracker(DateTime date, String title, int symbolResourceId, CharSequence value) {
        PersonalBestSingleTrackerStat tracker = new PersonalBestSingleTrackerStat(getActivity(), title, symbolResourceId);
        String dateText = date == null ? "" : KAppDateFormatter.formatToMonthDay(date);
        tracker.setDate(dateText);
        tracker.setValue(value);
        this.mTrackerStats.addStat(tracker);
    }

    private void setRunStatlabels() {
        addSingleTracker(null, getString(R.string.best_run_distance_name), R.string.glyph_distance, "");
        addSingleTracker(null, getString(R.string.best_run_pace_name), R.string.glyph_pace, "");
        addSingleTracker(null, getString(R.string.best_run_calories_name), R.string.glyph_calories, "");
        addSingleTracker(null, getString(R.string.best_run_split_name), R.string.glyph_time, "");
    }

    private void setExerciseStatlabels() {
        addSingleTracker(null, getString(R.string.best_exercise_calories_name), R.string.glyph_calories, "");
        addSingleTracker(null, getString(R.string.best_exercise_duration_name), R.string.glyph_duration, "");
    }

    private void setBikeStatlabels() {
        addSingleTracker(null, getString(R.string.best_bike_distance_name), R.string.glyph_distance, "");
        addSingleTracker(null, getString(R.string.best_bike_pace_name), R.string.glyph_speed, "");
        addSingleTracker(null, getString(R.string.best_bike_calories_name), R.string.glyph_calories, "");
        addSingleTracker(null, getString(R.string.best_bike_gain_name), R.string.glyph_elevation, "");
    }

    private void setStatValues(ArrayList<PersonalBest> statValues, final int eventTypeId) {
        if (statValues != null && eventTypeId != 0) {
            for (int i = 0; i < statValues.size(); i++) {
                PersonalBestSingleTrackerStat stat = (PersonalBestSingleTrackerStat) this.mTrackerStats.getStat(i);
                if (stat != null) {
                    final PersonalBest personalBest = statValues.get(i);
                    Spannable bestValue = personalBest.getPersonalBestValue();
                    stat.setDate(personalBest.getPersonalBestDate());
                    stat.setValue(bestValue);
                    if (personalBest.hasBestValue()) {
                        stat.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.history.HistorySummaryFragment.3
                            @Override // android.view.View.OnClickListener
                            public void onClick(View v) {
                                Intent intent = new Intent(HistorySummaryFragment.this.getActivity(), UserEventDetailsActivity.class);
                                intent.putExtra("eventId", personalBest.getId());
                                intent.putExtra(Constants.KEY_EVENT_TYPE_ID, eventTypeId);
                                intent.putExtra(Constants.EVENT_L2_VIEW, true);
                                HistorySummaryFragment.this.getActivity().startActivity(intent);
                            }
                        });
                    }
                }
            }
        }
    }

    private void clearHistoryData() {
        this.mEventData = new EventData();
        this.mTrackerStats.clearStats();
    }
}

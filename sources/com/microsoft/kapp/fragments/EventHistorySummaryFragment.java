package com.microsoft.kapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.UserEventDetailsActivity;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.EventData;
import com.microsoft.kapp.models.LoadStatus;
import com.microsoft.kapp.models.ScrollLoadStatus;
import com.microsoft.kapp.models.UserEventSummary;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.golf.GolfService;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.HistoryUtils;
import com.microsoft.kapp.utils.MultipleRequestManager;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.LinearListView;
import com.microsoft.kapp.views.ScrollLoadIndicatorView;
import com.microsoft.kapp.views.UserEventSummaryListView;
import com.microsoft.kapp.widgets.Interstitial;
import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.models.CategoryType;
import com.microsoft.krestsdk.models.ExerciseEvent;
import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.GoalType;
import com.microsoft.krestsdk.models.GolfEvent;
import com.microsoft.krestsdk.models.GuidedWorkoutEvent;
import com.microsoft.krestsdk.models.RunEvent;
import com.microsoft.krestsdk.models.SleepEvent;
import com.microsoft.krestsdk.models.UserEvent;
import com.microsoft.krestsdk.services.RestService;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class EventHistorySummaryFragment extends BaseFragment {
    private static final int ERROR = 1235;
    public static final String IS_FILTERING = "IS_FILTERING";
    private static final int LOADED = 1234;
    private static final int LOADING = 1233;
    private static final String TAG = EventHistorySummaryFragment.class.getSimpleName();
    public static final String USER_EVENT_ID = "USER_EVENT_ID";
    private int mCurrentFilterType;
    private int mCurrentState;
    private EventData mEventData;
    @Inject
    GolfService mGolfService;
    private Interstitial mInterstitial;
    private boolean mIsFiltering;
    private UserEventSummaryListView mRecentEventsList;
    private View mRootView;
    private ScrollLoadIndicatorView mScrollLoadIndicator;
    @Inject
    RestService mService;
    @Inject
    SettingsProvider mSettingsProvider;
    private ScrollLoadStatus mTotalLoadStatus;
    private String mUserEventId;
    private WeakReference<NotificationsReceiver> mWeakNotificationsReceiverRef;

    /* loaded from: classes.dex */
    public interface NotificationsReceiver {
        void OnItemsLoaded(EventData eventData);

        void OnLoadError();
    }

    public static EventHistorySummaryFragment newInstance(int filterType, String userEventId, NotificationsReceiver receiver) {
        EventHistorySummaryFragment eventHistoryFragment = new EventHistorySummaryFragment();
        Bundle args = new Bundle();
        eventHistoryFragment.setNotificationsReceiver(receiver);
        args.putInt(Constants.INTENT_HISTORY_FILTER_TYPE, filterType);
        args.putString(USER_EVENT_ID, userEventId);
        eventHistoryFragment.setArguments(args);
        return eventHistoryFragment;
    }

    public static EventHistorySummaryFragment newInstance(int filterType, boolean isFiltering, NotificationsReceiver receiver) {
        EventHistorySummaryFragment eventHistoryFragment = new EventHistorySummaryFragment();
        eventHistoryFragment.setNotificationsReceiver(receiver);
        Bundle args = new Bundle();
        args.putInt(Constants.INTENT_HISTORY_FILTER_TYPE, filterType);
        args.putBoolean(IS_FILTERING, isFiltering);
        eventHistoryFragment.setArguments(args);
        return eventHistoryFragment;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mCurrentFilterType = savedInstanceState == null ? getArguments().getInt(Constants.INTENT_HISTORY_FILTER_TYPE, 0) : savedInstanceState.getInt(Constants.INTENT_HISTORY_FILTER_TYPE);
        this.mIsFiltering = savedInstanceState == null ? getArguments().getBoolean(IS_FILTERING, false) : savedInstanceState.getBoolean(IS_FILTERING);
        this.mUserEventId = savedInstanceState == null ? getArguments().getString(USER_EVENT_ID) : savedInstanceState.getString(USER_EVENT_ID);
        this.mRootView = inflater.inflate(R.layout.event_history_summary_fragment, container, false);
        this.mRecentEventsList = (UserEventSummaryListView) ViewUtils.getValidView(this.mRootView, R.id.recent_events, UserEventSummaryListView.class);
        this.mScrollLoadIndicator = (ScrollLoadIndicatorView) ViewUtils.getValidView(this.mRootView, R.id.events_scroll_load_indicator, ScrollLoadIndicatorView.class);
        this.mRecentEventsList.setOnItemClickListener(new LinearListView.OnItemClickListener() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.1
            @Override // com.microsoft.kapp.views.LinearListView.OnItemClickListener
            public void onItemClick(LinearListView adapterView, View view, int i, long l) {
                UserEventSummary userEventSummary = (UserEventSummary) adapterView.getAdapter().getItem(i);
                if (userEventSummary != null) {
                    EventHistorySummaryFragment.this.navigateToUserEvent(userEventSummary);
                }
            }
        });
        this.mInterstitial = (Interstitial) ViewUtils.getValidView(this.mRootView, R.id.history_summary_interstitial, Interstitial.class);
        this.mEventData = new EventData();
        return this.mRootView;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.INTENT_HISTORY_FILTER_TYPE, this.mCurrentFilterType);
        outState.putBoolean(IS_FILTERING, this.mIsFiltering);
        outState.putString(USER_EVENT_ID, this.mUserEventId);
    }

    @Override // android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!this.mIsFiltering) {
            downloadHistoryData();
        }
    }

    public void setIsFiltering(boolean value) {
        this.mIsFiltering = value;
    }

    public void fetchMoreEvents() {
        if (this.mTotalLoadStatus == ScrollLoadStatus.PARTIAL) {
            setTotalLoadStatus(ScrollLoadStatus.LOADING);
            switch (this.mCurrentFilterType) {
                case Constants.FILTER_TYPE_ALL /* 250 */:
                    fetchRecentUserEvents();
                    return;
                case Constants.FILTER_TYPE_BESTS /* 251 */:
                    setTotalLoadStatus(ScrollLoadStatus.COMPLETE);
                    return;
                case Constants.FILTER_TYPE_SLEEP /* 252 */:
                    fetchRecentSleepEvents();
                    return;
                case Constants.FILTER_TYPE_RUNS /* 253 */:
                    fetchRecentRunEvents();
                    return;
                case Constants.FILTER_TYPE_EXERCISES /* 254 */:
                    fetchRecentExerciseEvents();
                    return;
                case 255:
                    fetchRecentGuidedWorkoutEvents();
                    return;
                case 256:
                    fetchRecentBikeEvents();
                    return;
                case 257:
                    fetchRecentGolfEvents();
                    return;
                default:
                    fetchRecentUserEvents();
                    return;
            }
        }
    }

    private void setNotificationsReceiver(NotificationsReceiver receiver) {
        this.mWeakNotificationsReceiverRef = new WeakReference<>(receiver);
    }

    public void downloadHistoryData() {
        clearHistoryData();
        MultipleRequestManager multipleRequestManager = new MultipleRequestManager(2, new MultipleRequestManager.OnRequestCompleteListener() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.2
            @Override // com.microsoft.kapp.utils.MultipleRequestManager.OnRequestCompleteListener
            public void requestComplete(LoadStatus status) {
                if (status == LoadStatus.LOADED) {
                    EventHistorySummaryFragment.this.setState(EventHistorySummaryFragment.LOADED);
                }
                if (EventHistorySummaryFragment.this.mEventData.getPersonalGoals() != null) {
                    EventHistorySummaryFragment.this.fetchPersonalBestEventsFromIDs(EventHistorySummaryFragment.this.mEventData.getPersonalGoals());
                } else if (EventHistorySummaryFragment.this.mEventData.getEvents() != null) {
                    EventHistorySummaryFragment.this.fetchInitialEventsCallback();
                }
            }
        });
        setState(LOADING);
        fetchPersonalBests(multipleRequestManager);
        fetchInitialRecentEvents(multipleRequestManager);
    }

    public void downloadHistoryData(int filterType) {
        this.mCurrentFilterType = filterType;
        downloadHistoryData();
    }

    public int getHistoryItemsSize() {
        return this.mRecentEventsList.getCount();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void navigateToUserEvent(UserEventSummary userEventSummary) {
        int eventTypeId = HistoryUtils.getIdForType(userEventSummary.getUserEvent());
        if (eventTypeId >= 0) {
            Intent intent = new Intent(getActivity(), UserEventDetailsActivity.class);
            intent.putExtra("eventId", userEventSummary.getEventId());
            intent.putExtra(Constants.KEY_EVENT_TYPE_ID, eventTypeId);
            intent.putExtra(Constants.EVENT_L2_VIEW, true);
            intent.putStringArrayListExtra(Constants.KEY_PERSONALBEST, userEventSummary.getPersonalBests());
            getActivity().startActivityForResult(intent, Constants.USER_EVENT_DETAILS_UPDATE_REQUEST);
        }
    }

    private void fetchInitialRecentEvents(MultipleRequestManager multipleRequestManager) {
        switch (this.mCurrentFilterType) {
            case Constants.FILTER_TYPE_ALL /* 250 */:
                fetchTopUserEvents(multipleRequestManager);
                return;
            case Constants.FILTER_TYPE_BESTS /* 251 */:
            default:
                fetchTopUserEvents(multipleRequestManager);
                return;
            case Constants.FILTER_TYPE_SLEEP /* 252 */:
                fetchTopSleepEvents(multipleRequestManager);
                return;
            case Constants.FILTER_TYPE_RUNS /* 253 */:
                fetchTopRunEvents(multipleRequestManager);
                return;
            case Constants.FILTER_TYPE_EXERCISES /* 254 */:
                fetchTopExerciseEvents(multipleRequestManager);
                return;
            case 255:
                fetchTopGuidedWorkoutEvents(multipleRequestManager);
                return;
            case 256:
                fetchTopBikeEvents(multipleRequestManager);
                return;
            case 257:
                fetchTopGolfEvents(multipleRequestManager);
                return;
        }
    }

    private void fetchPersonalBests(MultipleRequestManager multipleRequestManager) {
        switch (this.mCurrentFilterType) {
            case Constants.FILTER_TYPE_ALL /* 250 */:
                fetchBestEvents(GoalType.UNKNOWN, multipleRequestManager);
                return;
            case Constants.FILTER_TYPE_BESTS /* 251 */:
            default:
                fetchBestEvents(GoalType.UNKNOWN, multipleRequestManager);
                return;
            case Constants.FILTER_TYPE_SLEEP /* 252 */:
                multipleRequestManager.notifyRequestSucceeded();
                return;
            case Constants.FILTER_TYPE_RUNS /* 253 */:
                fetchBestEvents(GoalType.RUN, multipleRequestManager);
                return;
            case Constants.FILTER_TYPE_EXERCISES /* 254 */:
                fetchBestEvents(GoalType.EXERCISE, multipleRequestManager);
                return;
            case 255:
                fetchBestEvents(GoalType.EXERCISE, multipleRequestManager);
                return;
            case 256:
                fetchBestEvents(GoalType.BIKE, multipleRequestManager);
                return;
        }
    }

    private void fetchBestEvents(GoalType type, final MultipleRequestManager multipleRequestManager) {
        this.mService.getActiveGoalByType(new ActivityScopedCallback(this, new Callback<List<GoalDto>>() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.3
            @Override // com.microsoft.kapp.Callback
            public void callback(List<GoalDto> result) {
                if (result == null) {
                    EventHistorySummaryFragment.this.mEventData.setPersonalGoals(null);
                    multipleRequestManager.notifyRequestSucceeded();
                    return;
                }
                EventHistorySummaryFragment.this.mEventData.setPersonalGoals(result);
                multipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                EventHistorySummaryFragment.this.mEventData.setPersonalGoals(null);
                EventHistorySummaryFragment.this.mEventData.getPersonalBestEvents().clear();
                KLog.e(EventHistorySummaryFragment.TAG, "error loading personal bests.", ex);
                multipleRequestManager.notifyRequestFailed();
                EventHistorySummaryFragment.this.showLoadingDataError();
            }
        }), true, CategoryType.BESTS, type);
    }

    private void fetchTopGuidedWorkoutEvents(final MultipleRequestManager multipleRequestManager) {
        this.mTotalLoadStatus = ScrollLoadStatus.LOADING;
        this.mService.getTopGuidedWorkoutEvents(10, false, new ActivityScopedCallback(this, new Callback<List<GuidedWorkoutEvent>>() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.4
            @Override // com.microsoft.kapp.Callback
            public void callback(List<GuidedWorkoutEvent> result) {
                if (result == null) {
                    EventHistorySummaryFragment.this.mEventData.setEvents(null);
                    EventHistorySummaryFragment.this.showLoadingDataError();
                    return;
                }
                EventHistorySummaryFragment.this.mEventData.setEvents(result);
                multipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                EventHistorySummaryFragment.this.mEventData.setEvents(null);
                EventHistorySummaryFragment.this.showLoadingDataError();
                KLog.e(EventHistorySummaryFragment.TAG, "error loading top guided workouts.", ex);
                multipleRequestManager.notifyRequestFailed();
            }
        }));
    }

    private void fetchRecentGuidedWorkoutEvents() {
        this.mService.getRecentGuidedWorkoutEvents(HistoryUtils.getLastEventStartTime(this.mRecentEventsList), 10, false, new ActivityScopedCallback(this, new Callback<List<GuidedWorkoutEvent>>() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.5
            @Override // com.microsoft.kapp.Callback
            public void callback(List<GuidedWorkoutEvent> result) {
                if (result == null) {
                    EventHistorySummaryFragment.this.mEventData.setEvents(null);
                    EventHistorySummaryFragment.this.showLoadingDataError();
                    return;
                }
                EventHistorySummaryFragment.this.mEventData.setEvents(result);
                EventHistorySummaryFragment.this.fetchMoreEventsCallback();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                EventHistorySummaryFragment.this.mEventData.setEvents(null);
                EventHistorySummaryFragment.this.showLoadingDataError();
                KLog.e(EventHistorySummaryFragment.TAG, "error loading recent guided workout.", ex);
                EventHistorySummaryFragment.this.setTotalLoadStatus(ScrollLoadStatus.PARTIAL);
            }
        }));
    }

    private void fetchTopGolfEvents(final MultipleRequestManager multipleRequestManager) {
        this.mTotalLoadStatus = ScrollLoadStatus.LOADING;
        this.mGolfService.getTopGolfEvents(10, false, new ActivityScopedCallback(this, new Callback<List<GolfEvent>>() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.6
            @Override // com.microsoft.kapp.Callback
            public void callback(List<GolfEvent> result) {
                if (result == null) {
                    EventHistorySummaryFragment.this.mEventData.setEvents(null);
                    EventHistorySummaryFragment.this.showLoadingDataError();
                    return;
                }
                EventHistorySummaryFragment.this.mEventData.setEvents(result);
                multipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                EventHistorySummaryFragment.this.mEventData.setEvents(null);
                EventHistorySummaryFragment.this.showLoadingDataError();
                KLog.e(EventHistorySummaryFragment.TAG, "Error loading top golf events.", ex);
                multipleRequestManager.notifyRequestFailed();
            }
        }));
    }

    private void fetchTopUserEvents(final MultipleRequestManager multipleRequestManager) {
        this.mTotalLoadStatus = ScrollLoadStatus.LOADING;
        this.mService.getTopUserEvents(10, new ActivityScopedCallback(this, new Callback<List<UserEvent>>() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.7
            @Override // com.microsoft.kapp.Callback
            public void callback(List<UserEvent> result) {
                if (result == null) {
                    EventHistorySummaryFragment.this.mEventData.setEvents(null);
                    EventHistorySummaryFragment.this.showLoadingDataError();
                    return;
                }
                EventHistorySummaryFragment.this.mEventData.setEvents(result);
                multipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                EventHistorySummaryFragment.this.mEventData.setEvents(null);
                EventHistorySummaryFragment.this.showLoadingDataError();
                KLog.e(EventHistorySummaryFragment.TAG, "error loading top events.", ex);
                multipleRequestManager.notifyRequestFailed();
            }
        }));
    }

    private void fetchRecentUserEvents() {
        this.mService.getRecentUserEvents(HistoryUtils.getLastEventStartTime(this.mRecentEventsList), 10, new ActivityScopedCallback(this, new Callback<List<UserEvent>>() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.8
            @Override // com.microsoft.kapp.Callback
            public void callback(List<UserEvent> result) {
                if (result == null) {
                    EventHistorySummaryFragment.this.mEventData.setEvents(null);
                    EventHistorySummaryFragment.this.showLoadingDataError();
                    return;
                }
                EventHistorySummaryFragment.this.mEventData.setEvents(result);
                EventHistorySummaryFragment.this.fetchMoreEventsCallback();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                EventHistorySummaryFragment.this.mEventData.setEvents(null);
                EventHistorySummaryFragment.this.showLoadingDataError();
                KLog.e(EventHistorySummaryFragment.TAG, "error loading recent events.", ex);
                EventHistorySummaryFragment.this.setTotalLoadStatus(ScrollLoadStatus.PARTIAL);
            }
        }));
    }

    private void fetchTopSleepEvents(final MultipleRequestManager multipleRequestManager) {
        this.mTotalLoadStatus = ScrollLoadStatus.LOADING;
        this.mService.getTopSleepEvents(10, false, new ActivityScopedCallback(this, new Callback<List<SleepEvent>>() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.9
            @Override // com.microsoft.kapp.Callback
            public void callback(List<SleepEvent> result) {
                if (result == null) {
                    EventHistorySummaryFragment.this.mEventData.setEvents(null);
                    EventHistorySummaryFragment.this.showLoadingDataError();
                    return;
                }
                EventHistorySummaryFragment.this.mEventData.setEvents(result);
                multipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                EventHistorySummaryFragment.this.mEventData.setEvents(null);
                EventHistorySummaryFragment.this.showLoadingDataError();
                KLog.e(EventHistorySummaryFragment.TAG, "error loading top sleep events.", ex);
                multipleRequestManager.notifyRequestFailed();
            }
        }));
    }

    private void fetchRecentSleepEvents() {
        this.mService.getRecentSleepEvents(HistoryUtils.getLastEventStartTime(this.mRecentEventsList), 10, false, new ActivityScopedCallback(this, new Callback<List<SleepEvent>>() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.10
            @Override // com.microsoft.kapp.Callback
            public void callback(List<SleepEvent> result) {
                if (result == null) {
                    EventHistorySummaryFragment.this.mEventData.setEvents(null);
                    EventHistorySummaryFragment.this.showLoadingDataError();
                    return;
                }
                EventHistorySummaryFragment.this.mEventData.setEvents(result);
                EventHistorySummaryFragment.this.fetchMoreEventsCallback();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                EventHistorySummaryFragment.this.mEventData.setEvents(null);
                EventHistorySummaryFragment.this.showLoadingDataError();
                KLog.e(EventHistorySummaryFragment.TAG, "error loading recent sleep.", ex);
                EventHistorySummaryFragment.this.setTotalLoadStatus(ScrollLoadStatus.PARTIAL);
            }
        }));
    }

    private void fetchRecentGolfEvents() {
        this.mGolfService.getRecentGolfEvents(HistoryUtils.getLastEventStartTime(this.mRecentEventsList), 10, false, new ActivityScopedCallback(this, new Callback<List<GolfEvent>>() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.11
            @Override // com.microsoft.kapp.Callback
            public void callback(List<GolfEvent> result) {
                if (result == null) {
                    EventHistorySummaryFragment.this.mEventData.setEvents(null);
                    EventHistorySummaryFragment.this.showLoadingDataError();
                    return;
                }
                EventHistorySummaryFragment.this.mEventData.setEvents(result);
                EventHistorySummaryFragment.this.fetchMoreEventsCallback();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                EventHistorySummaryFragment.this.mEventData.setEvents(null);
                EventHistorySummaryFragment.this.showLoadingDataError();
                KLog.e(EventHistorySummaryFragment.TAG, "error loading recent golf.", ex);
                EventHistorySummaryFragment.this.setTotalLoadStatus(ScrollLoadStatus.PARTIAL);
            }
        }));
    }

    private void fetchTopRunEvents(final MultipleRequestManager multipleRequestManager) {
        this.mTotalLoadStatus = ScrollLoadStatus.LOADING;
        this.mService.getTopRunEvents(this.mSettingsProvider.isDistanceHeightMetric(), 10, new ActivityScopedCallback(this, new Callback<List<RunEvent>>() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.12
            @Override // com.microsoft.kapp.Callback
            public void callback(List<RunEvent> result) {
                if (result == null) {
                    EventHistorySummaryFragment.this.mEventData.setEvents(null);
                    EventHistorySummaryFragment.this.showLoadingDataError();
                    return;
                }
                EventHistorySummaryFragment.this.mEventData.setEvents(result);
                multipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                EventHistorySummaryFragment.this.mEventData.setEvents(null);
                EventHistorySummaryFragment.this.showLoadingDataError();
                KLog.e(EventHistorySummaryFragment.TAG, "error loading top runs.", ex);
                multipleRequestManager.notifyRequestFailed();
            }
        }));
    }

    private void fetchRecentRunEvents() {
        this.mService.getRecentRunEvents(this.mSettingsProvider.isDistanceHeightMetric(), HistoryUtils.getLastEventStartTime(this.mRecentEventsList), 10, new ActivityScopedCallback(this, new Callback<List<RunEvent>>() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.13
            @Override // com.microsoft.kapp.Callback
            public void callback(List<RunEvent> result) {
                if (result == null) {
                    EventHistorySummaryFragment.this.mEventData.setEvents(null);
                    EventHistorySummaryFragment.this.showLoadingDataError();
                    return;
                }
                EventHistorySummaryFragment.this.mEventData.setEvents(result);
                EventHistorySummaryFragment.this.fetchMoreEventsCallback();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                EventHistorySummaryFragment.this.mEventData.setEvents(null);
                EventHistorySummaryFragment.this.showLoadingDataError();
                KLog.e(EventHistorySummaryFragment.TAG, "error loading recent runs.", ex);
                EventHistorySummaryFragment.this.setTotalLoadStatus(ScrollLoadStatus.PARTIAL);
            }
        }));
    }

    private void fetchTopBikeEvents(final MultipleRequestManager multipleRequestManager) {
        this.mTotalLoadStatus = ScrollLoadStatus.LOADING;
        this.mService.getTopBikeEvents(this.mSettingsProvider.isDistanceHeightMetric(), 10, false, new ActivityScopedCallback(this, new Callback<List<BikeEvent>>() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.14
            @Override // com.microsoft.kapp.Callback
            public void callback(List<BikeEvent> result) {
                if (result == null) {
                    EventHistorySummaryFragment.this.mEventData.setEvents(null);
                    EventHistorySummaryFragment.this.showLoadingDataError();
                    return;
                }
                EventHistorySummaryFragment.this.mEventData.setEvents(result);
                multipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                EventHistorySummaryFragment.this.mEventData.setEvents(null);
                EventHistorySummaryFragment.this.showLoadingDataError();
                KLog.e(EventHistorySummaryFragment.TAG, "error loading top runs.", ex);
                multipleRequestManager.notifyRequestFailed();
            }
        }));
    }

    private void fetchRecentBikeEvents() {
        this.mService.getRecentBikeEvents(this.mSettingsProvider.isDistanceHeightMetric(), HistoryUtils.getLastEventStartTime(this.mRecentEventsList), 10, new ActivityScopedCallback(this, new Callback<List<BikeEvent>>() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.15
            @Override // com.microsoft.kapp.Callback
            public void callback(List<BikeEvent> result) {
                if (result == null) {
                    EventHistorySummaryFragment.this.mEventData.setEvents(null);
                    EventHistorySummaryFragment.this.showLoadingDataError();
                    return;
                }
                EventHistorySummaryFragment.this.mEventData.setEvents(result);
                EventHistorySummaryFragment.this.fetchMoreEventsCallback();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                EventHistorySummaryFragment.this.mEventData.setEvents(null);
                EventHistorySummaryFragment.this.showLoadingDataError();
                KLog.e(EventHistorySummaryFragment.TAG, "error loading recent bike events.", ex);
                EventHistorySummaryFragment.this.setTotalLoadStatus(ScrollLoadStatus.PARTIAL);
            }
        }));
    }

    private void fetchTopExerciseEvents(final MultipleRequestManager multipleRequestManager) {
        this.mTotalLoadStatus = ScrollLoadStatus.LOADING;
        this.mService.getTopExerciseEvents(10, false, new ActivityScopedCallback(this, new Callback<List<ExerciseEvent>>() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.16
            @Override // com.microsoft.kapp.Callback
            public void callback(List<ExerciseEvent> result) {
                if (result == null) {
                    EventHistorySummaryFragment.this.mEventData.setEvents(null);
                    EventHistorySummaryFragment.this.showLoadingDataError();
                    return;
                }
                EventHistorySummaryFragment.this.mEventData.setEvents(result);
                multipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                EventHistorySummaryFragment.this.mEventData.setEvents(null);
                EventHistorySummaryFragment.this.showLoadingDataError();
                KLog.e(EventHistorySummaryFragment.TAG, "error loading top exercises.", ex);
                multipleRequestManager.notifyRequestFailed();
            }
        }));
    }

    private void fetchRecentExerciseEvents() {
        this.mService.getRecentExerciseEvents(HistoryUtils.getLastEventStartTime(this.mRecentEventsList), 10, false, new ActivityScopedCallback(this, new Callback<List<ExerciseEvent>>() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.17
            @Override // com.microsoft.kapp.Callback
            public void callback(List<ExerciseEvent> result) {
                if (result == null) {
                    EventHistorySummaryFragment.this.mEventData.setEvents(null);
                    EventHistorySummaryFragment.this.showLoadingDataError();
                    return;
                }
                EventHistorySummaryFragment.this.mEventData.setEvents(result);
                EventHistorySummaryFragment.this.fetchMoreEventsCallback();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                EventHistorySummaryFragment.this.mEventData.setEvents(null);
                EventHistorySummaryFragment.this.showLoadingDataError();
                KLog.e(EventHistorySummaryFragment.TAG, "error loading recent exercises.", ex);
                EventHistorySummaryFragment.this.setTotalLoadStatus(ScrollLoadStatus.PARTIAL);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchPersonalBestEventsFromIDs(List<GoalDto> result) {
        HashMap<String, ArrayList<String>> validGoalsForExistingEvents = HistoryUtils.filterValidGoals(result);
        if (validGoalsForExistingEvents != null && !validGoalsForExistingEvents.isEmpty()) {
            int numOfRequestsIssued = validGoalsForExistingEvents.size();
            MultipleRequestManager secondMultipleRequestManager = new MultipleRequestManager(numOfRequestsIssued, new MultipleRequestManager.OnRequestCompleteListener() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.18
                @Override // com.microsoft.kapp.utils.MultipleRequestManager.OnRequestCompleteListener
                public void requestComplete(LoadStatus status) {
                    switch (AnonymousClass22.$SwitchMap$com$microsoft$kapp$models$LoadStatus[status.ordinal()]) {
                        case 1:
                            EventHistorySummaryFragment.this.fetchInitialEventsCallback();
                            return;
                        case 2:
                            EventHistorySummaryFragment.this.fetchInitialEventsCallback();
                            return;
                        case 3:
                            EventHistorySummaryFragment.this.fetchInitialEventsCallback();
                            return;
                        default:
                            KLog.e(EventHistorySummaryFragment.TAG, "Illegal value of LoadStatus has been used!");
                            return;
                    }
                }
            });
            for (String eventId : validGoalsForExistingEvents.keySet()) {
                fetchPersonalBestEventByID(validGoalsForExistingEvents.get(eventId), eventId, secondMultipleRequestManager);
            }
            return;
        }
        fetchInitialEventsCallback();
    }

    /* renamed from: com.microsoft.kapp.fragments.EventHistorySummaryFragment$22  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass22 {
        static final /* synthetic */ int[] $SwitchMap$com$microsoft$kapp$models$LoadStatus = new int[LoadStatus.values().length];

        static {
            try {
                $SwitchMap$com$microsoft$kapp$models$LoadStatus[LoadStatus.LOADED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$microsoft$kapp$models$LoadStatus[LoadStatus.NO_DATA.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$microsoft$kapp$models$LoadStatus[LoadStatus.ERROR.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    private void fetchPersonalBestEventByID(final ArrayList<String> goalNames, String eventId, final MultipleRequestManager multipleRequestManager) {
        if (isRunBestEvent(goalNames)) {
            ArrayList<RestService.ExpandType> expandTypes = new ArrayList<>();
            expandTypes.add(RestService.ExpandType.INFO);
            expandTypes.add(RestService.ExpandType.SEQUENCES);
            this.mService.getRunEventById(this.mSettingsProvider.isDistanceHeightMetric(), eventId, expandTypes, new ActivityScopedCallback(this, new Callback<RunEvent>() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.19
                @Override // com.microsoft.kapp.Callback
                public void callback(RunEvent result) {
                    if (result == null) {
                        KLog.e(EventHistorySummaryFragment.TAG, "error loading run event by Id.");
                        EventHistorySummaryFragment.this.showLoadingDataError();
                        return;
                    }
                    EventHistorySummaryFragment.this.insertBests(result, goalNames);
                    multipleRequestManager.notifyRequestSucceeded();
                }

                @Override // com.microsoft.kapp.Callback
                public void onError(Exception ex) {
                    KLog.e(EventHistorySummaryFragment.TAG, "error loading run event by Id.", ex);
                    multipleRequestManager.notifyRequestFailed();
                }
            }));
        } else if (isWorkoutBestEvent(goalNames)) {
            this.mService.getUserEventById(eventId, new ActivityScopedCallback(this, new Callback<UserEvent>() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.20
                @Override // com.microsoft.kapp.Callback
                public void callback(UserEvent result) {
                    if (result == null) {
                        KLog.e(EventHistorySummaryFragment.TAG, "error loading exercise event by Id.");
                        return;
                    }
                    EventHistorySummaryFragment.this.insertBests(result, goalNames);
                    multipleRequestManager.notifyRequestSucceeded();
                }

                @Override // com.microsoft.kapp.Callback
                public void onError(Exception ex) {
                    KLog.e(EventHistorySummaryFragment.TAG, "error loading exercise event by Id.", ex);
                    EventHistorySummaryFragment.this.setTotalLoadStatus(ScrollLoadStatus.PARTIAL);
                    multipleRequestManager.notifyRequestFailed();
                }
            }));
        } else if (isBikeBestEvent(goalNames)) {
            ArrayList<RestService.ExpandType> expandTypes2 = new ArrayList<>();
            expandTypes2.add(RestService.ExpandType.INFO);
            expandTypes2.add(RestService.ExpandType.SEQUENCES);
            this.mService.getBikeEventById(this.mSettingsProvider.isDistanceHeightMetric(), eventId, expandTypes2, new ActivityScopedCallback(this, new Callback<BikeEvent>() { // from class: com.microsoft.kapp.fragments.EventHistorySummaryFragment.21
                @Override // com.microsoft.kapp.Callback
                public void callback(BikeEvent result) {
                    if (result == null) {
                        KLog.e(EventHistorySummaryFragment.TAG, "error loading bike event by Id.");
                        EventHistorySummaryFragment.this.showLoadingDataError();
                        return;
                    }
                    EventHistorySummaryFragment.this.insertBests(result, goalNames);
                    multipleRequestManager.notifyRequestSucceeded();
                }

                @Override // com.microsoft.kapp.Callback
                public void onError(Exception ex) {
                    KLog.e(EventHistorySummaryFragment.TAG, "error loading bike event by Id.", ex);
                    multipleRequestManager.notifyRequestFailed();
                }
            }));
        } else {
            KLog.e(TAG, "Unsupported best event type!  " + goalNames.toString());
            multipleRequestManager.notifyRequestSucceeded();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x000a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean isRunBestEvent(java.util.ArrayList<java.lang.String> r4) {
        /*
            r3 = this;
            java.util.Iterator r1 = r4.iterator()
        L4:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L36
            java.lang.Object r0 = r1.next()
            java.lang.String r0 = (java.lang.String) r0
            java.lang.String r2 = "Furthest run"
            boolean r2 = r0.equals(r2)
            if (r2 != 0) goto L34
            java.lang.String r2 = "Fastest run pace"
            boolean r2 = r0.equals(r2)
            if (r2 != 0) goto L34
            java.lang.String r2 = "Most calories burned run"
            boolean r2 = r0.equals(r2)
            if (r2 != 0) goto L34
            java.lang.String r2 = "Fastest run split"
            boolean r2 = r0.equals(r2)
            if (r2 == 0) goto L4
        L34:
            r2 = 1
        L35:
            return r2
        L36:
            r2 = 0
            goto L35
        */
        throw new UnsupportedOperationException("Method not decompiled: com.microsoft.kapp.fragments.EventHistorySummaryFragment.isRunBestEvent(java.util.ArrayList):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x000a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean isWorkoutBestEvent(java.util.ArrayList<java.lang.String> r4) {
        /*
            r3 = this;
            java.util.Iterator r1 = r4.iterator()
        L4:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L24
            java.lang.Object r0 = r1.next()
            java.lang.String r0 = (java.lang.String) r0
            java.lang.String r2 = "Most calories burned exercise"
            boolean r2 = r0.equals(r2)
            if (r2 != 0) goto L22
            java.lang.String r2 = "Longest duration exercise"
            boolean r2 = r0.equals(r2)
            if (r2 == 0) goto L4
        L22:
            r2 = 1
        L23:
            return r2
        L24:
            r2 = 0
            goto L23
        */
        throw new UnsupportedOperationException("Method not decompiled: com.microsoft.kapp.fragments.EventHistorySummaryFragment.isWorkoutBestEvent(java.util.ArrayList):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x000a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean isBikeBestEvent(java.util.ArrayList<java.lang.String> r4) {
        /*
            r3 = this;
            java.util.Iterator r1 = r4.iterator()
        L4:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L36
            java.lang.Object r0 = r1.next()
            java.lang.String r0 = (java.lang.String) r0
            java.lang.String r2 = "Furthest ride"
            boolean r2 = r0.contains(r2)
            if (r2 != 0) goto L34
            java.lang.String r2 = "Fastest speed ride"
            boolean r2 = r0.contains(r2)
            if (r2 != 0) goto L34
            java.lang.String r2 = "Most calories burned ride"
            boolean r2 = r0.contains(r2)
            if (r2 != 0) goto L34
            java.lang.String r2 = "Largest elevation gain ride"
            boolean r2 = r0.contains(r2)
            if (r2 == 0) goto L4
        L34:
            r2 = 1
        L35:
            return r2
        L36:
            r2 = 0
            goto L35
        */
        throw new UnsupportedOperationException("Method not decompiled: com.microsoft.kapp.fragments.EventHistorySummaryFragment.isBikeBestEvent(java.util.ArrayList):boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void insertBests(UserEvent result, ArrayList<String> goalNames) {
        if (result != null) {
            result.addPersonalBests(goalNames);
            boolean exisitingZEventUpdated = false;
            Iterator i$ = this.mEventData.getPersonalBestEvents().iterator();
            while (true) {
                if (!i$.hasNext()) {
                    break;
                }
                UserEvent event = i$.next();
                if (TextUtils.equals(event.getEventId(), result.getEventId())) {
                    event.addPersonalBests(goalNames);
                    exisitingZEventUpdated = true;
                    break;
                }
            }
            if (!exisitingZEventUpdated) {
                this.mEventData.getPersonalBestEvents().add(result);
            }
        }
    }

    private void clearHistoryData() {
        this.mEventData = new EventData();
        this.mRecentEventsList.setItems(new ArrayList());
        this.mInterstitial.setVisibility(0);
    }

    protected void fetchInitialEventsCallback() {
        if (this.mEventData.getEvents() != null) {
            if (this.mEventData.getEvents().size() < 10) {
                setTotalLoadStatus(ScrollLoadStatus.COMPLETE);
            } else {
                setTotalLoadStatus(ScrollLoadStatus.PARTIAL);
            }
            populateRecentEventsList();
        }
    }

    private void populateRecentEventsList() {
        NotificationsReceiver notificationReceiver = null;
        if (this.mWeakNotificationsReceiverRef != null) {
            NotificationsReceiver notificationReceiver2 = this.mWeakNotificationsReceiverRef.get();
            notificationReceiver = notificationReceiver2;
        }
        if (this.mEventData.getEvents() == null || this.mEventData.getEvents().size() == 0) {
            this.mRecentEventsList.setVisibility(8);
            this.mInterstitial.setVisibility(8);
            if (notificationReceiver != null) {
                notificationReceiver.OnItemsLoaded(this.mEventData);
                return;
            }
            return;
        }
        if (this.mCurrentFilterType == 251) {
            this.mRecentEventsList.setItems(convertPersonalBestListToSummary());
        } else {
            this.mRecentEventsList.setItems(convertEventsListToSummaryList());
        }
        if (notificationReceiver != null) {
            notificationReceiver.OnItemsLoaded(this.mEventData);
        }
        this.mRecentEventsList.setVisibility(0);
        this.mInterstitial.setVisibility(8);
    }

    private List<UserEventSummary> convertPersonalBestListToSummary() {
        ArrayList<UserEventSummary> userEventSummaries = new ArrayList<>();
        if (this.mEventData.getPersonalBestEvents() != null) {
            CommonUtils.sortEventList(this.mEventData.getPersonalBestEvents());
            Iterator i$ = this.mEventData.getPersonalBestEvents().iterator();
            while (i$.hasNext()) {
                UserEvent event = i$.next();
                if (event != null) {
                    String name = event.getName();
                    if (event instanceof GolfEvent) {
                        name = ((GolfEvent) event).getDisplayName();
                    }
                    UserEventSummary userEventSummary = new UserEventSummary(name, HistoryUtils.getUserEventTypeName(event, getActivity()), event.getStartTime(), event, event.getMainMetric(getActivity(), this.mSettingsProvider.isDistanceHeightMetric()));
                    userEventSummary.setPersonalBests(event.getPersonalBests());
                    userEventSummaries.add(userEventSummary);
                }
            }
        }
        return userEventSummaries;
    }

    private List<UserEventSummary> convertEventsListToSummaryList() {
        ArrayList<UserEventSummary> userEventSummaries = new ArrayList<>();
        for (Object object : this.mEventData.getEvents()) {
            UserEvent event = (UserEvent) object;
            if (!TextUtils.equals(event.getEventId(), this.mUserEventId)) {
                UserEventSummary userEventSummary = new UserEventSummary(event.getName(), HistoryUtils.getUserEventTypeName(event, getActivity()), event.getStartTime(), event, event.getMainMetric(getActivity(), this.mSettingsProvider.isDistanceHeightMetric()));
                updatePersonalbests(userEventSummary);
                userEventSummaries.add(userEventSummary);
            }
        }
        return userEventSummaries;
    }

    private void updatePersonalbests(UserEventSummary userEventSummary) {
        if (this.mEventData.getPersonalBestEvents() != null && userEventSummary != null) {
            Iterator i$ = this.mEventData.getPersonalBestEvents().iterator();
            while (i$.hasNext()) {
                UserEvent event = i$.next();
                if (event != null && TextUtils.equals(userEventSummary.getEventId(), event.getEventId())) {
                    userEventSummary.setPersonalBests(event.getPersonalBests());
                }
            }
        }
    }

    protected void fetchMoreEventsCallback() {
        List<UserEventSummary> newSummaries = convertEventsListToSummaryList();
        this.mRecentEventsList.addItems(newSummaries);
        if (newSummaries.size() < 10) {
            setTotalLoadStatus(ScrollLoadStatus.COMPLETE);
        } else {
            setTotalLoadStatus(ScrollLoadStatus.PARTIAL);
        }
        NotificationsReceiver notificationReceiver = null;
        if (this.mWeakNotificationsReceiverRef != null) {
            NotificationsReceiver notificationReceiver2 = this.mWeakNotificationsReceiverRef.get();
            notificationReceiver = notificationReceiver2;
        }
        if (notificationReceiver != null) {
            notificationReceiver.OnItemsLoaded(this.mEventData);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTotalLoadStatus(ScrollLoadStatus status) {
        this.mTotalLoadStatus = status;
        this.mScrollLoadIndicator.setStatus(status);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showLoadingDataError() {
        setState(ERROR);
        Fragment fragment = getTargetFragment();
        if (fragment instanceof BaseFragmentWithOfflineSupport) {
            BaseFragmentWithOfflineSupport offlineFragment = (BaseFragmentWithOfflineSupport) fragment;
            offlineFragment.setState(ERROR);
        } else if (this.mWeakNotificationsReceiverRef != null) {
            NotificationsReceiver notificationReceiver = this.mWeakNotificationsReceiverRef.get();
            notificationReceiver.OnLoadError();
        } else {
            getDialogManager().showNetworkErrorDialog(getActivity());
        }
    }

    protected final void setState(int status) {
        if (isAdded()) {
            switch (status) {
                case LOADING /* 1233 */:
                    this.mRootView.setVisibility(8);
                    this.mCurrentState = LOADING;
                    return;
                case LOADED /* 1234 */:
                    if (this.mCurrentState != ERROR) {
                        this.mRootView.setVisibility(0);
                        this.mCurrentState = LOADED;
                        return;
                    }
                    return;
                case ERROR /* 1235 */:
                    this.mCurrentState = ERROR;
                    return;
                default:
                    return;
            }
        }
    }
}

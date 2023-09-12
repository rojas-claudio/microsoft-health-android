package com.microsoft.kapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.android.gms.location.LocationRequest;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.OnEventModifiedListener;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.BaseFragment;
import com.microsoft.kapp.fragments.MainContentFragment;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.services.golf.GolfService;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.views.CustomGlyphView;
import com.microsoft.kapp.widgets.BikeHomeTile;
import com.microsoft.kapp.widgets.GolfHomeTile;
import com.microsoft.kapp.widgets.GuidedWorkoutHomeTile;
import com.microsoft.kapp.widgets.HomeTile;
import com.microsoft.kapp.widgets.Interstitial;
import com.microsoft.kapp.widgets.RunHomeTile;
import com.microsoft.kapp.widgets.SleepHomeTile;
import com.microsoft.kapp.widgets.WorkoutHomeTile;
import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.models.EventType;
import com.microsoft.krestsdk.models.ExerciseEvent;
import com.microsoft.krestsdk.models.GolfEvent;
import com.microsoft.krestsdk.models.GuidedWorkoutEvent;
import com.microsoft.krestsdk.models.RunEvent;
import com.microsoft.krestsdk.models.SleepEvent;
import com.microsoft.krestsdk.models.UserEvent;
import com.microsoft.krestsdk.services.RestService;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class UserEventDetailsActivity extends BaseFragmentActivityWithOfflineSupport implements OnEventModifiedListener {
    private static final String STATE_USER_EVENT = "user_event";
    private static final String TAG_USER_EVENT_DETAILS_FRAGMENT = "user_event_details_fragment";
    private CustomGlyphView mBackArrow;
    private HomeTile mEventTile;
    @Inject
    GolfService mGolfService;
    private FrameLayout mHeaderContainer;
    private Interstitial mInterstitial;
    @Inject
    RestService mService;
    private boolean mUserEventUpdated = false;
    private boolean mIsL2View = false;

    @Override // com.microsoft.kapp.activities.BaseFragmentActivityWithOfflineSupport
    protected void onCreate(ViewGroup container, Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_event_details);
        Validate.notNull(findViewById(R.id.fragment_container), "fragment_container");
        this.mBackArrow = (CustomGlyphView) ActivityUtils.getAndValidateView(this, R.id.back_arrow, CustomGlyphView.class);
        this.mBackArrow.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.UserEventDetailsActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                UserEventDetailsActivity.this.setResult(0, UserEventDetailsActivity.this.mUserEventUpdated);
                UserEventDetailsActivity.this.finish();
            }
        });
        this.mHeaderContainer = (FrameLayout) findViewById(R.id.header_container);
        this.mInterstitial = (Interstitial) findViewById(R.id.user_event_details_interstitial);
        int eventTypeId = getIntent().getIntExtra(Constants.KEY_EVENT_TYPE_ID, -1);
        this.mIsL2View = getIntent().getBooleanExtra(Constants.EVENT_L2_VIEW, false);
        if (savedInstanceState == null || !savedInstanceState.containsKey(STATE_USER_EVENT)) {
            String eventId = getIntent().getStringExtra("eventId");
            loadForEventTypeId(eventId, eventTypeId);
            return;
        }
        UserEvent userEvent = (UserEvent) savedInstanceState.getParcelable(STATE_USER_EVENT);
        loadForSavedEvent(eventTypeId, userEvent);
    }

    private void loadForEventTypeId(String eventId, int eventTypeId) {
        switch (eventTypeId) {
            case 101:
                loadRun(eventId);
                return;
            case 102:
                loadExercise(eventId);
                return;
            case Constants.SLEEP_FRAGMENT_EVENT_DETAILS_REQUEST /* 103 */:
                loadSleep(eventId);
                return;
            case LocationRequest.PRIORITY_LOW_POWER /* 104 */:
            case 105:
            case 106:
            case Constants.HISTORY_SUMMARY_FRAGMENT_FILTER_LIST_REQUEST /* 107 */:
            case Constants.USER_EVENT_DETAILS_UPDATE_REQUEST /* 109 */:
            default:
                return;
            case Constants.GUIDED_WORKOUT_FRAGMENT_EVENT_DETAILS_REQUEST /* 108 */:
                loadGuidedWorkout(eventId);
                return;
            case 110:
                loadBike(eventId);
                return;
            case Constants.GOLF_FRAGMENT_EVENT_DETAILS_REQUEST /* 111 */:
                loadGolf(eventId);
                return;
        }
    }

    private void loadForSavedEvent(int eventTypeId, UserEvent userEvent) {
        switch (eventTypeId) {
            case 101:
                onRunDataReceived((RunEvent) userEvent);
                return;
            case 102:
                onExerciseDataReceived((ExerciseEvent) userEvent);
                return;
            case Constants.SLEEP_FRAGMENT_EVENT_DETAILS_REQUEST /* 103 */:
                onSleepDataReceived((SleepEvent) userEvent);
                return;
            default:
                return;
        }
    }

    private void loadRun(String eventId) {
        this.mService.getRunEventById(this.mSettingsProvider.isDistanceHeightMetric(), eventId, null, new ActivityScopedCallback(this, new Callback<RunEvent>() { // from class: com.microsoft.kapp.activities.UserEventDetailsActivity.2
            @Override // com.microsoft.kapp.Callback
            public void callback(RunEvent result) {
                if (result == null) {
                    UserEventDetailsActivity.this.showLoadingDataError(R.string.run_details_error_fetch_data);
                } else {
                    UserEventDetailsActivity.this.onRunDataReceived(result);
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                UserEventDetailsActivity.this.showLoadingDataError(R.string.run_details_error_fetch_data);
            }
        }));
    }

    private void loadBike(String eventId) {
        this.mService.getBikeEventById(this.mSettingsProvider.isDistanceHeightMetric(), eventId, null, new ActivityScopedCallback(this, new Callback<BikeEvent>() { // from class: com.microsoft.kapp.activities.UserEventDetailsActivity.3
            @Override // com.microsoft.kapp.Callback
            public void callback(BikeEvent result) {
                if (result == null) {
                    UserEventDetailsActivity.this.showLoadingDataError(R.string.run_details_error_fetch_data);
                } else {
                    UserEventDetailsActivity.this.onBikeDataReceived(result);
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                UserEventDetailsActivity.this.showLoadingDataError(R.string.run_details_error_fetch_data);
            }
        }));
    }

    private void loadSleep(String eventId) {
        this.mService.getSleepEventById(eventId, new ArrayList<>(), new ActivityScopedCallback(this, new Callback<SleepEvent>() { // from class: com.microsoft.kapp.activities.UserEventDetailsActivity.4
            @Override // com.microsoft.kapp.Callback
            public void callback(SleepEvent result) {
                if (result == null) {
                    UserEventDetailsActivity.this.showLoadingDataError(R.string.sleep_details_error_fetch_data);
                } else {
                    UserEventDetailsActivity.this.onSleepDataReceived(result);
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                UserEventDetailsActivity.this.showLoadingDataError(R.string.sleep_details_error_fetch_data);
            }
        }));
    }

    private void loadExercise(String eventId) {
        this.mService.getExerciseEventById(eventId, new ArrayList<>(), new ActivityScopedCallback(this, new Callback<ExerciseEvent>() { // from class: com.microsoft.kapp.activities.UserEventDetailsActivity.5
            @Override // com.microsoft.kapp.Callback
            public void callback(ExerciseEvent result) {
                if (result == null) {
                    UserEventDetailsActivity.this.showLoadingDataError(R.string.exercise_detail_error);
                } else {
                    UserEventDetailsActivity.this.onExerciseDataReceived(result);
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                UserEventDetailsActivity.this.showLoadingDataError(R.string.exercise_detail_error);
            }
        }));
    }

    private void loadGuidedWorkout(String eventId) {
        this.mService.getGuidedWorkoutEventById(eventId, false, (Callback<GuidedWorkoutEvent>) new ActivityScopedCallback(this, new Callback<GuidedWorkoutEvent>() { // from class: com.microsoft.kapp.activities.UserEventDetailsActivity.6
            @Override // com.microsoft.kapp.Callback
            public void callback(GuidedWorkoutEvent result) {
                if (result == null) {
                    UserEventDetailsActivity.this.showLoadingDataError(R.string.exercise_detail_error);
                } else {
                    UserEventDetailsActivity.this.onGuidedWorkoutDataReceived(result);
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                UserEventDetailsActivity.this.showLoadingDataError(R.string.exercise_detail_error);
            }
        }));
    }

    private void loadGolf(String eventId) {
        this.mService.getGolfEventById(eventId, (ArrayList<RestService.ExpandType>) null, new ActivityScopedCallback(this, new Callback<GolfEvent>() { // from class: com.microsoft.kapp.activities.UserEventDetailsActivity.7
            @Override // com.microsoft.kapp.Callback
            public void callback(GolfEvent result) {
                if (result == null) {
                    UserEventDetailsActivity.this.showLoadingDataError(R.string.golf_details_error_fetch_data);
                } else {
                    UserEventDetailsActivity.this.onGolfDataReceived(result);
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                UserEventDetailsActivity.this.showLoadingDataError(R.string.run_details_error_fetch_data);
            }
        }));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onRunDataReceived(RunEvent runEvent) {
        Validate.notNull(runEvent, "runEvent");
        CommonUtils.updatePersonalBests(HomeData.getInstance().getPersonalBests(), runEvent, this);
        RunHomeTile tile = new RunHomeTile(this);
        tile.setData(runEvent);
        int height = getResources().getDimensionPixelSize(R.dimen.history_header_tile_visible_height);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, height);
        tile.setLayoutParams(layoutParams);
        addTileToView(tile);
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = CommonUtils.getRunFragmentData(runEvent);
        showMainContentFragment(runEvent.getEventId(), fragmentsData, tile.getBackgroundColor());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBikeDataReceived(BikeEvent bikeEvent) {
        Validate.notNull(bikeEvent, "bikeEvent");
        CommonUtils.updatePersonalBests(HomeData.getInstance().getPersonalBests(), bikeEvent, this);
        BikeHomeTile tile = new BikeHomeTile(this);
        tile.setData(bikeEvent);
        int height = getResources().getDimensionPixelSize(R.dimen.history_header_tile_visible_height);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, height);
        tile.setLayoutParams(layoutParams);
        addTileToView(tile);
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = CommonUtils.getBikeFragmentData(bikeEvent);
        showMainContentFragment(bikeEvent.getEventId(), fragmentsData, tile.getBackgroundColor());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onGolfDataReceived(GolfEvent golfEvent) {
        Validate.notNull(golfEvent, "golfEvent");
        GolfHomeTile tile = new GolfHomeTile(this);
        tile.setData(golfEvent);
        int height = getResources().getDimensionPixelSize(R.dimen.history_header_tile_visible_height);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, height);
        tile.setLayoutParams(layoutParams);
        addTileToView(tile);
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = CommonUtils.getGolfFragmentData(golfEvent);
        showMainContentFragment(golfEvent.getEventId(), fragmentsData, tile.getBackgroundColor());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSleepDataReceived(SleepEvent sleepEvent) {
        Validate.notNull(sleepEvent, "sleepEvent");
        SleepHomeTile tile = new SleepHomeTile(this);
        tile.setData(sleepEvent);
        int height = getResources().getDimensionPixelSize(R.dimen.history_header_tile_visible_height);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, height);
        tile.setLayoutParams(layoutParams);
        addTileToView(tile);
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = CommonUtils.getSleepFragmentData(sleepEvent);
        showMainContentFragment(sleepEvent.getEventId(), fragmentsData, tile.getBackgroundColor());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onExerciseDataReceived(ExerciseEvent exerciseEvent) {
        Validate.notNull(exerciseEvent, "exerciseEvent");
        CommonUtils.updatePersonalBests(HomeData.getInstance().getPersonalBests(), exerciseEvent, this);
        WorkoutHomeTile tile = new WorkoutHomeTile(this);
        tile.setData(exerciseEvent);
        int height = getResources().getDimensionPixelSize(R.dimen.history_header_tile_visible_height);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, height);
        tile.setLayoutParams(layoutParams);
        addTileToView(tile);
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = CommonUtils.getExerciseFragmentData(exerciseEvent);
        showMainContentFragment(exerciseEvent.getEventId(), fragmentsData, tile.getBackgroundColor());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onGuidedWorkoutDataReceived(GuidedWorkoutEvent guidedWorkoutEvent) {
        Validate.notNull(guidedWorkoutEvent, "exerciseEvent");
        CommonUtils.updatePersonalBests(HomeData.getInstance().getPersonalBests(), guidedWorkoutEvent, this);
        GuidedWorkoutHomeTile tile = new GuidedWorkoutHomeTile(this);
        tile.setData(guidedWorkoutEvent);
        int height = getResources().getDimensionPixelSize(R.dimen.history_header_tile_visible_height);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, height);
        tile.setLayoutParams(layoutParams);
        addTileToView(tile);
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = CommonUtils.getGuidedWorkoutFragmentData(guidedWorkoutEvent);
        showMainContentFragment(guidedWorkoutEvent.getEventId(), fragmentsData, tile.getBackgroundColor());
    }

    private void addTileToView(HomeTile tile) {
        this.mHeaderContainer.removeAllViews();
        this.mHeaderContainer.addView(tile);
        this.mEventTile = tile;
        this.mEventTile.setDividerVisibility(8);
        this.mEventTile.setHistoryBackgroundColor();
        tile.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.UserEventDetailsActivity.8
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                UserEventDetailsActivity.this.setResult(0, UserEventDetailsActivity.this.mUserEventUpdated);
                UserEventDetailsActivity.this.finish();
            }
        });
    }

    private void showMainContentFragment(String eventId, List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData, int backgroundColor) {
        MainContentFragment fragment = new MainContentFragment(eventId, fragmentsData, backgroundColor, this.mIsL2View, true);
        this.mBackArrow.setBackgroundColor(backgroundColor);
        if (isSafeToCommitFragmentTransactions()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, TAG_USER_EVENT_DETAILS_FRAGMENT).commit();
            this.mInterstitial.setVisibility(8);
            setState(1234);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showLoadingDataError(int errorMessage) {
        this.mUserEventUpdated = true;
        setState(1236);
    }

    @Override // com.microsoft.kapp.OnEventModifiedListener
    public void onEventRenamed(EventType eventType, String eventName, String eventID) {
        this.mEventTile.setTileName(eventName);
        this.mUserEventUpdated = true;
        setResult(0, this.mUserEventUpdated);
    }

    @Override // com.microsoft.kapp.OnEventModifiedListener
    public void onEventDeleted(EventType eventType, String eventID) {
        this.mUserEventUpdated = true;
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        setResult(0, this.mUserEventUpdated);
        super.onBackPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setResult(int resultCode, boolean isEventUpdated) {
        Intent i = new Intent();
        i.putExtra(Constants.INTENT_USER_EVENT_UPDATE, isEventUpdated);
        setResult(resultCode, i);
    }
}

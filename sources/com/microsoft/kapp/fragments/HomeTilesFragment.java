package com.microsoft.kapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.microsoft.applicationinsights.contracts.EventData;
import com.microsoft.band.device.CargoStrapp;
import com.microsoft.band.device.StartStrip;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.R;
import com.microsoft.kapp.UserProfileFetcher;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.activities.HomeActivity;
import com.microsoft.kapp.activities.HomeActivityAnimationListener;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.event.SyncCompletedEvent;
import com.microsoft.kapp.event.SyncStartedEvent;
import com.microsoft.kapp.fragments.HomeTileNoDataFragment;
import com.microsoft.kapp.fragments.golf.GolfCourseDetailsFragment;
import com.microsoft.kapp.fragments.golf.GolfLandingPageFragment;
import com.microsoft.kapp.fragments.guidedworkout.BrowseGuidedWorkoutsFragment;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutBroadcastListener;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutBroadcastReceiver;
import com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutNextFragment;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.navigations.FragmentNavigationCommandV1;
import com.microsoft.kapp.navigations.HomeTilesNavigationManager;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.golf.GolfService;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutNotificationHandler;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.tasks.StrappsRetrieveTask;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.GuidedWorkoutUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.views.BlockableScrollView;
import com.microsoft.kapp.widgets.BaseTile;
import com.microsoft.kapp.widgets.BikeHomeTile;
import com.microsoft.kapp.widgets.CaloriesHomeTile;
import com.microsoft.kapp.widgets.GolfHomeTile;
import com.microsoft.kapp.widgets.GuidedWorkoutHomeTile;
import com.microsoft.kapp.widgets.HomeTile;
import com.microsoft.kapp.widgets.ManageStrappsTile;
import com.microsoft.kapp.widgets.MiniHomeTile;
import com.microsoft.kapp.widgets.RunHomeTile;
import com.microsoft.kapp.widgets.SleepHomeTile;
import com.microsoft.kapp.widgets.StepsHomeTile;
import com.microsoft.kapp.widgets.WorkoutHomeTile;
import com.microsoft.krestsdk.auth.MsaAuth;
import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.models.EventType;
import com.microsoft.krestsdk.models.ExerciseEvent;
import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.GoalType;
import com.microsoft.krestsdk.models.GolfEvent;
import com.microsoft.krestsdk.models.GuidedWorkoutEvent;
import com.microsoft.krestsdk.models.RunEvent;
import com.microsoft.krestsdk.models.SleepEvent;
import com.microsoft.krestsdk.models.UserDailySummary;
import com.microsoft.krestsdk.models.UserEvent;
import com.microsoft.krestsdk.services.RestService;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class HomeTilesFragment extends BaseFragment implements GuidedWorkoutBroadcastListener {
    private static final String AMOUNT_CONTENT_SCROLLED = "amount_of_pixels_content_is_scrolled";
    private static final String HOME_TILE_CLASS_NAME = "mHomeTileClassName";
    private static final String IS_HOME_TILE_OPEN = "mIsHomeTileOpen";
    private static final String LIST_HOME_TILE_FRAGMENTS_CLASS_NAMES = "listHomeTileFragmentsClassNames";
    private static final String LIST_INT = "listInt";
    private static final String SIZE_OF_CONTENT_INSIDE_SCROLL = "size_of_the_content_inside_scroll";
    private static final String UUID = "mUUIDs";
    @Inject
    MsaAuth mAuthService;
    private boolean mFirstRun;
    @Inject
    GolfService mGolfService;
    private GuidedWorkoutBroadcastReceiver mGuidedWorkoutBroadcastReceiver;
    @Inject
    GuidedWorkoutService mGuidedWorkoutService;
    private String mHomeTileClassName;
    List<Pair<Integer, Class<? extends BaseFragment>>> mHomeTileFragmentsData;
    private RelativeLayout mHomeTileLayout;
    private LinearLayout mHomeTilesContainer;
    private FrameLayout mHomeTilesFragmentContainer;
    private HomeTilesNavigationManager mHomeTilesNavigationManager;
    private boolean mIsCorrectTiles;
    private boolean mIsHomeTileOpen;
    private boolean mIsRecoveringState;
    private EventData mPageLoadEvent;
    private ProgressBar mProgressBar;
    @Inject
    RestService mRestService;
    private View mRootView;
    private BlockableScrollView mScrollView;
    @Inject
    SettingsProvider mSettingsProvider;
    private List<HomeTile> mTiles;
    private List<UUID> mUUIDs;
    @Inject
    UserProfileFetcher mUserProfileFetcher;
    private int mContentAmountScrolled = 0;
    private int mContentHeight = 0;
    private final Callback<HomeData> mHomeDataFetcherCallback = new ActivityScopedCallback(this, new Callback<HomeData>() { // from class: com.microsoft.kapp.fragments.HomeTilesFragment.1
        @Override // com.microsoft.kapp.Callback
        public void callback(HomeData result) {
            if (HomeTilesFragment.this.mIsCorrectTiles || !HomeTilesFragment.this.mSettingsProvider.getUUIDsOnDevice().isEmpty()) {
                HomeTilesFragment.this.onFetchDataSucceeded(result);
                if (HomeTilesFragment.this.mPageLoadEvent != null) {
                    HomeTilesFragment.this.mPageLoadEvent.getProperties().put("Status", "Success");
                    HomeTilesFragment.this.mPageLoadEvent.getMeasurements().put(TelemetryConstants.TimedEvents.HomePage.Dimensions.NUMBER_OF_TILES, Double.valueOf(HomeTilesFragment.getStrappCount(HomeTilesFragment.this.mTiles)));
                    Telemetry.endTimedEvent(HomeTilesFragment.this.mPageLoadEvent);
                    HomeTilesFragment.this.mPageLoadEvent = null;
                }
            }
        }

        @Override // com.microsoft.kapp.Callback
        public void onError(Exception ex) {
            HomeTilesFragment.this.onFetchDataFailed();
            if (HomeTilesFragment.this.mPageLoadEvent != null) {
                HomeTilesFragment.this.mPageLoadEvent.getProperties().put("Status", "Failure");
                Telemetry.endTimedEvent(HomeTilesFragment.this.mPageLoadEvent);
                HomeTilesFragment.this.mPageLoadEvent = null;
            }
        }
    });

    public static Fragment newInstance() {
        return new HomeTilesFragment();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.mContentAmountScrolled = savedInstanceState.getInt(AMOUNT_CONTENT_SCROLLED);
            this.mContentHeight = savedInstanceState.getInt(SIZE_OF_CONTENT_INSIDE_SCROLL);
            this.mIsRecoveringState = true;
            ArrayList<ParcelUuid> parcelUUIDs = savedInstanceState.getParcelableArrayList(UUID);
            if (parcelUUIDs != null) {
                this.mUUIDs = new ArrayList();
                Iterator i$ = parcelUUIDs.iterator();
                while (i$.hasNext()) {
                    ParcelUuid parcelUUID = i$.next();
                    this.mUUIDs.add(parcelUUID.getUuid());
                }
            }
            this.mHomeTileClassName = savedInstanceState.getString(HOME_TILE_CLASS_NAME);
            this.mIsHomeTileOpen = savedInstanceState.getBoolean(IS_HOME_TILE_OPEN, false);
            ArrayList<Integer> listInt = savedInstanceState.getIntegerArrayList(LIST_INT);
            ArrayList<String> listClassNames = savedInstanceState.getStringArrayList(LIST_HOME_TILE_FRAGMENTS_CLASS_NAMES);
            if (listInt != null && listClassNames != null) {
                int size = listClassNames.size();
                this.mHomeTileFragmentsData = new ArrayList();
                for (int i = 0; i < size; i++) {
                    try {
                        this.mHomeTileFragmentsData.add(new Pair<>(listInt.get(i), Class.forName(listClassNames.get(i))));
                    } catch (ClassNotFoundException e) {
                        KLog.e(this.TAG, "Class Not Found: %s", listClassNames.get(i), e);
                    }
                }
            }
        } else {
            this.mPageLoadEvent = Telemetry.startTimedEvent(TelemetryConstants.TimedEvents.HomePage.EVENT_NAME);
        }
        this.mTimeZoneChangeHandler.addTimeZoneChangeListenerWeakRef(this);
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home_tiles, container, false);
        this.mFirstRun = this.mSettingsProvider.isFirstRun();
        this.mHomeTilesContainer = (LinearLayout) ViewUtils.getValidView(view, R.id.home_tiles_container, LinearLayout.class);
        this.mHomeTilesFragmentContainer = (FrameLayout) ViewUtils.getValidView(view, R.id.home_tiles_fragment_container, FrameLayout.class);
        this.mScrollView = (BlockableScrollView) ViewUtils.getValidView(view, R.id.home_tiles_container_scrollview, BlockableScrollView.class);
        this.mProgressBar = (ProgressBar) ViewUtils.getValidView(view, R.id.home_tiles_loader, ProgressBar.class);
        this.mHomeTileLayout = (RelativeLayout) ViewUtils.getValidView(view, R.id.fragment_home_tiles, RelativeLayout.class);
        this.mTiles = new ArrayList();
        this.mGuidedWorkoutBroadcastReceiver = new GuidedWorkoutBroadcastReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(GuidedWorkoutNotificationHandler.OPERATION_NEXT_CALENDAR_WORKOUT);
        filter.addAction(GuidedWorkoutNotificationHandler.OPERATION_SYNC);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(this.mGuidedWorkoutBroadcastReceiver, filter);
        this.mRootView = view;
        return this.mRootView;
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            loadAsync();
        } else {
            load(HomeData.getInstance());
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.event.SyncStatusListener
    public void onSyncStarted(SyncStartedEvent e) {
        super.onSyncStarted(e);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.event.SyncStatusListener
    public void onSyncCompleted(SyncCompletedEvent e) {
        super.onSyncCompleted(e);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.event.SyncStatusListener
    public void onSyncPreComplete(SyncCompletedEvent e) {
        super.onSyncPreComplete(e);
        fetchHomeData();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.services.timeZone.TimeZoneChangeListner
    public void onTimeZoneChanged() {
        fetchHomeData();
    }

    private void fetchHomeData() {
        HomeData mHomeData = HomeData.getInstance();
        if (mHomeData.isHomeDataFetcherInitialized()) {
            mHomeData.fetchAsync(true);
        } else if (this.mUUIDs != null) {
            fetchDataAsync(this.mUUIDs, true);
        } else {
            loadAsync(true);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.event.SyncStatusListener
    public void onSyncProgress(int progressPercentage) {
        super.onSyncProgress(progressPercentage);
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.event.SyncStatusListener
    public void onSyncTerminated() {
        super.onSyncTerminated();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchDataAsync(List<UUID> uuidList, boolean isSync) {
        HomeData mHomeData = HomeData.getInstance();
        mHomeData.InitializeHomeDataFetcher(uuidList);
        mHomeData.fetchAsync(isSync);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getStrappCount(List<HomeTile> tiles) {
        int count = 0;
        if (tiles != null) {
            for (HomeTile tile : tiles) {
                if (!(tile instanceof ManageStrappsTile)) {
                    count++;
                }
            }
        }
        return count;
    }

    public void refresh(boolean forceRefresh) {
        refresh(HomeData.getInstance(), forceRefresh);
    }

    public void refresh() {
        refresh(false);
    }

    public void refreshName(EventType eventType, String name) {
        updateTileName(HomeData.getInstance(), eventType, name);
    }

    private void updateTileName(HomeData homeData, EventType eventType, String name) {
        Validate.notNull(homeData, "homeData");
        if (this.mTiles != null && this.mHomeTilesNavigationManager != null) {
            for (HomeTile tile : this.mTiles) {
                if ((tile instanceof SleepHomeTile) && eventType == EventType.Sleeping) {
                    tile.setTileName(name);
                    return;
                } else if ((tile instanceof RunHomeTile) && eventType == EventType.Running) {
                    tile.setTileName(name);
                    return;
                } else if ((tile instanceof BikeHomeTile) && eventType == EventType.Biking) {
                    tile.setTileName(name);
                    return;
                } else if ((tile instanceof WorkoutHomeTile) && eventType == EventType.Workout) {
                    tile.setTileName(name);
                    return;
                } else if ((tile instanceof GuidedWorkoutHomeTile) && eventType == EventType.GuidedWorkout) {
                    tile.setTileName(name);
                    return;
                } else if ((tile instanceof GolfHomeTile) && eventType == EventType.Golf) {
                    tile.setTileName(name);
                    return;
                }
            }
        }
    }

    private void refresh(HomeData homeData, boolean force) {
        Validate.notNull(homeData, "homeData");
        if (this.mTiles == null || this.mHomeTilesNavigationManager == null) {
            return;
        }
        if (force || !this.mHomeTilesNavigationManager.isOpen()) {
            updateLastSyncedCourse(homeData);
            for (HomeTile tile : this.mTiles) {
                if (tile instanceof SleepHomeTile) {
                    ((SleepHomeTile) tile).setData(homeData.getSleepEvent());
                    if (homeData.getSleepEvent() == null) {
                        setFtuStateClickListener(tile, HomeTileNoDataFragment.EventName.SLEEP.toString());
                    } else {
                        setTileClickListener(tile, homeData.getSleepEvent().getEventId(), CommonUtils.getSleepFragmentData(homeData.getSleepEvent()), new SleepTileRefreshRunnable(this));
                    }
                } else if (tile instanceof RunHomeTile) {
                    ((RunHomeTile) tile).setData(homeData.getRunEvent());
                    if (homeData.getRunEvent() == null) {
                        setFtuStateClickListener(tile, HomeTileNoDataFragment.EventName.RUN.toString());
                    } else {
                        setTileClickListener(tile, homeData.getRunEvent().getEventId(), CommonUtils.getRunFragmentData(homeData.getRunEvent()), new RunTileRefreshRunnable(this));
                    }
                } else if (tile instanceof BikeHomeTile) {
                    ((BikeHomeTile) tile).setData(homeData.getBikeEvent());
                    if (homeData.getBikeEvent() == null) {
                        setFtuStateClickListener(tile, HomeTileNoDataFragment.EventName.BIKE.toString());
                    } else {
                        setTileClickListener(tile, homeData.getBikeEvent().getEventId(), CommonUtils.getBikeFragmentData(homeData.getBikeEvent()), new BikeTileRefreshRunnable(this));
                    }
                } else if (tile instanceof WorkoutHomeTile) {
                    ((WorkoutHomeTile) tile).setData(homeData.getExerciseEvent());
                    if (homeData.getExerciseEvent() == null) {
                        setFtuStateClickListener(tile, HomeTileNoDataFragment.EventName.WORKOUT.toString());
                    } else {
                        setTileClickListener(tile, homeData.getExerciseEvent().getEventId(), CommonUtils.getExerciseFragmentData(homeData.getExerciseEvent()), new WorkoutTileRefreshRunnable(this));
                    }
                } else if (tile instanceof GuidedWorkoutHomeTile) {
                    ((GuidedWorkoutHomeTile) tile).setData(homeData.getGuidedWorkoutevent());
                    if (homeData.getGuidedWorkoutevent() == null) {
                        setFtuStateClickListener(tile, HomeTileNoDataFragment.EventName.GUIDED_WORKOUT.toString());
                    } else {
                        setTileClickListener(tile, homeData.getGuidedWorkoutevent().getEventId(), CommonUtils.getGuidedWorkoutFragmentData(homeData.getGuidedWorkoutevent()), new GuidedWorkoutTileRefreshRunnable(this));
                    }
                } else if (tile instanceof StepsHomeTile) {
                    GoalDto goal = getGoal(homeData, GoalType.STEP);
                    UserDailySummary userDailySummary = CommonUtils.getUserDailySummary(homeData);
                    ((StepsHomeTile) tile).setData(userDailySummary, goal, this.mFirstRun);
                    if (CommonUtils.getUserDailySummary(homeData) == null) {
                        setFtuStateClickListener(tile, HomeTileNoDataFragment.EventName.NO_ID.toString());
                    } else {
                        setTileClickListener(tile, null, CommonUtils.getStepsFragmentData(), new StepsTileRefreshRunnable(this));
                    }
                } else if (tile instanceof CaloriesHomeTile) {
                    GoalDto goal2 = getGoal(homeData, GoalType.CALORIE);
                    ((CaloriesHomeTile) tile).setData(CommonUtils.getUserDailySummary(homeData), goal2, this.mFirstRun);
                    if (CommonUtils.getUserDailySummary(homeData) == null) {
                        setFtuStateClickListener(tile, HomeTileNoDataFragment.EventName.NO_ID.toString());
                    } else {
                        setTileClickListener(tile, null, CommonUtils.getCaloriesFragmentData(), new CaloriesTileRefreshRunnable(this));
                    }
                } else if (tile instanceof GolfHomeTile) {
                    ((GolfHomeTile) tile).setData(homeData.getGolfEvent());
                    if (homeData.getGolfEvent() == null) {
                        setFtuStateClickListener(tile, HomeTileNoDataFragment.EventName.GOLF.toString());
                    } else {
                        setTileClickListener(tile, homeData.getGolfEvent().getEventId(), CommonUtils.getGolfFragmentData(homeData.getGolfEvent()), new GolfTileRefreshRunnable(this));
                    }
                } else if (tile instanceof MiniHomeTile) {
                    ((MiniHomeTile) tile).refresh();
                } else if (tile instanceof ManageStrappsTile) {
                    setTileClickListener(tile, null, null, new ManageStrappsTileRefreshRunnable(this));
                }
            }
            this.mHomeTilesNavigationManager.refresh(getChildFragmentManager());
        }
    }

    private GoalDto getGoal(HomeData homeData, GoalType goalType) {
        Validate.notNull(homeData, "homeData");
        HashMap<GoalType, GoalDto> goals = homeData.getGoals();
        if (goals == null) {
            KLog.e(this.TAG, "Goals in HomeData cannot be null!");
            return null;
        }
        return goals.get(goalType);
    }

    private void updateLastSyncedCourse(HomeData homeData) {
        if (this.mSettingsProvider.getSyncedGolfCourse() != null && !TextUtils.isEmpty((CharSequence) this.mSettingsProvider.getSyncedGolfCourse().first)) {
            homeData.setGolfSyncedCourse((String) this.mSettingsProvider.getSyncedGolfCourse().first);
        }
    }

    private void load(HomeData homeData) {
        Validate.notNull(homeData, "homeData");
        if (this.mHomeTilesNavigationManager != null) {
            this.mHomeTilesNavigationManager.clear();
        }
        this.mHomeTilesNavigationManager = new HomeTilesNavigationManager(getActivity(), (HomeActivityAnimationListener) HomeActivityAnimationListener.class.cast(getActivity()), this.mHomeTilesContainer, this.mHomeTilesFragmentContainer, this.mHomeTileLayout, this.mScrollView);
        if (!this.mTiles.isEmpty()) {
            this.mTiles.clear();
        }
        UserDailySummary userDailySummary = CommonUtils.getUserDailySummary(homeData);
        updateLastSyncedCourse(homeData);
        addStepsTile(userDailySummary, homeData);
        addCaloriesTile(userDailySummary, homeData);
        if (this.mMultiDeviceManager.hasBand() && Validate.isNotNullNotEmpty(this.mUUIDs)) {
            if (!this.mUUIDs.contains(DefaultStrappUUID.STRAPP_SLEEP)) {
                addSleepTile(homeData.getSleepEvent());
            }
            for (UUID uuid : this.mUUIDs) {
                if (uuid.equals(DefaultStrappUUID.STRAPP_SLEEP)) {
                    addSleepTile(homeData.getSleepEvent());
                } else if (uuid.equals(DefaultStrappUUID.STRAPP_RUN)) {
                    addRunTile(homeData.getRunEvent());
                } else if (uuid.equals(DefaultStrappUUID.STRAPP_BIKE)) {
                    addBikeTile(homeData.getBikeEvent());
                } else if (uuid.equals(DefaultStrappUUID.STRAPP_EXERCISE)) {
                    addWorkoutTile(homeData.getExerciseEvent());
                } else if (uuid.equals(DefaultStrappUUID.STRAPP_GUIDED_WORKOUTS)) {
                    addGuidedWorkoutTile(homeData.getGuidedWorkoutevent());
                    addWorkoutCalendarTile(homeData.getNextGuidedWorkoutStepPlanName(), homeData.getNextGuidedWorkoutStepName(), homeData.isRestDay());
                } else if (uuid.equals(DefaultStrappUUID.STRAPP_GOLF)) {
                    addGolfTile(homeData.getGolfEvent());
                    addGolfMiniTile();
                }
            }
        } else {
            addSleepTile(homeData.getSleepEvent());
            addRunTile(homeData.getRunEvent());
            addBikeTile(homeData.getBikeEvent());
            addWorkoutTile(homeData.getExerciseEvent());
            addGuidedWorkoutTile(homeData.getGuidedWorkoutevent());
            addWorkoutCalendarTile(null, null, false);
            addGolfTile(homeData.getGolfEvent());
            addGolfMiniTile();
        }
        if (this.mMultiDeviceManager.hasBand()) {
            addManageStrappsTile();
        }
        if (this.mIsRecoveringState && this.mIsHomeTileOpen && this.mHomeTileClassName != null && this.mHomeTileFragmentsData != null && this.mHomeTileFragmentsData.size() > 0) {
            this.mIsRecoveringState = false;
            for (HomeTile homeTile : this.mTiles) {
                if (this.mHomeTileClassName.equals(homeTile.getClass().getName())) {
                    String eventId = getEventId(homeTile, homeData);
                    if (!TextUtils.isEmpty(eventId)) {
                        this.mHomeTilesNavigationManager.restoreOpenTileState(homeTile, eventId, this.mHomeTileFragmentsData, getChildFragmentManager(), this.mContentAmountScrolled, this.mContentHeight);
                        return;
                    }
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onFetchDataSucceeded(HomeData homeData) {
        this.mProgressBar.setVisibility(8);
        if (!this.mTiles.isEmpty()) {
            refresh(homeData, false);
        } else {
            load(homeData);
        }
        this.mSettingsProvider.setLastHomeRefreshTime(DateTime.now());
    }

    private void addStepsTile(UserDailySummary userDailySummary, HomeData homeData) {
        GoalDto goal = getGoal(homeData, GoalType.STEP);
        StepsHomeTile tile = new StepsHomeTile(getActivity());
        homeData.addOnGoalsChangedListenerWeafRef(GoalType.STEP, tile);
        tile.setData(userDailySummary, goal, this.mFirstRun);
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = null;
        if (userDailySummary != null) {
            fragmentsData = CommonUtils.getStepsFragmentData();
        }
        addHomeTile(tile, false, null, fragmentsData, new StepsTileRefreshRunnable(this));
    }

    private void addCaloriesTile(UserDailySummary userDailySummary, HomeData homeData) {
        GoalDto goal = getGoal(homeData, GoalType.CALORIE);
        CaloriesHomeTile tile = new CaloriesHomeTile(getActivity());
        homeData.addOnGoalsChangedListenerWeafRef(GoalType.CALORIE, tile);
        tile.setData(userDailySummary, goal, this.mFirstRun);
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = null;
        if (userDailySummary != null) {
            fragmentsData = CommonUtils.getCaloriesFragmentData();
        }
        addHomeTile(tile, false, null, fragmentsData, new CaloriesTileRefreshRunnable(this));
    }

    private void addRunTile(RunEvent runEvent) {
        RunHomeTile tile = new RunHomeTile(getActivity());
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = null;
        tile.setData(runEvent);
        if (runEvent != null) {
            fragmentsData = CommonUtils.getRunFragmentData(runEvent);
        }
        String eventId = runEvent != null ? runEvent.getEventId() : HomeTileNoDataFragment.EventName.RUN.toString();
        addHomeTile(tile, runEvent == null, eventId, fragmentsData, new RunTileRefreshRunnable(this));
    }

    private void addBikeTile(BikeEvent bikeEvent) {
        BikeHomeTile tile = new BikeHomeTile(getActivity());
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = null;
        tile.setData(bikeEvent);
        if (bikeEvent != null) {
            fragmentsData = CommonUtils.getBikeFragmentData(bikeEvent);
        }
        String eventId = bikeEvent != null ? bikeEvent.getEventId() : HomeTileNoDataFragment.EventName.BIKE.toString();
        addHomeTile(tile, bikeEvent == null, eventId, fragmentsData, new BikeTileRefreshRunnable(this));
    }

    private void addSleepTile(SleepEvent sleepEvent) {
        SleepHomeTile sleepTile = new SleepHomeTile(getActivity());
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = null;
        sleepTile.setData(sleepEvent);
        if (sleepEvent != null) {
            fragmentsData = CommonUtils.getSleepFragmentData(sleepEvent);
        }
        String eventId = sleepEvent != null ? sleepEvent.getEventId() : HomeTileNoDataFragment.EventName.SLEEP.toString();
        addHomeTile(sleepTile, sleepEvent == null, eventId, fragmentsData, new SleepTileRefreshRunnable(this));
    }

    private void addWorkoutTile(ExerciseEvent exerciseEvent) {
        WorkoutHomeTile workoutTile = new WorkoutHomeTile(getActivity());
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = null;
        workoutTile.setData(exerciseEvent);
        if (exerciseEvent != null) {
            fragmentsData = CommonUtils.getExerciseFragmentData(exerciseEvent);
        }
        String eventId = exerciseEvent != null ? exerciseEvent.getEventId() : HomeTileNoDataFragment.EventName.WORKOUT.toString();
        addHomeTile(workoutTile, exerciseEvent == null, eventId, fragmentsData, new WorkoutTileRefreshRunnable(this));
    }

    private void addGuidedWorkoutTile(GuidedWorkoutEvent guidedWorkoutEvent) {
        GuidedWorkoutHomeTile guidedWorkoutTile = new GuidedWorkoutHomeTile(getActivity());
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = null;
        guidedWorkoutTile.setData(guidedWorkoutEvent);
        if (guidedWorkoutEvent != null) {
            fragmentsData = CommonUtils.getGuidedWorkoutFragmentData(guidedWorkoutEvent);
        }
        String eventId = guidedWorkoutEvent != null ? guidedWorkoutEvent.getEventId() : HomeTileNoDataFragment.EventName.GUIDED_WORKOUT.toString();
        addHomeTile(guidedWorkoutTile, guidedWorkoutEvent == null, eventId, fragmentsData, new GuidedWorkoutTileRefreshRunnable(this));
    }

    private void addGolfTile(GolfEvent golfEvent) {
        GolfHomeTile golfTile = new GolfHomeTile(getActivity());
        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = null;
        golfTile.setData(golfEvent);
        if (golfEvent != null) {
            fragmentsData = CommonUtils.getGolfFragmentData(golfEvent);
        }
        String eventId = golfEvent != null ? golfEvent.getEventId() : HomeTileNoDataFragment.EventName.GOLF.toString();
        addHomeTile(golfTile, golfEvent == null, eventId, fragmentsData, new GolfTileRefreshRunnable(this));
    }

    private void addGolfMiniTile() {
        MiniHomeTile golfMiniTile = new MiniHomeTile(getActivity());
        golfMiniTile.setTelemetryString(TelemetryConstants.Events.LogHomeTileTap.Dimensions.TILE_NAME_GOLF_MINI);
        setGolfMiniTileText(golfMiniTile);
        golfMiniTile.setRefreshListener(new MiniHomeTile.MiniTileRefreshListener() { // from class: com.microsoft.kapp.fragments.HomeTilesFragment.2
            @Override // com.microsoft.kapp.widgets.MiniHomeTile.MiniTileRefreshListener
            public void onRefresh(MiniHomeTile tile) {
                HomeTilesFragment.this.setGolfMiniTileText(tile);
                tile.setState(HomeTile.TileState.VIEWED);
            }
        });
        Runnable tileClickRunnable = getGolfMiniHomeTileRunnable();
        addHomeTile(golfMiniTile, null, tileClickRunnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setGolfMiniTileText(MiniHomeTile golfMiniTile) {
        if (this.mSettingsProvider.getSyncedGolfCourse().first != null && HomeData.getInstance().getGolfSyncedCourse() != null && !TextUtils.isEmpty(HomeData.getInstance().getGolfSyncedCourse())) {
            golfMiniTile.setText(getResources().getString(R.string.mini_home_tile_golf_course_synched_message));
        } else {
            golfMiniTile.setText(getResources().getString(R.string.mini_home_tile_golf_no_course_synched_message));
        }
    }

    private Runnable getGolfMiniHomeTileRunnable() {
        return new Runnable() { // from class: com.microsoft.kapp.fragments.HomeTilesFragment.3
            @Override // java.lang.Runnable
            public void run() {
                if (HomeData.getInstance().getGolfSyncedCourse() == null) {
                    try {
                        Bundle args = new Bundle();
                        args.putString(BaseFragment.ARG_REFERRER, TelemetryConstants.PageViews.Referrers.GOLF_MINI_HOME_TILE);
                        ((HomeActivity) HomeActivity.class.cast(HomeTilesFragment.this.getActivity())).navigateToFragment(GolfLandingPageFragment.class, args, true, false);
                        return;
                    } catch (Exception ex) {
                        KLog.e(HomeTilesFragment.this.TAG, "Error during class casting", ex);
                        return;
                    }
                }
                try {
                    Bundle args2 = new Bundle();
                    args2.putString(GolfCourseDetailsFragment.COURSE_ID, HomeData.getInstance().getGolfSyncedCourse());
                    args2.putBoolean(GolfCourseDetailsFragment.FROM_SPLIT_TILE, true);
                    args2.putString(BaseFragment.ARG_REFERRER, TelemetryConstants.PageViews.Referrers.GOLF_MINI_HOME_TILE);
                    ((HomeActivity) HomeActivity.class.cast(HomeTilesFragment.this.getActivity())).navigateToFragment(GolfCourseDetailsFragment.class, args2, true, false);
                } catch (Exception ex2) {
                    KLog.e(HomeTilesFragment.this.TAG, "Error during class casting", ex2);
                }
            }
        };
    }

    private void addWorkoutCalendarTile(String lastSyncedWorkoutPlanName, String nextGuidedWorkoutStepName, boolean isRestDay) {
        MiniHomeTile calendarTile = new MiniHomeTile(getActivity());
        calendarTile.setTelemetryString(TelemetryConstants.Events.LogHomeTileTap.Dimensions.TILE_NAME_GW_CALENDAR);
        calendarTile.setRefreshListener(new MiniHomeTile.MiniTileRefreshListener() { // from class: com.microsoft.kapp.fragments.HomeTilesFragment.4
            @Override // com.microsoft.kapp.widgets.MiniHomeTile.MiniTileRefreshListener
            public void onRefresh(MiniHomeTile tile) {
                HomeData homeData = HomeData.getInstance();
                String nextGuidedWorkoutStepNameRefresh = homeData.getNextGuidedWorkoutStepName();
                HomeTilesFragment.this.refreshGuidedWorkoutMiniTile(tile, homeData.getNextGuidedWorkoutStepName(), homeData.isRestDay());
                Runnable onClickRunnable = HomeTilesFragment.this.getWorkoutCalendarHomeTileRunnable(nextGuidedWorkoutStepNameRefresh, homeData.isRestDay());
                HomeTilesFragment.this.setTileClickListener(tile, null, null, onClickRunnable);
                tile.setState(HomeTile.TileState.VIEWED);
            }
        });
        refreshGuidedWorkoutMiniTile(calendarTile, lastSyncedWorkoutPlanName, isRestDay);
        Runnable tileClickRunnable = getWorkoutCalendarHomeTileRunnable(nextGuidedWorkoutStepName, isRestDay);
        addHomeTile(calendarTile, null, tileClickRunnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshGuidedWorkoutMiniTile(MiniHomeTile tile, String nextGuidedWorkoutStepPlanName, boolean isRestDay) {
        if (nextGuidedWorkoutStepPlanName == null) {
            tile.setText(getResources().getString(R.string.mini_home_tile_calendar_no_course_synched_message));
        } else if (isRestDay) {
            tile.setText(getResources().getString(R.string.home_tile_guided_workout_rest_day_message));
        } else {
            tile.setText(getResources().getString(R.string.mini_home_tile_calendar_course_synched_message));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Runnable getWorkoutCalendarHomeTileRunnable(final String nextWorkoutStepName, boolean isRestDay) {
        return new Runnable() { // from class: com.microsoft.kapp.fragments.HomeTilesFragment.5
            @Override // java.lang.Runnable
            public void run() {
                if (nextWorkoutStepName == null) {
                    try {
                        ((HomeActivity) HomeActivity.class.cast(HomeTilesFragment.this.getActivity())).navigateToFragment(BrowseGuidedWorkoutsFragment.class, null, true, false);
                        return;
                    } catch (Exception ex) {
                        KLog.e(HomeTilesFragment.this.TAG, "Error during class casting: %s", ex);
                        return;
                    }
                }
                try {
                    Bundle args = new Bundle();
                    args.putBoolean(Constants.KEY_GW_CALENDAR_TILE, true);
                    args.putBoolean(GolfCourseDetailsFragment.FROM_SPLIT_TILE, true);
                    args.putString(BaseFragment.ARG_REFERRER, TelemetryConstants.PageViews.Referrers.CALENDAR_TILE);
                    args.putParcelable(Constants.KEY_GUIDED_WORKOUT_SCHEDULED_WORKOUT, HomeData.getInstance().getNextGuidedWorkoutStepSchedule());
                    args.putBoolean(Constants.KEY_WORKOUT_HEADER_DETAIL, true);
                    args.putBoolean(Constants.KEY_MODE, true);
                    ((HomeActivity) HomeActivity.class.cast(HomeTilesFragment.this.getActivity())).navigateToFragment(GuidedWorkoutNextFragment.class, args, true, false);
                } catch (Exception ex2) {
                    KLog.e(HomeTilesFragment.this.TAG, "Error during class casting", ex2);
                }
            }
        };
    }

    private void addManageStrappsTile() {
        ManageStrappsTile manageStrappsTile = new ManageStrappsTile(getActivity());
        manageStrappsTile.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.HomeTilesFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (v instanceof BaseTile) {
                    ((FragmentNavigationCommandV1.FragmentNavigationListener) HomeTilesFragment.this.getActivity()).navigateToFragment(ManageTilesFragment.class, null, true, false);
                }
            }
        });
        addHomeTile(manageStrappsTile, new ManageStrappsTileRefreshRunnable(this));
    }

    private void addHomeTile(HomeTile tile, Runnable onClickRunnable) {
        addHomeTile(tile, false, null, null, onClickRunnable);
    }

    @Override // android.support.v4.app.Fragment
    public void onStart() {
        super.onStart();
        startFetch();
        HomeData.getInstance().addFetchListener(this.mHomeDataFetcherCallback);
    }

    private void startFetch() {
        DateTime lastSyncTime = this.mMultiDeviceManager.getLastSyncTime();
        DateTime lastHomeRefreshTime = this.mSettingsProvider.getLastHomeRefreshTime();
        if (lastHomeRefreshTime != null && lastSyncTime != null) {
            if (!CommonUtils.isDateToday(lastHomeRefreshTime) || lastSyncTime.isAfter(lastHomeRefreshTime)) {
                HomeData.getInstance().fetchAsync();
            }
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.HOME);
        refreshTilesIfUuidsChanged();
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onStop() {
        super.onStop();
        HomeData.getInstance().removeFetchListener(this.mHomeDataFetcherCallback);
    }

    private void refreshTilesIfUuidsChanged() {
        if (!this.mUUIDs.equals(this.mSettingsProvider.getUUIDsOnDevice())) {
            this.mUUIDs = this.mSettingsProvider.getUUIDsOnDevice();
            load(HomeData.getInstance());
        }
    }

    private void addHomeTile(HomeTile tile, List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData, Runnable onClickRunnable) {
        addHomeTile(tile, false, null, fragmentsData, onClickRunnable);
    }

    private void addHomeTile(HomeTile tile, boolean shouldUseFtuState, String eventID, List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData, Runnable onClickRunnable) {
        int height;
        tile.setId(View.generateViewId());
        Context context = getActivity();
        if (tile.isMiniTile()) {
            height = context.getResources().getDimensionPixelSize(R.dimen.mini_home_tile_height);
        } else {
            height = context.getResources().getDimensionPixelSize(R.dimen.home_tile_height);
        }
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, height);
        tile.setLayoutParams(layoutParams);
        this.mHomeTilesNavigationManager.addTile(tile);
        if (shouldUseFtuState) {
            setFtuStateClickListener(tile, eventID);
        } else {
            setTileClickListener(tile, eventID, fragmentsData, onClickRunnable);
        }
        this.mTiles.add(tile);
    }

    private View.OnClickListener getEmptyHomeTileClickListener(final String eventID, final Runnable onClickRunnable) {
        return new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.HomeTilesFragment.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if ((HomeData.getInstance().isFetchingComplete() || HomeTilesFragment.this.mHomeTilesNavigationManager.isOpen()) && !HomeTilesFragment.this.mHomeTilesNavigationManager.isTransitioning()) {
                    if (onClickRunnable != null) {
                        onClickRunnable.run();
                    }
                    if (v instanceof HomeTile) {
                        List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData = CommonUtils.getEmptyFragmentsData();
                        HomeTilesFragment.this.mHomeTileFragmentsData = fragmentsData;
                        HomeTile homeTile = (HomeTile) v;
                        HomeTilesFragment.this.mHomeTileClassName = homeTile.getClass().getName();
                        HomeTilesFragment.this.mHomeTilesNavigationManager.toggle(homeTile, eventID, fragmentsData, HomeTilesFragment.this.getChildFragmentManager());
                    }
                }
            }
        };
    }

    private void setFtuStateClickListener(BaseTile tile, String eventID) {
        setTileClickListener(tile, eventID, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTileClickListener(final BaseTile tile, final String eventID, final List<Pair<Integer, Class<? extends BaseFragment>>> fragmentsData, final Runnable onClickRunnable) {
        if (fragmentsData != null || onClickRunnable != null) {
            tile.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.HomeTilesFragment.8
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (tile.hasData() && !HomeTilesFragment.this.mHomeTilesNavigationManager.isTransitioning()) {
                        if (onClickRunnable != null) {
                            onClickRunnable.run();
                        }
                        if (fragmentsData != null && (v instanceof HomeTile)) {
                            HomeTilesFragment.this.mHomeTileFragmentsData = fragmentsData;
                            HomeTile homeTile = (HomeTile) v;
                            HomeTilesFragment.this.mHomeTileClassName = homeTile.getClass().getName();
                            HomeTilesFragment.this.mHomeTilesNavigationManager.toggle(homeTile, eventID, fragmentsData, HomeTilesFragment.this.getChildFragmentManager());
                        }
                    }
                }
            });
        } else if (!TextUtils.isEmpty(eventID)) {
            tile.setOnClickListener(getEmptyHomeTileClickListener(eventID, null));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onFetchDataFailed() {
        this.mProgressBar.setVisibility(8);
        getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.home_error_loading_data_title), Integer.valueOf((int) R.string.home_error_loading_data), DialogPriority.LOW);
        if (this.mTiles.isEmpty()) {
            load(HomeData.getInstance());
        }
    }

    private void loadAsync() {
        loadAsync(false);
    }

    private void loadAsync(boolean isSync) {
        this.mUUIDs = this.mSettingsProvider.getUUIDsOnDevice();
        if (this.mUUIDs != null && this.mUUIDs.size() > 0) {
            this.mIsCorrectTiles = true;
            fetchDataAsync(this.mUUIDs, isSync);
            return;
        }
        new RetrieveTask(this, isSync).execute(new Void[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class RetrieveTask extends StrappsRetrieveTask {
        private boolean mIsSync;

        public RetrieveTask(OnTaskListener onTaskListener) {
            super(HomeTilesFragment.this.mCargoConnection, onTaskListener, HomeTilesFragment.this.mSettingsProvider, null);
        }

        public RetrieveTask(OnTaskListener onTaskListener, boolean isSync) {
            super(HomeTilesFragment.this.mCargoConnection, onTaskListener, HomeTilesFragment.this.mSettingsProvider, null);
            this.mIsSync = isSync;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.tasks.StrappsTask
        public void onExecuteSucceeded(StartStrip result) {
            ArrayList<UUID> uuidSet = new ArrayList<>();
            for (CargoStrapp strapp : result.getAppList()) {
                uuidSet.add(strapp.getStrappData().getAppId());
            }
            HomeTilesFragment.this.mUUIDs = uuidSet;
            HomeTilesFragment.this.mIsCorrectTiles = true;
            HomeTilesFragment.this.fetchDataAsync(HomeTilesFragment.this.mUUIDs, this.mIsSync);
        }

        @Override // com.microsoft.kapp.tasks.StrappsTask
        protected void onExecuteFailed(Exception exception) {
            HomeTilesFragment.this.mIsCorrectTiles = true;
            Log.d(this.TAG, "unable to get UUIDs from type");
            HomeTilesFragment.this.fetchDataAsync(null, this.mIsSync);
        }
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, com.microsoft.kapp.activities.OnBackButtonPressedListener
    public boolean handleBackButton() {
        if (this.mHomeTilesNavigationManager == null || !this.mHomeTilesNavigationManager.isOpen()) {
            return false;
        }
        this.mHomeTilesNavigationManager.close(getChildFragmentManager());
        return true;
    }

    @Override // android.support.v4.app.Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<Fragment> nestedFragmentsList = getChildFragmentManager().getFragments();
        if (nestedFragmentsList != null) {
            for (Fragment nestedFragment : nestedFragmentsList) {
                if (nestedFragment != null) {
                    nestedFragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(this.mGuidedWorkoutBroadcastReceiver);
        this.mTimeZoneChangeHandler.removeTimeZoneChangeListner(this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        View scrollContent;
        super.onSaveInstanceState(outState);
        if (this.mUUIDs != null) {
            List<ParcelUuid> parcelUUIDs = new ArrayList<>();
            for (UUID uuid : this.mUUIDs) {
                parcelUUIDs.add(new ParcelUuid(uuid));
            }
            outState.putParcelableArrayList(UUID, (ArrayList) parcelUUIDs);
        }
        outState.putString(HOME_TILE_CLASS_NAME, this.mHomeTileClassName);
        outState.putBoolean(IS_HOME_TILE_OPEN, this.mHomeTilesNavigationManager != null ? this.mHomeTilesNavigationManager.isOpen() : this.mIsHomeTileOpen);
        if (this.mHomeTileFragmentsData != null) {
            int size = this.mHomeTileFragmentsData.size();
            ArrayList arrayList = new ArrayList(size);
            ArrayList<String> listClassNames = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                arrayList.add(this.mHomeTileFragmentsData.get(i).first);
                listClassNames.add(((Class) this.mHomeTileFragmentsData.get(i).second).getName());
            }
            outState.putIntegerArrayList(LIST_INT, arrayList);
            outState.putStringArrayList(LIST_HOME_TILE_FRAGMENTS_CLASS_NAMES, listClassNames);
        }
        if (this.mScrollView != null && this.mScrollView.getChildCount() > 0 && (scrollContent = this.mScrollView.getChildAt(0)) != null) {
            outState.putInt(SIZE_OF_CONTENT_INSIDE_SCROLL, scrollContent.getHeight());
            outState.putInt(AMOUNT_CONTENT_SCROLLED, this.mScrollView.getScrollY());
        }
    }

    public void refreshTiles() {
        refresh();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class WorkoutTileRefreshRunnable implements Runnable {
        WeakReference<HomeTilesFragment> mWeakReference;

        public WorkoutTileRefreshRunnable(HomeTilesFragment fragment) {
            this.mWeakReference = new WeakReference<>(fragment);
        }

        @Override // java.lang.Runnable
        public void run() {
            HomeTilesFragment fragment = null;
            if (this.mWeakReference != null) {
                HomeTilesFragment fragment2 = this.mWeakReference.get();
                fragment = fragment2;
            }
            if (fragment != null && fragment.isAdded()) {
                fragment.mSettingsProvider.setLastWorkoutClickedTime();
                fragment.refresh();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class GuidedWorkoutTileRefreshRunnable implements Runnable {
        WeakReference<HomeTilesFragment> mWeakReference;

        public GuidedWorkoutTileRefreshRunnable(HomeTilesFragment fragment) {
            this.mWeakReference = new WeakReference<>(fragment);
        }

        @Override // java.lang.Runnable
        public void run() {
            HomeTilesFragment fragment = null;
            if (this.mWeakReference != null) {
                HomeTilesFragment fragment2 = this.mWeakReference.get();
                fragment = fragment2;
            }
            if (fragment != null && fragment.isAdded()) {
                fragment.mSettingsProvider.setLastGuidedWorkoutClickedTime();
                fragment.refresh();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class StepsTileRefreshRunnable implements Runnable {
        WeakReference<HomeTilesFragment> mWeakReference;

        public StepsTileRefreshRunnable(HomeTilesFragment fragment) {
            this.mWeakReference = new WeakReference<>(fragment);
        }

        @Override // java.lang.Runnable
        public void run() {
            HomeTilesFragment fragment = null;
            if (this.mWeakReference != null) {
                HomeTilesFragment fragment2 = this.mWeakReference.get();
                fragment = fragment2;
            }
            if (fragment != null && fragment.isAdded()) {
                fragment.mSettingsProvider.setLastStepsTileClickedTime();
                fragment.refresh();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class CaloriesTileRefreshRunnable implements Runnable {
        WeakReference<HomeTilesFragment> mWeakReference;

        public CaloriesTileRefreshRunnable(HomeTilesFragment fragment) {
            this.mWeakReference = new WeakReference<>(fragment);
        }

        @Override // java.lang.Runnable
        public void run() {
            HomeTilesFragment fragment = null;
            if (this.mWeakReference != null) {
                HomeTilesFragment fragment2 = this.mWeakReference.get();
                fragment = fragment2;
            }
            if (fragment != null && fragment.isAdded()) {
                fragment.mSettingsProvider.setLastCaloriesTileClickedTime();
                fragment.refresh();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class RunTileRefreshRunnable implements Runnable {
        WeakReference<HomeTilesFragment> mWeakReference;

        public RunTileRefreshRunnable(HomeTilesFragment fragment) {
            this.mWeakReference = new WeakReference<>(fragment);
        }

        @Override // java.lang.Runnable
        public void run() {
            HomeTilesFragment fragment = null;
            if (this.mWeakReference != null) {
                HomeTilesFragment fragment2 = this.mWeakReference.get();
                fragment = fragment2;
            }
            if (fragment != null && fragment.isAdded()) {
                fragment.mSettingsProvider.setLastRunClickedTime();
                fragment.refresh();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class BikeTileRefreshRunnable implements Runnable {
        WeakReference<HomeTilesFragment> mWeakReference;

        public BikeTileRefreshRunnable(HomeTilesFragment fragment) {
            this.mWeakReference = new WeakReference<>(fragment);
        }

        @Override // java.lang.Runnable
        public void run() {
            HomeTilesFragment fragment = null;
            if (this.mWeakReference != null) {
                HomeTilesFragment fragment2 = this.mWeakReference.get();
                fragment = fragment2;
            }
            if (fragment != null && fragment.isAdded()) {
                fragment.mSettingsProvider.setLastBikingClickedTime();
                fragment.refresh();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class SleepTileRefreshRunnable implements Runnable {
        WeakReference<HomeTilesFragment> mWeakReference;

        public SleepTileRefreshRunnable(HomeTilesFragment fragment) {
            this.mWeakReference = new WeakReference<>(fragment);
        }

        @Override // java.lang.Runnable
        public void run() {
            HomeTilesFragment fragment = null;
            if (this.mWeakReference != null) {
                HomeTilesFragment fragment2 = this.mWeakReference.get();
                fragment = fragment2;
            }
            if (fragment != null && fragment.isAdded()) {
                fragment.mSettingsProvider.setLastSleepClickedTime();
                fragment.refresh();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ManageStrappsTileRefreshRunnable implements Runnable {
        WeakReference<HomeTilesFragment> mWeakReference;

        public ManageStrappsTileRefreshRunnable(HomeTilesFragment fragment) {
            this.mWeakReference = new WeakReference<>(fragment);
        }

        @Override // java.lang.Runnable
        public void run() {
            HomeTilesFragment fragment;
            if (this.mWeakReference != null && (fragment = this.mWeakReference.get()) != null) {
                ((FragmentNavigationCommandV1.FragmentNavigationListener) fragment.getActivity()).navigateToFragment(ManageTilesFragment.class, null, true, false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class GolfTileRefreshRunnable implements Runnable {
        WeakReference<HomeTilesFragment> mWeakReference;

        public GolfTileRefreshRunnable(HomeTilesFragment fragment) {
            this.mWeakReference = new WeakReference<>(fragment);
        }

        @Override // java.lang.Runnable
        public void run() {
            HomeTilesFragment fragment = null;
            if (this.mWeakReference != null) {
                HomeTilesFragment fragment2 = this.mWeakReference.get();
                fragment = fragment2;
            }
            if (fragment != null && fragment.isAdded()) {
                fragment.mSettingsProvider.setLastGolfTileClickedTime();
                fragment.refresh();
            }
        }
    }

    @Override // com.microsoft.kapp.fragments.guidedworkout.GuidedWorkoutBroadcastListener
    public void onGWBroadcastReceived(Context context, Intent data) {
        if (isAdded() && data != null) {
            String operation = data.getAction();
            int operationStatus = data.getIntExtra(GuidedWorkoutNotificationHandler.KEY_ACTION_STATUS, 2);
            boolean isUserInitiated = data.getBooleanExtra(GuidedWorkoutNotificationHandler.KEY_IS_USER_INITIATED, true);
            if (GuidedWorkoutNotificationHandler.OPERATION_NEXT_CALENDAR_WORKOUT.equals(operation)) {
                if (operationStatus == 1 && this.mTiles != null) {
                    for (HomeTile tile : this.mTiles) {
                        if (tile instanceof MiniHomeTile) {
                            ((MiniHomeTile) tile).refresh();
                        }
                    }
                    refresh();
                }
            } else if (GuidedWorkoutNotificationHandler.OPERATION_SYNC.equals(operation) && isUserInitiated && operationStatus == 2) {
                int errorId = data.getIntExtra("ErrorId", 0);
                getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.app_name), Integer.valueOf(GuidedWorkoutUtils.getWorkoutSyncErrorText(errorId)), DialogPriority.LOW);
            }
        }
    }

    private String getEventId(BaseTile homeTile, HomeData homeData) {
        UserEvent event = null;
        if (homeTile instanceof SleepHomeTile) {
            event = homeData.getSleepEvent();
        } else if (homeTile instanceof RunHomeTile) {
            event = homeData.getRunEvent();
        } else if (homeTile instanceof WorkoutHomeTile) {
            event = homeData.getExerciseEvent();
        } else if (homeTile instanceof GuidedWorkoutHomeTile) {
            event = homeData.getGuidedWorkoutevent();
        } else if (homeTile instanceof GolfHomeTile) {
            event = homeData.getGolfEvent();
        }
        if (event == null) {
            return null;
        }
        String currentEventID = event.getEventId();
        return currentEventID;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment
    protected int getTopMenuDividerVisibility() {
        return 0;
    }
}

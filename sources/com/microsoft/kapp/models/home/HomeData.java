package com.microsoft.kapp.models.home;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseIntArray;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.activities.ObservableGoalsManager;
import com.microsoft.kapp.activities.OnGoalsChangedListener;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.LoadStatus;
import com.microsoft.kapp.models.home.HomeDataFetcher;
import com.microsoft.kapp.parsers.InsightUtils;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.models.CategoryType;
import com.microsoft.krestsdk.models.ExerciseEvent;
import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.GoalType;
import com.microsoft.krestsdk.models.GolfEvent;
import com.microsoft.krestsdk.models.GuidedWorkoutEvent;
import com.microsoft.krestsdk.models.InsightMetadata;
import com.microsoft.krestsdk.models.InsightTracker;
import com.microsoft.krestsdk.models.RaisedInsight;
import com.microsoft.krestsdk.models.RunEvent;
import com.microsoft.krestsdk.models.ScheduledWorkout;
import com.microsoft.krestsdk.models.SleepEvent;
import com.microsoft.krestsdk.models.UserActivity;
import com.microsoft.krestsdk.models.UserDailySummary;
import com.microsoft.krestsdk.services.RestService;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
/* loaded from: classes.dex */
public class HomeData implements ObservableGoalsManager, Parcelable {
    private BikeEvent mBikeEvent;
    @Inject
    Context mContext;
    private ExerciseEvent mExerciseEvent;
    private Map<GoalType, SparseIntArray> mGoalHistory;
    private HashMap<GoalType, GoalDto> mGoals;
    private GolfEvent mGolfEvent;
    private String mGolfSynchedCourseID;
    @Inject
    GuidedWorkoutService mGuidedWorkoutService;
    private HomeDataFetcher mHomeDataFetcher;
    private List<InsightMetadata> mInsightMetadata;
    private boolean mIsFetchingComplete;
    private HomeDataFetcher.FetchResult mLastFinishedResult;
    Map<GoalType, List<WeakReference<OnGoalsChangedListener>>> mOnGoalsChangedListners;
    public List<GoalDto> mPersonalBests;
    private List<RaisedInsight> mRaisedInsights;
    @Inject
    RestService mRestService;
    private RunEvent mRunEvent;
    private SleepEvent mSleepEvent;
    private LocalDate mTargetDate;
    private List<UserActivity> mUserActivities;
    private List<UserDailySummary> mUserDailySummaries;
    private static HomeData singletonHomeDataInstance = new HomeData();
    public static final Parcelable.Creator<HomeData> CREATOR = new Parcelable.Creator<HomeData>() { // from class: com.microsoft.kapp.models.home.HomeData.2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HomeData createFromParcel(Parcel source) {
            return new HomeData(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HomeData[] newArray(int size) {
            return new HomeData[size];
        }
    };
    private final String TAG = getClass().getSimpleName();
    private final List<WeakReference<Callback<HomeData>>> mOnFetchCompleteListeners = new CopyOnWriteArrayList();

    public static synchronized HomeData getInstance() {
        HomeData homeData;
        synchronized (HomeData.class) {
            homeData = singletonHomeDataInstance;
        }
        return homeData;
    }

    static synchronized void setInstance(HomeData homeData) {
        synchronized (HomeData.class) {
            singletonHomeDataInstance = homeData;
        }
    }

    public void addFetchListener(Callback<HomeData> onFetchListener) {
        if (onFetchListener != null) {
            this.mOnFetchCompleteListeners.add(new WeakReference<>(onFetchListener));
            notifyFetchResult(onFetchListener, this.mLastFinishedResult);
        }
    }

    public void removeFetchListener(Callback<HomeData> onFetchListener) {
        if (onFetchListener != null) {
            List<WeakReference<Callback<HomeData>>> tmp = new ArrayList<>();
            for (WeakReference<Callback<HomeData>> weakCallback : this.mOnFetchCompleteListeners) {
                Callback<HomeData> callback = weakCallback.get();
                if (callback == null || callback.equals(onFetchListener)) {
                    tmp.add(weakCallback);
                }
            }
            this.mOnFetchCompleteListeners.removeAll(tmp);
        }
    }

    public void clearFetchListeners() {
        this.mOnFetchCompleteListeners.clear();
    }

    public static synchronized void copyAndReplace(HomeData newInstance) {
        synchronized (HomeData.class) {
            HomeData oldHomeData = getInstance();
            newInstance.mOnFetchCompleteListeners.addAll(oldHomeData.mOnFetchCompleteListeners);
            if (oldHomeData.mGoals != null) {
                newInstance.mGoals = oldHomeData.mGoals;
            }
            setInstance(newInstance);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onFetchFinished(HomeDataFetcher.FetchResult fetchResult) {
        this.mLastFinishedResult = fetchResult;
        for (WeakReference<Callback<HomeData>> weakCallback : this.mOnFetchCompleteListeners) {
            Callback<HomeData> callback = weakCallback.get();
            notifyFetchResult(callback, fetchResult);
        }
        this.mIsFetchingComplete = true;
    }

    private void notifyFetchResult(Callback<HomeData> callback, HomeDataFetcher.FetchResult fetchResult) {
        if (callback != null && fetchResult != null) {
            if (fetchResult.getStatus() == LoadStatus.LOADED) {
                callback.callback(this);
            } else if (fetchResult.getStatus() == LoadStatus.ERROR) {
                callback.onError(fetchResult.getException());
            } else {
                Log.e(this.TAG, "Loadstatus was NO_DATA, which shouldn't happen");
                callback.onError(new IllegalStateException());
            }
        }
    }

    public HomeData() {
        KApplicationGraph.getApplicationGraph().inject(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public HomeData(HomeDataFetcher homeDataFetcher, Map<GoalType, SparseIntArray> goalHistory, Map<GoalType, List<WeakReference<OnGoalsChangedListener>>> onGoalsChangedListners) {
        KApplicationGraph.getApplicationGraph().inject(this);
        this.mHomeDataFetcher = homeDataFetcher;
        this.mGoalHistory = goalHistory;
        this.mOnGoalsChangedListners = onGoalsChangedListners;
    }

    public LocalDate getTargetDate() {
        return this.mTargetDate;
    }

    public void setTargetDate(LocalDate mTargetDate) {
        this.mTargetDate = mTargetDate;
    }

    public List<UserActivity> getUserActivities() {
        return this.mUserActivities;
    }

    public void setUserActivities(List<UserActivity> mUserActivities) {
        this.mUserActivities = mUserActivities;
    }

    public List<UserDailySummary> getUserDailySummaries() {
        return this.mUserDailySummaries;
    }

    public void setUserDailySummaries(List<UserDailySummary> userDailySummaries) {
        this.mUserDailySummaries = userDailySummaries;
    }

    public List<GoalDto> getPersonalBests() {
        return this.mPersonalBests;
    }

    public void setPersonalBests(List<GoalDto> personalBests) {
        this.mPersonalBests = personalBests;
    }

    public HashMap<GoalType, GoalDto> getGoals() {
        return this.mGoals;
    }

    public void setGoals(HashMap<GoalType, GoalDto> goals) {
        this.mGoals = goals;
    }

    public GoalDto getStepsGoal() {
        return this.mGoals.get(GoalType.STEP);
    }

    public GoalDto getCaloriesGoal() {
        return this.mGoals.get(GoalType.CALORIE);
    }

    public RunEvent getRunEvent() {
        CommonUtils.updatePersonalBests(this.mPersonalBests, this.mRunEvent, this.mContext);
        return this.mRunEvent;
    }

    public void setRunEvent(RunEvent runEvent) {
        this.mRunEvent = runEvent;
    }

    public BikeEvent getBikeEvent() {
        CommonUtils.updatePersonalBests(this.mPersonalBests, this.mBikeEvent, this.mContext);
        return this.mBikeEvent;
    }

    public void setBikeEvent(BikeEvent bikeEvent) {
        this.mBikeEvent = bikeEvent;
    }

    public SleepEvent getSleepEvent() {
        return this.mSleepEvent;
    }

    public void setSleepEvent(SleepEvent sleepEvent) {
        this.mSleepEvent = sleepEvent;
    }

    public GolfEvent getGolfEvent() {
        return this.mGolfEvent;
    }

    public void setGolfEvent(GolfEvent golfEvent) {
        this.mGolfEvent = golfEvent;
    }

    public boolean isFetchingComplete() {
        return this.mIsFetchingComplete;
    }

    public void setGolfSyncedCourse(String golfCourseID) {
        this.mGolfSynchedCourseID = golfCourseID;
    }

    public String getGolfSyncedCourse() {
        return this.mGolfSynchedCourseID;
    }

    public ExerciseEvent getExerciseEvent() {
        CommonUtils.updatePersonalBests(this.mPersonalBests, this.mExerciseEvent, this.mContext);
        return this.mExerciseEvent;
    }

    public void setExerciseEvent(ExerciseEvent exerciseEvent) {
        this.mExerciseEvent = exerciseEvent;
    }

    public GuidedWorkoutEvent getGuidedWorkoutevent() {
        GuidedWorkoutEvent lastGuidedWorkout = this.mGuidedWorkoutService.getGuidedWorkoutevent();
        CommonUtils.updatePersonalBests(this.mPersonalBests, lastGuidedWorkout, this.mContext);
        return lastGuidedWorkout;
    }

    public boolean isUserSubscribedToWorkoutPlan() {
        return this.mGuidedWorkoutService.getSubscribedWorkoutPlanId() != null;
    }

    public String getSubscribedWorkoutPlanId() {
        return this.mGuidedWorkoutService.getSubscribedWorkoutPlanId();
    }

    public String getNextGuidedWorkoutStepName() {
        return this.mGuidedWorkoutService.getNextGuidedWorkoutStepName();
    }

    public String getNextGuidedWorkoutStepPlanName() {
        return this.mGuidedWorkoutService.getNextGuidedWorkoutStepPlanName();
    }

    public ScheduledWorkout getNextGuidedWorkoutStepSchedule() {
        return this.mGuidedWorkoutService.getNextGuidedWorkoutStepSchedule();
    }

    public boolean isRestDay() {
        return this.mGuidedWorkoutService.isRestDay();
    }

    public List<RaisedInsight> getRaisedInsights() {
        return this.mRaisedInsights;
    }

    public void setRaisedInsights(List<RaisedInsight> mRaisedInsights) {
        this.mRaisedInsights = mRaisedInsights;
    }

    public List<InsightMetadata> getInsightMetadata() {
        return this.mInsightMetadata;
    }

    public void setInsightMetadata(List<InsightMetadata> mInsightMetadata) {
        this.mInsightMetadata = mInsightMetadata;
    }

    public GoalDto getGoal(GoalType goalType) {
        if (this.mGoals == null) {
            return null;
        }
        return this.mGoals.get(goalType);
    }

    public RaisedInsight getInsight(InsightTracker insightType, int insightGroup) {
        return InsightUtils.getInsight(this.mRaisedInsights, this.mInsightMetadata, insightType, insightGroup);
    }

    private void fetchGoals(final GoalType goalType) {
        CategoryType category = CategoryType.PERSONALGOAL;
        Callback<List<GoalDto>> callBack = new Callback<List<GoalDto>>() { // from class: com.microsoft.kapp.models.home.HomeData.1
            @Override // com.microsoft.kapp.Callback
            public void callback(List<GoalDto> goals) {
                if (goals != null) {
                    if (HomeData.this.mGoals == null) {
                        HomeData.this.mGoals = new HashMap();
                    }
                    for (GoalDto goal : goals) {
                        if (goal != null) {
                            HomeData.this.mGoals.put(goal.getType(), goal);
                        }
                    }
                }
                HomeData.this.notifyDataChange(goalType);
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(HomeData.this.TAG, "HomeData: fetchGoals() failed.", ex);
            }
        };
        this.mRestService.getActiveGoalByType(callBack, false, category, goalType);
    }

    private void updateGoal(GoalType goalType) {
        fetchGoals(goalType);
    }

    @Override // com.microsoft.kapp.activities.ObservableGoalsManager
    public void notifyDataChange(GoalType type) {
        OnGoalsChangedListener observer;
        if (this.mOnGoalsChangedListners != null && this.mOnGoalsChangedListners.get(type) != null) {
            for (WeakReference<OnGoalsChangedListener> observerWeakReference : this.mOnGoalsChangedListners.get(type)) {
                if (observerWeakReference != null && (observer = observerWeakReference.get()) != null && observer.isValid()) {
                    observerWeakReference.get().onGoalsUpdated(type, this);
                }
            }
        }
    }

    @Override // com.microsoft.kapp.activities.ObservableGoalsManager
    public void addOnGoalsChangedListenerWeafRef(GoalType type, OnGoalsChangedListener observer) {
        if (observer != null) {
            if (this.mOnGoalsChangedListners == null) {
                this.mOnGoalsChangedListners = new HashMap();
            }
            List<WeakReference<OnGoalsChangedListener>> listenersList = this.mOnGoalsChangedListners.get(type);
            WeakReference<OnGoalsChangedListener> observerWeakReference = new WeakReference<>(observer);
            if (listenersList == null) {
                listenersList = new ArrayList<>();
                this.mOnGoalsChangedListners.put(type, listenersList);
            }
            Iterator<WeakReference<OnGoalsChangedListener>> it = listenersList.iterator();
            while (it.hasNext()) {
                WeakReference<OnGoalsChangedListener> weakReference = it.next();
                if (weakReference == null || weakReference.get() == null || observer.getClass().getName().equals(weakReference.get().getClass().getName())) {
                    it.remove();
                }
            }
            listenersList.add(observerWeakReference);
        }
    }

    @Override // com.microsoft.kapp.activities.ObservableGoalsManager
    public void deleteOnGoalsChangedListenerWeafRef(GoalType type, OnGoalsChangedListener observer) {
        if (this.mOnGoalsChangedListners != null && this.mOnGoalsChangedListners.get(type) != null) {
            List<WeakReference<OnGoalsChangedListener>> observersList = this.mOnGoalsChangedListners.get(type);
            Iterator<WeakReference<OnGoalsChangedListener>> it = observersList.iterator();
            while (it.hasNext()) {
                WeakReference<OnGoalsChangedListener> observerWeakReference = it.next();
                if (observerWeakReference != null && observerWeakReference.get() == observer) {
                    it.remove();
                }
            }
        }
    }

    public static synchronized void updateObservedData(GoalType type) {
        synchronized (HomeData.class) {
            switch (type) {
                case STEP:
                case CALORIE:
                    getInstance().updateGoal(type);
                    break;
            }
        }
    }

    public Map<GoalType, SparseIntArray> getGoalsHistory() {
        if (this.mGoalHistory == null) {
            this.mGoalHistory = new HashMap();
        }
        return this.mGoalHistory;
    }

    public void InitializeHomeDataFetcher(List<UUID> uuidsOnDevice) {
        this.mHomeDataFetcher = new HomeDataFetcher(uuidsOnDevice, this.mOnGoalsChangedListners, this.mGoalHistory);
    }

    public boolean isInitialized() {
        if (this.mHomeDataFetcher == null) {
            return false;
        }
        return this.mHomeDataFetcher.isHomeDataInitialized();
    }

    public boolean fetchAsync() {
        return fetchAsync(false);
    }

    public boolean fetchAsync(boolean isSync) {
        if (this.mHomeDataFetcher == null) {
            return false;
        }
        this.mLastFinishedResult = null;
        this.mHomeDataFetcher.fetchAsync(isSync);
        this.mIsFetchingComplete = false;
        return true;
    }

    public boolean isHomeDataFetcherInitialized() {
        return this.mHomeDataFetcher != null;
    }

    public HomeData(Parcel source) {
        readFromParcel(source);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        DateTime targetDate = this.mTargetDate.toDateTime(new LocalTime());
        dest.writeLong(targetDate.getMillis());
        dest.writeTypedList(this.mUserActivities);
        dest.writeTypedList(this.mUserDailySummaries);
        dest.writeTypedList(this.mPersonalBests);
        dest.writeParcelable(this.mRunEvent, flags);
        dest.writeParcelable(this.mSleepEvent, flags);
        dest.writeParcelable(this.mExerciseEvent, flags);
        writeGoalsMapAsBundle(this.mGoals, dest);
    }

    public void readFromParcel(Parcel in) {
        DateTime targetDate = new DateTime(in.readLong());
        this.mTargetDate = targetDate.toLocalDate();
        this.mUserActivities = new ArrayList();
        in.readTypedList(this.mUserActivities, UserActivity.CREATOR);
        this.mUserDailySummaries = new ArrayList();
        in.readTypedList(this.mUserDailySummaries, UserDailySummary.CREATOR);
        this.mPersonalBests = new ArrayList();
        in.readTypedList(this.mPersonalBests, GoalDto.CREATOR);
        this.mRunEvent = (RunEvent) in.readParcelable(RunEvent.class.getClassLoader());
        this.mSleepEvent = (SleepEvent) in.readParcelable(SleepEvent.class.getClassLoader());
        this.mExerciseEvent = (ExerciseEvent) in.readParcelable(ExerciseEvent.class.getClassLoader());
        CommonUtils.updatePersonalBests(this.mPersonalBests, this.mRunEvent, this.mContext);
        CommonUtils.updatePersonalBests(this.mPersonalBests, this.mExerciseEvent, this.mContext);
        this.mGoals = readBundleAsGoalsMap(in);
    }

    private static void writeGoalsMapAsBundle(Map<GoalType, GoalDto> map, Parcel dest) {
        if (map == null) {
            dest.writeStringList(null);
            return;
        }
        Set<GoalType> keySet = map.keySet();
        ArrayList<String> keyArrayList = new ArrayList<>();
        Bundle bundle = new Bundle();
        for (GoalType key : keySet) {
            if (key != GoalType.UNKNOWN) {
                String keyString = Integer.toString(key.value());
                bundle.putParcelable(keyString, map.get(key));
                keyArrayList.add(keyString);
            }
        }
        dest.writeStringList(keyArrayList);
        dest.writeBundle(bundle);
    }

    private static HashMap<GoalType, GoalDto> readBundleAsGoalsMap(Parcel in) {
        List<String> keyArrayList = in.createStringArrayList();
        if (keyArrayList == null) {
            return null;
        }
        Bundle bundle = in.readBundle();
        HashMap<GoalType, GoalDto> goalsMap = new HashMap<>();
        for (String keyString : keyArrayList) {
            GoalType goalType = GoalType.valueOf(Integer.parseInt(keyString));
            if (goalType != GoalType.UNKNOWN) {
                GoalDto goalDto = (GoalDto) bundle.getParcelable(keyString);
                goalsMap.put(goalType, goalDto);
            }
        }
        return goalsMap;
    }

    public void deleteLastGuidedWorkoutEventLocaly() {
        this.mGuidedWorkoutService.deleteLastGuidedWorkoutEventLocally();
    }
}

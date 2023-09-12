package com.microsoft.kapp.models.home;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.SparseIntArray;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.activities.OnGoalsChangedListener;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.LoadStatus;
import com.microsoft.kapp.models.home.MultipleDataFetcherManager;
import com.microsoft.kapp.models.strapp.DefaultStrappUUID;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.healthandfitness.HealthAndFitnessService;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.MultipleRequestManager;
import com.microsoft.krestsdk.auth.CredentialsFetcherAsync;
import com.microsoft.krestsdk.auth.KdsFetcher;
import com.microsoft.krestsdk.auth.MsaAuth;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.auth.credentials.KCredential;
import com.microsoft.krestsdk.models.GoalType;
import com.microsoft.krestsdk.services.RestService;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import org.joda.time.LocalDate;
/* loaded from: classes.dex */
public class HomeDataFetcher implements MultipleRequestManager.OnRequestCompleteListener {
    private static final int HOMEDATA_FETCHER_TIMEOUT = 120000;
    private static final String TAG = HomeDataFetcher.class.getSimpleName();
    @Inject
    MsaAuth mAuthService;
    @Inject
    Context mContext;
    @Inject
    CredentialsManager mCredentialsManager;
    private Map<GoalType, SparseIntArray> mGoalHistory;
    @Inject
    HealthAndFitnessService mHealthAndFitnessRestService;
    private HomeData mHomeData;
    private volatile Boolean mIsDataLoading;
    public boolean mIsHomeDataInitilized;
    private volatile boolean mIsSyncFetchingRequest;
    @Inject
    KdsFetcher mKdsFetcher;
    private LastPendingFetchRequest mLastPendingFetchRequest;
    @Inject
    MultiDeviceManager mMultiDeviceManager;
    private MultipleDataFetcherManager mMultipleRequestManager;
    private Map<GoalType, List<WeakReference<OnGoalsChangedListener>>> mOnGoalsChangedListners;
    @Inject
    RestService mRestService;
    @Inject
    SettingsProvider mSettingsProvider;
    private List<UUID> mUUIDsOnDevice;

    public HomeDataFetcher(List<UUID> uuidsOnDevice) {
        this(uuidsOnDevice, new HashMap(), new HashMap());
    }

    public HomeDataFetcher(List<UUID> uuidsOnDevice, Map<GoalType, List<WeakReference<OnGoalsChangedListener>>> onGoalsChangedListners, Map<GoalType, SparseIntArray> goalHistory) {
        this.mIsDataLoading = false;
        KApplicationGraph.getApplicationGraph().inject(this);
        this.mUUIDsOnDevice = uuidsOnDevice;
        this.mOnGoalsChangedListners = onGoalsChangedListners;
        this.mGoalHistory = goalHistory;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class LastPendingFetchRequest {
        private volatile boolean mIsLastPendingFetchRequestSyncRequest;

        public LastPendingFetchRequest(boolean isSync) {
            this.mIsLastPendingFetchRequestSyncRequest = isSync;
        }

        public synchronized void setLastPendingRequestType(boolean isSync) {
            if (!this.mIsLastPendingFetchRequestSyncRequest) {
                this.mIsLastPendingFetchRequestSyncRequest = isSync;
            }
        }

        public synchronized boolean getLastPendingRequestType() {
            return this.mIsLastPendingFetchRequestSyncRequest;
        }
    }

    public void fetchAsync(boolean isSync) {
        synchronized (this.mIsDataLoading) {
            if (this.mIsDataLoading.booleanValue()) {
                if (this.mLastPendingFetchRequest == null) {
                    this.mLastPendingFetchRequest = new LastPendingFetchRequest(isSync);
                } else {
                    this.mLastPendingFetchRequest.setLastPendingRequestType(isSync);
                }
                return;
            }
            this.mIsDataLoading = true;
            this.mIsSyncFetchingRequest = isSync;
            checkTokenAndFetch();
        }
    }

    private void fetchPendingRequest() {
        this.mIsDataLoading = true;
        if (this.mLastPendingFetchRequest == null) {
            KLog.e(TAG, "LastPendingFetchRequest shouldnt be null! please fix the race-condition error in the code..");
            this.mIsDataLoading = false;
            return;
        }
        this.mIsSyncFetchingRequest = this.mLastPendingFetchRequest.getLastPendingRequestType();
        this.mLastPendingFetchRequest = null;
        checkTokenAndFetch();
    }

    private void checkTokenAndFetch() {
        CredentialsFetcherAsync credentialsFetcher = new CredentialsFetcherAsync(this.mCredentialsManager);
        credentialsFetcher.execute(new Callback<KCredential>() { // from class: com.microsoft.kapp.models.home.HomeDataFetcher.1
            @Override // com.microsoft.kapp.Callback
            public void callback(KCredential credentials) {
                if (credentials != null) {
                    HomeDataFetcher.this.triggerDataFetching(HomeDataFetcher.this);
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
            }
        });
    }

    public static void fetchDataToCache(List<UUID> uuidsOnDevice) {
        if (!uuidsOnDevice.isEmpty()) {
            HomeDataFetcher fetcher = new HomeDataFetcher(uuidsOnDevice);
            fetcher.triggerDataFetching(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void triggerDataFetching(MultipleRequestManager.OnRequestCompleteListener listener) {
        if (this.mOnGoalsChangedListners == null) {
            this.mOnGoalsChangedListners = new HashMap();
        }
        this.mHomeData = new HomeData(this, this.mGoalHistory, this.mOnGoalsChangedListners);
        LocalDate targetDate = LocalDate.now();
        this.mHomeData.setTargetDate(targetDate);
        List<DataFetcher> dataFetchers = new ArrayList<>();
        dataFetchers.add(new UserActivitiesDataFetcher(targetDate, this.mRestService, this.mHomeData));
        dataFetchers.add(new DailySummariesDataFetcher(targetDate, this.mRestService, this.mHomeData));
        dataFetchers.add(new GoalsDataFetcher(this.mRestService, this.mHomeData));
        if (this.mMultiDeviceManager.hasEverHadBand()) {
            dataFetchers.add(new PersonalBestDataFetcher(this.mRestService, this.mHomeData));
            dataFetchers.add(new SleepDataFetcher(this.mRestService, this.mHomeData));
            if (this.mUUIDsOnDevice == null || this.mUUIDsOnDevice.isEmpty()) {
                dataFetchers.add(new RunDataFetcher(this.mRestService, this.mSettingsProvider, this.mHomeData));
                dataFetchers.add(new BikeDataFetcher(this.mRestService, this.mHomeData, this.mSettingsProvider));
                dataFetchers.add(new ExerciseDataFetcher(this.mRestService, this.mHomeData));
                dataFetchers.add(new GolfDataFetcher(this.mHomeData, this.mSettingsProvider));
            } else {
                if (this.mUUIDsOnDevice.contains(DefaultStrappUUID.STRAPP_RUN)) {
                    dataFetchers.add(new RunDataFetcher(this.mRestService, this.mSettingsProvider, this.mHomeData));
                }
                if (this.mUUIDsOnDevice.contains(DefaultStrappUUID.STRAPP_BIKE)) {
                    dataFetchers.add(new BikeDataFetcher(this.mRestService, this.mHomeData, this.mSettingsProvider));
                }
                if (this.mUUIDsOnDevice.contains(DefaultStrappUUID.STRAPP_EXERCISE)) {
                    dataFetchers.add(new ExerciseDataFetcher(this.mRestService, this.mHomeData));
                }
                dataFetchers.add(new GolfDataFetcher(this.mHomeData, this.mSettingsProvider));
            }
            dataFetchers.add(new GuidedWorkoutDataFetcher());
        }
        this.mMultipleRequestManager = new MultipleDataFetcherManager.Builder(listener).withFetchers(dataFetchers).withTimeOut(120000L).build();
        this.mMultipleRequestManager.start();
    }

    @Override // com.microsoft.kapp.utils.MultipleRequestManager.OnRequestCompleteListener
    public void requestComplete(LoadStatus status) {
        HomeData.copyAndReplace(this.mHomeData);
        this.mIsHomeDataInitilized = true;
        this.mHomeData.onFetchFinished(new FetchResult(status, this.mMultipleRequestManager.getException()));
        Intent intent = new Intent(Constants.INTENT_HOME_REFRESH_COMPLETE);
        LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent);
        this.mHomeData = null;
        synchronized (this.mIsDataLoading) {
            if (this.mLastPendingFetchRequest != null) {
                fetchPendingRequest();
            } else {
                this.mIsDataLoading = false;
            }
        }
    }

    public boolean isHomeDataInitialized() {
        return this.mIsHomeDataInitilized;
    }

    /* loaded from: classes.dex */
    public static class FetchResult {
        private final Exception mException;
        private final LoadStatus mLoadStatus;

        public FetchResult(LoadStatus loadStatus, Exception exception) {
            this.mLoadStatus = loadStatus == null ? LoadStatus.ERROR : loadStatus;
            this.mException = exception;
        }

        public LoadStatus getStatus() {
            return this.mLoadStatus;
        }

        public Exception getException() {
            return this.mException;
        }
    }
}

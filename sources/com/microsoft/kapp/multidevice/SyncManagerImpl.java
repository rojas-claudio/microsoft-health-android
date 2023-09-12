package com.microsoft.kapp.multidevice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.cache.CacheUtils;
import com.microsoft.kapp.device.CargoSyncResult;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.event.SyncCompletedEvent;
import com.microsoft.kapp.event.SyncStartedEvent;
import com.microsoft.kapp.event.SyncStatusListener;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.logging.http.FiddlerLogger;
import com.microsoft.kapp.models.SyncState;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.models.home.HomeDataFetcher;
import com.microsoft.kapp.multidevice.KSyncResult;
import com.microsoft.kapp.multidevice.MultiDeviceConstants;
import com.microsoft.kapp.sensor.PhoneSensorDataProvider;
import com.microsoft.kapp.sensor.SensorDataProvider;
import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.sensor.upload.BandSensorDataUploader;
import com.microsoft.kapp.sensor.upload.EventSensorDataUploader;
import com.microsoft.kapp.sensor.upload.SensorDataUploader;
import com.microsoft.kapp.services.KAppsUpdater;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class SyncManagerImpl implements SyncManager, SyncProgressCallback {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final int MAX_SYNC_THREADS = 4;
    private static final int SYNC_TIMEOUT_SECONDS = 180;
    private static final String TAG;
    private BandSensorDataUploader mBandSensorDataUploader;
    private CacheService mCacheService;
    private int mCombinedProgressPercentage;
    private KSyncResult mCombinedSyncResult;
    private Context mContext;
    private EventSensorDataUploader mEventSensorDataUploader;
    private FiddlerLogger mFiddlerLogger;
    private boolean mIsBackgroundSync;
    private boolean mIsPopupErrorAllowed;
    private volatile Boolean mIsWaitingHomeDataFetchComplete;
    private KAppsUpdater mKAppsUpdater;
    private PhoneSensorDataProvider mPhoneSensorDataProvider;
    private SensorUtils mSensorUtils;
    private SettingsProvider mSettingsProvider;
    private volatile SyncState mState;
    private final CopyOnWriteArrayList<WeakReference<SyncStatusListener>> mSyncStatusListenersWefRefs;
    private Map<MultiDeviceConstants.DeviceType, SensorDataProvider> mProviderMap = new HashMap();
    private Map<MultiDeviceConstants.DeviceType, SensorDataUploader> mUploaderMap = new HashMap();
    private Map<MultiDeviceConstants.DeviceType, Integer> mSyncProgressMap = new HashMap();
    private Map<MultiDeviceConstants.DeviceType, KSyncResult> mSyncResultsMap = new HashMap();
    private Handler mTimeOutHandler = new Handler();
    private BroadcastReceiver mReceiver = new HomeDataRefreshBroadcastReceiver();
    private Runnable mPostTimeOutAction = new Runnable() { // from class: com.microsoft.kapp.multidevice.SyncManagerImpl.1
        @Override // java.lang.Runnable
        public void run() {
            synchronized (SyncManagerImpl.this.mIsWaitingHomeDataFetchComplete) {
                if (SyncManagerImpl.this.mIsWaitingHomeDataFetchComplete.booleanValue()) {
                    SyncManagerImpl.this.setSyncStatus(SyncState.COMPLETED);
                }
            }
        }
    };
    ExecutorService mSyncTasksExecutor = null;

    static {
        $assertionsDisabled = !SyncManagerImpl.class.desiredAssertionStatus();
        TAG = SyncManagerImpl.class.getSimpleName();
    }

    public SyncManagerImpl(Context context, SettingsProvider settingsProvider, CacheService cacheService, KAppsUpdater kAppsUpdater, FiddlerLogger fiddlerLogger, PhoneSensorDataProvider phoneSensorDataProvider, EventSensorDataUploader eventSensorDataUploader, BandSensorDataUploader bandSensorDataUploader, SensorUtils sensorUtils) {
        this.mContext = context;
        this.mSettingsProvider = settingsProvider;
        this.mCacheService = cacheService;
        this.mKAppsUpdater = kAppsUpdater;
        this.mFiddlerLogger = fiddlerLogger;
        this.mPhoneSensorDataProvider = phoneSensorDataProvider;
        this.mEventSensorDataUploader = eventSensorDataUploader;
        this.mBandSensorDataUploader = bandSensorDataUploader;
        this.mSensorUtils = sensorUtils;
        this.mProviderMap.put(MultiDeviceConstants.DeviceType.PHONE, this.mPhoneSensorDataProvider);
        this.mUploaderMap.put(MultiDeviceConstants.DeviceType.PHONE, this.mEventSensorDataUploader);
        this.mProviderMap.put(MultiDeviceConstants.DeviceType.BAND, null);
        this.mUploaderMap.put(MultiDeviceConstants.DeviceType.BAND, this.mBandSensorDataUploader);
        this.mSyncStatusListenersWefRefs = new CopyOnWriteArrayList<>();
        this.mState = SyncState.NOT_STARTED;
        this.mCombinedSyncResult = new KSyncResult(KSyncResult.SyncResultCode.SUCCESS);
        this.mCombinedProgressPercentage = 0;
    }

    @Override // com.microsoft.kapp.multidevice.SyncManager
    public void cancelSyncInProgress() {
        this.mCombinedSyncResult = new KSyncResult(KSyncResult.SyncResultCode.UNKNOWN_ERROR);
        setSyncStatus(SyncState.PRE_COMPLETE);
    }

    @Override // com.microsoft.kapp.multidevice.SyncManager
    public KSyncResult syncAll(boolean isBackground, boolean isPopUpErrorAllowed) {
        return sync(Arrays.asList(MultiDeviceConstants.DeviceType.values()), isBackground, isPopUpErrorAllowed);
    }

    @Override // com.microsoft.kapp.multidevice.SyncManager
    public KSyncResult sync(List<MultiDeviceConstants.DeviceType> deviceTypes, boolean isBackground, boolean isPopUpErrorAllowed) {
        synchronized (this.mState) {
            if (getState() != SyncState.NOT_STARTED) {
                return null;
            }
            setSyncStatus(SyncState.IN_PROGRESS);
            this.mCombinedProgressPercentage = 0;
            this.mCombinedSyncResult = new KSyncResult(KSyncResult.SyncResultCode.SUCCESS);
            synchronized (this.mSyncProgressMap) {
                this.mSyncProgressMap.clear();
            }
            synchronized (this.mSyncResultsMap) {
                this.mSyncResultsMap.clear();
            }
            this.mSyncTasksExecutor = Executors.newFixedThreadPool(4);
            this.mIsBackgroundSync = isBackground;
            this.mIsPopupErrorAllowed = isPopUpErrorAllowed;
            CargoUserProfile profile = this.mSettingsProvider.getUserProfile();
            if (profile != null) {
                for (MultiDeviceConstants.DeviceType deviceType : deviceTypes) {
                    switch (deviceType) {
                        case PHONE:
                            if (this.mSettingsProvider.isSensorLoggingEnabled() && this.mSensorUtils.isPhoneConnected(profile)) {
                                this.mSyncTasksExecutor.execute(new SyncTask(this, MultiDeviceConstants.DeviceType.PHONE, this.mProviderMap.get(deviceType), this.mUploaderMap.get(deviceType)));
                                break;
                            }
                            break;
                        case BAND:
                            if (profile.getDeviceId() != null && this.mSensorUtils.hasBand(profile)) {
                                this.mSyncTasksExecutor.execute(new LegacySyncTask(this, MultiDeviceConstants.DeviceType.BAND, !this.mIsBackgroundSync, this.mProviderMap.get(deviceType), this.mUploaderMap.get(deviceType)));
                                break;
                            }
                            break;
                    }
                }
            }
            try {
                boolean completed = awaitSyncCompletion(180);
                if (!completed) {
                    this.mCombinedSyncResult.setStatus(KSyncResult.SyncResultCode.UNKNOWN_ERROR);
                }
            } catch (InterruptedException e) {
                this.mCombinedSyncResult.setStatus(KSyncResult.SyncResultCode.UNKNOWN_ERROR);
            }
            setSyncStatus(SyncState.PRE_COMPLETE);
            return this.mCombinedSyncResult;
        }
    }

    public boolean awaitSyncCompletion(int seconds) throws InterruptedException {
        this.mSyncTasksExecutor.shutdown();
        return this.mSyncTasksExecutor.awaitTermination(seconds, TimeUnit.SECONDS);
    }

    @Override // com.microsoft.kapp.multidevice.SyncManager
    public SyncState getState() {
        SyncState syncState;
        synchronized (this.mState) {
            syncState = this.mState;
        }
        return syncState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setSyncStatus(SyncState syncState) {
        Validate.notNull(syncState, "syncState");
        synchronized (this.mState) {
            this.mState = syncState;
        }
        switch (this.mState) {
            case NOT_STARTED:
                break;
            case IN_PROGRESS:
                notifyListenersOnSyncStarted();
                notifyListenersOnSyncProgress();
                break;
            case PRE_COMPLETE:
                LocalBroadcastManager.getInstance(this.mContext).registerReceiver(this.mReceiver, new IntentFilter(Constants.INTENT_HOME_REFRESH_COMPLETE));
                onSyncPreComplete();
                break;
            case COMPLETED:
                LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mReceiver);
                this.mIsWaitingHomeDataFetchComplete = false;
                cancelTimeOut();
                this.mCombinedProgressPercentage = 100;
                notifyListenersOnSyncProgress();
                notifyListenersOnSyncCompleted();
                setSyncStatus(SyncState.NOT_STARTED);
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError(this.mState);
                }
                break;
        }
    }

    private void onSyncPreComplete() {
        if (shouldRefreshHome(this.mCombinedSyncResult)) {
            this.mCacheService.removeForTag(CacheUtils.SYNCTAG);
            if (this.mIsBackgroundSync) {
                HomeDataFetcher.fetchDataToCache(this.mSettingsProvider.getUUIDsOnDevice());
            } else {
                HomeData.getInstance().fetchAsync();
            }
            cancelTimeOut();
            this.mIsWaitingHomeDataFetchComplete = true;
            notifyListenersOnSyncPreComplete();
            startTimeOut(Constants.SYNC_HOMEDATA_TIMEOUT);
        } else {
            setSyncStatus(SyncState.COMPLETED);
        }
        if (!this.mIsBackgroundSync) {
            KAppsUpdateTask updaterTask = new KAppsUpdateTask();
            updaterTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }
    }

    private boolean shouldRefreshHome(KSyncResult result) {
        return result.isSuccess() || result.getStatus() == KSyncResult.SyncResultCode.DEVICE_ERROR || isProcessingError(result);
    }

    private boolean isProcessingError(KSyncResult result) {
        return result.getStatus() == KSyncResult.SyncResultCode.CLOUD_ERROR && result.getCloudError() != null && result.getCloudError() == KSyncResult.CloudErrorState.CLOUD_PROCESSING_FAILURE;
    }

    @Override // com.microsoft.kapp.multidevice.SyncProgressCallback
    public void onSyncProgress(MultiDeviceConstants.DeviceType deviceType, int progress) {
        synchronized (this.mSyncProgressMap) {
            this.mSyncProgressMap.put(deviceType, Integer.valueOf(progress));
            int minProgress = 100;
            for (MultiDeviceConstants.DeviceType device : this.mSyncProgressMap.keySet()) {
                int partProgress = this.mSyncProgressMap.get(device).intValue();
                if (partProgress < minProgress) {
                    minProgress = partProgress;
                }
            }
            int newProgress = (int) (minProgress * 0.9d);
            if (this.mCombinedProgressPercentage < newProgress) {
                this.mCombinedProgressPercentage = newProgress;
            }
        }
        notifyListenersOnSyncProgress();
    }

    @Override // com.microsoft.kapp.multidevice.SyncProgressCallback
    public void onSyncComplete(MultiDeviceConstants.DeviceType deviceType, KSyncResult result) {
        if (result.isSuccess()) {
            DateTime lastSyncTime = DateTime.now();
            this.mSettingsProvider.setLastSyncTime(lastSyncTime);
        }
        if (result.isSuccess()) {
            DateTime lastSyncTime2 = DateTime.now();
            this.mSettingsProvider.setLastSyncTimeForDevice(lastSyncTime2, deviceType);
        }
        synchronized (this.mSyncResultsMap) {
            if (!this.mSyncResultsMap.containsKey(deviceType)) {
                this.mSyncResultsMap.put(deviceType, result);
            }
            KSyncResult finalResult = new KSyncResult(KSyncResult.SyncResultCode.SUCCESS);
            for (MultiDeviceConstants.DeviceType device : this.mSyncResultsMap.keySet()) {
                KSyncResult finalResult2 = this.mSyncResultsMap.get(device);
                finalResult = finalResult2;
                if (device == MultiDeviceConstants.DeviceType.BAND) {
                    break;
                }
            }
            this.mCombinedSyncResult = finalResult;
        }
    }

    @Override // com.microsoft.kapp.multidevice.SyncProgressCallback
    public void registerForCallbacks(MultiDeviceConstants.DeviceType deviceType) {
        synchronized (this.mSyncProgressMap) {
            if (!this.mSyncProgressMap.containsKey(deviceType)) {
                this.mSyncProgressMap.put(deviceType, 0);
            }
        }
    }

    @Override // com.microsoft.kapp.multidevice.SyncManager
    public void removeSyncStatusListener(SyncStatusListener listener) {
        Validate.notNull(listener, "listener");
        for (int i = this.mSyncStatusListenersWefRefs.size() - 1; i >= 0; i--) {
            SyncStatusListener current = this.mSyncStatusListenersWefRefs.get(i).get();
            if (current == null || current == listener) {
                this.mSyncStatusListenersWefRefs.remove(i);
            }
        }
    }

    @Override // com.microsoft.kapp.multidevice.SyncManager
    public void addSyncStatusListener(SyncStatusListener listener) {
        Validate.notNull(listener, "listener");
        this.mSyncStatusListenersWefRefs.add(new WeakReference<>(listener));
    }

    private void notifyListenersOnSyncProgress() {
        Handler uiHandler = new Handler(this.mContext.getMainLooper());
        Iterator i$ = this.mSyncStatusListenersWefRefs.iterator();
        while (i$.hasNext()) {
            final WeakReference<SyncStatusListener> listenerWeakRef = i$.next();
            uiHandler.post(new Runnable() { // from class: com.microsoft.kapp.multidevice.SyncManagerImpl.2
                @Override // java.lang.Runnable
                public void run() {
                    SyncStatusListener listener = (SyncStatusListener) listenerWeakRef.get();
                    if (listener != null) {
                        listener.onSyncProgress(SyncManagerImpl.this.mCombinedProgressPercentage);
                    }
                }
            });
        }
    }

    private void notifyListenersOnSyncStarted() {
        final SyncStartedEvent event = new SyncStartedEvent(DateTime.now());
        Handler uiHandler = new Handler(this.mContext.getMainLooper());
        Iterator i$ = this.mSyncStatusListenersWefRefs.iterator();
        while (i$.hasNext()) {
            final WeakReference<SyncStatusListener> listenerWeakRef = i$.next();
            uiHandler.post(new Runnable() { // from class: com.microsoft.kapp.multidevice.SyncManagerImpl.3
                @Override // java.lang.Runnable
                public void run() {
                    SyncStatusListener listener = (SyncStatusListener) listenerWeakRef.get();
                    if (listener != null) {
                        listener.onSyncStarted(event);
                    }
                }
            });
        }
    }

    private void notifyListenersOnSyncCompleted() {
        CargoSyncResult result = new CargoSyncResult(!this.mIsBackgroundSync, this.mIsPopupErrorAllowed, this.mCombinedSyncResult);
        final SyncCompletedEvent event = new SyncCompletedEvent(result);
        Handler uiHandler = new Handler(this.mContext.getMainLooper());
        Iterator i$ = this.mSyncStatusListenersWefRefs.iterator();
        while (i$.hasNext()) {
            final WeakReference<SyncStatusListener> listenerWeakRef = i$.next();
            uiHandler.post(new Runnable() { // from class: com.microsoft.kapp.multidevice.SyncManagerImpl.4
                @Override // java.lang.Runnable
                public void run() {
                    SyncStatusListener listener = (SyncStatusListener) listenerWeakRef.get();
                    if (listener != null) {
                        listener.onSyncCompleted(event);
                    }
                }
            });
        }
    }

    private void notifyListenersOnSyncPreComplete() {
        CargoSyncResult result = new CargoSyncResult(!this.mIsBackgroundSync, this.mIsPopupErrorAllowed, this.mCombinedSyncResult);
        final SyncCompletedEvent event = new SyncCompletedEvent(result);
        Handler uiHandler = new Handler(this.mContext.getMainLooper());
        Iterator i$ = this.mSyncStatusListenersWefRefs.iterator();
        while (i$.hasNext()) {
            final WeakReference<SyncStatusListener> listenerWeakRef = i$.next();
            uiHandler.post(new Runnable() { // from class: com.microsoft.kapp.multidevice.SyncManagerImpl.5
                @Override // java.lang.Runnable
                public void run() {
                    SyncStatusListener listener = (SyncStatusListener) listenerWeakRef.get();
                    if (listener != null) {
                        listener.onSyncPreComplete(event);
                    }
                }
            });
        }
    }

    private void startTimeOut(int timeInMillis) {
        this.mTimeOutHandler.postDelayed(this.mPostTimeOutAction, timeInMillis);
    }

    private void cancelTimeOut() {
        this.mTimeOutHandler.removeCallbacks(this.mPostTimeOutAction);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class KAppsUpdateTask extends AsyncTask<Void, Void, Void> {
        private KAppsUpdateTask() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... params) {
            try {
                KLog.i(SyncManagerImpl.TAG, "Syncing tile data started");
                SyncManagerImpl.this.mKAppsUpdater.updateAll();
                KLog.i(SyncManagerImpl.TAG, "Syncing tile data completed");
                return null;
            } catch (Exception ex) {
                KLog.w(SyncManagerImpl.TAG, "Tile Sync: An error occurred when syncing tile data", ex);
                return null;
            }
        }
    }

    /* loaded from: classes.dex */
    private final class HomeDataRefreshBroadcastReceiver extends BroadcastReceiver {
        private HomeDataRefreshBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (Constants.INTENT_HOME_REFRESH_COMPLETE.equals(action)) {
                    synchronized (SyncManagerImpl.this.mIsWaitingHomeDataFetchComplete) {
                        if (!SyncManagerImpl.this.mIsWaitingHomeDataFetchComplete.booleanValue()) {
                            KLog.d(SyncManagerImpl.TAG, "Sync has already timedOut the HomeData Fetching..");
                        } else {
                            SyncManagerImpl.this.setSyncStatus(SyncState.COMPLETED);
                        }
                    }
                }
            } catch (Exception exception) {
                KLog.d(SyncManagerImpl.TAG, "unable to send feedback ", exception);
            }
        }
    }

    @Override // com.microsoft.kapp.multidevice.SyncManager
    public int getCurrentSyncProgress() {
        return this.mCombinedProgressPercentage;
    }
}

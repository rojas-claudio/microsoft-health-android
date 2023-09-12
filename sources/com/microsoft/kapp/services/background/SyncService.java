package com.microsoft.kapp.services.background;

import android.content.Context;
import android.content.Intent;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.services.InjectableIntentService;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.LogScenarioTags;
import com.microsoft.kapp.utils.SyncUtils;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class SyncService extends InjectableIntentService {
    private static final String TAG = SyncService.class.getSimpleName();
    @Inject
    Context mContext;
    @Inject
    MultiDeviceManager mMultiDeviceManager;
    @Inject
    SettingsProvider mSettingsProvider;

    public SyncService() {
        super(TAG);
    }

    @Override // com.microsoft.kapp.services.InjectableIntentService, android.app.IntentService, android.app.Service
    public void onCreate() {
        super.onCreate();
        startForeground(1, SyncUtils.buildSyncNotification(this));
    }

    @Override // android.app.IntentService, android.app.Service
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // android.app.IntentService
    protected void onHandleIntent(Intent intent) {
        try {
            boolean isForegroundSync = intent.getBooleanExtra(Constants.SYNC_INTENT_SOURCE, false);
            boolean isPopUpErrorAllowed = intent.getBooleanExtra(Constants.SYNC_POPUP_ERROR, true);
            Object[] objArr = new Object[1];
            objArr[0] = Boolean.valueOf(!isForegroundSync);
            KLog.d(LogScenarioTags.Sync, "SYNC: SyncService onHandleIntent() called. isBackground=[%b]", objArr);
            if (this.mMultiDeviceManager.isSyncInProgress()) {
                KLog.i(TAG, "Sync cancelled, a sync is already in progress");
            } else if (!isForegroundSync && !SyncUtils.isDataSyncEnabled(this.mContext, this.mSettingsProvider)) {
                KLog.i(TAG, "Sync cancelled, is disabled");
            } else if (this.mSettingsProvider.getFreStatus() != FreStatus.SHOWN) {
                KLog.i(TAG, "Sync cancelled, FRE has not yet completed.");
            } else {
                KLog.i(TAG, "Sync started");
                this.mMultiDeviceManager.synchronizeAllDevices(isForegroundSync ? false : true, isPopUpErrorAllowed);
                KLog.i(TAG, "Sync complete");
            }
        } catch (Exception exception) {
            KLog.w(TAG, "Exception in onHandleIntent", exception);
            this.mMultiDeviceManager.cancelSyncInProgress();
        }
    }
}

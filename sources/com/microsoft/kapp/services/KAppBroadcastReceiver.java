package com.microsoft.kapp.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import com.microsoft.kapp.KApplication;
import com.microsoft.kapp.UserProfileFetcher;
import com.microsoft.kapp.diagnostics.Compatibility;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.logging.LogCleanupService;
import com.microsoft.kapp.multidevice.MultiDeviceManager;
import com.microsoft.kapp.services.background.AppConfigurationService;
import com.microsoft.kapp.services.background.CacheCleanupService;
import com.microsoft.kapp.services.background.KAppsService;
import com.microsoft.kapp.services.timeZone.TimeZoneChangeHandler;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.SyncUtils;
import java.util.TimeZone;
import javax.inject.Inject;
import org.joda.time.DateTimeZone;
/* loaded from: classes.dex */
public class KAppBroadcastReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = KAppBroadcastReceiver.class.getSimpleName();
    private volatile boolean mInitialized;
    @Inject
    MultiDeviceManager mMultiDeviceManager;
    @Inject
    SettingsProvider mSettingsProvider;
    @Inject
    TimeZoneChangeHandler mTimeZoneChangeHandler;
    @Inject
    UserProfileFetcher mUserProfileFetcher;

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (!this.mInitialized) {
            synchronized (this) {
                if (!this.mInitialized) {
                    KApplication application = (KApplication) context.getApplicationContext();
                    application.inject(this);
                    this.mInitialized = true;
                }
            }
        }
        String action = intent.getAction();
        if (action.equalsIgnoreCase(Constants.INTENT_DEVICE_BOOT)) {
            KLog.i(TAG, "Setting sync service alarm");
            SyncUtils.ensurePeriodicTasksSchedule(context, this.mSettingsProvider);
        } else if (action.equalsIgnoreCase(Constants.INTENT_TRIGGER_SYNC)) {
            if (SyncUtils.isDataSyncEnabled(context, this.mSettingsProvider)) {
                this.mMultiDeviceManager.startSync(true, false);
            }
        } else if (action.equalsIgnoreCase(Constants.INTENT_KAPPS_UPDATE)) {
            Intent updateIntent = new Intent(context, KAppsService.class);
            startWakefulService(context, updateIntent);
        } else if (action.equalsIgnoreCase(Constants.INTENT_CACHE_CLEANUP)) {
            Intent updateIntent2 = new Intent(context, CacheCleanupService.class);
            startWakefulService(context, updateIntent2);
        } else if (action.equalsIgnoreCase(Constants.INTENT_LOG_CLEANUP)) {
            Intent updateIntent3 = new Intent(context, LogCleanupService.class);
            startWakefulService(context, updateIntent3);
        } else if (action.equalsIgnoreCase(Constants.INTENT_REFRESH_PROFILE)) {
            this.mUserProfileFetcher.updateLocallyStoredValuesAsync();
        } else if (action.equalsIgnoreCase("android.intent.action.TIMEZONE_CHANGED")) {
            KLog.i(TAG, "Going to update joda time zone");
            DateTimeZone.setDefault(DateTimeZone.forTimeZone(TimeZone.getDefault()));
            this.mTimeZoneChangeHandler.notifyTimeZoneChange();
        } else if (action.equalsIgnoreCase("android.bluetooth.adapter.action.STATE_CHANGED")) {
            KLog.i(TAG, "Bluetooth enabled or disabled");
            int bluetoothState = intent.getExtras().getInt("android.bluetooth.adapter.extra.STATE");
            if (bluetoothState == 12) {
                SyncUtils.ensurePeriodicTasksSchedule(context, this.mSettingsProvider);
            } else if (bluetoothState == 10) {
                SyncUtils.clearSyncAndAppsUpdateSchedule(context);
            }
        } else if (action.equalsIgnoreCase(Constants.INTENT_DEBUG_SESSION_HEADER)) {
            if (!Compatibility.isPublicRelease()) {
                Bundle bundle = intent.getExtras();
                String headerValue = bundle.getString(Constants.INTENT_DEBUG_SESSION_HEADER);
                if (headerValue != null) {
                    this.mSettingsProvider.setSessionHeaderValue(headerValue);
                }
            }
        } else if (action.equalsIgnoreCase(Constants.INTENT_APP_CONFIGURATION)) {
            Intent updateIntent4 = new Intent(context, AppConfigurationService.class);
            startWakefulService(context, updateIntent4);
        } else if (action.equalsIgnoreCase("android.intent.action.LOCALE_CHANGED")) {
            Intent updateIntent5 = new Intent(context, AppConfigurationService.class);
            startWakefulService(context, updateIntent5);
        }
    }
}

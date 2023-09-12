package com.microsoft.kapp.services.background;

import android.content.Intent;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.services.InjectableIntentService;
import com.microsoft.kapp.services.KAppsUpdater;
import com.microsoft.kapp.services.SettingsProvider;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class KAppsService extends InjectableIntentService {
    private static final String SERVICE_NAME = "KAppsService";
    private static final String TAG = KAppsService.class.getSimpleName();
    @Inject
    SettingsProvider mSettingsProvider;
    @Inject
    KAppsUpdater mUpdater;

    public KAppsService() {
        super(SERVICE_NAME);
    }

    public KAppsService(SettingsProvider settingsProvider, KAppsUpdater updater) {
        super(SERVICE_NAME);
        this.mSettingsProvider = settingsProvider;
        this.mUpdater = updater;
    }

    @Override // android.app.IntentService
    protected void onHandleIntent(Intent intent) {
        try {
            if (this.mSettingsProvider.getFreStatus() == FreStatus.SHOWN) {
                KLog.i(TAG, "KApps update started");
                this.mUpdater.updateAll();
                KLog.i(TAG, "KApps update complete");
            } else {
                KLog.i(TAG, "KApps update cancelled, FRE not yet complete.");
            }
        } catch (Exception exception) {
            KLog.w(TAG, "Unexpected exception on KApps update task.", exception);
        }
    }
}

package com.microsoft.kapp.services.background;

import android.content.Intent;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.InjectableIntentService;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.AppConfigurationManager;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class AppConfigurationService extends InjectableIntentService {
    private static final String SERVICE_NAME = "AppConfigurationService";
    private static final String TAG = AppConfigurationService.class.getSimpleName();
    @Inject
    AppConfigurationManager mAppConfigurationManager;
    @Inject
    SettingsProvider mSettingsProvider;

    public AppConfigurationService() {
        super(SERVICE_NAME);
    }

    @Override // android.app.IntentService
    protected void onHandleIntent(Intent intent) {
        try {
            this.mAppConfigurationManager.downloadAppConfiguration();
        } catch (Exception exception) {
            KLog.e(TAG, "Unexpected exception during downloading application config.", exception);
        }
    }
}

package com.microsoft.kapp.models.home;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.MultipleRequestManager;
import com.microsoft.krestsdk.models.RunEvent;
import com.microsoft.krestsdk.services.RestService;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class RunDataFetcher implements DataFetcher {
    private static final String TAG = RunDataFetcher.class.getSimpleName();
    private HomeData mHomeData;
    private RestService mRestService;
    private SettingsProvider mSettingsProvider;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RunDataFetcher(RestService restService, SettingsProvider settingsProvider, HomeData homeData) {
        this.mRestService = restService;
        this.mSettingsProvider = settingsProvider;
        this.mHomeData = homeData;
    }

    @Override // com.microsoft.kapp.models.home.DataFetcher
    public void fetch(final MultipleRequestManager multipleRequestManager) {
        this.mRestService.getTopRunEvents(this.mSettingsProvider.isDistanceHeightMetric(), 1, false, new Callback<List<RunEvent>>() { // from class: com.microsoft.kapp.models.home.RunDataFetcher.1
            @Override // com.microsoft.kapp.Callback
            public void callback(List<RunEvent> result) {
                if (Validate.isNotNullNotEmpty(result)) {
                    RunDataFetcher.this.mHomeData.setRunEvent(result.get(0));
                }
                multipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(RunDataFetcher.TAG, "getTopRunEvents() failed.", ex);
                multipleRequestManager.notifyRequestFailed(ex);
            }
        });
    }
}

package com.microsoft.kapp.models.home;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.MultipleRequestManager;
import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.services.RestService;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class BikeDataFetcher implements DataFetcher {
    private static final String TAG = BikeDataFetcher.class.getSimpleName();
    private HomeData mHomeData;
    private RestService mRestService;
    private SettingsProvider mSettingsProvider;

    /* JADX INFO: Access modifiers changed from: package-private */
    public BikeDataFetcher(RestService restService, HomeData homeData, SettingsProvider settingsProvider) {
        this.mRestService = restService;
        this.mHomeData = homeData;
        this.mSettingsProvider = settingsProvider;
    }

    @Override // com.microsoft.kapp.models.home.DataFetcher
    public void fetch(final MultipleRequestManager multipleRequestManager) {
        this.mRestService.getTopBikeEvents(this.mSettingsProvider.isDistanceHeightMetric(), 1, true, new Callback<List<BikeEvent>>() { // from class: com.microsoft.kapp.models.home.BikeDataFetcher.1
            @Override // com.microsoft.kapp.Callback
            public void callback(List<BikeEvent> result) {
                if (Validate.isNotNullNotEmpty(result)) {
                    BikeDataFetcher.this.mHomeData.setBikeEvent(result.get(0));
                }
                multipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(BikeDataFetcher.TAG, "getTopBikeEvents() failed.", ex);
                multipleRequestManager.notifyRequestFailed(ex);
            }
        });
    }
}

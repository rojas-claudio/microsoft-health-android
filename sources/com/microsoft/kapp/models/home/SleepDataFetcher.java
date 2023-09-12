package com.microsoft.kapp.models.home;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.MultipleRequestManager;
import com.microsoft.krestsdk.models.SleepEvent;
import com.microsoft.krestsdk.services.RestService;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class SleepDataFetcher implements DataFetcher {
    private static final String TAG = SleepDataFetcher.class.getSimpleName();
    private HomeData mHomeData;
    private RestService mRestService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SleepDataFetcher(RestService restService, HomeData homeData) {
        this.mRestService = restService;
        this.mHomeData = homeData;
    }

    @Override // com.microsoft.kapp.models.home.DataFetcher
    public void fetch(final MultipleRequestManager multipleRequestManager) {
        this.mRestService.getTopSleepEvents(1, false, new Callback<List<SleepEvent>>() { // from class: com.microsoft.kapp.models.home.SleepDataFetcher.1
            @Override // com.microsoft.kapp.Callback
            public void callback(List<SleepEvent> result) {
                if (Validate.isNotNullNotEmpty(result)) {
                    SleepDataFetcher.this.mHomeData.setSleepEvent(result.get(0));
                }
                multipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(SleepDataFetcher.TAG, "getTopSleepEvents() failed.", ex);
                multipleRequestManager.notifyRequestFailed(ex);
            }
        });
    }
}

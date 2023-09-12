package com.microsoft.kapp.models.home;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.MultipleRequestManager;
import com.microsoft.krestsdk.models.InsightMetadata;
import com.microsoft.krestsdk.services.RestService;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class InsightsDataFetcher implements DataFetcher {
    private static final String TAG = InsightsDataFetcher.class.getSimpleName();
    private HomeData mHomeData;
    private RestService mRestService;

    InsightsDataFetcher(RestService restService, HomeData homeData) {
        this.mRestService = restService;
        this.mHomeData = homeData;
    }

    @Override // com.microsoft.kapp.models.home.DataFetcher
    public void fetch(final MultipleRequestManager multipleRequestManager) {
        this.mRestService.getInsights(new Callback<List<InsightMetadata>>() { // from class: com.microsoft.kapp.models.home.InsightsDataFetcher.1
            @Override // com.microsoft.kapp.Callback
            public void callback(List<InsightMetadata> result) {
                InsightsDataFetcher.this.mHomeData.setInsightMetadata(result);
                multipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(InsightsDataFetcher.TAG, "InsightsDataFetcher failed.", ex);
                multipleRequestManager.notifyRequestFailed(ex);
            }
        });
    }
}

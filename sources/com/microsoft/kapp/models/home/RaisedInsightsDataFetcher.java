package com.microsoft.kapp.models.home;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.MultipleRequestManager;
import com.microsoft.krestsdk.models.RaisedInsight;
import com.microsoft.krestsdk.services.RaisedInsightQuery;
import com.microsoft.krestsdk.services.RestService;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class RaisedInsightsDataFetcher implements DataFetcher {
    private static final String TAG = RaisedInsightsDataFetcher.class.getSimpleName();
    private HomeData mHomeData;
    private RaisedInsightQuery mQuery;
    private RestService mRestService;

    RaisedInsightsDataFetcher(RestService restService, HomeData homeData, RaisedInsightQuery query) {
        this.mRestService = restService;
        this.mHomeData = homeData;
        this.mQuery = query;
    }

    @Override // com.microsoft.kapp.models.home.DataFetcher
    public void fetch(final MultipleRequestManager multipleRequestManager) {
        this.mRestService.getRaisedInsights(new Callback<List<RaisedInsight>>() { // from class: com.microsoft.kapp.models.home.RaisedInsightsDataFetcher.1
            @Override // com.microsoft.kapp.Callback
            public void callback(List<RaisedInsight> result) {
                RaisedInsightsDataFetcher.this.mHomeData.setRaisedInsights(result);
                multipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(RaisedInsightsDataFetcher.TAG, "RaisedInsightsDataFetcher failed.", ex);
                multipleRequestManager.notifyRequestFailed(ex);
            }
        }, this.mQuery);
    }
}

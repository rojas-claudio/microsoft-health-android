package com.microsoft.kapp.models.home;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.MultipleRequestManager;
import com.microsoft.krestsdk.models.UserDailySummary;
import com.microsoft.krestsdk.services.RestService;
import java.util.List;
import org.joda.time.LocalDate;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class DailySummariesDataFetcher implements DataFetcher {
    private static final String TAG = DailySummariesDataFetcher.class.getSimpleName();
    private HomeData mHomeData;
    private RestService mRestService;
    private LocalDate mTargetDate;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DailySummariesDataFetcher(LocalDate targetDate, RestService restService, HomeData homeData) {
        this.mTargetDate = targetDate == null ? LocalDate.now() : targetDate;
        this.mRestService = restService;
        this.mHomeData = homeData;
    }

    @Override // com.microsoft.kapp.models.home.DataFetcher
    public void fetch(final MultipleRequestManager multipleRequestManager) {
        this.mRestService.getDailySummaries(this.mTargetDate.minusDays(6), this.mTargetDate.plusDays(1), new Callback<List<UserDailySummary>>() { // from class: com.microsoft.kapp.models.home.DailySummariesDataFetcher.1
            @Override // com.microsoft.kapp.Callback
            public void callback(List<UserDailySummary> result) {
                if (Validate.isNotNullNotEmpty(result)) {
                    DailySummariesDataFetcher.this.mHomeData.setUserDailySummaries(result);
                }
                multipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(DailySummariesDataFetcher.TAG, "getDailySummaries() failed.", ex);
                multipleRequestManager.notifyRequestFailed(ex);
            }
        });
    }
}

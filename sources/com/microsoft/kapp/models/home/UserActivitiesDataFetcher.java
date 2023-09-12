package com.microsoft.kapp.models.home;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.MultipleRequestManager;
import com.microsoft.krestsdk.models.UserActivity;
import com.microsoft.krestsdk.services.RestService;
import java.util.List;
import org.joda.time.LocalDate;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class UserActivitiesDataFetcher implements DataFetcher {
    private static final String TAG = UserActivitiesDataFetcher.class.getSimpleName();
    private HomeData mHomeData;
    private RestService mRestService;
    private LocalDate mTargetDate;

    /* JADX INFO: Access modifiers changed from: package-private */
    public UserActivitiesDataFetcher(LocalDate targetDate, RestService restService, HomeData homeData) {
        this.mTargetDate = targetDate == null ? LocalDate.now() : targetDate;
        this.mRestService = restService;
        this.mHomeData = homeData;
    }

    @Override // com.microsoft.kapp.models.home.DataFetcher
    public void fetch(final MultipleRequestManager multipleRequestManager) {
        this.mRestService.getUserActivitiesByHour(this.mTargetDate, new Callback<List<UserActivity>>() { // from class: com.microsoft.kapp.models.home.UserActivitiesDataFetcher.1
            @Override // com.microsoft.kapp.Callback
            public void callback(List<UserActivity> result) {
                if (Validate.isNotNullNotEmpty(result)) {
                    UserActivitiesDataFetcher.this.mHomeData.setUserActivities(result);
                }
                multipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(UserActivitiesDataFetcher.TAG, "getUserActivitiesByHour() failed.", ex);
                multipleRequestManager.notifyRequestFailed(ex);
            }
        });
    }
}

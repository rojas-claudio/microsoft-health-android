package com.microsoft.kapp.models.home;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.MultipleRequestManager;
import com.microsoft.krestsdk.models.CategoryType;
import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.GoalType;
import com.microsoft.krestsdk.services.RestService;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class PersonalBestDataFetcher implements DataFetcher {
    private static final String TAG = PersonalBestDataFetcher.class.getSimpleName();
    private HomeData mHomeData;
    private RestService mRestService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PersonalBestDataFetcher(RestService restService, HomeData homeData) {
        this.mRestService = restService;
        this.mHomeData = homeData;
    }

    @Override // com.microsoft.kapp.models.home.DataFetcher
    public void fetch(final MultipleRequestManager multipleRequestManager) {
        this.mRestService.getActiveGoalByType(new Callback<List<GoalDto>>() { // from class: com.microsoft.kapp.models.home.PersonalBestDataFetcher.1
            @Override // com.microsoft.kapp.Callback
            public void callback(List<GoalDto> result) {
                if (Validate.isNotNullNotEmpty(result)) {
                    PersonalBestDataFetcher.this.mHomeData.setPersonalBests(result);
                }
                multipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(PersonalBestDataFetcher.TAG, "getActiveGoalByType() failed.", ex);
                multipleRequestManager.notifyRequestFailed(ex);
            }
        }, true, CategoryType.BESTS, GoalType.UNKNOWN);
    }
}

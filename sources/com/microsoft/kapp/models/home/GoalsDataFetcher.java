package com.microsoft.kapp.models.home;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.MultipleRequestManager;
import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.GoalType;
import com.microsoft.krestsdk.services.RestService;
import java.util.HashMap;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class GoalsDataFetcher implements DataFetcher {
    private static final String TAG = GoalsDataFetcher.class.getSimpleName();
    private HomeData mHomeData;
    private RestService mRestService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GoalsDataFetcher(RestService restService, HomeData homeData) {
        this.mRestService = restService;
        this.mHomeData = homeData;
    }

    @Override // com.microsoft.kapp.models.home.DataFetcher
    public void fetch(final MultipleRequestManager multipleRequestManager) {
        this.mRestService.getGoals(new Callback<List<GoalDto>>() { // from class: com.microsoft.kapp.models.home.GoalsDataFetcher.1
            @Override // com.microsoft.kapp.Callback
            public void callback(List<GoalDto> goals) {
                if (Validate.isNotNullNotEmpty(goals)) {
                    HashMap<GoalType, GoalDto> results = new HashMap<>();
                    for (GoalDto goal : goals) {
                        if (goal != null) {
                            results.put(goal.getType(), goal);
                        }
                    }
                    GoalsDataFetcher.this.mHomeData.setGoals(results);
                }
                multipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(GoalsDataFetcher.TAG, "getGoals() failed.", ex);
                multipleRequestManager.notifyRequestFailed(ex);
            }
        });
    }
}

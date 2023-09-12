package com.microsoft.kapp.models.home;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.MultipleRequestManager;
import com.microsoft.krestsdk.models.ExerciseEvent;
import com.microsoft.krestsdk.services.RestService;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ExerciseDataFetcher implements DataFetcher {
    private static final String TAG = ExerciseDataFetcher.class.getSimpleName();
    private HomeData mHomeData;
    private RestService mRestService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ExerciseDataFetcher(RestService restService, HomeData homeData) {
        this.mRestService = restService;
        this.mHomeData = homeData;
    }

    @Override // com.microsoft.kapp.models.home.DataFetcher
    public void fetch(final MultipleRequestManager multipleRequestManager) {
        this.mRestService.getTopExerciseEvents(1, false, new Callback<List<ExerciseEvent>>() { // from class: com.microsoft.kapp.models.home.ExerciseDataFetcher.1
            @Override // com.microsoft.kapp.Callback
            public void callback(List<ExerciseEvent> result) {
                if (Validate.isNotNullNotEmpty(result)) {
                    ExerciseDataFetcher.this.mHomeData.setExerciseEvent(result.get(0));
                }
                multipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(ExerciseDataFetcher.TAG, "getTopExerciseEvents() failed.", ex);
                multipleRequestManager.notifyRequestFailed(ex);
            }
        });
    }
}

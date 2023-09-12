package com.microsoft.kapp.models.home;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.KApplicationGraph;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.utils.MultipleRequestManager;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class GuidedWorkoutDataFetcher implements DataFetcher {
    private static final String TAG = GuidedWorkoutDataFetcher.class.getSimpleName();
    @Inject
    GuidedWorkoutService mGuidedWorkoutService;

    public GuidedWorkoutDataFetcher() {
        KApplicationGraph.getApplicationGraph().inject(this);
    }

    @Override // com.microsoft.kapp.models.home.DataFetcher
    public void fetch(final MultipleRequestManager callBackMultipleRequestManager) {
        this.mGuidedWorkoutService.fetchGuidedWorkoutData(new Callback<Void>() { // from class: com.microsoft.kapp.models.home.GuidedWorkoutDataFetcher.1
            @Override // com.microsoft.kapp.Callback
            public void callback(Void result) {
                if (callBackMultipleRequestManager != null) {
                    callBackMultipleRequestManager.notifyRequestSucceeded();
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(GuidedWorkoutDataFetcher.TAG, "fetchGuidedWorkoutData() failed.", ex);
                if (callBackMultipleRequestManager != null) {
                    callBackMultipleRequestManager.notifyRequestFailed();
                }
            }
        }, false);
    }
}

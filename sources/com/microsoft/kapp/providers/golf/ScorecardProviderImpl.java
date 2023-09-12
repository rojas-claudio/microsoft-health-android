package com.microsoft.kapp.providers.golf;

import android.content.Context;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.golf.ScorecardModel;
import com.microsoft.kapp.services.golf.GolfService;
import com.microsoft.krestsdk.models.GolfEvent;
/* loaded from: classes.dex */
public class ScorecardProviderImpl implements ScorecardProvider {
    private static final String TAG = ScorecardProviderImpl.class.getSimpleName();
    private Context mContext;
    private GolfService mGolfService;

    public ScorecardProviderImpl(Context context, GolfService golfService) {
        this.mContext = context;
        this.mGolfService = golfService;
    }

    @Override // com.microsoft.kapp.providers.golf.ScorecardProvider
    public void getScoreCardForEvent(String eventID, final Callback<ScorecardModel> callback) {
        this.mGolfService.getGolfEventById(eventID, true, new Callback<GolfEvent>() { // from class: com.microsoft.kapp.providers.golf.ScorecardProviderImpl.1
            @Override // com.microsoft.kapp.Callback
            public void callback(GolfEvent result) {
                try {
                    ScorecardModel sm = new ScorecardModel(result);
                    callback.callback(sm);
                } catch (Exception ex) {
                    KLog.e(ScorecardProviderImpl.TAG, "Exception trying to create scorecardmodel", ex);
                    callback.onError(ex);
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.w(ScorecardProviderImpl.TAG, "Exception trying to get golfevent", ex);
                callback.onError(ex);
            }
        });
    }
}

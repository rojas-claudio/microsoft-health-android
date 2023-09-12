package com.microsoft.kapp.models.home;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.MultipleRequestManager;
import com.microsoft.krestsdk.models.GolfEvent;
import com.microsoft.krestsdk.services.RestService;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;
/* loaded from: classes.dex */
public class GolfDataFetcher extends BaseDataFetcher {
    private static final String TAG = GolfDataFetcher.class.getSimpleName();

    public GolfDataFetcher(HomeData homeData) {
        super(homeData);
    }

    public GolfDataFetcher(HomeData homeData, SettingsProvider settings) {
        super(homeData);
        this.mSettingsProvider = settings;
    }

    @Override // com.microsoft.kapp.models.home.DataFetcher
    public void fetch(final MultipleRequestManager multipleRequestManager) {
        this.mService.getTopGolfEvents(1, (ArrayList<RestService.ExpandType>) null, new Callback<List<GolfEvent>>() { // from class: com.microsoft.kapp.models.home.GolfDataFetcher.1
            @Override // com.microsoft.kapp.Callback
            public void callback(List<GolfEvent> result) {
                if (Validate.isNotNullNotEmpty(result)) {
                    GolfDataFetcher.this.mHomeData.setGolfEvent(result.get(0));
                }
                if (GolfDataFetcher.this.mSettingsProvider.getSyncedGolfCourse().first != null) {
                    if (GolfDataFetcher.this.mHomeData.getGolfEvent() != null && GolfDataFetcher.this.mSettingsProvider.getLastSyncedGolfTime() != null && ((String) GolfDataFetcher.this.mSettingsProvider.getSyncedGolfCourse().first).equalsIgnoreCase(GolfDataFetcher.this.mHomeData.getGolfEvent().getCourseID()) && GolfDataFetcher.this.mSettingsProvider.getLastSyncedGolfTime().compareTo((ReadableInstant) GolfDataFetcher.this.mHomeData.getGolfEvent().getEndTime()) <= 0) {
                        GolfDataFetcher.this.mSettingsProvider.updateLastSyncedGolfCourse("", "", DateTime.now());
                    }
                } else {
                    GolfDataFetcher.this.mSettingsProvider.updateLastSyncedGolfCourse("", "", DateTime.now());
                }
                multipleRequestManager.notifyRequestSucceeded();
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                KLog.d(GolfDataFetcher.TAG, "getTopGolfEvents() failed.", ex);
                multipleRequestManager.notifyRequestFailed(ex);
            }
        });
    }
}

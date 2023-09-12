package com.microsoft.kapp.services.background;

import android.content.Intent;
import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.InjectableIntentService;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class CacheCleanupService extends InjectableIntentService {
    private static final String SERVICE_NAME = "CacheCleanupService";
    private static final String TAG = CacheCleanupService.class.getSimpleName();
    @Inject
    CacheService mCacheService;

    public CacheCleanupService() {
        super(SERVICE_NAME);
    }

    @Override // android.app.IntentService
    protected void onHandleIntent(Intent arg0) {
        try {
            KLog.i(TAG, "Cache cleanup started");
            this.mCacheService.cleanup();
            KLog.i(TAG, "cache cleanup complete");
        } catch (Exception exception) {
            KLog.e(TAG, "Unexpected exception during cache cleanup.", exception);
        }
    }
}

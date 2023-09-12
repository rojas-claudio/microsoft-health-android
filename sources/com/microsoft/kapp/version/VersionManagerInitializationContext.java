package com.microsoft.kapp.version;

import com.microsoft.kapp.diagnostics.Validate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import javax.inject.Singleton;
@Singleton
/* loaded from: classes.dex */
public class VersionManagerInitializationContext {
    private static final long APPLICATION_UPDATE_CHECK_INTERVAL_IN_SECONDS = 86400;
    private static final long DEVICE_FIRMWARE_UPDATE_CHECK_INTERVAL_IN_SECONDS = 86400;
    private final ApplicationVersionUpdateNotifier mApplicationVersionUpdateNotifier;
    private final ExecutorService mExecutorService;

    @Inject
    public VersionManagerInitializationContext(ApplicationVersionUpdateNotifier applicationVersionUpdateNotifier) {
        Validate.notNull(applicationVersionUpdateNotifier, "applicationVersionUpdateNotifier");
        this.mExecutorService = Executors.newFixedThreadPool(1);
        this.mApplicationVersionUpdateNotifier = applicationVersionUpdateNotifier;
    }

    public long getApplicationUpdateCheckIntervalInSeconds() {
        return 86400L;
    }

    public long getDeviceFirmwareUpdateCheckIntervalInSeconds() {
        return 86400L;
    }

    public ExecutorService getExecutorService() {
        return this.mExecutorService;
    }

    public ApplicationVersionUpdateNotifier getApplicationVersionUpdateNotifier() {
        return this.mApplicationVersionUpdateNotifier;
    }
}

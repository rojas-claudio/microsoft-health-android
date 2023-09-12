package com.microsoft.kapp.version;

import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.utils.Constants;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import javax.inject.Inject;
import javax.inject.Singleton;
@Singleton
/* loaded from: classes.dex */
public class VersionManager implements VersionUpdateListener, VersionCheckExecutor {
    private ApplicationVersionUpdateNotifier mApplicationVersionUpdateNotifier;
    private final VersionManagerInitializationContext mContext;
    private final VersionUpdateInteractionCoordinator mCoordinator;
    private final ExecutorService mExecutor;
    private long mNextApplicationVersionCheckTime;
    private long mNextDeviceFirmwareVersionCheckTime;
    private VersionManagerDebugOverride mOverride;
    private volatile boolean mRegistered;

    @Inject
    public VersionManager(VersionManagerInitializationContext context, VersionUpdateInteractionCoordinator coordinator) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Validate.notNull(coordinator, "coordinator");
        this.mContext = context;
        this.mCoordinator = coordinator;
        this.mCoordinator.setVersionCheckExecutor(this);
        this.mExecutor = context.getExecutorService();
        this.mApplicationVersionUpdateNotifier = context.getApplicationVersionUpdateNotifier();
        if (this.mExecutor == null) {
            throw new IllegalStateException("getExecutorService returned null.");
        }
        if (this.mApplicationVersionUpdateNotifier == null) {
            throw new IllegalStateException("getApplicationVersionUpdateNotifier() returned null.");
        }
    }

    public synchronized void registerNotifiers() {
        if (this.mRegistered) {
            throw new IllegalStateException("The notifiers have already been registered.");
        }
        this.mApplicationVersionUpdateNotifier.registerListener(this);
        this.mRegistered = true;
    }

    public synchronized void unregisterNotifiers() {
        if (!this.mRegistered) {
            throw new IllegalStateException("The notifiers have not been registered.");
        }
        this.mApplicationVersionUpdateNotifier.unregisterListener(this);
        this.mRegistered = false;
    }

    @Override // com.microsoft.kapp.version.VersionUpdateListener
    public void versionUpdateDetected(VersionUpdateNotifier<?> notifier, VersionUpdate versionUpdate) {
        Validate.notNull(notifier, "notifier");
        Validate.notNull(versionUpdate, "versionUpdate");
        if (notifier == this.mApplicationVersionUpdateNotifier) {
            this.mCoordinator.notifyApplicationUpdateAvailable();
        }
    }

    @Override // com.microsoft.kapp.version.VersionUpdateListener
    public void versionUpdateCheckFailed(VersionUpdateNotifier<?> notifier, Exception ex) {
        if (notifier == this.mApplicationVersionUpdateNotifier) {
        }
    }

    @Override // com.microsoft.kapp.version.VersionCheckExecutor
    public void requestApplicationVersionUpdateCheck() {
        long currentTimeMilliseconds = System.currentTimeMillis();
        if (currentTimeMilliseconds >= this.mNextApplicationVersionCheckTime) {
            this.mNextApplicationVersionCheckTime = getApplicationUpdateCheckIntervalInMilliSeconds() + currentTimeMilliseconds;
            this.mExecutor.execute(this.mApplicationVersionUpdateNotifier);
        }
    }

    public boolean hasDebugOverride() {
        return this.mOverride != null;
    }

    public VersionManagerDebugOverride getDebugOverride() {
        return this.mOverride;
    }

    public void setDebugOverride(VersionManagerDebugOverride override) {
        this.mOverride = override;
        if (override != null) {
            this.mApplicationVersionUpdateNotifier.setVersionRetrieverOverride(new DebugOverrideApplicationVersionRetriever(override));
            long currentTimeMilliseconds = System.currentTimeMillis();
            this.mNextApplicationVersionCheckTime = getApplicationUpdateCheckIntervalInMilliSeconds() + currentTimeMilliseconds;
            this.mNextDeviceFirmwareVersionCheckTime = getDeviceFirmwareUpdateCheckIntervalInMilliSeconds() + currentTimeMilliseconds;
            return;
        }
        this.mApplicationVersionUpdateNotifier.setVersionRetrieverOverride(null);
    }

    public Date getNextApplicationVersionCheckTime() {
        return new Date(this.mNextApplicationVersionCheckTime);
    }

    public Date getNextDeviceFirmwareVersionCheckTime() {
        return new Date(this.mNextDeviceFirmwareVersionCheckTime);
    }

    private long getApplicationUpdateCheckIntervalInMilliSeconds() {
        long interval = this.mContext.getApplicationUpdateCheckIntervalInSeconds();
        if (this.mOverride != null) {
            interval = this.mOverride.getApplicationUpdateCheckIntervalInSeconds();
        }
        return 1000 * interval;
    }

    private long getDeviceFirmwareUpdateCheckIntervalInMilliSeconds() {
        long interval = this.mContext.getDeviceFirmwareUpdateCheckIntervalInSeconds();
        if (this.mOverride != null) {
            interval = this.mOverride.getDeviceFirmwareUpdateCheckIntervalInSeconds();
        }
        return 1000 * interval;
    }
}

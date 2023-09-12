package com.microsoft.applicationinsights.library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import com.microsoft.applicationinsights.library.CreateDataTask;
import com.microsoft.applicationinsights.library.config.ISessionConfig;
import com.microsoft.applicationinsights.logging.InternalLogging;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
/* JADX INFO: Access modifiers changed from: package-private */
@TargetApi(14)
/* loaded from: classes.dex */
public class LifeCycleTracking implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "LifeCycleTracking";
    private static boolean autoPageViewsEnabled;
    private static boolean autoSessionManagementEnabled;
    private static LifeCycleTracking instance;
    protected ISessionConfig config;
    protected TelemetryContext telemetryContext;
    private static volatile boolean isLoaded = false;
    private static final Object LOCK = new Object();
    protected final AtomicInteger activityCount = new AtomicInteger(0);
    protected final AtomicLong lastBackground = new AtomicLong(getTime());

    protected LifeCycleTracking(ISessionConfig config, TelemetryContext telemetryContext) {
        this.config = config;
        this.telemetryContext = telemetryContext;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void initialize(TelemetryContext telemetryContext, ISessionConfig config) {
        if (!isLoaded) {
            synchronized (LOCK) {
                if (!isLoaded) {
                    isLoaded = true;
                    instance = new LifeCycleTracking(config, telemetryContext);
                }
            }
        }
    }

    protected static LifeCycleTracking getInstance() {
        if (instance == null) {
            InternalLogging.error(TAG, "getInstance was called before initialization");
        }
        return instance;
    }

    @TargetApi(14)
    public static void registerActivityLifecycleCallbacks(Application application) {
        if (!autoPageViewsEnabled && !autoSessionManagementEnabled && Build.VERSION.SDK_INT >= 14) {
            application.registerActivityLifecycleCallbacks(getInstance());
        }
    }

    @TargetApi(14)
    private static void unregisterActivityLifecycleCallbacks(Application application) {
        if ((autoPageViewsEnabled ^ autoSessionManagementEnabled) && Build.VERSION.SDK_INT >= 14) {
            application.unregisterActivityLifecycleCallbacks(getInstance());
        }
    }

    @TargetApi(14)
    public static void registerPageViewCallbacks(Application application) {
        if (Build.VERSION.SDK_INT >= 14) {
            synchronized (LOCK) {
                registerActivityLifecycleCallbacks(application);
                autoPageViewsEnabled = true;
            }
        }
    }

    @TargetApi(14)
    public static void unregisterPageViewCallbacks(Application application) {
        if (Build.VERSION.SDK_INT >= 14) {
            synchronized (LOCK) {
                unregisterActivityLifecycleCallbacks(application);
                autoPageViewsEnabled = false;
            }
        }
    }

    @TargetApi(14)
    public static void registerSessionManagementCallbacks(Application application) {
        if (Build.VERSION.SDK_INT >= 14) {
            synchronized (LOCK) {
                registerActivityLifecycleCallbacks(application);
                autoSessionManagementEnabled = true;
            }
        }
    }

    @TargetApi(14)
    public static void unregisterSessionManagementCallbacks(Application application) {
        if (Build.VERSION.SDK_INT >= 14) {
            synchronized (LOCK) {
                unregisterActivityLifecycleCallbacks(application);
                autoSessionManagementEnabled = false;
            }
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        int count = this.activityCount.getAndIncrement();
        synchronized (LOCK) {
            if (count == 0) {
                if (autoSessionManagementEnabled) {
                    new CreateDataTask(CreateDataTask.DataType.NEW_SESSION).execute(new Void[0]);
                }
            }
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
        long now = getTime();
        long then = this.lastBackground.getAndSet(getTime());
        boolean shouldRenew = now - then >= this.config.getSessionIntervalMs();
        synchronized (LOCK) {
            if (autoSessionManagementEnabled && shouldRenew) {
                this.telemetryContext.renewSessionId();
                new CreateDataTask(CreateDataTask.DataType.NEW_SESSION).execute(new Void[0]);
            }
            if (autoPageViewsEnabled) {
                new CreateDataTask(CreateDataTask.DataType.PAGE_VIEW, activity.getClass().getName(), null, null).execute(new Void[0]);
            }
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
        this.lastBackground.set(getTime());
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
    }

    protected long getTime() {
        return new Date().getTime();
    }
}

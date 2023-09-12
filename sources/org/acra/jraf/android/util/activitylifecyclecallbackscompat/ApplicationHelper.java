package org.acra.jraf.android.util.activitylifecyclecallbackscompat;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;
/* loaded from: classes.dex */
public class ApplicationHelper {
    public static final boolean PRE_ICS;

    static {
        PRE_ICS = Build.VERSION.SDK_INT < 14;
    }

    public static void registerActivityLifecycleCallbacks(Application application, ActivityLifecycleCallbacksCompat callback) {
        if (PRE_ICS) {
            preIcsRegisterActivityLifecycleCallbacks(callback);
        } else {
            postIcsRegisterActivityLifecycleCallbacks(application, callback);
        }
    }

    private static void preIcsRegisterActivityLifecycleCallbacks(ActivityLifecycleCallbacksCompat callback) {
        MainLifecycleDispatcher.get().registerActivityLifecycleCallbacks(callback);
    }

    @TargetApi(14)
    private static void postIcsRegisterActivityLifecycleCallbacks(Application application, ActivityLifecycleCallbacksCompat callback) {
        application.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksWrapper(callback));
    }

    public void unregisterActivityLifecycleCallbacks(Application application, ActivityLifecycleCallbacksCompat callback) {
        if (PRE_ICS) {
            preIcsUnregisterActivityLifecycleCallbacks(callback);
        } else {
            postIcsUnregisterActivityLifecycleCallbacks(application, callback);
        }
    }

    private static void preIcsUnregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacksCompat callback) {
        MainLifecycleDispatcher.get().unregisterActivityLifecycleCallbacks(callback);
    }

    @TargetApi(14)
    private static void postIcsUnregisterActivityLifecycleCallbacks(Application application, ActivityLifecycleCallbacksCompat callback) {
        application.unregisterActivityLifecycleCallbacks(new ActivityLifecycleCallbacksWrapper(callback));
    }
}

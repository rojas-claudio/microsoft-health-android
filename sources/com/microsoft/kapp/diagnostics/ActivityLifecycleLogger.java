package com.microsoft.kapp.diagnostics;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import com.microsoft.kapp.R;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public class ActivityLifecycleLogger implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = ActivityLifecycleLogger.class.getSimpleName();
    private final Context mContext;

    public ActivityLifecycleLogger(Context context) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        this.mContext = context;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        log(R.string.activity_lifecycle_logger_on_activity_created, activity);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
        log(R.string.activity_lifecycle_logger_on_activity_destroyed, activity);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
        log(R.string.activity_lifecycle_logger_on_activity_paused, activity);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
        log(R.string.activity_lifecycle_logger_on_activity_resumed, activity);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        log(R.string.activity_lifecycle_logger_on_activity_save_instance_state, activity);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
        log(R.string.activity_lifecycle_logger_on_activity_started, activity);
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
        log(R.string.activity_lifecycle_logger_on_activity_stopped, activity);
    }

    private void log(int resourceId, Activity activity) {
        String format = this.mContext.getResources().getString(resourceId);
        KLog.d(TAG, format, activity.getClass());
    }
}

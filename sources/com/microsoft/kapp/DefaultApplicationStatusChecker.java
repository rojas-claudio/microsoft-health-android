package com.microsoft.kapp;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import java.util.List;
/* loaded from: classes.dex */
public class DefaultApplicationStatusChecker implements ApplicationStatusChecker {
    private Context mContext;

    public DefaultApplicationStatusChecker(Context context) {
        this.mContext = context;
    }

    @Override // com.microsoft.kapp.ApplicationStatusChecker
    public boolean isRunningInForeground() {
        ActivityManager activityManager = (ActivityManager) this.mContext.getSystemService("activity");
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            String topActivityPackageName = topActivity.getPackageName();
            String currentPackageName = this.mContext.getPackageName();
            if (topActivityPackageName.equals(currentPackageName)) {
                return true;
            }
        }
        return false;
    }
}

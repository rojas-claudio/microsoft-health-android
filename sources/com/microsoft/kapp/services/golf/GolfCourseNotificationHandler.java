package com.microsoft.kapp.services.golf;

import android.content.Intent;
/* loaded from: classes.dex */
public interface GolfCourseNotificationHandler {
    public static final String KEY_ACTION_STATUS = "Action";
    public static final String KEY_ERROR_ID = "ErrorId";
    public static final String KEY_GOLF_COURSE = "GolfCourse";
    public static final String OPERATION_SYNC = "Sync";

    void broadcastGolfCourse(int i, Intent intent);

    void notifyGolfCourseSyncError(String str, int i);

    void notifyGolfCourseSyncStarted(String str);

    void notifyGolfCourseSyncSuccess(String str);
}

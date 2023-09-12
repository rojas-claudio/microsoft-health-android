package com.microsoft.kapp.services.golf;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.microsoft.kapp.KApplicationGraph;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class GolfCourseNotificationHandlerImpl implements GolfCourseNotificationHandler {
    @Inject
    Context mApplicationContext;

    public GolfCourseNotificationHandlerImpl() {
        KApplicationGraph.getApplicationGraph().inject(this);
    }

    @Override // com.microsoft.kapp.services.golf.GolfCourseNotificationHandler
    public void notifyGolfCourseSyncStarted(String courseId) {
        Intent intent = getIntent(courseId);
        broadcastGolfCourse(0, intent);
    }

    @Override // com.microsoft.kapp.services.golf.GolfCourseNotificationHandler
    public void notifyGolfCourseSyncSuccess(String courseId) {
        Intent intent = getIntent(courseId);
        broadcastGolfCourse(1, intent);
    }

    @Override // com.microsoft.kapp.services.golf.GolfCourseNotificationHandler
    public void notifyGolfCourseSyncError(String courseId, int errorId) {
        Intent intent = getIntent(courseId);
        intent.putExtra("ErrorId", errorId);
        broadcastGolfCourse(2, intent);
    }

    @Override // com.microsoft.kapp.services.golf.GolfCourseNotificationHandler
    public void broadcastGolfCourse(int operationStatus, Intent intent) {
        intent.setAction("Sync");
        intent.putExtra("Action", operationStatus);
        LocalBroadcastManager.getInstance(this.mApplicationContext).sendBroadcast(intent);
    }

    private Intent getIntent(String courseId) {
        Intent data = new Intent();
        data.putExtra(GolfCourseNotificationHandler.KEY_GOLF_COURSE, courseId);
        return data;
    }
}

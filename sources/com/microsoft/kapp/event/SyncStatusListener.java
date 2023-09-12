package com.microsoft.kapp.event;
/* loaded from: classes.dex */
public interface SyncStatusListener {
    void onSyncCompleted(SyncCompletedEvent syncCompletedEvent);

    void onSyncPreComplete(SyncCompletedEvent syncCompletedEvent);

    void onSyncProgress(int i);

    void onSyncStarted(SyncStartedEvent syncStartedEvent);

    void onSyncTerminated();
}

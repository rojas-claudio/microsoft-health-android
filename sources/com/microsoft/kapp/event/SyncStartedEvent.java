package com.microsoft.kapp.event;

import com.microsoft.kapp.diagnostics.Validate;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class SyncStartedEvent {
    private final DateTime mSyncTime;

    public SyncStartedEvent(DateTime syncTime) {
        Validate.notNull(syncTime, "syncTime");
        this.mSyncTime = syncTime;
    }

    public DateTime getSyncTime() {
        return this.mSyncTime;
    }
}

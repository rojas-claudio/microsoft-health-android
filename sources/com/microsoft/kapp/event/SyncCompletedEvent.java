package com.microsoft.kapp.event;

import com.microsoft.kapp.device.CargoSyncResult;
import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public class SyncCompletedEvent {
    private final CargoSyncResult mSyncResult;

    public SyncCompletedEvent(CargoSyncResult syncResult) {
        Validate.notNull(syncResult, "syncResult");
        this.mSyncResult = syncResult;
    }

    public CargoSyncResult getSyncResult() {
        return this.mSyncResult;
    }
}

package com.microsoft.kapp.multidevice;

import com.microsoft.kapp.event.SyncStatusListener;
import com.microsoft.kapp.models.SyncState;
import com.microsoft.kapp.multidevice.MultiDeviceConstants;
import java.util.List;
/* loaded from: classes.dex */
public interface SyncManager {
    void addSyncStatusListener(SyncStatusListener syncStatusListener);

    void cancelSyncInProgress();

    int getCurrentSyncProgress();

    SyncState getState();

    void removeSyncStatusListener(SyncStatusListener syncStatusListener);

    KSyncResult sync(List<MultiDeviceConstants.DeviceType> list, boolean z, boolean z2);

    KSyncResult syncAll(boolean z, boolean z2);
}

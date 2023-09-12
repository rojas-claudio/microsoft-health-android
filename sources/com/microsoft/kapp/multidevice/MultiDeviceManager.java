package com.microsoft.kapp.multidevice;

import com.microsoft.band.client.CargoException;
import com.microsoft.kapp.event.SyncStatusListener;
import com.microsoft.kapp.models.SyncState;
import com.microsoft.kapp.multidevice.MultiDeviceConstants;
import com.microsoft.krestsdk.models.ConnectedDevice;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public interface MultiDeviceManager {
    void addSyncStatusListener(SyncStatusListener syncStatusListener);

    void cancelSyncInProgress();

    int getCurrentSyncProgress();

    DateTime getLastSyncTime();

    DateTime getLastSyncTime(MultiDeviceConstants.DeviceType deviceType);

    SyncState getState();

    boolean hasBand();

    boolean hasEverHadBand();

    boolean isPhoneRegistered();

    boolean isSyncInProgress();

    boolean isUsingV2Band() throws CargoException;

    void registerDevice(ConnectedDevice connectedDevice) throws CargoException;

    void removeSyncStatusListener(SyncStatusListener syncStatusListener);

    void startSync();

    void startSync(boolean z);

    void startSync(boolean z, boolean z2);

    void synchronizeAllDevices(boolean z, boolean z2);
}

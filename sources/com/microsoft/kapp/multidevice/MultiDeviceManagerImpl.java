package com.microsoft.kapp.multidevice;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.device.DeviceSettings;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.event.SyncStatusListener;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.SyncState;
import com.microsoft.kapp.multidevice.MultiDeviceConstants;
import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.services.background.SyncService;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.LogScenarioTags;
import com.microsoft.krestsdk.models.ConnectedDevice;
import java.util.Iterator;
import java.util.Locale;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class MultiDeviceManagerImpl implements MultiDeviceManager {
    public static final int CHUNK_DURATION_MINUTES = 1440;
    private CargoConnection mCargoConnection;
    private Context mContext;
    private SensorUtils mSensorUtils;
    private SettingsProvider mSettingsProvider;
    private SyncManager mSyncManager;

    public MultiDeviceManagerImpl(Context context, SettingsProvider settingsProvider, CargoConnection cargoConnection, SyncManager syncManager, SensorUtils sensorUtils) {
        this.mContext = context;
        this.mCargoConnection = cargoConnection;
        this.mSettingsProvider = settingsProvider;
        this.mSyncManager = syncManager;
        this.mSensorUtils = sensorUtils;
    }

    @Override // com.microsoft.kapp.multidevice.MultiDeviceManager
    public boolean hasBand() {
        if (this.mSettingsProvider.getUserProfile() != null) {
            return this.mSensorUtils.hasBand(this.mSettingsProvider.getUserProfile());
        }
        return false;
    }

    @Override // com.microsoft.kapp.multidevice.MultiDeviceManager
    public boolean isUsingV2Band() throws CargoException {
        return this.mCargoConnection.isUsingV2Band();
    }

    @Override // com.microsoft.kapp.multidevice.MultiDeviceManager
    public synchronized void startSync(boolean isPopUpErrorAllowed) {
        startSync(false, isPopUpErrorAllowed);
    }

    @Override // com.microsoft.kapp.multidevice.MultiDeviceManager
    public synchronized void startSync() {
        startSync(false, true);
    }

    @Override // com.microsoft.kapp.multidevice.MultiDeviceManager
    public synchronized void startSync(boolean isBackground, boolean isPopUpErrorAllowed) {
        KLog.d(LogScenarioTags.Sync, "SYNC: Started sync. isBackground=[%b].", Boolean.valueOf(isBackground));
        if (!isSyncInProgress()) {
            if (isBackground) {
                Intent syncIntent = new Intent(this.mContext, SyncService.class);
                WakefulBroadcastReceiver.startWakefulService(this.mContext, syncIntent);
            } else {
                Intent intent = new Intent(this.mContext, SyncService.class);
                intent.putExtra(Constants.SYNC_INTENT_SOURCE, true);
                intent.putExtra(Constants.SYNC_POPUP_ERROR, isPopUpErrorAllowed);
                this.mContext.startService(intent);
            }
        }
    }

    @Override // com.microsoft.kapp.multidevice.MultiDeviceManager
    public boolean isSyncInProgress() {
        return this.mSyncManager.getState() != SyncState.NOT_STARTED;
    }

    @Override // com.microsoft.kapp.multidevice.MultiDeviceManager
    public void synchronizeAllDevices(boolean isBackground, boolean isPopUpErrorAllowed) {
        this.mSyncManager.syncAll(isBackground, isPopUpErrorAllowed);
    }

    @Override // com.microsoft.kapp.multidevice.MultiDeviceManager
    public DateTime getLastSyncTime() {
        DateTime lastSyncTime = null;
        MultiDeviceConstants.DeviceType[] arr$ = MultiDeviceConstants.DeviceType.values();
        for (MultiDeviceConstants.DeviceType type : arr$) {
            DateTime deviceLastSyncTime = getLastSyncTime(type);
            if (lastSyncTime == null || lastSyncTime.isAfter(deviceLastSyncTime)) {
                lastSyncTime = deviceLastSyncTime;
            }
        }
        return lastSyncTime;
    }

    @Override // com.microsoft.kapp.multidevice.MultiDeviceManager
    public DateTime getLastSyncTime(MultiDeviceConstants.DeviceType deviceType) {
        return this.mSettingsProvider.getLastSyncTimeForDevice(deviceType);
    }

    @Override // com.microsoft.kapp.multidevice.MultiDeviceManager
    public void addSyncStatusListener(SyncStatusListener listener) {
        this.mSyncManager.addSyncStatusListener(listener);
    }

    @Override // com.microsoft.kapp.multidevice.MultiDeviceManager
    public void removeSyncStatusListener(SyncStatusListener listener) {
        this.mSyncManager.removeSyncStatusListener(listener);
    }

    @Override // com.microsoft.kapp.multidevice.MultiDeviceManager
    public SyncState getState() {
        return this.mSyncManager.getState();
    }

    @Override // com.microsoft.kapp.multidevice.MultiDeviceManager
    public int getCurrentSyncProgress() {
        if (this.mSyncManager == null || getState() != SyncState.IN_PROGRESS) {
            return 0;
        }
        return this.mSyncManager.getCurrentSyncProgress();
    }

    @Override // com.microsoft.kapp.multidevice.MultiDeviceManager
    public void registerDevice(ConnectedDevice device) throws CargoException {
        this.mCargoConnection.registerDevice(new DeviceSettings(device.getName(), device.getUUID().toString().toUpperCase(Locale.US), false));
    }

    @Override // com.microsoft.kapp.multidevice.MultiDeviceManager
    public boolean isPhoneRegistered() {
        return this.mSensorUtils.isPhoneConnected(this.mSettingsProvider.getUserProfile());
    }

    @Override // com.microsoft.kapp.multidevice.MultiDeviceManager
    public boolean hasEverHadBand() {
        CargoUserProfile profile = this.mSettingsProvider.getUserProfile();
        if (profile != null && profile.getDevices() != null) {
            Iterator i$ = profile.getDevices().iterator();
            while (i$.hasNext()) {
                DeviceSettings device = i$.next();
                if (!device.getDeviceId().equals("00000000-0000-0000-0000-000000000000") && device.getIsBand()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // com.microsoft.kapp.multidevice.MultiDeviceManager
    public void cancelSyncInProgress() {
        this.mSyncManager.cancelSyncInProgress();
    }
}

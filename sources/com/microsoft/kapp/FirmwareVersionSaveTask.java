package com.microsoft.kapp;

import com.microsoft.band.cloud.CargoFirmwareUpdateInfo;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.SettingsProvider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes.dex */
public class FirmwareVersionSaveTask {
    private static final String TAG = FirmwareVersionSaveTask.class.getSimpleName();
    private final CargoConnection mCargoConnection;
    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private final SettingsProvider mSettingsProvider;

    public FirmwareVersionSaveTask(CargoConnection cargoConnection, SettingsProvider settingsProvider) {
        this.mCargoConnection = cargoConnection;
        this.mSettingsProvider = settingsProvider;
    }

    public void saveFirmwareVersionToSettingsAsync() {
        this.mExecutor.execute(new Runnable() { // from class: com.microsoft.kapp.FirmwareVersionSaveTask.1
            @Override // java.lang.Runnable
            public void run() {
                if (FirmwareVersionSaveTask.this.mSettingsProvider != null && FirmwareVersionSaveTask.this.mCargoConnection != null) {
                    try {
                        CargoFirmwareUpdateInfo firmwareUpdateInfo = FirmwareVersionSaveTask.this.mCargoConnection.getLatestAvailableFirmwareVersion();
                        FirmwareVersionSaveTask.this.mSettingsProvider.setDeviceFirmwareVersion(firmwareUpdateInfo.getCurrentVersion());
                    } catch (Exception e) {
                        KLog.w(FirmwareVersionSaveTask.TAG, "saving firmware version to settings failed", e);
                    }
                }
            }
        });
    }
}

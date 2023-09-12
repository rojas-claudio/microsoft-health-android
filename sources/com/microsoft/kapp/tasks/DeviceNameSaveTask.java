package com.microsoft.kapp.tasks;

import android.os.AsyncTask;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.SettingsProvider;
/* loaded from: classes.dex */
public class DeviceNameSaveTask extends AsyncTask<Void, Void, Boolean> {
    private static final String TAG = DeviceNameSaveTask.class.getSimpleName();
    private CargoConnection mCargoConnection;
    private String mDeviceName;

    public DeviceNameSaveTask(CargoConnection cargoConnection, SettingsProvider settingsProvider, String deviceName) {
        Validate.notNull(cargoConnection, "cargoConnection");
        Validate.notNull(settingsProvider, "settingsProvider");
        Validate.notNull(deviceName, "deviceName");
        this.mDeviceName = deviceName;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Boolean doInBackground(Void... params) {
        KLog.v(TAG, "Executing PersonalizationSave Task");
        try {
            this.mCargoConnection.setDeviceName(this.mDeviceName);
            return true;
        } catch (Exception exception) {
            KLog.w(TAG, "error saving device name", exception);
            return false;
        }
    }
}

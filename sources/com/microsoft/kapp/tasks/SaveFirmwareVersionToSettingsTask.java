package com.microsoft.kapp.tasks;

import android.os.AsyncTask;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.SettingsProvider;
/* loaded from: classes.dex */
public class SaveFirmwareVersionToSettingsTask extends AsyncTask<Void, Void, String> {
    private final String TASK_TAG = SaveFirmwareVersionToSettingsTask.class.getSimpleName();
    private CargoConnection mCargoConnection;
    private SettingsProvider mSettingsProvider;

    public SaveFirmwareVersionToSettingsTask(SettingsProvider settingsProvider, CargoConnection cargoConnection) {
        this.mSettingsProvider = settingsProvider;
        this.mCargoConnection = cargoConnection;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public String doInBackground(Void... params) {
        try {
            return this.mCargoConnection.getDeviceFirmwareVersion().toString();
        } catch (Exception exception) {
            KLog.w(this.TASK_TAG, "error retrieving firmware version", exception);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(String version) {
        if (this.mSettingsProvider != null) {
            this.mSettingsProvider.setDeviceFirmwareVersion(version);
        }
    }
}

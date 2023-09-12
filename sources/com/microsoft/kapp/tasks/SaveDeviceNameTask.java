package com.microsoft.kapp.tasks;

import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.ScopedAsyncTask;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
/* loaded from: classes.dex */
public abstract class SaveDeviceNameTask extends ScopedAsyncTask<Void, Void, Boolean> {
    private static final String TAG = SaveDeviceNameTask.class.getSimpleName();
    private CargoConnection mCargoConnection;
    private String mDeviceName;

    public SaveDeviceNameTask(CargoConnection cargoConnection, String deviceName, OnTaskListener onTaskListener) {
        super(onTaskListener);
        Validate.notNull(cargoConnection, "cargoConnection");
        Validate.notNull(deviceName, "deviceName");
        this.mCargoConnection = cargoConnection;
        this.mDeviceName = deviceName;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.ScopedAsyncTask
    public Boolean doInBackground(Void... params) {
        KLog.v(TAG, "Executing SaveDeviceName Task");
        try {
            this.mCargoConnection.setDeviceName(this.mDeviceName);
            return true;
        } catch (Exception exception) {
            setException(exception);
            KLog.w(TAG, "setDeviceName() failed", exception);
            return false;
        }
    }
}

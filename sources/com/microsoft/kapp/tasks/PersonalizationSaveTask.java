package com.microsoft.kapp.tasks;

import android.graphics.Bitmap;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.ScopedAsyncTask;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.personalization.DeviceTheme;
/* loaded from: classes.dex */
public abstract class PersonalizationSaveTask extends ScopedAsyncTask<Void, Void, Boolean> {
    private CargoConnection mCargoConnection;
    private Bitmap mDeviceImage;
    private DeviceTheme mDeviceTheme;
    private Exception mException;
    private int mWallpaperPatternId;

    public PersonalizationSaveTask(CargoConnection cargoConnection, DeviceTheme deviceTheme, Bitmap deviceImage, int wallpaperPatternId, OnTaskListener onTaskListener) {
        super(onTaskListener);
        Validate.notNull(cargoConnection, "cargoConnection");
        this.mCargoConnection = cargoConnection;
        this.mDeviceTheme = deviceTheme;
        this.mDeviceImage = deviceImage;
        this.mWallpaperPatternId = wallpaperPatternId;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.ScopedAsyncTask
    public Boolean doInBackground(Void... params) {
        KLog.v(this.TAG, "Executing PersonalizationSave Task");
        try {
            this.mCargoConnection.personalizeDevice(this.mDeviceTheme, this.mDeviceImage, this.mWallpaperPatternId);
            return true;
        } catch (Exception exception) {
            KLog.w(this.TAG, "error saving device personalization", exception);
            this.mException = exception;
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.ScopedAsyncTask
    public Exception getException() {
        return this.mException;
    }
}

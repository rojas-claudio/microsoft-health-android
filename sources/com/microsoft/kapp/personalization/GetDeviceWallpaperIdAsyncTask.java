package com.microsoft.kapp.personalization;

import com.microsoft.band.client.CargoException;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.ScopedAsyncTask;
import com.microsoft.kapp.services.SettingsProvider;
/* loaded from: classes.dex */
public class GetDeviceWallpaperIdAsyncTask extends ScopedAsyncTask<Void, Void, Integer> {
    private Callback<Integer> mCallback;
    private CargoConnection mCargoConnection;
    private SettingsProvider mSettingsProvider;

    public GetDeviceWallpaperIdAsyncTask(CargoConnection cargoConnection, SettingsProvider settingsProvider, Callback<Integer> callback, OnTaskListener onTaskListener) {
        super(onTaskListener);
        this.mCargoConnection = cargoConnection;
        this.mSettingsProvider = settingsProvider;
        this.mCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.ScopedAsyncTask
    public Integer doInBackground(Void... params) {
        try {
            int wallpaperPatternId = this.mCargoConnection.getDeviceWallpaperId();
            this.mSettingsProvider.setCurrentWallpaperId(wallpaperPatternId);
            return Integer.valueOf(wallpaperPatternId);
        } catch (CargoException exception) {
            setException(exception);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.ScopedAsyncTask
    public void onPostExecute(Integer result) {
        Exception exception = getException();
        if (exception == null && result != null) {
            this.mCallback.callback(result);
        } else {
            this.mCallback.onError(exception);
        }
    }
}

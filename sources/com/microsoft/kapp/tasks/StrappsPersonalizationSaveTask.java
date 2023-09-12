package com.microsoft.kapp.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.microsoft.band.client.CargoException;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.strapp.StrappStateCollection;
import com.microsoft.kapp.personalization.DeviceTheme;
import com.microsoft.kapp.personalization.PersonalizationManager;
import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.models.BandVersion;
/* loaded from: classes.dex */
public class StrappsPersonalizationSaveTask extends StrappsSaveTask {
    public StrappsPersonalizationSaveTask(CargoConnection cargoConnection, StrappStateCollection strapps, SettingsProvider settingsProvider, PersonalizationManagerFactory personalizationManagerFactory, Context applicationContext, OnTaskListener onTaskListener) {
        super(cargoConnection, strapps, settingsProvider, personalizationManagerFactory, null, applicationContext, onTaskListener);
    }

    @Override // com.microsoft.kapp.tasks.StrappsSaveTask
    protected void doInBackgroundFinish() throws CargoException {
        try {
            super.doInBackgroundFinish();
            if (getCargoConnection().getBandVersion() == BandVersion.CARGO) {
                PersonalizationManager personalizationManager = this.mPersonalizationManagerFactory.getPersonalizationManager(BandVersion.CARGO);
                int wallpaperPatternId = personalizationManager.getDefaultWallpaper();
                DeviceTheme deviceTheme = personalizationManager.getThemeById(wallpaperPatternId);
                int wallpaperResId = deviceTheme.getWallpaperById(wallpaperPatternId).getResId();
                Bitmap deviceImage = BitmapFactory.decodeStream(this.mApplicationContext.getResources().openRawResource(wallpaperResId));
                getCargoConnection().personalizeDevice(this.mNewStartStrip, deviceTheme, deviceImage, wallpaperPatternId);
            }
        } catch (Exception ex) {
            setException(ex);
            KLog.w(this.TAG, "Posting strapp failed!", ex);
        }
    }
}

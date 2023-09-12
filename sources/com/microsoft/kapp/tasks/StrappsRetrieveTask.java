package com.microsoft.kapp.tasks;

import com.microsoft.band.device.CargoStrapp;
import com.microsoft.band.device.StartStrip;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.strappsettings.StrappSettingsManager;
import com.microsoft.krestsdk.models.BandVersion;
import java.util.ArrayList;
import java.util.UUID;
/* loaded from: classes.dex */
public class StrappsRetrieveTask extends StrappsTask<StartStrip> {
    BandVersion mBandVersion;
    SettingsProvider mSettingsProvider;
    StrappSettingsManager mStrappSettingsManager;

    public StrappsRetrieveTask(CargoConnection cargoConnection, OnTaskListener onTaskListener, SettingsProvider settingsProvider, StrappSettingsManager strappSettingsManager) {
        super(cargoConnection, onTaskListener);
        this.mBandVersion = BandVersion.CARGO;
        this.mSettingsProvider = settingsProvider;
        this.mStrappSettingsManager = strappSettingsManager;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.ScopedAsyncTask
    public final StartStrip doInBackground(Void... params) {
        StartStrip result = null;
        try {
            this.mBandVersion = getCargoConnection().getBandVersion();
            result = getCargoConnection().getStartStrip();
            ArrayList<UUID> uuidSet = new ArrayList<>();
            for (CargoStrapp strapp : result.getAppList()) {
                uuidSet.add(strapp.getStrappData().getAppId());
            }
            if (this.mStrappSettingsManager != null) {
                this.mStrappSettingsManager.setStrappStatesRetrievedFromBand(result.getAppList());
            }
            this.mSettingsProvider.setUUIDsOnDevice(uuidSet);
            this.mSettingsProvider.setNumberOfAllowedStrapps(getCargoConnection().getNumberOfStrappsAllowedOnDevice());
        } catch (Exception ex) {
            setException(ex);
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public BandVersion getBandVersion() {
        return this.mBandVersion;
    }
}

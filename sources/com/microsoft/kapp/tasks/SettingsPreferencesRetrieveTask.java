package com.microsoft.kapp.tasks;

import android.os.AsyncTask;
import com.microsoft.band.client.CargoException;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.UserProfileFetcher;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.SettingsPreferences;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.models.BandVersion;
/* loaded from: classes.dex */
public class SettingsPreferencesRetrieveTask extends AsyncTask<Void, Void, SettingsPreferences> {
    private static final String TAG = SettingsPreferencesRetrieveTask.class.getSimpleName();
    private BandVersion mBandVersion = BandVersion.UNKNOWN;
    private ActivityScopedCallback<Object> mCallback;
    private CargoConnection mCargoConnection;
    private final SettingsProvider mSettingsProvider;
    private final UserProfileFetcher mUserProfileFetcher;

    public SettingsPreferencesRetrieveTask(SettingsProvider settingsProvider, UserProfileFetcher userProfileFetcher, CargoConnection cargoConnection, ActivityScopedCallback<Object> callback) {
        Validate.notNull(settingsProvider, "settingsProvider");
        Validate.notNull(userProfileFetcher, "userProfileFetcher");
        Validate.notNull(callback, "callback");
        this.mSettingsProvider = settingsProvider;
        this.mUserProfileFetcher = userProfileFetcher;
        this.mCargoConnection = cargoConnection;
        this.mCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public SettingsPreferences doInBackground(Void... params) {
        this.mUserProfileFetcher.updateLocallyStoredValues();
        try {
            this.mBandVersion = this.mCargoConnection.getBandVersion();
        } catch (CargoException e) {
            KLog.v(TAG, "Couldn't load band version");
        }
        SettingsPreferences prefs = new SettingsPreferences();
        prefs.setIsWeightMetric(this.mSettingsProvider.isWeightMetric());
        prefs.setIsTemperatureMetric(this.mSettingsProvider.isTemperatureMetric());
        prefs.setIsDistanceHeightMetric(this.mSettingsProvider.isDistanceHeightMetric());
        prefs.setShouldSendDataOverWiFiOnly(this.mSettingsProvider.shouldSendDataOverWiFiOnly());
        return prefs;
    }

    protected BandVersion getBandVersion() {
        return this.mBandVersion;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(SettingsPreferences settingsPreferences) {
        this.mCallback.callback(new Object[]{this.mBandVersion, settingsPreferences});
    }
}

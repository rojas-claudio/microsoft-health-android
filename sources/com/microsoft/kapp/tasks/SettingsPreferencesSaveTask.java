package com.microsoft.kapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.SettingsPreferences;
import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.LocaleProvider;
/* loaded from: classes.dex */
public class SettingsPreferencesSaveTask extends AsyncTask<Void, Void, Integer> {
    public static final int RESULT_ERROR_CLOUD = 1003;
    public static final int RESULT_ERROR_DEVICE = 1001;
    public static final int RESULT_ERROR_SINGLE_DEVICE = 1002;
    public static final int RESULT_SUCCESS = 1000;
    private static final String TAG = SettingsPreferencesSaveTask.class.getSimpleName();
    private ActivityScopedCallback<Integer> mCallback;
    private CargoConnection mCargoConnection;
    private Context mContext;
    private SensorUtils mSensorUtils;
    private SettingsPreferences mSettingsPreferences;
    private SettingsProvider mSettingsProvider;

    public SettingsPreferencesSaveTask(SettingsProvider settingsProvider, SettingsPreferences settingsPreferences, CargoConnection cargoConnection, SensorUtils sensorUtils, Context context, ActivityScopedCallback<Integer> callback) {
        Validate.notNull(settingsProvider, "settingsProvider");
        Validate.notNull(settingsPreferences, "settingsPreferences");
        Validate.notNull(cargoConnection, "cargoConnection");
        Validate.notNull(callback, "callback");
        this.mSettingsProvider = settingsProvider;
        this.mSettingsPreferences = settingsPreferences;
        this.mCargoConnection = cargoConnection;
        this.mSensorUtils = sensorUtils;
        this.mContext = context;
        this.mCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Integer doInBackground(Void... params) {
        CargoUserProfile profile = this.mSettingsProvider.getUserProfile();
        profile.setDisplayDistanceHeightMetric(this.mSettingsPreferences.isDistanceHeightMetric());
        profile.setDisplayTemperatureMetric(this.mSettingsPreferences.isTemperatureMetric());
        profile.setDisplayWeightMetric(this.mSettingsPreferences.isWeightMetric());
        boolean shouldSendDataOverWiFiOnly = this.mSettingsPreferences.shouldSendDataOverWiFiOnly();
        if (this.mSettingsPreferences.getUserSelectedBandRegion() != null) {
            profile.setLocaleSettings(LocaleProvider.getLocaleSettings(this.mContext, this.mSettingsPreferences.getUserSelectedBandRegion()));
        }
        int result = RESULT_ERROR_CLOUD;
        try {
            this.mCargoConnection.saveUserCloudProfile(profile, System.currentTimeMillis());
            this.mSettingsProvider.setIsWeightMetric(this.mSettingsPreferences.isWeightMetric());
            this.mSettingsProvider.setIsTemperatureMetric(this.mSettingsPreferences.isTemperatureMetric());
            this.mSettingsProvider.setIsDistanceHeightMetric(this.mSettingsPreferences.isDistanceHeightMetric());
            this.mSettingsProvider.setShouldSendDataOverWiFiOnly(shouldSendDataOverWiFiOnly);
            this.mSettingsProvider.setBandRegionSettings(this.mSettingsPreferences.getUserSelectedBandRegion());
            if (this.mSensorUtils.hasBand(this.mSettingsProvider.getUserProfile())) {
                result = 1001;
                this.mCargoConnection.checkSingleDeviceEnforcement();
                this.mCargoConnection.saveUserBandProfile(profile, System.currentTimeMillis());
            }
            result = 1000;
        } catch (Exception e) {
            KLog.w(TAG, "Caught exception in SettingsPreferencesSaveTask.doInBackground");
        }
        return Integer.valueOf(result);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Integer result) {
        this.mCallback.callback(result);
    }
}

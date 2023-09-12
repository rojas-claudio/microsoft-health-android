package com.microsoft.kapp;

import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.auth.credentials.KCredential;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes.dex */
public class UserProfileFetcher {
    private static final String TAG = UserProfileFetcher.class.getSimpleName();
    private final CargoConnection mCargoConnection;
    private final CredentialsManager mCredentialsManager;
    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private final SettingsProvider mSettingsProvider;

    public UserProfileFetcher(CargoConnection cargoConnection, CredentialsManager credentialsManager, SettingsProvider settingsProvider) {
        this.mCargoConnection = cargoConnection;
        this.mCredentialsManager = credentialsManager;
        this.mSettingsProvider = settingsProvider;
    }

    public void updateLocallyStoredValuesAsync() {
        this.mExecutor.execute(new Runnable() { // from class: com.microsoft.kapp.UserProfileFetcher.1
            @Override // java.lang.Runnable
            public void run() {
                UserProfileFetcher.this.updateLocallyStoredValues();
            }
        });
    }

    public void updateLocallyStoredValues() {
        KCredential credentials = this.mCredentialsManager.getCredentials();
        if (credentials != null && this.mCargoConnection != null) {
            try {
                CargoUserProfile profile = this.mCargoConnection.getUserCloudProfile(credentials.getEndPoint(), credentials.getAccessToken(), credentials.getFUSEndPoint());
                if (profile != null) {
                    String url = profile.getThirdPartyPartnersPortalEndpoint();
                    String fusEndPoint = credentials.getFUSEndPoint();
                    this.mSettingsProvider.setThirdPartyPartnersPortalEndpoint(url);
                    this.mSettingsProvider.setFUSEndPoint(fusEndPoint);
                    this.mSettingsProvider.setUserProfile(profile);
                    this.mSettingsProvider.setIsWeightMetric(profile.isDisplayWeightMetric());
                    this.mSettingsProvider.setIsTemperatureMetric(profile.isDisplayTemperatureMetric());
                    this.mSettingsProvider.setIsDistanceHeightMetric(profile.isDisplayDistanceHeightMetric());
                }
            } catch (Exception e) {
                KLog.w(TAG, "getUserCloudProfile() failed", e);
            }
        }
    }
}

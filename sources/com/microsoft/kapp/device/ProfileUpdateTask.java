package com.microsoft.kapp.device;

import android.text.TextUtils;
import com.google.gson.JsonObject;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.cloud.UserProfileInfo;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.ScopedAsyncTask;
import com.microsoft.kapp.UserProfileFetcher;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.auth.MsaAuth;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
/* loaded from: classes.dex */
public class ProfileUpdateTask extends ScopedAsyncTask<Void, Void, CargoUserProfile> {
    MsaAuth mAuthService;
    CargoConnection mCargoConnection;
    ProfileUpdateCallback mFetcher;
    SettingsProvider mSettings;
    UserProfileFetcher mUserProfileFetcher;

    public ProfileUpdateTask(CargoConnection cargoConnection, SettingsProvider settings, UserProfileFetcher fetcher, MsaAuth authService, ProfileUpdateCallback fetchListener, OnTaskListener onTaskListener) {
        super(onTaskListener);
        this.mCargoConnection = cargoConnection;
        this.mSettings = settings;
        this.mUserProfileFetcher = fetcher;
        this.mFetcher = fetchListener;
        this.mAuthService = authService;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.ScopedAsyncTask
    public CargoUserProfile doInBackground(Void... values) {
        this.mUserProfileFetcher.updateLocallyStoredValues();
        return this.mSettings.getUserProfile();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.ScopedAsyncTask
    public void onPostExecute(CargoUserProfile profile) {
        if (profile == null || TextUtils.isEmpty(profile.getFirstName())) {
            this.mAuthService.getUserProfile(this.mSettings.getAuthUrl(), new Callback<JsonObject>() { // from class: com.microsoft.kapp.device.ProfileUpdateTask.1
                @Override // com.microsoft.kapp.Callback
                public void onError(Exception ex) {
                    ProfileUpdateTask.this.notifyResult(null);
                }

                @Override // com.microsoft.kapp.Callback
                public void callback(JsonObject result) {
                    if (result == null || !result.has("FirstName") || !result.has("BirthDate") || !result.has("ZipCode")) {
                        ProfileUpdateTask.this.setException(new CargoException("Bad profile data received from the cloud!", BandServiceMessage.Response.SERVICE_CLOUD_DATA_ERROR));
                        ProfileUpdateTask.this.notifyResult(null);
                        return;
                    }
                    CargoUserProfile resultProfile = new CargoUserProfile();
                    resultProfile.setFirstName(result.get("FirstName").getAsString());
                    LocalDateTime birthDateTime = LocalDateTime.parse(result.get("BirthDate").getAsString());
                    resultProfile.setBirthdate(new DateTime(birthDateTime.year().get(), birthDateTime.monthOfYear().get(), birthDateTime.dayOfMonth().get(), 0, 0));
                    resultProfile.setZipCode(result.get("ZipCode").getAsString());
                    resultProfile.setGender(UserProfileInfo.Gender.valueOf(result.has(TelemetryConstants.Events.OobeComplete.Dimensions.GENDER) ? result.get(TelemetryConstants.Events.OobeComplete.Dimensions.GENDER).getAsInt() : 0));
                    resultProfile.setOobeIsComplete(false);
                    ProfileUpdateTask.this.notifyResult(resultProfile);
                }
            });
        } else {
            notifyResult(profile);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyResult(CargoUserProfile cargoUserProfile) {
        if (this.mFetcher != null && this.mFetcher != null) {
            this.mFetcher.onProfileRetrieved(cargoUserProfile);
        }
    }
}

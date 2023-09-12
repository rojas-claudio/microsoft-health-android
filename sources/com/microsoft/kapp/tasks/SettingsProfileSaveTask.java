package com.microsoft.kapp.tasks;

import android.content.Context;
import com.microsoft.band.client.CargoException;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.ScopedAsyncTask;
import com.microsoft.kapp.UserProfileFetcher;
import com.microsoft.kapp.device.CargoUserProfile;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.LocaleProvider;
import com.microsoft.krestsdk.models.BandVersion;
/* loaded from: classes.dex */
public abstract class SettingsProfileSaveTask extends ScopedAsyncTask<Void, Void, Integer> {
    public static final int RESULT_ERROR_SAVING_CLOUD = 9000;
    public static final int RESULT_ERROR_SAVING_DEVICE = 9001;
    public static final int RESULT_ERROR_SINGLE_DEVICE = 9002;
    public static final int RESULT_SUCCESS = 9003;
    private CargoConnection mCargoConnection;
    private Context mContext;
    private Boolean mIsOobeModeActive;
    private SettingsProvider mSettingsProvider;
    private Boolean mShouldConnectBandToProfile;
    private CargoUserProfile mUserProfile;
    private UserProfileFetcher mUserProfileFetcher;

    public SettingsProfileSaveTask(CargoConnection cargoConnection, SettingsProvider settingsProvider, CargoUserProfile userProfile, UserProfileFetcher userProfileFetcher, OnTaskListener onTaskListener, Boolean isOobeModeActive, Boolean shouldConnectBandToProfile, Context context) {
        super(onTaskListener);
        Validate.notNull(cargoConnection, "cargoConnection");
        Validate.notNull(settingsProvider, "settingsProvider");
        Validate.notNull(userProfile, "userProfile");
        Validate.notNull(userProfileFetcher, "userProfileSaveTask");
        Validate.notNull(isOobeModeActive, "isOobeModeActive");
        this.mCargoConnection = cargoConnection;
        this.mSettingsProvider = settingsProvider;
        this.mUserProfile = userProfile;
        this.mUserProfileFetcher = userProfileFetcher;
        this.mIsOobeModeActive = isOobeModeActive;
        this.mContext = context;
        this.mShouldConnectBandToProfile = shouldConnectBandToProfile;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.ScopedAsyncTask
    public Integer doInBackground(Void... params) {
        BandVersion bandVersion;
        long millis;
        KLog.v(this.TAG, "Executing SaveProfileTask");
        int result = RESULT_SUCCESS;
        try {
            bandVersion = BandVersion.UNKNOWN;
            millis = System.currentTimeMillis();
        } catch (CargoException exception) {
            setException(exception);
            KLog.w(this.TAG, "error saving user profile: result=" + result, exception);
            byte responseCategory = exception.getResponse().getCategory();
            if (responseCategory == 4) {
                result = RESULT_ERROR_SAVING_CLOUD;
            } else {
                result = RESULT_ERROR_SAVING_DEVICE;
            }
        }
        if (this.mContext != null && !CommonUtils.isNetworkAvailable(this.mContext)) {
            return Integer.valueOf((int) RESULT_SUCCESS);
        }
        if (this.mShouldConnectBandToProfile.booleanValue()) {
            try {
                this.mCargoConnection.getBandVersion();
            } catch (Exception ex) {
                result = RESULT_ERROR_SAVING_DEVICE;
                KLog.w(this.TAG, "Error retrieveing band version!", ex);
            }
            if (this.mIsOobeModeActive.booleanValue() && bandVersion == BandVersion.NEON) {
                this.mUserProfile.setLocaleSettings(LocaleProvider.getLocaleSettings(this.mContext, this.mSettingsProvider.getBandRegionSettings()));
            }
        }
        boolean saveSucceeded = this.mCargoConnection.saveUserCloudProfile(this.mUserProfile, millis);
        if (saveSucceeded) {
            if (this.mShouldConnectBandToProfile.booleanValue()) {
                if (this.mIsOobeModeActive.booleanValue()) {
                    this.mCargoConnection.linkDeviceToProfile();
                } else {
                    this.mCargoConnection.importUserProfile();
                }
            }
            try {
                this.mCargoConnection.setCloudProfileTelemetryFlag(this.mSettingsProvider.isTelemetryEnabled());
            } catch (CargoException exception2) {
                setException(exception2);
                KLog.w(this.TAG, "error saving telemetry flag to cloud", exception2);
                result = RESULT_ERROR_SAVING_CLOUD;
            }
            if (this.mShouldConnectBandToProfile.booleanValue()) {
                try {
                    this.mCargoConnection.setTelemetryFlag(this.mSettingsProvider.isTelemetryEnabled());
                } catch (CargoException exception3) {
                    setException(exception3);
                    KLog.w(this.TAG, "error saving telemetry flag", exception3);
                    result = RESULT_ERROR_SAVING_DEVICE;
                }
            }
            this.mCargoConnection.checkSingleDeviceEnforcement();
            if (!this.mCargoConnection.saveUserBandProfile(this.mUserProfile, millis)) {
                result = RESULT_ERROR_SAVING_DEVICE;
            }
            this.mUserProfileFetcher.updateLocallyStoredValues();
            return Integer.valueOf(result);
        }
        return Integer.valueOf((int) RESULT_ERROR_SAVING_CLOUD);
    }
}

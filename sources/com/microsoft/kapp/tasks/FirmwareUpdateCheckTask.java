package com.microsoft.kapp.tasks;

import android.content.Context;
import android.os.PowerManager;
import com.microsoft.band.OOBEStage;
import com.microsoft.band.cloud.CargoFirmwareUpdateInfo;
import com.microsoft.band.internal.device.DeviceInfo;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.ScopedAsyncTask;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.FirmwareUpdateProgress;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.version.CheckedFirmwareUpdateInfo;
import com.microsoft.krestsdk.models.BandVersion;
/* loaded from: classes.dex */
public class FirmwareUpdateCheckTask extends ScopedAsyncTask<Void, FirmwareUpdateProgress, Integer> {
    private static final int RESULT_CHECK_COMPLETE = 5100;
    public static final int RESULT_ERROR_CLOUD = 5001;
    public static final int RESULT_ERROR_DEVICE = 5000;
    public static final int RESULT_ERROR_DEVICE_OR_CLOUD = 5002;
    private static final String TAG = FirmwareUpdateCheckTask.class.getSimpleName();
    private BandVersion mBandVersion;
    private final FirmwareUpdateNeededCallback mCallback;
    private final CargoConnection mCargoConnection;
    private Context mContext;
    private CheckedFirmwareUpdateInfo mFirmwareUpdateInfo;
    private boolean mIsOobe;
    private boolean mNeedsUpdated;
    private SettingsProvider mSettingsProvider;
    private final PowerManager.WakeLock mWakeLock;

    /* loaded from: classes.dex */
    public interface FirmwareUpdateNeededCallback {
        void onFirmwareUpdateCheckFailed(int i);

        void onFirmwareUpdateNeeded(CheckedFirmwareUpdateInfo checkedFirmwareUpdateInfo, BandVersion bandVersion);

        void onFirmwareUpdateNotNeeded(CheckedFirmwareUpdateInfo checkedFirmwareUpdateInfo);
    }

    public FirmwareUpdateCheckTask(Context context, FirmwareUpdateNeededCallback callback, SettingsProvider settingsProvider, CargoConnection cargoConnection, boolean isOobe, OnTaskListener listener) {
        super(listener);
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Validate.notNull(callback, "callback");
        Validate.notNull(cargoConnection, "cargoConnection");
        this.mContext = context;
        this.mCallback = callback;
        this.mCargoConnection = cargoConnection;
        this.mSettingsProvider = settingsProvider;
        this.mIsOobe = isOobe;
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        this.mWakeLock = powerManager.newWakeLock(536870913, TAG);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.ScopedAsyncTask
    public void onPreExecute() {
        this.mWakeLock.acquire();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.ScopedAsyncTask
    public Integer doInBackground(Void... params) {
        DeviceInfo deviceInfo;
        Integer result = Integer.valueOf((int) RESULT_CHECK_COMPLETE);
        try {
            if (!this.mIsOobe) {
                this.mCargoConnection.checkSingleDeviceEnforcement();
            }
            deviceInfo = this.mCargoConnection.getConnectedDevice();
        } catch (Exception ex) {
            KLog.w(TAG, "Exception while checking for firmware update.", ex);
        }
        if (deviceInfo == null || !this.mCargoConnection.isDeviceAvailable(deviceInfo)) {
            return 5000;
        }
        this.mBandVersion = this.mCargoConnection.getBandVersion();
        Integer result2 = Integer.valueOf((int) RESULT_ERROR_CLOUD);
        if (CommonUtils.isNetworkAvailable(this.mContext)) {
            if (this.mIsOobe) {
                Integer.valueOf(5000);
                try {
                    this.mCargoConnection.navigateToScreen(OOBEStage.CHECKING_FOR_UPDATE);
                } catch (Exception e) {
                }
            }
            Integer.valueOf((int) RESULT_ERROR_DEVICE_OR_CLOUD);
            CargoFirmwareUpdateInfo firmwareUpdateInfo = this.mCargoConnection.getLatestAvailableFirmwareVersion();
            if (firmwareUpdateInfo != null) {
                this.mFirmwareUpdateInfo = new CheckedFirmwareUpdateInfo(firmwareUpdateInfo.isFirmwareUpdateAvailable(), firmwareUpdateInfo.isFirmwareUpdateOptional(), firmwareUpdateInfo.getFirmwareVersion());
                this.mSettingsProvider.setDeviceFirmwareVersion(firmwareUpdateInfo.getCurrentVersion());
            }
            this.mNeedsUpdated = this.mFirmwareUpdateInfo != null && this.mFirmwareUpdateInfo.isUpdateNeeded();
            if (this.mIsOobe && !this.mNeedsUpdated) {
                Integer.valueOf(5000);
                try {
                    this.mCargoConnection.navigateToScreen(OOBEStage.WAITING_ON_PHONE_TO_COMPLETE_OOBE);
                } catch (Exception e2) {
                }
            }
            result = Integer.valueOf((int) RESULT_CHECK_COMPLETE);
            return result;
        }
        return result2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.ScopedAsyncTask
    public void onPostExecute(Integer result) {
        try {
            if (RESULT_CHECK_COMPLETE == result.intValue()) {
                if (this.mNeedsUpdated) {
                    this.mCallback.onFirmwareUpdateNeeded(this.mFirmwareUpdateInfo, this.mBandVersion);
                } else {
                    this.mCallback.onFirmwareUpdateNotNeeded(this.mFirmwareUpdateInfo);
                }
            } else {
                this.mCallback.onFirmwareUpdateCheckFailed(result.intValue());
            }
        } finally {
            releaseWakeLockSafely();
        }
    }

    @Override // com.microsoft.kapp.ScopedAsyncTask
    protected void onCancelled() {
        releaseWakeLockSafely();
    }

    private void releaseWakeLockSafely() {
        if (this.mWakeLock != null && this.mWakeLock.isHeld()) {
            this.mWakeLock.release();
        }
    }
}

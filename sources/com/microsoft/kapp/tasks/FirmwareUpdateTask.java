package com.microsoft.kapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import com.microsoft.band.OOBEStage;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.cloud.CargoFirmwareUpdateInfo;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.models.FirmwareUpdateCallback;
import com.microsoft.kapp.models.FirmwareUpdateProgress;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public class FirmwareUpdateTask extends AsyncTask<Void, FirmwareUpdateProgress, Void> {
    private static final String TAG = FirmwareUpdateTask.class.getSimpleName();
    protected FirmwareUpdateCallback mCallback;
    protected final CargoConnection mCargoConnection;
    private Exception mException;
    protected FirmwareUpdateProgress mProgress;
    private BandServiceMessage.Response mResponse;
    private boolean mShowDeviceMessages = false;
    private boolean mUpdated;
    private final PowerManager.WakeLock mWakeLock;

    public FirmwareUpdateTask(Context context, CargoConnection cargoConnection) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Validate.notNull(cargoConnection, "cargoConnection");
        this.mCargoConnection = cargoConnection;
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        this.mWakeLock = powerManager.newWakeLock(536870913, TAG);
    }

    public FirmwareUpdateTask(Context context, FirmwareUpdateCallback callback, CargoConnection cargoConnection) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Validate.notNull(callback, "callback");
        Validate.notNull(cargoConnection, "cargoConnection");
        this.mCallback = callback;
        this.mCargoConnection = cargoConnection;
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        this.mWakeLock = powerManager.newWakeLock(536870913, TAG);
    }

    public void setCallback(FirmwareUpdateCallback callback) {
        Validate.isNull(this.mCallback, "mCallback is already intialized");
        this.mCallback = callback;
    }

    public void setShowDeviceMessages(boolean showDeviceMessages) {
        this.mShowDeviceMessages = showDeviceMessages;
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        Validate.notNull(this.mCallback, "mCallback must be set prior to task execution");
        this.mWakeLock.acquire();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public Void doInBackground(Void... params) {
        Validate.notNull(this.mCallback, "mCallback must be set prior to task execution");
        this.mResponse = null;
        try {
            publishProgress(FirmwareUpdateProgress.CHECKING_VERSION);
            if (this.mShowDeviceMessages) {
                try {
                    this.mCargoConnection.navigateToScreen(OOBEStage.CHECKING_FOR_UPDATE);
                } catch (Exception e) {
                }
            }
            CargoFirmwareUpdateInfo firmwareUpdateInfo = this.mCargoConnection.getLatestAvailableFirmwareVersion();
            if (firmwareUpdateInfo.isFirmwareUpdateAvailable()) {
                publishProgress(FirmwareUpdateProgress.DOWNLOADING_FIRMWARE);
                if (this.mShowDeviceMessages) {
                    try {
                        this.mCargoConnection.navigateToScreen(OOBEStage.STARTING_UPDATE);
                    } catch (Exception e2) {
                    }
                }
                this.mResponse = this.mCargoConnection.downloadFirmwareUpdate(firmwareUpdateInfo);
                if (this.mResponse == null) {
                    publishProgress(FirmwareUpdateProgress.UPDATING_FIRMWARE);
                    this.mResponse = this.mCargoConnection.pushFirmwareUpdateToDevice(firmwareUpdateInfo);
                    if (this.mResponse == null) {
                        publishProgress(FirmwareUpdateProgress.COMPLETED);
                        if (this.mShowDeviceMessages) {
                            try {
                                this.mCargoConnection.navigateToScreen(OOBEStage.WAITING_ON_PHONE_TO_COMPLETE_OOBE);
                            } catch (Exception e3) {
                            }
                        }
                        this.mUpdated = true;
                    }
                }
            }
        } catch (CargoException ex) {
            this.mException = ex;
            if (ex.getResponse().equals(BandServiceMessage.Response.DEVICE_NOT_CONNECTED_ERROR)) {
                publishProgress(FirmwareUpdateProgress.DEVICE_NOT_CONNECTED);
            }
        } catch (Exception ex2) {
            this.mException = ex2;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(Void result) {
        Validate.notNull(this.mCallback, "mCallback must be set prior to task execution");
        try {
            if (this.mException == null && this.mResponse == null) {
                if (this.mUpdated) {
                    this.mCallback.onUpdateFirmwareSucceeded();
                } else {
                    this.mCallback.onFirmwareUpToDate();
                }
            } else {
                switch (this.mProgress) {
                    case DEVICE_NOT_CONNECTED:
                        this.mCallback.onSingleDeviceEnforcementFailed(this.mResponse);
                        break;
                    case CHECKING_VERSION:
                        this.mCallback.onCheckFirmwareVersionFailed(this.mResponse);
                        break;
                    case DOWNLOADING_FIRMWARE:
                        this.mCallback.onDownloadFirmwareFailed(this.mResponse);
                        break;
                    case UPDATING_FIRMWARE:
                        this.mCallback.onUpdateFirmwareFailed(this.mResponse);
                        break;
                }
            }
        } finally {
            releaseWakeLockSafely();
        }
    }

    @Override // android.os.AsyncTask
    protected void onCancelled() {
        releaseWakeLockSafely();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onProgressUpdate(FirmwareUpdateProgress... values) {
        Validate.notNull(this.mCallback, "mCallback must be set prior to task execution");
        this.mProgress = values[0];
        switch (this.mProgress) {
            case CHECKING_VERSION:
                this.mCallback.onCheckFirmwareVersionStarted();
                return;
            case DOWNLOADING_FIRMWARE:
                this.mCallback.onDownloadFirmwareStarted();
                return;
            case UPDATING_FIRMWARE:
                this.mCallback.onUpdateFirmwareStarted();
                return;
            default:
                return;
        }
    }

    private void releaseWakeLockSafely() {
        if (this.mWakeLock.isHeld()) {
            this.mWakeLock.release();
        }
    }
}

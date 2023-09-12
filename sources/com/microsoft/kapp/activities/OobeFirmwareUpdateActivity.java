package com.microsoft.kapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.microsoft.band.CargoConstants;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.kapp.KApplication;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.models.FirmwareUpdateCallback;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.personalization.PersonalizationManagerFactory;
import com.microsoft.kapp.tasks.FirmwareUpdateTask;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.FreUtils;
import com.microsoft.kapp.utils.StrappUtils;
import com.microsoft.kapp.utils.SyncUtils;
import com.microsoft.krestsdk.models.BandVersion;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class OobeFirmwareUpdateActivity extends OobeBaseActivity implements FirmwareUpdateCallback {
    public static final String ARG_IN_EXTRA_IS_IN_APP_UPDATE = "arg_in_extra_in_app_firmware_update";
    public static final String ARG_IN_EXTRA_IS_OPTIONAL_APP_UPDATE = "arg_in_extra_is_optional_app_update";
    private static final int ONE_HUNDRED_PERCENT = 100;
    private static final int TOTAL_UPDATE_TASKS = 5;
    private Button mCancelButton;
    private CountDownTimer mFakeProgressTimer;
    private TextView mHeader;
    private RelativeLayout mMessageContainer;
    private Button mNextButton;
    @Inject
    PersonalizationManagerFactory mPersonalizationManagerFactory;
    private ProgressBar mProgressBar;
    private int mProgressFakeCompleteMax;
    private int mProgressFakeTickMillis;
    private int mProgressFakeTotalMillis;
    private TextView mProgressMessage;
    private TextView mProgressStep;
    private BroadcastReceiver mReceiver;
    private TextView mSubtext;
    @Inject
    FirmwareUpdateTask mTask;
    private RelativeLayout mUpdateContainer;
    private boolean mFirmwareUpdateSucceeded = false;
    private boolean mIsInAppFirmwareUpdate = false;
    private boolean mIsOptionalAppUpdate = false;
    private int mLastShownPercent = -1;
    private BandVersion mBandVersion = BandVersion.CARGO;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.OobeBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oobe_firmware_update);
        Bundle bundle = savedInstanceState != null ? savedInstanceState : getIntent().getExtras();
        if (bundle != null) {
            this.mIsInAppFirmwareUpdate = bundle.getBoolean(ARG_IN_EXTRA_IS_IN_APP_UPDATE, false);
            this.mIsOptionalAppUpdate = bundle.getBoolean(ARG_IN_EXTRA_IS_OPTIONAL_APP_UPDATE, false);
            this.mBandVersion = BandVersion.values()[bundle.getInt(PersonalizationManagerFactory.BAND_VERSION_ID)];
        }
        getWindow().addFlags(128);
        this.mProgressFakeTotalMillis = getResources().getInteger(R.integer.firmware_update_fake_progress_total_millis);
        this.mProgressFakeTickMillis = getResources().getInteger(R.integer.firmware_update_fake_progress_tick_millis);
        this.mProgressFakeCompleteMax = getResources().getInteger(R.integer.firmware_update_progress_fake_complete_max);
        this.mProgressBar = (ProgressBar) ActivityUtils.getAndValidateView(this, R.id.firmware_update_progress_bar, ProgressBar.class);
        this.mProgressMessage = (TextView) ActivityUtils.getAndValidateView(this, R.id.firmware_update_progress_message, TextView.class);
        this.mProgressStep = (TextView) ActivityUtils.getAndValidateView(this, R.id.firmware_update_progress_step, TextView.class);
        this.mUpdateContainer = (RelativeLayout) ActivityUtils.getAndValidateView(this, R.id.updating_layout, RelativeLayout.class);
        this.mNextButton = (Button) ActivityUtils.getAndValidateView(this, R.id.oobe_confirm, Button.class);
        this.mNextButton.setText(R.string.retry);
        this.mCancelButton = (Button) ActivityUtils.getAndValidateView(this, R.id.oobe_cancel, Button.class);
        this.mCancelButton.setText(R.string.cancel);
        this.mMessageContainer = (RelativeLayout) ActivityUtils.getAndValidateView(this, R.id.update_message_layout, RelativeLayout.class);
        this.mHeader = (TextView) ActivityUtils.getAndValidateView(this, R.id.oobe_title, TextView.class);
        this.mSubtext = (TextView) ActivityUtils.getAndValidateView(this, R.id.oobe_subtitle, TextView.class);
        this.mHeader.setText(R.string.firmware_update_activity_header);
        this.mSubtext.setText(R.string.firmware_update_activity_subheader);
        this.mProgressStep.setText(String.format(getResources().getString(R.string.firmware_update_progress_text), 1, 5));
        this.mProgressBar.setMax(100);
        if (this.mIsInAppFirmwareUpdate && !this.mIsOptionalAppUpdate) {
            this.mCancelButton.setVisibility(8);
        } else {
            this.mCancelButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.OobeFirmwareUpdateActivity.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    if (OobeFirmwareUpdateActivity.this.mIsInAppFirmwareUpdate) {
                        if (OobeFirmwareUpdateActivity.this.mIsOptionalAppUpdate) {
                            OobeFirmwareUpdateActivity.this.finish();
                            return;
                        }
                        return;
                    }
                    OobeFirmwareUpdateActivity.this.getDialogManager().showDialog(OobeFirmwareUpdateActivity.this, Integer.valueOf((int) R.string.app_name), Integer.valueOf((int) R.string.firmware_update_cancel_dialog_text), R.string.yes, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.activities.OobeFirmwareUpdateActivity.1.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialog, int which) {
                            OobeFirmwareUpdateActivity.this.mSettingsProvider.setFreStatus(FreStatus.SKIP_REMAINING_OOBE_STEPS);
                            OobeFirmwareUpdateActivity.this.mSettingsProvider.setShouldOobeConnectPhone(false);
                            OobeFirmwareUpdateActivity.this.mSettingsProvider.setShouldOobeConnectBand(false);
                            OobeFirmwareUpdateActivity.this.mSettingsProvider.setOobeUserProfile("");
                            FreUtils.freRedirect(OobeFirmwareUpdateActivity.this, OobeFirmwareUpdateActivity.this.mSettingsProvider);
                        }
                    }, R.string.no, null, DialogPriority.LOW);
                }
            });
        }
        this.mNextButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.OobeFirmwareUpdateActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                OobeFirmwareUpdateActivity.this.restartThisOobeTask();
            }
        });
        Validate.notNull(this.mTask, "mTask");
        this.mTask.setCallback(this);
        this.mTask.setShowDeviceMessages(!this.mIsInAppFirmwareUpdate);
        this.mReceiver = new MyReceiver();
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(ARG_IN_EXTRA_IS_IN_APP_UPDATE, this.mIsInAppFirmwareUpdate);
        outState.putBoolean(ARG_IN_EXTRA_IS_OPTIONAL_APP_UPDATE, this.mIsOptionalAppUpdate);
        outState.putInt(PersonalizationManagerFactory.BAND_VERSION_ID, this.mBandVersion.ordinal());
        super.onSaveInstanceState(outState);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        this.mReceiver = null;
        if (this.mFakeProgressTimer != null) {
            this.mFakeProgressTimer.cancel();
            this.mFakeProgressTimer = null;
        }
        cancelFirmwareUpdateTask();
        super.onDestroy();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.OobeBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onPause() {
        unregisterUpdateBroadcastReceiver();
        super.onPause();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.OobeBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.OOBE_FIRMWARE_UPDATE);
        registerUpdateBroadcastReceiver();
        if (this.mFirmwareUpdateSucceeded) {
            onUpdateFirmwareSucceeded();
        } else {
            launchFirmwareUpdateTaskIfNeeded();
        }
    }

    private void launchFirmwareUpdateTaskIfNeeded() {
        if (this.mTask == null || this.mTask.getStatus() != AsyncTask.Status.RUNNING) {
            this.mTask = (FirmwareUpdateTask) ((KApplication) getApplication()).get(FirmwareUpdateTask.class);
            this.mTask.setCallback(this);
            this.mTask.setShowDeviceMessages(!this.mIsInAppFirmwareUpdate);
            this.mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }
    }

    private void cancelFirmwareUpdateTask() {
        if (this.mTask != null) {
            this.mTask.cancel(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setProgressPercent(int percent) {
        if (percent > this.mLastShownPercent) {
            this.mProgressBar.setProgress(percent);
            this.mLastShownPercent = percent;
        }
    }

    @Override // com.microsoft.kapp.models.FirmwareUpdateCallback
    public void onCheckFirmwareVersionStarted() {
    }

    @Override // com.microsoft.kapp.models.FirmwareUpdateCallback
    public void onFirmwareUpToDate() {
        if (!isDestroyed()) {
            onUpdateFirmwareSucceeded();
        }
    }

    @Override // com.microsoft.kapp.models.FirmwareUpdateCallback
    public void onCheckFirmwareVersionFailed(BandServiceMessage.Response response) {
        if (!isDestroyed()) {
            if (response != null && response.equals(BandServiceMessage.Response.DEVICE_BATTERY_LOW_ERROR)) {
                stopAndShowRetryDialog(R.string.oobe_device_error_dialog_title, R.string.firmware_update_error_battery);
            } else {
                stopAndShowRetryDialog(R.string.oobe_network_error_dialog_title, R.string.firmware_update_error_cloud_troubleshoot);
            }
        }
    }

    @Override // com.microsoft.kapp.models.FirmwareUpdateCallback
    public void onDownloadFirmwareStarted() {
        Telemetry.logEvent(TelemetryConstants.PageViews.OOBE_FIRMWARE_UPDATE_DOWNLOADING);
        if (!isDestroyed()) {
            this.mProgressMessage.setText(getString(R.string.firmware_update_progress_downloading_firmware));
        }
    }

    @Override // com.microsoft.kapp.models.FirmwareUpdateCallback
    public void onDownloadFirmwareFailed(BandServiceMessage.Response response) {
        if (!isDestroyed()) {
            if (response != null && response.equals(BandServiceMessage.Response.DEVICE_BATTERY_LOW_ERROR)) {
                stopAndShowRetryDialog(R.string.oobe_device_error_dialog_title, R.string.firmware_update_error_battery);
            } else {
                stopAndShowRetryDialog(R.string.oobe_network_error_dialog_title, R.string.oobe_network_error_dialog_message);
            }
        }
    }

    @Override // com.microsoft.kapp.models.FirmwareUpdateCallback
    public void onUpdateFirmwareStarted() {
        Telemetry.logEvent(TelemetryConstants.PageViews.OOBE_FIRMWARE_UPDATE_SENDING_TO_BAND);
        if (!isDestroyed()) {
            this.mProgressMessage.setText(getString(R.string.firmware_update_progress_sending_to_band));
        }
    }

    @Override // com.microsoft.kapp.models.FirmwareUpdateCallback
    public void onUpdateFirmwareSucceeded() {
        Telemetry.logEvent(TelemetryConstants.PageViews.OOBE_FIRMWARE_UPDATE_COMPLETE);
        if (this.mFakeProgressTimer != null) {
            this.mFakeProgressTimer.cancel();
        }
        this.mFirmwareUpdateSucceeded = true;
        this.mSettingsProvider.setCheckedFirmwareUpdateInfo(null);
        if (this.mIsInAppFirmwareUpdate) {
            StrappUtils.clearStrappAndCalendarCacheData(this.mSettingsProvider, this.mCargoConnection);
            SyncUtils.startStrappsDataSync(getBaseContext());
        }
        if (!isDestroyed()) {
            this.mHeader.setText(R.string.oobe_firmware_complete_title);
            this.mSubtext.setText(R.string.oobe_firmware_complete_subtext);
            showUpdateCompleteGraphic();
            showFirmwareCompleteNextButton();
        }
    }

    @Override // com.microsoft.kapp.models.FirmwareUpdateCallback
    public void onUpdateFirmwareFailed(BandServiceMessage.Response response) {
        if (!isDestroyed()) {
            if (response != null && response.equals(BandServiceMessage.Response.DEVICE_BATTERY_LOW_ERROR)) {
                stopAndShowRetryDialog(R.string.oobe_device_error_dialog_title, R.string.firmware_update_error_battery);
            } else {
                stopAndShowRetryDialog(R.string.oobe_device_error_dialog_title, R.string.firmware_update_error_band_troubleshoot);
            }
        }
    }

    @Override // com.microsoft.kapp.models.FirmwareUpdateCallback
    public void onSingleDeviceEnforcementFailed(BandServiceMessage.Response mResponse) {
        if (!isDestroyed()) {
            stopAndShowRetryDialog(R.string.oobe_device_error_dialog_title, R.string.firmware_update_error_band_troubleshoot);
        }
    }

    private void showUpdateCompleteGraphic() {
        this.mUpdateContainer.setVisibility(8);
        this.mMessageContainer.setVisibility(0);
        ((ImageView) findViewById(R.id.oobe_update_message_icon)).setImageResource(R.drawable.oobe_firmware_update_complete);
    }

    private void showFirmwareCompleteNextButton() {
        this.mUpdateContainer.setVisibility(8);
        this.mMessageContainer.setVisibility(0);
        this.mCancelButton.setVisibility(8);
        this.mHeader.setText(R.string.firmware_update_activity_complete_header);
        this.mSubtext.setText(R.string.firmware_update_activity_complete_subheader);
        this.mNextButton.setVisibility(0);
        this.mNextButton.setText(R.string.oobe_finish_button);
        this.mNextButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.OobeFirmwareUpdateActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                OobeFirmwareUpdateActivity.this.onFirmwareCompleteNextPressed();
            }
        });
    }

    public void onFirmwareCompleteNextPressed() {
        if (this.mIsInAppFirmwareUpdate) {
            finish();
        } else {
            moveToNextOobeTask();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restartThisOobeTask() {
        Intent intent = new Intent(this, OobeFirmwareUpdateActivity.class);
        intent.putExtra(ARG_IN_EXTRA_IS_IN_APP_UPDATE, this.mIsInAppFirmwareUpdate);
        intent.putExtra(ARG_IN_EXTRA_IS_OPTIONAL_APP_UPDATE, this.mIsOptionalAppUpdate);
        startActivity(intent);
        finish();
    }

    private void moveToNextOobeTask() {
        this.mSettingsProvider.setFreStatus(FreStatus.FIRMWARE_VERSION_CHECKED);
        FreUtils.devicePairingRedirect(this, this.mSettingsProvider);
    }

    private void stopAndShowRetryDialog(int errorMessage, int helpMessage) {
        if (this.mTask != null) {
            this.mTask.cancel(true);
        }
        if (this.mFakeProgressTimer != null) {
            this.mFakeProgressTimer.cancel();
        }
        this.mUpdateContainer.setVisibility(8);
        this.mMessageContainer.setVisibility(0);
        this.mHeader.setText(R.string.firmware_update_activity_error_header);
        this.mSubtext.setText(R.string.firmware_update_activity_error_subheader);
        getDialogManager().showDialog(this, Integer.valueOf(errorMessage), Integer.valueOf(helpMessage), DialogPriority.HIGH);
    }

    /* loaded from: classes.dex */
    private class FakeProgressCountDownTimer extends CountDownTimer {
        private int mProgressFakeStart;
        private long mTotalMillis;

        public FakeProgressCountDownTimer(long millisInFuture, long countDownInterval, int fakeStartPercent) {
            super(millisInFuture, countDownInterval);
            this.mTotalMillis = millisInFuture;
            this.mProgressFakeStart = fakeStartPercent;
        }

        @Override // android.os.CountDownTimer
        public void onTick(long millisUntilFinished) {
            int percent = OobeFirmwareUpdateActivity.this.mProgressFakeCompleteMax - ((int) ((OobeFirmwareUpdateActivity.this.mProgressFakeCompleteMax - this.mProgressFakeStart) * (millisUntilFinished / this.mTotalMillis)));
            OobeFirmwareUpdateActivity.this.setProgressPercent(percent);
        }

        @Override // android.os.CountDownTimer
        public void onFinish() {
            OobeFirmwareUpdateActivity.this.setProgressPercent(OobeFirmwareUpdateActivity.this.mProgressFakeCompleteMax);
        }
    }

    private void registerUpdateBroadcastReceiver() {
        IntentFilter filter = new IntentFilter(CargoConstants.ACTION_FW_UPGRADE_PROGRESS);
        LocalBroadcastManager.getInstance(this).registerReceiver(this.mReceiver, filter);
    }

    private void unregisterUpdateBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mReceiver);
    }

    /* loaded from: classes.dex */
    private class MyReceiver extends BroadcastReceiver {
        private MyReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(CargoConstants.PROGRESS_VALUE) && intent.hasExtra(CargoConstants.PROGRESS_CODE)) {
                int value = intent.getIntExtra(CargoConstants.PROGRESS_VALUE, -1);
                int code = intent.getIntExtra(CargoConstants.PROGRESS_CODE, -1);
                Validate.isTrue(value >= 0 && value <= 100, "PROGRESS_VALUE is not a valid percentage");
                if (OobeFirmwareUpdateActivity.this.mFakeProgressTimer == null) {
                    OobeFirmwareUpdateActivity.this.setProgressPercent(value);
                }
                if (code != BandServiceMessage.Response.DOWNLOAD_FIRMWARE_UPDATE_STARTED.getCode()) {
                    if (code == BandServiceMessage.Response.UPGRADE_FIRMWARE_LOADING_FIRMWARE.getCode()) {
                        OobeFirmwareUpdateActivity.this.mProgressStep.setText(String.format(OobeFirmwareUpdateActivity.this.getResources().getString(R.string.firmware_update_progress_text), 2, 5));
                        OobeFirmwareUpdateActivity.this.mProgressMessage.setText(R.string.firmware_update_progress_sending_to_band);
                    } else if (code == BandServiceMessage.Response.UPGRADE_FIRMWARE_ENTERING_UPGRADE_MODE.getCode()) {
                        OobeFirmwareUpdateActivity.this.mProgressStep.setText(String.format(OobeFirmwareUpdateActivity.this.getResources().getString(R.string.firmware_update_progress_text), 3, 5));
                        OobeFirmwareUpdateActivity.this.mProgressMessage.setText(R.string.firmware_update_progress_restarting_band);
                    } else if (code == BandServiceMessage.Response.UPGRADE_FIRMWARE_INSTALLING_FIRMWARE.getCode()) {
                        Telemetry.logEvent(TelemetryConstants.PageViews.OOBE_FIRMWARE_UPDATE_APPLYING_UPDATE);
                        OobeFirmwareUpdateActivity.this.mProgressStep.setText(String.format(OobeFirmwareUpdateActivity.this.getResources().getString(R.string.firmware_update_progress_text), 4, 5));
                        OobeFirmwareUpdateActivity.this.mProgressMessage.setText(R.string.firmware_update_progress_installing_firmware);
                        if (OobeFirmwareUpdateActivity.this.mFakeProgressTimer == null) {
                            OobeFirmwareUpdateActivity.this.mFakeProgressTimer = new FakeProgressCountDownTimer(OobeFirmwareUpdateActivity.this.mProgressFakeTotalMillis, OobeFirmwareUpdateActivity.this.mProgressFakeTickMillis, value);
                            OobeFirmwareUpdateActivity.this.mFakeProgressTimer.start();
                        }
                    } else if (code == BandServiceMessage.Response.UPGRADE_FIRMWARE_FINALIZING_UPGRADE.getCode()) {
                        OobeFirmwareUpdateActivity.this.mProgressStep.setText(String.format(OobeFirmwareUpdateActivity.this.getResources().getString(R.string.firmware_update_progress_text), 5, 5));
                        OobeFirmwareUpdateActivity.this.mProgressMessage.setText(R.string.firmware_update_progress_finalizing_update);
                        Telemetry.logEvent(TelemetryConstants.PageViews.OOBE_FIRMWARE_UPDATE_REBOOTING);
                    }
                }
            }
        }
    }
}

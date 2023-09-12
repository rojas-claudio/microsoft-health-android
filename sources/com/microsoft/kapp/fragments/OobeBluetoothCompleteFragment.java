package com.microsoft.kapp.fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DeviceConnectActivity;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.tasks.FirmwareUpdateCheckTask;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.version.CheckedFirmwareUpdateInfo;
import com.microsoft.krestsdk.models.BandVersion;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class OobeBluetoothCompleteFragment extends BaseFragment implements FirmwareUpdateCheckTask.FirmwareUpdateNeededCallback {
    private static final long UPDATE_TASK_TIMEOUT_MILLISECONDS = 30000;
    private BandVersion mBandVersion;
    private TextView mCheckingForUpdate;
    private Button mFinishedButton;
    private Handler mHandler;
    @Inject
    SettingsProvider mSettingsProvider;
    private FirmwareUpdateCheckTask mTask;
    private Runnable mTimeout;

    /* loaded from: classes.dex */
    public interface OnUpdateCheckCompleteListener {
        void onUpdateComplete();
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.mBandVersion = this.mSettingsProvider.getOobeUiBandVersion();
        View view = inflater.inflate(R.layout.fragment_device_connect_complete, container, false);
        if (this.mBandVersion != BandVersion.NEON) {
            ((ImageView) ViewUtils.getValidView(view, R.id.band_connected_image, ImageView.class)).setImageResource(R.drawable.oobe_cargo_band_connected);
            ((TextView) ViewUtils.getValidView(view, R.id.oobe_subtitle, TextView.class)).setText(R.string.pair_band_complete_subtitle_cargo);
        } else {
            ((TextView) ViewUtils.getValidView(view, R.id.oobe_subtitle, TextView.class)).setText(R.string.pair_band_complete_subtitle_neon);
        }
        ((TextView) ViewUtils.getValidView(view, R.id.oobe_title, TextView.class)).setText(R.string.pair_band_complete_title);
        ((TextView) ViewUtils.getValidView(view, R.id.oobe_cancel, TextView.class)).setVisibility(8);
        this.mFinishedButton = (Button) ViewUtils.getValidView(view, R.id.oobe_confirm, Button.class);
        this.mFinishedButton.setText(R.string.oobe_finish_button);
        this.mCheckingForUpdate = (TextView) ViewUtils.getValidView(view, R.id.checking_for_updates, TextView.class);
        this.mFinishedButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.OobeBluetoothCompleteFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View clickView) {
                OobeBluetoothCompleteFragment.this.notifyComplete();
            }
        });
        this.mFinishedButton.setVisibility(8);
        return view;
    }

    @Override // com.microsoft.kapp.fragments.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.OOBE_BLUETOOTH_PAIRING_SUCCESS);
        startNewUpdateCheckTask();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startNewUpdateCheckTask() {
        if (this.mTask == null || this.mTask.getStatus() == AsyncTask.Status.FINISHED) {
            this.mTask = new FirmwareUpdateCheckTask(getActivity(), this, this.mSettingsProvider, this.mCargoConnection, true, this);
            this.mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            this.mHandler = new Handler();
            this.mTimeout = new Runnable() { // from class: com.microsoft.kapp.fragments.OobeBluetoothCompleteFragment.2
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        if (OobeBluetoothCompleteFragment.this.isAdded() && OobeBluetoothCompleteFragment.this.mTask.getStatus() != AsyncTask.Status.FINISHED) {
                            OobeBluetoothCompleteFragment.this.mTask.cancel(true);
                            OobeBluetoothCompleteFragment.this.onFirmwareUpdateCheckFailed(FirmwareUpdateCheckTask.RESULT_ERROR_CLOUD);
                        }
                    } catch (Exception ex) {
                        KLog.d(OobeBluetoothCompleteFragment.this.TAG, "Update task timed out!", ex);
                        OobeBluetoothCompleteFragment.this.onFirmwareUpdateCheckFailed(FirmwareUpdateCheckTask.RESULT_ERROR_CLOUD);
                    }
                }
            };
            this.mHandler.postDelayed(this.mTimeout, UPDATE_TASK_TIMEOUT_MILLISECONDS);
        }
    }

    public void notifyComplete() {
        DeviceConnectActivity activity = (DeviceConnectActivity) getActivity();
        if (activity != null) {
            activity.onUpdateComplete();
        }
    }

    @Override // com.microsoft.kapp.tasks.FirmwareUpdateCheckTask.FirmwareUpdateNeededCallback
    public void onFirmwareUpdateNeeded(CheckedFirmwareUpdateInfo firmwareUpdateInfo, BandVersion bandVersion) {
        cancelTimeout();
        if (isAdded()) {
            getDialogManager().showDialog(getActivity(), Integer.valueOf((int) R.string.firmware_update_dialog_title), Integer.valueOf((int) R.string.oobe_firmware_update_required_message), R.string.firmware_update_activity_update, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.OobeBluetoothCompleteFragment.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int id) {
                    OobeBluetoothCompleteFragment.this.mSettingsProvider.setFreStatus(FreStatus.CONNECTED_DEVICE);
                    OobeBluetoothCompleteFragment.this.notifyComplete();
                }
            }, DialogPriority.HIGH);
        }
    }

    private void cancelTimeout() {
        if (this.mHandler != null && this.mTimeout != null) {
            this.mHandler.removeCallbacks(this.mTimeout);
        }
    }

    @Override // com.microsoft.kapp.tasks.FirmwareUpdateCheckTask.FirmwareUpdateNeededCallback
    public void onFirmwareUpdateNotNeeded(CheckedFirmwareUpdateInfo firmwareUpdateInfo) {
        cancelTimeout();
        this.mCheckingForUpdate.setVisibility(8);
        this.mFinishedButton.setVisibility(0);
        this.mFinishedButton.setTextColor(getResources().getColor(R.color.oobe_text));
        this.mSettingsProvider.setFreStatus(FreStatus.FIRMWARE_VERSION_CHECKED);
    }

    @Override // com.microsoft.kapp.tasks.FirmwareUpdateCheckTask.FirmwareUpdateNeededCallback
    public void onFirmwareUpdateCheckFailed(int resultCode) {
        cancelTimeout();
        if (isAdded()) {
            switch (resultCode) {
                case 5000:
                    showRetryDialog(R.string.oobe_device_error_dialog_title, -1, R.string.oobe_connection_with_device_error);
                    return;
                case FirmwareUpdateCheckTask.RESULT_ERROR_CLOUD /* 5001 */:
                    showRetryDialog(R.string.oobe_network_error_dialog_title, -1, R.string.oobe_connection_with_cloud_error);
                    return;
                default:
                    showRetryDialog(R.string.oobe_device_or_cloud_dialog_title, R.string.firmware_update_error_checking, R.string.firmware_update_error_cloud_troubleshoot);
                    return;
            }
        }
    }

    private void showRetryDialog(int title, int errorMessage, int helpMessage) {
        StringBuilder builder = new StringBuilder();
        if (errorMessage != -1) {
            builder.append(getString(errorMessage)).append("\n\n");
        }
        builder.append(getString(helpMessage));
        String message = builder.toString();
        getDialogManager().showDialog(getActivity(), getString(title), message, getString(R.string.button_retry), new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.fragments.OobeBluetoothCompleteFragment.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                OobeBluetoothCompleteFragment.this.startNewUpdateCheckTask();
            }
        }, DialogPriority.HIGH);
    }
}

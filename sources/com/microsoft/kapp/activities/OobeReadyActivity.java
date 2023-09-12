package com.microsoft.kapp.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import com.microsoft.band.client.CargoException;
import com.microsoft.band.internal.BandServiceMessage;
import com.microsoft.kapp.CargoExtensions;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.FreUtils;
import com.microsoft.kapp.utils.SyncUtils;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class OobeReadyActivity extends LastOobeActivity {
    private static final String TAG = OobeReadyActivity.class.getSimpleName();
    private View mErrorLayout;
    private boolean mIsDevicePairingFinalize;
    private View mLoadingLayout;

    @Override // com.microsoft.kapp.activities.LastOobeActivity, com.microsoft.kapp.activities.OobeBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oobe_ready);
        this.mIsDevicePairingFinalize = this.mSettingsProvider.shouldOobeConnectBand() || this.mSettingsProvider.shouldOobeConnectPhone();
        TextView somethingWentWrongTitle = (TextView) findViewById(R.id.oobe_title);
        String somethingWentWrongTitleString = String.format(getResources().getString(R.string.oobe_error_title), new Object[0]);
        TextView somethingWentWrongSubTitle = (TextView) findViewById(R.id.oobe_subtitle);
        String somethingWentWrongSubTitleString = String.format(getResources().getString(R.string.oobe_error_subtitle), new Object[0]);
        TextView tryAgainButtonTitle = (TextView) findViewById(R.id.oobe_confirm);
        String tryAgainButtonTitleString = String.format(getResources().getString(R.string.oobe_try_again), new Object[0]);
        TextView cancelButtonTitle = (TextView) findViewById(R.id.oobe_cancel);
        String cancelButtonTitleString = String.format(getResources().getString(R.string.oobe_cancel), new Object[0]);
        somethingWentWrongTitle.setText(Html.fromHtml(somethingWentWrongTitleString));
        somethingWentWrongSubTitle.setText(Html.fromHtml(somethingWentWrongSubTitleString));
        tryAgainButtonTitle.setText(Html.fromHtml(tryAgainButtonTitleString));
        cancelButtonTitle.setText(Html.fromHtml(cancelButtonTitleString));
        this.mErrorLayout = (View) ActivityUtils.getAndValidateView(this, R.id.error_layout, View.class);
        tryAgainButtonTitle.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.OobeReadyActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View retryButton) {
                OobeReadyActivity.this.mErrorLayout.setVisibility(8);
                OobeReadyActivity.this.mLoadingLayout.setVisibility(0);
                OobeReadyActivity.this.completeOOBE();
            }
        });
        cancelButtonTitle.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.OobeReadyActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View cancelButton) {
                OobeReadyActivity.this.mSettingsProvider.setFreStatus(FreStatus.SKIP_REMAINING_OOBE_STEPS);
                OobeReadyActivity.this.mSettingsProvider.setShouldOobeConnectPhone(false);
                OobeReadyActivity.this.mSettingsProvider.setShouldOobeConnectBand(false);
                OobeReadyActivity.this.mSettingsProvider.setOobeUserProfile("");
                FreUtils.freRedirect(OobeReadyActivity.this, OobeReadyActivity.this.mSettingsProvider);
            }
        });
        this.mLoadingLayout = (View) ActivityUtils.getAndValidateView(this, R.id.loading_layout, View.class);
        if (this.mIsDevicePairingFinalize) {
            ((TextView) findViewById(R.id.oobe_ready_title)).setText(R.string.oobe_last_screen_subtitle);
        }
        completeOOBE();
    }

    @Override // com.microsoft.kapp.activities.LastOobeActivity
    public void taskFailed(int taskId, Exception exception, boolean optionalIsCloudError, boolean optionalIsDevicError) {
        BandServiceMessage.Response response;
        int errorTitleId;
        int errorMessageId;
        DialogInterface.OnClickListener dialogClickListener = null;
        if (this.mIsDevicePairingFinalize) {
            this.mErrorLayout.setVisibility(0);
            this.mLoadingLayout.setVisibility(8);
        } else {
            dialogClickListener = new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.activities.OobeReadyActivity.3
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    OobeReadyActivity.this.completeOOBE();
                }
            };
        }
        Boolean isDeviceError = null;
        if (optionalIsCloudError) {
            isDeviceError = false;
        } else if (optionalIsDevicError) {
            isDeviceError = true;
        } else if (exception instanceof CargoExtensions.SingleDeviceCheckFailedException) {
            isDeviceError = true;
        } else if ((exception instanceof CargoException) && (response = ((CargoException) exception).getResponse()) != null && !response.equals(BandServiceMessage.Response.SERVICE_COMMAND_ERROR)) {
            switch (response.getCategory()) {
                case 3:
                    isDeviceError = true;
                    break;
                case 4:
                    isDeviceError = false;
                    break;
            }
        }
        KLog.w(TAG, "Unknown/unexpected error for taskId=" + taskId, exception);
        if (isDeviceError == null) {
            errorTitleId = R.string.unknown_error_title;
            errorMessageId = R.string.oobe_unknown_error;
        } else if (isDeviceError.booleanValue()) {
            errorTitleId = R.string.connection_with_device_error_title;
            errorMessageId = R.string.oobe_connection_with_device_error;
        } else {
            errorTitleId = R.string.connection_with_cloud_error_title;
            errorMessageId = R.string.oobe_connection_with_cloud_error;
        }
        getDialogManager().showDialog(this, Integer.valueOf(errorTitleId), Integer.valueOf(errorMessageId), dialogClickListener, DialogPriority.LOW);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.OobeBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.OOBE_FINISH);
    }

    @Override // com.microsoft.kapp.activities.LastOobeActivity
    public void taskSucceed() {
        if (this.mIsDevicePairingFinalize) {
            this.mSettingsProvider.setFreStatus(FreStatus.OOBE_READY);
            FreUtils.freRedirect(this, this.mSettingsProvider);
            this.mSettingsProvider.setOobeUserProfile("");
            this.mSettingsProvider.setDeviceName("");
            this.mSettingsProvider.setIsInNoDevicePairedModeState(false);
            this.mMultiDeviceManager.startSync(false);
            SyncUtils.startStrappsDataSync(getApplicationContext());
        } else {
            this.mSettingsProvider.setFreStatus(FreStatus.DEVICE_CONNECT_START);
            FreUtils.devicePairingRedirect(this, this.mSettingsProvider);
            this.mSettingsProvider.setFirstRunTime(DateTime.now());
        }
        this.mSettingsProvider.setShouldOobeConnectPhone(false);
        this.mSettingsProvider.setShouldOobeConnectBand(false);
    }
}

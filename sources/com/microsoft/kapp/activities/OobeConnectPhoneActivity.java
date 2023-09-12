package com.microsoft.kapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.FreUtils;
/* loaded from: classes.dex */
public class OobeConnectPhoneActivity extends OobeBaseActivity {
    private Button mNextButton;
    private TextView mSkipButton;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.OobeBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oobe_connect_phone_activity);
        this.mNextButton = (Button) ActivityUtils.getAndValidateView(this, R.id.oobe_confirm, Button.class);
        this.mNextButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.OobeConnectPhoneActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                OobeConnectPhoneActivity.this.mSettingsProvider.setShouldOobeConnectPhone(true);
                OobeConnectPhoneActivity.this.launchNextActivity();
            }
        });
        this.mSkipButton = (TextView) ActivityUtils.getAndValidateView(this, R.id.oobe_cancel, Button.class);
        this.mSkipButton.setText(getResources().getString(R.string.motion_settings_disabled));
        this.mSkipButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.OobeConnectPhoneActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                OobeConnectPhoneActivity.this.mSettingsProvider.setFreStatus(FreStatus.SKIP_REMAINING_OOBE_STEPS);
                FreUtils.devicePairingRedirect(OobeConnectPhoneActivity.this, OobeConnectPhoneActivity.this.mSettingsProvider);
            }
        });
        this.mNextButton.setText(getResources().getString(R.string.motion_settings_enabled));
        TextView header = (TextView) ActivityUtils.getAndValidateView(this, R.id.oobe_title, TextView.class);
        header.setText(R.string.oobe_connect_phone_finished_title);
        TextView subtext = (TextView) ActivityUtils.getAndValidateView(this, R.id.oobe_subtitle, TextView.class);
        subtext.setText(R.string.oobe_connect_phone_finished_subtitle);
        if (!this.mSensorUtils.isKitkatWithStepSensor()) {
            this.mSettingsProvider.setShouldOobeConnectPhone(false);
            launchNextActivity();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.OobeBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void launchNextActivity() {
        this.mSettingsProvider.setFreStatus(FreStatus.PHONE_BIOMETRICS_ESTABLISHED);
        FreUtils.devicePairingRedirect(this, this.mSettingsProvider);
    }
}

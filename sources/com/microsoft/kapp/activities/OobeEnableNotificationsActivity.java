package com.microsoft.kapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.microsoft.kapp.R;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.utils.ActivityUtils;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.FreUtils;
/* loaded from: classes.dex */
public class OobeEnableNotificationsActivity extends OobeBaseActivity {
    private Button mActivateNotificationsButton;
    private Button mSkipText;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.OobeBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oobe_enable_notification_activity);
        this.mActivateNotificationsButton = (Button) ActivityUtils.getAndValidateView(this, R.id.oobe_confirm, Button.class);
        this.mActivateNotificationsButton.setText(R.string.oobe_connect_phone_button);
        this.mActivateNotificationsButton.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.OobeEnableNotificationsActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Intent intent = new Intent(Constants.INTENT_ANDROID_NOTIFICATION_LISTENER);
                OobeEnableNotificationsActivity.this.startActivity(intent);
            }
        });
        this.mSkipText = (Button) ActivityUtils.getAndValidateView(this, R.id.oobe_cancel, Button.class);
        this.mSkipText.setText(getResources().getString(R.string.button_skip));
        this.mSkipText.setOnClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.activities.OobeEnableNotificationsActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                OobeEnableNotificationsActivity.this.startNextActivity();
            }
        });
        TextView header = (TextView) ActivityUtils.getAndValidateView(this, R.id.oobe_title, TextView.class);
        header.setText(R.string.oobe_notifications_title);
        TextView subtext = (TextView) ActivityUtils.getAndValidateView(this, R.id.oobe_subtitle, TextView.class);
        subtext.setText(R.string.oobe_notifications_subtitle);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.OobeBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (!CommonUtils.areNotificationsSupported() || CommonUtils.areNotificationsEnabled(getApplicationContext())) {
            startNextActivity();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startNextActivity() {
        this.mSettingsProvider.setFreStatus(FreStatus.NOTIFICATION_ENABLE_COMPLETE);
        FreUtils.devicePairingRedirect(this, this.mSettingsProvider);
    }
}

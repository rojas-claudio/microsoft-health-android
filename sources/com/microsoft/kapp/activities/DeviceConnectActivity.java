package com.microsoft.kapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import com.microsoft.kapp.R;
import com.microsoft.kapp.fragments.OobeBluetoothCompleteFragment;
import com.microsoft.kapp.fragments.OobeBluetoothConnectionFragment;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.utils.FreUtils;
/* loaded from: classes.dex */
public class DeviceConnectActivity extends OobeBaseActivity {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.OobeBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_connect);
        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            OobeBluetoothConnectionFragment pairingFragment = new OobeBluetoothConnectionFragment();
            ft.add(R.id.current_fragment, pairingFragment);
            ft.commit();
        }
    }

    public void onPairingComplete() {
        this.mSettingsProvider.setShouldOobeConnectBand(true);
        this.mSettingsProvider.setFreStatus(FreStatus.DEVICE_CONNECT_START);
        if (!isDestroyed() && !isFinishing()) {
            OobeBluetoothCompleteFragment pairedFragment = new OobeBluetoothCompleteFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.current_fragment, pairedFragment);
            ft.commitAllowingStateLoss();
        }
    }

    public void onUpdateComplete() {
        if (!isDestroyed() && !isFinishing()) {
            FreUtils.devicePairingRedirect(this, this.mSettingsProvider);
        }
    }

    @Override // com.microsoft.kapp.activities.OobeBaseActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        this.mSettingsProvider.setFreStatus(FreStatus.SKIP_REMAINING_OOBE_STEPS);
        FreUtils.freRedirect(this, this.mSettingsProvider);
    }
}

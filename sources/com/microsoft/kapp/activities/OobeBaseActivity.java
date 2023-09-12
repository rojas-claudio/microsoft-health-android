package com.microsoft.kapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.microsoft.kapp.CargoConnection;
import com.microsoft.kapp.KApplication;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.models.FreStatus;
import com.microsoft.kapp.sensor.SensorUtils;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.DialogManagerImpl;
import com.microsoft.kapp.utils.FreUtils;
import com.microsoft.kapp.utils.StubDialogManager;
import javax.inject.Inject;
/* loaded from: classes.dex */
public abstract class OobeBaseActivity extends FragmentActivity implements OnTaskListener, DialogManagerProvider {
    @Inject
    CargoConnection mCargoConnection;
    @Inject
    DialogManagerImpl mDialogManager;
    private boolean mIsInForeground;
    @Inject
    SensorUtils mSensorUtils;
    @Inject
    SettingsProvider mSettingsProvider;
    @Inject
    StubDialogManager mStubDialogManager;

    @Override // com.microsoft.kapp.activities.DialogManagerProvider
    public DialogManager getDialogManager() {
        return this.mIsInForeground ? this.mDialogManager : this.mStubDialogManager;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(1);
        KApplication application = (KApplication) getApplication();
        application.inject(this);
        Validate.notNull(this.mSettingsProvider, "mSettingsProvider");
        Validate.notNull(this.mCargoConnection, "mCargoConnection");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        this.mIsInForeground = true;
        super.onResume();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onPause() {
        this.mDialogManager.dismiss();
        this.mIsInForeground = false;
        super.onPause();
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void moveToNextOobeTask(FreStatus updateStatus) {
        if (updateStatus != null) {
            this.mSettingsProvider.setFreStatus(updateStatus);
        }
        if (!FreUtils.freRedirect(this, this.mSettingsProvider)) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(32768);
            intent.addFlags(268435456);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
    }

    @Override // com.microsoft.kapp.OnTaskListener
    public boolean isWaitingForResult() {
        return Validate.isActivityAlive(this);
    }
}

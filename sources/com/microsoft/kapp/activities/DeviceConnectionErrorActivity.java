package com.microsoft.kapp.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.microsoft.kapp.DeviceErrorState;
import com.microsoft.kapp.DeviceStateDisplayManager;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.CommonUtils;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class DeviceConnectionErrorActivity extends FragmentActivity implements DialogManagerProvider {
    public static final String DEVICE_ERROR_STATE_ID = "deviceErrorState";
    private BaseActivityAdapter mAdapter = new BaseActivityAdapter(this);
    @Inject
    DeviceStateDisplayManager mDeviceStateDisplayManager;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mAdapter.onCreate(savedInstanceState);
        setRequestedOrientation(1);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_device_connection_error);
        Bundle extras = getIntent().getExtras();
        DeviceErrorState deviceErrorState = (DeviceErrorState) extras.get(DEVICE_ERROR_STATE_ID);
        showUIForError(deviceErrorState);
        this.mDeviceStateDisplayManager.setUIDisplayed(true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.mAdapter.onResume();
    }

    private void showUIForError(DeviceErrorState deviceErrorState) {
        if (deviceErrorState != DeviceErrorState.NONE) {
            DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.activities.DeviceConnectionErrorActivity.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    DeviceConnectionErrorActivity.this.finish();
                    DeviceConnectionErrorActivity.this.overridePendingTransition(0, 0);
                }
            };
            if (deviceErrorState == DeviceErrorState.MULTIPLE_DEVICES_BONDED) {
                getDialogManager().showMultipleDevicesConnectedError(this, dialogListener);
            } else {
                getDialogManager().showDialog(this, CommonUtils.getResIdForDeviceErrorState(deviceErrorState), Integer.valueOf((int) R.string.ok), dialogListener, DialogPriority.HIGH);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.mAdapter.onPause();
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.mDeviceStateDisplayManager.setUIDisplayed(false);
    }

    @Override // android.support.v4.app.FragmentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @Override // com.microsoft.kapp.activities.DialogManagerProvider
    public DialogManager getDialogManager() {
        return this.mAdapter.getDialogManager();
    }
}

package com.microsoft.kapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import com.microsoft.kapp.activities.DialogPriority;
import com.microsoft.kapp.logging.KLog;
/* loaded from: classes.dex */
public class StubDialogManager implements com.microsoft.kapp.activities.DialogManager {
    private final String TAG = getClass().getSimpleName();

    @Override // com.microsoft.kapp.activities.DialogManager
    public void dismiss() {
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showNetworkErrorDialog(Context context) {
        log();
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showNetworkErrorDialogWithCallback(Context context, DialogInterface.OnClickListener clickListener) {
        log();
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showDeviceErrorDialog(Context context) {
        log();
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showDeviceErrorDialogWithCallback(Context context, DialogInterface.OnClickListener clickListener) {
        log();
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showDialog(Context context, Integer dialogTitleResId, Integer dialogMessageResId, DialogPriority priority) {
        log();
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showDialog(Context context, Integer dialogTitleResId, Integer dialogMessageResId, DialogInterface.OnClickListener positiveCallback, DialogPriority priority) {
        log();
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showDialog(Context context, Integer dialogTitleResId, Integer dialogMessageResId, int positiveButtonResId, DialogInterface.OnClickListener positiveCallback, DialogPriority priority) {
        log();
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showDialog(Context context, Integer dialogTitleResId, Integer dialogMessageResId, DialogInterface.OnClickListener positiveCallback, DialogInterface.OnClickListener negativeCallback, DialogPriority priority) {
        log();
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showDialog(Context context, Integer dialogTitleResId, Integer dialogMessageResId, int positiveButtonResId, DialogInterface.OnClickListener positiveCallback, DialogInterface.OnClickListener negativeCallback, DialogPriority priority) {
        log();
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showDialog(Context context, Integer dialogTitleResId, Integer dialogMessageResId, int positiveButtonResId, DialogInterface.OnClickListener positiveCallback, int negativeButtonResId, DialogInterface.OnClickListener negativeCallback, DialogPriority priority) {
        log();
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showDialog(Context context, String dialogTitle, String dialogMessage, String positiveButton, DialogInterface.OnClickListener positiveCallback, DialogPriority priority) {
        log();
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showDialog(Context context, CharSequence dialogTitle, CharSequence dialogMessage, String positiveButton, DialogInterface.OnClickListener positiveCallback, DialogInterface.OnClickListener negativeCallback, DialogPriority priority) {
        log();
    }

    private void log() {
        KLog.i(this.TAG, "Dialog request has been made when the activity is not in foreground");
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showMultipleDevicesConnectedError(Context context) {
        log();
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showMultipleDevicesConnectedError(Context context, DialogInterface.OnClickListener dialogListener) {
        log();
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void clickConfirm() {
        log();
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void clickCancel() {
        log();
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public boolean isDialogVisible() {
        log();
        return false;
    }
}

package com.microsoft.kapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.microsoft.kapp.R;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public class DialogManager {
    private static final AtomicBoolean mIsDialogVisible = new AtomicBoolean(false);
    private static final DialogInterface.OnClickListener mCloseDialog = new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.utils.DialogManager.1
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    };
    private static final DialogInterface.OnDismissListener mDialogDismissedListener = new DialogInterface.OnDismissListener() { // from class: com.microsoft.kapp.utils.DialogManager.2
        @Override // android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface dialog) {
            DialogManager.mIsDialogVisible.set(false);
        }
    };

    /* loaded from: classes.dex */
    public enum Priority {
        LOW,
        HIGH
    }

    public static void showNetworkErrorDialog(Context context) {
        showNetworkErrorDialogWithCallback(context, null);
    }

    public static void showNetworkErrorDialogWithCallback(Context context, DialogInterface.OnClickListener clickListener) {
        showDialog(context, Integer.valueOf((int) R.string.network_error_loading_data_title), Integer.valueOf((int) R.string.network_error_loading_data), clickListener, Priority.LOW);
    }

    public static void showDeviceErrorDialog(Context context) {
        showDeviceErrorDialogWithCallback(context, null);
    }

    public static void showDeviceErrorDialogWithCallback(Context context, DialogInterface.OnClickListener clickListener) {
        showDialog(context, Integer.valueOf((int) R.string.connection_with_device_error_title), Integer.valueOf((int) R.string.band_save_failed_message), clickListener, Priority.LOW);
    }

    public static void showDialog(Context context, Integer dialogTitleResId, Integer dialogMessageResId, Priority priority) {
        showDialog(context, dialogTitleResId, dialogMessageResId, null, priority);
    }

    public static void showDialog(Context context, Integer dialogTitleResId, Integer dialogMessageResId, DialogInterface.OnClickListener positiveCallback, Priority priority) {
        String dialogTitle = dialogTitleResId != null ? context.getString(dialogTitleResId.intValue()) : null;
        String dialogMessage = dialogMessageResId != null ? context.getString(dialogMessageResId.intValue()) : null;
        String positiveButton = context.getString(R.string.ok);
        showDialog(context, dialogTitle, dialogMessage, positiveButton, positiveCallback, priority);
    }

    public static void showDialog(Context context, Integer dialogTitleResId, Integer dialogMessageResId, int positiveButtonResId, DialogInterface.OnClickListener positiveCallback, Priority priority) {
        String dialogTitle = dialogTitleResId != null ? context.getString(dialogTitleResId.intValue()) : null;
        String dialogMessage = dialogMessageResId != null ? context.getString(dialogMessageResId.intValue()) : null;
        String positiveButton = context.getString(positiveButtonResId);
        showDialog(context, dialogTitle, dialogMessage, positiveButton, positiveCallback, priority);
    }

    public static void showDialog(Context context, Integer dialogTitleResId, Integer dialogMessageResId, DialogInterface.OnClickListener positiveCallback, DialogInterface.OnClickListener negativeCallback, Priority priority) {
        String dialogTitle = dialogTitleResId != null ? context.getString(dialogTitleResId.intValue()) : null;
        String dialogMessage = dialogMessageResId != null ? context.getString(dialogMessageResId.intValue()) : null;
        String positiveButton = context.getString(R.string.ok);
        String negativeButton = context.getString(R.string.cancel);
        showDialog(context, dialogTitle, dialogMessage, positiveButton, positiveCallback, negativeButton, negativeCallback, priority);
    }

    public static void showDialog(Context context, Integer dialogTitleResId, Integer dialogMessageResId, int positiveButtonResId, DialogInterface.OnClickListener positiveCallback, DialogInterface.OnClickListener negativeCallback, Priority priority) {
        String dialogTitle = dialogTitleResId != null ? context.getString(dialogTitleResId.intValue()) : null;
        String dialogMessage = dialogMessageResId != null ? context.getString(dialogMessageResId.intValue()) : null;
        String positiveButton = context.getString(positiveButtonResId);
        showDialog(context, dialogTitle, dialogMessage, positiveButton, positiveCallback, negativeCallback, priority);
    }

    public static void showDialog(Context context, Integer dialogTitleResId, Integer dialogMessageResId, int positiveButtonResId, DialogInterface.OnClickListener positiveCallback, int negativeButtonResId, DialogInterface.OnClickListener negativeCallback, Priority priority) {
        String dialogTitle = dialogTitleResId != null ? context.getString(dialogTitleResId.intValue()) : null;
        String dialogMessage = dialogMessageResId != null ? context.getString(dialogMessageResId.intValue()) : null;
        String positiveButton = context.getString(positiveButtonResId);
        String negativeButton = context.getString(negativeButtonResId);
        showDialog(context, dialogTitle, dialogMessage, positiveButton, positiveCallback, negativeButton, negativeCallback, priority);
    }

    public static void showDialog(Context context, String dialogTitle, String dialogMessage, String positiveButton, DialogInterface.OnClickListener positiveCallback, Priority priority) {
        if (canShowDialog(context, priority)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            AlertDialog.Builder onDismissListener = alertDialogBuilder.setCancelable(false).setOnDismissListener(mDialogDismissedListener);
            if (positiveCallback == null) {
                positiveCallback = mCloseDialog;
            }
            onDismissListener.setPositiveButton(positiveButton, positiveCallback);
            if (dialogTitle != null) {
                alertDialogBuilder.setTitle(dialogTitle);
            }
            if (dialogMessage != null) {
                alertDialogBuilder.setMessage(dialogMessage);
            }
            alertDialogBuilder.create().show();
        }
    }

    public static void showDialog(Context context, CharSequence dialogTitle, CharSequence dialogMessage, String positiveButton, DialogInterface.OnClickListener positiveCallback, DialogInterface.OnClickListener negativeCallback, Priority priority) {
        if (canShowDialog(context, priority)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            AlertDialog.Builder onDismissListener = alertDialogBuilder.setCancelable(true).setOnDismissListener(mDialogDismissedListener);
            if (positiveCallback == null) {
                positiveCallback = mCloseDialog;
            }
            AlertDialog.Builder positiveButton2 = onDismissListener.setPositiveButton(positiveButton, positiveCallback);
            if (negativeCallback == null) {
                negativeCallback = mCloseDialog;
            }
            positiveButton2.setNegativeButton(R.string.cancel, negativeCallback);
            if (dialogTitle != null) {
                alertDialogBuilder.setTitle(dialogTitle);
            }
            if (dialogMessage != null) {
                alertDialogBuilder.setMessage(dialogMessage);
            }
            alertDialogBuilder.create().show();
        }
    }

    private static void showDialog(Context context, String dialogTitle, String dialogMessage, String positiveButton, DialogInterface.OnClickListener positiveCallback, String negativeButton, DialogInterface.OnClickListener negativeCallback, Priority priority) {
        if (canShowDialog(context, priority)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            AlertDialog.Builder onDismissListener = alertDialogBuilder.setCancelable(true).setOnDismissListener(mDialogDismissedListener);
            if (positiveCallback == null) {
                positiveCallback = mCloseDialog;
            }
            AlertDialog.Builder positiveButton2 = onDismissListener.setPositiveButton(positiveButton, positiveCallback);
            if (negativeCallback == null) {
                negativeCallback = mCloseDialog;
            }
            positiveButton2.setNegativeButton(negativeButton, negativeCallback);
            if (dialogTitle != null) {
                alertDialogBuilder.setTitle(dialogTitle);
            }
            if (dialogMessage != null) {
                alertDialogBuilder.setMessage(dialogMessage);
            }
            alertDialogBuilder.create().show();
        }
    }

    private static boolean canShowDialog(Context context, Priority priority) {
        if (context != null) {
            if (priority == Priority.HIGH) {
                mIsDialogVisible.set(true);
                return true;
            }
            return mIsDialogVisible.compareAndSet(false, true);
        }
        return false;
    }
}

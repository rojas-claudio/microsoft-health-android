package com.microsoft.kapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Button;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.DialogManagerProvider;
import com.microsoft.kapp.activities.DialogPriority;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public class DialogManagerImpl implements com.microsoft.kapp.activities.DialogManager {
    private final AtomicBoolean mIsDialogVisible = new AtomicBoolean(false);
    private HashMap<UUID, WeakReference<AlertDialog>> mDialogList = new HashMap<>();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class DialogEventListener implements DialogInterface.OnDismissListener, DialogInterface.OnClickListener {
        private UUID mDialogId;

        public DialogEventListener(UUID id) {
            this.mDialogId = id;
        }

        @Override // android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface dialog) {
            clearState();
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }

        public void clearState() {
            if (DialogManagerImpl.this.mDialogList.containsKey(this.mDialogId)) {
                DialogManagerImpl.this.mDialogList.remove(this.mDialogId);
                DialogManagerImpl.this.mIsDialogVisible.set(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class OnClickListenerWrapper implements DialogInterface.OnClickListener {
        private DialogInterface.OnClickListener mCallback;

        public OnClickListenerWrapper(DialogInterface.OnClickListener callback) {
            this.mCallback = callback;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            this.mCallback.onClick(dialog, which);
        }
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void dismiss() {
        if (this.mIsDialogVisible.get()) {
            for (UUID dialogId : this.mDialogList.keySet()) {
                WeakReference<AlertDialog> weakRefDialog = this.mDialogList.get(dialogId);
                if (weakRefDialog != null && weakRefDialog.get() != null) {
                    weakRefDialog.get().dismiss();
                }
            }
        }
        this.mDialogList.clear();
        this.mIsDialogVisible.set(false);
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void clickConfirm() {
        clickButton(-1);
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void clickCancel() {
        clickButton(-2);
    }

    private void clickButton(int button) {
        Button dialogButton;
        if (this.mIsDialogVisible.get()) {
            for (UUID dialogId : this.mDialogList.keySet()) {
                WeakReference<AlertDialog> weakRefDialog = this.mDialogList.get(dialogId);
                if (weakRefDialog != null && weakRefDialog.get() != null && (dialogButton = weakRefDialog.get().getButton(button)) != null) {
                    dialogButton.performClick();
                }
            }
        }
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public boolean isDialogVisible() {
        return this.mIsDialogVisible.get();
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showNetworkErrorDialog(Context context) {
        showNetworkErrorDialogWithCallback(context, null);
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showNetworkErrorDialogWithCallback(Context context, DialogInterface.OnClickListener clickListener) {
        showDialog(context, Integer.valueOf((int) R.string.network_error_loading_data_title), Integer.valueOf((int) R.string.network_error_loading_data), clickListener, DialogPriority.LOW);
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showMultipleDevicesConnectedError(Context context) {
        showMultipleDevicesConnectedError(context, null);
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showMultipleDevicesConnectedError(final Context context, DialogInterface.OnClickListener dialogListener) {
        showDialog(context, Integer.valueOf((int) R.string.multiple_devices_error_title), Integer.valueOf((int) R.string.multiple_devices_error_text), R.string.multiple_devices_error_button_settings, new OnClickListenerWrapper(new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.utils.DialogManagerImpl.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                if (context != null) {
                    Intent intent = new Intent("android.settings.BLUETOOTH_SETTINGS");
                    context.startActivity(intent);
                }
            }
        }), dialogListener, DialogPriority.LOW);
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showDeviceErrorDialog(Context context) {
        showDeviceErrorDialogWithCallback(context, null);
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showDeviceErrorDialogWithCallback(Context context, DialogInterface.OnClickListener clickListener) {
        showDialog(context, Integer.valueOf((int) R.string.connection_with_device_error_title), Integer.valueOf((int) R.string.band_save_failed_message), clickListener, DialogPriority.LOW);
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showDialog(Context context, Integer dialogTitleResId, Integer dialogMessageResId, DialogPriority priority) {
        showDialog(context, dialogTitleResId, dialogMessageResId, null, priority);
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showDialog(Context context, Integer dialogTitleResId, Integer dialogMessageResId, DialogInterface.OnClickListener positiveCallback, DialogPriority priority) {
        String dialogTitle = dialogTitleResId != null ? context.getString(dialogTitleResId.intValue()) : null;
        String dialogMessage = dialogMessageResId != null ? context.getString(dialogMessageResId.intValue()) : null;
        String positiveButton = context.getString(R.string.ok);
        showDialog(context, dialogTitle, dialogMessage, positiveButton, positiveCallback, priority);
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showDialog(Context context, Integer dialogTitleResId, Integer dialogMessageResId, int positiveButtonResId, DialogInterface.OnClickListener positiveCallback, DialogPriority priority) {
        String dialogTitle = dialogTitleResId != null ? context.getString(dialogTitleResId.intValue()) : null;
        String dialogMessage = dialogMessageResId != null ? context.getString(dialogMessageResId.intValue()) : null;
        String positiveButton = context.getString(positiveButtonResId);
        showDialog(context, dialogTitle, dialogMessage, positiveButton, positiveCallback, priority);
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showDialog(Context context, Integer dialogTitleResId, Integer dialogMessageResId, DialogInterface.OnClickListener positiveCallback, DialogInterface.OnClickListener negativeCallback, DialogPriority priority) {
        String dialogTitle = dialogTitleResId != null ? context.getString(dialogTitleResId.intValue()) : null;
        String dialogMessage = dialogMessageResId != null ? context.getString(dialogMessageResId.intValue()) : null;
        String positiveButton = context.getString(R.string.ok);
        String negativeButton = context.getString(R.string.cancel);
        showDialog(context, dialogTitle, dialogMessage, positiveButton, positiveCallback, negativeButton, negativeCallback, priority);
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showDialog(Context context, Integer dialogTitleResId, Integer dialogMessageResId, int positiveButtonResId, DialogInterface.OnClickListener positiveCallback, DialogInterface.OnClickListener negativeCallback, DialogPriority priority) {
        String dialogTitle = dialogTitleResId != null ? context.getString(dialogTitleResId.intValue()) : null;
        String dialogMessage = dialogMessageResId != null ? context.getString(dialogMessageResId.intValue()) : null;
        String positiveButton = context.getString(positiveButtonResId);
        showDialog(context, dialogTitle, dialogMessage, positiveButton, positiveCallback, negativeCallback, priority);
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showDialog(Context context, Integer dialogTitleResId, Integer dialogMessageResId, int positiveButtonResId, DialogInterface.OnClickListener positiveCallback, int negativeButtonResId, DialogInterface.OnClickListener negativeCallback, DialogPriority priority) {
        String dialogTitle = dialogTitleResId != null ? context.getString(dialogTitleResId.intValue()) : null;
        String dialogMessage = dialogMessageResId != null ? context.getString(dialogMessageResId.intValue()) : null;
        String positiveButton = context.getString(positiveButtonResId);
        String negativeButton = context.getString(negativeButtonResId);
        showDialog(context, dialogTitle, dialogMessage, positiveButton, positiveCallback, negativeButton, negativeCallback, priority);
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showDialog(Context context, String dialogTitle, String dialogMessage, String positiveButton, DialogInterface.OnClickListener positiveCallback, DialogPriority priority) {
        if (canShowDialog(context, priority)) {
            UUID uuid = UUID.randomUUID();
            DialogEventListener dialogListener = new DialogEventListener(uuid);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            AlertDialog.Builder onDismissListener = alertDialogBuilder.setCancelable(false).setOnDismissListener(dialogListener);
            DialogInterface.OnClickListener onClickListener = dialogListener;
            if (positiveCallback != null) {
                onClickListener = new OnClickListenerWrapper(positiveCallback);
            }
            onDismissListener.setPositiveButton(positiveButton, onClickListener);
            if (dialogTitle != null) {
                alertDialogBuilder.setTitle(dialogTitle);
            }
            if (dialogMessage != null) {
                alertDialogBuilder.setMessage(dialogMessage);
            }
            AlertDialog alertDialog = alertDialogBuilder.create();
            WeakReference<AlertDialog> weakRef = new WeakReference<>(alertDialog);
            this.mDialogList.put(uuid, weakRef);
            alertDialog.show();
        }
    }

    @Override // com.microsoft.kapp.activities.DialogManager
    public void showDialog(Context context, CharSequence dialogTitle, CharSequence dialogMessage, String positiveButton, DialogInterface.OnClickListener positiveCallback, final DialogInterface.OnClickListener negativeCallback, DialogPriority priority) {
        if (canShowDialog(context, priority)) {
            UUID uuid = UUID.randomUUID();
            DialogEventListener dialogListener = new DialogEventListener(uuid);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            AlertDialog.Builder positiveButton2 = alertDialogBuilder.setCancelable(true).setOnDismissListener(dialogListener).setPositiveButton(positiveButton, positiveCallback != null ? new OnClickListenerWrapper(positiveCallback) : dialogListener);
            DialogInterface.OnClickListener onClickListener = dialogListener;
            if (negativeCallback != null) {
                onClickListener = new OnClickListenerWrapper(negativeCallback);
            }
            positiveButton2.setNegativeButton(R.string.cancel, onClickListener).setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.microsoft.kapp.utils.DialogManagerImpl.2
                @Override // android.content.DialogInterface.OnKeyListener
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == 4) {
                        dialog.dismiss();
                        if (negativeCallback != null) {
                            negativeCallback.onClick(dialog, 0);
                            return true;
                        }
                        return true;
                    }
                    return true;
                }
            });
            if (dialogTitle != null) {
                alertDialogBuilder.setTitle(dialogTitle);
            }
            if (dialogMessage != null) {
                alertDialogBuilder.setMessage(dialogMessage);
            }
            AlertDialog alertDialog = alertDialogBuilder.create();
            WeakReference<AlertDialog> weakRef = new WeakReference<>(alertDialog);
            this.mDialogList.put(uuid, weakRef);
            alertDialog.show();
        }
    }

    private void showDialog(Context context, String dialogTitle, String dialogMessage, String positiveButton, DialogInterface.OnClickListener positiveCallback, String negativeButton, final DialogInterface.OnClickListener negativeCallback, DialogPriority priority) {
        if (canShowDialog(context, priority)) {
            UUID uuid = UUID.randomUUID();
            DialogEventListener dialogListener = new DialogEventListener(uuid);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            AlertDialog.Builder positiveButton2 = alertDialogBuilder.setCancelable(true).setOnDismissListener(dialogListener).setPositiveButton(positiveButton, positiveCallback != null ? new OnClickListenerWrapper(positiveCallback) : dialogListener);
            DialogInterface.OnClickListener onClickListener = dialogListener;
            if (negativeCallback != null) {
                onClickListener = new OnClickListenerWrapper(negativeCallback);
            }
            positiveButton2.setNegativeButton(negativeButton, onClickListener).setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.microsoft.kapp.utils.DialogManagerImpl.3
                @Override // android.content.DialogInterface.OnKeyListener
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == 4) {
                        dialog.dismiss();
                        if (negativeCallback != null) {
                            negativeCallback.onClick(dialog, 0);
                            return true;
                        }
                        return true;
                    }
                    return true;
                }
            });
            if (dialogTitle != null) {
                alertDialogBuilder.setTitle(dialogTitle);
            }
            if (dialogMessage != null) {
                alertDialogBuilder.setMessage(dialogMessage);
            }
            AlertDialog alertDialog = alertDialogBuilder.create();
            WeakReference<AlertDialog> weakRef = new WeakReference<>(alertDialog);
            this.mDialogList.put(uuid, weakRef);
            alertDialog.show();
        }
    }

    private boolean canShowDialog(Context context, DialogPriority priority) {
        if (context != null) {
            if (priority == DialogPriority.HIGH) {
                this.mIsDialogVisible.set(true);
                return true;
            }
            return this.mIsDialogVisible.compareAndSet(false, true);
        }
        return false;
    }

    public static com.microsoft.kapp.activities.DialogManager getDialogManager(Context context) {
        return context instanceof DialogManagerProvider ? ((DialogManagerProvider) context).getDialogManager() : new StubDialogManager();
    }
}

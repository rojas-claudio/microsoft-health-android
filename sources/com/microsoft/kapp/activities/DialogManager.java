package com.microsoft.kapp.activities;

import android.content.Context;
import android.content.DialogInterface;
/* loaded from: classes.dex */
public interface DialogManager {
    void clickCancel();

    void clickConfirm();

    void dismiss();

    boolean isDialogVisible();

    void showDeviceErrorDialog(Context context);

    void showDeviceErrorDialogWithCallback(Context context, DialogInterface.OnClickListener onClickListener);

    void showDialog(Context context, CharSequence charSequence, CharSequence charSequence2, String str, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogPriority dialogPriority);

    void showDialog(Context context, Integer num, Integer num2, int i, DialogInterface.OnClickListener onClickListener, int i2, DialogInterface.OnClickListener onClickListener2, DialogPriority dialogPriority);

    void showDialog(Context context, Integer num, Integer num2, int i, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogPriority dialogPriority);

    void showDialog(Context context, Integer num, Integer num2, int i, DialogInterface.OnClickListener onClickListener, DialogPriority dialogPriority);

    void showDialog(Context context, Integer num, Integer num2, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2, DialogPriority dialogPriority);

    void showDialog(Context context, Integer num, Integer num2, DialogInterface.OnClickListener onClickListener, DialogPriority dialogPriority);

    void showDialog(Context context, Integer num, Integer num2, DialogPriority dialogPriority);

    void showDialog(Context context, String str, String str2, String str3, DialogInterface.OnClickListener onClickListener, DialogPriority dialogPriority);

    void showMultipleDevicesConnectedError(Context context);

    void showMultipleDevicesConnectedError(Context context, DialogInterface.OnClickListener onClickListener);

    void showNetworkErrorDialog(Context context);

    void showNetworkErrorDialogWithCallback(Context context, DialogInterface.OnClickListener onClickListener);
}

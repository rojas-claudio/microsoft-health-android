package com.microsoft.kapp.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
/* loaded from: classes.dex */
public class ToastUtils {
    public static void showGenericErrorToast(Context context) {
        showLongToast(context, (int) R.string.generic_toast_generic_error_message);
    }

    public static void showLongToast(Fragment fragment, int resourceId) {
        Validate.notNull(fragment, "fragment");
        Activity activity = fragment.getActivity();
        if (activity != null) {
            showLongToast(activity, resourceId);
        }
    }

    public static void showLongToast(Context context, int resourceId) {
        showToast(context, resourceId, 1);
    }

    public static void showLongToast(Context context, String message) {
        showToast(context, message, 1);
    }

    public static void showShortToast(Context context, int resourceId) {
        showToast(context, resourceId, 0);
    }

    public static void showShortToast(Context context, String message) {
        showToast(context, message, 1);
    }

    public static void showToast(Context context, int resourceId, int duration) {
        if (context != null) {
            showToast(context, context.getResources().getString(resourceId), duration);
        }
    }

    public static void showToast(Context context, String message, int duration) {
        if (context != null) {
            Validate.notNullOrEmpty(message, "message");
            Toast toast = Toast.makeText(context, message, duration);
            toast.show();
        }
    }
}

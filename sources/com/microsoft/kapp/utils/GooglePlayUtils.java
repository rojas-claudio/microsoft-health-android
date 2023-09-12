package com.microsoft.kapp.utils;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.microsoft.kapp.diagnostics.Compatibility;
import com.microsoft.kapp.fragments.ErrorDialogFragment;
/* loaded from: classes.dex */
public class GooglePlayUtils {
    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    public void ensureGooglePlayAvailable(FragmentActivity activity) {
        int googlePlayCode;
        Dialog errorDialog;
        if (!Compatibility.inEmulator() && (googlePlayCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity)) != 0 && (errorDialog = GooglePlayServicesUtil.getErrorDialog(googlePlayCode, activity, 9000)) != null) {
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();
            errorFragment.setDialog(errorDialog);
            errorFragment.show(activity.getSupportFragmentManager(), "Location Updates");
        }
    }
}

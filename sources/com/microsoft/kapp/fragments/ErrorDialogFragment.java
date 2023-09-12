package com.microsoft.kapp.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
/* loaded from: classes.dex */
public class ErrorDialogFragment extends DialogFragment {
    private Dialog mDialog = null;

    public void setDialog(Dialog dialog) {
        this.mDialog = dialog;
    }

    @Override // android.support.v4.app.DialogFragment
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return this.mDialog;
    }
}

package com.google.android.gms.internal;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
/* loaded from: classes.dex */
public class dc implements DialogInterface.OnClickListener {
    private final Activity fD;
    private final int ky;
    private final Intent mIntent;

    public dc(Activity activity, Intent intent, int i) {
        this.fD = activity;
        this.mIntent = intent;
        this.ky = i;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        try {
            if (this.mIntent != null) {
                this.fD.startActivityForResult(this.mIntent, this.ky);
            }
            dialog.dismiss();
        } catch (ActivityNotFoundException e) {
            Log.e("SettingsRedirect", "Can't redirect to app settings for Google Play services");
        }
    }
}

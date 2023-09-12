package com.google.android.gms.ads;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.gms.internal.bm;
import com.google.android.gms.internal.bn;
import com.google.android.gms.internal.cn;
/* loaded from: classes.dex */
public final class AdActivity extends Activity {
    public static final String CLASS_NAME = "com.google.android.gms.ads.AdActivity";
    public static final String SIMPLE_CLASS_NAME = "AdActivity";
    private bn dH;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dH = bm.a(this);
        if (this.dH == null) {
            cn.q("Could not create ad overlay.");
            finish();
            return;
        }
        try {
            this.dH.onCreate(savedInstanceState);
        } catch (RemoteException e) {
            cn.b("Could not forward onCreate to ad overlay:", e);
            finish();
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        try {
            if (this.dH != null) {
                this.dH.onDestroy();
            }
        } catch (RemoteException e) {
            cn.b("Could not forward onDestroy to ad overlay:", e);
        }
        super.onDestroy();
    }

    @Override // android.app.Activity
    protected void onPause() {
        try {
            if (this.dH != null) {
                this.dH.onPause();
            }
        } catch (RemoteException e) {
            cn.b("Could not forward onPause to ad overlay:", e);
            finish();
        }
        super.onPause();
    }

    @Override // android.app.Activity
    protected void onRestart() {
        super.onRestart();
        try {
            if (this.dH != null) {
                this.dH.onRestart();
            }
        } catch (RemoteException e) {
            cn.b("Could not forward onRestart to ad overlay:", e);
            finish();
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        try {
            if (this.dH != null) {
                this.dH.onResume();
            }
        } catch (RemoteException e) {
            cn.b("Could not forward onResume to ad overlay:", e);
            finish();
        }
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        try {
            if (this.dH != null) {
                this.dH.onSaveInstanceState(outState);
            }
        } catch (RemoteException e) {
            cn.b("Could not forward onSaveInstanceState to ad overlay:", e);
            finish();
        }
        super.onSaveInstanceState(outState);
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        try {
            if (this.dH != null) {
                this.dH.onStart();
            }
        } catch (RemoteException e) {
            cn.b("Could not forward onStart to ad overlay:", e);
            finish();
        }
    }

    @Override // android.app.Activity
    protected void onStop() {
        try {
            if (this.dH != null) {
                this.dH.onStop();
            }
        } catch (RemoteException e) {
            cn.b("Could not forward onStop to ad overlay:", e);
            finish();
        }
        super.onStop();
    }
}

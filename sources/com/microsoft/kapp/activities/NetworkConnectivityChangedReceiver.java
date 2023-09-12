package com.microsoft.kapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.microsoft.kapp.utils.CommonUtils;
/* loaded from: classes.dex */
public class NetworkConnectivityChangedReceiver extends BroadcastReceiver {
    private static final IntentFilter INTENT_CONNECTIVITY_FILTER = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    private OnNetworkConnectivityChanged mListener;

    /* loaded from: classes.dex */
    public interface OnNetworkConnectivityChanged {
        void onNetworkConnected();

        void onNetworkDisconnected();
    }

    public NetworkConnectivityChangedReceiver(OnNetworkConnectivityChanged listener) {
        this.mListener = listener;
    }

    public void register(Context context) {
        context.registerReceiver(this, INTENT_CONNECTIVITY_FILTER);
    }

    public void unregister(Context context) {
        context.unregisterReceiver(this);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (!extras.getBoolean("noConnectivity") && CommonUtils.isNetworkAvailable(context)) {
            this.mListener.onNetworkConnected();
        } else {
            this.mListener.onNetworkDisconnected();
        }
    }
}

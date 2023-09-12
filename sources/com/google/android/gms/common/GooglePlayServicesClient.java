package com.google.android.gms.common;

import android.os.Bundle;
/* loaded from: classes.dex */
public interface GooglePlayServicesClient {

    /* loaded from: classes.dex */
    public interface ConnectionCallbacks {
        void onConnected(Bundle bundle);

        void onDisconnected();
    }

    /* loaded from: classes.dex */
    public interface OnConnectionFailedListener {
        void onConnectionFailed(ConnectionResult connectionResult);
    }

    void connect();

    void disconnect();

    boolean isConnected();

    boolean isConnecting();

    boolean isConnectionCallbacksRegistered(ConnectionCallbacks connectionCallbacks);

    boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener onConnectionFailedListener);

    void registerConnectionCallbacks(ConnectionCallbacks connectionCallbacks);

    void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener);

    void unregisterConnectionCallbacks(ConnectionCallbacks connectionCallbacks);

    void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener);
}

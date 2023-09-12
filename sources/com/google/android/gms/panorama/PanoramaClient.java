package com.google.android.gms.panorama;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.internal.ff;
/* loaded from: classes.dex */
public class PanoramaClient implements GooglePlayServicesClient {
    private final ff qU;

    /* loaded from: classes.dex */
    public interface OnPanoramaInfoLoadedListener {
        void onPanoramaInfoLoaded(ConnectionResult connectionResult, Intent intent);
    }

    /* loaded from: classes.dex */
    public interface a {
        void a(ConnectionResult connectionResult, int i, Intent intent);
    }

    public PanoramaClient(Context context, GooglePlayServicesClient.ConnectionCallbacks connectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener connectionFailedListener) {
        this.qU = new ff(context, connectionCallbacks, connectionFailedListener);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void connect() {
        this.qU.connect();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void disconnect() {
        this.qU.disconnect();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnected() {
        return this.qU.isConnected();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnecting() {
        return this.qU.isConnecting();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnectionCallbacksRegistered(GooglePlayServicesClient.ConnectionCallbacks listener) {
        return this.qU.isConnectionCallbacksRegistered(listener);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnectionFailedListenerRegistered(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        return this.qU.isConnectionFailedListenerRegistered(listener);
    }

    public void loadPanoramaInfo(OnPanoramaInfoLoadedListener listener, Uri uri) {
        this.qU.a(listener, uri, false);
    }

    public void loadPanoramaInfoAndGrantAccess(OnPanoramaInfoLoadedListener listener, Uri uri) {
        this.qU.a(listener, uri, true);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void registerConnectionCallbacks(GooglePlayServicesClient.ConnectionCallbacks listener) {
        this.qU.registerConnectionCallbacks(listener);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void registerConnectionFailedListener(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        this.qU.registerConnectionFailedListener(listener);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void unregisterConnectionCallbacks(GooglePlayServicesClient.ConnectionCallbacks listener) {
        this.qU.unregisterConnectionCallbacks(listener);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void unregisterConnectionFailedListener(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        this.qU.unregisterConnectionFailedListener(listener);
    }
}

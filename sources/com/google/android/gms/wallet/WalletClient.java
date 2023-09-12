package com.google.android.gms.wallet;

import android.app.Activity;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.internal.gj;
/* loaded from: classes.dex */
public class WalletClient implements GooglePlayServicesClient {
    private final gj uq;

    public WalletClient(Activity activity, int environment, String accountName, int theme, GooglePlayServicesClient.ConnectionCallbacks connectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener connectionFailedListener) {
        this.uq = new gj(activity, connectionCallbacks, connectionFailedListener, environment, accountName, theme);
    }

    public WalletClient(Activity activity, int environment, String accountName, GooglePlayServicesClient.ConnectionCallbacks connectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener connectionFailedListener) {
        this(activity, environment, accountName, 0, connectionCallbacks, connectionFailedListener);
    }

    public void changeMaskedWallet(String googleTransactionId, String merchantTransactionId, int requestCode) {
        this.uq.changeMaskedWallet(googleTransactionId, merchantTransactionId, requestCode);
    }

    public void checkForPreAuthorization(int requestCode) {
        this.uq.checkForPreAuthorization(requestCode);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void connect() {
        this.uq.connect();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void disconnect() {
        this.uq.disconnect();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnected() {
        return this.uq.isConnected();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnecting() {
        return this.uq.isConnecting();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnectionCallbacksRegistered(GooglePlayServicesClient.ConnectionCallbacks listener) {
        return this.uq.isConnectionCallbacksRegistered(listener);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnectionFailedListenerRegistered(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        return this.uq.isConnectionFailedListenerRegistered(listener);
    }

    public void loadFullWallet(FullWalletRequest request, int requestCode) {
        this.uq.loadFullWallet(request, requestCode);
    }

    public void loadMaskedWallet(MaskedWalletRequest request, int requestCode) {
        this.uq.loadMaskedWallet(request, requestCode);
    }

    public void notifyTransactionStatus(NotifyTransactionStatusRequest request) {
        this.uq.notifyTransactionStatus(request);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void registerConnectionCallbacks(GooglePlayServicesClient.ConnectionCallbacks listener) {
        this.uq.registerConnectionCallbacks(listener);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void registerConnectionFailedListener(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        this.uq.registerConnectionFailedListener(listener);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void unregisterConnectionCallbacks(GooglePlayServicesClient.ConnectionCallbacks listener) {
        this.uq.unregisterConnectionCallbacks(listener);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void unregisterConnectionFailedListener(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        this.uq.unregisterConnectionFailedListener(listener);
    }
}

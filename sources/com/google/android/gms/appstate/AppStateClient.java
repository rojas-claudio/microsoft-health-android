package com.google.android.gms.appstate;

import android.content.Context;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.internal.cw;
import com.google.android.gms.internal.dm;
/* loaded from: classes.dex */
public final class AppStateClient implements GooglePlayServicesClient {
    public static final int STATUS_CLIENT_RECONNECT_REQUIRED = 2;
    public static final int STATUS_DEVELOPER_ERROR = 7;
    public static final int STATUS_INTERNAL_ERROR = 1;
    public static final int STATUS_NETWORK_ERROR_NO_DATA = 4;
    public static final int STATUS_NETWORK_ERROR_OPERATION_DEFERRED = 5;
    public static final int STATUS_NETWORK_ERROR_OPERATION_FAILED = 6;
    public static final int STATUS_NETWORK_ERROR_STALE_DATA = 3;
    public static final int STATUS_OK = 0;
    public static final int STATUS_STATE_KEY_LIMIT_EXCEEDED = 2003;
    public static final int STATUS_STATE_KEY_NOT_FOUND = 2002;
    public static final int STATUS_WRITE_OUT_OF_DATE_VERSION = 2000;
    public static final int STATUS_WRITE_SIZE_EXCEEDED = 2001;
    private final cw io;

    /* loaded from: classes.dex */
    public static final class Builder {
        private static final String[] ip = {Scopes.APP_STATE};
        private GooglePlayServicesClient.ConnectionCallbacks iq;
        private GooglePlayServicesClient.OnConnectionFailedListener ir;
        private String[] is = ip;
        private String it = "<<default account>>";
        private Context mContext;

        public Builder(Context context, GooglePlayServicesClient.ConnectionCallbacks connectedListener, GooglePlayServicesClient.OnConnectionFailedListener connectionFailedListener) {
            this.mContext = context;
            this.iq = connectedListener;
            this.ir = connectionFailedListener;
        }

        public AppStateClient create() {
            return new AppStateClient(this.mContext, this.iq, this.ir, this.it, this.is);
        }

        public Builder setAccountName(String accountName) {
            this.it = (String) dm.e(accountName);
            return this;
        }

        public Builder setScopes(String... scopes) {
            this.is = scopes;
            return this;
        }
    }

    private AppStateClient(Context context, GooglePlayServicesClient.ConnectionCallbacks connectedListener, GooglePlayServicesClient.OnConnectionFailedListener connectionFailedListener, String accountName, String[] scopes) {
        this.io = new cw(context, connectedListener, connectionFailedListener, accountName, scopes);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void connect() {
        this.io.connect();
    }

    public void deleteState(OnStateDeletedListener listener, int stateKey) {
        this.io.deleteState(listener, stateKey);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void disconnect() {
        this.io.disconnect();
    }

    public int getMaxNumKeys() {
        return this.io.getMaxNumKeys();
    }

    public int getMaxStateSize() {
        return this.io.getMaxStateSize();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnected() {
        return this.io.isConnected();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnecting() {
        return this.io.isConnecting();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnectionCallbacksRegistered(GooglePlayServicesClient.ConnectionCallbacks listener) {
        return this.io.isConnectionCallbacksRegistered(listener);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnectionFailedListenerRegistered(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        return this.io.isConnectionFailedListenerRegistered(listener);
    }

    public void listStates(OnStateListLoadedListener listener) {
        this.io.listStates(listener);
    }

    public void loadState(OnStateLoadedListener listener, int stateKey) {
        this.io.loadState(listener, stateKey);
    }

    public void reconnect() {
        this.io.disconnect();
        this.io.connect();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void registerConnectionCallbacks(GooglePlayServicesClient.ConnectionCallbacks listener) {
        this.io.registerConnectionCallbacks(listener);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void registerConnectionFailedListener(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        this.io.registerConnectionFailedListener(listener);
    }

    public void resolveState(OnStateLoadedListener listener, int stateKey, String resolvedVersion, byte[] resolvedData) {
        this.io.resolveState(listener, stateKey, resolvedVersion, resolvedData);
    }

    public void signOut() {
        this.io.signOut(null);
    }

    public void signOut(OnSignOutCompleteListener listener) {
        dm.a(listener, "Must provide a valid listener");
        this.io.signOut(listener);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void unregisterConnectionCallbacks(GooglePlayServicesClient.ConnectionCallbacks listener) {
        this.io.unregisterConnectionCallbacks(listener);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void unregisterConnectionFailedListener(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        this.io.unregisterConnectionFailedListener(listener);
    }

    public void updateState(int stateKey, byte[] data) {
        this.io.a((OnStateLoadedListener) null, stateKey, data);
    }

    public void updateStateImmediate(OnStateLoadedListener listener, int stateKey, byte[] data) {
        dm.a(listener, "Must provide a valid listener");
        this.io.a(listener, stateKey, data);
    }
}

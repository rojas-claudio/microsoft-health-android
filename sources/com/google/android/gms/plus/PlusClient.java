package com.google.android.gms.plus;

import android.content.Context;
import android.net.Uri;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.internal.fl;
import com.google.android.gms.internal.fo;
import com.google.android.gms.plus.model.moments.Moment;
import com.google.android.gms.plus.model.moments.MomentBuffer;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import java.util.Collection;
/* loaded from: classes.dex */
public class PlusClient implements GooglePlayServicesClient {
    final fl rb;

    /* loaded from: classes.dex */
    public static class Builder {
        private final GooglePlayServicesClient.OnConnectionFailedListener ir;
        private final Context mContext;
        private final GooglePlayServicesClient.ConnectionCallbacks rc;
        private final fo rd;

        public Builder(Context context, GooglePlayServicesClient.ConnectionCallbacks connectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener connectionFailedListener) {
            this.mContext = context;
            this.rc = connectionCallbacks;
            this.ir = connectionFailedListener;
            this.rd = new fo(this.mContext);
        }

        public PlusClient build() {
            return new PlusClient(new fl(this.mContext, this.rd.dh(), this.rc, this.ir));
        }

        public Builder clearScopes() {
            this.rd.dg();
            return this;
        }

        public Builder setAccountName(String accountName) {
            this.rd.Z(accountName);
            return this;
        }

        public Builder setActions(String... actions) {
            this.rd.e(actions);
            return this;
        }

        public Builder setScopes(String... scopes) {
            this.rd.d(scopes);
            return this;
        }
    }

    /* loaded from: classes.dex */
    public interface OnAccessRevokedListener {
        void onAccessRevoked(ConnectionResult connectionResult);
    }

    /* loaded from: classes.dex */
    public interface OnMomentsLoadedListener {
        void onMomentsLoaded(ConnectionResult connectionResult, MomentBuffer momentBuffer, String str, String str2);
    }

    /* loaded from: classes.dex */
    public interface OnPeopleLoadedListener {
        void onPeopleLoaded(ConnectionResult connectionResult, PersonBuffer personBuffer, String str);
    }

    /* loaded from: classes.dex */
    public interface OrderBy {
        public static final int ALPHABETICAL = 0;
        public static final int BEST = 1;
    }

    PlusClient(fl plusClientImpl) {
        this.rb = plusClientImpl;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public fl cR() {
        return this.rb;
    }

    public void clearDefaultAccount() {
        this.rb.clearDefaultAccount();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void connect() {
        this.rb.connect();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void disconnect() {
        this.rb.disconnect();
    }

    public String getAccountName() {
        return this.rb.getAccountName();
    }

    public Person getCurrentPerson() {
        return this.rb.getCurrentPerson();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnected() {
        return this.rb.isConnected();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnecting() {
        return this.rb.isConnecting();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnectionCallbacksRegistered(GooglePlayServicesClient.ConnectionCallbacks listener) {
        return this.rb.isConnectionCallbacksRegistered(listener);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnectionFailedListenerRegistered(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        return this.rb.isConnectionFailedListenerRegistered(listener);
    }

    public void loadMoments(OnMomentsLoadedListener listener) {
        this.rb.loadMoments(listener);
    }

    public void loadMoments(OnMomentsLoadedListener listener, int maxResults, String pageToken, Uri targetUrl, String type, String userId) {
        this.rb.loadMoments(listener, maxResults, pageToken, targetUrl, type, userId);
    }

    public void loadPeople(OnPeopleLoadedListener listener, Collection<String> personIds) {
        this.rb.a(listener, personIds);
    }

    public void loadPeople(OnPeopleLoadedListener listener, String... personIds) {
        this.rb.a(listener, personIds);
    }

    public void loadVisiblePeople(OnPeopleLoadedListener listener, int orderBy, String pageToken) {
        this.rb.loadVisiblePeople(listener, orderBy, pageToken);
    }

    public void loadVisiblePeople(OnPeopleLoadedListener listener, String pageToken) {
        this.rb.loadVisiblePeople(listener, pageToken);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void registerConnectionCallbacks(GooglePlayServicesClient.ConnectionCallbacks listener) {
        this.rb.registerConnectionCallbacks(listener);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void registerConnectionFailedListener(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        this.rb.registerConnectionFailedListener(listener);
    }

    public void removeMoment(String momentId) {
        this.rb.removeMoment(momentId);
    }

    public void revokeAccessAndDisconnect(OnAccessRevokedListener listener) {
        this.rb.revokeAccessAndDisconnect(listener);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void unregisterConnectionCallbacks(GooglePlayServicesClient.ConnectionCallbacks listener) {
        this.rb.unregisterConnectionCallbacks(listener);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void unregisterConnectionFailedListener(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        this.rb.unregisterConnectionFailedListener(listener);
    }

    public void writeMoment(Moment moment) {
        this.rb.writeMoment(moment);
    }
}

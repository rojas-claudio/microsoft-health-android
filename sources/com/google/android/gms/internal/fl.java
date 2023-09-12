package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.de;
import com.google.android.gms.internal.fk;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.moments.Moment;
import com.google.android.gms.plus.model.moments.MomentBuffer;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
/* loaded from: classes.dex */
public class fl extends de<fk> implements GooglePlayServicesClient {
    private Person ro;
    private fn rp;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class a extends fg {
        private final PlusClient.OnMomentsLoadedListener rq;

        public a(PlusClient.OnMomentsLoadedListener onMomentsLoadedListener) {
            this.rq = onMomentsLoadedListener;
        }

        @Override // com.google.android.gms.internal.fg, com.google.android.gms.internal.fh
        public void a(com.google.android.gms.common.data.d dVar, String str, String str2) {
            com.google.android.gms.common.data.d dVar2;
            ConnectionResult connectionResult = new ConnectionResult(dVar.getStatusCode(), dVar.aM() != null ? (PendingIntent) dVar.aM().getParcelable("pendingIntent") : null);
            if (connectionResult.isSuccess() || dVar == null) {
                dVar2 = dVar;
            } else {
                if (!dVar.isClosed()) {
                    dVar.close();
                }
                dVar2 = null;
            }
            fl.this.a(new b(this.rq, connectionResult, dVar2, str, str2));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class b extends de<fk>.c<PlusClient.OnMomentsLoadedListener> {
        private final ConnectionResult rs;
        private final String rt;
        private final String ru;

        public b(PlusClient.OnMomentsLoadedListener onMomentsLoadedListener, ConnectionResult connectionResult, com.google.android.gms.common.data.d dVar, String str, String str2) {
            super(onMomentsLoadedListener, dVar);
            this.rs = connectionResult;
            this.rt = str;
            this.ru = str2;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.de.c
        public void a(PlusClient.OnMomentsLoadedListener onMomentsLoadedListener, com.google.android.gms.common.data.d dVar) {
            onMomentsLoadedListener.onMomentsLoaded(this.rs, dVar != null ? new MomentBuffer(dVar) : null, this.rt, this.ru);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class c extends fg {
        private final PlusClient.OnPeopleLoadedListener rv;

        public c(PlusClient.OnPeopleLoadedListener onPeopleLoadedListener) {
            this.rv = onPeopleLoadedListener;
        }

        @Override // com.google.android.gms.internal.fg, com.google.android.gms.internal.fh
        public void a(com.google.android.gms.common.data.d dVar, String str) {
            com.google.android.gms.common.data.d dVar2;
            ConnectionResult connectionResult = new ConnectionResult(dVar.getStatusCode(), dVar.aM() != null ? (PendingIntent) dVar.aM().getParcelable("pendingIntent") : null);
            if (connectionResult.isSuccess() || dVar == null) {
                dVar2 = dVar;
            } else {
                if (!dVar.isClosed()) {
                    dVar.close();
                }
                dVar2 = null;
            }
            fl.this.a(new d(this.rv, connectionResult, dVar2, str));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class d extends de<fk>.c<PlusClient.OnPeopleLoadedListener> {
        private final ConnectionResult rs;
        private final String rt;

        public d(PlusClient.OnPeopleLoadedListener onPeopleLoadedListener, ConnectionResult connectionResult, com.google.android.gms.common.data.d dVar, String str) {
            super(onPeopleLoadedListener, dVar);
            this.rs = connectionResult;
            this.rt = str;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.de.c
        public void a(PlusClient.OnPeopleLoadedListener onPeopleLoadedListener, com.google.android.gms.common.data.d dVar) {
            onPeopleLoadedListener.onPeopleLoaded(this.rs, dVar != null ? new PersonBuffer(dVar) : null, this.rt);
        }
    }

    /* loaded from: classes.dex */
    final class e extends fg {
        private final PlusClient.OnAccessRevokedListener rw;

        public e(PlusClient.OnAccessRevokedListener onAccessRevokedListener) {
            this.rw = onAccessRevokedListener;
        }

        @Override // com.google.android.gms.internal.fg, com.google.android.gms.internal.fh
        public void b(int i, Bundle bundle) {
            fl.this.a(new f(this.rw, new ConnectionResult(i, bundle != null ? (PendingIntent) bundle.getParcelable("pendingIntent") : null)));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class f extends de<fk>.b<PlusClient.OnAccessRevokedListener> {
        private final ConnectionResult rs;

        public f(PlusClient.OnAccessRevokedListener onAccessRevokedListener, ConnectionResult connectionResult) {
            super(onAccessRevokedListener);
            this.rs = connectionResult;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.de.b
        public void a(PlusClient.OnAccessRevokedListener onAccessRevokedListener) {
            fl.this.disconnect();
            if (onAccessRevokedListener != null) {
                onAccessRevokedListener.onAccessRevoked(this.rs);
            }
        }

        @Override // com.google.android.gms.internal.de.b
        protected void aF() {
        }
    }

    public fl(Context context, fn fnVar, GooglePlayServicesClient.ConnectionCallbacks connectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, connectionCallbacks, onConnectionFailedListener, fnVar.cZ());
        this.rp = fnVar;
    }

    public boolean Y(String str) {
        return Arrays.asList(aY()).contains(str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.de
    public void a(int i, IBinder iBinder, Bundle bundle) {
        if (i == 0 && bundle != null && bundle.containsKey("loaded_person")) {
            this.ro = fv.e(bundle.getByteArray("loaded_person"));
        }
        super.a(i, iBinder, bundle);
    }

    @Override // com.google.android.gms.internal.de
    protected void a(dj djVar, de.d dVar) throws RemoteException {
        Bundle bundle = new Bundle();
        bundle.putString("client_id", this.rp.df());
        bundle.putStringArray("request_visible_actions", this.rp.da());
        djVar.a(dVar, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, this.rp.dd(), this.rp.dc(), aY(), this.rp.getAccountName(), bundle);
    }

    public void a(PlusClient.OnPeopleLoadedListener onPeopleLoadedListener, Collection<String> collection) {
        bc();
        c cVar = new c(onPeopleLoadedListener);
        try {
            bd().a(cVar, new ArrayList(collection));
        } catch (RemoteException e2) {
            cVar.a(com.google.android.gms.common.data.d.r(8), null);
        }
    }

    public void a(PlusClient.OnPeopleLoadedListener onPeopleLoadedListener, String[] strArr) {
        a(onPeopleLoadedListener, Arrays.asList(strArr));
    }

    @Override // com.google.android.gms.internal.de
    protected String ag() {
        return "com.google.android.gms.plus.service.START";
    }

    @Override // com.google.android.gms.internal.de
    protected String ah() {
        return "com.google.android.gms.plus.internal.IPlusService";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.de
    /* renamed from: ar */
    public fk p(IBinder iBinder) {
        return fk.a.aq(iBinder);
    }

    public void clearDefaultAccount() {
        bc();
        try {
            this.ro = null;
            bd().clearDefaultAccount();
        } catch (RemoteException e2) {
            throw new IllegalStateException(e2);
        }
    }

    public String getAccountName() {
        bc();
        try {
            return bd().getAccountName();
        } catch (RemoteException e2) {
            throw new IllegalStateException(e2);
        }
    }

    public Person getCurrentPerson() {
        bc();
        return this.ro;
    }

    public void loadMoments(PlusClient.OnMomentsLoadedListener listener) {
        loadMoments(listener, 20, null, null, null, "me");
    }

    public void loadMoments(PlusClient.OnMomentsLoadedListener listener, int maxResults, String pageToken, Uri targetUrl, String type, String userId) {
        bc();
        a aVar = listener != null ? new a(listener) : null;
        try {
            bd().a(aVar, maxResults, pageToken, targetUrl, type, userId);
        } catch (RemoteException e2) {
            aVar.a(com.google.android.gms.common.data.d.r(8), (String) null, (String) null);
        }
    }

    public void loadVisiblePeople(PlusClient.OnPeopleLoadedListener listener, int orderBy, String pageToken) {
        bc();
        c cVar = new c(listener);
        try {
            bd().a(cVar, 1, orderBy, -1, pageToken);
        } catch (RemoteException e2) {
            cVar.a(com.google.android.gms.common.data.d.r(8), null);
        }
    }

    public void loadVisiblePeople(PlusClient.OnPeopleLoadedListener listener, String pageToken) {
        loadVisiblePeople(listener, 0, pageToken);
    }

    public void removeMoment(String momentId) {
        bc();
        try {
            bd().removeMoment(momentId);
        } catch (RemoteException e2) {
            throw new IllegalStateException(e2);
        }
    }

    public void revokeAccessAndDisconnect(PlusClient.OnAccessRevokedListener listener) {
        bc();
        clearDefaultAccount();
        e eVar = new e(listener);
        try {
            bd().b(eVar);
        } catch (RemoteException e2) {
            eVar.b(8, null);
        }
    }

    public void writeMoment(Moment moment) {
        bc();
        try {
            bd().a(ec.a((fs) moment));
        } catch (RemoteException e2) {
            throw new IllegalStateException(e2);
        }
    }
}

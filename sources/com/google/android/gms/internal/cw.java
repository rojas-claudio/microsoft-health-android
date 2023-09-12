package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.appstate.AppState;
import com.google.android.gms.appstate.AppStateBuffer;
import com.google.android.gms.appstate.OnSignOutCompleteListener;
import com.google.android.gms.appstate.OnStateDeletedListener;
import com.google.android.gms.appstate.OnStateListLoadedListener;
import com.google.android.gms.appstate.OnStateLoadedListener;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.internal.cy;
import com.google.android.gms.internal.de;
/* loaded from: classes.dex */
public final class cw extends de<cy> {
    private final String it;

    /* loaded from: classes.dex */
    final class a extends cv {
        private final OnStateDeletedListener iA;

        public a(OnStateDeletedListener onStateDeletedListener) {
            this.iA = (OnStateDeletedListener) dm.a(onStateDeletedListener, "Listener must not be null");
        }

        @Override // com.google.android.gms.internal.cv, com.google.android.gms.internal.cx
        public void onStateDeleted(int statusCode, int stateKey) {
            cw.this.a(new b(this.iA, statusCode, stateKey));
        }
    }

    /* loaded from: classes.dex */
    final class b extends de<cy>.b<OnStateDeletedListener> {
        private final int iC;
        private final int iD;

        public b(OnStateDeletedListener onStateDeletedListener, int i, int i2) {
            super(onStateDeletedListener);
            this.iC = i;
            this.iD = i2;
        }

        @Override // com.google.android.gms.internal.de.b
        public void a(OnStateDeletedListener onStateDeletedListener) {
            onStateDeletedListener.onStateDeleted(this.iC, this.iD);
        }

        @Override // com.google.android.gms.internal.de.b
        protected void aF() {
        }
    }

    /* loaded from: classes.dex */
    final class c extends cv {
        private final OnStateListLoadedListener iE;

        public c(OnStateListLoadedListener onStateListLoadedListener) {
            this.iE = (OnStateListLoadedListener) dm.a(onStateListLoadedListener, "Listener must not be null");
        }

        @Override // com.google.android.gms.internal.cv, com.google.android.gms.internal.cx
        public void a(com.google.android.gms.common.data.d dVar) {
            cw.this.a(new d(this.iE, dVar));
        }
    }

    /* loaded from: classes.dex */
    final class d extends de<cy>.c<OnStateListLoadedListener> {
        public d(OnStateListLoadedListener onStateListLoadedListener, com.google.android.gms.common.data.d dVar) {
            super(onStateListLoadedListener, dVar);
        }

        @Override // com.google.android.gms.internal.de.c
        public void a(OnStateListLoadedListener onStateListLoadedListener, com.google.android.gms.common.data.d dVar) {
            onStateListLoadedListener.onStateListLoaded(dVar.getStatusCode(), new AppStateBuffer(dVar));
        }
    }

    /* loaded from: classes.dex */
    final class e extends cv {
        private final OnStateLoadedListener iF;

        public e(OnStateLoadedListener onStateLoadedListener) {
            this.iF = (OnStateLoadedListener) dm.a(onStateLoadedListener, "Listener must not be null");
        }

        @Override // com.google.android.gms.internal.cv, com.google.android.gms.internal.cx
        public void a(int i, com.google.android.gms.common.data.d dVar) {
            cw.this.a(new f(this.iF, i, dVar));
        }
    }

    /* loaded from: classes.dex */
    final class f extends de<cy>.c<OnStateLoadedListener> {
        private final int iD;

        public f(OnStateLoadedListener onStateLoadedListener, int i, com.google.android.gms.common.data.d dVar) {
            super(onStateLoadedListener, dVar);
            this.iD = i;
        }

        @Override // com.google.android.gms.internal.de.c
        public void a(OnStateLoadedListener onStateLoadedListener, com.google.android.gms.common.data.d dVar) {
            byte[] bArr;
            String str;
            byte[] bArr2 = null;
            AppStateBuffer appStateBuffer = new AppStateBuffer(dVar);
            try {
                if (appStateBuffer.getCount() > 0) {
                    AppState appState = appStateBuffer.get(0);
                    str = appState.getConflictVersion();
                    bArr = appState.getLocalData();
                    bArr2 = appState.getConflictData();
                } else {
                    bArr = null;
                    str = null;
                }
                appStateBuffer.close();
                int statusCode = dVar.getStatusCode();
                if (statusCode == 2000) {
                    onStateLoadedListener.onStateConflict(this.iD, str, bArr, bArr2);
                } else {
                    onStateLoadedListener.onStateLoaded(statusCode, this.iD, bArr);
                }
            } catch (Throwable th) {
                appStateBuffer.close();
                throw th;
            }
        }
    }

    /* loaded from: classes.dex */
    final class g extends cv {
        private final OnSignOutCompleteListener iG;

        public g(OnSignOutCompleteListener onSignOutCompleteListener) {
            this.iG = (OnSignOutCompleteListener) dm.a(onSignOutCompleteListener, "Listener must not be null");
        }

        @Override // com.google.android.gms.internal.cv, com.google.android.gms.internal.cx
        public void onSignOutComplete() {
            cw.this.a(new h(this.iG));
        }
    }

    /* loaded from: classes.dex */
    final class h extends de<cy>.b<OnSignOutCompleteListener> {
        public h(OnSignOutCompleteListener onSignOutCompleteListener) {
            super(onSignOutCompleteListener);
        }

        @Override // com.google.android.gms.internal.de.b
        public void a(OnSignOutCompleteListener onSignOutCompleteListener) {
            onSignOutCompleteListener.onSignOutComplete();
        }

        @Override // com.google.android.gms.internal.de.b
        protected void aF() {
        }
    }

    public cw(Context context, GooglePlayServicesClient.ConnectionCallbacks connectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener onConnectionFailedListener, String str, String[] strArr) {
        super(context, connectionCallbacks, onConnectionFailedListener, strArr);
        this.it = (String) dm.e(str);
    }

    public void a(OnStateLoadedListener onStateLoadedListener, int i, byte[] bArr) {
        e eVar;
        if (onStateLoadedListener == null) {
            eVar = null;
        } else {
            try {
                eVar = new e(onStateLoadedListener);
            } catch (RemoteException e2) {
                Log.w("AppStateClient", "service died");
                return;
            }
        }
        bd().a(eVar, i, bArr);
    }

    @Override // com.google.android.gms.internal.de
    protected void a(dj djVar, de.d dVar) throws RemoteException {
        djVar.a(dVar, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, getContext().getPackageName(), this.it, aY());
    }

    @Override // com.google.android.gms.internal.de
    protected void a(String... strArr) {
        boolean z = false;
        for (String str : strArr) {
            if (str.equals(Scopes.APP_STATE)) {
                z = true;
            }
        }
        dm.a(z, String.format("AppStateClient requires %s to function.", Scopes.APP_STATE));
    }

    @Override // com.google.android.gms.internal.de
    protected String ag() {
        return "com.google.android.gms.appstate.service.START";
    }

    @Override // com.google.android.gms.internal.de
    protected String ah() {
        return "com.google.android.gms.appstate.internal.IAppStateService";
    }

    public void deleteState(OnStateDeletedListener listener, int stateKey) {
        try {
            bd().b(new a(listener), stateKey);
        } catch (RemoteException e2) {
            Log.w("AppStateClient", "service died");
        }
    }

    public int getMaxNumKeys() {
        try {
            return bd().getMaxNumKeys();
        } catch (RemoteException e2) {
            Log.w("AppStateClient", "service died");
            return 2;
        }
    }

    public int getMaxStateSize() {
        try {
            return bd().getMaxStateSize();
        } catch (RemoteException e2) {
            Log.w("AppStateClient", "service died");
            return 2;
        }
    }

    public void listStates(OnStateListLoadedListener listener) {
        try {
            bd().a(new c(listener));
        } catch (RemoteException e2) {
            Log.w("AppStateClient", "service died");
        }
    }

    public void loadState(OnStateLoadedListener listener, int stateKey) {
        try {
            bd().a(new e(listener), stateKey);
        } catch (RemoteException e2) {
            Log.w("AppStateClient", "service died");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.de
    /* renamed from: r */
    public cy p(IBinder iBinder) {
        return cy.a.t(iBinder);
    }

    public void resolveState(OnStateLoadedListener listener, int stateKey, String resolvedVersion, byte[] resolvedData) {
        try {
            bd().a(new e(listener), stateKey, resolvedVersion, resolvedData);
        } catch (RemoteException e2) {
            Log.w("AppStateClient", "service died");
        }
    }

    public void signOut(OnSignOutCompleteListener listener) {
        g gVar;
        if (listener == null) {
            gVar = null;
        } else {
            try {
                gVar = new g(listener);
            } catch (RemoteException e2) {
                Log.w("AppStateClient", "service died");
                return;
            }
        }
        bd().b(gVar);
    }
}

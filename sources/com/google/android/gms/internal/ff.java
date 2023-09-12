package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.de;
import com.google.android.gms.internal.fd;
import com.google.android.gms.internal.fe;
import com.google.android.gms.panorama.PanoramaClient;
/* loaded from: classes.dex */
public class ff extends de<fe> {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class a extends de<fe>.b<PanoramaClient.a> {
        public final ConnectionResult qV;
        public final Intent qW;
        public final int type;

        public a(PanoramaClient.a aVar, ConnectionResult connectionResult, int i, Intent intent) {
            super(aVar);
            this.qV = connectionResult;
            this.type = i;
            this.qW = intent;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.de.b
        public void a(PanoramaClient.a aVar) {
            if (aVar != null) {
                aVar.a(this.qV, this.type, this.qW);
            }
        }

        @Override // com.google.android.gms.internal.de.b
        protected void aF() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class b extends fd.a {
        private final PanoramaClient.a qY = null;
        private final PanoramaClient.OnPanoramaInfoLoadedListener qZ;
        private final Uri ra;

        public b(PanoramaClient.OnPanoramaInfoLoadedListener onPanoramaInfoLoadedListener, Uri uri) {
            this.qZ = onPanoramaInfoLoadedListener;
            this.ra = uri;
        }

        @Override // com.google.android.gms.internal.fd
        public void a(int i, Bundle bundle, int i2, Intent intent) {
            if (this.ra != null) {
                ff.this.getContext().revokeUriPermission(this.ra, 1);
            }
            ConnectionResult connectionResult = new ConnectionResult(i, bundle != null ? (PendingIntent) bundle.getParcelable("pendingIntent") : null);
            if (this.qY != null) {
                ff.this.a(new a(this.qY, connectionResult, i2, intent));
            } else {
                ff.this.a(new c(this.qZ, connectionResult, intent));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class c extends de<fe>.b<PanoramaClient.OnPanoramaInfoLoadedListener> {
        private final ConnectionResult qV;
        private final Intent qW;

        public c(PanoramaClient.OnPanoramaInfoLoadedListener onPanoramaInfoLoadedListener, ConnectionResult connectionResult, Intent intent) {
            super(onPanoramaInfoLoadedListener);
            this.qV = connectionResult;
            this.qW = intent;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.de.b
        public void a(PanoramaClient.OnPanoramaInfoLoadedListener onPanoramaInfoLoadedListener) {
            if (onPanoramaInfoLoadedListener != null) {
                onPanoramaInfoLoadedListener.onPanoramaInfoLoaded(this.qV, this.qW);
            }
        }

        @Override // com.google.android.gms.internal.de.b
        protected void aF() {
        }
    }

    public ff(Context context, GooglePlayServicesClient.ConnectionCallbacks connectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener onConnectionFailedListener) {
        super(context, connectionCallbacks, onConnectionFailedListener, null);
    }

    @Override // com.google.android.gms.internal.de
    protected void a(dj djVar, de.d dVar) throws RemoteException {
        djVar.a(dVar, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, getContext().getPackageName(), new Bundle());
    }

    public void a(b bVar, Uri uri, Bundle bundle, boolean z) {
        bc();
        if (z) {
            getContext().grantUriPermission(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE, uri, 1);
        }
        try {
            bd().a(bVar, uri, bundle, z);
        } catch (RemoteException e) {
            bVar.a(8, null, 0, null);
        }
    }

    public void a(PanoramaClient.OnPanoramaInfoLoadedListener onPanoramaInfoLoadedListener, Uri uri, boolean z) {
        a(new b(onPanoramaInfoLoadedListener, z ? uri : null), uri, null, z);
    }

    @Override // com.google.android.gms.internal.de
    protected String ag() {
        return "com.google.android.gms.panorama.service.START";
    }

    @Override // com.google.android.gms.internal.de
    protected String ah() {
        return "com.google.android.gms.panorama.internal.IPanoramaService";
    }

    @Override // com.google.android.gms.internal.de
    /* renamed from: am */
    public fe p(IBinder iBinder) {
        return fe.a.al(iBinder);
    }
}

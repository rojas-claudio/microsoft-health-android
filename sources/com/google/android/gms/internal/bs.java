package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.internal.br;
/* loaded from: classes.dex */
public abstract class bs extends cg {
    private final bu eI;
    private final br.a gx;

    /* loaded from: classes.dex */
    public static final class a extends bs {
        private final Context mContext;

        public a(Context context, bu buVar, br.a aVar) {
            super(buVar, aVar);
            this.mContext = context;
        }

        @Override // com.google.android.gms.internal.bs
        public void ae() {
        }

        @Override // com.google.android.gms.internal.bs
        public by af() {
            return bz.a(this.mContext, new am());
        }
    }

    /* loaded from: classes.dex */
    public static final class b extends bs implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {
        private final Object eJ;
        private final br.a gx;
        private final bt gy;

        public b(Context context, bu buVar, br.a aVar) {
            super(buVar, aVar);
            this.eJ = new Object();
            this.gx = aVar;
            this.gy = new bt(context, this, this, buVar.eg.hR);
            this.gy.connect();
        }

        @Override // com.google.android.gms.internal.bs
        public void ae() {
            synchronized (this.eJ) {
                if (this.gy.isConnected() || this.gy.isConnecting()) {
                    this.gy.disconnect();
                }
            }
        }

        @Override // com.google.android.gms.internal.bs
        public by af() {
            by byVar;
            synchronized (this.eJ) {
                try {
                    byVar = this.gy.ai();
                } catch (IllegalStateException e) {
                    byVar = null;
                }
            }
            return byVar;
        }

        @Override // com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks
        public void onConnected(Bundle connectionHint) {
            start();
        }

        @Override // com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener
        public void onConnectionFailed(ConnectionResult result) {
            this.gx.a(new bw(0));
        }

        @Override // com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks
        public void onDisconnected() {
            cn.m("Disconnected from remote ad request service.");
        }
    }

    public bs(bu buVar, br.a aVar) {
        this.eI = buVar;
        this.gx = aVar;
    }

    private static bw a(by byVar, bu buVar) {
        try {
            return byVar.a(buVar);
        } catch (RemoteException e) {
            cn.b("Could not fetch ad response from ad request service.", e);
            return null;
        }
    }

    @Override // com.google.android.gms.internal.cg
    public final void ac() {
        bw a2;
        try {
            by af = af();
            if (af == null) {
                a2 = new bw(0);
            } else {
                a2 = a(af, this.eI);
                if (a2 == null) {
                    a2 = new bw(0);
                }
            }
            ae();
            this.gx.a(a2);
        } catch (Throwable th) {
            ae();
            throw th;
        }
    }

    public abstract void ae();

    public abstract by af();

    @Override // com.google.android.gms.internal.cg
    public final void onStop() {
        ae();
    }
}

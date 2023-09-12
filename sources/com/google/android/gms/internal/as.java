package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
import android.os.SystemClock;
import com.google.android.gms.internal.at;
/* loaded from: classes.dex */
public final class as implements at.a {
    private final aw dZ;
    private final v em;
    private final String fd;
    private final long fe;
    private final ao ff;
    private final x fg;
    private ax fh;
    private final Context mContext;
    private final Object eJ = new Object();
    private int fi = -2;

    public as(Context context, String str, aw awVar, ap apVar, ao aoVar, v vVar, x xVar) {
        this.mContext = context;
        this.fd = str;
        this.dZ = awVar;
        this.fe = apVar.eV != -1 ? apVar.eV : 10000L;
        this.ff = aoVar;
        this.em = vVar;
        this.fg = xVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ax P() {
        cn.o("Instantiating mediation adapter: " + this.fd);
        try {
            return this.dZ.g(this.fd);
        } catch (RemoteException e) {
            cn.a("Could not instantiate mediation adapter: " + this.fd, e);
            return null;
        }
    }

    private void a(long j, long j2, long j3, long j4) {
        while (this.fi == -2) {
            b(j, j2, j3, j4);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(ar arVar) {
        try {
            if (this.fg.ex) {
                this.fh.a(com.google.android.gms.dynamic.c.g(this.mContext), this.em, this.ff.eS, arVar);
            } else {
                this.fh.a(com.google.android.gms.dynamic.c.g(this.mContext), this.fg, this.em, this.ff.eS, arVar);
            }
        } catch (RemoteException e) {
            cn.b("Could not request ad from mediation adapter.", e);
            d(5);
        }
    }

    private void b(long j, long j2, long j3, long j4) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long j5 = j2 - (elapsedRealtime - j);
        long j6 = j4 - (elapsedRealtime - j3);
        if (j5 <= 0 || j6 <= 0) {
            cn.o("Timed out waiting for adapter.");
            this.fi = 3;
            return;
        }
        try {
            this.eJ.wait(Math.min(j5, j6));
        } catch (InterruptedException e) {
            this.fi = -1;
        }
    }

    public at b(long j, long j2) {
        at atVar;
        synchronized (this.eJ) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            final ar arVar = new ar();
            cm.hO.post(new Runnable() { // from class: com.google.android.gms.internal.as.1
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (as.this.eJ) {
                        if (as.this.fi != -2) {
                            return;
                        }
                        as.this.fh = as.this.P();
                        if (as.this.fh == null) {
                            as.this.d(4);
                            return;
                        }
                        arVar.a(as.this);
                        as.this.a(arVar);
                    }
                }
            });
            a(elapsedRealtime, this.fe, j, j2);
            atVar = new at(this.ff, this.fh, this.fd, arVar, this.fi);
        }
        return atVar;
    }

    public void cancel() {
        synchronized (this.eJ) {
            try {
                if (this.fh != null) {
                    this.fh.destroy();
                }
            } catch (RemoteException e) {
                cn.b("Could not destroy mediation adapter.", e);
            }
            this.fi = -1;
            this.eJ.notify();
        }
    }

    @Override // com.google.android.gms.internal.at.a
    public void d(int i) {
        synchronized (this.eJ) {
            this.fi = i;
            this.eJ.notify();
        }
    }
}

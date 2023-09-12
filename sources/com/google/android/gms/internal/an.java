package com.google.android.gms.internal;

import android.content.Context;
import android.os.RemoteException;
/* loaded from: classes.dex */
public final class an {
    private final aw dZ;
    private final bu eI;
    private final ap eK;
    private as eM;
    private final Context mContext;
    private final Object eJ = new Object();
    private boolean eL = false;

    public an(Context context, bu buVar, aw awVar, ap apVar) {
        this.mContext = context;
        this.eI = buVar;
        this.dZ = awVar;
        this.eK = apVar;
    }

    public at a(long j, long j2) {
        cn.m("Starting mediation.");
        for (ao aoVar : this.eK.eU) {
            cn.o("Trying mediation network: " + aoVar.eP);
            for (String str : aoVar.eQ) {
                synchronized (this.eJ) {
                    if (this.eL) {
                        return new at(-1);
                    }
                    this.eM = new as(this.mContext, str, this.dZ, this.eK, aoVar, this.eI.gB, this.eI.ed);
                    final at b = this.eM.b(j, j2);
                    if (b.fl == 0) {
                        cn.m("Adapter succeeded.");
                        return b;
                    } else if (b.fn != null) {
                        cm.hO.post(new Runnable() { // from class: com.google.android.gms.internal.an.1
                            @Override // java.lang.Runnable
                            public void run() {
                                try {
                                    b.fn.destroy();
                                } catch (RemoteException e) {
                                    cn.b("Could not destroy mediation adapter.", e);
                                }
                            }
                        });
                    }
                }
            }
        }
        return new at(1);
    }

    public void cancel() {
        synchronized (this.eJ) {
            this.eL = true;
            if (this.eM != null) {
                this.eM.cancel();
            }
        }
    }
}

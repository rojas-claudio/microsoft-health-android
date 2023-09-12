package com.google.android.gms.internal;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import com.google.android.gms.internal.bp;
import com.google.android.gms.internal.br;
import com.google.android.gms.internal.bu;
import com.google.android.gms.internal.cr;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONException;
/* loaded from: classes.dex */
public final class bq extends cg implements br.a, cr.a {
    private final aw dZ;
    private ap eK;
    private final cq fG;
    private final bp.a gm;
    private final bu.a gn;
    private final h go;
    private cg gp;
    private bw gq;
    private an gs;
    private at gt;
    private final Context mContext;
    private final Object eJ = new Object();
    private boolean gr = false;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class a extends Exception {
        private final int gw;

        public a(String str, int i) {
            super(str);
            this.gw = i;
        }

        public int getErrorCode() {
            return this.gw;
        }
    }

    public bq(Context context, bu.a aVar, h hVar, cq cqVar, aw awVar, bp.a aVar2) {
        this.dZ = awVar;
        this.gm = aVar2;
        this.fG = cqVar;
        this.mContext = context;
        this.gn = aVar;
        this.go = hVar;
    }

    private void a(bu buVar, long j) throws a {
        this.gs = new an(this.mContext, buVar, this.dZ, this.eK);
        this.gt = this.gs.a(j, DateUtils.MILLIS_PER_MINUTE);
        switch (this.gt.fl) {
            case 0:
                return;
            case 1:
                throw new a("No fill from any mediation ad networks.", 3);
            default:
                throw new a("Unexpected mediation result: " + this.gt.fl, 0);
        }
    }

    private void ad() throws a {
        if (this.gq.errorCode == -3) {
            return;
        }
        if (TextUtils.isEmpty(this.gq.gG)) {
            throw new a("No fill from ad server.", 3);
        }
        if (this.gq.gI) {
            try {
                this.eK = new ap(this.gq.gG);
            } catch (JSONException e) {
                throw new a("Could not parse mediation config: " + this.gq.gG, 0);
            }
        }
    }

    private void b(long j) throws a {
        cm.hO.post(new Runnable() { // from class: com.google.android.gms.internal.bq.3
            @Override // java.lang.Runnable
            public void run() {
                synchronized (bq.this.eJ) {
                    if (bq.this.gq.errorCode != -2) {
                        return;
                    }
                    bq.this.fG.aw().a(bq.this);
                    if (bq.this.gq.errorCode == -3) {
                        cn.p("Loading URL in WebView: " + bq.this.gq.fW);
                        bq.this.fG.loadUrl(bq.this.gq.fW);
                    } else {
                        cn.p("Loading HTML in WebView.");
                        bq.this.fG.loadDataWithBaseURL(ci.j(bq.this.gq.fW), bq.this.gq.gG, "text/html", "UTF-8", null);
                    }
                }
            }
        });
        d(j);
    }

    private void c(long j) throws a {
        while (e(j)) {
            if (this.gq != null) {
                this.gp = null;
                if (this.gq.errorCode != -2 && this.gq.errorCode != -3) {
                    throw new a("There was a problem getting an ad response. ErrorCode: " + this.gq.errorCode, this.gq.errorCode);
                }
                return;
            }
        }
        throw new a("Timed out waiting for ad response.", 2);
    }

    private void d(long j) throws a {
        while (e(j)) {
            if (this.gr) {
                return;
            }
        }
        throw new a("Timed out waiting for WebView to finish loading.", 2);
    }

    private boolean e(long j) throws a {
        long elapsedRealtime = DateUtils.MILLIS_PER_MINUTE - (SystemClock.elapsedRealtime() - j);
        if (elapsedRealtime <= 0) {
            return false;
        }
        try {
            this.eJ.wait(elapsedRealtime);
            return true;
        } catch (InterruptedException e) {
            throw new a("Ad request cancelled.", -1);
        }
    }

    @Override // com.google.android.gms.internal.br.a
    public void a(bw bwVar) {
        synchronized (this.eJ) {
            cn.m("Received ad response.");
            this.gq = bwVar;
            this.eJ.notify();
        }
    }

    @Override // com.google.android.gms.internal.cr.a
    public void a(cq cqVar) {
        synchronized (this.eJ) {
            cn.m("WebView finished loading.");
            this.gr = true;
            this.eJ.notify();
        }
    }

    @Override // com.google.android.gms.internal.cg
    public void ac() {
        long elapsedRealtime;
        synchronized (this.eJ) {
            cn.m("AdLoaderBackgroundTask started.");
            bu buVar = new bu(this.gn, this.go.g().a(this.mContext));
            int i = -2;
            try {
                elapsedRealtime = SystemClock.elapsedRealtime();
                this.gp = br.a(this.mContext, buVar, this);
            } catch (a e) {
                i = e.getErrorCode();
                if (i == 3 || i == -1) {
                    cn.o(e.getMessage());
                } else {
                    cn.q(e.getMessage());
                }
                this.gq = new bw(i);
                cm.hO.post(new Runnable() { // from class: com.google.android.gms.internal.bq.1
                    @Override // java.lang.Runnable
                    public void run() {
                        bq.this.onStop();
                    }
                });
            }
            if (this.gp == null) {
                throw new a("Could not start the ad request service.", 0);
            }
            c(elapsedRealtime);
            ad();
            if (this.gq.gI) {
                a(buVar, elapsedRealtime);
            } else {
                b(elapsedRealtime);
            }
            final ce ceVar = new ce(buVar.gB, this.fG, this.gq.eW, i, this.gq.eX, this.gq.gK, this.gq.orientation, this.gq.fa, buVar.gE, this.gq.gI, this.gt != null ? this.gt.fm : null, this.gt != null ? this.gt.fn : null, this.gt != null ? this.gt.fo : null, this.eK, this.gt != null ? this.gt.fp : null, this.gq.gJ, this.gq.gH);
            cm.hO.post(new Runnable() { // from class: com.google.android.gms.internal.bq.2
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (bq.this.eJ) {
                        bq.this.gm.a(ceVar);
                    }
                }
            });
        }
    }

    @Override // com.google.android.gms.internal.cg
    public void onStop() {
        synchronized (this.eJ) {
            if (this.gp != null) {
                this.gp.cancel();
            }
            this.fG.stopLoading();
            ci.a(this.fG);
            if (this.gs != null) {
                this.gs.cancel();
            }
        }
    }
}

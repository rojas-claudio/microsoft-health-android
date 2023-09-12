package com.google.android.gms.internal;

import java.util.Map;
/* loaded from: classes.dex */
public final class cb {
    private cq fG;
    private String gT;
    private final Object eJ = new Object();
    private int gw = -2;
    public final ai gU = new ai() { // from class: com.google.android.gms.internal.cb.1
        @Override // com.google.android.gms.internal.ai
        public void a(cq cqVar, Map<String, String> map) {
            synchronized (cb.this.eJ) {
                cn.q("Invalid " + map.get("type") + " request error: " + map.get("errors"));
                cb.this.gw = 1;
                cb.this.eJ.notify();
            }
        }
    };
    public final ai gV = new ai() { // from class: com.google.android.gms.internal.cb.2
        @Override // com.google.android.gms.internal.ai
        public void a(cq cqVar, Map<String, String> map) {
            synchronized (cb.this.eJ) {
                String str = map.get("url");
                if (str == null) {
                    cn.q("URL missing in loadAdUrl GMSG.");
                    return;
                }
                cb.this.gT = str;
                cb.this.eJ.notify();
            }
        }
    };

    public String aj() {
        String str;
        synchronized (this.eJ) {
            while (this.gT == null && this.gw == -2) {
                try {
                    this.eJ.wait();
                } catch (InterruptedException e) {
                    cn.q("Ad request service was interrupted.");
                    str = null;
                }
            }
            str = this.gT;
        }
        return str;
    }

    public void b(cq cqVar) {
        synchronized (this.eJ) {
            this.fG = cqVar;
        }
    }

    public int getErrorCode() {
        int i;
        synchronized (this.eJ) {
            i = this.gw;
        }
        return i;
    }
}

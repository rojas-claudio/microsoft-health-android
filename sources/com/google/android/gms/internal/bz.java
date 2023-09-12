package com.google.android.gms.internal;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.gms.internal.by;
import com.unnamed.b.atv.model.TreeNode;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public final class bz extends by.a {
    private static final Object gL = new Object();
    private static bz gM;
    private final al gN;
    private final Context mContext;

    private bz(Context context, al alVar) {
        this.mContext = context;
        this.gN = alVar;
    }

    private static bw a(final Context context, al alVar, final bu buVar) {
        cn.m("Starting ad request from service.");
        alVar.init();
        cd cdVar = new cd(context);
        if (cdVar.hs == -1) {
            cn.m("Device is offline.");
            return new bw(2);
        }
        final cb cbVar = new cb();
        final String a = ca.a(buVar, cdVar, alVar.a(250L));
        if (a == null) {
            return new bw(0);
        }
        cm.hO.post(new Runnable() { // from class: com.google.android.gms.internal.bz.1
            @Override // java.lang.Runnable
            public void run() {
                cq a2 = cq.a(context, new x(), false, false, null, buVar.eg);
                a2.setWillNotDraw(true);
                cbVar.b(a2);
                cr aw = a2.aw();
                aw.a("/invalidRequest", cbVar.gU);
                aw.a("/loadAdURL", cbVar.gV);
                aw.a("/log", ah.eE);
                cn.m("Getting the ad request URL.");
                a2.loadDataWithBaseURL("http://googleads.g.doubleclick.net", "<!DOCTYPE html><html><head><script src=\"http://googleads.g.doubleclick.net/mads/static/sdk/native/sdk-core-v40.js\"></script><script>AFMA_buildAdURL(" + a + ");</script></head><body></body></html>", "text/html", "UTF-8", null);
            }
        });
        String aj = cbVar.aj();
        return TextUtils.isEmpty(aj) ? new bw(cbVar.getErrorCode()) : a(context, buVar.eg.hP, aj);
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x008e, code lost:
        com.google.android.gms.internal.cn.q("Received error HTTP response code: " + r4);
        r1 = new com.google.android.gms.internal.bw(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00ab, code lost:
        r0.disconnect();
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:?, code lost:
        return r1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static com.google.android.gms.internal.bw a(android.content.Context r8, java.lang.String r9, java.lang.String r10) {
        /*
            r7 = 300(0x12c, float:4.2E-43)
            r0 = 0
            com.google.android.gms.internal.cc r3 = new com.google.android.gms.internal.cc     // Catch: java.io.IOException -> Lb8
            r3.<init>()     // Catch: java.io.IOException -> Lb8
            java.net.URL r1 = new java.net.URL     // Catch: java.io.IOException -> Lb8
            r1.<init>(r10)     // Catch: java.io.IOException -> Lb8
            r2 = r1
            r1 = r0
        Lf:
            java.net.URLConnection r0 = r2.openConnection()     // Catch: java.io.IOException -> Lb8
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch: java.io.IOException -> Lb8
            r4 = 0
            com.google.android.gms.internal.ci.a(r8, r9, r4, r0)     // Catch: java.lang.Throwable -> Ldc
            int r4 = r0.getResponseCode()     // Catch: java.lang.Throwable -> Ldc
            java.util.Map r5 = r0.getHeaderFields()     // Catch: java.lang.Throwable -> Ldc
            r6 = 200(0xc8, float:2.8E-43)
            if (r4 < r6) goto L47
            if (r4 >= r7) goto L47
            java.lang.String r1 = r2.toString()     // Catch: java.lang.Throwable -> Ldc
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> Ldc
            java.io.InputStream r6 = r0.getInputStream()     // Catch: java.lang.Throwable -> Ldc
            r2.<init>(r6)     // Catch: java.lang.Throwable -> Ldc
            java.lang.String r2 = com.google.android.gms.internal.ci.a(r2)     // Catch: java.lang.Throwable -> Ldc
            a(r1, r5, r2, r4)     // Catch: java.lang.Throwable -> Ldc
            r3.a(r1, r5, r2)     // Catch: java.lang.Throwable -> Ldc
            com.google.android.gms.internal.bw r1 = r3.ak()     // Catch: java.lang.Throwable -> Ldc
            r0.disconnect()     // Catch: java.io.IOException -> Lb8
            r0 = r1
        L46:
            return r0
        L47:
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> Ldc
            r6 = 0
            a(r2, r5, r6, r4)     // Catch: java.lang.Throwable -> Ldc
            if (r4 < r7) goto L8e
            r2 = 400(0x190, float:5.6E-43)
            if (r4 >= r2) goto L8e
            java.lang.String r2 = "Location"
            java.lang.String r4 = r0.getHeaderField(r2)     // Catch: java.lang.Throwable -> Ldc
            boolean r2 = android.text.TextUtils.isEmpty(r4)     // Catch: java.lang.Throwable -> Ldc
            if (r2 == 0) goto L73
            java.lang.String r1 = "No location header to follow redirect."
            com.google.android.gms.internal.cn.q(r1)     // Catch: java.lang.Throwable -> Ldc
            com.google.android.gms.internal.bw r1 = new com.google.android.gms.internal.bw     // Catch: java.lang.Throwable -> Ldc
            r2 = 0
            r1.<init>(r2)     // Catch: java.lang.Throwable -> Ldc
            r0.disconnect()     // Catch: java.io.IOException -> Lb8
            r0 = r1
            goto L46
        L73:
            java.net.URL r2 = new java.net.URL     // Catch: java.lang.Throwable -> Ldc
            r2.<init>(r4)     // Catch: java.lang.Throwable -> Ldc
            int r1 = r1 + 1
            r4 = 5
            if (r1 <= r4) goto Lb0
            java.lang.String r1 = "Too many redirects."
            com.google.android.gms.internal.cn.q(r1)     // Catch: java.lang.Throwable -> Ldc
            com.google.android.gms.internal.bw r1 = new com.google.android.gms.internal.bw     // Catch: java.lang.Throwable -> Ldc
            r2 = 0
            r1.<init>(r2)     // Catch: java.lang.Throwable -> Ldc
            r0.disconnect()     // Catch: java.io.IOException -> Lb8
            r0 = r1
            goto L46
        L8e:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Ldc
            r1.<init>()     // Catch: java.lang.Throwable -> Ldc
            java.lang.String r2 = "Received error HTTP response code: "
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch: java.lang.Throwable -> Ldc
            java.lang.StringBuilder r1 = r1.append(r4)     // Catch: java.lang.Throwable -> Ldc
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> Ldc
            com.google.android.gms.internal.cn.q(r1)     // Catch: java.lang.Throwable -> Ldc
            com.google.android.gms.internal.bw r1 = new com.google.android.gms.internal.bw     // Catch: java.lang.Throwable -> Ldc
            r2 = 0
            r1.<init>(r2)     // Catch: java.lang.Throwable -> Ldc
            r0.disconnect()     // Catch: java.io.IOException -> Lb8
            r0 = r1
            goto L46
        Lb0:
            r3.d(r5)     // Catch: java.lang.Throwable -> Ldc
            r0.disconnect()     // Catch: java.io.IOException -> Lb8
            goto Lf
        Lb8:
            r0 = move-exception
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Error while connecting to ad server: "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r0 = r0.getMessage()
            java.lang.StringBuilder r0 = r1.append(r0)
            java.lang.String r0 = r0.toString()
            com.google.android.gms.internal.cn.q(r0)
            com.google.android.gms.internal.bw r0 = new com.google.android.gms.internal.bw
            r1 = 2
            r0.<init>(r1)
            goto L46
        Ldc:
            r1 = move-exception
            r0.disconnect()     // Catch: java.io.IOException -> Lb8
            throw r1     // Catch: java.io.IOException -> Lb8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.bz.a(android.content.Context, java.lang.String, java.lang.String):com.google.android.gms.internal.bw");
    }

    public static bz a(Context context, al alVar) {
        bz bzVar;
        synchronized (gL) {
            if (gM == null) {
                gM = new bz(context.getApplicationContext(), alVar);
            }
            bzVar = gM;
        }
        return bzVar;
    }

    private static void a(String str, Map<String, List<String>> map, String str2, int i) {
        if (cn.k(2)) {
            cn.p("Http Response: {\n  URL:\n    " + str + "\n  Headers:");
            if (map != null) {
                for (String str3 : map.keySet()) {
                    cn.p("    " + str3 + TreeNode.NODES_ID_SEPARATOR);
                    Iterator<String> it = map.get(str3).iterator();
                    while (it.hasNext()) {
                        cn.p("      " + it.next());
                    }
                }
            }
            cn.p("  Body:");
            if (str2 != null) {
                for (int i2 = 0; i2 < Math.min(str2.length(), 100000); i2 += 1000) {
                    cn.p(str2.substring(i2, Math.min(str2.length(), i2 + 1000)));
                }
            } else {
                cn.p("    null");
            }
            cn.p("  Response Code:\n    " + i + "\n}");
        }
    }

    @Override // com.google.android.gms.internal.by
    public bw a(bu buVar) {
        return a(this.mContext, this.gN, buVar);
    }
}

package com.google.android.gms.internal;

import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.HashMap;
/* loaded from: classes.dex */
public class cr extends WebViewClient {
    private ag ey;
    protected final cq fG;
    private q ia;
    private bi ib;
    private a ic;
    private boolean ie;

    /* renamed from: if  reason: not valid java name */
    private bl f0if;
    private final HashMap<String, ai> hZ = new HashMap<>();
    private final Object eJ = new Object();
    private boolean id = false;

    /* loaded from: classes.dex */
    public interface a {
        void a(cq cqVar);
    }

    public cr(cq cqVar, boolean z) {
        this.fG = cqVar;
        this.ie = z;
    }

    private void a(bh bhVar) {
        bf.a(this.fG.getContext(), bhVar);
    }

    private static boolean b(Uri uri) {
        String scheme = uri.getScheme();
        return "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
    }

    private void c(Uri uri) {
        String path = uri.getPath();
        ai aiVar = this.hZ.get(path);
        if (aiVar == null) {
            cn.q("No GMSG handler found for GMSG: " + uri);
            return;
        }
        HashMap hashMap = new HashMap();
        UrlQuerySanitizer urlQuerySanitizer = new UrlQuerySanitizer();
        urlQuerySanitizer.setAllowUnregisteredParamaters(true);
        urlQuerySanitizer.setUnregisteredParameterValueSanitizer(UrlQuerySanitizer.getAllButNulLegal());
        urlQuerySanitizer.parseUrl(uri.toString());
        for (UrlQuerySanitizer.ParameterValuePair parameterValuePair : urlQuerySanitizer.getParameterList()) {
            hashMap.put(parameterValuePair.mParameter, parameterValuePair.mValue);
        }
        if (cn.k(2)) {
            cn.p("Received GMSG: " + path);
            for (String str : hashMap.keySet()) {
                cn.p("  " + str + ": " + ((String) hashMap.get(str)));
            }
        }
        aiVar.a(this.fG, hashMap);
    }

    public final void S() {
        synchronized (this.eJ) {
            this.id = false;
            this.ie = true;
            final bf au = this.fG.au();
            if (au != null) {
                if (cm.ar()) {
                    au.S();
                } else {
                    cm.hO.post(new Runnable() { // from class: com.google.android.gms.internal.cr.1
                        @Override // java.lang.Runnable
                        public void run() {
                            au.S();
                        }
                    });
                }
            }
        }
    }

    public final void a(be beVar) {
        boolean az = this.fG.az();
        a(new bh(beVar, (!az || this.fG.av().ex) ? this.ia : null, az ? null : this.ib, this.f0if, this.fG.ay()));
    }

    public final void a(a aVar) {
        this.ic = aVar;
    }

    public void a(q qVar, bi biVar, ag agVar, bl blVar, boolean z) {
        a("/appEvent", new af(agVar));
        a("/canOpenURLs", ah.ez);
        a("/click", ah.eA);
        a("/close", ah.eB);
        a("/customClose", ah.eC);
        a("/httpTrack", ah.eD);
        a("/log", ah.eE);
        a("/open", ah.eF);
        a("/touch", ah.eG);
        a("/video", ah.eH);
        this.ia = qVar;
        this.ib = biVar;
        this.ey = agVar;
        this.f0if = blVar;
        j(z);
    }

    public final void a(String str, ai aiVar) {
        this.hZ.put(str, aiVar);
    }

    public final void a(boolean z, int i) {
        a(new bh((!this.fG.az() || this.fG.av().ex) ? this.ia : null, this.ib, this.f0if, this.fG, z, i, this.fG.ay()));
    }

    public final void a(boolean z, int i, String str) {
        boolean az = this.fG.az();
        a(new bh((!az || this.fG.av().ex) ? this.ia : null, az ? null : this.ib, this.ey, this.f0if, this.fG, z, i, str, this.fG.ay()));
    }

    public final void a(boolean z, int i, String str, String str2) {
        boolean az = this.fG.az();
        a(new bh((!az || this.fG.av().ex) ? this.ia : null, az ? null : this.ib, this.ey, this.f0if, this.fG, z, i, str, str2, this.fG.ay()));
    }

    public boolean aD() {
        boolean z;
        synchronized (this.eJ) {
            z = this.ie;
        }
        return z;
    }

    public final void j(boolean z) {
        this.id = z;
    }

    @Override // android.webkit.WebViewClient
    public final void onPageFinished(WebView webView, String url) {
        if (this.ic != null) {
            this.ic.a(this.fG);
            this.ic = null;
        }
    }

    public final void reset() {
        synchronized (this.eJ) {
            this.hZ.clear();
            this.ia = null;
            this.ib = null;
            this.ic = null;
            this.ey = null;
            this.id = false;
            this.ie = false;
            this.f0if = null;
        }
    }

    @Override // android.webkit.WebViewClient
    public final boolean shouldOverrideUrlLoading(WebView webView, String url) {
        Uri uri;
        cn.p("AdWebView shouldOverrideUrlLoading: " + url);
        Uri parse = Uri.parse(url);
        if ("gmsg".equalsIgnoreCase(parse.getScheme()) && "mobileads.google.com".equalsIgnoreCase(parse.getHost())) {
            c(parse);
        } else if (this.id && b(parse)) {
            return super.shouldOverrideUrlLoading(webView, url);
        } else {
            if (this.fG.willNotDraw()) {
                cn.q("AdWebView unable to handle URL: " + url);
            } else {
                try {
                    h ax = this.fG.ax();
                    if (ax != null && ax.a(parse)) {
                        parse = ax.a(parse, this.fG.getContext());
                    }
                    uri = parse;
                } catch (i e) {
                    cn.q("Unable to append parameter to URL: " + url);
                    uri = parse;
                }
                a(new be("android.intent.action.VIEW", uri.toString(), null, null, null, null, null));
            }
        }
        return true;
    }
}

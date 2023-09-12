package com.google.android.gms.internal;

import android.content.Context;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/* loaded from: classes.dex */
public final class ct extends cr {
    public ct(cq cqVar, boolean z) {
        super(cqVar, z);
    }

    private static WebResourceResponse b(Context context, String str, String str2) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str2).openConnection();
        try {
            ci.a(context, str, true, httpURLConnection);
            httpURLConnection.connect();
            return new WebResourceResponse("application/javascript", "UTF-8", new ByteArrayInputStream(ci.a(new InputStreamReader(httpURLConnection.getInputStream())).getBytes("UTF-8")));
        } finally {
            httpURLConnection.disconnect();
        }
    }

    @Override // android.webkit.WebViewClient
    public WebResourceResponse shouldInterceptRequest(WebView webView, String url) {
        WebResourceResponse b;
        try {
            if (!"mraid.js".equalsIgnoreCase(new File(url).getName())) {
                b = super.shouldInterceptRequest(webView, url);
            } else if (webView instanceof cq) {
                cq cqVar = (cq) webView;
                cqVar.aw().S();
                if (cqVar.av().ex) {
                    cn.p("shouldInterceptRequest(http://media.admob.com/mraid/v1/mraid_app_interstitial.js)");
                    b = b(cqVar.getContext(), this.fG.ay().hP, "http://media.admob.com/mraid/v1/mraid_app_interstitial.js");
                } else if (cqVar.az()) {
                    cn.p("shouldInterceptRequest(http://media.admob.com/mraid/v1/mraid_app_expanded_banner.js)");
                    b = b(cqVar.getContext(), this.fG.ay().hP, "http://media.admob.com/mraid/v1/mraid_app_expanded_banner.js");
                } else {
                    cn.p("shouldInterceptRequest(http://media.admob.com/mraid/v1/mraid_app_banner.js)");
                    b = b(cqVar.getContext(), this.fG.ay().hP, "http://media.admob.com/mraid/v1/mraid_app_banner.js");
                }
            } else {
                cn.q("Tried to intercept request from a WebView that wasn't an AdWebView.");
                b = super.shouldInterceptRequest(webView, url);
            }
            return b;
        } catch (IOException e) {
            cn.q("Could not fetching MRAID JS. " + e.getMessage());
            return super.shouldInterceptRequest(webView, url);
        }
    }
}

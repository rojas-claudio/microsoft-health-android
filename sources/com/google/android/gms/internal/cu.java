package com.google.android.gms.internal;

import android.view.View;
import android.webkit.WebChromeClient;
/* loaded from: classes.dex */
public final class cu extends cs {
    public cu(cq cqVar) {
        super(cqVar);
    }

    @Override // android.webkit.WebChromeClient
    public void onShowCustomView(View view, int requestedOrientation, WebChromeClient.CustomViewCallback customViewCallback) {
        a(view, requestedOrientation, customViewCallback);
    }
}

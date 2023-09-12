package com.microsoft.krestsdk.services;

import java.net.URI;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
/* loaded from: classes.dex */
class HttpDelete extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "DELETE";

    public HttpDelete() {
    }

    public HttpDelete(URI uri) {
        setURI(uri);
    }

    public HttpDelete(String uri) {
        setURI(URI.create(uri));
    }

    public String getMethod() {
        return "DELETE";
    }
}

package com.microsoft.kapp.logging.http;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import org.apache.http.client.methods.HttpRequestBase;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public interface HttpTransaction {
    String getMethod();

    String getReason();

    byte[] getRequest();

    KHTTPHeader[] getRequestHeaders();

    DateTime getRequestTime();

    byte[] getResponse();

    KHTTPHeader[] getResponseHeaders();

    DateTime getResponseHeadersTime();

    DateTime getResponseTime();

    int getStatusCode();

    URI getUri();

    void setRequest(byte[] bArr);

    void setResponse(byte[] bArr);

    void writeRequestTransaction(String str, Map<String, String> map, String str2);

    void writeRequestTransaction(HttpRequestBase httpRequestBase);

    void writeResponseTransaction(Map<String, String> map, int i, String str, InputStream inputStream);
}

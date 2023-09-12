package com.microsoft.kapp.logging.http;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import org.apache.http.client.methods.HttpRequestBase;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class MockHttpTransactionImpl implements HttpTransaction {
    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public byte[] getRequest() {
        return null;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public void setRequest(byte[] request) {
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public byte[] getResponse() {
        return null;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public void setResponse(byte[] response) {
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public KHTTPHeader[] getRequestHeaders() {
        return null;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public KHTTPHeader[] getResponseHeaders() {
        return null;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public DateTime getRequestTime() {
        return null;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public DateTime getResponseHeadersTime() {
        return null;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public DateTime getResponseTime() {
        return null;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public String getMethod() {
        return null;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public URI getUri() {
        return null;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public int getStatusCode() {
        return 0;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public String getReason() {
        return null;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public void writeRequestTransaction(HttpRequestBase request) {
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public void writeRequestTransaction(String url, Map<String, String> headers, String method) {
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public void writeResponseTransaction(Map<String, String> headers, int statusCode, String getReasonPhrase, InputStream response) {
    }
}

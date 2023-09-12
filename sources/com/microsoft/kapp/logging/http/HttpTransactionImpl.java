package com.microsoft.kapp.logging.http;

import com.microsoft.band.util.StreamUtils;
import com.microsoft.kapp.logging.KLog;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class HttpTransactionImpl implements HttpTransaction {
    public static final String TAG = HttpTransactionImpl.class.getSimpleName();
    private String mMethod;
    private String mReason;
    private byte[] mRequest;
    private KHTTPHeader[] mRequestHeaders;
    private DateTime mRequestTime;
    private byte[] mResponse;
    private KHTTPHeader[] mResponseHeaders;
    private DateTime mResponseHeadersTime;
    private DateTime mResponseTime;
    private int mStatusCode;
    private URI mUri;

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public byte[] getRequest() {
        return this.mRequest;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public void setRequest(byte[] request) {
        this.mRequest = request;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public byte[] getResponse() {
        return this.mResponse;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public void setResponse(byte[] response) {
        this.mResponse = response;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public KHTTPHeader[] getRequestHeaders() {
        return this.mRequestHeaders;
    }

    public void setRequestHeaders(KHTTPHeader[] requestHeaders) {
        this.mRequestHeaders = requestHeaders;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public KHTTPHeader[] getResponseHeaders() {
        return this.mResponseHeaders;
    }

    public void setResponseHeaders(KHTTPHeader[] responseHeaders) {
        this.mResponseHeaders = responseHeaders;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public DateTime getRequestTime() {
        return this.mRequestTime;
    }

    public void setRequestTime(DateTime requestTime) {
        this.mRequestTime = requestTime;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public DateTime getResponseHeadersTime() {
        return this.mResponseHeadersTime;
    }

    public void setResponseHeadersTime(DateTime responseHeadersTime) {
        this.mResponseHeadersTime = responseHeadersTime;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public DateTime getResponseTime() {
        return this.mResponseTime;
    }

    public void setResponseTime(DateTime responseTime) {
        this.mResponseTime = responseTime;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public String getMethod() {
        return this.mMethod;
    }

    public void setMethod(String method) {
        this.mMethod = method;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public URI getUri() {
        return this.mUri;
    }

    public void setUri(URI uri) {
        this.mUri = uri;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public int getStatusCode() {
        return this.mStatusCode;
    }

    public void setStatusCode(int statusCode) {
        this.mStatusCode = statusCode;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public String getReason() {
        return this.mReason;
    }

    public void setReason(String reason) {
        this.mReason = reason;
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public void writeRequestTransaction(HttpRequestBase request) {
        setUri(request.getURI());
        setRequestTime(DateTime.now());
        setRequestHeaders(convertToKHttpHeaders(request.getAllHeaders()));
        setMethod(request.getMethod());
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public void writeRequestTransaction(String url, Map<String, String> headers, String method) {
        try {
            setUri(new URI(url));
            setRequestTime(DateTime.now());
            setRequestHeaders(convertToKHttpHeaders(headers));
            setMethod(method);
        } catch (Exception e) {
            KLog.e(TAG, "unable to write http response to log", e);
        }
    }

    @Override // com.microsoft.kapp.logging.http.HttpTransaction
    public void writeResponseTransaction(Map<String, String> headers, int statusCode, String getReasonPhrase, InputStream response) {
        try {
            setResponseHeadersTime(DateTime.now());
            setResponseHeaders(convertToKHttpHeaders(headers));
            setStatusCode(statusCode);
            setReason(getReasonPhrase);
            setResponseTime(DateTime.now());
            if (response != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[2048];
                while (true) {
                    int count = response.read(buffer);
                    if (count != -1) {
                        bos.write(buffer, 0, count);
                    } else {
                        setResponse(bos.toByteArray());
                        StreamUtils.closeQuietly(response);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            KLog.e(TAG, "unable to write http response to log", e);
        }
    }

    private KHTTPHeader[] convertToKHttpHeaders(Header[] headers) {
        if (headers == null) {
            return null;
        }
        List<KHTTPHeader> kHeaders = new ArrayList<>();
        for (Header header : headers) {
            kHeaders.add(new KHTTPHeader(header.getName(), header.getValue()));
        }
        return (KHTTPHeader[]) kHeaders.toArray(new KHTTPHeader[kHeaders.size()]);
    }

    private KHTTPHeader[] convertToKHttpHeaders(Map<String, String> headers) {
        if (headers == null) {
            return null;
        }
        List<KHTTPHeader> kHeaders = new ArrayList<>();
        for (String key : headers.keySet()) {
            if (!key.equalsIgnoreCase("Content-Encoding")) {
                kHeaders.add(new KHTTPHeader(key, headers.get(key)));
            }
        }
        return (KHTTPHeader[]) kHeaders.toArray(new KHTTPHeader[kHeaders.size()]);
    }
}

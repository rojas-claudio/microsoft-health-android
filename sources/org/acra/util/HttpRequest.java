package org.acra.util;

import com.microsoft.kapp.utils.Constants;
import com.microsoft.krestsdk.services.KCloudConstants;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import org.acra.ACRA;
import org.acra.sender.HttpSender;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
/* loaded from: classes.dex */
public final class HttpRequest {
    private Map<String, String> headers;
    private String login;
    private String password;
    private int connectionTimeOut = 3000;
    private int socketTimeOut = 3000;
    private int maxNrRetries = 3;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SocketTimeOutRetryHandler implements HttpRequestRetryHandler {
        private final HttpParams httpParams;
        private final int maxNrRetries;

        private SocketTimeOutRetryHandler(HttpParams httpParams, int maxNrRetries) {
            this.httpParams = httpParams;
            this.maxNrRetries = maxNrRetries;
        }

        public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
            if (exception instanceof SocketTimeoutException) {
                if (executionCount <= this.maxNrRetries) {
                    if (this.httpParams != null) {
                        int newSocketTimeOut = HttpConnectionParams.getSoTimeout(this.httpParams) * 2;
                        HttpConnectionParams.setSoTimeout(this.httpParams, newSocketTimeOut);
                        ACRA.log.d(ACRA.LOG_TAG, "SocketTimeOut - increasing time out to " + newSocketTimeOut + " millis and trying again");
                    } else {
                        ACRA.log.d(ACRA.LOG_TAG, "SocketTimeOut - no HttpParams, cannot increase time out. Trying again with current settings");
                    }
                    return true;
                }
                ACRA.log.d(ACRA.LOG_TAG, "SocketTimeOut but exceeded max number of retries : " + this.maxNrRetries);
            }
            return false;
        }
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConnectionTimeOut(int connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    public void setSocketTimeOut(int socketTimeOut) {
        this.socketTimeOut = socketTimeOut;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setMaxNrRetries(int maxNrRetries) {
        this.maxNrRetries = maxNrRetries;
    }

    public void send(URL url, HttpSender.Method method, String content, HttpSender.Type type) throws IOException {
        HttpClient httpClient = getHttpClient();
        HttpEntityEnclosingRequestBase httpRequest = getHttpRequest(url, method, content, type);
        ACRA.log.d(ACRA.LOG_TAG, "Sending request to " + url);
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpRequest, new BasicHttpContext());
            if (response != null) {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine != null) {
                    String statusCode = Integer.toString(response.getStatusLine().getStatusCode());
                    if (!statusCode.equals("409") && !statusCode.equals("403") && (statusCode.startsWith("4") || statusCode.startsWith("5"))) {
                        throw new IOException("Host returned error code " + statusCode);
                    }
                }
                EntityUtils.toString(response.getEntity());
            }
        } finally {
            if (response != null) {
                response.getEntity().consumeContent();
            }
        }
    }

    private HttpClient getHttpClient() {
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        basicHttpParams.setParameter("http.protocol.cookie-policy", "rfc2109");
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, this.connectionTimeOut);
        HttpConnectionParams.setSoTimeout(basicHttpParams, this.socketTimeOut);
        HttpConnectionParams.setSocketBufferSize(basicHttpParams, 8192);
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", new PlainSocketFactory(), 80));
        if (ACRA.getConfig().disableSSLCertValidation()) {
            registry.register(new Scheme("https", new FakeSocketFactory(), 443));
        } else {
            registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        }
        DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(basicHttpParams, registry), basicHttpParams);
        HttpRequestRetryHandler retryHandler = new SocketTimeOutRetryHandler(basicHttpParams, this.maxNrRetries);
        httpClient.setHttpRequestRetryHandler(retryHandler);
        return httpClient;
    }

    private UsernamePasswordCredentials getCredentials() {
        if (this.login == null && this.password == null) {
            return null;
        }
        return new UsernamePasswordCredentials(this.login, this.password);
    }

    private HttpEntityEnclosingRequestBase getHttpRequest(URL url, HttpSender.Method method, String content, HttpSender.Type type) throws UnsupportedEncodingException, UnsupportedOperationException {
        HttpPost httpPut;
        switch (method) {
            case POST:
                httpPut = new HttpPost(url.toString());
                break;
            case PUT:
                httpPut = new HttpPut(url.toString());
                break;
            default:
                throw new UnsupportedOperationException("Unknown method: " + method.name());
        }
        UsernamePasswordCredentials creds = getCredentials();
        if (creds != null) {
            httpPut.addHeader(BasicScheme.authenticate(creds, "UTF-8", false));
        }
        httpPut.setHeader("User-Agent", Constants.ANDROID_PHONE_IDENTIFIER);
        httpPut.setHeader(KCloudConstants.ACCEPT_HEADER, "text/html,application/xml,application/json,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        httpPut.setHeader("Content-Type", type.getContentType());
        if (this.headers != null) {
            for (String header : this.headers.keySet()) {
                String value = this.headers.get(header);
                httpPut.setHeader(header, value);
            }
        }
        httpPut.setEntity(new StringEntity(content, "UTF-8"));
        return httpPut;
    }

    public static String getParamsAsFormString(Map<?, ?> parameters) throws UnsupportedEncodingException {
        StringBuilder dataBfr = new StringBuilder();
        for (Object key : parameters.keySet()) {
            if (dataBfr.length() != 0) {
                dataBfr.append('&');
            }
            Object preliminaryValue = parameters.get(key);
            String value = preliminaryValue == null ? "" : preliminaryValue;
            dataBfr.append(URLEncoder.encode(key.toString(), "UTF-8"));
            dataBfr.append('=');
            dataBfr.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return dataBfr.toString();
    }
}

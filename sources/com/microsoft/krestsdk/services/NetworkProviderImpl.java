package com.microsoft.krestsdk.services;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.text.TextUtils;
import com.microsoft.band.util.StreamUtils;
import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.debug.KappConfig;
import com.microsoft.kapp.diagnostics.Compatibility;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.http.FiddlerLogger;
import com.microsoft.kapp.logging.http.HttpTransaction;
import com.microsoft.kapp.logging.http.HttpTransactionFactory;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.LocaleProvider;
import com.microsoft.kapp.utils.MultiReadInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
/* loaded from: classes.dex */
public class NetworkProviderImpl implements NetworkProvider {
    private static final int CONNECTION_TIMEOUT_MS = 10000;
    private static final int READ_TIMEOUT_MS = 50000;
    private static final String USER_AGENT = "User-Agent";
    protected final String TAG = getClass().getSimpleName();
    CacheService mCacheService;
    Context mContext;
    private FiddlerLogger mFiddlerLogger;
    private SettingsProvider mSettingsProvider;
    private UserAgentProvider mUserAgentProvider;

    private Map<String, String> getDefaultHeaders() {
        Map<String, String> defaultHeaders = new HashMap<>();
        String language = LocaleProvider.getLocaleSettings(this.mContext).getLocaleName();
        defaultHeaders.put("Accept-Encoding", "gzip");
        defaultHeaders.put("Accept-Language", language);
        defaultHeaders.put("Region", Locale.getDefault().getCountry());
        if (KappConfig.isDebbuging) {
            defaultHeaders.put("X-Mockler-SessionName", this.mSettingsProvider.getSessionHeaderValue());
        }
        defaultHeaders.put(USER_AGENT, this.mUserAgentProvider.getUserAgent());
        return defaultHeaders;
    }

    public NetworkProviderImpl(UserAgentProvider userAgentProvider, FiddlerLogger fiddlerLogger, SettingsProvider settingsProvider, Context context) {
        Validate.notNull(userAgentProvider, "userAgentProvider");
        Validate.notNull(fiddlerLogger, "fiddlerLogger");
        Validate.notNull(settingsProvider, "settingsProvider");
        this.mUserAgentProvider = userAgentProvider;
        this.mFiddlerLogger = fiddlerLogger;
        this.mSettingsProvider = settingsProvider;
        this.mContext = context;
    }

    @Override // com.microsoft.krestsdk.services.NetworkProvider
    public String executeHttpGet(String url, Map<String, String> headers) throws MalformedURLException, IOException {
        return executeHttpGet(url, headers, null);
    }

    @Override // com.microsoft.krestsdk.services.NetworkProvider
    public String executeHttpGet(String url, Map<String, String> requestHeaders, Map<String, String> responseHeaders) throws MalformedURLException, IOException {
        Get get = new Get(url, requestHeaders);
        Response response = executeRequest(get);
        setReponseHeaders(response, responseHeaders);
        return response.getString();
    }

    @Override // com.microsoft.krestsdk.services.NetworkProvider
    public byte[] executeHttpGetBinary(String url, Map<String, String> headers) throws MalformedURLException, IOException {
        Get get = new Get(url, headers);
        Response response = executeRequest(get);
        return response.getBytes();
    }

    @Override // com.microsoft.krestsdk.services.NetworkProvider
    public void executeHttpGetAndWriteToCache(String url, Map<String, String> headers, CacheService cacheService, List<String> tags) throws IOException, MalformedURLException {
        Validate.notNullOrEmpty(url, "url");
        Validate.notNull(cacheService, "cacheService");
        Validate.notNullOrEmpty(tags, "tags");
        InputStream inputStream = null;
        try {
            Get getRequest = new Get(url, headers);
            Response response = executeRequest(getRequest);
            inputStream = response.getInputStream();
            cacheService.put(url, inputStream, tags);
        } finally {
            if (inputStream != null) {
                StreamUtils.closeQuietly(inputStream);
            }
        }
    }

    @Override // com.microsoft.krestsdk.services.NetworkProvider
    public void executeHttpFileGetAndWriteToCache(String url, Map<String, String> headers, CacheService cacheService, List<String> tags) throws IOException, MalformedURLException {
        Validate.notNullOrEmpty(url, "url");
        Validate.notNull(cacheService, "cacheService");
        Validate.notNullOrEmpty(tags, "tags");
        InputStream inputStream = null;
        try {
            Get getRequest = new Get(url, headers);
            Response response = executeFileDownloadRequest(getRequest);
            inputStream = response.getInputStream();
            cacheService.put(url, inputStream, tags);
        } finally {
            if (inputStream != null) {
                StreamUtils.closeQuietly(inputStream);
            }
        }
    }

    @Override // com.microsoft.krestsdk.services.NetworkProvider
    public String executeHttpPut(String url, Map<String, String> headers, String body) throws MalformedURLException, IOException {
        Put putRequest = new Put(url, headers, body);
        Response response = executeRequest(putRequest);
        return response.getString();
    }

    @Override // com.microsoft.krestsdk.services.NetworkProvider
    public String executeHttpPost(String url, Map<String, String> requestHeaders, String body) throws URISyntaxException, IOException {
        return executeHttpPost(url, requestHeaders, null, body);
    }

    @Override // com.microsoft.krestsdk.services.NetworkProvider
    public String executeHttpPost(String url, Map<String, String> requestHeaders, Map<String, String> responseHeaders, String body) throws URISyntaxException, IOException {
        Post postRequest = new Post(url, requestHeaders, body);
        Response response = executeRequest(postRequest);
        setReponseHeaders(response, responseHeaders);
        return response.getString();
    }

    private void setReponseHeaders(Response response, Map<String, String> responseHeaders) {
        Map<String, String> headers = response.getHeaders();
        if (responseHeaders != null && headers != null) {
            for (String key : headers.keySet()) {
                if (!responseHeaders.containsKey(key)) {
                    responseHeaders.put(key, headers.get(key));
                }
            }
        }
    }

    @Override // com.microsoft.krestsdk.services.NetworkProvider
    public String executeHttpDelete(String url, Map<String, String> headers) throws URISyntaxException, IOException {
        Response response = executeRequest(new Delete(url, headers));
        return response.getString();
    }

    @Override // com.microsoft.krestsdk.services.NetworkProvider
    public String executeHttpPatch(String url, Map<String, String> headers, String body) throws URISyntaxException, IOException {
        Response response;
        AndroidHttpClient client = AndroidHttpClient.newInstance(this.mUserAgentProvider.getUserAgent());
        HttpPatch patchRequest = new HttpPatch();
        patchRequest.setEntity(new StringEntity(body, "UTF-8"));
        patchRequest.setURI(new URI(url));
        Request request = new Patch(url, headers, body);
        String uri = request.uri;
        Map<String, String> allHeaders = appendDefaultHeaders(request.headers);
        String method = request.getMethod();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            patchRequest.setHeader(header.getKey(), header.getValue());
        }
        HttpTransaction transaction = HttpTransactionFactory.createTransaction();
        transaction.writeRequestTransaction(uri, allHeaders, method);
        HttpResponse httpResponse = null;
        Response response2 = null;
        Map<String, String> responseHeaders = new HashMap<>();
        int responseCode = 0;
        String statusLine = "";
        try {
            BasicHttpParams basicHttpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(basicHttpParams, CONNECTION_TIMEOUT_MS);
            HttpConnectionParams.setSoTimeout(basicHttpParams, READ_TIMEOUT_MS);
            patchRequest.setParams(basicHttpParams);
            transaction.setRequest(request.getBody().getBytes());
            httpResponse = client.execute(patchRequest);
            HttpEntity entity = httpResponse.getEntity();
            InputStream responseStream = AndroidHttpClient.getUngzippedContent(entity);
            if (httpResponse.getAllHeaders() != null) {
                Header[] arr$ = httpResponse.getAllHeaders();
                for (Header header2 : arr$) {
                    HeaderElement[] elements = header2.getElements();
                    if (elements != null && elements.length > 0) {
                        responseHeaders.put(elements[0].getName(), elements[0].getValue());
                    }
                }
            }
            response = new Response(responseStream, responseHeaders);
        } catch (Throwable th) {
            th = th;
        }
        try {
            String string = response.getString();
            if (Compatibility.shouldLogHTTPToFiddler()) {
                MultiReadInputStream mris = response != null ? response.getInputStream() : null;
                if (httpResponse != null) {
                    responseCode = httpResponse.getStatusLine().getStatusCode();
                    statusLine = httpResponse.getStatusLine().getReasonPhrase();
                }
                transaction.writeResponseTransaction(responseHeaders, responseCode, statusLine, mris);
                this.mFiddlerLogger.logHttpCall(transaction);
            }
            if (client != null) {
                client.close();
            }
            return string;
        } catch (Throwable th2) {
            th = th2;
            response2 = response;
            if (Compatibility.shouldLogHTTPToFiddler()) {
                MultiReadInputStream mris2 = response2 != null ? response2.getInputStream() : null;
                if (httpResponse != null) {
                    responseCode = httpResponse.getStatusLine().getStatusCode();
                    statusLine = httpResponse.getStatusLine().getReasonPhrase();
                }
                transaction.writeResponseTransaction(responseHeaders, responseCode, statusLine, mris2);
                this.mFiddlerLogger.logHttpCall(transaction);
            }
            if (client != null) {
                client.close();
            }
            throw th;
        }
    }

    private <T extends Request> Response executeRequest(T request) throws MalformedURLException, IOException {
        BufferedWriter bw;
        Validate.notNull(request, "request");
        String uri = request.uri;
        Map<String, String> headers = appendDefaultHeaders(request.headers);
        String method = request.getMethod();
        HttpTransaction transaction = HttpTransactionFactory.createTransaction();
        transaction.writeRequestTransaction(uri, headers, method);
        URL androidURL = new URL(uri);
        HttpURLConnection urlConnection = (HttpURLConnection) androidURL.openConnection();
        prepareHeader(urlConnection, headers);
        Response response = null;
        try {
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT_MS);
            urlConnection.setReadTimeout(READ_TIMEOUT_MS);
            urlConnection.setRequestMethod(method);
            if (!"GET".equals(method) && !"DELETE".equals(method) && !TextUtils.isEmpty(request.getBody())) {
                transaction.setRequest(request.getBody().getBytes());
                urlConnection.setDoOutput(true);
                BufferedWriter bw2 = null;
                try {
                    bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    bw.write(request.getBody());
                    if (bw != null) {
                        bw.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    bw2 = bw;
                    if (bw2 != null) {
                        bw2.close();
                    }
                    throw th;
                }
            }
            InputStream in = urlConnection.getInputStream();
            String statusLine = urlConnection.getResponseMessage();
            Map<String, String> responseHeaders = getHeaderMap(urlConnection.getHeaderFields());
            String contentEncoding = urlConnection.getContentEncoding();
            if (contentEncoding != null && contentEncoding.contains("gzip")) {
                in = new GZIPInputStream(in);
            }
            Response response2 = new Response(in, responseHeaders);
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 404 || responseCode == 401) {
                throw new HttpResponseException(responseCode, urlConnection.getResponseMessage());
            }
            if (in != null) {
                StreamUtils.closeQuietly(in);
            }
            urlConnection.disconnect();
            if (Compatibility.shouldLogHTTPToFiddler()) {
                MultiReadInputStream mris = response2 != null ? response2.getInputStream() : null;
                transaction.writeResponseTransaction(responseHeaders, responseCode, statusLine, mris);
                this.mFiddlerLogger.logHttpCall(transaction);
            }
            return response2;
        } catch (Throwable th3) {
            int responseCode2 = urlConnection.getResponseCode();
            if (responseCode2 == 404 || responseCode2 == 401) {
                throw new HttpResponseException(responseCode2, urlConnection.getResponseMessage());
            }
            if (0 != 0) {
                StreamUtils.closeQuietly(null);
            }
            urlConnection.disconnect();
            if (Compatibility.shouldLogHTTPToFiddler()) {
                MultiReadInputStream mris2 = 0 != 0 ? response.getInputStream() : null;
                transaction.writeResponseTransaction(null, responseCode2, "", mris2);
                this.mFiddlerLogger.logHttpCall(transaction);
            }
            throw th3;
        }
    }

    private <T extends Request> Response executeFileDownloadRequest(T request) throws MalformedURLException, IOException {
        URL androidURL = new URL(request.uri);
        HttpURLConnection httpConn = (HttpURLConnection) androidURL.openConnection();
        int responseCode = httpConn.getResponseCode();
        if (responseCode == 200) {
            InputStream in = httpConn.getInputStream();
            Map<String, String> responseHeaders = getHeaderMap(httpConn.getHeaderFields());
            return new Response(in, responseHeaders);
        }
        httpConn.disconnect();
        return null;
    }

    private Map<String, String> getHeaderMap(Map<String, List<String>> headerFields) {
        if (headerFields == null) {
            return null;
        }
        Map<String, String> headerMap = new HashMap<>();
        for (String key : headerFields.keySet()) {
            if (key != null && !key.equalsIgnoreCase("null") && headerFields.get(key) != null && headerFields.get(key).size() > 0) {
                headerMap.put(key, headerFields.get(key).get(0));
            }
        }
        return headerMap;
    }

    private void prepareHeader(HttpURLConnection connection, Map<String, String> headers) {
        for (Map.Entry<String, String> header : headers.entrySet()) {
            connection.addRequestProperty(header.getKey(), header.getValue());
        }
    }

    private Map<String, String> appendDefaultHeaders(Map<String, String> headers) {
        Map<String, String> allHeaders = getDefaultHeaders();
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                allHeaders.put(header.getKey(), header.getValue());
            }
        }
        return allHeaders;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class Response {
        Map<String, String> headers;
        MultiReadInputStream mris;

        public Response(InputStream stream, Map<String, String> headers) throws IOException {
            this.mris = new MultiReadInputStream(stream);
            this.headers = headers;
        }

        public MultiReadInputStream getInputStream() {
            this.mris.reset();
            return this.mris;
        }

        public String getString() throws IOException {
            this.mris.reset();
            try {
                return StreamUtils.toString(this.mris);
            } finally {
                StreamUtils.closeQuietly(this.mris);
            }
        }

        public byte[] getBytes() throws IOException {
            this.mris.reset();
            try {
                return StreamUtils.toArray(this.mris);
            } finally {
                StreamUtils.closeQuietly(this.mris);
            }
        }

        public Map<String, String> getHeaders() {
            return this.headers;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class Request {
        public Map<String, String> headers;
        public String uri;

        public Request(String uri) {
            this.uri = uri;
        }

        public Request(String uri, Map<String, String> headers) {
            this.uri = uri;
            this.headers = headers;
        }

        public String getMethod() {
            throw new UnsupportedOperationException();
        }

        public String getBody() {
            throw new UnsupportedOperationException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class Get extends Request {
        public static final String METHOD_NAME = "GET";

        public Get(String uri) {
            super(uri);
        }

        public Get(String uri, Map<String, String> headers) {
            super(uri, headers);
        }

        @Override // com.microsoft.krestsdk.services.NetworkProviderImpl.Request
        public String getMethod() {
            return "GET";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class Post extends Request {
        public static final String METHOD_NAME = "POST";
        private String body;

        public Post(String uri, Map<String, String> headers, String body) {
            super(uri, headers);
            this.body = body;
        }

        @Override // com.microsoft.krestsdk.services.NetworkProviderImpl.Request
        public String getMethod() {
            return "POST";
        }

        @Override // com.microsoft.krestsdk.services.NetworkProviderImpl.Request
        public String getBody() {
            return this.body;
        }
    }

    /* loaded from: classes.dex */
    private class Put extends Request {
        public static final String METHOD_NAME = "PUT";
        private String body;

        public Put(String uri, Map<String, String> headers, String body) {
            super(uri, headers);
            this.body = body;
        }

        @Override // com.microsoft.krestsdk.services.NetworkProviderImpl.Request
        public String getMethod() {
            return "PUT";
        }

        @Override // com.microsoft.krestsdk.services.NetworkProviderImpl.Request
        public String getBody() {
            return this.body;
        }
    }

    /* loaded from: classes.dex */
    private class Delete extends Request {
        public static final String METHOD_NAME = "DELETE";

        public Delete(String uri, Map<String, String> headers) {
            super(uri, headers);
        }

        @Override // com.microsoft.krestsdk.services.NetworkProviderImpl.Request
        public String getMethod() {
            return "DELETE";
        }
    }

    /* loaded from: classes.dex */
    private class Patch extends Request {
        public static final String METHOD_NAME = "PATCH";
        private String body;

        public Patch(String uri, Map<String, String> headers, String body) {
            super(uri, headers);
            this.body = body;
        }

        @Override // com.microsoft.krestsdk.services.NetworkProviderImpl.Request
        public String getMethod() {
            return "PATCH";
        }

        @Override // com.microsoft.krestsdk.services.NetworkProviderImpl.Request
        public String getBody() {
            return this.body;
        }
    }
}

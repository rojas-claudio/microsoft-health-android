package com.microsoft.krestsdk.services;

import android.os.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.microsoft.applicationinsights.contracts.RequestData;
import com.microsoft.band.util.StreamUtils;
import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.auth.credentials.KCredential;
import com.microsoft.krestsdk.services.KRestServiceV1;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.http.client.HttpResponseException;
/* loaded from: classes.dex */
class KRestQuery<T> extends AsyncTask<String, Void, T> {
    private final String TAG;
    private CacheService mCacheService;
    private KRestServiceV1.NetworkCallback<T> mCallback;
    private final CredentialsManager mCredentialsManager;
    private Exception mException;
    private final Gson mGsonDeserializer;
    private final NetworkProvider mNetworkProvider;
    private String mRestUrl;
    private List<String> mTags;
    private RequestData mTelemetryRequest;
    private boolean mTreat404AsEmptyResult;
    private Type mType;
    private Map<String, String> mUrlParams;

    public KRestQuery(NetworkProvider networkProvider, CredentialsManager credentialsManager, CacheService cacheService, List<String> tags, Gson gsonDeserializer, String restUrl, KRestServiceV1.NetworkCallback<T> callback, TypeToken<T> token) {
        this(networkProvider, credentialsManager, cacheService, tags, gsonDeserializer, restUrl, callback, new HashMap(), token);
    }

    public KRestQuery(NetworkProvider networkProvider, CredentialsManager credentialsManager, CacheService cacheService, List<String> tags, Gson gsonDeserializer, String restUrl, KRestServiceV1.NetworkCallback<T> callback, Map<String, String> urlParams, TypeToken<T> token) {
        this.TAG = getClass().getSimpleName();
        this.mTreat404AsEmptyResult = false;
        this.mNetworkProvider = networkProvider;
        this.mCredentialsManager = credentialsManager;
        this.mCacheService = cacheService;
        this.mTags = tags;
        this.mGsonDeserializer = gsonDeserializer;
        this.mRestUrl = restUrl;
        this.mCallback = callback;
        this.mUrlParams = urlParams;
        this.mType = token.getType();
    }

    public KRestQuery(NetworkProvider networkProvider, CredentialsManager credentialsManager, CacheService cacheService, List<String> tags, String restUrl, KRestServiceV1.NetworkCallback<T> callback, Map<String, String> urlParams) {
        this.TAG = getClass().getSimpleName();
        this.mTreat404AsEmptyResult = false;
        this.mNetworkProvider = networkProvider;
        this.mCredentialsManager = credentialsManager;
        this.mCacheService = cacheService;
        this.mTags = tags;
        this.mRestUrl = restUrl;
        this.mCallback = callback;
        this.mUrlParams = urlParams;
        this.mGsonDeserializer = null;
    }

    public void setTreatNotFoundAsEmptyResult(boolean treatAsEmptyResult) {
        this.mTreat404AsEmptyResult = treatAsEmptyResult;
    }

    public void setTelemetryRequest(RequestData request) {
        this.mTelemetryRequest = request;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public T doInBackground(String... strings) {
        Map<String, String> headers;
        String requestUrl = null;
        String response = null;
        T parsedResponse = null;
        Reader reader = null;
        try {
            try {
                String guid = UUID.randomUUID().toString();
                headers = getHeaders(guid);
                requestUrl = KRestServiceUtils.populateUrl(this.mRestUrl, this.mUrlParams);
            } catch (Exception e) {
                exception = e;
            }
            if (this.mCacheService != null) {
                if (!this.mCacheService.isResponseCached(requestUrl)) {
                    makeRestCallAndThenCache(requestUrl, headers);
                }
                if (this.mGsonDeserializer == null) {
                    parsedResponse = null;
                    if (0 != 0) {
                        StreamUtils.closeQuietly(null);
                    }
                } else {
                    InputStream cachedResponseStream = this.mCacheService.getCachedResponseAsStream(requestUrl);
                    if (cachedResponseStream == null) {
                        makeRestCallAndThenCache(requestUrl, headers);
                        cachedResponseStream = this.mCacheService.getCachedResponseAsStream(requestUrl);
                    }
                    if (cachedResponseStream != null) {
                        Reader reader2 = new BufferedReader(new InputStreamReader(cachedResponseStream, "UTF-8"), 8192);
                        try {
                            JsonReader jsonReader = new JsonReader(reader2);
                            parsedResponse = (T) this.mGsonDeserializer.fromJson(jsonReader, this.mType);
                            QueryUtils.validateDeserializedObject(parsedResponse);
                            if (reader2 != null) {
                                StreamUtils.closeQuietly(reader2);
                            }
                        } catch (Exception e2) {
                            exception = e2;
                            reader = reader2;
                            boolean handled = false;
                            if (exception instanceof HttpResponseException) {
                                HttpResponseException convertedException = exception;
                                handled = convertedException.getStatusCode() == 404 && this.mTreat404AsEmptyResult;
                            }
                            if (!handled) {
                                KRestServiceUtils.logException(exception, "GET", requestUrl);
                                this.mException = exception;
                            }
                            if (reader != null) {
                                StreamUtils.closeQuietly(reader);
                            }
                            KLog.logPrivate(this.TAG, "[Request URL]:  " + requestUrl + " [Response]: " + response);
                            return parsedResponse;
                        } catch (Throwable th) {
                            th = th;
                            reader = reader2;
                            if (reader != null) {
                                StreamUtils.closeQuietly(reader);
                            }
                            throw th;
                        }
                    }
                }
                return parsedResponse;
            }
            response = this.mNetworkProvider.executeHttpGet(requestUrl, headers);
            if (this.mGsonDeserializer != null) {
                parsedResponse = (T) this.mGsonDeserializer.fromJson(response, this.mType);
                QueryUtils.validateDeserializedObject(parsedResponse);
            }
            if (0 != 0) {
                StreamUtils.closeQuietly(null);
            }
            KLog.logPrivate(this.TAG, "[Request URL]:  " + requestUrl + " [Response]: " + response);
            return parsedResponse;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    @Override // android.os.AsyncTask
    public void onPostExecute(T result) {
        if (this.mException == null) {
            this.mCallback.callback(result);
        } else {
            this.mCallback.onError(this.mException);
        }
    }

    public String readFully(InputStream inputStream, String encoding) throws IOException {
        return new String(readFully(inputStream), encoding);
    }

    private byte[] readFully(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int length = inputStream.read(buffer);
            if (length != -1) {
                baos.write(buffer, 0, length);
            } else {
                return baos.toByteArray();
            }
        }
    }

    private void makeRestCallAndThenCache(String requestUrl, Map<String, String> headers) throws MalformedURLException, IOException {
        this.mNetworkProvider.executeHttpGetAndWriteToCache(requestUrl, headers, this.mCacheService, this.mTags);
    }

    private Map<String, String> getHeaders(String guid) throws HttpResponseException {
        Map<String, String> headers = new HashMap<>();
        headers.put(TelemetryConstants.TimedEvents.Cloud.CLOUD_CALL_UUID, guid);
        headers.put(KCloudConstants.ACCEPT_HEADER, "application/json");
        KCredential credentials = this.mCredentialsManager.getCredentials();
        if (!KRestServiceUtils.addKRestQueryHeaders(headers, this.mUrlParams, credentials)) {
            throw new HttpResponseException((int) RestService.HTTP_ERROR_CODE_UNAUTHORIZE, "There are no credentials");
        }
        return headers;
    }
}

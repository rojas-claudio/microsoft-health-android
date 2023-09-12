package com.microsoft.krestsdk.services;

import android.os.AsyncTask;
import com.facebook.internal.ServerProtocol;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.auth.credentials.KCredential;
import com.microsoft.krestsdk.services.KRestServiceV1;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpResponseException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class KRestQueryPut<TRequest, TResponse> extends AsyncTask<Void, Void, TResponse> {
    private final String TAG;
    private CacheService mCacheService;
    private final KRestServiceV1.NetworkCallback<TResponse> mCallBack;
    private final CredentialsManager mCredentialsManager;
    private Exception mException;
    private final Gson mGsonDeserializer;
    private final NetworkProvider mNetworkProvider;
    private final TRequest mRequest;
    private final String mRestUrl;
    private List<String> mTags;
    private final Type mType;
    private final Map<String, String> mUrlParams;

    public KRestQueryPut(NetworkProvider networkProvider, CredentialsManager credentialsManager, CacheService cacheService, List<String> tags, Gson gsonDeserializer, String restUrl, TRequest request, KRestServiceV1.NetworkCallback<TResponse> callback, TypeToken<TResponse> token) {
        this(networkProvider, credentialsManager, cacheService, tags, gsonDeserializer, restUrl, new HashMap(), request, callback, token);
    }

    public KRestQueryPut(NetworkProvider networkProvider, CredentialsManager credentialStore, CacheService cacheService, List<String> tags, Gson gsonDeserializer, String restUrl, Map<String, String> urlParams, TRequest request, KRestServiceV1.NetworkCallback<TResponse> callback, TypeToken<TResponse> token) {
        this.TAG = getClass().getSimpleName();
        Validate.notNull(restUrl, "restUrl");
        Validate.notNull(urlParams, "urlParams");
        Validate.notNull(callback, "callback");
        Validate.notNull(token, ServerProtocol.DIALOG_RESPONSE_TYPE_TOKEN);
        this.mNetworkProvider = networkProvider;
        this.mCredentialsManager = credentialStore;
        this.mCacheService = cacheService;
        this.mTags = tags;
        this.mGsonDeserializer = gsonDeserializer;
        this.mRestUrl = restUrl;
        this.mUrlParams = urlParams;
        this.mRequest = request;
        this.mCallBack = callback;
        this.mType = token.getType();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public TResponse doInBackground(Void... params) {
        Map<String, String> headers;
        KCredential credentials;
        String requestUrl = null;
        String response = null;
        TResponse parsedResponse = null;
        try {
            headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put(KCloudConstants.ENCODING_TYPE, "UTF-8");
            credentials = this.mCredentialsManager.getCredentials();
        } catch (Exception exception) {
            KRestServiceUtils.logException(exception, getMethodName(), null);
            this.mException = exception;
        }
        if (!KRestServiceUtils.addKRestQueryHeaders(headers, this.mUrlParams, credentials)) {
            throw new HttpResponseException((int) RestService.HTTP_ERROR_CODE_UNAUTHORIZE, "There are no credentials");
        }
        requestUrl = KRestServiceUtils.populateUrl(this.mRestUrl, this.mUrlParams);
        String body = this.mGsonDeserializer.toJson(this.mRequest);
        response = executeNetwork(this.mNetworkProvider, requestUrl, headers, body);
        if (!StringUtils.isBlank(response)) {
            parsedResponse = (TResponse) this.mGsonDeserializer.fromJson(response, this.mType);
            QueryUtils.validateDeserializedObject(parsedResponse);
        }
        if (this.mCacheService != null) {
            this.mCacheService.removeForTags(this.mTags);
        }
        KLog.logPrivate(this.TAG, "[Request URL]:  " + requestUrl + " [Response]: " + response);
        return parsedResponse;
    }

    protected String executeNetwork(NetworkProvider networkProvider, String url, Map<String, String> headers, String body) throws IOException, URISyntaxException {
        return networkProvider.executeHttpPut(url, headers, body);
    }

    protected String getMethodName() {
        return "PUT";
    }

    @Override // android.os.AsyncTask
    public void onPostExecute(TResponse response) {
        if (this.mException == null) {
            this.mCallBack.callback(response);
        } else {
            this.mCallBack.onError(this.mException);
        }
    }
}

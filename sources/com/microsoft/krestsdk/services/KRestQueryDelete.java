package com.microsoft.krestsdk.services;

import android.os.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microsoft.kapp.cache.CacheService;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.auth.credentials.KCredential;
import com.microsoft.krestsdk.services.KRestServiceV1;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.client.HttpResponseException;
/* loaded from: classes.dex */
class KRestQueryDelete<TRequest, TResponse> extends AsyncTask<Void, Void, TResponse> {
    private final String TAG;
    private CacheService mCacheService;
    private KRestServiceV1.NetworkCallback<TResponse> mCallback;
    private final CredentialsManager mCredentialsManager;
    private Exception mException;
    private final Gson mGsonDeserializer;
    private final NetworkProvider mNetworkProvider;
    private String mRestUrl;
    private List<String> mTags;
    private Map<String, String> mUrlParams;

    public KRestQueryDelete(NetworkProvider networkProvider, CredentialsManager credentialsManager, CacheService cacheService, List<String> tags, Gson gsonDeserializer, String restUrl, KRestServiceV1.NetworkCallback<TResponse> callback, TypeToken<TResponse> token) {
        this(networkProvider, credentialsManager, cacheService, tags, gsonDeserializer, restUrl, callback, new HashMap(), token);
    }

    public KRestQueryDelete(NetworkProvider networkProvider, CredentialsManager credentialsManager, CacheService cacheService, List<String> tags, Gson gsonDeserializer, String restUrl, KRestServiceV1.NetworkCallback<TResponse> callback, Map<String, String> urlParams, TypeToken<TResponse> token) {
        this.TAG = getClass().getSimpleName();
        this.mNetworkProvider = networkProvider;
        this.mCredentialsManager = credentialsManager;
        this.mCacheService = cacheService;
        this.mTags = tags;
        this.mGsonDeserializer = gsonDeserializer;
        this.mRestUrl = restUrl;
        this.mCallback = callback;
        this.mUrlParams = urlParams;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public TResponse doInBackground(Void... params) {
        Map<String, String> headers;
        KCredential credentials;
        String requestUrl = null;
        String response = null;
        try {
            headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            credentials = this.mCredentialsManager.getCredentials();
        } catch (Exception exception) {
            KRestServiceUtils.logException(exception, "DELETE", null);
            this.mException = exception;
        }
        if (!KRestServiceUtils.addKRestQueryHeaders(headers, this.mUrlParams, credentials)) {
            throw new HttpResponseException((int) RestService.HTTP_ERROR_CODE_UNAUTHORIZE, "There are no credentials");
        }
        requestUrl = KRestServiceUtils.populateUrl(this.mRestUrl, this.mUrlParams);
        response = this.mNetworkProvider.executeHttpDelete(requestUrl, headers);
        if (this.mCacheService != null) {
            this.mCacheService.removeForTags(this.mTags);
        }
        KLog.logPrivate(this.TAG, "[Request URL]:  " + requestUrl + " [Response]: " + response);
        return null;
    }

    @Override // android.os.AsyncTask
    public void onPostExecute(TResponse result) {
        if (this.mException == null) {
            this.mCallback.callback(result);
        } else {
            this.mCallback.onError(this.mException);
        }
    }
}

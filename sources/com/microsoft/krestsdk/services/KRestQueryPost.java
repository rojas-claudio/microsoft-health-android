package com.microsoft.krestsdk.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microsoft.kapp.cache.CacheService;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.services.KRestServiceV1;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class KRestQueryPost<TRequest, TResponse> extends KRestQueryPut<TRequest, TResponse> {
    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.microsoft.krestsdk.services.KRestQueryPut, android.os.AsyncTask
    public /* bridge */ /* synthetic */ void onPostExecute(Object x0) {
        super.onPostExecute(x0);
    }

    public KRestQueryPost(NetworkProvider networkProvider, CredentialsManager credentialsManager, CacheService cacheService, List<String> tags, Gson gsonDeserializer, String restUrl, TRequest request, KRestServiceV1.NetworkCallback<TResponse> callback, TypeToken<TResponse> token) {
        this(networkProvider, credentialsManager, cacheService, tags, gsonDeserializer, restUrl, new HashMap(), request, callback, token);
    }

    public KRestQueryPost(NetworkProvider networkProvider, CredentialsManager credentialsManager, CacheService cacheService, List<String> tags, Gson gsonDeserializer, String restUrl, Map<String, String> urlParams, TRequest request, KRestServiceV1.NetworkCallback<TResponse> callback, TypeToken<TResponse> token) {
        super(networkProvider, credentialsManager, cacheService, tags, gsonDeserializer, restUrl, urlParams, request, callback, token);
    }

    @Override // com.microsoft.krestsdk.services.KRestQueryPut
    protected String executeNetwork(NetworkProvider networkProvider, String url, Map<String, String> headers, String body) throws IOException, URISyntaxException {
        return networkProvider.executeHttpPost(url, headers, body);
    }

    @Override // com.microsoft.krestsdk.services.KRestQueryPut
    protected String getMethodName() {
        return "POST";
    }
}

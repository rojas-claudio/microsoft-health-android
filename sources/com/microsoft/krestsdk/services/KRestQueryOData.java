package com.microsoft.krestsdk.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microsoft.kapp.cache.CacheService;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.models.ODataResponse;
import com.microsoft.krestsdk.services.KRestServiceV1;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes.dex */
class KRestQueryOData<T> extends KRestQuery<ODataResponse<T>> {
    public KRestQueryOData(NetworkProvider networkProvider, CredentialsManager credentialsManager, CacheService cacheService, List<String> tags, Gson gsonDeserializer, ODataRequest request, TypeToken<ODataResponse<T>> token, final KRestServiceV1.NetworkCallback<T> callback) {
        super(networkProvider, credentialsManager, cacheService, tags, gsonDeserializer, request.getUrl(), new KRestServiceV1.NetworkCallback<ODataResponse<T>>(null, null) { // from class: com.microsoft.krestsdk.services.KRestQueryOData.1
            @Override // com.microsoft.krestsdk.services.KRestServiceV1.NetworkCallback, com.microsoft.kapp.Callback
            public /* bridge */ /* synthetic */ void callback(Object x0) {
                callback((ODataResponse) ((ODataResponse) x0));
            }

            public void callback(ODataResponse<T> result) {
                callback.callback(result.getValue());
            }

            @Override // com.microsoft.krestsdk.services.KRestServiceV1.NetworkCallback, com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        }, new HashMap(), token);
    }
}

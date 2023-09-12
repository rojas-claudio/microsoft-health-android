package com.microsoft.krestsdk.services;

import java.io.IOException;
import java.net.URISyntaxException;
import org.json.JSONArray;
import org.json.JSONException;
/* loaded from: classes.dex */
public class NetProviderListFetcher implements ProviderListFetcher {
    private NetworkProvider mNetworkProvider;

    public NetProviderListFetcher(NetworkProvider provider) {
        this.mNetworkProvider = provider;
    }

    @Override // com.microsoft.krestsdk.services.ProviderListFetcher
    public JSONArray getProviderList(String providerListURL) throws IOException, JSONException, URISyntaxException {
        String providerResponse = this.mNetworkProvider.executeHttpGet(providerListURL, null);
        JSONArray providerList = new JSONArray(providerResponse);
        return providerList;
    }
}

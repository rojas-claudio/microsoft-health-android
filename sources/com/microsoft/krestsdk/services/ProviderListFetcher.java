package com.microsoft.krestsdk.services;

import java.io.IOException;
import java.net.URISyntaxException;
import org.json.JSONArray;
import org.json.JSONException;
/* loaded from: classes.dex */
public interface ProviderListFetcher {
    JSONArray getProviderList(String str) throws IOException, JSONException, URISyntaxException;
}

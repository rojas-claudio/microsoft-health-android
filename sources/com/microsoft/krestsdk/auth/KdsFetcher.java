package com.microsoft.krestsdk.auth;

import java.io.IOException;
import java.net.URISyntaxException;
import org.json.JSONException;
/* loaded from: classes.dex */
public interface KdsFetcher {
    ServiceInfo getServiceInfo(String str, String str2) throws URISyntaxException, IOException, JSONException;
}

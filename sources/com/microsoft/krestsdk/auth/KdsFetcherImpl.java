package com.microsoft.krestsdk.auth;

import com.microsoft.kapp.utils.Constants;
import com.microsoft.krestsdk.services.NetworkProvider;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.HttpResponseException;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class KdsFetcherImpl implements KdsFetcher {
    private static final String USER_ME_ENDPOINT = "api/v1/user";
    private NetworkProvider mNetworkProvider;

    public KdsFetcherImpl(NetworkProvider networkProvider) {
        this.mNetworkProvider = networkProvider;
    }

    @Override // com.microsoft.krestsdk.auth.KdsFetcher
    public ServiceInfo getServiceInfo(String authURL, String acsToken) throws URISyntaxException, IOException, JSONException {
        String url = authURL + USER_ME_ENDPOINT;
        Map<String, String> requestHeaders = new HashMap<>();
        Map<String, String> responseHeaders = new HashMap<>();
        requestHeaders.put(Constants.AUTHORIZATION_HEADER_NAME, acsToken);
        requestHeaders.put("Content-Type", "application/json");
        boolean isNewUser = false;
        String response = "";
        try {
            response = this.mNetworkProvider.executeHttpGet(url, requestHeaders, responseHeaders);
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 404) {
                isNewUser = true;
            } else {
                throw e;
            }
        }
        if (isNewUser) {
            responseHeaders.clear();
            response = this.mNetworkProvider.executeHttpPost(url, requestHeaders, responseHeaders, "{}");
        }
        JSONObject kdsResponse = new JSONObject(response);
        String katHeader = responseHeaders.get(Constants.AUTHORIZATION_HEADER_NAME);
        String kat = TokenOperations.extractSwtFromHeader(katHeader);
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.AccessToken = kat;
        serviceInfo.UserId = kdsResponse.getString("ODSUserID");
        serviceInfo.PodAddress = kdsResponse.getString("EndPoint");
        serviceInfo.FUSEndpoint = kdsResponse.getString("FUSEndPoint");
        serviceInfo.IsNewlyCreatedProfile = isNewUser;
        serviceInfo.AuthedHnFEndPoint = kdsResponse.getString("AuthedHnFEndPoint");
        serviceInfo.AuthedHnFQueryParameters = kdsResponse.getString("AuthedHnFQueryParameters");
        serviceInfo.ProfileCreatedDate = kdsResponse.getString("CreatedOn");
        return serviceInfo;
    }
}

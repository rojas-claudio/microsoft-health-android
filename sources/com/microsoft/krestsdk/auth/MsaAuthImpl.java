package com.microsoft.krestsdk.auth;

import android.os.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.CloudEnvironment;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.GsonUtils;
import com.microsoft.krestsdk.auth.credentials.CredentialStore;
import com.microsoft.krestsdk.auth.credentials.KCredential;
import com.microsoft.krestsdk.auth.credentials.MsaCredential;
import com.microsoft.krestsdk.services.KRestServiceUtils;
import com.microsoft.krestsdk.services.NetworkProvider;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class MsaAuthImpl implements MsaAuth {
    private static final String ACS_TOKEN = "access_token";
    private static final String EXPIRES_IN = "expires_in";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String USER_PROFILE_ENDPOINT = "api/v1/user/live";
    private CredentialStore mCredentialStore;
    private NetworkProvider mNetworkProvider;
    private static final String TAG = MsaAuthImpl.class.getSimpleName();
    private static final Gson CUSTOM_GSON_DESERIALIZER = GsonUtils.getCustomDeserializer();

    public MsaAuthImpl(NetworkProvider networkProvider, CredentialStore credentialStore) {
        this.mCredentialStore = credentialStore;
        this.mNetworkProvider = networkProvider;
    }

    @Override // com.microsoft.krestsdk.auth.MsaAuth
    public MsaCredential refreshAccessToken(MsaCredential msaCredential) {
        if (msaCredential == null || msaCredential.getRefreshToken() == null) {
            return null;
        }
        String body = String.format(Constants.OAUTH_REFRESH_TOKEN_BODY, CloudEnvironment.getDefault().getRealm(), msaCredential.getRefreshToken());
        JsonObject jsonResult = null;
        try {
            jsonResult = getMSAToken(body);
        } catch (Exception e) {
            KLog.e(TAG, "Exception trying to get the token from the msa response", e);
        }
        MsaCredential result = jsonToCredential(jsonResult);
        return result;
    }

    @Override // com.microsoft.krestsdk.auth.MsaAuth
    public void getUserProfile(String authUrl, Callback<JsonObject> callback) {
        MsaCredential msaCredential;
        KCredential credentials = this.mCredentialStore.getCredentials();
        if (credentials != null && (msaCredential = credentials.getMsaCredential()) != null && msaCredential.getAccessToken() != null) {
            String url = authUrl + USER_PROFILE_ENDPOINT;
            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put(Constants.AUTHORIZATION_HEADER_NAME, msaCredential.getAccessToken());
            requestHeaders.put("Content-Type", "application/json");
            OAuthQueryGet<JsonObject> query = new OAuthQueryGet<>(this.mNetworkProvider, CUSTOM_GSON_DESERIALIZER, url, requestHeaders, callback, new TypeToken<JsonObject>() { // from class: com.microsoft.krestsdk.auth.MsaAuthImpl.1
            });
            query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        } else if (callback != null) {
            callback.onError(new IllegalArgumentException("The credentials are null"));
        }
    }

    private MsaCredential jsonToCredential(JsonObject jsonResult) {
        if (jsonResult == null || !jsonResult.has("access_token") || !jsonResult.has(REFRESH_TOKEN) || !jsonResult.has("expires_in")) {
            return null;
        }
        String accessToken = jsonResult.get("access_token").getAsString();
        String refreshToken = jsonResult.get(REFRESH_TOKEN).getAsString();
        int expiresIn = jsonResult.get("expires_in").getAsInt();
        DateTime expirationDate = DateTime.now().plusSeconds(expiresIn);
        return new MsaCredential(accessToken, refreshToken, expirationDate);
    }

    private JsonObject getMSAToken(String body) {
        String response = null;
        JsonObject parsedResponse = null;
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", Constants.APPLICATION_URL_ENCODED);
            String requestBody = body != null ? body : "";
            response = this.mNetworkProvider.executeHttpPost(Constants.OAUTH_REFRESH_TOKEN, headers, requestBody);
            if (!StringUtils.isBlank(response)) {
                parsedResponse = (JsonObject) CUSTOM_GSON_DESERIALIZER.fromJson(response, (Class<Object>) JsonObject.class);
            }
        } catch (Exception exception) {
            KRestServiceUtils.logException(exception, "POST", Constants.OAUTH_REFRESH_TOKEN);
            KLog.e(TAG, "unable to refresh token", exception);
        }
        KLog.v(TAG, "[Request URL]:  https://login.live.com/oauth20_token.srf [Response]: " + response);
        return parsedResponse;
    }
}

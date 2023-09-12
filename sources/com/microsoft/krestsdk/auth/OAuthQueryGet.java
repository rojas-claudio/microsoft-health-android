package com.microsoft.krestsdk.auth;

import android.os.AsyncTask;
import com.facebook.internal.ServerProtocol;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.krestsdk.services.KRestServiceUtils;
import com.microsoft.krestsdk.services.NetworkProvider;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
/* loaded from: classes.dex */
class OAuthQueryGet<TResponse> extends AsyncTask<Void, Void, TResponse> {
    private final String TAG = getClass().getSimpleName();
    private final Callback<TResponse> mCallBack;
    private Exception mException;
    private final Gson mGsonDeserializer;
    private final NetworkProvider mNetworkProvider;
    private final Map<String, String> mRequestHeaders;
    private final String mRestUrl;
    private final Type mType;

    public OAuthQueryGet(NetworkProvider networkProvider, Gson gsonDeserializer, String restUrl, Map<String, String> requestHeaders, Callback<TResponse> callback, TypeToken<TResponse> token) {
        Validate.notNull(restUrl, "restUrl");
        Validate.notNull(callback, "callback");
        Validate.notNull(token, ServerProtocol.DIALOG_RESPONSE_TYPE_TOKEN);
        this.mNetworkProvider = networkProvider;
        this.mGsonDeserializer = gsonDeserializer;
        this.mRestUrl = restUrl;
        this.mRequestHeaders = requestHeaders;
        this.mCallBack = callback;
        this.mType = token.getType();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public TResponse doInBackground(Void... params) {
        String response = null;
        TResponse parsedResponse = null;
        try {
            response = executeNetwork(this.mNetworkProvider, this.mRestUrl, this.mRequestHeaders);
            if (this.mType == Void.class) {
                response = null;
            } else if (!StringUtils.isBlank(response)) {
                parsedResponse = (TResponse) this.mGsonDeserializer.fromJson(response, this.mType);
            }
        } catch (Exception exception) {
            KRestServiceUtils.logException(exception, getMethodName(), this.mRestUrl);
            this.mException = exception;
        }
        KLog.logPrivate(this.TAG, "[Request URL]:  " + this.mRestUrl + " [Response]: " + response);
        return parsedResponse;
    }

    protected String executeNetwork(NetworkProvider networkProvider, String url, Map<String, String> headers) throws IOException, URISyntaxException {
        return networkProvider.executeHttpGet(url, headers);
    }

    protected String getMethodName() {
        return "GET";
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

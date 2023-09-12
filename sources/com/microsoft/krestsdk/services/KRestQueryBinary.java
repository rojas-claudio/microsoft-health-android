package com.microsoft.krestsdk.services;

import android.os.AsyncTask;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.auth.credentials.KCredential;
import com.microsoft.krestsdk.services.KRestServiceV1;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.client.HttpResponseException;
/* loaded from: classes.dex */
class KRestQueryBinary extends AsyncTask<String, Void, byte[]> {
    private final String TAG = getClass().getSimpleName();
    private KRestServiceV1.NetworkCallback<byte[]> mCallback;
    private final CredentialsManager mCredentialsManager;
    private Exception mException;
    private final NetworkProvider mNetworkProvider;
    private String mRestUrl;
    private Map<String, String> mUrlParams;

    public KRestQueryBinary(NetworkProvider networkProvider, CredentialsManager credentialsManager, String restUrl, HashMap<String, String> urlParams, KRestServiceV1.NetworkCallback<byte[]> callback) {
        this.mNetworkProvider = networkProvider;
        this.mCredentialsManager = credentialsManager;
        this.mRestUrl = restUrl;
        this.mCallback = callback;
        this.mUrlParams = urlParams;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public byte[] doInBackground(String... strings) {
        Map<String, String> headers;
        KCredential credentials;
        byte[] response = null;
        try {
            headers = new HashMap<>();
            credentials = this.mCredentialsManager.getCredentials();
        } catch (Exception exception) {
            KRestServiceUtils.logException(exception, "GET", this.mRestUrl);
            this.mException = exception;
        }
        if (!KRestServiceUtils.addKRestQueryHeaders(headers, this.mUrlParams, credentials)) {
            throw new HttpResponseException((int) RestService.HTTP_ERROR_CODE_UNAUTHORIZE, "There are no credentials");
        }
        this.mRestUrl = KRestServiceUtils.populateUrl(this.mRestUrl, this.mUrlParams);
        response = this.mNetworkProvider.executeHttpGetBinary(this.mRestUrl, headers);
        KLog.logPrivate(this.TAG, "[Request URL]:  " + this.mRestUrl + " [Response]: " + response);
        return response;
    }

    @Override // android.os.AsyncTask
    public void onPostExecute(byte[] result) {
        if (this.mException == null) {
            this.mCallback.callback(result);
        } else {
            this.mCallback.onError(this.mException);
        }
    }
}

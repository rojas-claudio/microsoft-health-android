package com.microsoft.krestsdk.auth.credentials;

import android.os.AsyncTask;
import com.microsoft.kapp.Callback;
import com.microsoft.krestsdk.auth.KdsFetcher;
import com.microsoft.krestsdk.auth.ServiceInfo;
import java.io.IOException;
import java.net.URISyntaxException;
import org.json.JSONException;
/* loaded from: classes.dex */
public class KdsRetrieverAsync extends AsyncTask<Callback<ServiceInfo>, Void, ServiceInfo> {
    private String mAccessToken;
    private String mAuthUrl;
    private Exception mException;
    private KdsFetcher mKdsFetcher;
    private Callback<ServiceInfo>[] mListeners;

    public KdsRetrieverAsync(KdsFetcher mKdsFetcher, String mAccessToken, String mAuthUrl) {
        this.mKdsFetcher = mKdsFetcher;
        this.mAccessToken = mAccessToken;
        this.mAuthUrl = mAuthUrl;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public ServiceInfo doInBackground(Callback<ServiceInfo>... params) {
        this.mListeners = params;
        try {
            ServiceInfo serviceInfo = this.mKdsFetcher.getServiceInfo(this.mAuthUrl, this.mAccessToken);
            return serviceInfo;
        } catch (IOException e) {
            this.mException = e;
            return null;
        } catch (URISyntaxException e2) {
            this.mException = e2;
            return null;
        } catch (JSONException e3) {
            this.mException = e3;
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(ServiceInfo result) {
        if (this.mListeners != null) {
            Callback<ServiceInfo>[] arr$ = this.mListeners;
            for (Callback<ServiceInfo> listener : arr$) {
                if (listener != null) {
                    if (this.mException == null) {
                        listener.callback(result);
                    } else {
                        listener.onError(this.mException);
                    }
                }
            }
        }
    }
}

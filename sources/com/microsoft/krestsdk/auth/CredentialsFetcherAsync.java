package com.microsoft.krestsdk.auth;

import android.os.AsyncTask;
import com.microsoft.kapp.Callback;
import com.microsoft.krestsdk.auth.credentials.CredentialsManager;
import com.microsoft.krestsdk.auth.credentials.KCredential;
/* loaded from: classes.dex */
public class CredentialsFetcherAsync extends AsyncTask<Callback<KCredential>, Void, KCredential> {
    private CredentialsManager mCredentialsManager;
    private Callback<KCredential>[] mListeners;

    public CredentialsFetcherAsync(CredentialsManager credentialsManager) {
        this.mCredentialsManager = credentialsManager;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public KCredential doInBackground(Callback<KCredential>... listeners) {
        this.mListeners = listeners;
        if (this.mCredentialsManager == null) {
            return null;
        }
        return this.mCredentialsManager.getCredentials();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(KCredential result) {
        if (this.mListeners != null) {
            Callback<KCredential>[] arr$ = this.mListeners;
            for (Callback<KCredential> listener : arr$) {
                if (listener != null) {
                    listener.callback(result);
                }
            }
        }
    }
}

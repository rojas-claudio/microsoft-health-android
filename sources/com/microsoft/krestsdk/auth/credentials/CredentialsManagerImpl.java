package com.microsoft.krestsdk.auth.credentials;

import android.content.Context;
import android.content.Intent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.facebook.internal.Validate;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.SettingsUtils;
import com.microsoft.krestsdk.auth.KdsAuth;
import com.microsoft.krestsdk.auth.MsaAuth;
import com.microsoft.krestsdk.services.RestService;
/* loaded from: classes.dex */
public class CredentialsManagerImpl implements CredentialsManager {
    private static final String TAG = CredentialsManagerImpl.class.getSimpleName();
    private volatile KCredential mCacheCredential;
    private Context mContext;
    private CredentialStore mCredentialStore;
    private KdsAuth mKdsAuth;
    private MsaAuth mMsaAuth;
    private SettingsProvider mSettingsProvider;

    public CredentialsManagerImpl(Context context, SettingsProvider settingsProvider, CredentialStore credentialStore, MsaAuth msaAuth, KdsAuth kdsAuth) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Validate.notNull(settingsProvider, "settringProvider");
        Validate.notNull(credentialStore, "credentialStore");
        this.mContext = context;
        this.mSettingsProvider = settingsProvider;
        this.mCredentialStore = credentialStore;
        this.mMsaAuth = msaAuth;
        this.mKdsAuth = kdsAuth;
        this.mCacheCredential = credentialStore.getCredentials();
    }

    @Override // com.microsoft.krestsdk.auth.credentials.CredentialsManager
    public void logoutUser(Context context, Callback<Void> callback) {
        this.mCredentialStore.deleteCredentials();
        CookieSyncManager.createInstance(context);
        CookieManager.getInstance().removeAllCookie();
        if (callback != null) {
            callback.callback(null);
        }
    }

    @Override // com.microsoft.krestsdk.auth.credentials.CredentialsManager
    public synchronized KCredential getCredentials() {
        SettingsUtils.throwExceptionIfOnMainThread();
        if (this.mCacheCredential == null || (this.mCacheCredential != null && this.mCacheCredential.isTokenExpired())) {
            KCredential refreshedCredentials = refreshCredential(this.mCacheCredential);
            if (refreshedCredentials != null) {
                setCredentialsNonUithread(refreshedCredentials);
            } else {
                KLog.i(TAG, "Couldn't refresh the credentials, redirecting to sign in page");
                Intent signinIntent = new Intent(RestService.SIGN_IN_REQUIRED_INTENT);
                this.mContext.sendBroadcast(signinIntent);
            }
        }
        return this.mCacheCredential;
    }

    @Override // com.microsoft.krestsdk.auth.credentials.CredentialsManager
    public void setCredentials(final KCredential credential) {
        new Thread(new Runnable() { // from class: com.microsoft.krestsdk.auth.credentials.CredentialsManagerImpl.1
            @Override // java.lang.Runnable
            public void run() {
                CredentialsManagerImpl.this.setCredentialsNonUithread(credential);
            }
        }).start();
    }

    @Override // com.microsoft.krestsdk.auth.credentials.CredentialsManager
    public void setCredentialsNonUithread(KCredential credential) {
        SettingsUtils.throwExceptionIfOnMainThread();
        this.mCredentialStore.setCredentials(credential);
        if (credential != null && credential.getAccountMetadata() != null) {
            Telemetry.setUserId(credential.getAccountMetadata().getUserId());
        }
        this.mCacheCredential = credential;
    }

    @Override // com.microsoft.krestsdk.auth.credentials.CredentialsManager
    public void deleteCredentials(Context context) {
        if (context != null) {
            this.mCacheCredential = null;
            CookieSyncManager.createInstance(context);
            CookieManager.getInstance().removeAllCookie();
            new Thread(new Runnable() { // from class: com.microsoft.krestsdk.auth.credentials.CredentialsManagerImpl.2
                @Override // java.lang.Runnable
                public void run() {
                    CredentialsManagerImpl.this.mCredentialStore.deleteCredentials();
                }
            }).start();
        }
    }

    @Override // com.microsoft.krestsdk.auth.credentials.CredentialsManager
    public AccountMetadata getAccountMetada() {
        if (this.mCacheCredential == null) {
            return null;
        }
        return this.mCacheCredential.getAccountMetadata();
    }

    private KCredential refreshCredential(KCredential oldCredential) {
        SettingsUtils.throwExceptionIfOnMainThread();
        if (oldCredential == null) {
            KLog.i(TAG, "Couldn't refresh the credentials, previous credentials are null");
            return null;
        } else if (!this.mSettingsProvider.isTokenRefreshEnabled()) {
            KLog.e(TAG, "Couldn't refresh the credentials, token refresh is disabled");
            return oldCredential;
        } else {
            MsaCredential msaCredential = this.mMsaAuth.refreshAccessToken(oldCredential.getMsaCredential());
            KdsCredential kdsCredential = this.mKdsAuth.refreshAccessToken(msaCredential);
            KCredential kCredential = null;
            if (msaCredential != null && kdsCredential != null) {
                kCredential = new KCredential(msaCredential, kdsCredential);
            } else {
                KLog.i(TAG, "Couldn't refresh the credentials");
            }
            return kCredential;
        }
    }
}

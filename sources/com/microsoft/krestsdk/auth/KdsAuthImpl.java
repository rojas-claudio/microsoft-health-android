package com.microsoft.krestsdk.auth;

import android.os.AsyncTask;
import com.microsoft.band.CargoConstants;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.CloudEnvironment;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.krestsdk.auth.credentials.KdsCredential;
import com.microsoft.krestsdk.auth.credentials.KdsRetrieverAsync;
import com.microsoft.krestsdk.auth.credentials.MsaCredential;
/* loaded from: classes.dex */
public class KdsAuthImpl implements KdsAuth {
    private static final String TAG = KdsAuthImpl.class.getSimpleName();
    private KdsFetcher mKdsFetcher;
    private SettingsProvider mSettingsProvider;

    public KdsAuthImpl(KdsFetcher kdsFetcher, SettingsProvider settingsProvider) {
        this.mKdsFetcher = kdsFetcher;
        this.mSettingsProvider = settingsProvider;
    }

    @Override // com.microsoft.krestsdk.auth.KdsAuth
    public KdsCredential refreshAccessToken(MsaCredential msaCredential) {
        if (msaCredential == null) {
            return null;
        }
        String authUrl = this.mSettingsProvider.getAuthUrl();
        KdsRetrieverAsync query = new KdsRetrieverAsync(this.mKdsFetcher, msaCredential.getAccessToken(), authUrl);
        ServiceInfo serviceInfo = null;
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
        try {
            serviceInfo = this.mKdsFetcher.getServiceInfo(authUrl, msaCredential.getAccessToken());
        } catch (Exception e) {
            KLog.e(TAG, "Exception trying to get the KDS token", e);
        }
        if (serviceInfo == null) {
            return null;
        }
        String accessToken = serviceInfo.AccessToken;
        String endPoint = serviceInfo.PodAddress;
        String userId = serviceInfo.UserId;
        String loginUrl = getLoginUrl(this.mSettingsProvider);
        String fusEndPoint = serviceInfo.FUSEndpoint;
        String hnfEndPoint = serviceInfo.AuthedHnFEndPoint;
        String hnfQueryParameters = serviceInfo.AuthedHnFQueryParameters;
        String profileCreation = serviceInfo.ProfileCreatedDate;
        KdsCredential kdsCredential = new KdsCredential(accessToken, endPoint, authUrl, userId, loginUrl, fusEndPoint, hnfEndPoint, hnfQueryParameters, profileCreation);
        return kdsCredential;
    }

    private String getLoginUrl(SettingsProvider settingsProvider) {
        CloudEnvironment cloudEnvironment = settingsProvider.getEnvironment();
        return String.format(CargoConstants.OAUTH_LOGIN_URL, cloudEnvironment.getRealm());
    }
}

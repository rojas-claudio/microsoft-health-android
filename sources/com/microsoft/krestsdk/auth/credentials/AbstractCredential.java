package com.microsoft.krestsdk.auth.credentials;

import org.joda.time.DateTime;
/* loaded from: classes.dex */
abstract class AbstractCredential implements BaseAuthCredential {
    private String mAccessToken;

    public abstract DateTime getTokenExpiration();

    public AbstractCredential(String accessToken) {
        this.mAccessToken = accessToken;
    }

    @Override // com.microsoft.krestsdk.auth.credentials.BaseAuthCredential
    public String getAccessToken() {
        return this.mAccessToken;
    }

    @Override // com.microsoft.krestsdk.auth.credentials.BaseAuthCredential
    public boolean isTokenExpired() {
        DateTime expiration = getTokenExpiration();
        return expiration == null || expiration.isBeforeNow();
    }
}

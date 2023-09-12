package com.microsoft.krestsdk.auth.credentials;

import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class MsaCredential extends AbstractCredential {
    private DateTime mExpiration;
    private String mRefreshToken;

    @Override // com.microsoft.krestsdk.auth.credentials.AbstractCredential, com.microsoft.krestsdk.auth.credentials.BaseAuthCredential
    public /* bridge */ /* synthetic */ String getAccessToken() {
        return super.getAccessToken();
    }

    @Override // com.microsoft.krestsdk.auth.credentials.AbstractCredential, com.microsoft.krestsdk.auth.credentials.BaseAuthCredential
    public /* bridge */ /* synthetic */ boolean isTokenExpired() {
        return super.isTokenExpired();
    }

    public MsaCredential(String accessToken, String refreshToken, DateTime expiresIn) {
        super(accessToken);
        this.mRefreshToken = refreshToken;
        this.mExpiration = expiresIn;
    }

    public String getRefreshToken() {
        return this.mRefreshToken;
    }

    @Override // com.microsoft.krestsdk.auth.credentials.AbstractCredential
    public DateTime getTokenExpiration() {
        return this.mExpiration;
    }
}

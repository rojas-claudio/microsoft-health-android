package com.microsoft.krestsdk.auth.credentials;
/* loaded from: classes.dex */
interface BaseAuthCredential {
    String getAccessToken();

    boolean isTokenExpired();
}

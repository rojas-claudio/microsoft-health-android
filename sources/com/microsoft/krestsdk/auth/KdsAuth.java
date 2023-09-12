package com.microsoft.krestsdk.auth;

import com.microsoft.krestsdk.auth.credentials.KdsCredential;
import com.microsoft.krestsdk.auth.credentials.MsaCredential;
/* loaded from: classes.dex */
public interface KdsAuth {
    KdsCredential refreshAccessToken(MsaCredential msaCredential);
}

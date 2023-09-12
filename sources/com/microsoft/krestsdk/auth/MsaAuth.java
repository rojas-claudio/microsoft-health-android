package com.microsoft.krestsdk.auth;

import com.google.gson.JsonObject;
import com.microsoft.kapp.Callback;
import com.microsoft.krestsdk.auth.credentials.MsaCredential;
/* loaded from: classes.dex */
public interface MsaAuth {
    void getUserProfile(String str, Callback<JsonObject> callback);

    MsaCredential refreshAccessToken(MsaCredential msaCredential);
}

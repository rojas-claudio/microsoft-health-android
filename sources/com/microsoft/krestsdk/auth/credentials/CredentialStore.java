package com.microsoft.krestsdk.auth.credentials;
/* loaded from: classes.dex */
public interface CredentialStore {
    void deleteCredentials();

    KCredential getCredentials();

    void setCredentials(KCredential kCredential);
}

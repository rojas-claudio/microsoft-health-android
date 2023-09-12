package com.microsoft.krestsdk.auth.credentials;

import android.content.Context;
import com.microsoft.kapp.Callback;
/* loaded from: classes.dex */
public interface CredentialsManager {
    void deleteCredentials(Context context);

    AccountMetadata getAccountMetada();

    KCredential getCredentials();

    void logoutUser(Context context, Callback<Void> callback);

    void setCredentials(KCredential kCredential);

    void setCredentialsNonUithread(KCredential kCredential);
}

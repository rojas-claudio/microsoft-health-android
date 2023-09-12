package com.google.android.gms.auth;

import android.content.Intent;
/* loaded from: classes.dex */
public class GooglePlayServicesAvailabilityException extends UserRecoverableAuthException {
    private final int iL;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GooglePlayServicesAvailabilityException(int connectionStatusCode, String msg, Intent intent) {
        super(msg, intent);
        this.iL = connectionStatusCode;
    }

    public int getConnectionStatusCode() {
        return this.iL;
    }
}

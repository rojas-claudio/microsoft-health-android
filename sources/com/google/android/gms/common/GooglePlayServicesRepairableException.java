package com.google.android.gms.common;

import android.content.Intent;
/* loaded from: classes.dex */
public class GooglePlayServicesRepairableException extends UserRecoverableException {
    private final int iL;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GooglePlayServicesRepairableException(int connectionStatusCode, String msg, Intent intent) {
        super(msg, intent);
        this.iL = connectionStatusCode;
    }

    public int getConnectionStatusCode() {
        return this.iL;
    }
}

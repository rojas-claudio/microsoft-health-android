package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.internal.by;
import com.google.android.gms.internal.de;
/* loaded from: classes.dex */
public class bt extends de<by> {
    private final int gz;

    public bt(Context context, GooglePlayServicesClient.ConnectionCallbacks connectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener onConnectionFailedListener, int i) {
        super(context, connectionCallbacks, onConnectionFailedListener, new String[0]);
        this.gz = i;
    }

    @Override // com.google.android.gms.internal.de
    protected void a(dj djVar, de.d dVar) throws RemoteException {
        djVar.g(dVar, this.gz, getContext().getPackageName(), new Bundle());
    }

    @Override // com.google.android.gms.internal.de
    protected String ag() {
        return "com.google.android.gms.ads.service.START";
    }

    @Override // com.google.android.gms.internal.de
    protected String ah() {
        return "com.google.android.gms.ads.internal.request.IAdRequestService";
    }

    public by ai() {
        return (by) super.bd();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.de
    /* renamed from: o */
    public by p(IBinder iBinder) {
        return by.a.q(iBinder);
    }
}

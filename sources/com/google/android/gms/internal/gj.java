package com.google.android.gms.internal;

import android.accounts.Account;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.de;
import com.google.android.gms.internal.gh;
import com.google.android.gms.internal.gi;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.NotifyTransactionStatusRequest;
import com.google.android.gms.wallet.WalletConstants;
/* loaded from: classes.dex */
public class gj extends de<gh> {
    private final Activity fD;
    private final String it;
    private final int mTheme;
    private final int us;

    /* loaded from: classes.dex */
    final class a extends gi.a {
        private final int ky;

        public a(int i) {
            this.ky = i;
        }

        @Override // com.google.android.gms.internal.gi
        public void a(int i, FullWallet fullWallet, Bundle bundle) {
            int i2;
            Intent intent;
            ConnectionResult connectionResult = new ConnectionResult(i, bundle != null ? (PendingIntent) bundle.getParcelable("com.google.android.gms.wallet.EXTRA_PENDING_INTENT") : null);
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(gj.this.fD, this.ky);
                    return;
                } catch (IntentSender.SendIntentException e) {
                    Log.w("WalletClientImpl", "Exception starting pending intent", e);
                    return;
                }
            }
            if (connectionResult.isSuccess()) {
                i2 = -1;
                intent = new Intent();
                intent.putExtra(WalletConstants.EXTRA_FULL_WALLET, fullWallet);
            } else {
                i2 = i == 408 ? 0 : 1;
                intent = new Intent();
                intent.putExtra(WalletConstants.EXTRA_ERROR_CODE, i);
            }
            try {
                gj.this.fD.createPendingResult(this.ky, intent, 1073741824).send(i2);
            } catch (PendingIntent.CanceledException e2) {
                Log.w("WalletClientImpl", "Exception setting pending result", e2);
            }
        }

        @Override // com.google.android.gms.internal.gi
        public void a(int i, MaskedWallet maskedWallet, Bundle bundle) {
            int i2;
            Intent intent;
            ConnectionResult connectionResult = new ConnectionResult(i, bundle != null ? (PendingIntent) bundle.getParcelable("com.google.android.gms.wallet.EXTRA_PENDING_INTENT") : null);
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(gj.this.fD, this.ky);
                    return;
                } catch (IntentSender.SendIntentException e) {
                    Log.w("WalletClientImpl", "Exception starting pending intent", e);
                    return;
                }
            }
            if (connectionResult.isSuccess()) {
                i2 = -1;
                intent = new Intent();
                intent.putExtra(WalletConstants.EXTRA_MASKED_WALLET, maskedWallet);
            } else {
                i2 = i == 408 ? 0 : 1;
                intent = new Intent();
                intent.putExtra(WalletConstants.EXTRA_ERROR_CODE, i);
            }
            try {
                gj.this.fD.createPendingResult(this.ky, intent, 1073741824).send(i2);
            } catch (PendingIntent.CanceledException e2) {
                Log.w("WalletClientImpl", "Exception setting pending result", e2);
            }
        }

        @Override // com.google.android.gms.internal.gi
        public void a(int i, boolean z, Bundle bundle) {
            Intent intent = new Intent();
            intent.putExtra(WalletConstants.EXTRA_IS_USER_PREAUTHORIZED, z);
            try {
                gj.this.fD.createPendingResult(this.ky, intent, 1073741824).send(-1);
            } catch (PendingIntent.CanceledException e) {
                Log.w("WalletClientImpl", "Exception setting pending result", e);
            }
        }
    }

    public gj(Activity activity, GooglePlayServicesClient.ConnectionCallbacks connectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener onConnectionFailedListener, int i, String str, int i2) {
        super(activity, connectionCallbacks, onConnectionFailedListener, new String[0]);
        this.fD = activity;
        this.us = i;
        this.it = str;
        this.mTheme = i2;
    }

    private Bundle eb() {
        Bundle bundle = new Bundle();
        bundle.putInt("com.google.android.gms.wallet.EXTRA_ENVIRONMENT", this.us);
        bundle.putString("androidPackageName", this.fD.getPackageName());
        if (!TextUtils.isEmpty(this.it)) {
            bundle.putParcelable("com.google.android.gms.wallet.EXTRA_BUYER_ACCOUNT", new Account(this.it, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE));
        }
        bundle.putInt("com.google.android.gms.wallet.EXTRA_THEME", this.mTheme);
        return bundle;
    }

    @Override // com.google.android.gms.internal.de
    protected void a(dj djVar, de.d dVar) throws RemoteException {
        djVar.a(dVar, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE);
    }

    @Override // com.google.android.gms.internal.de
    protected String ag() {
        return "com.google.android.gms.wallet.service.BIND";
    }

    @Override // com.google.android.gms.internal.de
    protected String ah() {
        return "com.google.android.gms.wallet.internal.IOwService";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.de
    /* renamed from: au */
    public gh p(IBinder iBinder) {
        return gh.a.as(iBinder);
    }

    public void changeMaskedWallet(String googleTransactionId, String merchantTransactionId, int requestCode) {
        Bundle eb = eb();
        a aVar = new a(requestCode);
        try {
            bd().a(googleTransactionId, merchantTransactionId, eb, aVar);
        } catch (RemoteException e) {
            Log.e("WalletClientImpl", "RemoteException changing masked wallet", e);
            aVar.a(8, (MaskedWallet) null, (Bundle) null);
        }
    }

    public void checkForPreAuthorization(int requestCode) {
        Bundle eb = eb();
        a aVar = new a(requestCode);
        try {
            bd().a(eb, aVar);
        } catch (RemoteException e) {
            Log.e("WalletClientImpl", "RemoteException during checkForPreAuthorization", e);
            aVar.a(8, false, (Bundle) null);
        }
    }

    public void loadFullWallet(FullWalletRequest request, int requestCode) {
        a aVar = new a(requestCode);
        try {
            bd().a(request, eb(), aVar);
        } catch (RemoteException e) {
            Log.e("WalletClientImpl", "RemoteException getting full wallet", e);
            aVar.a(8, (FullWallet) null, (Bundle) null);
        }
    }

    public void loadMaskedWallet(MaskedWalletRequest request, int requestCode) {
        Bundle eb = eb();
        a aVar = new a(requestCode);
        try {
            bd().a(request, eb, aVar);
        } catch (RemoteException e) {
            Log.e("WalletClientImpl", "RemoteException getting masked wallet", e);
            aVar.a(8, (MaskedWallet) null, (Bundle) null);
        }
    }

    public void notifyTransactionStatus(NotifyTransactionStatusRequest request) {
        try {
            bd().a(request, eb());
        } catch (RemoteException e) {
        }
    }
}

package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.de;
import com.google.android.gms.internal.ew;
import com.google.android.gms.internal.ex;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationStatusCodes;
import java.util.List;
/* loaded from: classes.dex */
public class ez extends de<ex> {
    private final fc<ex> oO;
    private final ey oU;
    private final String oV;

    /* loaded from: classes.dex */
    private final class a extends de<ex>.b<LocationClient.OnAddGeofencesResultListener> {
        private final int iC;
        private final String[] oW;

        public a(LocationClient.OnAddGeofencesResultListener onAddGeofencesResultListener, int i, String[] strArr) {
            super(onAddGeofencesResultListener);
            this.iC = LocationStatusCodes.Z(i);
            this.oW = strArr;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.de.b
        public void a(LocationClient.OnAddGeofencesResultListener onAddGeofencesResultListener) {
            if (onAddGeofencesResultListener != null) {
                onAddGeofencesResultListener.onAddGeofencesResult(this.iC, this.oW);
            }
        }

        @Override // com.google.android.gms.internal.de.b
        protected void aF() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class b extends ew.a {
        private LocationClient.OnAddGeofencesResultListener oY;
        private LocationClient.OnRemoveGeofencesResultListener oZ;
        private ez pa;

        public b(LocationClient.OnAddGeofencesResultListener onAddGeofencesResultListener, ez ezVar) {
            this.oY = onAddGeofencesResultListener;
            this.oZ = null;
            this.pa = ezVar;
        }

        public b(LocationClient.OnRemoveGeofencesResultListener onRemoveGeofencesResultListener, ez ezVar) {
            this.oZ = onRemoveGeofencesResultListener;
            this.oY = null;
            this.pa = ezVar;
        }

        @Override // com.google.android.gms.internal.ew
        public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) throws RemoteException {
            if (this.pa == null) {
                Log.wtf("LocationClientImpl", "onAddGeofenceResult called multiple times");
                return;
            }
            ez ezVar = this.pa;
            ez ezVar2 = this.pa;
            ezVar2.getClass();
            ezVar.a(new a(this.oY, statusCode, geofenceRequestIds));
            this.pa = null;
            this.oY = null;
            this.oZ = null;
        }

        @Override // com.google.android.gms.internal.ew
        public void onRemoveGeofencesByPendingIntentResult(int statusCode, PendingIntent pendingIntent) {
            if (this.pa == null) {
                Log.wtf("LocationClientImpl", "onRemoveGeofencesByPendingIntentResult called multiple times");
                return;
            }
            ez ezVar = this.pa;
            ez ezVar2 = this.pa;
            ezVar2.getClass();
            ezVar.a(new d(1, this.oZ, statusCode, pendingIntent));
            this.pa = null;
            this.oY = null;
            this.oZ = null;
        }

        @Override // com.google.android.gms.internal.ew
        public void onRemoveGeofencesByRequestIdsResult(int statusCode, String[] geofenceRequestIds) {
            if (this.pa == null) {
                Log.wtf("LocationClientImpl", "onRemoveGeofencesByRequestIdsResult called multiple times");
                return;
            }
            ez ezVar = this.pa;
            ez ezVar2 = this.pa;
            ezVar2.getClass();
            ezVar.a(new d(2, this.oZ, statusCode, geofenceRequestIds));
            this.pa = null;
            this.oY = null;
            this.oZ = null;
        }
    }

    /* loaded from: classes.dex */
    private final class c implements fc<ex> {
        private c() {
        }

        @Override // com.google.android.gms.internal.fc
        public void bc() {
            ez.this.bc();
        }

        @Override // com.google.android.gms.internal.fc
        /* renamed from: cn */
        public ex bd() {
            return (ex) ez.this.bd();
        }
    }

    /* loaded from: classes.dex */
    private final class d extends de<ex>.b<LocationClient.OnRemoveGeofencesResultListener> {
        private final int iC;
        private final PendingIntent mPendingIntent;
        private final String[] oW;
        private final int pb;

        public d(int i, LocationClient.OnRemoveGeofencesResultListener onRemoveGeofencesResultListener, int i2, PendingIntent pendingIntent) {
            super(onRemoveGeofencesResultListener);
            db.k(i == 1);
            this.pb = i;
            this.iC = LocationStatusCodes.Z(i2);
            this.mPendingIntent = pendingIntent;
            this.oW = null;
        }

        public d(int i, LocationClient.OnRemoveGeofencesResultListener onRemoveGeofencesResultListener, int i2, String[] strArr) {
            super(onRemoveGeofencesResultListener);
            db.k(i == 2);
            this.pb = i;
            this.iC = LocationStatusCodes.Z(i2);
            this.oW = strArr;
            this.mPendingIntent = null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.de.b
        public void a(LocationClient.OnRemoveGeofencesResultListener onRemoveGeofencesResultListener) {
            if (onRemoveGeofencesResultListener != null) {
                switch (this.pb) {
                    case 1:
                        onRemoveGeofencesResultListener.onRemoveGeofencesByPendingIntentResult(this.iC, this.mPendingIntent);
                        return;
                    case 2:
                        onRemoveGeofencesResultListener.onRemoveGeofencesByRequestIdsResult(this.iC, this.oW);
                        return;
                    default:
                        Log.wtf("LocationClientImpl", "Unsupported action: " + this.pb);
                        return;
                }
            }
        }

        @Override // com.google.android.gms.internal.de.b
        protected void aF() {
        }
    }

    public ez(Context context, GooglePlayServicesClient.ConnectionCallbacks connectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener onConnectionFailedListener, String str) {
        super(context, connectionCallbacks, onConnectionFailedListener, new String[0]);
        this.oO = new c();
        this.oU = new ey(context, this.oO);
        this.oV = str;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.de
    /* renamed from: G */
    public ex p(IBinder iBinder) {
        return ex.a.F(iBinder);
    }

    @Override // com.google.android.gms.internal.de
    protected void a(dj djVar, de.d dVar) throws RemoteException {
        Bundle bundle = new Bundle();
        bundle.putString("client_name", this.oV);
        djVar.e(dVar, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, getContext().getPackageName(), bundle);
    }

    public void addGeofences(List<fa> geofences, PendingIntent pendingIntent, LocationClient.OnAddGeofencesResultListener listener) {
        b bVar;
        bc();
        dm.b(geofences != null && geofences.size() > 0, "At least one geofence must be specified.");
        dm.a(pendingIntent, "PendingIntent must be specified.");
        dm.a(listener, "OnAddGeofencesResultListener not provided.");
        if (listener == null) {
            bVar = null;
        } else {
            try {
                bVar = new b(listener, this);
            } catch (RemoteException e) {
                throw new IllegalStateException(e);
            }
        }
        bd().a(geofences, pendingIntent, bVar, getContext().getPackageName());
    }

    @Override // com.google.android.gms.internal.de
    protected String ag() {
        return "com.google.android.location.internal.GoogleLocationManagerService.START";
    }

    @Override // com.google.android.gms.internal.de
    protected String ah() {
        return "com.google.android.gms.location.internal.IGoogleLocationManagerService";
    }

    @Override // com.google.android.gms.internal.de, com.google.android.gms.common.GooglePlayServicesClient
    public void disconnect() {
        synchronized (this.oU) {
            if (isConnected()) {
                this.oU.removeAllListeners();
                this.oU.cm();
            }
            super.disconnect();
        }
    }

    public Location getLastLocation() {
        return this.oU.getLastLocation();
    }

    public void removeActivityUpdates(PendingIntent callbackIntent) {
        bc();
        dm.e(callbackIntent);
        try {
            bd().removeActivityUpdates(callbackIntent);
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    public void removeGeofences(PendingIntent pendingIntent, LocationClient.OnRemoveGeofencesResultListener listener) {
        b bVar;
        bc();
        dm.a(pendingIntent, "PendingIntent must be specified.");
        dm.a(listener, "OnRemoveGeofencesResultListener not provided.");
        if (listener == null) {
            bVar = null;
        } else {
            try {
                bVar = new b(listener, this);
            } catch (RemoteException e) {
                throw new IllegalStateException(e);
            }
        }
        bd().a(pendingIntent, bVar, getContext().getPackageName());
    }

    public void removeGeofences(List<String> geofenceRequestIds, LocationClient.OnRemoveGeofencesResultListener listener) {
        b bVar;
        bc();
        dm.b(geofenceRequestIds != null && geofenceRequestIds.size() > 0, "geofenceRequestIds can't be null nor empty.");
        dm.a(listener, "OnRemoveGeofencesResultListener not provided.");
        String[] strArr = (String[]) geofenceRequestIds.toArray(new String[0]);
        if (listener == null) {
            bVar = null;
        } else {
            try {
                bVar = new b(listener, this);
            } catch (RemoteException e) {
                throw new IllegalStateException(e);
            }
        }
        bd().a(strArr, bVar, getContext().getPackageName());
    }

    public void removeLocationUpdates(PendingIntent callbackIntent) {
        this.oU.removeLocationUpdates(callbackIntent);
    }

    public void removeLocationUpdates(LocationListener listener) {
        this.oU.removeLocationUpdates(listener);
    }

    public void requestActivityUpdates(long detectionIntervalMillis, PendingIntent callbackIntent) {
        bc();
        dm.e(callbackIntent);
        dm.b(detectionIntervalMillis >= 0, "detectionIntervalMillis must be >= 0");
        try {
            bd().a(detectionIntervalMillis, true, callbackIntent);
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    public void requestLocationUpdates(LocationRequest request, PendingIntent callbackIntent) {
        this.oU.requestLocationUpdates(request, callbackIntent);
    }

    public void requestLocationUpdates(LocationRequest request, LocationListener listener) {
        requestLocationUpdates(request, listener, null);
    }

    public void requestLocationUpdates(LocationRequest request, LocationListener listener, Looper looper) {
        synchronized (this.oU) {
            this.oU.requestLocationUpdates(request, listener, looper);
        }
    }

    public void setMockLocation(Location mockLocation) {
        this.oU.setMockLocation(mockLocation);
    }

    public void setMockMode(boolean isMockMode) {
        this.oU.setMockMode(isMockMode);
    }
}

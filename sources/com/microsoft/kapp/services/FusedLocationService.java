package com.microsoft.kapp.services;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.microsoft.kapp.logging.KLog;
/* loaded from: classes.dex */
public class FusedLocationService implements LocationService, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {
    private static final String TAG = FusedLocationService.class.getSimpleName();
    private LocationCallbacks mCallbacks;
    private LocationClient mLocationClient;

    @Override // com.microsoft.kapp.services.LocationService
    public boolean isLocationServiceAvailable(Context context) {
        int googlePlayCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        return googlePlayCode == 0;
    }

    @Override // com.microsoft.kapp.services.LocationService
    public void getCurrentLocation(Context context, LocationCallbacks callbacks) {
        this.mCallbacks = callbacks;
        this.mLocationClient = new LocationClient(context, this, this);
        this.mLocationClient.connect();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener
    public void onConnectionFailed(ConnectionResult connectionResult) {
        this.mCallbacks.onError();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks
    public void onConnected(Bundle dataBundle) {
        Location location = null;
        try {
            location = this.mLocationClient.getLastLocation();
        } catch (Exception e) {
            KLog.w(TAG, "Getting location failed!");
        } finally {
            this.mLocationClient.disconnect();
        }
        if (location == null) {
            this.mCallbacks.onError();
            KLog.w(TAG, "Location is not available.");
            return;
        }
        this.mCallbacks.onLocationFound(location);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks
    public void onDisconnected() {
    }
}

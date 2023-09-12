package com.microsoft.kapp.models;
/* loaded from: classes.dex */
public class GeoCoordinate {
    private double mLatitude;
    private double mLongitude;
    private double mScaledPace;

    public GeoCoordinate(double longitude, double latitude, double scaledPace) {
        this.mLongitude = longitude;
        this.mLatitude = latitude;
        this.mScaledPace = scaledPace;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public double getLongitude() {
        return this.mLongitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLatitude() {
        return this.mLatitude;
    }

    public double getScaledPace() {
        return this.mScaledPace;
    }

    public void setScaledPace(double scaledPace) {
        this.mScaledPace = scaledPace;
    }
}

package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.r;
/* loaded from: classes.dex */
public final class LatLng implements SafeParcelable {
    public static final LatLngCreator CREATOR = new LatLngCreator();
    private final int iM;
    public final double latitude;
    public final double longitude;

    public LatLng(double latitude, double longitude) {
        this(1, latitude, longitude);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LatLng(int versionCode, double latitude, double longitude) {
        this.iM = versionCode;
        if (-180.0d > longitude || longitude >= 180.0d) {
            this.longitude = ((((longitude - 180.0d) % 360.0d) + 360.0d) % 360.0d) - 180.0d;
        } else {
            this.longitude = longitude;
        }
        this.latitude = Math.max(-90.0d, Math.min(90.0d, latitude));
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof LatLng) {
            LatLng latLng = (LatLng) o;
            return Double.doubleToLongBits(this.latitude) == Double.doubleToLongBits(latLng.latitude) && Double.doubleToLongBits(this.longitude) == Double.doubleToLongBits(latLng.longitude);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getVersionCode() {
        return this.iM;
    }

    public int hashCode() {
        long doubleToLongBits = Double.doubleToLongBits(this.latitude);
        long doubleToLongBits2 = Double.doubleToLongBits(this.longitude);
        return ((((int) (doubleToLongBits ^ (doubleToLongBits >>> 32))) + 31) * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)));
    }

    public String toString() {
        return "lat/lng: (" + this.latitude + "," + this.longitude + ")";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        if (r.cK()) {
            e.a(this, out, flags);
        } else {
            LatLngCreator.a(this, out, flags);
        }
    }
}

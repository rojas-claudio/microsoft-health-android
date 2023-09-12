package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.dm;
import com.google.android.gms.maps.internal.r;
/* loaded from: classes.dex */
public final class LatLngBounds implements SafeParcelable {
    public static final LatLngBoundsCreator CREATOR = new LatLngBoundsCreator();
    private final int iM;
    public final LatLng northeast;
    public final LatLng southwest;

    /* loaded from: classes.dex */
    public static final class Builder {
        private double qv = Double.POSITIVE_INFINITY;
        private double qw = Double.NEGATIVE_INFINITY;
        private double qx = Double.NaN;
        private double qy = Double.NaN;

        private boolean b(double d) {
            boolean z = false;
            if (this.qx <= this.qy) {
                return this.qx <= d && d <= this.qy;
            }
            if (this.qx <= d || d <= this.qy) {
                z = true;
            }
            return z;
        }

        public LatLngBounds build() {
            dm.a(!Double.isNaN(this.qx), "no included points");
            return new LatLngBounds(new LatLng(this.qv, this.qx), new LatLng(this.qw, this.qy));
        }

        public Builder include(LatLng point) {
            this.qv = Math.min(this.qv, point.latitude);
            this.qw = Math.max(this.qw, point.latitude);
            double d = point.longitude;
            if (Double.isNaN(this.qx)) {
                this.qx = d;
                this.qy = d;
            } else if (!b(d)) {
                if (LatLngBounds.b(this.qx, d) < LatLngBounds.c(this.qy, d)) {
                    this.qx = d;
                } else {
                    this.qy = d;
                }
            }
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LatLngBounds(int versionCode, LatLng southwest, LatLng northeast) {
        dm.a(southwest, "null southwest");
        dm.a(northeast, "null northeast");
        dm.a(northeast.latitude >= southwest.latitude, "southern latitude exceeds northern latitude (%s > %s)", Double.valueOf(southwest.latitude), Double.valueOf(northeast.latitude));
        this.iM = versionCode;
        this.southwest = southwest;
        this.northeast = northeast;
    }

    public LatLngBounds(LatLng southwest, LatLng northeast) {
        this(1, southwest, northeast);
    }

    private boolean a(double d) {
        return this.southwest.latitude <= d && d <= this.northeast.latitude;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double b(double d, double d2) {
        return ((d - d2) + 360.0d) % 360.0d;
    }

    private boolean b(double d) {
        boolean z = false;
        if (this.southwest.longitude <= this.northeast.longitude) {
            return this.southwest.longitude <= d && d <= this.northeast.longitude;
        }
        if (this.southwest.longitude <= d || d <= this.northeast.longitude) {
            z = true;
        }
        return z;
    }

    public static Builder builder() {
        return new Builder();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double c(double d, double d2) {
        return ((d2 - d) + 360.0d) % 360.0d;
    }

    public boolean contains(LatLng point) {
        return a(point.latitude) && b(point.longitude);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof LatLngBounds) {
            LatLngBounds latLngBounds = (LatLngBounds) o;
            return this.southwest.equals(latLngBounds.southwest) && this.northeast.equals(latLngBounds.northeast);
        }
        return false;
    }

    public LatLng getCenter() {
        double d = (this.southwest.latitude + this.northeast.latitude) / 2.0d;
        double d2 = this.northeast.longitude;
        double d3 = this.southwest.longitude;
        return new LatLng(d, d3 <= d2 ? (d2 + d3) / 2.0d : ((d2 + 360.0d) + d3) / 2.0d);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getVersionCode() {
        return this.iM;
    }

    public int hashCode() {
        return dl.hashCode(this.southwest, this.northeast);
    }

    public LatLngBounds including(LatLng point) {
        double d;
        double min = Math.min(this.southwest.latitude, point.latitude);
        double max = Math.max(this.northeast.latitude, point.latitude);
        double d2 = this.northeast.longitude;
        double d3 = this.southwest.longitude;
        double d4 = point.longitude;
        if (b(d4)) {
            d4 = d3;
            d = d2;
        } else if (b(d3, d4) < c(d2, d4)) {
            d = d2;
        } else {
            d = d4;
            d4 = d3;
        }
        return new LatLngBounds(new LatLng(min, d4), new LatLng(max, d));
    }

    public String toString() {
        return dl.d(this).a("southwest", this.southwest).a("northeast", this.northeast).toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        if (r.cK()) {
            d.a(this, out, flags);
        } else {
            LatLngBoundsCreator.a(this, out, flags);
        }
    }
}

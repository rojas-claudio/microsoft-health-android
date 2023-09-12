package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.r;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public final class CircleOptions implements SafeParcelable {
    public static final CircleOptionsCreator CREATOR = new CircleOptionsCreator();
    private final int iM;
    private LatLng qf;
    private double qg;
    private float qh;
    private int qi;
    private int qj;
    private float qk;
    private boolean ql;

    public CircleOptions() {
        this.qf = null;
        this.qg = Constants.SPLITS_ACCURACY;
        this.qh = 10.0f;
        this.qi = -16777216;
        this.qj = 0;
        this.qk = 0.0f;
        this.ql = true;
        this.iM = 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CircleOptions(int versionCode, LatLng center, double radius, float strokeWidth, int strokeColor, int fillColor, float zIndex, boolean visible) {
        this.qf = null;
        this.qg = Constants.SPLITS_ACCURACY;
        this.qh = 10.0f;
        this.qi = -16777216;
        this.qj = 0;
        this.qk = 0.0f;
        this.ql = true;
        this.iM = versionCode;
        this.qf = center;
        this.qg = radius;
        this.qh = strokeWidth;
        this.qi = strokeColor;
        this.qj = fillColor;
        this.qk = zIndex;
        this.ql = visible;
    }

    public CircleOptions center(LatLng center) {
        this.qf = center;
        return this;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public CircleOptions fillColor(int color) {
        this.qj = color;
        return this;
    }

    public LatLng getCenter() {
        return this.qf;
    }

    public int getFillColor() {
        return this.qj;
    }

    public double getRadius() {
        return this.qg;
    }

    public int getStrokeColor() {
        return this.qi;
    }

    public float getStrokeWidth() {
        return this.qh;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getVersionCode() {
        return this.iM;
    }

    public float getZIndex() {
        return this.qk;
    }

    public boolean isVisible() {
        return this.ql;
    }

    public CircleOptions radius(double radius) {
        this.qg = radius;
        return this;
    }

    public CircleOptions strokeColor(int color) {
        this.qi = color;
        return this;
    }

    public CircleOptions strokeWidth(float width) {
        this.qh = width;
        return this;
    }

    public CircleOptions visible(boolean visible) {
        this.ql = visible;
        return this;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        if (r.cK()) {
            b.a(this, out, flags);
        } else {
            CircleOptionsCreator.a(this, out, flags);
        }
    }

    public CircleOptions zIndex(float zIndex) {
        this.qk = zIndex;
        return this;
    }
}

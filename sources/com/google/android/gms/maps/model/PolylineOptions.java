package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.r;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public final class PolylineOptions implements SafeParcelable {
    public static final PolylineOptionsCreator CREATOR = new PolylineOptionsCreator();
    private final int iM;
    private int jc;
    private final List<LatLng> qK;
    private boolean qM;
    private float qk;
    private boolean ql;
    private float qp;

    public PolylineOptions() {
        this.qp = 10.0f;
        this.jc = -16777216;
        this.qk = 0.0f;
        this.ql = true;
        this.qM = false;
        this.iM = 1;
        this.qK = new ArrayList();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PolylineOptions(int versionCode, List points, float width, int color, float zIndex, boolean visible, boolean geodesic) {
        this.qp = 10.0f;
        this.jc = -16777216;
        this.qk = 0.0f;
        this.ql = true;
        this.qM = false;
        this.iM = versionCode;
        this.qK = points;
        this.qp = width;
        this.jc = color;
        this.qk = zIndex;
        this.ql = visible;
        this.qM = geodesic;
    }

    public PolylineOptions add(LatLng point) {
        this.qK.add(point);
        return this;
    }

    public PolylineOptions add(LatLng... points) {
        this.qK.addAll(Arrays.asList(points));
        return this;
    }

    public PolylineOptions addAll(Iterable<LatLng> points) {
        for (LatLng latLng : points) {
            this.qK.add(latLng);
        }
        return this;
    }

    public PolylineOptions color(int color) {
        this.jc = color;
        return this;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public PolylineOptions geodesic(boolean geodesic) {
        this.qM = geodesic;
        return this;
    }

    public int getColor() {
        return this.jc;
    }

    public List<LatLng> getPoints() {
        return this.qK;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getVersionCode() {
        return this.iM;
    }

    public float getWidth() {
        return this.qp;
    }

    public float getZIndex() {
        return this.qk;
    }

    public boolean isGeodesic() {
        return this.qM;
    }

    public boolean isVisible() {
        return this.ql;
    }

    public PolylineOptions visible(boolean visible) {
        this.ql = visible;
        return this;
    }

    public PolylineOptions width(float width) {
        this.qp = width;
        return this;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        if (r.cK()) {
            h.a(this, out, flags);
        } else {
            PolylineOptionsCreator.a(this, out, flags);
        }
    }

    public PolylineOptions zIndex(float zIndex) {
        this.qk = zIndex;
        return this;
    }
}

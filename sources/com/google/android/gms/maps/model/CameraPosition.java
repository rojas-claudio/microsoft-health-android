package com.google.android.gms.maps.model;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.util.AttributeSet;
import com.google.android.gms.R;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dl;
import com.google.android.gms.internal.dm;
import com.google.android.gms.maps.internal.r;
import com.microsoft.kapp.utils.Constants;
/* loaded from: classes.dex */
public final class CameraPosition implements SafeParcelable {
    public static final CameraPositionCreator CREATOR = new CameraPositionCreator();
    public final float bearing;
    private final int iM;
    public final LatLng target;
    public final float tilt;
    public final float zoom;

    /* loaded from: classes.dex */
    public static final class Builder {
        private LatLng qa;
        private float qb;
        private float qc;
        private float qd;

        public Builder() {
        }

        public Builder(CameraPosition previous) {
            this.qa = previous.target;
            this.qb = previous.zoom;
            this.qc = previous.tilt;
            this.qd = previous.bearing;
        }

        public Builder bearing(float bearing) {
            this.qd = bearing;
            return this;
        }

        public CameraPosition build() {
            return new CameraPosition(this.qa, this.qb, this.qc, this.qd);
        }

        public Builder target(LatLng location) {
            this.qa = location;
            return this;
        }

        public Builder tilt(float tilt) {
            this.qc = tilt;
            return this;
        }

        public Builder zoom(float zoom) {
            this.qb = zoom;
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CameraPosition(int versionCode, LatLng target, float zoom, float tilt, float bearing) {
        dm.a(target, "null camera target");
        dm.b(0.0f <= tilt && tilt <= 90.0f, "Tilt needs to be between 0 and 90 inclusive");
        this.iM = versionCode;
        this.target = target;
        this.zoom = zoom;
        this.tilt = tilt + 0.0f;
        this.bearing = (((double) bearing) <= Constants.SPLITS_ACCURACY ? (bearing % 360.0f) + 360.0f : bearing) % 360.0f;
    }

    public CameraPosition(LatLng target, float zoom, float tilt, float bearing) {
        this(1, target, zoom, tilt, bearing);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(CameraPosition camera) {
        return new Builder(camera);
    }

    public static CameraPosition createFromAttributes(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return null;
        }
        TypedArray obtainAttributes = context.getResources().obtainAttributes(attrs, R.styleable.MapAttrs);
        LatLng latLng = new LatLng(obtainAttributes.hasValue(2) ? obtainAttributes.getFloat(2, 0.0f) : 0.0f, obtainAttributes.hasValue(3) ? obtainAttributes.getFloat(3, 0.0f) : 0.0f);
        Builder builder = builder();
        builder.target(latLng);
        if (obtainAttributes.hasValue(5)) {
            builder.zoom(obtainAttributes.getFloat(5, 0.0f));
        }
        if (obtainAttributes.hasValue(1)) {
            builder.bearing(obtainAttributes.getFloat(1, 0.0f));
        }
        if (obtainAttributes.hasValue(4)) {
            builder.tilt(obtainAttributes.getFloat(4, 0.0f));
        }
        return builder.build();
    }

    public static final CameraPosition fromLatLngZoom(LatLng target, float zoom) {
        return new CameraPosition(target, zoom, 0.0f, 0.0f);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof CameraPosition) {
            CameraPosition cameraPosition = (CameraPosition) o;
            return this.target.equals(cameraPosition.target) && Float.floatToIntBits(this.zoom) == Float.floatToIntBits(cameraPosition.zoom) && Float.floatToIntBits(this.tilt) == Float.floatToIntBits(cameraPosition.tilt) && Float.floatToIntBits(this.bearing) == Float.floatToIntBits(cameraPosition.bearing);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getVersionCode() {
        return this.iM;
    }

    public int hashCode() {
        return dl.hashCode(this.target, Float.valueOf(this.zoom), Float.valueOf(this.tilt), Float.valueOf(this.bearing));
    }

    public String toString() {
        return dl.d(this).a("target", this.target).a("zoom", Float.valueOf(this.zoom)).a("tilt", Float.valueOf(this.tilt)).a("bearing", Float.valueOf(this.bearing)).toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        if (r.cK()) {
            a.a(this, out, flags);
        } else {
            CameraPositionCreator.a(this, out, flags);
        }
    }
}

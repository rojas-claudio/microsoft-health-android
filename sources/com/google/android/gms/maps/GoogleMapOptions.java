package com.google.android.gms.maps;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.util.AttributeSet;
import com.google.android.gms.R;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.r;
import com.google.android.gms.maps.model.CameraPosition;
/* loaded from: classes.dex */
public final class GoogleMapOptions implements SafeParcelable {
    public static final GoogleMapOptionsCreator CREATOR = new GoogleMapOptionsCreator();
    private final int iM;
    private CameraPosition pA;
    private Boolean pB;
    private Boolean pC;
    private Boolean pD;
    private Boolean pE;
    private Boolean pF;
    private Boolean pG;
    private Boolean px;
    private Boolean py;
    private int pz;

    public GoogleMapOptions() {
        this.pz = -1;
        this.iM = 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public GoogleMapOptions(int versionCode, byte zOrderOnTop, byte useViewLifecycleInFragment, int mapType, CameraPosition camera, byte zoomControlsEnabled, byte compassEnabled, byte scrollGesturesEnabled, byte zoomGesturesEnabled, byte tiltGesturesEnabled, byte rotateGesturesEnabled) {
        this.pz = -1;
        this.iM = versionCode;
        this.px = com.google.android.gms.maps.internal.a.a(zOrderOnTop);
        this.py = com.google.android.gms.maps.internal.a.a(useViewLifecycleInFragment);
        this.pz = mapType;
        this.pA = camera;
        this.pB = com.google.android.gms.maps.internal.a.a(zoomControlsEnabled);
        this.pC = com.google.android.gms.maps.internal.a.a(compassEnabled);
        this.pD = com.google.android.gms.maps.internal.a.a(scrollGesturesEnabled);
        this.pE = com.google.android.gms.maps.internal.a.a(zoomGesturesEnabled);
        this.pF = com.google.android.gms.maps.internal.a.a(tiltGesturesEnabled);
        this.pG = com.google.android.gms.maps.internal.a.a(rotateGesturesEnabled);
    }

    public static GoogleMapOptions createFromAttributes(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return null;
        }
        TypedArray obtainAttributes = context.getResources().obtainAttributes(attrs, R.styleable.MapAttrs);
        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        if (obtainAttributes.hasValue(0)) {
            googleMapOptions.mapType(obtainAttributes.getInt(0, -1));
        }
        if (obtainAttributes.hasValue(13)) {
            googleMapOptions.zOrderOnTop(obtainAttributes.getBoolean(13, false));
        }
        if (obtainAttributes.hasValue(12)) {
            googleMapOptions.useViewLifecycleInFragment(obtainAttributes.getBoolean(12, false));
        }
        if (obtainAttributes.hasValue(6)) {
            googleMapOptions.compassEnabled(obtainAttributes.getBoolean(6, true));
        }
        if (obtainAttributes.hasValue(7)) {
            googleMapOptions.rotateGesturesEnabled(obtainAttributes.getBoolean(7, true));
        }
        if (obtainAttributes.hasValue(8)) {
            googleMapOptions.scrollGesturesEnabled(obtainAttributes.getBoolean(8, true));
        }
        if (obtainAttributes.hasValue(9)) {
            googleMapOptions.tiltGesturesEnabled(obtainAttributes.getBoolean(9, true));
        }
        if (obtainAttributes.hasValue(11)) {
            googleMapOptions.zoomGesturesEnabled(obtainAttributes.getBoolean(11, true));
        }
        if (obtainAttributes.hasValue(10)) {
            googleMapOptions.zoomControlsEnabled(obtainAttributes.getBoolean(10, true));
        }
        googleMapOptions.camera(CameraPosition.createFromAttributes(context, attrs));
        obtainAttributes.recycle();
        return googleMapOptions;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte cA() {
        return com.google.android.gms.maps.internal.a.b(this.pE);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte cB() {
        return com.google.android.gms.maps.internal.a.b(this.pF);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte cC() {
        return com.google.android.gms.maps.internal.a.b(this.pG);
    }

    public GoogleMapOptions camera(CameraPosition camera) {
        this.pA = camera;
        return this;
    }

    public GoogleMapOptions compassEnabled(boolean enabled) {
        this.pC = Boolean.valueOf(enabled);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte cv() {
        return com.google.android.gms.maps.internal.a.b(this.px);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte cw() {
        return com.google.android.gms.maps.internal.a.b(this.py);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte cx() {
        return com.google.android.gms.maps.internal.a.b(this.pB);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte cy() {
        return com.google.android.gms.maps.internal.a.b(this.pC);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte cz() {
        return com.google.android.gms.maps.internal.a.b(this.pD);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public CameraPosition getCamera() {
        return this.pA;
    }

    public Boolean getCompassEnabled() {
        return this.pC;
    }

    public int getMapType() {
        return this.pz;
    }

    public Boolean getRotateGesturesEnabled() {
        return this.pG;
    }

    public Boolean getScrollGesturesEnabled() {
        return this.pD;
    }

    public Boolean getTiltGesturesEnabled() {
        return this.pF;
    }

    public Boolean getUseViewLifecycleInFragment() {
        return this.py;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getVersionCode() {
        return this.iM;
    }

    public Boolean getZOrderOnTop() {
        return this.px;
    }

    public Boolean getZoomControlsEnabled() {
        return this.pB;
    }

    public Boolean getZoomGesturesEnabled() {
        return this.pE;
    }

    public GoogleMapOptions mapType(int mapType) {
        this.pz = mapType;
        return this;
    }

    public GoogleMapOptions rotateGesturesEnabled(boolean enabled) {
        this.pG = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions scrollGesturesEnabled(boolean enabled) {
        this.pD = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions tiltGesturesEnabled(boolean enabled) {
        this.pF = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions useViewLifecycleInFragment(boolean useViewLifecycleInFragment) {
        this.py = Boolean.valueOf(useViewLifecycleInFragment);
        return this;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        if (r.cK()) {
            a.a(this, out, flags);
        } else {
            GoogleMapOptionsCreator.a(this, out, flags);
        }
    }

    public GoogleMapOptions zOrderOnTop(boolean zOrderOnTop) {
        this.px = Boolean.valueOf(zOrderOnTop);
        return this;
    }

    public GoogleMapOptions zoomControlsEnabled(boolean enabled) {
        this.pB = Boolean.valueOf(enabled);
        return this;
    }

    public GoogleMapOptions zoomGesturesEnabled(boolean enabled) {
        this.pE = Boolean.valueOf(enabled);
        return this;
    }
}

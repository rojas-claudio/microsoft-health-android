package com.google.android.gms.maps;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.RemoteException;
import android.view.View;
import com.google.android.gms.internal.dm;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import com.google.android.gms.maps.internal.ILocationSourceDelegate;
import com.google.android.gms.maps.internal.b;
import com.google.android.gms.maps.internal.d;
import com.google.android.gms.maps.internal.e;
import com.google.android.gms.maps.internal.f;
import com.google.android.gms.maps.internal.g;
import com.google.android.gms.maps.internal.h;
import com.google.android.gms.maps.internal.i;
import com.google.android.gms.maps.internal.j;
import com.google.android.gms.maps.internal.k;
import com.google.android.gms.maps.internal.l;
import com.google.android.gms.maps.internal.m;
import com.google.android.gms.maps.internal.n;
import com.google.android.gms.maps.internal.o;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.internal.c;
import com.google.android.gms.maps.model.internal.d;
import com.google.android.gms.maps.model.internal.f;
/* loaded from: classes.dex */
public final class GoogleMap {
    public static final int MAP_TYPE_HYBRID = 4;
    public static final int MAP_TYPE_NONE = 0;
    public static final int MAP_TYPE_NORMAL = 1;
    public static final int MAP_TYPE_SATELLITE = 2;
    public static final int MAP_TYPE_TERRAIN = 3;
    private final IGoogleMapDelegate pf;
    private UiSettings pg;

    /* loaded from: classes.dex */
    public interface CancelableCallback {
        void onCancel();

        void onFinish();
    }

    /* loaded from: classes.dex */
    public interface InfoWindowAdapter {
        View getInfoContents(Marker marker);

        View getInfoWindow(Marker marker);
    }

    /* loaded from: classes.dex */
    public interface OnCameraChangeListener {
        void onCameraChange(CameraPosition cameraPosition);
    }

    /* loaded from: classes.dex */
    public interface OnInfoWindowClickListener {
        void onInfoWindowClick(Marker marker);
    }

    /* loaded from: classes.dex */
    public interface OnMapClickListener {
        void onMapClick(LatLng latLng);
    }

    /* loaded from: classes.dex */
    public interface OnMapLoadedCallback {
        void onMapLoaded();
    }

    /* loaded from: classes.dex */
    public interface OnMapLongClickListener {
        void onMapLongClick(LatLng latLng);
    }

    /* loaded from: classes.dex */
    public interface OnMarkerClickListener {
        boolean onMarkerClick(Marker marker);
    }

    /* loaded from: classes.dex */
    public interface OnMarkerDragListener {
        void onMarkerDrag(Marker marker);

        void onMarkerDragEnd(Marker marker);

        void onMarkerDragStart(Marker marker);
    }

    /* loaded from: classes.dex */
    public interface OnMyLocationButtonClickListener {
        boolean onMyLocationButtonClick();
    }

    @Deprecated
    /* loaded from: classes.dex */
    public interface OnMyLocationChangeListener {
        void onMyLocationChange(Location location);
    }

    /* loaded from: classes.dex */
    public interface SnapshotReadyCallback {
        void onSnapshotReady(Bitmap bitmap);
    }

    /* loaded from: classes.dex */
    private static final class a extends b.a {
        private final CancelableCallback pw;

        a(CancelableCallback cancelableCallback) {
            this.pw = cancelableCallback;
        }

        @Override // com.google.android.gms.maps.internal.b
        public void onCancel() {
            this.pw.onCancel();
        }

        @Override // com.google.android.gms.maps.internal.b
        public void onFinish() {
            this.pw.onFinish();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public GoogleMap(IGoogleMapDelegate map) {
        this.pf = (IGoogleMapDelegate) dm.e(map);
    }

    public final Circle addCircle(CircleOptions options) {
        try {
            return new Circle(this.pf.addCircle(options));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final GroundOverlay addGroundOverlay(GroundOverlayOptions options) {
        try {
            c addGroundOverlay = this.pf.addGroundOverlay(options);
            if (addGroundOverlay != null) {
                return new GroundOverlay(addGroundOverlay);
            }
            return null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Marker addMarker(MarkerOptions options) {
        try {
            d addMarker = this.pf.addMarker(options);
            if (addMarker != null) {
                return new Marker(addMarker);
            }
            return null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Polygon addPolygon(PolygonOptions options) {
        try {
            return new Polygon(this.pf.addPolygon(options));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Polyline addPolyline(PolylineOptions options) {
        try {
            return new Polyline(this.pf.addPolyline(options));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final TileOverlay addTileOverlay(TileOverlayOptions options) {
        try {
            f addTileOverlay = this.pf.addTileOverlay(options);
            if (addTileOverlay != null) {
                return new TileOverlay(addTileOverlay);
            }
            return null;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void animateCamera(CameraUpdate update) {
        try {
            this.pf.animateCamera(update.cs());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void animateCamera(CameraUpdate update, int durationMs, CancelableCallback callback) {
        try {
            this.pf.animateCameraWithDurationAndCallback(update.cs(), durationMs, callback == null ? null : new a(callback));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void animateCamera(CameraUpdate update, CancelableCallback callback) {
        try {
            this.pf.animateCameraWithCallback(update.cs(), callback == null ? null : new a(callback));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void clear() {
        try {
            this.pf.clear();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IGoogleMapDelegate cu() {
        return this.pf;
    }

    public final CameraPosition getCameraPosition() {
        try {
            return this.pf.getCameraPosition();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final int getMapType() {
        try {
            return this.pf.getMapType();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getMaxZoomLevel() {
        try {
            return this.pf.getMaxZoomLevel();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final float getMinZoomLevel() {
        try {
            return this.pf.getMinZoomLevel();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    @Deprecated
    public final Location getMyLocation() {
        try {
            return this.pf.getMyLocation();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final Projection getProjection() {
        try {
            return new Projection(this.pf.getProjection());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final UiSettings getUiSettings() {
        try {
            if (this.pg == null) {
                this.pg = new UiSettings(this.pf.getUiSettings());
            }
            return this.pg;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isBuildingsEnabled() {
        try {
            return this.pf.isBuildingsEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isIndoorEnabled() {
        try {
            return this.pf.isIndoorEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isMyLocationEnabled() {
        try {
            return this.pf.isMyLocationEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean isTrafficEnabled() {
        try {
            return this.pf.isTrafficEnabled();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void moveCamera(CameraUpdate update) {
        try {
            this.pf.moveCamera(update.cs());
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setBuildingsEnabled(boolean enabled) {
        try {
            this.pf.setBuildingsEnabled(enabled);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final boolean setIndoorEnabled(boolean enabled) {
        try {
            return this.pf.setIndoorEnabled(enabled);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setInfoWindowAdapter(final InfoWindowAdapter adapter) {
        try {
            if (adapter == null) {
                this.pf.setInfoWindowAdapter(null);
            } else {
                this.pf.setInfoWindowAdapter(new d.a() { // from class: com.google.android.gms.maps.GoogleMap.11
                    @Override // com.google.android.gms.maps.internal.d
                    public com.google.android.gms.dynamic.b f(com.google.android.gms.maps.model.internal.d dVar) {
                        return com.google.android.gms.dynamic.c.g(adapter.getInfoWindow(new Marker(dVar)));
                    }

                    @Override // com.google.android.gms.maps.internal.d
                    public com.google.android.gms.dynamic.b g(com.google.android.gms.maps.model.internal.d dVar) {
                        return com.google.android.gms.dynamic.c.g(adapter.getInfoContents(new Marker(dVar)));
                    }
                });
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setLocationSource(final LocationSource source) {
        try {
            if (source == null) {
                this.pf.setLocationSource(null);
            } else {
                this.pf.setLocationSource(new ILocationSourceDelegate.a() { // from class: com.google.android.gms.maps.GoogleMap.1
                    @Override // com.google.android.gms.maps.internal.ILocationSourceDelegate
                    public void activate(final g listener) {
                        source.activate(new LocationSource.OnLocationChangedListener() { // from class: com.google.android.gms.maps.GoogleMap.1.1
                            @Override // com.google.android.gms.maps.LocationSource.OnLocationChangedListener
                            public void onLocationChanged(Location location) {
                                try {
                                    listener.g(com.google.android.gms.dynamic.c.g(location));
                                } catch (RemoteException e) {
                                    throw new RuntimeRemoteException(e);
                                }
                            }
                        });
                    }

                    @Override // com.google.android.gms.maps.internal.ILocationSourceDelegate
                    public void deactivate() {
                        source.deactivate();
                    }
                });
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setMapType(int type) {
        try {
            this.pf.setMapType(type);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setMyLocationEnabled(boolean enabled) {
        try {
            this.pf.setMyLocationEnabled(enabled);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnCameraChangeListener(final OnCameraChangeListener listener) {
        try {
            if (listener == null) {
                this.pf.setOnCameraChangeListener(null);
            } else {
                this.pf.setOnCameraChangeListener(new e.a() { // from class: com.google.android.gms.maps.GoogleMap.5
                    @Override // com.google.android.gms.maps.internal.e
                    public void onCameraChange(CameraPosition position) {
                        listener.onCameraChange(position);
                    }
                });
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnInfoWindowClickListener(final OnInfoWindowClickListener listener) {
        try {
            if (listener == null) {
                this.pf.setOnInfoWindowClickListener(null);
            } else {
                this.pf.setOnInfoWindowClickListener(new f.a() { // from class: com.google.android.gms.maps.GoogleMap.10
                    @Override // com.google.android.gms.maps.internal.f
                    public void e(com.google.android.gms.maps.model.internal.d dVar) {
                        listener.onInfoWindowClick(new Marker(dVar));
                    }
                });
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnMapClickListener(final OnMapClickListener listener) {
        try {
            if (listener == null) {
                this.pf.setOnMapClickListener(null);
            } else {
                this.pf.setOnMapClickListener(new h.a() { // from class: com.google.android.gms.maps.GoogleMap.6
                    @Override // com.google.android.gms.maps.internal.h
                    public void onMapClick(LatLng point) {
                        listener.onMapClick(point);
                    }
                });
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public void setOnMapLoadedCallback(final OnMapLoadedCallback callback) {
        try {
            if (callback == null) {
                this.pf.setOnMapLoadedCallback(null);
            } else {
                this.pf.setOnMapLoadedCallback(new i.a() { // from class: com.google.android.gms.maps.GoogleMap.3
                    @Override // com.google.android.gms.maps.internal.i
                    public void onMapLoaded() throws RemoteException {
                        callback.onMapLoaded();
                    }
                });
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnMapLongClickListener(final OnMapLongClickListener listener) {
        try {
            if (listener == null) {
                this.pf.setOnMapLongClickListener(null);
            } else {
                this.pf.setOnMapLongClickListener(new j.a() { // from class: com.google.android.gms.maps.GoogleMap.7
                    @Override // com.google.android.gms.maps.internal.j
                    public void onMapLongClick(LatLng point) {
                        listener.onMapLongClick(point);
                    }
                });
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnMarkerClickListener(final OnMarkerClickListener listener) {
        try {
            if (listener == null) {
                this.pf.setOnMarkerClickListener(null);
            } else {
                this.pf.setOnMarkerClickListener(new k.a() { // from class: com.google.android.gms.maps.GoogleMap.8
                    @Override // com.google.android.gms.maps.internal.k
                    public boolean a(com.google.android.gms.maps.model.internal.d dVar) {
                        return listener.onMarkerClick(new Marker(dVar));
                    }
                });
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnMarkerDragListener(final OnMarkerDragListener listener) {
        try {
            if (listener == null) {
                this.pf.setOnMarkerDragListener(null);
            } else {
                this.pf.setOnMarkerDragListener(new l.a() { // from class: com.google.android.gms.maps.GoogleMap.9
                    @Override // com.google.android.gms.maps.internal.l
                    public void b(com.google.android.gms.maps.model.internal.d dVar) {
                        listener.onMarkerDragStart(new Marker(dVar));
                    }

                    @Override // com.google.android.gms.maps.internal.l
                    public void c(com.google.android.gms.maps.model.internal.d dVar) {
                        listener.onMarkerDragEnd(new Marker(dVar));
                    }

                    @Override // com.google.android.gms.maps.internal.l
                    public void d(com.google.android.gms.maps.model.internal.d dVar) {
                        listener.onMarkerDrag(new Marker(dVar));
                    }
                });
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setOnMyLocationButtonClickListener(final OnMyLocationButtonClickListener listener) {
        try {
            if (listener == null) {
                this.pf.setOnMyLocationButtonClickListener(null);
            } else {
                this.pf.setOnMyLocationButtonClickListener(new m.a() { // from class: com.google.android.gms.maps.GoogleMap.2
                    @Override // com.google.android.gms.maps.internal.m
                    public boolean onMyLocationButtonClick() throws RemoteException {
                        return listener.onMyLocationButtonClick();
                    }
                });
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    @Deprecated
    public final void setOnMyLocationChangeListener(final OnMyLocationChangeListener listener) {
        try {
            if (listener == null) {
                this.pf.setOnMyLocationChangeListener(null);
            } else {
                this.pf.setOnMyLocationChangeListener(new n.a() { // from class: com.google.android.gms.maps.GoogleMap.12
                    @Override // com.google.android.gms.maps.internal.n
                    public void d(com.google.android.gms.dynamic.b bVar) {
                        listener.onMyLocationChange((Location) com.google.android.gms.dynamic.c.b(bVar));
                    }
                });
            }
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setPadding(int left, int top, int right, int bottom) {
        try {
            this.pf.setPadding(left, top, right, bottom);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void setTrafficEnabled(boolean enabled) {
        try {
            this.pf.setTrafficEnabled(enabled);
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void snapshot(SnapshotReadyCallback callback) {
        snapshot(callback, null);
    }

    public final void snapshot(final SnapshotReadyCallback callback, Bitmap bitmap) {
        try {
            this.pf.snapshot(new o.a() { // from class: com.google.android.gms.maps.GoogleMap.4
                @Override // com.google.android.gms.maps.internal.o
                public void c(com.google.android.gms.dynamic.b bVar) throws RemoteException {
                    callback.onSnapshotReady((Bitmap) com.google.android.gms.dynamic.c.b(bVar));
                }

                @Override // com.google.android.gms.maps.internal.o
                public void onSnapshotReady(Bitmap snapshot) throws RemoteException {
                    callback.onSnapshotReady(snapshot);
                }
            }, (com.google.android.gms.dynamic.c) (bitmap != null ? com.google.android.gms.dynamic.c.g(bitmap) : null));
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    public final void stopAnimation() {
        try {
            this.pf.stopAnimation();
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }
}

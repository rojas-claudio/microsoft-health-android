package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.r;
import com.google.android.gms.maps.model.internal.g;
/* loaded from: classes.dex */
public final class TileOverlayOptions implements SafeParcelable {
    public static final TileOverlayOptionsCreator CREATOR = new TileOverlayOptionsCreator();
    private final int iM;
    private com.google.android.gms.maps.model.internal.g qP;
    private TileProvider qQ;
    private float qk;
    private boolean ql;

    public TileOverlayOptions() {
        this.ql = true;
        this.iM = 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TileOverlayOptions(int versionCode, IBinder delegate, boolean visible, float zIndex) {
        this.ql = true;
        this.iM = versionCode;
        this.qP = g.a.aj(delegate);
        this.qQ = this.qP == null ? null : new TileProvider() { // from class: com.google.android.gms.maps.model.TileOverlayOptions.1
            private final com.google.android.gms.maps.model.internal.g qR;

            {
                this.qR = TileOverlayOptions.this.qP;
            }

            @Override // com.google.android.gms.maps.model.TileProvider
            public Tile getTile(int x, int y, int zoom) {
                try {
                    return this.qR.getTile(x, y, zoom);
                } catch (RemoteException e) {
                    return null;
                }
            }
        };
        this.ql = visible;
        this.qk = zIndex;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IBinder cP() {
        return this.qP.asBinder();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public TileProvider getTileProvider() {
        return this.qQ;
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

    public TileOverlayOptions tileProvider(final TileProvider tileProvider) {
        this.qQ = tileProvider;
        this.qP = this.qQ == null ? null : new g.a() { // from class: com.google.android.gms.maps.model.TileOverlayOptions.2
            @Override // com.google.android.gms.maps.model.internal.g
            public Tile getTile(int x, int y, int zoom) {
                return tileProvider.getTile(x, y, zoom);
            }
        };
        return this;
    }

    public TileOverlayOptions visible(boolean visible) {
        this.ql = visible;
        return this;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        if (r.cK()) {
            j.a(this, out, flags);
        } else {
            TileOverlayOptionsCreator.a(this, out, flags);
        }
    }

    public TileOverlayOptions zIndex(float zIndex) {
        this.qk = zIndex;
        return this;
    }
}

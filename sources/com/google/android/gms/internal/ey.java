package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.ContentProviderClient;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.a;
import java.util.HashMap;
/* loaded from: classes.dex */
public class ey {
    private final Context mContext;
    private final fc<ex> oO;
    private ContentProviderClient oP = null;
    private boolean oQ = false;
    private HashMap<LocationListener, b> oR = new HashMap<>();

    /* loaded from: classes.dex */
    private static class a extends Handler {
        private final LocationListener oS;

        public a(LocationListener locationListener) {
            this.oS = locationListener;
        }

        public a(LocationListener locationListener, Looper looper) {
            super(looper);
            this.oS = locationListener;
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    this.oS.onLocationChanged(new Location((Location) msg.obj));
                    return;
                default:
                    Log.e("LocationClientHelper", "unknown message in LocationHandler.handleMessage");
                    return;
            }
        }
    }

    /* loaded from: classes.dex */
    private static class b extends a.AbstractBinderC0038a {
        private Handler oT;

        b(LocationListener locationListener, Looper looper) {
            this.oT = looper == null ? new a(locationListener) : new a(locationListener, looper);
        }

        @Override // com.google.android.gms.location.a
        public void onLocationChanged(Location location) {
            if (this.oT == null) {
                Log.e("LocationClientHelper", "Received a location in client after calling removeLocationUpdates.");
                return;
            }
            Message obtain = Message.obtain();
            obtain.what = 1;
            obtain.obj = location;
            this.oT.sendMessage(obtain);
        }

        public void release() {
            this.oT = null;
        }
    }

    public ey(Context context, fc<ex> fcVar) {
        this.mContext = context;
        this.oO = fcVar;
    }

    public void cm() {
        if (this.oQ) {
            setMockMode(false);
        }
    }

    public Location getLastLocation() {
        this.oO.bc();
        try {
            return this.oO.bd().cl();
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    public void removeAllListeners() {
        try {
            synchronized (this.oR) {
                for (b bVar : this.oR.values()) {
                    if (bVar != null) {
                        this.oO.bd().a(bVar);
                    }
                }
                this.oR.clear();
            }
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    public void removeLocationUpdates(PendingIntent callbackIntent) {
        this.oO.bc();
        try {
            this.oO.bd().a(callbackIntent);
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    public void removeLocationUpdates(LocationListener listener) {
        this.oO.bc();
        dm.a(listener, "Invalid null listener");
        synchronized (this.oR) {
            b remove = this.oR.remove(listener);
            if (this.oP != null && this.oR.isEmpty()) {
                this.oP.release();
                this.oP = null;
            }
            if (remove != null) {
                remove.release();
                try {
                    this.oO.bd().a(remove);
                } catch (RemoteException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    public void requestLocationUpdates(LocationRequest request, PendingIntent callbackIntent) {
        this.oO.bc();
        try {
            this.oO.bd().a(request, callbackIntent);
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    public void requestLocationUpdates(LocationRequest request, LocationListener listener, Looper looper) {
        this.oO.bc();
        if (looper == null) {
            dm.a(Looper.myLooper(), "Can't create handler inside thread that has not called Looper.prepare()");
        }
        synchronized (this.oR) {
            b bVar = this.oR.get(listener);
            b bVar2 = bVar == null ? new b(listener, looper) : bVar;
            this.oR.put(listener, bVar2);
            try {
                this.oO.bd().a(request, bVar2, this.mContext.getPackageName());
            } catch (RemoteException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public void setMockLocation(Location mockLocation) {
        this.oO.bc();
        try {
            this.oO.bd().setMockLocation(mockLocation);
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    public void setMockMode(boolean isMockMode) {
        this.oO.bc();
        try {
            this.oO.bd().setMockMode(isMockMode);
            this.oQ = isMockMode;
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }
}

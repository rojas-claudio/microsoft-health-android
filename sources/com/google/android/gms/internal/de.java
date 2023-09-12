package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.di;
import com.google.android.gms.internal.dj;
import java.util.ArrayList;
/* loaded from: classes.dex */
public abstract class de<T extends IInterface> implements GooglePlayServicesClient {
    public static final String[] kO = {"service_esmobile", "service_googleme"};
    private final String[] is;
    private T kD;
    private ArrayList<GooglePlayServicesClient.OnConnectionFailedListener> kH;
    private de<T>.e kK;
    private final Context mContext;
    final Handler mHandler;
    final ArrayList<GooglePlayServicesClient.ConnectionCallbacks> kF = new ArrayList<>();
    private boolean kG = false;
    private boolean kI = false;
    private final ArrayList<de<T>.b<?>> kJ = new ArrayList<>();
    boolean kL = false;
    boolean kM = false;
    private final Object kN = new Object();
    private ArrayList<GooglePlayServicesClient.ConnectionCallbacks> kE = new ArrayList<>();

    /* loaded from: classes.dex */
    final class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (msg.what == 1 && !de.this.isConnecting()) {
                b bVar = (b) msg.obj;
                bVar.aF();
                bVar.unregister();
                return;
            }
            synchronized (de.this.kN) {
                de.this.kM = false;
            }
            if (msg.what == 3) {
                de.this.a(new ConnectionResult(((Integer) msg.obj).intValue(), null));
            } else if (msg.what == 4) {
                synchronized (de.this.kE) {
                    if (de.this.kL && de.this.isConnected() && de.this.kE.contains(msg.obj)) {
                        ((GooglePlayServicesClient.ConnectionCallbacks) msg.obj).onConnected(de.this.ba());
                    }
                }
            } else if (msg.what == 2 && !de.this.isConnected()) {
                b bVar2 = (b) msg.obj;
                bVar2.aF();
                bVar2.unregister();
            } else if (msg.what == 2 || msg.what == 1) {
                ((b) msg.obj).be();
            } else {
                Log.wtf("GmsClient", "Don't know how to handle this message.");
            }
        }
    }

    /* loaded from: classes.dex */
    protected abstract class b<TListener> {
        private boolean kQ = false;
        private TListener mListener;

        public b(TListener tlistener) {
            this.mListener = tlistener;
        }

        protected abstract void a(TListener tlistener);

        protected abstract void aF();

        public void be() {
            TListener tlistener;
            synchronized (this) {
                tlistener = this.mListener;
                if (this.kQ) {
                    Log.w("GmsClient", "Callback proxy " + this + " being reused. This is not safe.");
                }
            }
            if (tlistener != null) {
                try {
                    a(tlistener);
                } catch (RuntimeException e) {
                    aF();
                    throw e;
                }
            } else {
                aF();
            }
            synchronized (this) {
                this.kQ = true;
            }
            unregister();
        }

        public void bf() {
            synchronized (this) {
                this.mListener = null;
            }
        }

        public void unregister() {
            bf();
            synchronized (de.this.kJ) {
                de.this.kJ.remove(this);
            }
        }
    }

    /* loaded from: classes.dex */
    public abstract class c<TListener> extends de<T>.b<TListener> {
        private final com.google.android.gms.common.data.d jf;

        public c(TListener tlistener, com.google.android.gms.common.data.d dVar) {
            super(tlistener);
            this.jf = dVar;
        }

        @Override // com.google.android.gms.internal.de.b
        protected final void a(TListener tlistener) {
            a(tlistener, this.jf);
        }

        protected abstract void a(TListener tlistener, com.google.android.gms.common.data.d dVar);

        @Override // com.google.android.gms.internal.de.b
        protected void aF() {
            if (this.jf != null) {
                this.jf.close();
            }
        }

        @Override // com.google.android.gms.internal.de.b
        public /* bridge */ /* synthetic */ void be() {
            super.be();
        }

        @Override // com.google.android.gms.internal.de.b
        public /* bridge */ /* synthetic */ void bf() {
            super.bf();
        }

        @Override // com.google.android.gms.internal.de.b
        public /* bridge */ /* synthetic */ void unregister() {
            super.unregister();
        }
    }

    /* loaded from: classes.dex */
    public static final class d extends di.a {
        private de kR;

        public d(de deVar) {
            this.kR = deVar;
        }

        @Override // com.google.android.gms.internal.di
        public void b(int i, IBinder iBinder, Bundle bundle) {
            dm.a("onPostInitComplete can be called only once per call to getServiceFromBroker", this.kR);
            this.kR.a(i, iBinder, bundle);
            this.kR = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class e implements ServiceConnection {
        e() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName component, IBinder binder) {
            de.this.u(binder);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName component) {
            de.this.kD = null;
            de.this.bb();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public final class f extends de<T>.b<Boolean> {
        public final Bundle kS;
        public final IBinder kT;
        public final int statusCode;

        public f(int i, IBinder iBinder, Bundle bundle) {
            super(true);
            this.statusCode = i;
            this.kT = iBinder;
            this.kS = bundle;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.de.b
        public void a(Boolean bool) {
            if (bool == null) {
                return;
            }
            switch (this.statusCode) {
                case 0:
                    try {
                        if (de.this.ah().equals(this.kT.getInterfaceDescriptor())) {
                            de.this.kD = de.this.p(this.kT);
                            if (de.this.kD != null) {
                                de.this.aZ();
                                return;
                            }
                        }
                    } catch (RemoteException e) {
                    }
                    df.s(de.this.mContext).b(de.this.ag(), de.this.kK);
                    de.this.kK = null;
                    de.this.kD = null;
                    de.this.a(new ConnectionResult(8, null));
                    return;
                case 10:
                    throw new IllegalStateException("A fatal developer error has occurred. Check the logs for further information.");
                default:
                    PendingIntent pendingIntent = this.kS != null ? (PendingIntent) this.kS.getParcelable("pendingIntent") : null;
                    if (de.this.kK != null) {
                        df.s(de.this.mContext).b(de.this.ag(), de.this.kK);
                        de.this.kK = null;
                    }
                    de.this.kD = null;
                    de.this.a(new ConnectionResult(this.statusCode, pendingIntent));
                    return;
            }
        }

        @Override // com.google.android.gms.internal.de.b
        protected void aF() {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    public de(Context context, GooglePlayServicesClient.ConnectionCallbacks connectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener onConnectionFailedListener, String... strArr) {
        this.mContext = (Context) dm.e(context);
        this.kE.add(dm.e(connectionCallbacks));
        this.kH = new ArrayList<>();
        this.kH.add(dm.e(onConnectionFailedListener));
        this.mHandler = new a(context.getMainLooper());
        a(strArr);
        this.is = strArr;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(int i, IBinder iBinder, Bundle bundle) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1, new f(i, iBinder, bundle)));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void a(ConnectionResult connectionResult) {
        this.mHandler.removeMessages(4);
        synchronized (this.kH) {
            this.kI = true;
            ArrayList<GooglePlayServicesClient.OnConnectionFailedListener> arrayList = this.kH;
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                if (!this.kL) {
                    return;
                }
                if (this.kH.contains(arrayList.get(i))) {
                    arrayList.get(i).onConnectionFailed(connectionResult);
                }
            }
            this.kI = false;
        }
    }

    public final void a(de<T>.b<?> bVar) {
        synchronized (this.kJ) {
            this.kJ.add(bVar);
        }
        this.mHandler.sendMessage(this.mHandler.obtainMessage(2, bVar));
    }

    protected abstract void a(dj djVar, d dVar) throws RemoteException;

    protected void a(String... strArr) {
    }

    public final String[] aY() {
        return this.is;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void aZ() {
        synchronized (this.kE) {
            dm.k(!this.kG);
            this.mHandler.removeMessages(4);
            this.kG = true;
            dm.k(this.kF.size() == 0);
            Bundle ba = ba();
            ArrayList<GooglePlayServicesClient.ConnectionCallbacks> arrayList = this.kE;
            int size = arrayList.size();
            for (int i = 0; i < size && this.kL && isConnected(); i++) {
                this.kF.size();
                if (!this.kF.contains(arrayList.get(i))) {
                    arrayList.get(i).onConnected(ba);
                }
            }
            this.kF.clear();
            this.kG = false;
        }
    }

    protected abstract String ag();

    protected abstract String ah();

    protected Bundle ba() {
        return null;
    }

    protected final void bb() {
        this.mHandler.removeMessages(4);
        synchronized (this.kE) {
            this.kG = true;
            ArrayList<GooglePlayServicesClient.ConnectionCallbacks> arrayList = this.kE;
            int size = arrayList.size();
            for (int i = 0; i < size && this.kL; i++) {
                if (this.kE.contains(arrayList.get(i))) {
                    arrayList.get(i).onDisconnected();
                }
            }
            this.kG = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void bc() {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final T bd() {
        bc();
        return this.kD;
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void connect() {
        this.kL = true;
        synchronized (this.kN) {
            this.kM = true;
        }
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.mContext);
        if (isGooglePlayServicesAvailable != 0) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(3, Integer.valueOf(isGooglePlayServicesAvailable)));
            return;
        }
        if (this.kK != null) {
            Log.e("GmsClient", "Calling connect() while still connected, missing disconnect().");
            this.kD = null;
            df.s(this.mContext).b(ag(), this.kK);
        }
        this.kK = new e();
        if (df.s(this.mContext).a(ag(), this.kK)) {
            return;
        }
        Log.e("GmsClient", "unable to connect to service: " + ag());
        this.mHandler.sendMessage(this.mHandler.obtainMessage(3, 9));
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void disconnect() {
        this.kL = false;
        synchronized (this.kN) {
            this.kM = false;
        }
        synchronized (this.kJ) {
            int size = this.kJ.size();
            for (int i = 0; i < size; i++) {
                this.kJ.get(i).bf();
            }
            this.kJ.clear();
        }
        this.kD = null;
        if (this.kK != null) {
            df.s(this.mContext).b(ag(), this.kK);
            this.kK = null;
        }
    }

    public final Context getContext() {
        return this.mContext;
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnected() {
        return this.kD != null;
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnecting() {
        boolean z;
        synchronized (this.kN) {
            z = this.kM;
        }
        return z;
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnectionCallbacksRegistered(GooglePlayServicesClient.ConnectionCallbacks listener) {
        boolean contains;
        dm.e(listener);
        synchronized (this.kE) {
            contains = this.kE.contains(listener);
        }
        return contains;
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnectionFailedListenerRegistered(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        boolean contains;
        dm.e(listener);
        synchronized (this.kH) {
            contains = this.kH.contains(listener);
        }
        return contains;
    }

    protected abstract T p(IBinder iBinder);

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void registerConnectionCallbacks(GooglePlayServicesClient.ConnectionCallbacks listener) {
        dm.e(listener);
        synchronized (this.kE) {
            if (this.kE.contains(listener)) {
                Log.w("GmsClient", "registerConnectionCallbacks(): listener " + listener + " is already registered");
            } else {
                if (this.kG) {
                    this.kE = new ArrayList<>(this.kE);
                }
                this.kE.add(listener);
            }
        }
        if (isConnected()) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(4, listener));
        }
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void registerConnectionFailedListener(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        dm.e(listener);
        synchronized (this.kH) {
            if (this.kH.contains(listener)) {
                Log.w("GmsClient", "registerConnectionFailedListener(): listener " + listener + " is already registered");
            } else {
                if (this.kI) {
                    this.kH = new ArrayList<>(this.kH);
                }
                this.kH.add(listener);
            }
        }
    }

    protected final void u(IBinder iBinder) {
        try {
            a(dj.a.w(iBinder), new d(this));
        } catch (RemoteException e2) {
            Log.w("GmsClient", "service died");
        }
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void unregisterConnectionCallbacks(GooglePlayServicesClient.ConnectionCallbacks listener) {
        dm.e(listener);
        synchronized (this.kE) {
            if (this.kE != null) {
                if (this.kG) {
                    this.kE = new ArrayList<>(this.kE);
                }
                if (!this.kE.remove(listener)) {
                    Log.w("GmsClient", "unregisterConnectionCallbacks(): listener " + listener + " not found");
                } else if (this.kG && !this.kF.contains(listener)) {
                    this.kF.add(listener);
                }
            }
        }
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void unregisterConnectionFailedListener(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        dm.e(listener);
        synchronized (this.kH) {
            if (this.kH != null) {
                if (this.kI) {
                    this.kH = new ArrayList<>(this.kH);
                }
                if (!this.kH.remove(listener)) {
                    Log.w("GmsClient", "unregisterConnectionFailedListener(): listener " + listener + " not found");
                }
            }
        }
    }
}

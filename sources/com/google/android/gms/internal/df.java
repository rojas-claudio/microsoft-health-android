package com.google.android.gms.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.de;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class df implements Handler.Callback {
    private static final Object kU = new Object();
    private static df kV;
    private final Context kW;
    private final HashMap<String, a> kX = new HashMap<>();
    private final Handler mHandler;

    /* loaded from: classes.dex */
    final class a {
        private final String kY;
        private boolean lb;
        private IBinder lc;
        private ComponentName ld;
        private final ServiceConnectionC0019a kZ = new ServiceConnectionC0019a();
        private final HashSet<de<?>.e> la = new HashSet<>();
        private int mState = 0;

        /* renamed from: com.google.android.gms.internal.df$a$a  reason: collision with other inner class name */
        /* loaded from: classes.dex */
        public class ServiceConnectionC0019a implements ServiceConnection {
            public ServiceConnectionC0019a() {
            }

            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName component, IBinder binder) {
                synchronized (df.this.kX) {
                    a.this.lc = binder;
                    a.this.ld = component;
                    Iterator it = a.this.la.iterator();
                    while (it.hasNext()) {
                        ((de.e) it.next()).onServiceConnected(component, binder);
                    }
                    a.this.mState = 1;
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName component) {
                synchronized (df.this.kX) {
                    a.this.lc = null;
                    a.this.ld = component;
                    Iterator it = a.this.la.iterator();
                    while (it.hasNext()) {
                        ((de.e) it.next()).onServiceDisconnected(component);
                    }
                    a.this.mState = 2;
                }
            }
        }

        public a(String str) {
            this.kY = str;
        }

        public void a(de<?>.e eVar) {
            this.la.add(eVar);
        }

        public void b(de<?>.e eVar) {
            this.la.remove(eVar);
        }

        public ServiceConnectionC0019a bg() {
            return this.kZ;
        }

        public String bh() {
            return this.kY;
        }

        public boolean bi() {
            return this.la.isEmpty();
        }

        public boolean c(de<?>.e eVar) {
            return this.la.contains(eVar);
        }

        public IBinder getBinder() {
            return this.lc;
        }

        public ComponentName getComponentName() {
            return this.ld;
        }

        public int getState() {
            return this.mState;
        }

        public boolean isBound() {
            return this.lb;
        }

        public void l(boolean z) {
            this.lb = z;
        }
    }

    private df(Context context) {
        this.mHandler = new Handler(context.getMainLooper(), this);
        this.kW = context.getApplicationContext();
    }

    public static df s(Context context) {
        synchronized (kU) {
            if (kV == null) {
                kV = new df(context.getApplicationContext());
            }
        }
        return kV;
    }

    public boolean a(String str, de<?>.e eVar) {
        boolean isBound;
        synchronized (this.kX) {
            a aVar = this.kX.get(str);
            if (aVar != null) {
                this.mHandler.removeMessages(0, aVar);
                if (!aVar.c(eVar)) {
                    aVar.a(eVar);
                    switch (aVar.getState()) {
                        case 1:
                            eVar.onServiceConnected(aVar.getComponentName(), aVar.getBinder());
                            break;
                        case 2:
                            aVar.l(this.kW.bindService(new Intent(str).setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE), aVar.bg(), 129));
                            break;
                    }
                } else {
                    throw new IllegalStateException("Trying to bind a GmsServiceConnection that was already connected before.  startServiceAction=" + str);
                }
            } else {
                aVar = new a(str);
                aVar.a(eVar);
                aVar.l(this.kW.bindService(new Intent(str).setPackage(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE), aVar.bg(), 129));
                this.kX.put(str, aVar);
            }
            isBound = aVar.isBound();
        }
        return isBound;
    }

    public void b(String str, de<?>.e eVar) {
        synchronized (this.kX) {
            a aVar = this.kX.get(str);
            if (aVar == null) {
                throw new IllegalStateException("Nonexistent connection status for service action: " + str);
            }
            if (!aVar.c(eVar)) {
                throw new IllegalStateException("Trying to unbind a GmsServiceConnection  that was not bound before.  startServiceAction=" + str);
            }
            aVar.b(eVar);
            if (aVar.bi()) {
                this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(0, aVar), 5000L);
            }
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                a aVar = (a) msg.obj;
                synchronized (this.kX) {
                    if (aVar.bi()) {
                        this.kW.unbindService(aVar.bg());
                        this.kX.remove(aVar.bh());
                    }
                }
                return true;
            default:
                return false;
        }
    }
}

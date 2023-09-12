package com.google.android.gms.dynamic;

import android.content.Context;
import android.os.IBinder;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.dm;
/* loaded from: classes.dex */
public abstract class e<T> {
    private final String mi;
    private T mj;

    /* loaded from: classes.dex */
    public static class a extends Exception {
        public a(String str) {
            super(str);
        }

        public a(String str, Throwable th) {
            super(str, th);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public e(String str) {
        this.mi = str;
    }

    protected abstract T d(IBinder iBinder);

    /* JADX INFO: Access modifiers changed from: protected */
    public final T t(Context context) throws a {
        if (this.mj == null) {
            dm.e(context);
            Context remoteContext = GooglePlayServicesUtil.getRemoteContext(context);
            if (remoteContext == null) {
                throw new a("Could not get remote context.");
            }
            try {
                this.mj = d((IBinder) remoteContext.getClassLoader().loadClass(this.mi).newInstance());
            } catch (ClassNotFoundException e) {
                throw new a("Could not load creator class.");
            } catch (IllegalAccessException e2) {
                throw new a("Could not access creator.");
            } catch (InstantiationException e3) {
                throw new a("Could not instantiate creator.");
            }
        }
        return this.mj;
    }
}

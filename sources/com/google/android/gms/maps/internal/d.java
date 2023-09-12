package com.google.android.gms.maps.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.b;
import com.google.android.gms.maps.model.internal.d;
/* loaded from: classes.dex */
public interface d extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class a extends Binder implements d {

        /* renamed from: com.google.android.gms.maps.internal.d$a$a  reason: collision with other inner class name */
        /* loaded from: classes.dex */
        private static class C0050a implements d {
            private IBinder dG;

            C0050a(IBinder iBinder) {
                this.dG = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.dG;
            }

            @Override // com.google.android.gms.maps.internal.d
            public com.google.android.gms.dynamic.b f(com.google.android.gms.maps.model.internal.d dVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    obtain.writeStrongBinder(dVar != null ? dVar.asBinder() : null);
                    this.dG.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return b.a.z(obtain2.readStrongBinder());
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.maps.internal.d
            public com.google.android.gms.dynamic.b g(com.google.android.gms.maps.model.internal.d dVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    obtain.writeStrongBinder(dVar != null ? dVar.asBinder() : null);
                    this.dG.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return b.a.z(obtain2.readStrongBinder());
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public a() {
            attachInterface(this, "com.google.android.gms.maps.internal.IInfoWindowAdapter");
        }

        public static d L(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.maps.internal.IInfoWindowAdapter");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof d)) ? new C0050a(iBinder) : (d) queryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    com.google.android.gms.dynamic.b f = f(d.a.af(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeStrongBinder(f != null ? f.asBinder() : null);
                    return true;
                case 2:
                    data.enforceInterface("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    com.google.android.gms.dynamic.b g = g(d.a.af(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeStrongBinder(g != null ? g.asBinder() : null);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.maps.internal.IInfoWindowAdapter");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    com.google.android.gms.dynamic.b f(com.google.android.gms.maps.model.internal.d dVar) throws RemoteException;

    com.google.android.gms.dynamic.b g(com.google.android.gms.maps.model.internal.d dVar) throws RemoteException;
}

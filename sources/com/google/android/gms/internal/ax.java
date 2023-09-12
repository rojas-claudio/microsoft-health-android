package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.b;
import com.google.android.gms.internal.ay;
/* loaded from: classes.dex */
public interface ax extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class a extends Binder implements ax {

        /* renamed from: com.google.android.gms.internal.ax$a$a  reason: collision with other inner class name */
        /* loaded from: classes.dex */
        private static class C0010a implements ax {
            private IBinder dG;

            C0010a(IBinder iBinder) {
                this.dG = iBinder;
            }

            @Override // com.google.android.gms.internal.ax
            public void a(com.google.android.gms.dynamic.b bVar, v vVar, String str, ay ayVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    obtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                    if (vVar != null) {
                        obtain.writeInt(1);
                        vVar.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    this.dG.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.ax
            public void a(com.google.android.gms.dynamic.b bVar, x xVar, v vVar, String str, ay ayVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    obtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                    if (xVar != null) {
                        obtain.writeInt(1);
                        xVar.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (vVar != null) {
                        obtain.writeInt(1);
                        vVar.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    obtain.writeStrongBinder(ayVar != null ? ayVar.asBinder() : null);
                    this.dG.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.dG;
            }

            @Override // com.google.android.gms.internal.ax
            public void destroy() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    this.dG.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.ax
            public com.google.android.gms.dynamic.b getView() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    this.dG.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return b.a.z(obtain2.readStrongBinder());
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.ax
            public void showInterstitial() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    this.dG.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public a() {
            attachInterface(this, "com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
        }

        public static ax j(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ax)) ? new C0010a(iBinder) : (ax) queryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    a(b.a.z(data.readStrongBinder()), data.readInt() != 0 ? x.CREATOR.createFromParcel(data) : null, data.readInt() != 0 ? v.CREATOR.createFromParcel(data) : null, data.readString(), ay.a.k(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    com.google.android.gms.dynamic.b view = getView();
                    reply.writeNoException();
                    reply.writeStrongBinder(view != null ? view.asBinder() : null);
                    return true;
                case 3:
                    data.enforceInterface("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    a(b.a.z(data.readStrongBinder()), data.readInt() != 0 ? v.CREATOR.createFromParcel(data) : null, data.readString(), ay.a.k(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    showInterstitial();
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    destroy();
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.ads.internal.mediation.client.IMediationAdapter");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void a(com.google.android.gms.dynamic.b bVar, v vVar, String str, ay ayVar) throws RemoteException;

    void a(com.google.android.gms.dynamic.b bVar, x xVar, v vVar, String str, ay ayVar) throws RemoteException;

    void destroy() throws RemoteException;

    com.google.android.gms.dynamic.b getView() throws RemoteException;

    void showInterstitial() throws RemoteException;
}

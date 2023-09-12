package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.b;
import com.google.android.gms.internal.aw;
/* loaded from: classes.dex */
public interface ad extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class a extends Binder implements ad {

        /* renamed from: com.google.android.gms.internal.ad$a$a  reason: collision with other inner class name */
        /* loaded from: classes.dex */
        private static class C0007a implements ad {
            private IBinder dG;

            C0007a(IBinder iBinder) {
                this.dG = iBinder;
            }

            @Override // com.google.android.gms.internal.ad
            public IBinder a(com.google.android.gms.dynamic.b bVar, x xVar, String str, aw awVar, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.ads.internal.client.IAdManagerCreator");
                    obtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                    if (xVar != null) {
                        obtain.writeInt(1);
                        xVar.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeString(str);
                    obtain.writeStrongBinder(awVar != null ? awVar.asBinder() : null);
                    obtain.writeInt(i);
                    this.dG.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readStrongBinder();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.dG;
            }
        }

        public static ad g(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.ads.internal.client.IAdManagerCreator");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof ad)) ? new C0007a(iBinder) : (ad) queryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface("com.google.android.gms.ads.internal.client.IAdManagerCreator");
                    IBinder a = a(b.a.z(data.readStrongBinder()), data.readInt() != 0 ? x.CREATOR.createFromParcel(data) : null, data.readString(), aw.a.i(data.readStrongBinder()), data.readInt());
                    reply.writeNoException();
                    reply.writeStrongBinder(a);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.ads.internal.client.IAdManagerCreator");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    IBinder a(com.google.android.gms.dynamic.b bVar, x xVar, String str, aw awVar, int i) throws RemoteException;
}

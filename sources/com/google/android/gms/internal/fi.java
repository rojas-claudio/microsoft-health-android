package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.b;
/* loaded from: classes.dex */
public interface fi extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class a extends Binder implements fi {

        /* renamed from: com.google.android.gms.internal.fi$a$a  reason: collision with other inner class name */
        /* loaded from: classes.dex */
        private static class C0030a implements fi {
            private IBinder dG;

            C0030a(IBinder iBinder) {
                this.dG = iBinder;
            }

            @Override // com.google.android.gms.internal.fi
            public com.google.android.gms.dynamic.b a(com.google.android.gms.dynamic.b bVar, int i, int i2, String str, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusOneButtonCreator");
                    obtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    obtain.writeInt(i3);
                    this.dG.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return b.a.z(obtain2.readStrongBinder());
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.fi
            public com.google.android.gms.dynamic.b a(com.google.android.gms.dynamic.b bVar, int i, int i2, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.plus.internal.IPlusOneButtonCreator");
                    obtain.writeStrongBinder(bVar != null ? bVar.asBinder() : null);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.dG.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return b.a.z(obtain2.readStrongBinder());
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

        public static fi ao(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.plus.internal.IPlusOneButtonCreator");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof fi)) ? new C0030a(iBinder) : (fi) queryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusOneButtonCreator");
                    com.google.android.gms.dynamic.b a = a(b.a.z(data.readStrongBinder()), data.readInt(), data.readInt(), data.readString(), data.readInt());
                    reply.writeNoException();
                    reply.writeStrongBinder(a != null ? a.asBinder() : null);
                    return true;
                case 2:
                    data.enforceInterface("com.google.android.gms.plus.internal.IPlusOneButtonCreator");
                    com.google.android.gms.dynamic.b a2 = a(b.a.z(data.readStrongBinder()), data.readInt(), data.readInt(), data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeStrongBinder(a2 != null ? a2.asBinder() : null);
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.plus.internal.IPlusOneButtonCreator");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    com.google.android.gms.dynamic.b a(com.google.android.gms.dynamic.b bVar, int i, int i2, String str, int i3) throws RemoteException;

    com.google.android.gms.dynamic.b a(com.google.android.gms.dynamic.b bVar, int i, int i2, String str, String str2) throws RemoteException;
}

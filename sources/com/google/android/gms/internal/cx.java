package com.google.android.gms.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.microsoft.kapp.tasks.FirmwareUpdateCheckTask;
/* loaded from: classes.dex */
public interface cx extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class a extends Binder implements cx {

        /* renamed from: com.google.android.gms.internal.cx$a$a  reason: collision with other inner class name */
        /* loaded from: classes.dex */
        private static class C0016a implements cx {
            private IBinder dG;

            C0016a(IBinder iBinder) {
                this.dG = iBinder;
            }

            @Override // com.google.android.gms.internal.cx
            public void a(int i, com.google.android.gms.common.data.d dVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                    obtain.writeInt(i);
                    if (dVar != null) {
                        obtain.writeInt(1);
                        dVar.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.dG.transact(FirmwareUpdateCheckTask.RESULT_ERROR_CLOUD, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.cx
            public void a(com.google.android.gms.common.data.d dVar) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                    if (dVar != null) {
                        obtain.writeInt(1);
                        dVar.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.dG.transact(FirmwareUpdateCheckTask.RESULT_ERROR_DEVICE_OR_CLOUD, obtain, obtain2, 0);
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

            @Override // com.google.android.gms.internal.cx
            public void m(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                    obtain.writeInt(i);
                    this.dG.transact(5005, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.cx
            public void onSignOutComplete() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                    this.dG.transact(5004, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.google.android.gms.internal.cx
            public void onStateDeleted(int statusCode, int stateKey) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                    obtain.writeInt(statusCode);
                    obtain.writeInt(stateKey);
                    this.dG.transact(5003, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public a() {
            attachInterface(this, "com.google.android.gms.appstate.internal.IAppStateCallbacks");
        }

        public static cx s(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.appstate.internal.IAppStateCallbacks");
            return (queryLocalInterface == null || !(queryLocalInterface instanceof cx)) ? new C0016a(iBinder) : (cx) queryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case FirmwareUpdateCheckTask.RESULT_ERROR_CLOUD /* 5001 */:
                    data.enforceInterface("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                    a(data.readInt(), data.readInt() != 0 ? com.google.android.gms.common.data.d.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case FirmwareUpdateCheckTask.RESULT_ERROR_DEVICE_OR_CLOUD /* 5002 */:
                    data.enforceInterface("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                    a(data.readInt() != 0 ? com.google.android.gms.common.data.d.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 5003:
                    data.enforceInterface("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                    onStateDeleted(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 5004:
                    data.enforceInterface("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                    onSignOutComplete();
                    reply.writeNoException();
                    return true;
                case 5005:
                    data.enforceInterface("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                    m(data.readInt());
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("com.google.android.gms.appstate.internal.IAppStateCallbacks");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void a(int i, com.google.android.gms.common.data.d dVar) throws RemoteException;

    void a(com.google.android.gms.common.data.d dVar) throws RemoteException;

    void m(int i) throws RemoteException;

    void onSignOutComplete() throws RemoteException;

    void onStateDeleted(int i, int i2) throws RemoteException;
}

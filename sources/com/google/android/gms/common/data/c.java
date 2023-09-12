package com.google.android.gms.common.data;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
/* loaded from: classes.dex */
public class c<T extends SafeParcelable> extends DataBuffer<T> {
    private static final String[] jk = {"data"};
    private final Parcelable.Creator<T> jl;

    public c(d dVar, Parcelable.Creator<T> creator) {
        super(dVar);
        this.jl = creator;
    }

    @Override // com.google.android.gms.common.data.DataBuffer
    /* renamed from: p */
    public T get(int i) {
        byte[] e = this.jf.e("data", i, 0);
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(e, 0, e.length);
        obtain.setDataPosition(0);
        T createFromParcel = this.jl.createFromParcel(obtain);
        obtain.recycle();
        return createFromParcel;
    }
}

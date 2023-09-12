package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.dw;
/* loaded from: classes.dex */
public class dr implements SafeParcelable {
    public static final ds CREATOR = new ds();
    private final int iM;
    private final dt lt;

    /* JADX INFO: Access modifiers changed from: package-private */
    public dr(int i, dt dtVar) {
        this.iM = i;
        this.lt = dtVar;
    }

    private dr(dt dtVar) {
        this.iM = 1;
        this.lt = dtVar;
    }

    public static dr a(dw.b<?, ?> bVar) {
        if (bVar instanceof dt) {
            return new dr((dt) bVar);
        }
        throw new IllegalArgumentException("Unsupported safe parcelable field converter class.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public dt bk() {
        return this.lt;
    }

    public dw.b<?, ?> bl() {
        if (this.lt != null) {
            return this.lt;
        }
        throw new IllegalStateException("There was no converter wrapped in this ConverterWrapper.");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        ds dsVar = CREATOR;
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getVersionCode() {
        return this.iM;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        ds dsVar = CREATOR;
        ds.a(this, out, flags);
    }
}

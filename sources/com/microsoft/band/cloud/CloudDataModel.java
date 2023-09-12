package com.microsoft.band.cloud;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
/* loaded from: classes.dex */
public abstract class CloudDataModel implements Parcelable, Serializable {
    public static final int CARGO_SERVICE_VERSION = 1;
    private static final long serialVersionUID = 1;
    private int mCargoVersion;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CloudDataModel(Parcel in) {
        this.mCargoVersion = 1;
        this.mCargoVersion = in.readInt();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CloudDataModel() {
        this.mCargoVersion = 1;
        this.mCargoVersion = 1;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return this.mCargoVersion;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int arg1) {
        dest.writeInt(this.mCargoVersion);
    }
}

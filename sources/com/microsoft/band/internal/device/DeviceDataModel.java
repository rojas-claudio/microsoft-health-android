package com.microsoft.band.internal.device;

import android.os.Parcel;
import android.os.Parcelable;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public abstract class DeviceDataModel implements Parcelable {
    private int mVersion;

    /* JADX INFO: Access modifiers changed from: protected */
    public DeviceDataModel(ByteBuffer buffer) {
        this();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DeviceDataModel() {
        this.mVersion = 16842752;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DeviceDataModel(Parcel in) {
        this.mVersion = in.readInt();
    }

    public int getVersion() {
        return this.mVersion;
    }

    public void setVersion(int version) {
        this.mVersion = version;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return getVersion();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mVersion);
    }
}

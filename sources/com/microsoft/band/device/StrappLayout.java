package com.microsoft.band.device;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class StrappLayout implements Parcelable {
    public static final Parcelable.Creator<StrappLayout> CREATOR = new Parcelable.Creator<StrappLayout>() { // from class: com.microsoft.band.device.StrappLayout.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StrappLayout createFromParcel(Parcel in) {
            return new StrappLayout(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StrappLayout[] newArray(int size) {
            return new StrappLayout[size];
        }
    };
    private byte[] mLayoutBlob;

    public StrappLayout(byte[] layoutBlob) {
        this.mLayoutBlob = layoutBlob;
    }

    public byte[] getLayoutBlob() {
        return this.mLayoutBlob;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mLayoutBlob.length);
        dest.writeByteArray(this.mLayoutBlob);
    }

    StrappLayout(Parcel in) {
        this.mLayoutBlob = new byte[in.readInt()];
        in.readByteArray(this.mLayoutBlob);
    }
}

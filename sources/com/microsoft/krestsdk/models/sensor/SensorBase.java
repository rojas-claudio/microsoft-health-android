package com.microsoft.krestsdk.models.sensor;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class SensorBase implements Parcelable {
    public static final Parcelable.Creator<SensorBase> CREATOR = new Parcelable.Creator<SensorBase>() { // from class: com.microsoft.krestsdk.models.sensor.SensorBase.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SensorBase createFromParcel(Parcel in) {
            return new SensorBase(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SensorBase[] newArray(int size) {
            return new SensorBase[size];
        }
    };
    @SerializedName("Duration")
    private int mDuration;
    @SerializedName("Offset")
    private int mOffset;

    public int getOffset() {
        return this.mOffset;
    }

    public void setOffset(int mOffset) {
        this.mOffset = mOffset;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SensorBase() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SensorBase(Parcel in) {
        this.mOffset = in.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mOffset);
    }
}

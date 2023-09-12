package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class SplitMapPoint implements Parcelable {
    public static final Parcelable.Creator<SplitMapPoint> CREATOR = new Parcelable.Creator<SplitMapPoint>() { // from class: com.microsoft.krestsdk.models.SplitMapPoint.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SplitMapPoint createFromParcel(Parcel in) {
            return new SplitMapPoint(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SplitMapPoint[] newArray(int size) {
            return new SplitMapPoint[size];
        }
    };
    @SerializedName("SplitOrdinal")
    private int mSplitOrdinal;

    public void setSplitOrdinal(int ordinal) {
        this.mSplitOrdinal = ordinal;
    }

    public int getSplitOrdinal() {
        return this.mSplitOrdinal;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    protected SplitMapPoint(Parcel in) {
        this.mSplitOrdinal = in.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mSplitOrdinal);
    }
}

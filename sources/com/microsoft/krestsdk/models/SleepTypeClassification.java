package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class SleepTypeClassification implements Parcelable {
    public static final Parcelable.Creator<SleepTypeClassification> CREATOR = new Parcelable.Creator<SleepTypeClassification>() { // from class: com.microsoft.krestsdk.models.SleepTypeClassification.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SleepTypeClassification createFromParcel(Parcel in) {
            return new SleepTypeClassification(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SleepTypeClassification[] newArray(int size) {
            return new SleepTypeClassification[size];
        }
    };
    @SerializedName("RestType")
    public RestType mRestType;
    @SerializedName("Seconds")
    public int mSeconds;

    /* loaded from: classes.dex */
    public enum RestType {
        UNKNOWN,
        AWAKE,
        LightSleep,
        DeepSleep
    }

    public void setRestType(RestType restType) {
        this.mRestType = restType;
    }

    public RestType getRestType() {
        return this.mRestType;
    }

    public void setSeconds(int seconds) {
        this.mSeconds = seconds;
    }

    public int getSeconds() {
        return this.mSeconds;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    protected SleepTypeClassification(Parcel in) {
        this.mSeconds = in.readInt();
        this.mRestType = RestType.values()[in.readInt()];
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mSeconds);
        dest.writeInt(this.mRestType.ordinal());
    }
}

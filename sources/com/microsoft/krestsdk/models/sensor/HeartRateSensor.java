package com.microsoft.krestsdk.models.sensor;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class HeartRateSensor extends SensorBase implements Parcelable {
    public static final Parcelable.Creator<HeartRateSensor> CREATOR = new Parcelable.Creator<HeartRateSensor>() { // from class: com.microsoft.krestsdk.models.sensor.HeartRateSensor.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HeartRateSensor createFromParcel(Parcel in) {
            return new HeartRateSensor(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HeartRateSensor[] newArray(int size) {
            return new HeartRateSensor[size];
        }
    };
    @SerializedName("Quality")
    private int mQuality;
    @SerializedName("Rate")
    private int mRate;

    public int getRate() {
        return this.mRate;
    }

    public void setRate(int mRate) {
        this.mRate = mRate;
    }

    public int getQuality() {
        return this.mQuality;
    }

    public void setQuality(int mQuality) {
        this.mQuality = mQuality;
    }

    protected HeartRateSensor(Parcel in) {
        super(in);
        this.mRate = in.readInt();
        this.mQuality = in.readInt();
    }

    @Override // com.microsoft.krestsdk.models.sensor.SensorBase, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.microsoft.krestsdk.models.sensor.SensorBase, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mRate);
        dest.writeInt(this.mQuality);
    }
}

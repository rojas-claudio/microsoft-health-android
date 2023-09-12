package com.microsoft.krestsdk.models.sensor;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class DistanceSensor extends SensorBase implements Parcelable {
    public static final Parcelable.Creator<DistanceSensor> CREATOR = new Parcelable.Creator<DistanceSensor>() { // from class: com.microsoft.krestsdk.models.sensor.DistanceSensor.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DistanceSensor createFromParcel(Parcel in) {
            return new DistanceSensor(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DistanceSensor[] newArray(int size) {
            return new DistanceSensor[size];
        }
    };
    @SerializedName("Dis")
    private long mDistance;
    @SerializedName("DisPedo")
    private long mDistancePedometer;
    @SerializedName("DisGPS")
    private long mDisttanceGPS;
    @SerializedName("Pace")
    private long mPace;
    @SerializedName("Speed")
    private long mSpeed;

    public long getDistance() {
        return this.mDistance;
    }

    public void setDistance(long mDistance) {
        this.mDistance = mDistance;
    }

    public long getDistancePedometer() {
        return this.mDistancePedometer;
    }

    public void setDistancePedometer(long mDistancePedometer) {
        this.mDistancePedometer = mDistancePedometer;
    }

    public long getDisttanceGPS() {
        return this.mDisttanceGPS;
    }

    public void setDisttanceGPS(long mDisttanceGPS) {
        this.mDisttanceGPS = mDisttanceGPS;
    }

    public long getSpeed() {
        return this.mSpeed;
    }

    public void setSpeed(long mSpeed) {
        this.mSpeed = mSpeed;
    }

    public long getPace() {
        return this.mPace;
    }

    public void setPace(long mPace) {
        this.mPace = mPace;
    }

    protected DistanceSensor(Parcel in) {
        super(in);
        this.mDistance = in.readLong();
        this.mDistancePedometer = in.readLong();
        this.mDisttanceGPS = in.readLong();
        this.mSpeed = in.readLong();
        this.mPace = in.readLong();
    }

    @Override // com.microsoft.krestsdk.models.sensor.SensorBase, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.microsoft.krestsdk.models.sensor.SensorBase, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.mDistance);
        dest.writeLong(this.mDistancePedometer);
        dest.writeLong(this.mDisttanceGPS);
        dest.writeLong(this.mSpeed);
        dest.writeLong(this.mPace);
    }
}

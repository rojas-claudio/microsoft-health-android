package com.microsoft.krestsdk.models.sensor;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
/* loaded from: classes.dex */
public class StepsSensor extends SensorBase implements Parcelable {
    public static final Parcelable.Creator<StepsSensor> CREATOR = new Parcelable.Creator<StepsSensor>() { // from class: com.microsoft.krestsdk.models.sensor.StepsSensor.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StepsSensor createFromParcel(Parcel in) {
            return new StepsSensor(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public StepsSensor[] newArray(int size) {
            return new StepsSensor[size];
        }
    };
    @SerializedName(TelemetryConstants.Events.LogHomeTileTap.Dimensions.TILE_NAME_STEPS)
    private long mSteps;

    public StepsSensor() {
    }

    protected StepsSensor(Parcel in) {
        super(in);
        setSteps(in.readLong());
    }

    public long getSteps() {
        return this.mSteps;
    }

    public void setSteps(long mSteps) {
        this.mSteps = mSteps;
    }

    @Override // com.microsoft.krestsdk.models.sensor.SensorBase, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.microsoft.krestsdk.models.sensor.SensorBase, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(getSteps());
    }
}

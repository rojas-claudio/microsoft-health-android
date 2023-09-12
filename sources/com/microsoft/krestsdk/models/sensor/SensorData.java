package com.microsoft.krestsdk.models.sensor;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class SensorData implements Parcelable {
    public static final Parcelable.Creator<SensorData> CREATOR = new Parcelable.Creator<SensorData>() { // from class: com.microsoft.krestsdk.models.sensor.SensorData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SensorData createFromParcel(Parcel in) {
            return new SensorData(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SensorData[] newArray(int size) {
            return new SensorData[size];
        }
    };
    @SerializedName(TelemetryConstants.Events.LogHomeTileTap.Dimensions.TILE_NAME_CALORIES)
    private CaloriesSensor[] mCalories;
    @SerializedName("DeviceId")
    private String mDeviceId;
    @SerializedName("Distances")
    private DistanceSensor[] mDistances;
    @SerializedName("HeartRates")
    private HeartRateSensor[] mHeartRates;
    @SerializedName("LogStartUtcTime")
    private DateTime mLogStartUTCTime;
    @SerializedName("OperationId")
    private long mOperationId;
    @SerializedName(TelemetryConstants.Events.LogHomeTileTap.Dimensions.TILE_NAME_STEPS)
    private StepsSensor[] mSteps;
    @SerializedName("UploadTimeZoneOffset")
    private int mUploadTimeZoneOffset;

    public long getOperationId() {
        return this.mOperationId;
    }

    public void setOperationId(long mOperationId) {
        this.mOperationId = mOperationId;
    }

    public String getDeviceId() {
        return this.mDeviceId;
    }

    public void setDeviceId(String mDeviceId) {
        this.mDeviceId = mDeviceId;
    }

    public DateTime getLogStartUTCTime() {
        return this.mLogStartUTCTime;
    }

    public void setLogStartUTCTime(DateTime mLogStartUTCTime) {
        this.mLogStartUTCTime = mLogStartUTCTime;
    }

    public StepsSensor[] getSteps() {
        return this.mSteps;
    }

    public void setSteps(StepsSensor[] mSteps) {
        this.mSteps = mSteps;
    }

    public int getUploadTimeZoneOffset() {
        return this.mUploadTimeZoneOffset;
    }

    public void setUploadTimeZoneOffset(int mUploadTimeZoneOffset) {
        this.mUploadTimeZoneOffset = mUploadTimeZoneOffset;
    }

    public CaloriesSensor[] getCalories() {
        return this.mCalories;
    }

    public void setCalories(CaloriesSensor[] mCalories) {
        this.mCalories = mCalories;
    }

    public DistanceSensor[] getDistances() {
        return this.mDistances;
    }

    public void setDistances(DistanceSensor[] mDistances) {
        this.mDistances = mDistances;
    }

    public HeartRateSensor[] getHeartRates() {
        return this.mHeartRates;
    }

    public void setHeartRates(HeartRateSensor[] mHeartRates) {
        this.mHeartRates = mHeartRates;
    }

    public SensorData() {
    }

    protected SensorData(Parcel in) {
        this.mOperationId = in.readLong();
        this.mDeviceId = in.readString();
        this.mLogStartUTCTime = (DateTime) in.readValue(DateTime.class.getClassLoader());
        this.mUploadTimeZoneOffset = in.readInt();
        this.mSteps = (StepsSensor[]) in.createTypedArray(StepsSensor.CREATOR);
        this.mCalories = (CaloriesSensor[]) in.createTypedArray(CaloriesSensor.CREATOR);
        this.mDistances = (DistanceSensor[]) in.createTypedArray(DistanceSensor.CREATOR);
        this.mHeartRates = (HeartRateSensor[]) in.createTypedArray(HeartRateSensor.CREATOR);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mOperationId);
        dest.writeString(this.mDeviceId);
        dest.writeValue(this.mLogStartUTCTime);
        dest.writeValue(Integer.valueOf(this.mUploadTimeZoneOffset));
        dest.writeTypedArray(this.mSteps, flags);
        dest.writeTypedArray(this.mCalories, flags);
        dest.writeTypedArray(this.mDistances, flags);
        dest.writeTypedArray(this.mHeartRates, flags);
    }
}

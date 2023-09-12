package com.microsoft.krestsdk.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spannable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.utils.Formatter;
/* loaded from: classes.dex */
public class BikeEvent extends ExerciseEventBase implements MeasuredEvent, AltitudeEvent {
    public static final Parcelable.Creator<BikeEvent> CREATOR = new Parcelable.Creator<BikeEvent>() { // from class: com.microsoft.krestsdk.models.BikeEvent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BikeEvent createFromParcel(Parcel in) {
            return new BikeEvent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BikeEvent[] newArray(int size) {
            return new BikeEvent[size];
        }
    };
    private static final int GPS_STATE_ENABLED = 1;
    @SerializedName("AverageSpeed")
    private int mAverageSpeed;
    @SerializedName("GPSState")
    private int mGPSState;
    @SerializedName("MapPoints")
    private MeasuredEventMapPoint[] mMapPoints;
    @SerializedName("MaxAltitude")
    private int mMaxAltitude;
    @SerializedName("MaxSpeed")
    private int mMaxSpeed;
    @SerializedName("MinAltitude")
    private int mMinAltitude;
    @SerializedName("Sequences")
    private BikeEventSequence[] mSequences;
    @SerializedName("SplitDistance")
    private double mSplitDistance;
    @SerializedName("SplitGroupSize")
    private int mSplitGroupSize;
    @SerializedName("StepsTaken")
    private int mStepsTaken;
    @SerializedName("TotalAltitudeGain")
    private int mTotalAltitudeGain;
    @SerializedName("TotalAltitudeLoss")
    private int mTotalAltitudeLoss;
    @SerializedName("TotalDistance")
    private int mTotalDistance;
    @SerializedName("WaypointDistance")
    private int mWaypointDistance;

    public int getTotalDistance() {
        return this.mTotalDistance;
    }

    public BikeEvent() {
    }

    public boolean hasGpsData() {
        return this.mGPSState == 1;
    }

    public void setTotalDistance(int totalDistance) {
        this.mTotalDistance = totalDistance;
    }

    public int getStepsTaken() {
        return this.mStepsTaken;
    }

    @Override // com.microsoft.krestsdk.models.AltitudeEvent
    public int getTotalAltitudeGain() {
        return this.mTotalAltitudeGain;
    }

    @Override // com.microsoft.krestsdk.models.AltitudeEvent
    public int getTotalAltitudeLoss() {
        return this.mTotalAltitudeLoss;
    }

    @Override // com.microsoft.krestsdk.models.AltitudeEvent
    public int getMaxAltitude() {
        return this.mMaxAltitude;
    }

    @Override // com.microsoft.krestsdk.models.AltitudeEvent
    public int getMinAltitude() {
        return this.mMinAltitude;
    }

    public int getAverageSpeed() {
        return this.mAverageSpeed;
    }

    public int getMaxSpeed() {
        return this.mMaxSpeed;
    }

    public void setStepsTaken(int stepsTaken) {
        this.mStepsTaken = stepsTaken;
    }

    @Override // com.microsoft.krestsdk.models.MeasuredEvent
    public BikeEventSequence[] getSequences() {
        return this.mSequences;
    }

    public void setSequences(BikeEventSequence[] sequences) {
        this.mSequences = sequences;
    }

    public int getPace() {
        return (int) ((getDuration() * 1000) / (getTotalDistance() / 100000.0d));
    }

    @Override // com.microsoft.krestsdk.models.MeasuredEvent
    public MeasuredEventMapPoint[] getMapPoints() {
        return this.mMapPoints;
    }

    public void setMapPoints(MeasuredEventMapPoint[] mMapPoints) {
        this.mMapPoints = mMapPoints;
    }

    public int getGPSState() {
        return this.mGPSState;
    }

    public void setGPSState(int mGPSState) {
        this.mGPSState = mGPSState;
    }

    public void setSplitGroupSize(int splitGroupSize) {
        this.mSplitGroupSize = splitGroupSize;
    }

    public int getSplitGroupSize() {
        return this.mSplitGroupSize;
    }

    public double getSplitDistance() {
        return this.mSplitDistance;
    }

    public void setSplitDistance(double splitDistance) {
        this.mSplitDistance = splitDistance;
    }

    @Override // com.microsoft.krestsdk.models.ExerciseEventBase, com.microsoft.krestsdk.models.UserEvent
    public Spannable getMainMetric(Context context, boolean isMetric) {
        return getGPSState() == 1 ? Formatter.formatDistanceStat(context, getTotalDistance(), isMetric) : Formatter.formatCalories(context, getCaloriesBurned());
    }

    @Override // com.microsoft.krestsdk.models.ExerciseEventBase, com.microsoft.krestsdk.models.UserEvent
    public Spannable getMainMetric(Context context, int styleResId, boolean isMetric) {
        return getGPSState() == 1 ? Formatter.formatDistanceStat(context, styleResId, getTotalDistance(), isMetric) : Formatter.formatCalories(context, styleResId, getCaloriesBurned());
    }

    protected BikeEvent(Parcel in) {
        super(in);
        this.mTotalDistance = in.readInt();
        this.mStepsTaken = in.readInt();
        this.mSequences = (BikeEventSequence[]) in.createTypedArray(BikeEventSequence.CREATOR);
        this.mMapPoints = (MeasuredEventMapPoint[]) in.createTypedArray(MeasuredEventMapPoint.CREATOR);
        this.mSplitGroupSize = in.readInt();
        this.mTotalAltitudeGain = in.readInt();
        this.mTotalAltitudeLoss = in.readInt();
        this.mMaxAltitude = in.readInt();
        this.mMinAltitude = in.readInt();
        this.mSplitDistance = in.readDouble();
    }

    @Override // com.microsoft.krestsdk.models.ExerciseEventBase, com.microsoft.krestsdk.models.UserEvent, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mTotalDistance);
        dest.writeInt(this.mStepsTaken);
        dest.writeTypedArray(this.mSequences, flags);
        dest.writeTypedArray(this.mMapPoints, flags);
        dest.writeInt(this.mSplitGroupSize);
        dest.writeInt(this.mTotalAltitudeGain);
        dest.writeInt(this.mTotalAltitudeLoss);
        dest.writeInt(this.mMaxAltitude);
        dest.writeInt(this.mMinAltitude);
        dest.writeDouble(this.mSplitDistance);
    }
}

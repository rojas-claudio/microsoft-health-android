package com.microsoft.krestsdk.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spannable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.utils.Formatter;
/* loaded from: classes.dex */
public class RunEvent extends ExerciseEventBase implements MeasuredEvent, AltitudeEvent {
    public static final Parcelable.Creator<RunEvent> CREATOR = new Parcelable.Creator<RunEvent>() { // from class: com.microsoft.krestsdk.models.RunEvent.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RunEvent createFromParcel(Parcel in) {
            return new RunEvent(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RunEvent[] newArray(int size) {
            return new RunEvent[size];
        }
    };
    @SerializedName("MapPoints")
    private MeasuredEventMapPoint[] mMapPoints;
    @SerializedName("MaxAltitude")
    private int mMaxAltitude;
    @SerializedName("MinAltitude")
    private int mMinAltitude;
    @SerializedName("Pace")
    private int mPace;
    @SerializedName("Sequences")
    private RunEventSequence[] mSequences;
    @SerializedName("StepsTaken")
    private int mStepsTaken;
    @SerializedName("TotalAltitudeGain")
    private int mTotalAltitudeGain;
    @SerializedName("TotalAltitudeLoss")
    private int mTotalAltitudeLoss;
    @SerializedName("TotalDistance")
    private int mTotalDistance;

    public int getTotalDistance() {
        return this.mTotalDistance;
    }

    public RunEvent() {
    }

    public void setTotalDistance(int totalDistance) {
        this.mTotalDistance = totalDistance;
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

    public int getStepsTaken() {
        return this.mStepsTaken;
    }

    public void setStepsTaken(int stepsTaken) {
        this.mStepsTaken = stepsTaken;
    }

    @Override // com.microsoft.krestsdk.models.MeasuredEvent
    public RunEventSequence[] getSequences() {
        return this.mSequences;
    }

    public void setSequences(RunEventSequence[] sequences) {
        this.mSequences = sequences;
    }

    public int getPace() {
        return this.mPace;
    }

    public void setPace(int pace) {
        this.mPace = pace;
    }

    @Override // com.microsoft.krestsdk.models.MeasuredEvent
    public MeasuredEventMapPoint[] getMapPoints() {
        return this.mMapPoints;
    }

    public void setMapPoints(MeasuredEventMapPoint[] mMapPoints) {
        this.mMapPoints = mMapPoints;
    }

    @Override // com.microsoft.krestsdk.models.ExerciseEventBase, com.microsoft.krestsdk.models.UserEvent
    public Spannable getMainMetric(Context context, boolean isMetric) {
        return Formatter.formatDistanceStat(context, getTotalDistance(), isMetric);
    }

    @Override // com.microsoft.krestsdk.models.ExerciseEventBase, com.microsoft.krestsdk.models.UserEvent
    public Spannable getMainMetric(Context context, int styleResId, boolean isMetric) {
        return Formatter.formatDistanceStat(context, styleResId, getTotalDistance(), isMetric);
    }

    protected RunEvent(Parcel in) {
        super(in);
        this.mTotalDistance = in.readInt();
        this.mStepsTaken = in.readInt();
        this.mPace = in.readInt();
        this.mTotalAltitudeGain = in.readInt();
        this.mTotalAltitudeLoss = in.readInt();
        this.mMaxAltitude = in.readInt();
        this.mMinAltitude = in.readInt();
        this.mSequences = (RunEventSequence[]) in.createTypedArray(RunEventSequence.CREATOR);
        this.mMapPoints = (MeasuredEventMapPoint[]) in.createTypedArray(MeasuredEventMapPoint.CREATOR);
    }

    @Override // com.microsoft.krestsdk.models.ExerciseEventBase, com.microsoft.krestsdk.models.UserEvent, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mTotalDistance);
        dest.writeInt(this.mStepsTaken);
        dest.writeInt(this.mPace);
        dest.writeInt(this.mTotalAltitudeGain);
        dest.writeInt(this.mTotalAltitudeLoss);
        dest.writeInt(this.mMaxAltitude);
        dest.writeInt(this.mMinAltitude);
        dest.writeTypedArray(this.mSequences, flags);
        dest.writeTypedArray(this.mMapPoints, flags);
    }
}

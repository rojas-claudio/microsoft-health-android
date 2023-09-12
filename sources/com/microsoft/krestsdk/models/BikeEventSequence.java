package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class BikeEventSequence extends ExerciseEventSequence implements MeasuredEventSequence {
    public static final Parcelable.Creator<BikeEventSequence> CREATOR = new Parcelable.Creator<BikeEventSequence>() { // from class: com.microsoft.krestsdk.models.BikeEventSequence.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BikeEventSequence createFromParcel(Parcel in) {
            return new BikeEventSequence(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BikeEventSequence[] newArray(int size) {
            return new BikeEventSequence[size];
        }
    };
    @SerializedName("Order")
    private int mOrder;
    @SerializedName("AltitudeGain")
    private int mSplitAltitudeGain;
    @SerializedName("AltitudeLoss")
    private int mSplitAltitudeLoss;
    @SerializedName("SplitDistance")
    private double mSplitDistance;
    @SerializedName("SplitSpeed")
    private int mSplitSpeed;
    @SerializedName("TotalDistance")
    private int mTotalDistance;

    public BikeEventSequence() {
    }

    public int getTotalDistance() {
        return this.mTotalDistance;
    }

    public void setTotalDistance(int totalDistance) {
        this.mTotalDistance = totalDistance;
    }

    public void setSplitDistance(int distance) {
        this.mSplitDistance = distance;
    }

    @Override // com.microsoft.krestsdk.models.MeasuredEventSequence
    public double getSplitDistance() {
        return this.mSplitDistance;
    }

    public void setSplitAltitudeGain(int gain) {
        this.mSplitAltitudeGain = gain;
    }

    public int getSplitAltitudeGain() {
        return this.mSplitAltitudeGain;
    }

    public void setSplitAltitudeLoss(int loss) {
        this.mSplitAltitudeLoss = loss;
    }

    public int getSplitAltitudeLoss() {
        return this.mSplitAltitudeLoss;
    }

    public void setSplitSpeed(int splitSpeed) {
        this.mSplitSpeed = splitSpeed;
    }

    public int getSplitSpeed() {
        return this.mSplitSpeed;
    }

    public void setOrder(int order) {
        this.mOrder = order;
    }

    public int getOrder() {
        return this.mOrder;
    }

    protected BikeEventSequence(Parcel in) {
        super(in);
        this.mTotalDistance = in.readInt();
        this.mSplitDistance = in.readDouble();
        this.mSplitAltitudeGain = in.readInt();
        this.mSplitAltitudeLoss = in.readInt();
        this.mSplitSpeed = in.readInt();
        this.mOrder = in.readInt();
    }

    @Override // com.microsoft.krestsdk.models.ExerciseEventSequence, com.microsoft.krestsdk.models.UserEventSequence, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mTotalDistance);
        dest.writeDouble(this.mSplitDistance);
        dest.writeInt(this.mSplitAltitudeGain);
        dest.writeInt(this.mSplitAltitudeLoss);
        dest.writeInt(this.mSplitSpeed);
        dest.writeInt(this.mOrder);
    }

    @Override // com.microsoft.krestsdk.models.MeasuredEventSequence
    public int getSplitPace() {
        return (int) ((getDuration() * 1000) / (getSplitDistance() / 100000.0d));
    }
}

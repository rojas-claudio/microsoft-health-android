package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class RunEventSequence extends ExerciseEventSequence implements MeasuredEventSequence {
    public static final Parcelable.Creator<RunEventSequence> CREATOR = new Parcelable.Creator<RunEventSequence>() { // from class: com.microsoft.krestsdk.models.RunEventSequence.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RunEventSequence createFromParcel(Parcel in) {
            return new RunEventSequence(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RunEventSequence[] newArray(int size) {
            return new RunEventSequence[size];
        }
    };
    @SerializedName("Order")
    private int mOrder;
    @SerializedName("SplitDistance")
    private double mSplitDistance;
    @SerializedName("SplitPace")
    private int mSplitPace;
    @SerializedName("StepsTaken")
    private int mStepsTaken;
    @SerializedName("TotalDistance")
    private int mTotalDistance;

    public RunEventSequence() {
    }

    public int getStepsTaken() {
        return this.mStepsTaken;
    }

    public void setStepsTaken(int stepsTaken) {
        this.mStepsTaken = stepsTaken;
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

    @Override // com.microsoft.krestsdk.models.MeasuredEventSequence
    public int getSplitPace() {
        return this.mSplitPace;
    }

    public void setSplitPace(int splitPace) {
        this.mSplitPace = splitPace;
    }

    public void setOrder(int order) {
        this.mOrder = order;
    }

    public int getOrder() {
        return this.mOrder;
    }

    protected RunEventSequence(Parcel in) {
        super(in);
        this.mStepsTaken = in.readInt();
        this.mTotalDistance = in.readInt();
        this.mSplitDistance = in.readDouble();
        this.mSplitPace = in.readInt();
        this.mOrder = in.readInt();
    }

    @Override // com.microsoft.krestsdk.models.ExerciseEventSequence, com.microsoft.krestsdk.models.UserEventSequence, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mStepsTaken);
        dest.writeInt(this.mTotalDistance);
        dest.writeDouble(this.mSplitDistance);
        dest.writeInt(this.mSplitPace);
        dest.writeInt(this.mOrder);
    }
}

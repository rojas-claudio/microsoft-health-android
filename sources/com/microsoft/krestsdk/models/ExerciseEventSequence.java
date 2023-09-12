package com.microsoft.krestsdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class ExerciseEventSequence extends UserEventSequence {
    public static final Parcelable.Creator<ExerciseEventSequence> CREATOR = new Parcelable.Creator<ExerciseEventSequence>() { // from class: com.microsoft.krestsdk.models.ExerciseEventSequence.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ExerciseEventSequence createFromParcel(Parcel in) {
            return new ExerciseEventSequence(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ExerciseEventSequence[] newArray(int size) {
            return new ExerciseEventSequence[size];
        }
    };
    @SerializedName("AverageVO2")
    private double mAverageVO2;
    @SerializedName("CaloriesFromCarbs")
    private float mCaloriesFromCarbs;
    @SerializedName("CaloriesFromFat")
    private float mCaloriesFromFat;
    @SerializedName("HRZoneClassification")
    private String mHRZoneClassification;
    @SerializedName("HealthIndex")
    private double mHealthIndex;
    @SerializedName("RelaxationPercentage")
    private double mRelaxationPercentage;
    @SerializedName("RelaxationTime")
    private double mRelaxationTime;
    @SerializedName("StressPercentage")
    private double mStressPercentage;
    @SerializedName("StressTime")
    private double mStressTime;
    @SerializedName("TrainingEffect")
    private double mTrainingEffect;

    public ExerciseEventSequence() {
    }

    public float getCaloriesFromCarbs() {
        return this.mCaloriesFromCarbs;
    }

    public void setCaloriesFromCarbs(float caloriesFromCarbs) {
        this.mCaloriesFromCarbs = caloriesFromCarbs;
    }

    public float getCaloriesFromFat() {
        return this.mCaloriesFromFat;
    }

    public void setCaloriesFromFat(float caloriesFromFat) {
        this.mCaloriesFromFat = caloriesFromFat;
    }

    public double getRelaxationPercentage() {
        return this.mRelaxationPercentage;
    }

    public void setRelaxationPercentage(double relaxationPercentage) {
        this.mRelaxationPercentage = relaxationPercentage;
    }

    public double getRelaxationTime() {
        return this.mRelaxationTime;
    }

    public void setRelaxationTime(double relaxationTime) {
        this.mRelaxationTime = relaxationTime;
    }

    public double getStressPercentage() {
        return this.mStressPercentage;
    }

    public void setStressPercentage(double stressPercentage) {
        this.mStressPercentage = stressPercentage;
    }

    public double getStressTime() {
        return this.mStressTime;
    }

    public void setStressTime(double stressTime) {
        this.mStressTime = stressTime;
    }

    public double getAverageVO2() {
        return this.mAverageVO2;
    }

    public void setAverageVO2(double averageVO2) {
        this.mAverageVO2 = averageVO2;
    }

    public double getTrainingEffect() {
        return this.mTrainingEffect;
    }

    public void setTrainingEffect(double trainingEffect) {
        this.mTrainingEffect = trainingEffect;
    }

    public double getHealthIndex() {
        return this.mHealthIndex;
    }

    public void setHealthIndex(double healthIndex) {
        this.mHealthIndex = healthIndex;
    }

    public String getHRZoneClassification() {
        return this.mHRZoneClassification;
    }

    public void setHRZoneClassification(String HRZoneClassification) {
        this.mHRZoneClassification = HRZoneClassification;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ExerciseEventSequence(Parcel in) {
        super(in);
        this.mCaloriesFromCarbs = in.readFloat();
        this.mCaloriesFromFat = in.readFloat();
        this.mRelaxationPercentage = in.readDouble();
        this.mRelaxationTime = in.readDouble();
        this.mStressPercentage = in.readDouble();
        this.mStressTime = in.readDouble();
        this.mAverageVO2 = in.readDouble();
        this.mTrainingEffect = in.readDouble();
        this.mHealthIndex = in.readDouble();
        this.mHRZoneClassification = in.readString();
    }

    @Override // com.microsoft.krestsdk.models.UserEventSequence, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeFloat(this.mCaloriesFromCarbs);
        dest.writeFloat(this.mCaloriesFromFat);
        dest.writeDouble(this.mRelaxationPercentage);
        dest.writeDouble(this.mRelaxationTime);
        dest.writeDouble(this.mStressPercentage);
        dest.writeDouble(this.mStressTime);
        dest.writeDouble(this.mAverageVO2);
        dest.writeDouble(this.mTrainingEffect);
        dest.writeDouble(this.mHealthIndex);
        dest.writeString(this.mHRZoneClassification);
    }
}

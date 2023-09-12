package com.microsoft.krestsdk.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spannable;
import com.google.gson.annotations.SerializedName;
import com.microsoft.kapp.comparators.UserActivityComparator;
import com.microsoft.kapp.utils.Formatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public class ExerciseEventBase extends UserEvent {
    public static final Parcelable.Creator<ExerciseEventBase> CREATOR = new Parcelable.Creator<ExerciseEventBase>() { // from class: com.microsoft.krestsdk.models.ExerciseEventBase.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ExerciseEventBase createFromParcel(Parcel in) {
            return new ExerciseEventBase(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ExerciseEventBase[] newArray(int size) {
            return new ExerciseEventBase[size];
        }
    };
    @SerializedName("AverageHeartRate")
    private int mAverageHeartRate;
    @SerializedName("AverageVO2")
    private double mAverageVO2;
    @SerializedName("EECHCalories")
    private double mEECHCalories;
    @SerializedName("EEFatCalories")
    private double mEEFatCalories;
    @SerializedName("EETotalCalories")
    private double mEETotalCalories;
    @SerializedName("FinishHeartRate")
    private int mFinishHeartRate;
    @SerializedName("FitnessBenefit")
    private String mFitnessBenefit;
    @SerializedName("FitnessBenefitMsg")
    private String mFitnessBenefitMsg;
    @SerializedName("HRZoneClassification")
    private String mHRZoneClassification;
    @SerializedName("HeartRateZones")
    private HRZones mHRZones;
    @SerializedName("HealthIndex")
    private double mHealthIndex;
    @SerializedName("LowestHeartRate")
    private int mLowestHeartRate;
    @SerializedName("PeakHeartRate")
    private int mPeakHeartRate;
    @SerializedName("RecoveryHeartRate1Minute")
    private int mRecoveryHeartRate1Minute;
    @SerializedName("RecoveryHeartRate2Minute")
    private int mRecoveryHeartRate2Minute;
    @SerializedName("RecoveryTime")
    private int mRecoveryTime;
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

    public ExerciseEventBase() {
    }

    public UserActivity[] getUpdatedInfoSequenceWithPauseTime() {
        UserActivity[] activities = getInfo();
        List<UserActivity> activityList = Arrays.asList(activities);
        Collections.sort(activityList, new UserActivityComparator());
        return (UserActivity[]) activityList.toArray(new UserActivity[activityList.size()]);
    }

    public double getEETotalCalories() {
        return this.mEETotalCalories;
    }

    public void setEETotalCalories(double EETotalCalories) {
        this.mEETotalCalories = EETotalCalories;
    }

    public double getEECHCalories() {
        return this.mEECHCalories;
    }

    public void setEECHCalories(double EECHCalories) {
        this.mEECHCalories = EECHCalories;
    }

    public double getEEFatCalories() {
        return this.mEEFatCalories;
    }

    public void setEEFatCalories(double EEFatCalories) {
        this.mEEFatCalories = EEFatCalories;
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

    public HRZones getHRZones() {
        return this.mHRZones;
    }

    public void setHRZones(HRZones HRZones) {
        this.mHRZones = HRZones;
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

    public int getAverageHeartRate() {
        return this.mAverageHeartRate;
    }

    public void setAverageHeartRate(int averageHeartRate) {
        this.mAverageHeartRate = averageHeartRate;
    }

    public int getLowestHeartRate() {
        return this.mLowestHeartRate;
    }

    public void setLowestHeartRate(int lowestHeartRate) {
        this.mLowestHeartRate = lowestHeartRate;
    }

    public int getPeakHeartRate() {
        return this.mPeakHeartRate;
    }

    public void setPeakHeartRate(int peakHeartRate) {
        this.mPeakHeartRate = peakHeartRate;
    }

    public int getFinishHeartRate() {
        return this.mFinishHeartRate;
    }

    public void setFinishHeartRate(int finishHeartRate) {
        this.mFinishHeartRate = finishHeartRate;
    }

    public int getRecoveryTime() {
        return this.mRecoveryTime;
    }

    public void setRecoveryTime(int mRecoveryTime) {
        this.mRecoveryTime = mRecoveryTime;
    }

    public int getRecoveryHeartRate1Minute() {
        return this.mRecoveryHeartRate1Minute;
    }

    public void setRecoveryHeartRate1Minute(int mRecoveryHeartRate1Minute) {
        this.mRecoveryHeartRate1Minute = mRecoveryHeartRate1Minute;
    }

    public int getRecoveryHeartRate2Minute() {
        return this.mRecoveryHeartRate2Minute;
    }

    public void setRecoveryHeartRate2Minute(int mRecoveryHeartRate2Minute) {
        this.mRecoveryHeartRate2Minute = mRecoveryHeartRate2Minute;
    }

    public String getFitnessBenefit() {
        return this.mFitnessBenefit;
    }

    public void setFitnessBenefit(String fitnessBenefit) {
        this.mFitnessBenefit = fitnessBenefit;
    }

    public String getFitnessBenefitMsg() {
        return this.mFitnessBenefitMsg;
    }

    public void setFitnessBenefitMsg(String fitnessBenefitMsg) {
        this.mFitnessBenefitMsg = fitnessBenefitMsg;
    }

    @Override // com.microsoft.krestsdk.models.UserEvent
    public Spannable getMainMetric(Context context, boolean isMetric) {
        return Formatter.formatCalories(context, getCaloriesBurned());
    }

    @Override // com.microsoft.krestsdk.models.UserEvent
    public Spannable getMainMetric(Context context, int styleResId, boolean isMetric) {
        return Formatter.formatCalories(context, styleResId, getCaloriesBurned());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ExerciseEventBase(Parcel in) {
        super(in);
        this.mEETotalCalories = in.readDouble();
        this.mEECHCalories = in.readDouble();
        this.mEEFatCalories = in.readDouble();
        this.mRelaxationPercentage = in.readDouble();
        this.mRelaxationTime = in.readDouble();
        this.mStressPercentage = in.readDouble();
        this.mStressTime = in.readDouble();
        this.mAverageVO2 = in.readDouble();
        this.mTrainingEffect = in.readDouble();
        this.mHealthIndex = in.readDouble();
        this.mHRZoneClassification = in.readString();
        this.mAverageHeartRate = in.readInt();
        this.mLowestHeartRate = in.readInt();
        this.mPeakHeartRate = in.readInt();
        this.mFinishHeartRate = in.readInt();
        this.mRecoveryHeartRate1Minute = in.readInt();
        this.mRecoveryHeartRate2Minute = in.readInt();
        this.mRecoveryTime = in.readInt();
        this.mFitnessBenefit = in.readString();
        this.mFitnessBenefitMsg = in.readString();
    }

    @Override // com.microsoft.krestsdk.models.UserEvent, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeDouble(this.mEETotalCalories);
        dest.writeDouble(this.mEECHCalories);
        dest.writeDouble(this.mEEFatCalories);
        dest.writeDouble(this.mRelaxationPercentage);
        dest.writeDouble(this.mRelaxationTime);
        dest.writeDouble(this.mStressPercentage);
        dest.writeDouble(this.mStressTime);
        dest.writeDouble(this.mAverageVO2);
        dest.writeDouble(this.mTrainingEffect);
        dest.writeDouble(this.mHealthIndex);
        dest.writeString(this.mHRZoneClassification);
        dest.writeInt(this.mAverageHeartRate);
        dest.writeInt(this.mLowestHeartRate);
        dest.writeInt(this.mPeakHeartRate);
        dest.writeInt(this.mFinishHeartRate);
        dest.writeInt(this.mRecoveryHeartRate1Minute);
        dest.writeInt(this.mRecoveryHeartRate2Minute);
        dest.writeInt(this.mRecoveryTime);
        dest.writeString(this.mFitnessBenefit);
        dest.writeString(this.mFitnessBenefitMsg);
    }
}

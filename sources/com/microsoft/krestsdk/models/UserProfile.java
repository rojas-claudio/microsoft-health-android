package com.microsoft.krestsdk.models;

import com.google.gson.annotations.SerializedName;
/* loaded from: classes.dex */
public class UserProfile {
    @SerializedName("BestRunDistance")
    private int mBestRunDistance;
    @SerializedName("BestRunPace")
    private int mBestRunPace;
    @SerializedName("CaloriesGoalForTheDay")
    private int mCaloriesGoalForTheDay;
    @SerializedName("PreferredLocaleName")
    private int mPreferredLocaleName;
    @SerializedName("StepGoalForTheDay")
    private int mStepGoalForTheDay;
    @SerializedName("StrideLength")
    private int mStrideLength;
    @SerializedName("StridesPerMinute")
    private int mStridesPerMinute;
    @SerializedName("TimeZoneOffset")
    private int mTimeZoneOffset;

    public int getStrideLength() {
        return this.mStrideLength;
    }

    public void setStrideLength(int strideLength) {
        this.mStrideLength = strideLength;
    }

    public int getStridesPerMinute() {
        return this.mStridesPerMinute;
    }

    public void setStridesPerMinute(int stridesPerMinute) {
        this.mStridesPerMinute = stridesPerMinute;
    }

    public int getPreferredLocaleName() {
        return this.mPreferredLocaleName;
    }

    public void setPreferredLocaleName(int preferredLocaleName) {
        this.mPreferredLocaleName = preferredLocaleName;
    }

    public int getBestRunPace() {
        return this.mBestRunPace;
    }

    public void setBestRunPace(int bestRunPace) {
        this.mBestRunPace = bestRunPace;
    }

    public int getBestRunDistance() {
        return this.mBestRunDistance;
    }

    public void setBestRunDistance(int bestRunDistance) {
        this.mBestRunDistance = bestRunDistance;
    }

    public int getStepGoalForTheDay() {
        return this.mStepGoalForTheDay;
    }

    public void setStepGoalForTheDay(int stepGoalForTheDay) {
        this.mStepGoalForTheDay = stepGoalForTheDay;
    }

    public int getCaloriesGoalForTheDay() {
        return this.mCaloriesGoalForTheDay;
    }

    public void setCaloriesGoalForTheDay(int caloriesGoalForTheDay) {
        this.mCaloriesGoalForTheDay = caloriesGoalForTheDay;
    }

    public int getTimeZoneOffset() {
        return this.mTimeZoneOffset;
    }

    public void setTimeZoneOffset(int timeZoneOffset) {
        this.mTimeZoneOffset = timeZoneOffset;
    }
}

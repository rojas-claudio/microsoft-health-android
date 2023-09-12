package com.microsoft.kapp.services.healthandfitness.models;

import com.google.gson.annotations.SerializedName;
import com.microsoft.krestsdk.models.CompletionType;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class WorkoutExercise implements Serializable {
    private static final Pattern mRepsTimePattern = Pattern.compile("(.*)s$");
    private static final long serialVersionUID = 3325262326664247905L;
    @SerializedName("no")
    private String mCode;
    @SerializedName("id")
    private String mId;
    @SerializedName("IsUseCustomeryUnits")
    private boolean mIsUseCustomeryUnits;
    @SerializedName("kcompletiontype")
    private int mKCompletionType;
    @SerializedName("kcompletionvalue")
    private int mKCompletionValue;
    @SerializedName("kissupportedcounting")
    private boolean mKIsSupportedCounting;
    @SerializedName("kshortname")
    private String mKShortName;
    @SerializedName("kshowinterstitial")
    private boolean mKShowInterstitial;
    @SerializedName("exname")
    private String mName;
    @SerializedName("repsdur")
    private String mRepsDuration;
    @SerializedName("repstimes")
    private String mRepsTimes;
    @SerializedName("rest")
    private String mRestSeconds;
    @SerializedName("sets")
    private String mSets;
    @SerializedName("thumbnail")
    private String mThumbnail;
    @SerializedName("videoid")
    private String mVideoId;

    public String getId() {
        return this.mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public boolean getShowInterstitial() {
        return this.mKShowInterstitial;
    }

    public void setShowInterstitial(boolean showInterstitial) {
        this.mKShowInterstitial = showInterstitial;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getRepsTimes() {
        return this.mRepsTimes;
    }

    public void setRepsTimes(String mRepsTimes) {
        this.mRepsTimes = mRepsTimes;
    }

    public String getRepsDuration() {
        return this.mRepsDuration;
    }

    public void setRepsDuration(String mRepsDuration) {
        this.mRepsDuration = mRepsDuration;
    }

    public String getRestSeconds() {
        return this.mRestSeconds;
    }

    public void setRestSeconds(String mRest) {
        this.mRestSeconds = mRest;
    }

    public String getSets() {
        return this.mSets;
    }

    public void setSets(String mSets) {
        this.mSets = mSets;
    }

    public String getCode() {
        return this.mCode;
    }

    public void setCode(String mCode) {
        this.mCode = mCode;
    }

    public String getVideoId() {
        return this.mVideoId;
    }

    public void setVideoId(String videoId) {
        this.mVideoId = videoId;
    }

    public String getExerciseReps() {
        if (this.mRepsTimes != null) {
            Matcher matcher = mRepsTimePattern.matcher(this.mRepsTimes);
            if (!matcher.matches()) {
                return this.mRepsTimes;
            }
        }
        return null;
    }

    public String getExerciseTimeSeconds() {
        if (this.mRepsTimes != null) {
            Matcher matcher = mRepsTimePattern.matcher(this.mRepsTimes);
            if (matcher.matches()) {
                return matcher.group(1);
            }
        } else if (this.mRepsDuration != null) {
            Matcher matcher2 = mRepsTimePattern.matcher(this.mRepsDuration);
            if (matcher2.matches()) {
                return matcher2.group(1);
            }
        }
        return null;
    }

    public CompletionType getCompletionType() {
        return CompletionType.valueOf(this.mKCompletionType);
    }

    public void setCompletionType(CompletionType type) {
        this.mKCompletionType = type.ordinal();
    }

    public int getCompletionValue() {
        return this.mKCompletionValue;
    }

    public void setCompletionValue(int value) {
        this.mKCompletionValue = value;
    }

    public boolean getKIsSupportedCounting() {
        return this.mKIsSupportedCounting;
    }

    public void setKIsSupportedCounting(boolean isSupportedCounting) {
        this.mKIsSupportedCounting = isSupportedCounting;
    }

    public String getThumbnail() {
        return this.mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.mThumbnail = thumbnail;
    }

    public boolean getIsUseCustomeryUnits() {
        return this.mIsUseCustomeryUnits;
    }

    public void setIsUseCustomeryUnits(boolean isUseCustomeryUnits) {
        this.mIsUseCustomeryUnits = isUseCustomeryUnits;
    }
}

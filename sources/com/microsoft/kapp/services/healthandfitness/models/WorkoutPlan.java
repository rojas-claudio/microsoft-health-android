package com.microsoft.kapp.services.healthandfitness.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
/* loaded from: classes.dex */
public class WorkoutPlan implements Serializable {
    private static final long serialVersionUID = -3283956804823160454L;
    @SerializedName("bdyprts")
    private String[] mBodyPart;
    @SerializedName("lvl")
    private String mDifficultyLevel;
    @SerializedName("dur")
    private String mDuration;
    @SerializedName("eqpmts")
    private String[] mEquipment;
    @SerializedName(WorkoutSummary.GOAL)
    private String[] mGoal;
    @SerializedName("desc")
    private String mHTMLDescription;
    @SerializedName("path")
    private String mHeroImageURL;
    @SerializedName("how")
    private String mHow;
    @SerializedName("id")
    private String mId;
    @SerializedName(WorkoutSummary.NAME)
    private String mName;
    @SerializedName("safety")
    private String mSafetyCaveats;
    @SerializedName("dtls")
    private WorkoutStep[] mSteps;
    @SerializedName("type")
    private String mType;
    @SerializedName("vids")
    private WorkoutVideos mVideos;
    @SerializedName("wklist")
    private WorkoutWeeklySchedule[] mWeeklyList;

    public String getId() {
        return this.mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public WorkoutStep[] getSteps() {
        return this.mSteps;
    }

    public void setSteps(WorkoutStep[] steps) {
        this.mSteps = steps;
    }

    public WorkoutVideos getVideos() {
        return this.mVideos;
    }

    public void setVideos(WorkoutVideos mWorkoutVideos) {
        this.mVideos = mWorkoutVideos;
    }

    public String[] getGoal() {
        return this.mGoal;
    }

    public void setGoal(String[] mGoal) {
        this.mGoal = mGoal;
    }

    public String getDuration() {
        return this.mDuration;
    }

    public void setDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    public String[] getBodyPart() {
        return this.mBodyPart;
    }

    public void setBodyPart(String[] mBodyPart) {
        this.mBodyPart = mBodyPart;
    }

    public String[] getEquipment() {
        return this.mEquipment;
    }

    public void setEquipment(String[] mEquipment) {
        this.mEquipment = mEquipment;
    }

    public String getDifficultyLevel() {
        return this.mDifficultyLevel;
    }

    public void setDifficultyLevel(String mDifficultyLevel) {
        this.mDifficultyLevel = mDifficultyLevel;
    }

    public WorkoutWeeklySchedule[] getWeeklyList() {
        return this.mWeeklyList;
    }

    public void setWeeklyList(WorkoutWeeklySchedule[] mWeeklyList) {
        this.mWeeklyList = mWeeklyList;
    }

    public String getHTMLDescription() {
        return this.mHTMLDescription;
    }

    public void setHTMLDescription(String mDescription) {
        this.mHTMLDescription = mDescription;
    }

    public String getHow() {
        return this.mHow;
    }

    public void setHow(String mHow) {
        this.mHow = mHow;
    }

    public String getHeroImageURL() {
        return this.mHeroImageURL;
    }

    public void setHeroImageURL(String mHeroImage) {
        this.mHeroImageURL = mHeroImage;
    }

    public String getSafetyCaveats() {
        return this.mSafetyCaveats;
    }

    public void setSafetyCaveats(String mSafetyCaveats) {
        this.mSafetyCaveats = mSafetyCaveats;
    }

    public String getType() {
        return this.mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }
}

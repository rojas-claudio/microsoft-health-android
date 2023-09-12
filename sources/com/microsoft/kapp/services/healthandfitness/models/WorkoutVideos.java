package com.microsoft.kapp.services.healthandfitness.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
/* loaded from: classes.dex */
public class WorkoutVideos implements Serializable {
    private static final long serialVersionUID = 2321746398646924648L;
    @SerializedName("bird'seye")
    private String mBirdsEyeView;
    @SerializedName("frnt")
    private String mFront;
    @SerializedName("prfle")
    private String mProfile;
    @SerializedName("rear")
    private String mRearView;
    @SerializedName("3fourth")
    private String mThreeFourthView;

    public String getFront() {
        return this.mFront;
    }

    public void setFront(String mFront) {
        this.mFront = mFront;
    }

    public String getThreeFourthView() {
        return this.mThreeFourthView;
    }

    public void setThreeFourthView(String mThreeFourthView) {
        this.mThreeFourthView = mThreeFourthView;
    }

    public String getProfile() {
        return this.mProfile;
    }

    public void setProfile(String mProfile) {
        this.mProfile = mProfile;
    }

    public String getRearView() {
        return this.mRearView;
    }

    public void setRearView(String mRearView) {
        this.mRearView = mRearView;
    }

    public String getBirdsEyeView() {
        return this.mBirdsEyeView;
    }

    public void setBirdsEyeView(String mBirdsEyeView) {
        this.mBirdsEyeView = mBirdsEyeView;
    }
}

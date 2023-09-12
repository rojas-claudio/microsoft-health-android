package com.microsoft.kapp.models.golf;

import com.microsoft.krestsdk.models.GolfCourseType;
/* loaded from: classes.dex */
public class GolfSearchResultItem {
    private String mCity;
    private String mCourseId;
    private GolfCourseType mCourseType;
    private long mDistance;
    private boolean mIsDistanceAvailable;
    private String mName;
    private int mNumberOfHoles;

    public String getCity() {
        return this.mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }

    public long getDistance() {
        return this.mDistance;
    }

    public void setDistance(long mDistance) {
        this.mDistance = mDistance;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getNumberOfHoles() {
        return this.mNumberOfHoles;
    }

    public void setNumberOfHoles(int mNumberOfHoles) {
        this.mNumberOfHoles = mNumberOfHoles;
    }

    public GolfCourseType getCourseType() {
        return this.mCourseType;
    }

    public void setCourseType(GolfCourseType mCourseType) {
        this.mCourseType = mCourseType;
    }

    public String getCourseId() {
        return this.mCourseId;
    }

    public void setCourseId(String mCourseId) {
        this.mCourseId = mCourseId;
    }

    public boolean isDistanceAvailable() {
        return this.mIsDistanceAvailable;
    }

    public void setIsDistanceAvailable(boolean isDistanceAvailable) {
        this.mIsDistanceAvailable = isDistanceAvailable;
    }
}

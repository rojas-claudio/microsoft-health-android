package com.microsoft.kapp.models;
/* loaded from: classes.dex */
public class SplitSummary {
    private int mAvgHeartBeat;
    private EventPaceLevel mEventPaceLevel;
    private TimeSpan mLaptime;
    private double mSplitDistance;
    private SplitType mSplitType = SplitType.NORMAL;

    /* loaded from: classes.dex */
    public enum SplitType {
        NORMAL,
        SLOWEST,
        FASTEST
    }

    public SplitSummary(double splitDistance, TimeSpan lapTime, int averageHeartRate, EventPaceLevel paceLevel) {
        this.mSplitDistance = splitDistance;
        this.mLaptime = lapTime;
        this.mAvgHeartBeat = averageHeartRate;
        this.mEventPaceLevel = paceLevel;
    }

    public double getSplitDistance() {
        return this.mSplitDistance;
    }

    public TimeSpan getLapTime() {
        return this.mLaptime;
    }

    public int getAverageHeartBeat() {
        return this.mAvgHeartBeat;
    }

    public EventPaceLevel getEventPaceLevel() {
        return this.mEventPaceLevel;
    }

    public void setPaceLevel(EventPaceLevel paceLevel) {
        this.mEventPaceLevel = paceLevel;
    }

    public SplitType getSplitType() {
        return this.mSplitType;
    }

    public void setSplitType(SplitType splitType) {
        this.mSplitType = splitType;
    }
}

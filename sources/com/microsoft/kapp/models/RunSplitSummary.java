package com.microsoft.kapp.models;

import android.content.Context;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.ConversionUtils;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.krestsdk.models.RunEventSequence;
/* loaded from: classes.dex */
public class RunSplitSummary {
    private int mAvgHeartBeat;
    private String mDistance;
    private EventPaceLevel mEventPaceLevel;
    private String mHeartRate;
    private TimeSpan mLaptime;
    private String mPace;
    private double mSplitDistance;
    private SplitType mSplitType = SplitType.NORMAL;
    private double mTotalDistance;

    /* loaded from: classes.dex */
    public enum SplitType {
        NORMAL,
        SLOWEST,
        FASTEST
    }

    public RunSplitSummary(Context context, RunEventSequence sequence, boolean isMetric) {
        this.mSplitDistance = sequence.getSplitDistance();
        this.mTotalDistance = sequence.getTotalDistance();
        this.mLaptime = TimeSpan.fromMilliseconds(isMetric ? sequence.getSplitPace() : sequence.getSplitPace() * 1.60934d);
        this.mAvgHeartBeat = sequence.getAverageHeartRate();
        this.mEventPaceLevel = EventPaceLevel.NONE;
        this.mDistance = Formatter.formatDistanceSplit(context, this.mTotalDistance, isMetric);
        this.mPace = this.mLaptime.formatTimePrimeNotation(context);
        this.mHeartRate = getAverageHeartBeat() == 0 ? context.getResources().getString(R.string.no_value) : Formatter.formatHR(getAverageHeartBeat()).toString();
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

    public double getTotalDistance() {
        return this.mTotalDistance;
    }

    public SplitType getSplitType() {
        return this.mSplitType;
    }

    public void setSplitType(SplitType splitType) {
        this.mSplitType = splitType;
    }

    public String getDistance() {
        return this.mDistance;
    }

    public String getPace() {
        return this.mPace;
    }

    public String getHeartRate() {
        return this.mHeartRate;
    }

    public boolean isFullSplit(boolean isMetric) {
        double totalDistance = isMetric ? ConversionUtils.CentimetersToKilometers(this.mTotalDistance) : ConversionUtils.CentimetersToMiles(this.mTotalDistance);
        double fractionValue = totalDistance % 1.0d;
        return fractionValue >= 0.99d;
    }
}

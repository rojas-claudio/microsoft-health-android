package com.microsoft.kapp.models;

import android.content.Context;
import android.text.Spannable;
import com.microsoft.kapp.R;
import com.microsoft.kapp.models.RunSplitSummary;
import com.microsoft.kapp.utils.ConversionUtils;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.krestsdk.models.BikeEventSequence;
/* loaded from: classes.dex */
public class BikeSplitSummary {
    private String mDistance;
    private int mDuration;
    private Spannable mElevation;
    private int mElevationChange;
    private int mElevationChangeCM;
    private boolean mIsEmpty;
    private boolean mIsEstimation;
    private boolean mIsFullSplit;
    private boolean mIsMetric;
    private Spannable mSpeed;
    private int mSpeedValue;
    private double mSplitDistance;
    private RunSplitSummary.SplitType mSplitType;
    private double mTotalDistance;

    public BikeSplitSummary(Context context, BikeEventSequence sequence, boolean isMetric) {
        this(context, sequence, false, isMetric);
    }

    public BikeSplitSummary(Context context, BikeEventSequence sequence, boolean isEmpty, boolean isMetric) {
        this.mSplitType = RunSplitSummary.SplitType.NORMAL;
        this.mIsFullSplit = true;
        this.mDistance = Formatter.formatDistanceSplit(context, sequence.getTotalDistance(), isMetric);
        this.mDuration = sequence.getDuration();
        this.mIsMetric = isMetric;
        int elevationChangeCM = sequence.getSplitAltitudeGain() - sequence.getSplitAltitudeLoss();
        this.mElevationChange = (int) (isMetric ? ConversionUtils.CentimetersToMeters(elevationChangeCM) : ConversionUtils.CentimetersToFeet(elevationChangeCM));
        this.mTotalDistance = sequence.getTotalDistance();
        this.mSplitDistance = sequence.getSplitDistance();
        this.mIsEmpty = isEmpty;
        if (this.mDuration == 0) {
            this.mIsEmpty = true;
        }
        if (this.mIsEmpty) {
            String noValue = context.getString(R.string.no_value);
            this.mSpeed = new Spannable.Factory().newSpannable(noValue);
            this.mElevation = new Spannable.Factory().newSpannable(noValue);
        } else {
            this.mSpeedValue = sequence.getSplitSpeed();
            this.mElevationChangeCM = elevationChangeCM;
            this.mSpeed = Formatter.formatSpeedSplit(context, sequence.getSplitSpeed(), isMetric);
            this.mElevation = Formatter.formatElevation(context, elevationChangeCM, isMetric);
        }
        double fullSplitSize = isMetric ? 99000.0d : 159324.66d;
        if (this.mSplitDistance < fullSplitSize || this.mIsEmpty) {
            this.mIsFullSplit = false;
        }
    }

    public void setSplitType(RunSplitSummary.SplitType type) {
        this.mSplitType = type;
    }

    public RunSplitSummary.SplitType getSplitType() {
        return this.mSplitType;
    }

    public boolean isEmpty() {
        return this.mIsEmpty;
    }

    public String getDistance() {
        return this.mDistance;
    }

    public Spannable getSpeed() {
        return this.mSpeed;
    }

    public Spannable getElevation() {
        return this.mElevation;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public int getElevationChange() {
        return this.mElevationChange;
    }

    public double getTotalDistance() {
        return this.mTotalDistance;
    }

    public double getSplitDistance() {
        return this.mSplitDistance;
    }

    public void setIsEstimation(Context context) {
        if (!this.mIsEstimation && !this.mIsEmpty) {
            this.mIsEstimation = true;
            this.mSpeed = Formatter.formatSpeedSplitAsEstimation(context, this.mSpeedValue, this.mIsMetric);
            this.mElevation = Formatter.formatElevationAsEstimation(context, this.mElevationChangeCM, this.mIsMetric);
        }
    }

    public boolean isEstimation() {
        return this.mIsEstimation;
    }

    public boolean isFullSplit() {
        return this.mIsFullSplit;
    }

    public boolean shouldIgnoreSplit(boolean isMetric) {
        double totalDistance = isMetric ? ConversionUtils.CentimetersToKilometers(this.mTotalDistance) : ConversionUtils.CentimetersToMiles(this.mTotalDistance);
        double fractionValue = totalDistance % 1.0d;
        return fractionValue >= 0.99d;
    }
}

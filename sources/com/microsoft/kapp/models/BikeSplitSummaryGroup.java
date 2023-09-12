package com.microsoft.kapp.models;

import android.content.Context;
import android.text.Spannable;
import com.microsoft.kapp.R;
import com.microsoft.kapp.models.RunSplitSummary;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.Formatter;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class BikeSplitSummaryGroup {
    private String mDistance;
    private Spannable mElevation;
    private boolean mHasValidSplit;
    private boolean mIsEstimation;
    private Spannable mSpeed;
    private double mSpeedCmPerSec;
    private RunSplitSummary.SplitType mSplitType = RunSplitSummary.SplitType.NORMAL;
    private List<BikeSplitSummary> mSplits;

    public BikeSplitSummaryGroup(Context context, List<BikeSplitSummary> splits, boolean isMetric) {
        this.mSplits = new ArrayList(splits);
        int elevation = 0;
        double duration = Constants.SPLITS_ACCURACY;
        double totalDistance = Constants.SPLITS_ACCURACY;
        double groupDistance = Constants.SPLITS_ACCURACY;
        boolean estimation = false;
        this.mHasValidSplit = false;
        for (BikeSplitSummary split : splits) {
            totalDistance = split.getTotalDistance();
            if (!split.isEmpty()) {
                groupDistance += split.getSplitDistance();
                elevation += split.getElevationChange();
                duration += split.getDuration();
                this.mHasValidSplit = true;
                if (split.isEstimation()) {
                    estimation = true;
                }
            } else {
                estimation = true;
            }
        }
        if (duration == Constants.SPLITS_ACCURACY) {
            this.mHasValidSplit = false;
        }
        this.mDistance = Formatter.formatDistanceSplitGroup(context, totalDistance, isMetric);
        if (this.mHasValidSplit) {
            this.mSpeed = Formatter.formatSpeedSplit(context, groupDistance / duration, isMetric);
            this.mElevation = Formatter.formatElevationConverted(context, elevation, isMetric);
            this.mSpeedCmPerSec = groupDistance / duration;
            if (estimation) {
                setIsEstimation(context);
                return;
            }
            return;
        }
        String noValue = context.getString(R.string.no_value);
        this.mSpeed = new Spannable.Factory().newSpannable(noValue);
        this.mElevation = new Spannable.Factory().newSpannable(noValue);
    }

    public void setSplitType(RunSplitSummary.SplitType type) {
        this.mSplitType = type;
    }

    public RunSplitSummary.SplitType getSplitType() {
        return this.mSplitType;
    }

    public String getDistance() {
        return this.mDistance;
    }

    public Spannable getSpeed() {
        return this.mSpeed;
    }

    public double getSpeedValue() {
        return this.mSpeedCmPerSec;
    }

    public Spannable getElevation() {
        return this.mElevation;
    }

    public BikeSplitSummary getSplitAt(int index) {
        return this.mSplits.get(index);
    }

    public int getSplitCount() {
        return this.mSplits.size();
    }

    public void setIsEstimation(Context context) {
        if (this.mHasValidSplit && !this.mIsEstimation) {
            this.mIsEstimation = true;
            this.mSpeed = Formatter.formatEstimation(context, this.mSpeed);
            this.mElevation = Formatter.formatEstimation(context, this.mElevation);
        }
    }
}

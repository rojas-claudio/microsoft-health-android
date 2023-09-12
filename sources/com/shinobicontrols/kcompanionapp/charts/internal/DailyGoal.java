package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.res.Resources;
import android.util.TypedValue;
import com.microsoft.kapp.R;
/* loaded from: classes.dex */
public class DailyGoal {
    private static final double RANGE_MAX_TO_GOAL_RATIO = 1.3333333333333333d;
    private final TypedValue outValue = new TypedValue();
    private Resources resources;
    private double value;

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public double getAxisMaxBasedOnDailyGoalForDailyCharts() {
        checkResources();
        this.resources.getValue(R.id.shinobicharts_daily_goal_factor_for_hour, this.outValue, true);
        return getAxisMaxBasedOnDailyGoalForWeeklyCharts() / this.outValue.getFloat();
    }

    public double getAxisMaxBasedOnDailyGoalForWeeklyCharts() {
        return RANGE_MAX_TO_GOAL_RATIO * this.value;
    }

    private void checkResources() {
        if (this.resources == null) {
            throw new IllegalStateException("DailyGoal must be given Resources to calculate axis max range.");
        }
    }

    public double getAxisMaxBasedOnDailyGoalForWeeklyChartsV1(Double highestValue, int numberOfIntervals, int toTheNearest) {
        if (highestValue == null) {
            highestValue = Double.valueOf(getAxisMaxBasedOnDailyGoalForWeeklyCharts());
        }
        if (numberOfIntervals != 0) {
            double frequency = highestValue.doubleValue() / numberOfIntervals;
            double roundedFrequency = Rounder.roundUp(frequency, toTheNearest);
            return numberOfIntervals * roundedFrequency;
        }
        return highestValue.doubleValue();
    }

    public double getAxisMaxBasedOnDailyGoalForDailyChartsV1(Integer highestBurn, int numberOfIntervals, int toTheNearest) {
        int goalFactor = this.resources.getInteger(R.integer.shinobicharts_calories_daily_axis_calculation_goal_factor);
        int intervalFactor = this.resources.getInteger(R.integer.shinobicharts_calories_daily_axis_calculation_interval_factor);
        if (numberOfIntervals == 0 || goalFactor == 0 || intervalFactor == 0) {
            return highestBurn.intValue();
        }
        double thirdTickMark = this.value / goalFactor;
        double max = thirdTickMark + (thirdTickMark / intervalFactor);
        if (highestBurn != null && highestBurn.intValue() > max) {
            max = highestBurn.intValue();
        }
        double frequency = max / numberOfIntervals;
        double roundedFrequency = Rounder.roundUp(frequency, toTheNearest);
        return numberOfIntervals * roundedFrequency;
    }
}

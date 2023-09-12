package com.shinobicontrols.kcompanionapp.charts.internal;

import java.util.HashMap;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class WeeklyGoals {
    private static final double RANGE_MAX_TO_GOAL_RATIO = 1.3333333333333333d;
    public static final String WEEKLY_GOALS = "WeeklyGoals";
    HashMap<DateTime, Double> goalValues;

    public HashMap<DateTime, Double> getGoalValues() {
        return this.goalValues;
    }

    public void setGoals(HashMap<DateTime, Double> goals) {
        this.goalValues = goals;
    }

    public double getAxisMaxBasedOnDailyGoalForWeeklyChartsV1(Double highestValue, int numberOfIntervals, int toTheNearest) {
        double max = -2.147483648E9d;
        for (Double d : this.goalValues.values()) {
            double goal = d.doubleValue();
            max = Math.max(max, getAxisMaxBasedOnDailyGoal(highestValue, numberOfIntervals, toTheNearest, goal));
        }
        return max;
    }

    public double getGoal(DateTime date) {
        return this.goalValues.get(date).doubleValue();
    }

    public double getAxisMaxBasedOnDailyGoalForWeeklyCharts(double value) {
        return RANGE_MAX_TO_GOAL_RATIO * value;
    }

    public double getAxisMaxBasedOnDailyGoal(Double highestValue, int numberOfIntervals, int toTheNearest, double value) {
        if (highestValue == null) {
            highestValue = Double.valueOf(getAxisMaxBasedOnDailyGoalForWeeklyCharts(value));
        }
        if (numberOfIntervals != 0) {
            double frequency = highestValue.doubleValue() / numberOfIntervals;
            double roundedFrequency = Rounder.roundUp(frequency, toTheNearest);
            return numberOfIntervals * roundedFrequency;
        }
        return highestValue.doubleValue();
    }
}

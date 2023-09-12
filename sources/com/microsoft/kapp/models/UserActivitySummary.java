package com.microsoft.kapp.models;

import android.content.Context;
import com.microsoft.kapp.R;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.kapp.utils.KAppDateFormatter;
import org.joda.time.LocalDate;
/* loaded from: classes.dex */
public class UserActivitySummary {
    private ActivityType mActivityType;
    private int mCaloriesBurned;
    private LocalDate mEndDate;
    private int mNumberOfActiveHours;
    private LocalDate mStartDate;
    private int mStepsTaken;
    private DurationType mType;

    /* loaded from: classes.dex */
    public enum ActivityType {
        STEPS,
        CALORIES
    }

    /* loaded from: classes.dex */
    public enum DurationType {
        DAILY,
        WEEKLY
    }

    public UserActivitySummary(DurationType type, LocalDate startDate, LocalDate endDate, int stepsTaken, int caloriesBurned, ActivityType activityType, int numberOfActiveHours) {
        this.mType = type;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.mStepsTaken = stepsTaken;
        this.mCaloriesBurned = caloriesBurned;
        this.mActivityType = activityType;
        this.mNumberOfActiveHours = numberOfActiveHours;
    }

    public String getName(Context context) {
        String name = "";
        switch (this.mType) {
            case DAILY:
                return KAppDateFormatter.formatToMonthWithDay(context.getResources(), this.mEndDate);
            case WEEKLY:
                String startDateFormat = KAppDateFormatter.formatToMonthDay(this.mStartDate);
                String endDateFormat = KAppDateFormatter.formatToMonthDay(this.mEndDate);
                name = String.format(context.getString(R.string.chart_date_format_long), startDateFormat, endDateFormat);
                break;
        }
        return name;
    }

    public LocalDate getStartDate() {
        return this.mStartDate;
    }

    public LocalDate getEndDate() {
        return this.mEndDate;
    }

    public String getValue(Context context) {
        switch (this.mType) {
            case DAILY:
                if (this.mActivityType == null) {
                    return "";
                }
                switch (this.mActivityType) {
                    case STEPS:
                        String value = Formatter.formatIntegerValue(context, this.mStepsTaken);
                        return value;
                    case CALORIES:
                        String value2 = Formatter.formatIntegerValue(context, this.mCaloriesBurned);
                        return value2;
                    default:
                        return "";
                }
            case WEEKLY:
                return "";
            default:
                return "";
        }
    }

    public int getValue() {
        switch (this.mType) {
            case DAILY:
                if (this.mActivityType == null) {
                    return 0;
                }
                switch (this.mActivityType) {
                    case STEPS:
                        int value = this.mStepsTaken;
                        return value;
                    case CALORIES:
                        int value2 = this.mCaloriesBurned;
                        return value2;
                    default:
                        return 0;
                }
            case WEEKLY:
                return 0;
            default:
                return 0;
        }
    }

    public int getNumberOfActiveHours() {
        return this.mNumberOfActiveHours;
    }
}

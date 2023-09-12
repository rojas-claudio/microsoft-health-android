package com.microsoft.kapp.services.weather;

import org.joda.time.LocalDate;
/* loaded from: classes.dex */
public class WeatherDay {
    private String mConditionPlaintext;
    private int mCurrentTemp;
    private LocalDate mDate;
    private int mHigh;
    private int mIconCode;
    private final boolean mIsCurrentDay = true;
    private String mLocation;
    private int mLow;

    public WeatherDay(LocalDate date, int high, int low, int iconCode, String conditionPlaintext) {
        this.mDate = date;
        this.mHigh = high;
        this.mLow = low;
        this.mIconCode = iconCode;
        this.mConditionPlaintext = conditionPlaintext;
    }

    public WeatherDay(Integer currentTemp, int iconCode, String location, String conditionPlaintext) {
        this.mCurrentTemp = currentTemp.intValue();
        this.mIconCode = iconCode;
        this.mLocation = location;
        this.mConditionPlaintext = conditionPlaintext;
    }

    public LocalDate getDate() {
        return this.mDate;
    }

    public int getHigh() {
        return this.mHigh;
    }

    public int getLow() {
        return this.mLow;
    }

    public int getCurrentTemperature() {
        return this.mCurrentTemp;
    }

    public int getIconCode() {
        return this.mIconCode;
    }

    public boolean isCurrentDay() {
        return this.mIsCurrentDay;
    }

    public String getLocation() {
        return this.mLocation;
    }

    public String getConditionPlaintext() {
        return this.mConditionPlaintext;
    }
}

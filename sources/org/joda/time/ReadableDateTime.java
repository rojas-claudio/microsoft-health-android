package org.joda.time;

import java.util.Locale;
/* loaded from: classes.dex */
public interface ReadableDateTime extends ReadableInstant {
    int getCenturyOfEra();

    int getDayOfMonth();

    int getDayOfWeek();

    int getDayOfYear();

    int getEra();

    int getHourOfDay();

    int getMillisOfDay();

    int getMillisOfSecond();

    int getMinuteOfDay();

    int getMinuteOfHour();

    int getMonthOfYear();

    int getSecondOfDay();

    int getSecondOfMinute();

    int getWeekOfWeekyear();

    int getWeekyear();

    int getYear();

    int getYearOfCentury();

    int getYearOfEra();

    DateTime toDateTime();

    MutableDateTime toMutableDateTime();

    String toString(String str) throws IllegalArgumentException;

    String toString(String str, Locale locale) throws IllegalArgumentException;
}

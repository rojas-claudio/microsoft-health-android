package com.microsoft.kapp.utils;

import android.content.res.Resources;
import com.microsoft.kapp.R;
import java.util.HashMap;
import java.util.Locale;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
/* loaded from: classes.dex */
public class KAppDateFormatter {
    private static final String TAG = KAppDateFormatter.class.getSimpleName();
    private static HashMap<Locale, String> MonthDayPatterns = new HashMap<>();
    private static HashMap<String, String> MonthDayTimePatterns = new HashMap<>();
    private static String RegexToRemoveYear = "([^\\p{Alpha}']|('[\\p{Alpha}]+'))*y+([^\\p{Alpha}']|('[\\p{Alpha}]+'))*";

    public static String formatToMonthDay(ReadableInstant dt) {
        String pattern = getMonthDatePattern();
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return formatter.print(dt);
    }

    public static String formatToMonthDay(ReadablePartial dt) {
        String pattern = getMonthDatePattern();
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return formatter.print(dt);
    }

    public static String formatToMonthDayTime(ReadableInstant dt) {
        String pattern = getMonthDateTimePattern(false);
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return formatter.print(dt);
    }

    public static String formatToMonthDayTime24Hour(ReadableInstant dt) {
        String pattern = getMonthDateTimePattern(true);
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return formatter.print(dt);
    }

    public static String formatToMonthWithDay(Resources resources, ReadableInstant dt) {
        String pattern = getMonthDatePattern();
        DateTimeFormatter formatter = DateTimeFormat.forPattern(resources.getString(R.string.day_month_day_format, pattern));
        return formatter.print(dt);
    }

    public static String formatToMonthWithDay(Resources resources, ReadablePartial dt) {
        String pattern = getMonthDatePattern();
        DateTimeFormatter formatter = DateTimeFormat.forPattern(resources.getString(R.string.day_month_day_format, pattern));
        return formatter.print(dt);
    }

    private static String getMonthDatePattern() {
        if (MonthDayPatterns.containsKey(Locale.getDefault())) {
            return MonthDayPatterns.get(Locale.getDefault());
        }
        String pattern = DateTimeFormat.patternForStyle("S-", null).replaceAll(RegexToRemoveYear, "");
        MonthDayPatterns.put(Locale.getDefault(), pattern);
        return pattern;
    }

    private static String getMonthDateTimePattern(boolean is24Hour) {
        String key = Locale.getDefault() + String.valueOf(is24Hour);
        if (MonthDayTimePatterns.containsKey(key)) {
            return MonthDayTimePatterns.get(key);
        }
        String format = is24Hour ? "%1$s H:mm" : "%1$s h:mm aa";
        String pattern = String.format(format, getMonthDatePattern());
        MonthDayTimePatterns.put(key, pattern);
        return pattern;
    }
}

package com.microsoft.kapp.utils;

import android.content.res.Resources;
import android.text.TextUtils;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
/* loaded from: classes.dex */
public class StringUtils {
    public static final String EMPTY = "";
    private static final String TAG = StringUtils.class.getSimpleName();

    public static String unitSeconds(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.seconds_format_singular : R.string.seconds_format_plural, Integer.valueOf(value));
    }

    public static String unitMinutes(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.minutes_format_singular : R.string.minutes_format_plural, Integer.valueOf(value));
    }

    public static String unitMins(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.mins_format_singular : R.string.mins_format_plural, Integer.valueOf(value));
    }

    public static String unitSecs(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.secs_format_singular : R.string.secs_format_plural, Integer.valueOf(value));
    }

    public static String unitHrs(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.hrs_format_singular : R.string.hrs_format_plural, Integer.valueOf(value));
    }

    public static String unitHours(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.hours_format_singular : R.string.hours_format_plural, Integer.valueOf(value));
    }

    public static String unitSteps(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.steps_format_singular : R.string.steps_format_plural, Integer.valueOf(value));
    }

    public static String unitCalories(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.calories_format_singular : R.string.calories_format_plural, Integer.valueOf(value));
    }

    public static String unitCals(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.cals_format_singular : R.string.cals_format_plural, Integer.valueOf(value));
    }

    public static String unitMiles(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.miles_format_singular : R.string.miles_format_plural, Integer.valueOf(value));
    }

    public static String unitKilometers(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.kilometers_format_singular : R.string.kilometers_format_plural, Integer.valueOf(value));
    }

    public static String unitRepetitions(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.repetitions_format_singular : R.string.repetitions_format_plural, Integer.valueOf(value));
    }

    public static String unitCycles(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.cycles_format_singular : R.string.cycles_format_plural, Integer.valueOf(value));
    }

    public static String unitSets(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.sets_format_singular : R.string.sets_format_plural, Integer.valueOf(value));
    }

    public static String unitMeter(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.meters_format_singular : R.string.meters_format_plural, Integer.valueOf(value));
    }

    public static String unitJoule(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.joules_format_singular : R.string.joules_format_plural, Integer.valueOf(value));
    }

    public static String unitReps(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.reps_format_singular : R.string.reps_format_plural, Integer.valueOf(value));
    }

    public static String unitRounds(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.rounds_format_singular : R.string.rounds_format_plural, Integer.valueOf(value));
    }

    public static int parseToInt(String value) {
        try {
            int integer = Integer.parseInt(value);
            return integer;
        } catch (Exception ex) {
            KLog.e(TAG, "Error during the parsing of String to int", ex);
            return 0;
        }
    }

    public static String ifDefaultThenEmpty(int value, int defaultValue) {
        return value == defaultValue ? "" : String.valueOf(value);
    }

    public static String ifDefaultThenEmpty(double value, int defaultValue) {
        return value == ((double) defaultValue) ? "" : String.valueOf(value);
    }

    public static String joinWhenItemNotEmpty(String seperator, String... values) {
        Validate.notNull(seperator, "seperator");
        StringBuffer buffer = new StringBuffer();
        String firstItem = values[0];
        boolean addSeperator = false;
        if (!TextUtils.isEmpty(firstItem)) {
            buffer.append(firstItem);
            addSeperator = true;
        }
        for (int i = 1; i < values.length; i++) {
            String value = values[i];
            if (!TextUtils.isEmpty(value)) {
                if (addSeperator) {
                    buffer.append(seperator);
                }
                buffer.append(value);
            }
        }
        return buffer.toString();
    }
}

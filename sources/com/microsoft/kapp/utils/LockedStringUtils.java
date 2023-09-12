package com.microsoft.kapp.utils;

import android.content.res.Resources;
import com.microsoft.kapp.R;
/* loaded from: classes.dex */
public class LockedStringUtils {
    public static final String EMPTY = "";
    private static final String TAG = LockedStringUtils.class.getSimpleName();

    public static String unitSeconds(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.seconds_format_singular_locked : R.string.seconds_format_plural_locked, Integer.valueOf(value));
    }

    public static String unitMinutes(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.minutes_format_singular_locked : R.string.minutes_format_plural_locked, Integer.valueOf(value));
    }

    public static String unitMins(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.mins_format_singular_locked : R.string.mins_format_plural_locked, Integer.valueOf(value));
    }

    public static String unitSecs(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.secs_format_singular_locked : R.string.secs_format_plural_locked, Integer.valueOf(value));
    }

    public static String unitHrs(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.hrs_format_singular_locked : R.string.hrs_format_plural_locked, Integer.valueOf(value));
    }

    public static String unitHours(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.hours_format_singular_locked : R.string.hours_format_plural_locked, Integer.valueOf(value));
    }

    public static String unitSteps(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.steps_format_singular_locked : R.string.steps_format_plural_locked, Integer.valueOf(value));
    }

    public static String unitCalories(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.calories_format_singular_locked : R.string.calories_format_plural_locked, Integer.valueOf(value));
    }

    public static String unitCals(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.cals_format_singular_locked : R.string.cals_format_plural_locked, Integer.valueOf(value));
    }

    public static String unitMiles(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.miles_format_singular_locked : R.string.miles_format_plural_locked, Integer.valueOf(value));
    }

    public static String unitKilometers(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.kilometers_format_singular_locked : R.string.kilometers_format_plural_locked, Integer.valueOf(value));
    }

    public static String unitRepetitions(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.repetitions_format_singular_locked : R.string.repetitions_format_plural_locked, Integer.valueOf(value));
    }

    public static String unitCycles(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.cycles_format_singular_locked : R.string.cycles_format_plural_locked, Integer.valueOf(value));
    }

    public static String unitSets(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.sets_format_singular_locked : R.string.sets_format_plural_locked, Integer.valueOf(value));
    }

    public static String unitMeter(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.meters_format_singular_locked : R.string.meters_format_plural_locked, Integer.valueOf(value));
    }

    public static String unitJoule(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.joules_format_singular_locked : R.string.joules_format_plural_locked, Integer.valueOf(value));
    }

    public static String unitReps(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.reps_format_singular_locked : R.string.reps_format_plural_locked, Integer.valueOf(value));
    }

    public static String unitRounds(int value, Resources resources) {
        return resources.getString(value == 1 ? R.string.rounds_format_singular_locked : R.string.rounds_format_plural_locked, Integer.valueOf(value));
    }
}

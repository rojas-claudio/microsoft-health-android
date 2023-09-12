package com.microsoft.kapp.diagnostics;

import android.app.Activity;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
/* loaded from: classes.dex */
public class Validate {
    public static <T> void notNull(T parameter, String parameterName) {
        if (parameter == null) {
            String message = formatMessage("%s cannot be null", parameterName);
            throw new IllegalArgumentException(message);
        }
    }

    public static <T> void isNull(T parameter, String message) {
        if (parameter != null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNullOrEmpty(String parameter, String parameterName) {
        if (parameter == null || parameter.isEmpty()) {
            String message = formatMessage("%s cannot be null or empty", parameterName);
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNullOrEmpty(Collection parameter, String parameterName) {
        if (parameter == null || parameter.isEmpty()) {
            String message = formatMessage("%s cannot be null or empty", parameterName);
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean condition, String format, Object... args) {
        if (!condition) {
            String message = formatMessage(format, args);
            throw new IllegalArgumentException(message);
        }
    }

    public static void inRange(int value, int minValue, int maxValue, String parameterName) {
        if (value < minValue || value >= maxValue) {
            String message = formatMessage("%s is outside of the bounds. Value: %d. Inclusive Minimum: %d. Exclusive Maximum: %d.", parameterName, Integer.valueOf(value), Integer.valueOf(minValue), Integer.valueOf(maxValue));
            throw new IllegalArgumentException(message);
        }
    }

    public static String formatMessage(String format, Object... args) {
        return String.format(Locale.getDefault(), format, args);
    }

    public static boolean isActivityAlive(Activity activity) {
        return (activity == null || activity.isDestroyed() || activity.isFinishing()) ? false : true;
    }

    public static <T> boolean isNotNullNotEmpty(List<T> list) {
        return (list == null || list.size() <= 0 || list.get(0) == null) ? false : true;
    }
}

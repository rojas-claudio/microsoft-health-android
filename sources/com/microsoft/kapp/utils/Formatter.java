package com.microsoft.kapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.RelativeSizeSpan;
import com.microsoft.kapp.R;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.TimeSpan;
import com.microsoft.kapp.style.text.StylableString;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
/* loaded from: classes.dex */
public class Formatter {
    private static final String TAG = Formatter.class.getSimpleName();
    private static final Float SPANNABLE_SUBTEXT_SIZE_IN_PERCENT = Float.valueOf(0.6f);

    private Formatter() {
    }

    public static String formatDurationMinutesToHrMinSecs(Context context, int timeInMinutes) {
        TimeSpan span = TimeSpan.fromMinutes(timeInMinutes);
        return span.formatTimeHrMinSec(context, false);
    }

    public static String formatDurationSecondsToHrMinSecs(Context context, int timeInSeconds) {
        TimeSpan span = TimeSpan.fromSeconds(timeInSeconds);
        return span.formatTimeHrMinSec(context, false);
    }

    public static String formatDurationSecondsToHrMinSecsLocked(Context context, int timeInSeconds) {
        TimeSpan span = TimeSpan.fromSeconds(timeInSeconds);
        return span.formatTimeHrMinSecLocked(context, false);
    }

    public static String formatDurationSecondsToHourMinutesSeconds(Context context, int timeInSeconds) {
        TimeSpan span = TimeSpan.fromSeconds(timeInSeconds);
        return span.formatTimeHrMinSec(context, true);
    }

    public static String formatDurationSecondsToHrMin(Context context, int timeInSeconds) {
        TimeSpan span = TimeSpan.fromSeconds(timeInSeconds);
        return span.getTotalSeconds() < 60.0d ? StringUtils.unitSecs(timeInSeconds, context.getResources()) : formatDurationMinutesToHrMinSecs(context, (int) Math.round(span.getTotalMinutes()));
    }

    public static String formatDurationSecondsToHrMin(int timeInSeconds, String hourString, String hoursString, String minuteString, String minutesString, String secondString, String secondsString, int secondsThreshold) {
        return formatDurationSecondsToHrMin(timeInSeconds, hourString, hoursString, minuteString, minutesString, secondString, secondsString, secondsThreshold, true, MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE);
    }

    public static String formatDurationSecondsToHrMin(int timeInSeconds, String hourString, String hoursString, String minuteString, String minutesString, String secondString, String secondsString, int secondsThreshold, boolean hasSpace, String separator) {
        TimeSpan spanInSeconds = TimeSpan.fromSeconds(timeInSeconds);
        String space = hasSpace ? MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE : "";
        if (spanInSeconds.getTotalSeconds() < secondsThreshold) {
            Object[] objArr = new Object[3];
            objArr[0] = Integer.valueOf(timeInSeconds);
            objArr[1] = space;
            if (timeInSeconds != 1) {
                secondString = secondsString;
            }
            objArr[2] = secondString;
            return String.format("%d%s%s", objArr);
        }
        TimeSpan spanRoundedToMinutes = TimeSpan.fromMinutes((int) Math.round(spanInSeconds.getTotalMinutes()));
        int hours = (int) spanRoundedToMinutes.getTotalHours();
        int minutes = spanRoundedToMinutes.getMinutes();
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(String.format("%1d%s", Integer.valueOf(hours), space));
            if (hours != 1) {
                hourString = hoursString;
            }
            sb.append(hourString);
        }
        if (minutes > 0) {
            if (hours > 0) {
                sb.append(String.format("%s%d%s", separator, Integer.valueOf(minutes), space));
            } else {
                sb.append(String.format("%d%s", Integer.valueOf(minutes), space));
            }
            if (minutes != 1) {
                minuteString = minutesString;
            }
            sb.append(minuteString);
        }
        return sb.toString();
    }

    public static Spannable formatTimeInHoursAndMin(Context context, int timeInSeconds) {
        return formatTimeInHoursAndMin(context, -1, timeInSeconds);
    }

    public static Spannable formatTimeInHoursAndMin(Context context, int styleResId, int timeInSeconds) {
        TimeSpan span = TimeSpan.fromSeconds(timeInSeconds);
        return getTimeFromTimespan(span, styleResId, context);
    }

    public static Spannable getTimeFromTimespan(TimeSpan span, int styleResId, Context context) {
        Spannable textLineHours;
        Spannable textLineMinutes;
        int sleepTimeMinutes = span.getMinutes();
        int sleepTimeHours = span.getHours();
        if (-1 == styleResId) {
            textLineHours = getSubtextSpannable(context, context.getString(R.string.home_tile_sleep_value_hours, Integer.valueOf(sleepTimeHours)), String.valueOf(sleepTimeHours).length());
            textLineMinutes = getSubtextSpannable(context, context.getString(R.string.home_tile_sleep_value_minutes, Integer.valueOf(sleepTimeMinutes)), String.valueOf(sleepTimeMinutes).length());
        } else {
            textLineHours = StylableString.format(context, (int) R.string.metric_format_no_space, styleResId, Integer.valueOf(sleepTimeHours), context.getString(R.string.hours_unit_short));
            textLineMinutes = StylableString.format(context, (int) R.string.metric_format_no_space, styleResId, Integer.valueOf(sleepTimeMinutes), context.getString(R.string.mins_unit_short));
        }
        return sleepTimeHours < 1 ? textLineMinutes : StylableString.format(context, (int) R.string.home_tile_sleep_value_hours_minutes, -1, textLineHours, textLineMinutes);
    }

    public static Spannable formatSleepTime(Context context, int timeInSeconds) {
        TimeSpan span = TimeSpan.fromSeconds(timeInSeconds);
        int sleepTimeMinutes = span.getMinutes();
        int sleepTimeHours = span.getHours();
        Spannable textLineHours = getSubtextSpannable(context, context.getString(R.string.home_tile_sleep_value_hours, Integer.valueOf(sleepTimeHours)), String.valueOf(sleepTimeHours).length());
        Spannable textLineMinutes = getSubtextSpannable(context, context.getString(R.string.home_tile_sleep_value_minutes, Integer.valueOf(sleepTimeMinutes)), String.valueOf(sleepTimeMinutes).length());
        return StylableString.format(context, (int) R.string.home_tile_sleep_value_hours_minutes, -1, textLineHours, textLineMinutes);
    }

    public static Spannable formatSleepTime(Context context, int stringStyleResId, int timeInSeconds) {
        TimeSpan span = TimeSpan.fromSeconds(timeInSeconds);
        int sleepTimeMinutes = span.getMinutes();
        int sleepTimeHours = span.getHours();
        Spannable textLineHours = StylableString.format(context, (int) R.string.metric_format_no_space, stringStyleResId, String.valueOf(sleepTimeHours), context.getString(R.string.home_tile_sleep_hours_unit));
        Spannable textLineMinutes = StylableString.format(context, (int) R.string.metric_format_no_space, stringStyleResId, String.valueOf(sleepTimeMinutes), context.getString(R.string.home_tile_sleep_min_unit));
        return StylableString.format(context, (int) R.string.home_tile_sleep_value_hours_minutes, -1, textLineHours, textLineMinutes);
    }

    public static CharSequence formatDurationSecondsToSpannableColon(int timeInSeconds) {
        TimeSpan timeSpan = TimeSpan.fromSeconds(timeInSeconds);
        int hours = (int) timeSpan.getTotalHours();
        int minutes = timeSpan.getMinutes();
        int seconds = timeSpan.getSeconds();
        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(String.format("%1d", Integer.valueOf(hours)));
            sb.append(String.format(":%02d", Integer.valueOf(minutes)));
            sb.append(String.format(":%02d", Integer.valueOf(seconds)));
        } else {
            sb.append(String.format("%02d", Integer.valueOf(minutes)));
            sb.append(String.format(":%02d", Integer.valueOf(seconds)));
        }
        return sb.toString();
    }

    public static CharSequence formatNumberOfTimes(Context context, int numberOfWakeUps) {
        return new SpannableString(getSubtextSpannable(context, context.getString(R.string.count_format_x, Integer.valueOf(numberOfWakeUps)), String.valueOf(numberOfWakeUps).length()));
    }

    public static String formatMonthAndDate(Context context, DateTime formatDate) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(context.getString(R.string.date_format_tracker_date));
        return formatter.print(formatDate);
    }

    public static String formatTrackerDateWithDay(Context context, LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(context.getString(R.string.date_format_tracker_date_with_day));
        return formatter.print(date);
    }

    public static CharSequence formatToPercentage(Context context, double number) {
        int num = (int) number;
        return new SpannableString(getSubtextSpannable(context, context.getString(R.string.percentage_format, Integer.valueOf(num)), String.valueOf(num).length()));
    }

    public static Spannable getSubtextSpannable(Context context, String textToSpan, int startIndex) {
        if (context == null) {
            KLog.e(TAG, "Context cannot be null!");
            return new Spannable.Factory().newSpannable("");
        } else if (textToSpan == null) {
            KLog.e(TAG, "textToSpan cannot be null!");
            return new Spannable.Factory().newSpannable("");
        } else if (startIndex > 0) {
            Spannable spannableText = new SpannableString(textToSpan);
            spannableText.setSpan(new RelativeSizeSpan(SPANNABLE_SUBTEXT_SIZE_IN_PERCENT.floatValue()), startIndex, spannableText.length(), 34);
            return spannableText;
        } else {
            Spannable spannableText2 = new SpannableString(String.format("%s ", textToSpan));
            spannableText2.setSpan(new RelativeSizeSpan(SPANNABLE_SUBTEXT_SIZE_IN_PERCENT.floatValue()), 0, textToSpan.length(), 34);
            return spannableText2;
        }
    }

    public static String formatFilteredEventListheader(String type) {
        if (type == null) {
            KLog.e(TAG, "type should not be null!");
            return "";
        }
        return String.format("%s", type.toUpperCase());
    }

    private static List<String> convertToPersonalBestBannerNames(List<String> personalBestTokens, Context context) {
        if (personalBestTokens == null || personalBestTokens.size() == 0) {
            return new ArrayList();
        }
        String bestRunDistanceName = context.getString(R.string.best_run_distance_name);
        String bestRunDistanceNameBanner = context.getString(R.string.best_run_distance_name_banner);
        String bestRunPaceName = context.getString(R.string.best_run_pace_name);
        String bestRunPaceNameBanner = context.getString(R.string.best_run_pace_name_banner);
        String bestRunCaloriesName = context.getString(R.string.best_run_calories_name);
        String bestRunCaloriesNameBanner = context.getString(R.string.best_run_calories_name_banner);
        String bestRunSplitName = context.getString(R.string.best_run_split_name);
        String bestRunSplitNameBanner = context.getString(R.string.best_run_split_name_banner);
        String bestExerciseDurationName = context.getString(R.string.best_exercise_duration_name);
        String bestExerciseDurationNameBanner = context.getString(R.string.best_exercise_duration_name_banner);
        String bestExerciseCaloriesName = context.getString(R.string.best_exercise_calories_name);
        String bestExerciseCaloriesNameBanner = context.getString(R.string.best_exercise_calories_name_banner);
        String bestBikeSpeedName = context.getString(R.string.best_bike_pace_name);
        String bestBikeSpeedNameBanner = context.getString(R.string.best_bike_pace_name_banner);
        String bestBikeCaloriesName = context.getString(R.string.best_bike_calories_name);
        String bestBikeCaloriesNameBanner = context.getString(R.string.best_bike_calories_name_banner);
        String bestBikeGainName = context.getString(R.string.best_bike_gain_name);
        String bestBikeGainNameBanner = context.getString(R.string.best_bike_gain_name_banner);
        String bestBikeDistanceName = context.getString(R.string.best_bike_distance_name);
        String bestBikeDistanceNameBanner = context.getString(R.string.best_bike_distance_name_banner);
        List<String> personalBests = new ArrayList<>(personalBestTokens.size());
        for (String name : personalBestTokens) {
            if (bestRunDistanceName.equalsIgnoreCase(name)) {
                personalBests.add(bestRunDistanceNameBanner);
            } else if (bestRunPaceName.equalsIgnoreCase(name)) {
                personalBests.add(bestRunPaceNameBanner);
            } else if (bestRunCaloriesName.equalsIgnoreCase(name)) {
                personalBests.add(bestRunCaloriesNameBanner);
            } else if (bestRunSplitName.equalsIgnoreCase(name)) {
                personalBests.add(bestRunSplitNameBanner);
            } else if (bestExerciseDurationName.equalsIgnoreCase(name)) {
                personalBests.add(bestExerciseDurationNameBanner);
            } else if (bestExerciseCaloriesName.equalsIgnoreCase(name)) {
                personalBests.add(bestExerciseCaloriesNameBanner);
            } else if (bestBikeSpeedName.equalsIgnoreCase(name)) {
                personalBests.add(bestBikeSpeedNameBanner);
            } else if (bestBikeCaloriesName.equalsIgnoreCase(name)) {
                personalBests.add(bestBikeCaloriesNameBanner);
            } else if (bestBikeGainName.equalsIgnoreCase(name)) {
                personalBests.add(bestBikeGainNameBanner);
            } else if (bestBikeDistanceName.equalsIgnoreCase(name)) {
                personalBests.add(bestBikeDistanceNameBanner);
            }
        }
        return personalBests;
    }

    public static String formatPersonalBestBanner(List<String> personalBestTokens, Context context) {
        int formattedBannerResourceId;
        if (context == null) {
            KLog.e(TAG, "Context cannot be null!");
            return "";
        } else if (personalBestTokens == null) {
            KLog.e(TAG, "personalBestTokens cannot be null!");
            return "";
        } else {
            List<String> personalBestTokens2 = convertToPersonalBestBannerNames(personalBestTokens, context);
            switch (personalBestTokens2.size()) {
                case 1:
                    formattedBannerResourceId = R.string.best_banner_1;
                    break;
                case 2:
                    formattedBannerResourceId = R.string.best_banner_2;
                    break;
                case 3:
                    formattedBannerResourceId = R.string.best_banner_3;
                    break;
                default:
                    formattedBannerResourceId = R.string.best_banner_4;
                    break;
            }
            String formattedStr = context.getString(formattedBannerResourceId, personalBestTokens2.toArray());
            return formattedStr;
        }
    }

    public static int formatTimeUnitShortForLocale(Context context, DateTime time, Locale locale) {
        if (context == null) {
            KLog.e(TAG, "Context cannot be null!");
            return -1;
        } else if (time == null) {
            KLog.e(TAG, "Time cannot be null!");
            return -1;
        } else {
            DateTimeField hoursField = time.getChronology().clockhourOfDay();
            String hourText = hoursField.getAsText(time.getMillis());
            int hourValue = -1;
            try {
                hourValue = Integer.parseInt(hourText) % 24;
            } catch (NumberFormatException ex) {
                KLog.e(TAG, String.format("Cannot convert [hour=%s] into an int: %s", hourText, ex.getMessage()));
            }
            if (hourValue >= 0) {
                if (hourValue < 12) {
                    return R.string.tracker_time_unit_halfday_am_short;
                }
                return R.string.tracker_time_unit_halfday_pm_short;
            }
            return -1;
        }
    }

    public static String formatTimeValueForLocale(Context context, DateTime time, Locale locale) {
        if (context == null) {
            KLog.e(TAG, "Context cannot be null!");
            return "";
        } else if (time == null) {
            KLog.e(TAG, "Time cannot be null!");
            return "";
        } else {
            DateTimeFormatter timeFormatter = DateTimeFormat.forPattern(context.getResources().getString(R.string.tracker_time_format_metric_value));
            if (DateFormat.is24HourFormat(context)) {
                timeFormatter = DateTimeFormat.forPattern(context.getResources().getString(R.string.tracker_time_format_metric_value_24h));
            }
            return timeFormatter.print(time).toLowerCase(locale);
        }
    }

    public static String formatTimeForLocale(Context context, DateTime time, Locale locale) {
        String formattedTimeValue = formatTimeValueForLocale(context, time, locale);
        int formattedTimeUnitResId = formatTimeUnitShortForLocale(context, time, locale);
        if (!DateFormat.is24HourFormat(context)) {
            if (formattedTimeUnitResId == -1) {
                return "";
            }
            return context.getString(R.string.time_value_short, formattedTimeValue, context.getString(formattedTimeUnitResId));
        }
        return formattedTimeValue;
    }

    public static String formatNumberWithCommas(Resources resources, String label) {
        try {
            int labelValue = Integer.parseInt(label);
            return resources.getString(R.string.shinobicharts_number_format, Integer.valueOf(labelValue));
        } catch (NumberFormatException e) {
            return label;
        }
    }

    public static String addQuotationMarks(String str) {
        if (!str.startsWith("\"")) {
            str = String.format("\"%s", str);
        }
        if (!str.endsWith("\"")) {
            return String.format("%s\"", str);
        }
        return str;
    }

    public static Spannable formatSteps(Context context, int steps) {
        return formatSteps(context, -1, steps);
    }

    public static Spannable formatSteps(Context context, int stringStyleId, int steps) {
        if (steps < 0) {
            return new SpannableString(context.getString(R.string.no_value));
        }
        String units = steps == 1 ? context.getString(R.string.unit_steps_singular) : context.getString(R.string.unit_steps_plural);
        String format = context.getString(R.string.metric_format_calories);
        return formatIntegerValue(context, stringStyleId, steps, units, format);
    }

    public static Spannable formatCalories(Context context, int calories) {
        return formatCalories(context, -1, calories);
    }

    public static Spannable formatCalories(Context context, int stringStyleId, int calories) {
        if (calories < 0) {
            return new SpannableString(context.getString(R.string.no_value));
        }
        String units = calories == 1 ? context.getString(R.string.unit_calories_singular) : context.getString(R.string.unit_calories_plural);
        String format = context.getString(R.string.metric_format_calories);
        return formatIntegerValue(context, stringStyleId, calories, units, format);
    }

    public static Spannable formatElevation(Context context, double elevationCM, boolean isMetric) {
        return formatElevation(context, -1, elevationCM, isMetric);
    }

    public static Spannable formatElevation(Context context, int stringStyleId, double elevationCM, boolean isMetric) {
        int elevation = isMetric ? (int) ConversionUtils.CentimetersToMeters(elevationCM) : (int) ConversionUtils.CentimetersToFeet(elevationCM);
        return formatElevationConverted(context, stringStyleId, elevation, isMetric);
    }

    public static Spannable formatElevationAsEstimation(Context context, int elevationCM, boolean isMetric) {
        return formatElevationAsEstimation(context, -1, elevationCM, isMetric);
    }

    public static Spannable formatElevationAsEstimation(Context context, int stringStyleId, int elevationCM, boolean isMetric) {
        int elevation = isMetric ? (int) ConversionUtils.CentimetersToMeters(elevationCM) : (int) ConversionUtils.CentimetersToFeet(elevationCM);
        String unit = isMetric ? context.getString(R.string.unit_elevation_metric) : context.getString(R.string.unit_elevation_imperial);
        String formattedUnit = context.getString(R.string.estimation_with_value, unit);
        String formatValueUnit = context.getString(R.string.metric_format_elevation);
        return formatIntegerValue(context, stringStyleId, elevation, formattedUnit, formatValueUnit);
    }

    public static Spannable formatElevationConverted(Context context, int elevation, boolean isMetric) {
        return formatElevationConverted(context, -1, elevation, isMetric);
    }

    public static Spannable formatElevationConverted(Context context, int stringStyleId, int elevation, boolean isMetric) {
        String unit = isMetric ? context.getString(R.string.unit_elevation_metric) : context.getString(R.string.unit_elevation_imperial);
        String format = context.getString(R.string.metric_format_elevation);
        return formatIntegerValue(context, stringStyleId, elevation, unit, format);
    }

    public static Spannable formatDistanceStat(Context context, double distanceCM, boolean isMetric) {
        return formatDistanceStat(context, -1, distanceCM, isMetric);
    }

    public static Spannable formatDistanceStat(Context context, int stringStyleId, double distanceCM, boolean isMetric) {
        double distance = isMetric ? ConversionUtils.CentimetersToKilometers(distanceCM) : ConversionUtils.CentimetersToMiles(distanceCM);
        String units = isMetric ? context.getString(R.string.unit_distance_metric_short) : context.getString(R.string.unit_distance_imperial_short);
        String format = context.getString(R.string.metric_format_distance_stat);
        String valueFormat = context.getString(R.string.decimal_format_double_decimal_digit);
        return formatDecimalValue(context, stringStyleId, distance, valueFormat, units, format);
    }

    public static String formatDistanceSplitGroup(Context context, double distanceCM, boolean isMetric) {
        return formatDistanceSplit(context, distanceCM, isMetric, context.getString(R.string.metric_format_distance_group_header));
    }

    public static String formatDistanceSplit(Context context, double distanceCM, boolean isMetric) {
        return formatDistanceSplit(context, distanceCM, isMetric, context.getString(R.string.metric_format_distance_split));
    }

    public static String formatDistanceSplit(Context context, double distanceCM, boolean isMetric, String format) {
        return formatDistanceSplit(context, -1, distanceCM, isMetric, format);
    }

    public static String formatDistanceSplit(Context context, int stringStyleId, double distanceCM, boolean isMetric, String format) {
        double distance = isMetric ? ConversionUtils.CentimetersToKilometers(distanceCM) : ConversionUtils.CentimetersToMiles(distanceCM);
        boolean isWhole = distance % 1.0d <= 0.009d;
        String units = isMetric ? context.getString(R.string.unit_distance_metric_short) : context.getString(R.string.unit_distance_imperial_short);
        String valueFormat = context.getString(isWhole ? R.string.decimal_format_whole_number : R.string.decimal_format_double_decimal_digit);
        return formatDecimalValue(context, stringStyleId, distance, valueFormat, units, format).toString();
    }

    public static Spannable formatDistance(Context context, double distanceCM, boolean isMetric) {
        return formatDistance(context, distanceCM, isMetric, true);
    }

    public static Spannable formatDistanceToOneDecimalUnit(Context context, double distanceCM, boolean isMetric) {
        return formatDistance(context, distanceCM, isMetric, false);
    }

    public static Spannable formatDistanceToOneDecimalUnitLocked(Context context, double distanceCM, boolean isMetric) {
        return formatDistanceToOneDecimalUnitLocked(context, -1, distanceCM, isMetric);
    }

    public static Spannable formatDistanceToOneDecimalUnitLocked(Context context, int stringStyleId, double distanceCM, boolean isMetric) {
        double distance = isMetric ? ConversionUtils.CentimetersToKilometers(distanceCM) : ConversionUtils.CentimetersToMiles(distanceCM);
        String units = isMetric ? context.getString(R.string.unit_distance_metric_short_locked) : context.getString(R.string.unit_distance_imperial_short_locked);
        String format = context.getString(R.string.metric_format_distance);
        String valueFormat = context.getString(R.string.decimal_format_single_decimal_digit);
        return formatDecimalValue(context, stringStyleId, distance, valueFormat, units, format);
    }

    public static Spannable formatDistance(Context context, double distanceCM, boolean isMetric, boolean isDoubleDigitDecimal) {
        return formatDistance(context, -1, distanceCM, isMetric, isDoubleDigitDecimal);
    }

    public static Spannable formatDistance(Context context, int stringStyleId, double distanceCM, boolean isMetric, boolean isDoubleDigitDecimal) {
        double distance = isMetric ? ConversionUtils.CentimetersToKilometers(distanceCM) : ConversionUtils.CentimetersToMiles(distanceCM);
        String units = isMetric ? context.getString(R.string.unit_distance_metric_short) : context.getString(R.string.unit_distance_imperial_short);
        String format = context.getString(R.string.metric_format_distance);
        String valueFormat = isDoubleDigitDecimal ? context.getString(R.string.decimal_format_double_decimal_digit) : context.getString(R.string.decimal_format_single_decimal_digit);
        return formatDecimalValue(context, stringStyleId, distance, valueFormat, units, format);
    }

    public static Spannable formatSpeedSplit(Context context, double speedCMPerSec, boolean isMetric) {
        return formatSpeedSplit(context, -1, speedCMPerSec, isMetric);
    }

    public static Spannable formatSpeedSplit(Context context, int stringStyleId, double speedCMPerSec, boolean isMetric) {
        double speed = isMetric ? ConversionUtils.CentimeterPerSecondToKilometersPerHour(speedCMPerSec) : ConversionUtils.CentimeterPerSecondToMilesPerHour(speedCMPerSec);
        String units = isMetric ? context.getString(R.string.unit_speed_metric) : context.getString(R.string.unit_speed_imperial);
        String format = context.getString(R.string.metric_format_speed_split);
        String valueFormat = context.getString(R.string.decimal_format_single_decimal_digit);
        return formatDecimalValue(context, stringStyleId, speed, valueFormat, units, format);
    }

    public static Spannable formatSpeedSplitAsEstimation(Context context, int speedCMPerSec, boolean isMetric) {
        return formatSpeedSplitAsEstimation(context, -1, speedCMPerSec, isMetric);
    }

    public static Spannable formatSpeedSplitAsEstimation(Context context, int stringStyleId, int speedCMPerSec, boolean isMetric) {
        double speed = isMetric ? ConversionUtils.CentimeterPerSecondToKilometersPerHour(speedCMPerSec) : ConversionUtils.CentimeterPerSecondToMilesPerHour(speedCMPerSec);
        String unit = isMetric ? context.getString(R.string.unit_speed_metric) : context.getString(R.string.unit_speed_imperial);
        String formattedUnit = context.getString(R.string.estimation_with_value, unit);
        String valueFormat = context.getString(R.string.decimal_format_single_decimal_digit);
        String format = context.getString(R.string.metric_format_speed_split);
        return formatDecimalValue(context, stringStyleId, speed, valueFormat, formattedUnit, format);
    }

    public static Spannable formatSpeedStat(Context context, double speedCMPerSec, boolean isMetric) {
        return formatSpeedStat(context, -1, speedCMPerSec, isMetric);
    }

    public static Spannable formatSpeedStat(Context context, int stringStyleId, double speedCMPerSec, boolean isMetric) {
        double speed = isMetric ? ConversionUtils.CentimeterPerSecondToKilometersPerHour(speedCMPerSec) : ConversionUtils.CentimeterPerSecondToMilesPerHour(speedCMPerSec);
        String units = isMetric ? context.getString(R.string.unit_speed_metric) : context.getString(R.string.unit_speed_imperial);
        String format = context.getString(R.string.metric_format_speed_stat);
        String valueFormat = context.getString(R.string.decimal_format_single_decimal_digit);
        return formatDecimalValue(context, stringStyleId, speed, valueFormat, units, format);
    }

    private static Spannable formatDecimalValue(Context context, int stringStyleResId, double value, String valueFormat, String units, String format) {
        NumberFormat decimalFormat = new DecimalFormat(valueFormat);
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        String decimalValue = decimalFormat.format(value);
        return stringStyleResId == -1 ? getSubtextSpannable(context, String.format(format, decimalValue, units), decimalValue.length()) : StylableString.format(context, format, stringStyleResId, decimalValue, units);
    }

    public static String formatIntegerValue(Context context, int value) {
        NumberFormat integerFormat = new DecimalFormat(context.getString(R.string.decimal_format_whole_number));
        integerFormat.setRoundingMode(RoundingMode.DOWN);
        integerFormat.setGroupingUsed(true);
        return integerFormat.format(value);
    }

    private static Spannable formatIntegerValue(Context context, int stringStyleResId, int value, String units, String format) {
        NumberFormat integerFormat = new DecimalFormat(context.getString(R.string.decimal_format_whole_number));
        integerFormat.setRoundingMode(RoundingMode.DOWN);
        integerFormat.setGroupingUsed(true);
        String integerValue = integerFormat.format(value);
        return stringStyleResId == -1 ? getSubtextSpannable(context, String.format(format, integerValue, units), integerValue.length()) : StylableString.format(context, format, stringStyleResId, integerValue, units);
    }

    public static Spannable formatEstimation(Context context, Spannable value) {
        String estimation = context.getString(R.string.estimation_with_value, value);
        return new SpannableString(estimation);
    }

    public static String formatGuidedWorkoutDisplayType(Context context, int typeResId, int subTypeResId) {
        return context.getString(subTypeResId, context.getString(typeResId));
    }

    public static String formatTrainingEffect(Context context, double trainingEffect) {
        if (trainingEffect > Constants.SPLITS_ACCURACY) {
            NumberFormat doubleFormat = new DecimalFormat(context.getString(R.string.decimal_format_single_decimal_digit));
            doubleFormat.setRoundingMode(RoundingMode.DOWN);
            return doubleFormat.format(trainingEffect);
        }
        return getSubtextSpannable(context, context.getString(R.string.no_value), 0).toString();
    }

    public static Spannable formatGolfScore(Context context, int totalScore, int aboveOrBelowPar) {
        return formatGolfScore(context, -1, totalScore, aboveOrBelowPar);
    }

    public static Spannable formatGolfScore(Context context, int styleResId, int totalScore, int aboveOrBelowPar) {
        String parValue;
        if (aboveOrBelowPar < 0) {
            parValue = context.getString(R.string.below_par_value, Integer.valueOf(aboveOrBelowPar * (-1)));
        } else {
            parValue = aboveOrBelowPar == 0 ? context.getString(R.string.at_par_value, Integer.valueOf(aboveOrBelowPar)) : context.getString(R.string.above_par_value, Integer.valueOf(aboveOrBelowPar));
        }
        String format = context.getString(R.string.golf_score_with_par);
        return formatIntegerValue(context, styleResId, totalScore, parValue, format);
    }

    public static CharSequence formatLongestDrive(double distanceCentimeters, Context context, boolean isMetric) {
        int distance = isMetric ? (int) ConversionUtils.CentimetersToMeters(distanceCentimeters) : ConversionUtils.CentimetersToYards(distanceCentimeters);
        String unit = isMetric ? context.getString(R.string.meters_abbreviation) : context.getString(R.string.formatter_value_yards);
        return StylableString.format(context, unit, (int) R.array.MerticSmallUnitFormat, Integer.valueOf(distance), unit);
    }

    public static CharSequence formatHR(int heartRate) {
        return String.valueOf(heartRate);
    }

    public static CharSequence formatNumber(int number, Context context) {
        return formatIntegerValue(context, number);
    }

    public static String formatPostGuidedWorkoutDetailCompletionValueTime(int completionValueInt, Context context) {
        String SECOND_STRING_SEC = context.getString(R.string.secs_singular);
        String MINUTE_STRING_MIN = context.getString(R.string.mins_singular);
        String HOUR_STRING_HR = context.getString(R.string.hrs_singular);
        return formatDurationSecondsToHrMin(completionValueInt, HOUR_STRING_HR, HOUR_STRING_HR, MINUTE_STRING_MIN, MINUTE_STRING_MIN, SECOND_STRING_SEC, SECOND_STRING_SEC, Constants.GUIDED_WORKOUT_SECONDS_120S_THRESHOLD, false, MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE);
    }

    public static Spannable formatUV(Context context, int styleResId, int timeInMinutes) {
        TimeSpan span = TimeSpan.fromMinutes(timeInMinutes);
        return getTimeFromTimespan(span, styleResId, context);
    }

    public static Spannable formatFloorsClimbed(Context context, int floorsClimbed) {
        return floorsClimbed >= 0 ? new SpannableString(String.valueOf(floorsClimbed)) : new SpannableString(context.getString(R.string.no_value));
    }
}

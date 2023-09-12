package com.microsoft.kapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.models.TimeSpan;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.views.BaseTrackerStat;
import com.microsoft.kapp.views.DescriptiveTextTrackerStat;
import com.microsoft.kapp.views.DoubleTrackerStat;
import com.microsoft.kapp.views.SingleTrackerStat;
import com.microsoft.kapp.views.TripleTrackerStat;
import com.microsoft.krestsdk.models.AltitudeEvent;
import com.microsoft.krestsdk.models.BikeEvent;
import com.microsoft.krestsdk.models.ExerciseEventBase;
import com.microsoft.krestsdk.models.MeasuredEvent;
import com.microsoft.krestsdk.models.MeasuredEventSequence;
import com.microsoft.krestsdk.models.RunEvent;
import com.microsoft.krestsdk.models.SleepEvent;
import com.microsoft.krestsdk.models.UserEvent;
import java.util.Locale;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
/* loaded from: classes.dex */
public class TrackerStatUtils {
    public static BaseTrackerStat getPaceStat(BikeEvent event, Resources res, Context context, boolean isMetric) {
        Validate.notNull(event, "event");
        Validate.notNull(res, "res");
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        SingleTrackerStat pace = new SingleTrackerStat(context, res.getString(R.string.tracker_pace), R.string.glyph_pace);
        double eventPace = event.getPace() * event.getSplitGroupSize();
        double paceValue = isMetric ? eventPace : eventPace * 1.60934d;
        CharSequence notAvailable = Formatter.getSubtextSpannable(context, context.getString(R.string.no_value), 0);
        if (paceValue > Constants.SPLITS_ACCURACY) {
            notAvailable = TimeSpan.fromMilliseconds(paceValue).formatTimePrimeNotation(context);
        }
        pace.setValue(notAvailable);
        return pace;
    }

    public static BaseTrackerStat getBurnedCaloriesStat(ExerciseEventBase exerciseEvent, Resources res, Context context) {
        Validate.notNull(exerciseEvent, "exerciseEvent");
        Validate.notNull(res, "res");
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        TripleTrackerStat burnedCalories = new TripleTrackerStat(context, res.getString(R.string.tracker_calories_burned), R.string.glyph_calories);
        burnedCalories.setLeftTitle(R.string.tracker_calories_burned_left_sub_title);
        burnedCalories.setRightTitle(R.string.tracker_calories_burned_right_sub_title);
        burnedCalories.setValue(String.valueOf(exerciseEvent.getCaloriesBurned()));
        burnedCalories.setLeftValue(String.valueOf(exerciseEvent.getCaloriesFromFat()));
        burnedCalories.setRightValue(String.valueOf(exerciseEvent.getCaloriesFromCarbs()));
        return burnedCalories;
    }

    public static BaseTrackerStat getBestSplitStat(double bestSplitValue, Resources res, Context context) {
        Validate.notNull(res, "res");
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        SingleTrackerStat bestSplit = new SingleTrackerStat(context, res.getString(R.string.tracker_best_split), R.string.glyph_time);
        String noValue = context.getString(R.string.no_value);
        TimeSpan splitValue = TimeSpan.fromMilliseconds(bestSplitValue);
        int minutesThresholdValueToIgnore = res.getInteger(R.integer.pace_minutes_threshold_value_to_ignore);
        if (((int) splitValue.getTotalMinutes()) > minutesThresholdValueToIgnore) {
            bestSplit.setValue(Formatter.getSubtextSpannable(context, noValue, 0));
        } else {
            bestSplit.setValue(splitValue.formatTimePrimeNotation(context));
        }
        return bestSplit;
    }

    public static BaseTrackerStat gestBestSplitStat(MeasuredEvent event, Resources res, Context context, SettingsProvider settingsProvider) {
        double splitDuration = getBestSplit(event.getSequences(), settingsProvider.isDistanceHeightMetric());
        return getBestSplitStat(splitDuration, res, context);
    }

    private static double getBestSplit(MeasuredEventSequence[] sequences, boolean isMetric) {
        if (sequences == null || sequences.length == 0) {
            return Constants.SPLITS_ACCURACY;
        }
        double completeSplitDistance = isMetric ? 100000.0d : 160934.0d;
        int bestSplit = Integer.MAX_VALUE;
        int lastIndex = sequences.length - 1;
        for (int i = 0; i < lastIndex; i++) {
            MeasuredEventSequence event = sequences[i];
            int splitPace = event.getSplitPace();
            if ((i < lastIndex - 1 || isCompleteSplit(event.getSplitDistance(), completeSplitDistance)) && splitPace < bestSplit) {
                bestSplit = splitPace;
            }
        }
        return isMetric ? bestSplit : ConversionUtils.MilesToKilometers(bestSplit);
    }

    private static boolean isCompleteSplit(double splitDistance, double completeSplitDistance) {
        double diff = completeSplitDistance - splitDistance;
        double epsilon = completeSplitDistance * 0.1d;
        return diff <= epsilon && diff >= (-epsilon);
    }

    public static BaseTrackerStat getAvgBpmStat(int averageHeartRate, int peakHeartRate, int lowestHeartRate, Resources res, Context context) {
        Validate.notNull(res, "res");
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        TripleTrackerStat avgBpm = new TripleTrackerStat(context, res.getString(R.string.tracker_average_bpm), R.string.glyph_heart_fill);
        avgBpm.setLeftTitle(R.string.tracker_average_bpm_left_sub_title);
        avgBpm.setRightTitle(R.string.tracker_average_bpm_right_sub_title);
        CharSequence notAvailable = Formatter.getSubtextSpannable(context, context.getString(R.string.no_value), 0);
        avgBpm.setValue(averageHeartRate > 0 ? String.valueOf(averageHeartRate) : notAvailable);
        avgBpm.setLeftValue(peakHeartRate > 0 ? String.valueOf(peakHeartRate) : notAvailable);
        if (lowestHeartRate > 0) {
            notAvailable = String.valueOf(lowestHeartRate);
        }
        avgBpm.setRightValue(notAvailable);
        return avgBpm;
    }

    public static BaseTrackerStat getEndHRStat(ExerciseEventBase exerciseEvent, Resources res, Context context) {
        Validate.notNull(exerciseEvent, "exerciseEvent");
        Validate.notNull(res, "res");
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        TripleTrackerStat endHr = new TripleTrackerStat(context, res.getString(R.string.tracker_end_heart_rate), R.string.glyph_heart_fill);
        endHr.setLeftTitle(R.string.tracker_one_minute);
        endHr.setRightTitle(R.string.tracker_two_minutes);
        CharSequence notAvailable = Formatter.getSubtextSpannable(context, context.getString(R.string.no_value), 0);
        endHr.setValue(exerciseEvent.getFinishHeartRate() > 0 ? String.valueOf(exerciseEvent.getFinishHeartRate()) : notAvailable);
        endHr.setLeftValue(exerciseEvent.getRecoveryHeartRate1Minute() > 0 ? String.valueOf(exerciseEvent.getRecoveryHeartRate1Minute()) : notAvailable);
        if (exerciseEvent.getRecoveryHeartRate2Minute() > 0) {
            notAvailable = String.valueOf(exerciseEvent.getRecoveryHeartRate2Minute());
        }
        endHr.setRightValue(notAvailable);
        return endHr;
    }

    public static BaseTrackerStat getDurationStat(int duration, DateTime startTime, Resources res, Context context) {
        return getDurationStat(duration, startTime, res, context, false);
    }

    public static BaseTrackerStat getDurationStat(int duration, DateTime startTime, Resources res, Context context, boolean formatWithHrMinSubText) {
        return getDurationStat(duration, startTime, res, context, formatWithHrMinSubText, -1);
    }

    public static BaseTrackerStat getDurationStat(int duration, DateTime startTime, Resources res, Context context, boolean formatWithHrMinSubText, int stringStyleResId) {
        Validate.notNull(res, "res");
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        DoubleTrackerStat stat = new DoubleTrackerStat(context, res.getString(R.string.tracker_exercise_duration), R.string.glyph_duration);
        stat.setSubTitle(R.string.tracker_exercise_duration_sub_title);
        stat.setValue(formatWithHrMinSubText ? Formatter.formatTimeInHoursAndMin(context, stringStyleResId, duration) : Formatter.formatDurationSecondsToSpannableColon(duration));
        CharSequence startTimeValue = Formatter.formatTimeForLocale(context, startTime, Locale.getDefault());
        stat.setSubValue(startTimeValue);
        return stat;
    }

    public static BaseTrackerStat getRecoveryTimeStat(ExerciseEventBase exerciseEvent, Resources res, Context context) {
        String dateEvent;
        Validate.notNull(exerciseEvent, "exerciseEvent");
        Validate.notNull(res, "res");
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        if (exerciseEvent.getRecoveryTime() == 0) {
            SingleTrackerStat recoveryTime = new SingleTrackerStat(context, res.getString(R.string.tracker_recovery_time), R.string.glyph_duration);
            CharSequence notAvailable = Formatter.getSubtextSpannable(context, context.getString(R.string.no_value), 0);
            recoveryTime.setValue(notAvailable);
            return recoveryTime;
        }
        DoubleTrackerStat recoveryTime2 = new DoubleTrackerStat(context, res.getString(R.string.tracker_recovery_time), R.string.glyph_duration);
        TimeSpan recovery = TimeSpan.fromSeconds(exerciseEvent.getRecoveryTime());
        CharSequence recoveryTimeValue = recovery.formatTimeShortNotationStyled(context);
        recoveryTime2.setValue(recoveryTimeValue);
        DateTime endTime = exerciseEvent.getEndTime().plusSeconds((int) recovery.getTotalSeconds());
        CharSequence recoveryEndTimeValue = Formatter.formatTimeForLocale(context, endTime, Locale.getDefault());
        recoveryTime2.setSubValue(recoveryEndTimeValue);
        if (DateTimeComparator.getDateOnlyInstance().compare(endTime, DateTime.now()) == 0) {
            dateEvent = res.getString(R.string.tracker_recovery_time_today);
        } else if (DateTimeComparator.getDateOnlyInstance().compare(endTime, DateTime.now().minusDays(1)) == 0) {
            dateEvent = res.getString(R.string.tracker_recovery_time_yesterday);
        } else if (DateTimeComparator.getDateOnlyInstance().compare(endTime, DateTime.now().plusDays(1)) == 0) {
            dateEvent = res.getString(R.string.tracker_recovery_time_tomorrow);
        } else {
            String recoverDate = KAppDateFormatter.formatToMonthDay(endTime);
            dateEvent = String.format(res.getString(R.string.tracker_recovery_time_at), recoverDate);
        }
        recoveryTime2.setSubTitle(dateEvent);
        return recoveryTime2;
    }

    public static BaseTrackerStat getPaceStat(RunEvent event, Resources res, Context context, boolean isMetric) {
        Validate.notNull(event, "event");
        Validate.notNull(res, "res");
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        SingleTrackerStat pace = new SingleTrackerStat(context, context.getString(R.string.tracker_pace), R.string.glyph_pace);
        double paceValue = isMetric ? event.getPace() : event.getPace() * 1.60934d;
        CharSequence notAvailable = Formatter.getSubtextSpannable(context, context.getString(R.string.no_value), 0);
        if (paceValue > Constants.SPLITS_ACCURACY) {
            notAvailable = TimeSpan.fromMilliseconds(paceValue).formatTimePrimeNotation(context);
        }
        pace.setValue(notAvailable);
        return pace;
    }

    public static BaseTrackerStat getAverageSpeedStat(BikeEvent event, Resources res, Context context, boolean isMetric) {
        SingleTrackerStat speed = new SingleTrackerStat(context, res.getString(R.string.tracker_average_speed), R.string.glyph_speed);
        int averageSpeed = event.getAverageSpeed();
        CharSequence notAvailable = Formatter.getSubtextSpannable(context, context.getString(R.string.no_value), 0);
        if (averageSpeed > 0) {
            notAvailable = Formatter.formatSpeedStat(context, R.array.MerticSmallUnitFormat, averageSpeed, isMetric);
        }
        speed.setValue(notAvailable);
        return speed;
    }

    public static BaseTrackerStat getTopSpeedStat(BikeEvent event, Resources res, Context context, boolean isMetric) {
        SingleTrackerStat stat = new SingleTrackerStat(context, res.getString(R.string.tracker_top_speed), R.string.glyph_speed);
        int maxSpeed = event.getMaxSpeed();
        CharSequence notAvailable = Formatter.getSubtextSpannable(context, context.getString(R.string.no_value), 0);
        if (maxSpeed > 0) {
            notAvailable = Formatter.formatSpeedStat(context, R.array.MerticSmallUnitFormat, maxSpeed, isMetric);
        }
        stat.setValue(notAvailable);
        return stat;
    }

    public static BaseTrackerStat getElevationMaxStat(BikeEvent event, Resources res, Context context, boolean isMetric) {
        DoubleTrackerStat stat = new DoubleTrackerStat(context, res.getString(R.string.tracker_elevation_max), R.string.glyph_elevation);
        double maxElevation = event.getMaxAltitude();
        CharSequence notAvailable = Formatter.getSubtextSpannable(context, context.getString(R.string.no_value), 0);
        if (maxElevation > Constants.SPLITS_ACCURACY) {
            notAvailable = Formatter.formatElevation(context, R.array.MerticSmallUnitFormat, maxElevation, isMetric);
        }
        stat.setValue(notAvailable);
        return stat;
    }

    public static BaseTrackerStat getElevationLossStat(AltitudeEvent event, Resources res, Context context, boolean isMetric) {
        DoubleTrackerStat stat = new DoubleTrackerStat(context, res.getString(R.string.tracker_elevation_loss), R.string.glyph_elevation);
        stat.setSubTitle(R.string.tracker_elevation_min);
        double elevationLoss = event.getTotalAltitudeLoss();
        CharSequence notAvailable = Formatter.getSubtextSpannable(context, context.getString(R.string.no_value), 0);
        if (elevationLoss > Constants.SPLITS_ACCURACY) {
            notAvailable = Formatter.formatElevation(context, R.array.MerticSmallUnitFormat, -elevationLoss, isMetric);
        }
        stat.setValue(notAvailable);
        stat.setSubValue(Formatter.formatElevation(context, event.getMinAltitude(), isMetric));
        return stat;
    }

    public static BaseTrackerStat getElevationGainStat(AltitudeEvent event, Resources res, Context context, boolean isMetric) {
        DoubleTrackerStat stat = new DoubleTrackerStat(context, res.getString(R.string.tracker_elevation_gain), R.string.glyph_elevation);
        stat.setSubTitle(R.string.tracker_elevation_max);
        double elevationGain = event.getTotalAltitudeGain();
        CharSequence notAvailable = Formatter.getSubtextSpannable(context, context.getString(R.string.no_value), 0);
        if (elevationGain > Constants.SPLITS_ACCURACY) {
            notAvailable = Formatter.formatElevation(context, R.array.MerticSmallUnitFormat, elevationGain, isMetric);
        }
        stat.setValue(notAvailable);
        stat.setSubValue(Formatter.formatElevation(context, event.getMaxAltitude(), isMetric));
        return stat;
    }

    public static BaseTrackerStat getUvExposure(UserEvent event, Resources res, Context context) {
        SingleTrackerStat stat = new SingleTrackerStat(context, res.getString(R.string.tracker_uv_exposure), R.string.glyph_UV);
        CharSequence notAvailable = Formatter.getSubtextSpannable(context, context.getString(R.string.no_value), 0);
        if (event.getUvExposure() > 0) {
            notAvailable = Formatter.formatUV(context, R.array.MerticSmallUnitFormat, event.getUvExposure());
        }
        stat.setValue(notAvailable);
        return stat;
    }

    public static BaseTrackerStat getTrainingEffectStat(ExerciseEventBase event, Context context) {
        SingleTrackerStat stat = new DescriptiveTextTrackerStat(context, context.getString(R.string.tracker_exercise_training_effect), R.string.glyph_training_effect);
        double trainingEffect = event.getTrainingEffect();
        if (trainingEffect > Constants.SPLITS_ACCURACY) {
            stat.setValue(event.getFitnessBenefitMsg());
        } else {
            stat.setValue(context.getResources().getString(R.string.no_value));
        }
        return stat;
    }

    public static BaseTrackerStat getSleepRestorationStat(SleepEvent event, Context context) {
        SingleTrackerStat stat = new DescriptiveTextTrackerStat(context, context.getString(R.string.tracker_exercise_sleep_restoration), R.string.glyph_sleep_restoration);
        String sleepRestorationMsg = event.getSleepRestorationMsg();
        String value = TextUtils.isEmpty(sleepRestorationMsg) ? context.getString(R.string.no_value) : sleepRestorationMsg;
        stat.setValue(value);
        return stat;
    }

    public static BaseTrackerStat getAverageTimePerGolfCourse(int averageTimeSeconds, Context context) {
        CharSequence notAvailable = Formatter.getSubtextSpannable(context, context.getString(R.string.no_value), 0);
        SingleTrackerStat stat = new SingleTrackerStat(context, context.getString(R.string.tracker_golf_pace_of_play), R.string.glyph_timer);
        if (averageTimeSeconds != 0) {
            notAvailable = Formatter.formatTimeInHoursAndMin(context, R.array.MerticSmallUnitFormat, averageTimeSeconds);
        }
        stat.setValue(notAvailable);
        return stat;
    }

    public static BaseTrackerStat getLongestDrive(double longestDrive, boolean isMetric, Context context) {
        SingleTrackerStat stat = new SingleTrackerStat(context, context.getString(R.string.tracker_golf_longest_drive), R.string.glyph_longest_drive);
        CharSequence notAvailable = Formatter.getSubtextSpannable(context, context.getString(R.string.no_value), 0);
        if (longestDrive != Constants.SPLITS_ACCURACY) {
            notAvailable = Formatter.formatLongestDrive(longestDrive, context, isMetric);
        }
        stat.setValue(notAvailable);
        return stat;
    }

    public static BaseTrackerStat getNumberAtParOrBetter(int numberAtPar, Context context) {
        SingleTrackerStat stat = new SingleTrackerStat(context, context.getString(R.string.tracker_golf_par_or_better), R.string.glyph_check);
        stat.setValue(Formatter.formatNumberOfTimes(context, numberAtPar));
        return stat;
    }

    public static BaseTrackerStat getTotalDistance(int distanceCM, boolean isMetric, Context context) {
        SingleTrackerStat stat = new SingleTrackerStat(context, context.getString(R.string.tracker_header_steps_distance), R.string.glyph_distance);
        CharSequence notAvailable = Formatter.getSubtextSpannable(context, context.getString(R.string.no_value), 0);
        if (distanceCM != 0) {
            notAvailable = Formatter.formatDistanceStat(context, R.array.MerticSmallUnitFormat, distanceCM, isMetric);
        }
        stat.setValue(notAvailable);
        return stat;
    }

    public static BaseTrackerStat getTotalSteps(int steps, Context context) {
        SingleTrackerStat stat = new SingleTrackerStat(context, context.getString(R.string.tracker_steps), R.string.glyph_steps);
        CharSequence notAvailable = Formatter.getSubtextSpannable(context, context.getString(R.string.no_value), 0);
        if (steps != 0) {
            notAvailable = String.valueOf(steps);
        }
        stat.setValue(notAvailable);
        return stat;
    }

    public static BaseTrackerStat getCaloriesBurned(int calories, Context context) {
        SingleTrackerStat stat = new SingleTrackerStat(context, context.getString(R.string.tracker_calories_burned), R.string.glyph_calories);
        stat.setValue(Formatter.formatCalories(context, R.array.MerticSmallUnitFormat, calories));
        return stat;
    }
}

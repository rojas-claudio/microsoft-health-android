package com.microsoft.kapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.HomeActivity;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.fragments.ManageTilesFragment;
import com.microsoft.kapp.guidedworkout.WorkoutPlanSummaryDetails;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.SyncedWorkoutInfo;
import com.microsoft.kapp.models.guidedworkout.BaseGuidedWorkoutItem;
import com.microsoft.kapp.models.guidedworkout.GuidedWorkoutUnitType;
import com.microsoft.kapp.services.guidedworkout.GuidedWorkoutService;
import com.microsoft.kapp.services.healthandfitness.models.CircuitGroupType;
import com.microsoft.kapp.services.healthandfitness.models.CircuitType;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutCircuit;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutExercise;
import com.microsoft.kapp.utils.DialogManager;
import com.microsoft.krestsdk.models.CompletionType;
import com.microsoft.krestsdk.models.ExerciseTraversalType;
import com.microsoft.krestsdk.models.FavoriteWorkoutPlan;
import com.microsoft.krestsdk.models.GuidedWorkoutEvent;
import com.microsoft.krestsdk.models.ScheduledWorkout;
import com.microsoft.krestsdk.models.UserWorkoutStatus;
import java.util.HashSet;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Days;
/* loaded from: classes.dex */
public class GuidedWorkoutUtils {
    private static final String TAG = GuidedWorkoutUtils.class.getSimpleName();

    public static void playVideo(String url, double screenWidth, double screenHeight, Context context, Callback<Void> callback) {
        VideoUtils.playVideo(url, screenWidth, screenHeight, context, callback);
    }

    public static ScheduledWorkout getNextWorkout(SyncedWorkoutInfo lastSyncedWorkoutInfo, List<ScheduledWorkout> allWorkouts, GuidedWorkoutEvent lastCompletedWorkoutEvent, FavoriteWorkoutPlan currentSubscribedPlan) {
        Validate.notNull(lastSyncedWorkoutInfo, "lastSyncedWorkoutInfo");
        Validate.notNull(lastCompletedWorkoutEvent, "lastCompletedWorkoutEvent");
        Validate.notNull(allWorkouts, "allWorkouts");
        Validate.notNull(currentSubscribedPlan, "currentSubscribedPlanId");
        try {
            if ((lastSyncedWorkoutInfo.getWorkoutPlanId().equals(lastCompletedWorkoutEvent.getWorkoutPlanID()) && lastSyncedWorkoutInfo.getDay() == lastCompletedWorkoutEvent.getWorkoutDayID() && lastSyncedWorkoutInfo.getWeek() == lastCompletedWorkoutEvent.getWorkoutWeekID() && lastCompletedWorkoutEvent.getWorkoutPlanInstanceID() == lastSyncedWorkoutInfo.getWorkoutPlanInstanceId()) || lastCompletedWorkoutEvent.getStartTime().isAfter(lastSyncedWorkoutInfo.getTimeSynced()) || lastSyncedWorkoutInfo.getTimeSynced().isBefore(currentSubscribedPlan.getTimeSubscribed())) {
                ScheduledWorkout currentWorkout = allWorkouts.get(0);
                if (isCompletedOrSkippedWorkout(currentWorkout)) {
                    for (int i = 1; i < allWorkouts.size(); i++) {
                        ScheduledWorkout currentWorkout2 = allWorkouts.get(i);
                        if (!isCompletedOrSkippedWorkout(currentWorkout2)) {
                            ScheduledWorkout previousWorkout = allWorkouts.get(i - 1);
                            if (lastSyncedWorkoutInfo.getWorkoutPlanId().equals(previousWorkout.getWorkoutPlanId()) && lastCompletedWorkoutEvent.getWorkoutDayID() == previousWorkout.getDay() && lastCompletedWorkoutEvent.getWorkoutWeekID() == previousWorkout.getWeekId()) {
                                if (isRestDay(previousWorkout, currentWorkout2, lastCompletedWorkoutEvent)) {
                                    return null;
                                }
                                return currentWorkout2;
                            }
                            return currentWorkout2;
                        }
                    }
                } else {
                    return currentWorkout;
                }
            }
        } catch (Exception e) {
            KLog.e(TAG, "Exception syning the next workout to the device", e);
        }
        return null;
    }

    public static boolean isRestDay(ScheduledWorkout lastCompletedWorkout, ScheduledWorkout nextWorkout, GuidedWorkoutEvent lastCompletedWorkoutEvent) {
        int daysSinceLastCompletedWorkout = 0;
        if (lastCompletedWorkout != null && nextWorkout != null && lastCompletedWorkoutEvent != null) {
            int dayIndexOfLastCompletedWorkout = (lastCompletedWorkout.getDay() - 1) + ((lastCompletedWorkout.getWeekId() - 1) * 7);
            int dayIndexOfNextWorkout = (nextWorkout.getDay() - 1) + ((nextWorkout.getWeekId() - 1) * 7);
            if (lastCompletedWorkoutEvent.getEndTime() != null) {
                daysSinceLastCompletedWorkout = Days.daysBetween(lastCompletedWorkoutEvent.getEndTime(), DateTime.now()).getDays();
            }
            return (dayIndexOfLastCompletedWorkout + daysSinceLastCompletedWorkout) + 1 < dayIndexOfNextWorkout;
        }
        KLog.e(TAG, "Could not calculate rest day due to null parameters passed.");
        return false;
    }

    public static boolean isCompletedOrSkippedWorkout(ScheduledWorkout currentWorkout) {
        return currentWorkout.getTypedWorkoutStatus().equals(UserWorkoutStatus.COMPLETED) || currentWorkout.getTypedWorkoutStatus().equals(UserWorkoutStatus.SKIPPED) || currentWorkout.getTypedWorkoutStatus().equals(UserWorkoutStatus.SKIPPED_DEPRECATED) || currentWorkout.getTypedWorkoutStatus().equals(UserWorkoutStatus.ERRORED);
    }

    public static int getWorkoutSyncErrorText(int errorId) {
        switch (errorId) {
            case 1:
                return R.string.guided_workout_sync_tile_disabled_error;
            case 2:
                return R.string.guided_workout_sync_no_band_paired_mode_error;
            case 3:
                return R.string.guided_workout_sync_tile_open_error;
            case 4:
                return R.string.sync_failed_cloud_exception;
            default:
                return R.string.guided_workout_sync_error;
        }
    }

    public static int getWorkoutSubscribeSyncErrorText(int errorId) {
        switch (errorId) {
            case 1:
                return R.string.guided_workout_subscribe_tile_disabled_error;
            case 2:
                return R.string.guided_workout_subscribe_no_band_paired_mode_error;
            case 3:
                return R.string.guided_workout_subscribe_tile_open_error;
            case 4:
                return R.string.sync_failed_cloud_exception;
            default:
                return R.string.guided_workout_subscribe_error;
        }
    }

    public static boolean isRestCircuit(WorkoutCircuit circuit) {
        WorkoutExercise[] exercisesList;
        WorkoutExercise exercise;
        if (circuit == null || (exercisesList = circuit.getExerciseList()) == null || exercisesList.length != 1 || (exercise = exercisesList[0]) == null) {
            return false;
        }
        return Constants.REST_EXERCISE_ID.equalsIgnoreCase(exercise.getId());
    }

    public static boolean isWorkoutSynced(GuidedWorkoutService guidedWorkoutSyncService, ScheduledWorkout scheduledWorkout) {
        if (guidedWorkoutSyncService == null || scheduledWorkout == null) {
            return false;
        }
        SyncedWorkoutInfo lastSyncedWorkout = guidedWorkoutSyncService.getLastSyncedWorkout();
        return isSameScheduledWorkout(lastSyncedWorkout, scheduledWorkout);
    }

    public static boolean isSameScheduledWorkout(ScheduledWorkout scheduledWorkout1, ScheduledWorkout scheduledWorkout2) {
        return (scheduledWorkout1 == null || scheduledWorkout2 == null) ? scheduledWorkout1 == null && scheduledWorkout2 == null : scheduledWorkout1.getWorkoutPlanId() != null && scheduledWorkout1.getWorkoutPlanId().equals(scheduledWorkout2.getWorkoutPlanId()) && scheduledWorkout1.getWeekId() == scheduledWorkout2.getWeekId() && scheduledWorkout1.getDay() == scheduledWorkout2.getDay() && scheduledWorkout1.getWorkoutIndex() == scheduledWorkout2.getWorkoutIndex();
    }

    public static boolean isSameScheduledWorkout(SyncedWorkoutInfo lastSyncedWorkoutInfo, ScheduledWorkout scheduledWorkout) {
        return lastSyncedWorkoutInfo != null && scheduledWorkout != null && lastSyncedWorkoutInfo.getWorkoutPlanId() != null && lastSyncedWorkoutInfo.getWorkoutPlanId().equals(scheduledWorkout.getWorkoutPlanId()) && lastSyncedWorkoutInfo.getWeek() == scheduledWorkout.getWeekId() && lastSyncedWorkoutInfo.getDay() == scheduledWorkout.getDay() && lastSyncedWorkoutInfo.getWorkoutIndex() == scheduledWorkout.getWorkoutIndex();
    }

    public static boolean isSameCompletedWorkout(SyncedWorkoutInfo lastSyncedWorkoutInfo, GuidedWorkoutEvent guidedWorkoutEvent) {
        return lastSyncedWorkoutInfo != null && guidedWorkoutEvent != null && lastSyncedWorkoutInfo.getWorkoutPlanId() != null && TextUtils.equals(lastSyncedWorkoutInfo.getWorkoutPlanId(), guidedWorkoutEvent.getWorkoutPlanID()) && lastSyncedWorkoutInfo.getWeek() == guidedWorkoutEvent.getWorkoutWeekID() && lastSyncedWorkoutInfo.getDay() == guidedWorkoutEvent.getWorkoutDayID() && lastSyncedWorkoutInfo.getWorkoutIndex() == guidedWorkoutEvent.getWorkoutIndex() && lastSyncedWorkoutInfo.getWorkoutPlanInstanceId() == guidedWorkoutEvent.getWorkoutPlanInstanceID();
    }

    public static boolean showFilter(String filter) {
        return filter != null && (filter.equals("level") || filter.equals("duration"));
    }

    public static void showGuidedWorkoutTileDisabledDialog(final Activity activity) {
        if (activity != null) {
            DialogManager.showDialog(activity, Integer.valueOf((int) R.string.connection_with_device_error_title), Integer.valueOf((int) R.string.guided_workout_subscribe_tile_disabled_error), (int) R.string.guided_workout_sync_error_manage_tiles_text, new DialogInterface.OnClickListener() { // from class: com.microsoft.kapp.utils.GuidedWorkoutUtils.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(activity, HomeActivity.class);
                    intent.putExtra(HomeActivity.STARTING_PAGE, ManageTilesFragment.class.getSimpleName());
                    activity.startActivity(intent);
                }
            }, (DialogInterface.OnClickListener) null, DialogManager.Priority.HIGH);
        }
    }

    public static HashSet<String> getFavoritedWorkoutPlanIdSet(List<FavoriteWorkoutPlan> favoriteWorkoutPlansList) {
        String planId;
        if (favoriteWorkoutPlansList != null) {
            HashSet<String> favoritePlanIdSet = new HashSet<>();
            for (FavoriteWorkoutPlan favorite : favoriteWorkoutPlansList) {
                if (favorite != null && (planId = favorite.getWorkoutPlanId()) != null) {
                    favoritePlanIdSet.add(planId);
                }
            }
            return favoritePlanIdSet;
        }
        return null;
    }

    public static void flagFavoritedWorkoutPlans(List<WorkoutPlanSummaryDetails> workoutPlanBrowseDetailsList, HashSet<String> favoritePlanIdSet) {
        if (workoutPlanBrowseDetailsList != null && favoritePlanIdSet != null) {
            for (WorkoutPlanSummaryDetails summary : workoutPlanBrowseDetailsList) {
                if (summary != null) {
                    summary.setIsFavorite(favoritePlanIdSet.contains(summary.getWorkoutPlanId()));
                }
            }
        }
    }

    public static String getUnitTextForPostGuidedWorkout(Context context, BaseGuidedWorkoutItem baseGuidedWorkoutItem, GuidedWorkoutUnitType guidedWorkoutUnitType, boolean isMetric) {
        String unitText = null;
        int unit = 0;
        if (baseGuidedWorkoutItem != null && guidedWorkoutUnitType != GuidedWorkoutUnitType.Hide) {
            switch (guidedWorkoutUnitType) {
                case Calories:
                    unit = baseGuidedWorkoutItem.getCalories();
                    unitText = Integer.toString(unit);
                    break;
                case Distance:
                    unit = baseGuidedWorkoutItem.getDistance();
                    unitText = Formatter.formatDistanceStat(context, unit * 100, isMetric).toString();
                    break;
                case Time:
                    unit = baseGuidedWorkoutItem.getDuration();
                    unitText = Formatter.formatDurationSecondsToSpannableColon(unit).toString();
                    break;
                case Reps:
                    unit = baseGuidedWorkoutItem.getRepetitions();
                    unitText = Integer.toString(baseGuidedWorkoutItem.getRepetitions());
                    if (baseGuidedWorkoutItem.isRepsEstimated()) {
                        unitText = unitText + "*";
                        break;
                    }
                    break;
            }
        }
        if (unit == 0) {
            return "--";
        }
        return unitText;
    }

    public static String getCircuitIcon(Context context, CircuitGroupType type) {
        switch (type) {
            case CircuitTask:
            case CircuitTime:
                return context.getString(R.string.glyph_circuit);
            case FreePlay:
                return context.getString(R.string.glyph_free);
            case Interval:
                return context.getString(R.string.glyph_interval);
            case Rest:
                return context.getString(R.string.glyph_rest);
            default:
                return context.getString(R.string.glyph_list);
        }
    }

    public static int getCircuitHeaderColor(Context context, CircuitType type, boolean isChild) {
        if (context == null) {
            return 0;
        }
        switch (type) {
            case WarmUp:
                return isChild ? context.getResources().getColor(R.color.WarmupLightColor) : context.getResources().getColor(R.color.WarmupDarkColor);
            case CoolDown:
                return isChild ? context.getResources().getColor(R.color.CooldownLightColor) : context.getResources().getColor(R.color.CooldownDarkColor);
            default:
                return isChild ? context.getResources().getColor(R.color.WorkoutLightColor) : context.getResources().getColor(R.color.WorkoutDarkColor);
        }
    }

    public static String getCircuitTypeString(Context context, CircuitType type) {
        switch (type) {
            case WarmUp:
                return context.getString(R.string.workout_type_warmup);
            case CoolDown:
                return context.getString(R.string.workout_type_cooldown);
            default:
                return context.getString(R.string.workout_type_workout);
        }
    }

    public static String getCircuitGroupTypeTitle(Context context, CircuitGroupType type) {
        if (context == null) {
            return "";
        }
        switch (type) {
            case CircuitTask:
                return context.getString(R.string.guided_workout_group_type_circuit_task);
            case CircuitTime:
                return context.getString(R.string.guided_workout_group_type_circuit_time);
            case FreePlay:
                return context.getString(R.string.guided_workout_group_type_free_play);
            case Interval:
                return context.getString(R.string.guided_workout_group_type_interval);
            case Rest:
                return context.getString(R.string.guided_workout_group_type_rest);
            default:
                return context.getString(R.string.guided_workout_group_type_list);
        }
    }

    public static String getCircuitGroupDescription(Context context, WorkoutCircuit circuit) {
        String description;
        if (context == null) {
            return "";
        }
        CircuitGroupType type = circuit.getGroupType();
        switch (type) {
            case CircuitTask:
                if (circuit.getCompletionValue() <= 1) {
                    description = context.getString(R.string.guided_workout_group_description_circuit_task_single);
                    break;
                } else {
                    description = String.format(context.getString(R.string.guided_workout_group_description_circuit_task_multiple), Integer.valueOf(circuit.getCompletionValue()));
                    break;
                }
            case CircuitTime:
                description = String.format(context.getString(R.string.guided_workout_group_description_circuit_time), Integer.valueOf(ConversionUtils.SecondsToMinutes(circuit.getCompletionValue())));
                break;
            case FreePlay:
                description = String.format(context.getString(R.string.guided_workout_group_description_free_time), circuit.getName(), Integer.valueOf(ConversionUtils.SecondsToMinutes(circuit.getCompletionValue())));
                break;
            case Interval:
                if (circuit.getExerciseList().length <= 1) {
                    description = context.getString(R.string.guided_workout_group_description_intervals_single);
                    break;
                } else {
                    description = context.getString(R.string.guided_workout_group_description_intervals_multiple);
                    break;
                }
            case Rest:
                if (circuit.getCompletionValue() <= 1) {
                    description = context.getString(R.string.guided_workout_group_description_rest_as_needed);
                    break;
                } else {
                    description = String.format(context.getString(R.string.guided_workout_group_description_rest_time), Integer.valueOf(ConversionUtils.SecondsToMinutes(circuit.getCompletionValue())));
                    break;
                }
            default:
                if (circuit.getExerciseList().length <= 1) {
                    description = context.getString(R.string.guided_workout_group_description_list_single);
                    break;
                } else {
                    description = context.getString(R.string.guided_workout_group_description_list_multiple);
                    break;
                }
        }
        if (circuit.getDropLastRest()) {
            return context.getString(R.string.guided_workout_group_description_circuit_drop_last_rest, description);
        }
        return description;
    }

    public static String getRepsDescriptionFromExercise(Context context, WorkoutExercise exercise, CircuitGroupType groupType) {
        if (exercise == null) {
            KLog.w(TAG, "exercise should not be null!");
            return "";
        }
        String extraDirectiveText = "";
        if (groupType == CircuitGroupType.List) {
            int sets = getMaxIntegerfromString(exercise.getSets());
            int reps = exercise.getCompletionValue();
            if (sets > 1) {
                String repString = "";
                if (exercise.getCompletionType() == CompletionType.SECONDS) {
                    if (reps > 0) {
                        repString = getDurationString(context, reps);
                    }
                } else if (exercise.getCompletionType() == CompletionType.REPETITIONS) {
                    repString = (reps < 1 || reps >= Integer.MAX_VALUE) ? context.getString(R.string.guided_workout_exercise_details_max_reps) : LockedStringUtils.unitReps(reps, context.getResources());
                }
                if (!repString.equals("")) {
                    return context.getString(R.string.guided_workout_exercise_details_sets_full, Integer.valueOf(sets), repString, getDurationString(context, getMaxIntegerfromString(exercise.getRestSeconds()), true));
                }
            } else if (sets == 1 && reps >= Integer.MAX_VALUE) {
                return context.getString(R.string.guided_workout_exercise_details_max_reps);
            }
        }
        String reps2 = exercise.getRepsTimes();
        String duration = exercise.getRepsDuration();
        if (reps2 != null || duration != null) {
            String repsDur = reps2 + duration;
            if (repsDur.contains(Constants.CHAR_STARSTAR)) {
                extraDirectiveText = context.getString(R.string.workout_exercise_reps_as_mauch_as_possible);
            } else if (repsDur.contains(Constants.CHAR_AT) || repsDur.contains("*")) {
                extraDirectiveText = context.getString(R.string.workout_exercise_each_side);
            }
        }
        int completionValue = exercise.getCompletionValue();
        CompletionType completionType = exercise.getCompletionType();
        if (completionValue >= Integer.MAX_VALUE) {
            switch (completionType) {
                case SECONDS:
                    return String.format(extraDirectiveText, context.getString(R.string.workout_exercise_max_seconds_description));
                case REPETITIONS:
                    return String.format(extraDirectiveText, context.getString(R.string.workout_exercise_max_repetitions_description));
                case METERS:
                    return String.format(extraDirectiveText, context.getString(R.string.workout_exercise_max_meters_description));
                default:
                    return String.format(extraDirectiveText, context.getString(R.string.workout_exercise_max_default_description));
            }
        }
        switch (completionType) {
            case SECONDS:
                String first = context.getString(R.string.workout_exercise_reps_name_gr_120_seconds);
                String second = Formatter.formatDurationSecondsToHrMinSecsLocked(context, completionValue);
                return String.format(first, second, extraDirectiveText);
            case REPETITIONS:
                return context.getString(R.string.workout_reps_name_with_extra, LockedStringUtils.unitRepetitions(completionValue, context.getResources()), extraDirectiveText);
            case METERS:
                return context.getString(R.string.workout_reps_name_with_extra, LockedStringUtils.unitMeter(completionValue, context.getResources()), extraDirectiveText);
            case CALORIES:
                return context.getString(R.string.workout_reps_name_with_extra, LockedStringUtils.unitCalories(completionValue, context.getResources()), extraDirectiveText);
            case JOULES:
                return context.getString(R.string.workout_reps_name_with_extra, LockedStringUtils.unitJoule(completionValue, context.getResources()), extraDirectiveText);
            case HEART_RATE:
                return context.getString(R.string.workout_exercise_reps_name_HR, Integer.valueOf(completionValue));
            default:
                return String.format(extraDirectiveText, Integer.valueOf(completionValue));
        }
    }

    public static String getRestDurationString(Context context, WorkoutExercise exercise, CircuitGroupType groupType) {
        boolean getDuration;
        int duration = getMaxIntegerfromString(exercise.getRestSeconds());
        int sets = getMaxIntegerfromString(exercise.getSets());
        switch (groupType) {
            case CircuitTask:
            case CircuitTime:
            case Interval:
            case Rest:
                getDuration = true;
                break;
            case FreePlay:
            default:
                getDuration = false;
                break;
            case List:
                if ((sets == 1 && duration > 0) || (duration == 0 && exercise.getShowInterstitial())) {
                    getDuration = true;
                    break;
                } else {
                    getDuration = false;
                    break;
                }
        }
        if (getDuration) {
            if (duration > 0) {
                return getDurationString(context, duration);
            }
            if (duration == 0 && exercise.getShowInterstitial()) {
                return context.getString(R.string.guided_workout_group_description_as_needed);
            }
        }
        return "";
    }

    public static String getDurationString(Context context, int duration) {
        return getDurationString(context, duration, false);
    }

    public static String getDurationString(Context context, int duration, boolean shortForm) {
        if (duration > 0) {
            return Formatter.formatDurationSecondsToHrMinSecsLocked(context, duration);
        }
        return shortForm ? context.getString(R.string.guided_workout_group_description_as_needed) : context.getString(R.string.guided_workout_group_description_rest_as_needed);
    }

    public static String getRestDescriptionFromCircuit(Context context, WorkoutCircuit circuit) {
        if (circuit == null) {
            KLog.w(TAG, "Circuit should not be null!");
            return "";
        }
        WorkoutExercise[] exercisesList = circuit.getExerciseList();
        if (exercisesList == null) {
            KLog.w(TAG, "WorkoutExercise list should not be null!");
            return "";
        }
        int maxRest = -1;
        int minRest = Integer.MAX_VALUE;
        for (WorkoutExercise exercise : exercisesList) {
            if (exercise == null) {
                KLog.w(TAG, "exercise should not be null!");
            } else {
                String exerciseRestTime = exercise.getRestSeconds();
                if (exerciseRestTime == null) {
                    KLog.w(TAG, "Exercise RestTime should not be null!");
                } else if (exerciseRestTime.contains("*")) {
                    return context.getString(R.string.workout_circuit_description_rest_as_little_as_possible);
                } else {
                    int restTime = getMaxIntegerfromString(exerciseRestTime);
                    if (restTime > maxRest) {
                        maxRest = restTime;
                    }
                    if (restTime < minRest) {
                        minRest = restTime;
                    }
                }
            }
        }
        if (circuit.getExerciseTraversalType() == ExerciseTraversalType.SetOrder) {
            if (minRest == maxRest) {
                return context.getString(R.string.workout_circuit_description_rest_set, LockedStringUtils.unitSeconds(minRest, context.getResources()));
            }
            return context.getString(R.string.workout_circuit_description_rest_set_range, Integer.valueOf(minRest), Integer.valueOf(maxRest));
        } else if (maxRest == 0) {
            return context.getString(R.string.workout_circuit_description_no_rest);
        } else {
            if (maxRest > 0) {
                return context.getString(R.string.workout_circuit_description_rest_round, LockedStringUtils.unitSeconds(maxRest, context.getResources()));
            }
            KLog.w(TAG, "Error getting the description of the Circuit Rest Time!");
            return "";
        }
    }

    public static int getMaxIntegerfromString(String text) {
        if (text == null) {
            return -1;
        }
        if (text.contains("-")) {
            String[] numbersArray = text.split("-");
            int max = -1;
            for (String number : numbersArray) {
                try {
                    int num = Integer.parseInt(number.replaceAll("[^0-9]", ""));
                    if (num > max) {
                        max = num;
                    }
                } catch (NumberFormatException ex) {
                    KLog.e(TAG, "Invalid String argument passed. Could not convert to Integer format ", ex);
                }
            }
            return max;
        }
        String number2 = text.replaceAll("[^0-9]", "");
        try {
            return Integer.parseInt(number2);
        } catch (NumberFormatException ex2) {
            KLog.e(TAG, "Invalid String argument passed. Could not convert to Integer format ", ex2);
            return -1;
        }
    }

    public static String getGroupDurationString(Context context, WorkoutCircuit circuit) {
        int completionValue = circuit.getCompletionValue();
        switch (circuit.getGroupType()) {
            case CircuitTask:
                return LockedStringUtils.unitRounds(completionValue, context.getResources());
            case CircuitTime:
                if (completionValue > 0) {
                    return getDurationString(context, completionValue);
                }
                break;
            case FreePlay:
                if (circuit.getExerciseList().length > 0) {
                    return getRepsDescriptionFromExercise(context, circuit.getExerciseList()[0], circuit.getGroupType());
                }
                break;
            case Interval:
                if (circuit.getExerciseList().length > 0) {
                    int value = getMaxIntegerfromString(circuit.getExerciseList()[0].getSets());
                    return LockedStringUtils.unitSets(value, context.getResources());
                }
                break;
            case Rest:
                if (circuit.getExerciseList().length > 0) {
                    return getRestDurationString(context, circuit.getExerciseList()[0], circuit.getGroupType());
                }
                break;
        }
        return "";
    }
}

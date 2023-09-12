package com.microsoft.kapp.utils;

import android.content.Context;
import com.microsoft.kapp.R;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.views.UserEventSummaryListView;
import com.microsoft.krestsdk.models.EventType;
import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.GoalValueHistoryDto;
import com.microsoft.krestsdk.models.GoalValueRecordDto;
import com.microsoft.krestsdk.models.SleepEvent;
import com.microsoft.krestsdk.models.UserEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class HistoryUtils {
    private static final String TAG = HistoryUtils.class.getSimpleName();

    public static DateTime getLastEventStartTime(UserEventSummaryListView recentEventList) {
        DateTime startTime = null;
        if (recentEventList.getLastItem() != null && recentEventList.getLastItem().getUserEvent() != null) {
            startTime = recentEventList.getLastItem().getUserEvent().getStartTime();
        }
        if (startTime == null) {
            return DateTime.now();
        }
        return startTime;
    }

    public static int getIdForType(UserEvent event) {
        if (event.getEventType().equals(EventType.Running)) {
            return 101;
        }
        if (event.getEventType().equals(EventType.Sleeping)) {
            return Constants.SLEEP_FRAGMENT_EVENT_DETAILS_REQUEST;
        }
        if (event.getEventType().equals(EventType.GuidedWorkout)) {
            return Constants.GUIDED_WORKOUT_FRAGMENT_EVENT_DETAILS_REQUEST;
        }
        if (event.getEventType().equals(EventType.Workout)) {
            return 102;
        }
        if (event.getEventType().equals(EventType.Biking)) {
            return 110;
        }
        if (event.getEventType().equals(EventType.Golf)) {
            return Constants.GOLF_FRAGMENT_EVENT_DETAILS_REQUEST;
        }
        return -1;
    }

    public static String getUserEventTypeName(UserEvent event, Context mContext) {
        if (event.getEventType().equals(EventType.Running)) {
            return mContext.getString(R.string.user_event_run_default_name);
        }
        if (event.getEventType().equals(EventType.Sleeping)) {
            boolean isAutoDetected = false;
            if (event instanceof SleepEvent) {
                isAutoDetected = ((SleepEvent) event).getIsAutoSleep();
            }
            return mContext.getString(isAutoDetected ? R.string.user_event_sleep_auto_detect_default_name : R.string.user_event_sleep_default_name);
        } else if (event.getEventType().equals(EventType.GuidedWorkout)) {
            return mContext.getString(R.string.user_event_workout_default_name);
        } else {
            if (event.getEventType().equals(EventType.Workout)) {
                return mContext.getString(R.string.user_event_exercise_default_name);
            }
            if (event.getEventType().equals(EventType.Biking)) {
                return mContext.getString(R.string.user_event_bike_default_name);
            }
            if (event.getEventType().equals(EventType.Golf)) {
                return mContext.getString(R.string.user_event_golf_default_name);
            }
            return "";
        }
    }

    public static HashMap<String, ArrayList<String>> filterValidGoals(List<GoalDto> result) {
        HashMap<String, ArrayList<String>> validGoalsForExistingEvents = new HashMap<>();
        for (GoalDto goal : result) {
            if (goal != null && goal.getValueHistory() != null && !goal.getValueHistory().isEmpty()) {
                GoalValueHistoryDto history = goal.getValueHistory().get(goal.getValueHistory().size() - 1);
                if (history != null && history.getHistoryRecords() != null) {
                    if (!history.getHistoryRecords().isEmpty()) {
                        GoalValueRecordDto record = history.getHistoryRecords().get(history.getHistoryRecords().size() - 1);
                        if (record != null && record.getExtension() != null && record.getExtension().split("EventId:").length > 1) {
                            try {
                                String eventId = record.getExtension().split("EventId:")[1].split("_")[0];
                                KLog.logPrivate(TAG, "" + eventId);
                                if (goal.getName() != null) {
                                    if (validGoalsForExistingEvents.containsKey(eventId)) {
                                        validGoalsForExistingEvents.get(eventId).add(goal.getName());
                                    } else {
                                        ArrayList<String> goalNames = new ArrayList<>();
                                        goalNames.add(goal.getName());
                                        validGoalsForExistingEvents.put(eventId, goalNames);
                                    }
                                } else {
                                    KLog.e(TAG, "error loading personal best event. Invalid goal name");
                                }
                            } catch (NumberFormatException ex) {
                                KLog.e(TAG, "error loading personal best event. Invalid event ID", ex);
                            }
                        } else {
                            KLog.e(TAG, "error loading personal best event. Invalid personal best event ID");
                        }
                    }
                } else {
                    KLog.e(TAG, "error loading personal best event. Invalid personal best history record data");
                }
            } else {
                KLog.e(TAG, "error loading personal best event. Invalid personal best value history data");
            }
        }
        return validGoalsForExistingEvents;
    }

    public static String getFilterTypeString(int filterType, Context mContext) {
        switch (filterType) {
            case Constants.FILTER_TYPE_BESTS /* 251 */:
                return mContext.getString(R.string.label_history_filter_bests);
            case Constants.FILTER_TYPE_SLEEP /* 252 */:
                return mContext.getString(R.string.label_history_filter_sleep);
            case Constants.FILTER_TYPE_RUNS /* 253 */:
                return mContext.getString(R.string.label_history_filter_runs);
            case Constants.FILTER_TYPE_EXERCISES /* 254 */:
                return mContext.getString(R.string.label_history_filter_exercises);
            case 255:
                return mContext.getString(R.string.label_history_filter_guided_workout);
            case 256:
                return mContext.getString(R.string.label_history_filter_biking);
            case 257:
                return mContext.getString(R.string.label_history_filter_golf);
            default:
                return mContext.getString(R.string.label_history_filter_all);
        }
    }

    public static String getFilterTypeMetricString(int filterType, Context mContext) {
        switch (filterType) {
            case Constants.FILTER_TYPE_SLEEP /* 252 */:
                return mContext.getString(R.string.label_history_table_header_time);
            case Constants.FILTER_TYPE_RUNS /* 253 */:
                return mContext.getString(R.string.label_history_table_header_distance);
            case Constants.FILTER_TYPE_EXERCISES /* 254 */:
            case 255:
                return mContext.getString(R.string.label_history_table_header_calories);
            case 256:
            default:
                return mContext.getString(R.string.label_history_table_header_metric);
            case 257:
                return mContext.getString(R.string.label_history_table_header_golf_score);
        }
    }
}

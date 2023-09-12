package com.microsoft.kapp.utils;

import android.text.SpannableStringBuilder;
import com.microsoft.kapp.FontManager;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import com.microsoft.kapp.style.utils.TextSpannerUtils;
import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.GoalValueCurrent;
import com.microsoft.krestsdk.models.GoalValueHistoryDto;
import com.microsoft.krestsdk.models.GoalValueRecordDto;
import com.microsoft.krestsdk.models.GoalValueSummaryDto;
import com.microsoft.krestsdk.models.GoalValueTemplateDto;
import com.microsoft.krestsdk.services.KCloudConstants;
import com.unnamed.b.atv.model.TreeNode;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public class GoalsUtils {
    private static final String TAG = GoalsUtils.class.getSimpleName();

    public static int getGoalValue(GoalDto goal) {
        if (goal == null) {
            KLog.e(TAG, "Goal cannot be null!");
            return -1;
        }
        List<GoalValueSummaryDto> valueSummary = goal.getValueSummary();
        List<GoalValueHistoryDto> valueHistory = goal.getValueHistory();
        GoalValueRecordDto threshold = null;
        if (Validate.isNotNullNotEmpty(valueSummary)) {
            threshold = valueSummary.get(0).getCurrentThreshold();
        } else if (Validate.isNotNullNotEmpty(valueHistory)) {
            List<GoalValueRecordDto> goalValueRecords = valueHistory.get(0).getHistoryThresholds();
            GoalValueRecordDto threshold2 = goalValueRecords.get(goalValueRecords.size() - 1);
            threshold = threshold2;
        }
        if (threshold == null) {
            return -1;
        }
        return ((Double) threshold.getValue()).intValue();
    }

    public static void setGoalValue(GoalDto goal, int value) {
        Validate.notNull(goal, WorkoutSummary.GOAL);
        List<GoalValueSummaryDto> valueSummary = goal.getValueSummary();
        List<GoalValueHistoryDto> valueHistory = goal.getValueHistory();
        GoalValueRecordDto threshold = null;
        if (Validate.isNotNullNotEmpty(valueSummary)) {
            threshold = valueSummary.get(0).getCurrentThreshold();
        } else if (Validate.isNotNullNotEmpty(valueHistory)) {
            List<GoalValueRecordDto> goalValueRecords = valueHistory.get(0).getHistoryThresholds();
            GoalValueRecordDto threshold2 = goalValueRecords.get(goalValueRecords.size() - 1);
            threshold = threshold2;
        }
        if (threshold != null) {
            threshold.setValue(Double.valueOf(value));
        }
    }

    public static String getExtensionValue(GoalDto goal) {
        List<GoalValueRecordDto> historyThresholds;
        List<GoalValueSummaryDto> valueSummary = goal.getValueSummary();
        List<GoalValueHistoryDto> valueHistory = goal.getValueHistory();
        String extension = null;
        if (Validate.isNotNullNotEmpty(valueSummary)) {
            GoalValueCurrent currentValue = valueSummary.get(0).getCurrentvalue();
            if (currentValue != null) {
                extension = currentValue.getExtension();
            }
        } else if (Validate.isNotNullNotEmpty(valueHistory) && (historyThresholds = valueHistory.get(0).getHistoryThresholds()) != null && historyThresholds.size() > 0 && historyThresholds.get(historyThresholds.size() - 1) != null) {
            extension = historyThresholds.get(historyThresholds.size() - 1).getExtension();
        }
        if (extension == null) {
            return null;
        }
        String eventId = getEventId(extension);
        return eventId;
    }

    public static String getGoalName(GoalDto goal) {
        List<GoalValueSummaryDto> valueSummary = goal.getValueSummary();
        List<GoalValueHistoryDto> valueHistory = goal.getValueHistory();
        GoalValueTemplateDto valueTemplate = null;
        if (Validate.isNotNullNotEmpty(valueSummary)) {
            valueTemplate = valueSummary.get(0).getValueTemplate();
        } else if (Validate.isNotNullNotEmpty(valueHistory)) {
            valueTemplate = valueHistory.get(0).getValueTemplate();
        }
        if (valueTemplate == null) {
            return null;
        }
        return valueTemplate.getName();
    }

    private static String getEventId(String extension) {
        List<String> keyValueList = Arrays.asList(extension.split(","));
        String eventId = null;
        for (String keyValue : keyValueList) {
            List<String> KeyValuePairs = Arrays.asList(keyValue.split(TreeNode.NODES_ID_SEPARATOR));
            String keyValuePair = KeyValuePairs.get(0);
            if (keyValuePair != null && KeyValuePairs.get(0).equals(KCloudConstants.VALUE_TEMPLATE_EVENTID)) {
                String eventId2 = KeyValuePairs.get(1);
                eventId = eventId2;
            }
        }
        return eventId;
    }

    public static CharSequence formatGoalText(FontManager fontManager, String goal, String message) {
        CharSequence goalValueText = TextSpannerUtils.spanTextWithCustomTypeface(goal, fontManager.getFontFace(2), 0, goal.length());
        CharSequence goalMessageText = TextSpannerUtils.spanTextWithCustomTypeface(message, fontManager.getFontFace(0), 0, message.length());
        return new SpannableStringBuilder(goalValueText).append(goalMessageText);
    }
}

package com.microsoft.kapp.models.goal;

import android.content.Context;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.krestsdk.models.GoalType;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class GoalProcessorManager {
    private static final String TAG = GoalProcessorManager.class.getSimpleName();
    private final Map<GoalType, GoalProcessor> mGoalProcessorMap;

    @Inject
    public GoalProcessorManager(Context context, SettingsProvider settingsProvider) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Validate.notNull(settingsProvider, "settingsProvider");
        this.mGoalProcessorMap = new HashMap();
        try {
            this.mGoalProcessorMap.put(GoalType.CALORIE, new CaloriesGoalProcessor(context, settingsProvider));
        } catch (Exception e) {
            KLog.e(TAG, "CaloriesGoalProcessor cannot be null!", e);
        }
        try {
            this.mGoalProcessorMap.put(GoalType.STEP, new StepsGoalProcessor(context, settingsProvider));
        } catch (Exception e2) {
            KLog.e(TAG, "StepsGoalProcessor cannot be null!", e2);
        }
    }

    public GoalProcessor getGoalProcessor(GoalType goalType) {
        Validate.notNull(goalType, "goalType");
        return this.mGoalProcessorMap.get(goalType);
    }
}

package com.microsoft.kapp.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.AttributeSet;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.OnGoalsChangedListener;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.models.goal.GoalProcessor;
import com.microsoft.kapp.models.goal.GoalProcessorManager;
import com.microsoft.kapp.models.home.HomeData;
import com.microsoft.kapp.sensor.SensorDataDebugProvider;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.CommonUtils;
import com.microsoft.kapp.utils.Formatter;
import com.microsoft.kapp.utils.GoalsUtils;
import com.microsoft.kapp.utils.StringUtils;
import com.microsoft.kapp.widgets.HomeTile;
import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.GoalType;
import com.microsoft.krestsdk.models.UserDailySummary;
import javax.inject.Inject;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class StepsHomeTile extends HomeTile implements OnGoalsChangedListener {
    private int mAchievedValue;
    private final int mGoalNearAchievementThreshold;
    private final int mGoalOverageThreshold;
    @Inject
    GoalProcessorManager mGoalProcessorManager;
    @Inject
    SensorDataDebugProvider mSensorDataDebugProvider;
    @Inject
    SettingsProvider mSettingsProvider;

    public StepsHomeTile(Context context) {
        this(context, null);
    }

    public StepsHomeTile(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.confirmationBarStyle);
    }

    public StepsHomeTile(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setGlyphResourceId(R.string.glyph_steps);
        Resources currentResourses = getResources();
        setHomeTileContainerContentDesc(currentResourses.getString(R.string.home_tile_container_content_desc_steps));
        setTileEventNameContentDesc(currentResourses.getString(R.string.home_tile_event_name_content_desc_steps));
        setGlyphResourceContentDesc(currentResourses.getString(R.string.home_tile_glyph_content_desc_steps));
        setTileEventDateContentDesc(currentResourses.getString(R.string.home_tile_event_date_content_desc_steps));
        setTileEventMetricContentDesc(currentResourses.getString(R.string.home_tile_event_metric_content_desc_steps));
        this.mOldContentThreshold = currentResourses.getInteger(R.integer.home_tile_steps_calories_goal_achievement_threshold);
        this.mGoalNearAchievementThreshold = getContext().getResources().getInteger(R.integer.home_tile_steps_calories_goal_achievement_threshold);
        this.mGoalOverageThreshold = getContext().getResources().getInteger(R.integer.home_tile_steps_calories_goal_overage_threshold);
    }

    public void setData(UserDailySummary userDailySummary, GoalDto goal, boolean isFirstRun) {
        Spannable stepsText;
        this.mAchievedValue = 0;
        if (userDailySummary != null && !isOldContent(userDailySummary.getTimeOfDay())) {
            this.mAchievedValue = userDailySummary.getStepsTaken();
        }
        HomeTile.TileState tileState = HomeTile.TileState.VIEWED;
        if (this.mSettingsProvider.isUseLocalSensorDataEnabled()) {
            stepsText = new SpannableString(this.mAchievedValue + "(" + ((int) this.mSensorDataDebugProvider.getStepCountForTodayLocally()) + ")");
        } else {
            stepsText = Formatter.formatSteps(getContext(), R.array.MerticInvertedValueFormat, this.mAchievedValue);
        }
        setTileData(tileState, null, getPercentageOfGoalCompleted(goal), stepsText);
        if (isFirstRun) {
            setFirstRun(goal, tileState, stepsText);
        }
    }

    private String getPercentageOfGoalCompleted(GoalDto goal) {
        int goalValue = 0;
        if (goal != null) {
            goalValue = GoalsUtils.getGoalValue(goal);
        }
        if (goalValue <= 0) {
            String percentageCompleted = getContext().getString(R.string.empty);
            return percentageCompleted;
        }
        float percentageAchieved = (this.mAchievedValue / goalValue) * 100.0f;
        if (percentageAchieved > this.mGoalOverageThreshold) {
            String percentageCompleted2 = getContext().getString(R.string.home_tile_steps_goal_complete_with_overage, StringUtils.unitSteps(this.mAchievedValue - goalValue, getResources()));
            return percentageCompleted2;
        } else if (percentageAchieved >= 100.0f) {
            String percentageCompleted3 = getContext().getString(R.string.home_tile_steps_goal_complete);
            return percentageCompleted3;
        } else if (percentageAchieved > this.mGoalNearAchievementThreshold) {
            GoalProcessor processor = this.mGoalProcessorManager.getGoalProcessor(GoalType.STEP);
            int minutesRemaining = (int) Math.ceil(processor.calculateNumberOfSecondsRequired(goalValue - this.mAchievedValue) / 60);
            String percentageCompleted4 = getContext().getString(R.string.home_tile_steps_goal_almost_there_message, Integer.valueOf(Math.max(minutesRemaining, 1)));
            return percentageCompleted4;
        } else {
            String percentageCompleted5 = getContext().getString(R.string.home_tile_steps_percentage_complete, Integer.valueOf((int) percentageAchieved));
            return percentageCompleted5;
        }
    }

    @Override // com.microsoft.kapp.activities.OnGoalsChangedListener
    public void onGoalsUpdated(GoalType dataType, HomeData homeData) {
        setData(CommonUtils.getUserDailySummary(homeData), homeData.getStepsGoal(), this.mSettingsProvider.isFirstRun());
    }

    @Override // com.microsoft.kapp.activities.OnGoalsChangedListener
    public boolean isValid() {
        return true;
    }

    private void setFirstRun(GoalDto goal, HomeTile.TileState tileState, Spannable goalText) {
        int goalValue = GoalsUtils.getGoalValue(goal);
        setTileData(tileState, null, goalValue != -1 ? getContext().getString(R.string.home_tile_steps_first_use, Integer.valueOf(goalValue)) : null, goalText);
    }

    @Override // com.microsoft.kapp.widgets.HomeTile
    public boolean isOldContent(DateTime activityTime) {
        DateTime now = new DateTime();
        return activityTime.isBefore(now.minusDays(1));
    }

    @Override // com.microsoft.kapp.widgets.BaseTile
    public String getTelemetryName() {
        return TelemetryConstants.Events.LogHomeTileTap.Dimensions.TILE_NAME_STEPS;
    }
}

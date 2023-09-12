package com.microsoft.kapp.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.text.Spannable;
import android.util.AttributeSet;
import com.microsoft.kapp.R;
import com.microsoft.kapp.activities.OnGoalsChangedListener;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.kapp.models.goal.GoalProcessorManager;
import com.microsoft.kapp.models.home.HomeData;
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
public class CaloriesHomeTile extends HomeTile implements OnGoalsChangedListener {
    private int mAchievedValue;
    private final int mGoalNearAchievementThreshold;
    private final int mGoalOverageThreshold;
    @Inject
    GoalProcessorManager mGoalProcessorManager;
    @Inject
    SettingsProvider mSettingsProvider;

    public CaloriesHomeTile(Context context) {
        this(context, null);
    }

    public CaloriesHomeTile(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.confirmationBarStyle);
    }

    public CaloriesHomeTile(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setGlyphResourceId(R.string.glyph_calories);
        Resources currentResourses = getResources();
        setHomeTileContainerContentDesc(currentResourses.getString(R.string.home_tile_container_content_desc_calories));
        setTileEventNameContentDesc(currentResourses.getString(R.string.home_tile_event_name_content_desc_calories));
        setGlyphResourceContentDesc(currentResourses.getString(R.string.home_tile_glyph_content_desc_calories));
        setTileEventDateContentDesc(currentResourses.getString(R.string.home_tile_event_date_content_desc_calories));
        setTileEventMetricContentDesc(currentResourses.getString(R.string.home_tile_event_metric_content_desc_calories));
        this.mGoalNearAchievementThreshold = currentResourses.getInteger(R.integer.home_tile_steps_calories_goal_achievement_threshold);
        this.mGoalOverageThreshold = currentResourses.getInteger(R.integer.home_tile_steps_calories_goal_overage_threshold);
    }

    public void setData(UserDailySummary userDailySummary, GoalDto goal, boolean isFirstRun) {
        this.mOldContentThreshold = getContext().getResources().getInteger(R.integer.home_tile_steps_calories_goal_achievement_threshold);
        this.mAchievedValue = 0;
        if (userDailySummary != null && !isOldContent(userDailySummary.getTimeOfDay())) {
            this.mAchievedValue = userDailySummary.getCaloriesBurned();
        }
        HomeTile.TileState tileState = HomeTile.TileState.VIEWED;
        Spannable caloriesText = Formatter.formatCalories(getContext(), R.array.MerticInvertedValueFormat, this.mAchievedValue);
        if (isFirstRun) {
            setFirstRun(goal, tileState, caloriesText);
        } else {
            setTileData(tileState, null, getPercentageOfGoalCompleted(goal), caloriesText);
        }
    }

    private String getPercentageOfGoalCompleted(GoalDto goal) {
        int goalValue = 0;
        if (goal != null) {
            goalValue = GoalsUtils.getGoalValue(goal);
        }
        if (goalValue <= 0) {
            String percentageCompleted = getContext().getString(R.string.empty);
            setState(HomeTile.TileState.NOT_VIEWED);
            return percentageCompleted;
        }
        float percentageAchieved = (this.mAchievedValue / goalValue) * 100.0f;
        if (percentageAchieved > this.mGoalOverageThreshold) {
            String percentageCompleted2 = getContext().getString(R.string.home_tile_calories_goal_complete_with_overage, StringUtils.unitCalories(this.mAchievedValue - goalValue, getResources()));
            return percentageCompleted2;
        } else if (percentageAchieved >= 100.0f) {
            String percentageCompleted3 = getContext().getString(R.string.home_tile_calories_goal_complete);
            return percentageCompleted3;
        } else if (percentageAchieved > this.mGoalNearAchievementThreshold) {
            String percentageCompleted4 = getContext().getString(R.string.home_tile_calories_goal_almost_there_message);
            return percentageCompleted4;
        } else {
            String percentageCompleted5 = getContext().getString(R.string.home_tile_calories_percentage_complete, Integer.valueOf((int) percentageAchieved));
            return percentageCompleted5;
        }
    }

    private void setFirstRun(GoalDto goal, HomeTile.TileState tileState, Spannable goalText) {
        int goalValue = GoalsUtils.getGoalValue(goal);
        setTileData(tileState, null, goalValue != -1 ? getContext().getString(R.string.home_tile_calories_first_use, Integer.valueOf(goalValue)) : null, goalText);
    }

    @Override // com.microsoft.kapp.activities.OnGoalsChangedListener
    public void onGoalsUpdated(GoalType dataType, HomeData homeData) {
        setData(CommonUtils.getUserDailySummary(homeData), homeData.getCaloriesGoal(), this.mSettingsProvider.isFirstRun());
    }

    @Override // com.microsoft.kapp.activities.OnGoalsChangedListener
    public boolean isValid() {
        return true;
    }

    @Override // com.microsoft.kapp.widgets.HomeTile
    public boolean isOldContent(DateTime runTime) {
        DateTime now = new DateTime();
        return runTime.isBefore(now.minusDays(1));
    }

    @Override // com.microsoft.kapp.widgets.BaseTile
    public String getTelemetryName() {
        return TelemetryConstants.Events.LogHomeTileTap.Dimensions.TILE_NAME_CALORIES;
    }
}

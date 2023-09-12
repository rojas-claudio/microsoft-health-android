package com.microsoft.kapp.activities;

import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Telemetry;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import com.microsoft.krestsdk.models.GoalType;
/* loaded from: classes.dex */
public class StepsEditGoalActivity extends EditGoalActivity {
    @Override // com.microsoft.kapp.activities.EditGoalActivity, com.microsoft.kapp.views.CircularSeekBar.OnValueChangeListener
    public void CircularSeekBarValueChanged(int newValue) {
        setEstimationTrackerText(getDistanceText(newValue));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.activities.BaseFragmentActivity, android.support.v4.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        Telemetry.logPage(TelemetryConstants.PageViews.FITNESS_STEPS_EDIT_GOAL);
    }

    @Override // com.microsoft.kapp.activities.EditGoalActivity
    protected GoalType getGoalType() {
        return GoalType.STEP;
    }

    @Override // com.microsoft.kapp.activities.EditGoalActivity
    protected int getMinStartValue() {
        return getResources().getInteger(R.integer.steps_min_start_value);
    }

    @Override // com.microsoft.kapp.activities.EditGoalActivity
    protected int getIncrementValue() {
        return getResources().getInteger(R.integer.steps_increment);
    }

    @Override // com.microsoft.kapp.activities.EditGoalActivity
    protected int getMaxValue() {
        return getResources().getInteger(R.integer.steps_max_value);
    }

    @Override // com.microsoft.kapp.activities.EditGoalActivity
    protected String getInsightText() {
        return getResources().getString(R.string.goals_steps_insight);
    }

    @Override // com.microsoft.kapp.activities.EditGoalActivity
    public String getGoalStatusText() {
        return getResources().getString(R.string.goals_steps_text);
    }

    @Override // com.microsoft.kapp.activities.EditGoalActivity
    protected boolean shouldShowEstimationTracker() {
        return true;
    }
}

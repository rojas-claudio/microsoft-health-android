package com.microsoft.kapp.models;

import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.krestsdk.models.GoalType;
/* loaded from: classes.dex */
public class UserGoal {
    private int mAchievedValue;
    private final GoalType mGoalType;
    private final int mGoalValue;

    public UserGoal(GoalType goalType, int goalValue, int achievedValue) {
        Validate.notNull(goalType, "goalType");
        Validate.isTrue(goalValue >= 0, "goalValue should be greater than or equal to zero.");
        Validate.isTrue(achievedValue >= 0, "achievedValue should be greater than or equal to zero.");
        this.mGoalType = goalType;
        this.mGoalValue = goalValue;
        this.mAchievedValue = achievedValue;
    }

    public GoalType getGoalType() {
        return this.mGoalType;
    }

    public int getGoalValue() {
        return this.mGoalValue;
    }

    public int getAchievedValue() {
        return this.mAchievedValue;
    }

    public boolean isAccomplished() {
        return this.mAchievedValue >= this.mGoalValue;
    }

    public float getPercentageComplete() {
        if (this.mGoalValue == 0) {
            return 0.0f;
        }
        float value = (this.mAchievedValue / this.mGoalValue) * 100.0f;
        if (value > 100.0f) {
            return 100.0f;
        }
        return value;
    }
}

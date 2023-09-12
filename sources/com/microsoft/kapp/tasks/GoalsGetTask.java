package com.microsoft.kapp.tasks;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.tasks.RestQueryTaskBase;
import com.microsoft.krestsdk.models.CategoryType;
import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.GoalType;
import java.util.List;
/* loaded from: classes.dex */
public class GoalsGetTask extends RestQueryTaskBase<List<GoalDto>, OnGoalsRetrieveTaskListener> {
    private CategoryType mGoalCategory;
    private GoalType mGoalType;
    private boolean mIsHistory;

    /* loaded from: classes.dex */
    public interface OnGoalsRetrieveTaskListener extends OnTaskStateChangedListener {
        void onGoalsRetrieved(List<GoalDto> list);
    }

    /* loaded from: classes.dex */
    public static class Builder extends RestQueryTaskBase.Builder<Builder, OnGoalsRetrieveTaskListener> {
        @Override // com.microsoft.kapp.tasks.RestQueryTaskBase.Builder
        public GoalsGetTask build() {
            validate();
            return new GoalsGetTask(this);
        }

        public GoalsGetTask build(boolean isHistory, CategoryType category, GoalType goalType) {
            validate();
            return new GoalsGetTask(this, isHistory, category, goalType);
        }
    }

    private GoalsGetTask(Builder builder) {
        super(builder);
    }

    private GoalsGetTask(Builder builder, boolean isHistory, CategoryType category, GoalType goalType) {
        super(builder);
        this.mIsHistory = isHistory;
        this.mGoalType = goalType;
        this.mGoalCategory = category;
    }

    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase
    protected void executeInternal(Callback<List<GoalDto>> callback) {
        if (this.mGoalCategory == null || this.mGoalType == null) {
            getRestService().getGoals(callback, this.mIsHistory);
        } else {
            getRestService().getActiveGoalByType(callback, this.mIsHistory, this.mGoalCategory, this.mGoalType);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase
    public void onSuccess(List<GoalDto> goals) {
        getListener().onGoalsRetrieved(goals);
    }
}

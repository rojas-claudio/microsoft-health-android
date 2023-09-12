package com.microsoft.kapp.tasks;

import android.support.v4.util.ArrayMap;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.models.UserGoal;
import com.microsoft.kapp.tasks.DailySummaryGetTask;
import com.microsoft.kapp.tasks.GoalsGetTask;
import com.microsoft.kapp.tasks.RestQueryTaskBase;
import com.microsoft.kapp.utils.GoalsUtils;
import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.GoalType;
import com.microsoft.krestsdk.models.UserDailySummary;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.joda.time.LocalDate;
/* loaded from: classes.dex */
public class GoalsWithProgressGetTask extends RestQueryTaskBase<List<UserGoal>, OnGoalsWithProgressRetrieveTaskListener> implements GoalsGetTask.OnGoalsRetrieveTaskListener, DailySummaryGetTask.OnDailySummaryRetrievedListener {
    private Callback<List<UserGoal>> mCallback;
    private List<UserDailySummary> mDailySummaries;
    private Map<GoalType, GoalDto> mGoalMap;
    private final AtomicInteger mTaskCompletionCount;
    private final StateListenerTask[] mTasks;

    /* loaded from: classes.dex */
    public interface OnGoalsWithProgressRetrieveTaskListener extends OnTaskStateChangedListener {
        void onGoalsWithProgressRetrieved(List<UserGoal> list);
    }

    /* loaded from: classes.dex */
    public static class Builder extends RestQueryTaskBase.Builder<Builder, OnGoalsWithProgressRetrieveTaskListener> {
        @Override // com.microsoft.kapp.tasks.RestQueryTaskBase.Builder
        public GoalsWithProgressGetTask build() {
            return new GoalsWithProgressGetTask(this);
        }
    }

    private GoalsWithProgressGetTask(Builder builder) {
        super(builder);
        LocalDate today = LocalDate.now();
        this.mTasks = new StateListenerTask[]{new GoalsGetTask.Builder().forParentFragment(builder.getParentFragment()).usingRestService(builder.getRestService()).withListener(this).build(), new DailySummaryGetTask.Builder().forParentFragment(builder.getParentFragment()).usingRestService(builder.getRestService()).withListener(this).withStartDate(today).withEndDate(today.plusDays(1)).build()};
        this.mTaskCompletionCount = new AtomicInteger(this.mTasks.length);
    }

    @Override // com.microsoft.kapp.tasks.GoalsGetTask.OnGoalsRetrieveTaskListener
    public void onGoalsRetrieved(List<GoalDto> goalsList) {
        if (goalsList != null) {
            Map<GoalType, GoalDto> goals = new ArrayMap<>();
            for (GoalDto goal : goalsList) {
                goals.put(goal.getType(), goal);
            }
            this.mGoalMap = goals;
            if (this.mTaskCompletionCount.decrementAndGet() == 0) {
                mergeResult();
            }
        }
    }

    @Override // com.microsoft.kapp.tasks.DailySummaryGetTask.OnDailySummaryRetrievedListener
    public void onDailySummaryRetrieved(List<UserDailySummary> dailySummaries) {
        this.mDailySummaries = dailySummaries;
        if (this.mTaskCompletionCount.decrementAndGet() == 0) {
            mergeResult();
        }
    }

    @Override // com.microsoft.kapp.tasks.OnTaskStateChangedListener
    public void onTaskFailed(StateListenerTask task, Exception ex) {
        getListener().onTaskFailed(task, ex);
    }

    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase
    protected void executeInternal(Callback<List<UserGoal>> callback) {
        this.mCallback = callback;
        try {
            StateListenerTask[] arr$ = this.mTasks;
            for (StateListenerTask task : arr$) {
                task.execute();
            }
        } catch (Exception ex) {
            callback.onError(ex);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase
    public void onSuccess(List<UserGoal> userGoals) {
        getListener().onGoalsWithProgressRetrieved(userGoals);
    }

    private void mergeResult() {
        if (this.mTaskCompletionCount.get() == 0) {
            List<UserGoal> userGoals = new ArrayList<>();
            UserDailySummary userDailySummary = null;
            if (this.mDailySummaries != null && this.mDailySummaries.size() > 0) {
                userDailySummary = this.mDailySummaries.get(this.mDailySummaries.size() - 1);
            }
            userGoals.add(createGoal(GoalType.STEP, userDailySummary));
            userGoals.add(createGoal(GoalType.CALORIE, userDailySummary));
            this.mCallback.callback(userGoals);
        }
    }

    private UserGoal createGoal(GoalType goalType, UserDailySummary userDailySummary) {
        GoalDto goal;
        int goalValue = 0;
        if (this.mGoalMap != null && (goal = this.mGoalMap.get(goalType)) != null) {
            goalValue = GoalsUtils.getGoalValue(goal);
        }
        int achievedValue = 0;
        if (userDailySummary != null) {
            if (goalType == GoalType.STEP) {
                achievedValue = userDailySummary.getStepsTaken();
            } else if (goalType == GoalType.CALORIE) {
                achievedValue = userDailySummary.getCaloriesBurned();
            }
        }
        return new UserGoal(goalType, goalValue, achievedValue);
    }
}

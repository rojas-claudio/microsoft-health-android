package com.microsoft.kapp.tasks;

import android.app.Activity;
import android.support.v4.app.Fragment;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import com.microsoft.kapp.tasks.RestQueryTaskBase;
import com.microsoft.kapp.utils.GoalsUtils;
import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.GoalOperationResultDto;
import com.microsoft.krestsdk.models.GoalOperationUpdateDto;
import com.microsoft.krestsdk.models.GoalValueHistoryDto;
import com.microsoft.krestsdk.models.GoalValueRecordDto;
import com.microsoft.krestsdk.models.GoalValueTemplateDto;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public class GoalUpdateTask extends RestQueryTaskBase<GoalOperationResultDto, OnGoalUpdateTaskListener> {
    private GoalDto mGoal;
    private String mGoalId;
    private String mGoalName;
    private final int mGoalValue;
    private boolean mIsGoalDataProvided;

    /* loaded from: classes.dex */
    public interface OnGoalUpdateTaskListener extends OnTaskStateChangedListener {
        void onGoalUpdated(GoalOperationResultDto goalOperationResultDto);
    }

    /* loaded from: classes.dex */
    public static class Builder extends RestQueryTaskBase.Builder<Builder, OnGoalUpdateTaskListener> {
        private GoalDto mGoal;
        private String mGoalId;
        private String mGoalName;
        private int mGoalValue;
        private boolean mIsGoalDataProvided;

        public Builder forGoal(GoalDto goal) {
            Validate.notNull(goal, WorkoutSummary.GOAL);
            this.mGoal = goal;
            this.mIsGoalDataProvided = false;
            return this;
        }

        public Builder withGoalData(String goalId, String goalName) {
            if (goalId != null && goalName != null) {
                this.mGoalId = goalId;
                this.mGoalName = goalName;
                this.mIsGoalDataProvided = true;
            }
            return this;
        }

        public Builder withGoalValue(int value) {
            this.mGoalValue = value;
            return this;
        }

        @Override // com.microsoft.kapp.tasks.RestQueryTaskBase.Builder
        public GoalUpdateTask build() {
            validate();
            return new GoalUpdateTask(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.tasks.RestQueryTaskBase.Builder
        public void validate() {
            super.validate();
            if (this.mGoal == null && !this.mIsGoalDataProvided) {
                throw new IllegalStateException("Goal is not set.");
            }
        }
    }

    private GoalUpdateTask(Builder builder) {
        super(builder);
        this.mGoal = builder.mGoal;
        this.mGoalValue = builder.mGoalValue;
        this.mIsGoalDataProvided = builder.mIsGoalDataProvided;
        this.mGoalId = builder.mGoalId;
        this.mGoalName = builder.mGoalName;
    }

    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase, com.microsoft.kapp.tasks.StateListenerTask
    public void execute() {
        Callback<GoalOperationResultDto> callback;
        if (getParentType() == 0) {
            WeakReference<Activity> activityWeakRef = getParentActivity();
            if (activityWeakRef != null && activityWeakRef.get() != null) {
                callback = new ActivityScopedCallback<>(activityWeakRef.get(), new Callback<GoalOperationResultDto>() { // from class: com.microsoft.kapp.tasks.GoalUpdateTask.1
                    @Override // com.microsoft.kapp.Callback
                    public void callback(GoalOperationResultDto result) {
                        GoalUpdateTask.this.onSuccess(result);
                    }

                    @Override // com.microsoft.kapp.Callback
                    public void onError(Exception ex) {
                        GoalUpdateTask.this.onFailed(ex);
                    }
                });
            } else {
                return;
            }
        } else if (getParentType() == 1) {
            WeakReference<Fragment> fragmentWeakRef = getParentFragment();
            if (fragmentWeakRef != null && fragmentWeakRef.get() != null) {
                callback = new ActivityScopedCallback<>(fragmentWeakRef.get(), new Callback<GoalOperationResultDto>() { // from class: com.microsoft.kapp.tasks.GoalUpdateTask.2
                    @Override // com.microsoft.kapp.Callback
                    public void callback(GoalOperationResultDto result) {
                        GoalUpdateTask.this.onSuccess(result);
                    }

                    @Override // com.microsoft.kapp.Callback
                    public void onError(Exception ex) {
                        GoalUpdateTask.this.onFailed(ex);
                    }
                });
            } else {
                return;
            }
        } else {
            KLog.w(this.TAG, "CallBack cannot be null! GoalUpdateTask aborted!");
            return;
        }
        executeInternal(callback);
    }

    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase
    protected void executeInternal(Callback<GoalOperationResultDto> callback) {
        GoalOperationUpdateDto operation = new GoalOperationUpdateDto();
        if (!this.mIsGoalDataProvided) {
            this.mGoalId = this.mGoal.getId();
            this.mGoalName = GoalsUtils.getGoalName(this.mGoal);
        }
        operation.setId(this.mGoalId);
        GoalValueRecordDto threshold = new GoalValueRecordDto();
        threshold.setValue(Integer.valueOf(this.mGoalValue));
        GoalValueTemplateDto template = new GoalValueTemplateDto();
        template.setName(this.mGoalName);
        template.setThreshold(Integer.valueOf(this.mGoalValue));
        GoalValueHistoryDto history = new GoalValueHistoryDto();
        history.setValueTemplate(template);
        operation.getValueHistory().add(history);
        getRestService().updateGoal(operation, callback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase
    public void onSuccess(GoalOperationResultDto goalOperationResult) {
        getListener().onGoalUpdated(goalOperationResult);
    }
}

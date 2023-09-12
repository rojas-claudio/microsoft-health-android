package com.microsoft.kapp.tasks;

import android.support.v4.app.Fragment;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.tasks.RestQueryTaskBase;
import com.microsoft.krestsdk.models.GoalOperationAddDto;
import com.microsoft.krestsdk.models.GoalOperationResultDto;
import com.microsoft.krestsdk.models.GoalTemplateDto;
import com.microsoft.krestsdk.models.GoalValueHistoryDto;
import com.microsoft.krestsdk.models.GoalValueTemplateDto;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class GoalsAddTask extends RestQueryTaskBase<GoalOperationResultDto, OnGoalsAddTaskListener> {
    private final ArrayList<GoalTemplateDto> mGoalTemplates;
    private final int mGoalValues;

    /* loaded from: classes.dex */
    public interface OnGoalsAddTaskListener extends OnTaskStateChangedListener {
        void onGoalsAdded(GoalOperationResultDto goalOperationResultDto);
    }

    /* loaded from: classes.dex */
    public static class Builder extends RestQueryTaskBase.Builder<Builder, OnGoalsAddTaskListener> {
        private ArrayList<GoalTemplateDto> mGoalTemplates;
        private int mGoalValues;

        public Builder usingGoalTemplate(ArrayList<GoalTemplateDto> goalTemplates) {
            Validate.notNull(goalTemplates, "goalTemplates");
            this.mGoalTemplates = goalTemplates;
            return this;
        }

        public Builder withGoalValue(int value) {
            this.mGoalValues = value;
            return this;
        }

        @Override // com.microsoft.kapp.tasks.RestQueryTaskBase.Builder
        public GoalsAddTask build() {
            validate();
            return new GoalsAddTask(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.tasks.RestQueryTaskBase.Builder
        public void validate() {
            super.validate();
            if (this.mGoalTemplates == null) {
                throw new IllegalStateException("GoalTemplate is not set.");
            }
        }
    }

    private GoalsAddTask(Builder builder) {
        super(builder);
        this.mGoalTemplates = builder.mGoalTemplates;
        this.mGoalValues = builder.mGoalValues;
    }

    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase, com.microsoft.kapp.tasks.StateListenerTask
    public void execute() {
        WeakReference<Fragment> fragmentWeakRef = getParentFragment();
        if (fragmentWeakRef != null && fragmentWeakRef.get() != null) {
            Callback<GoalOperationResultDto> callback = new ActivityScopedCallback<>(fragmentWeakRef.get(), new Callback<GoalOperationResultDto>() { // from class: com.microsoft.kapp.tasks.GoalsAddTask.1
                @Override // com.microsoft.kapp.Callback
                public void callback(GoalOperationResultDto result) {
                    GoalsAddTask.this.onSuccess(result);
                }

                @Override // com.microsoft.kapp.Callback
                public void onError(Exception ex) {
                    GoalsAddTask.this.onFailed(ex);
                }
            });
            executeInternal(callback);
        }
    }

    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase
    protected void executeInternal(Callback<GoalOperationResultDto> callback) {
        try {
            GoalOperationAddDto operation = new GoalOperationAddDto();
            ArrayList<GoalOperationAddDto> operations = new ArrayList<>();
            for (int i = 0; i < this.mGoalTemplates.size(); i++) {
                GoalTemplateDto template = this.mGoalTemplates.get(i);
                operation.setName(template.getName());
                operation.setTemplateId(template.getId());
                operation.setDescription(template.getDescription());
                GoalValueTemplateDto existingTemplate = template.getGoalValueTemplates().get(0);
                GoalValueTemplateDto newTemplate = new GoalValueTemplateDto();
                newTemplate.setName(existingTemplate.getName());
                newTemplate.setDescription(existingTemplate.getDescription());
                newTemplate.setThreshold(Integer.valueOf(this.mGoalValues));
                GoalValueHistoryDto history = new GoalValueHistoryDto();
                history.setValueTemplate(newTemplate);
                operation.getValueHistory().add(history);
                operations.add(operation);
                getRestService().addGoals(operations, callback);
            }
        } catch (Exception e) {
            KLog.d(this.TAG, "executeInternal() failed", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase
    public void onSuccess(GoalOperationResultDto goalOperationResult) {
        getListener().onGoalsAdded(goalOperationResult);
    }
}

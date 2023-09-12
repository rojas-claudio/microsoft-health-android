package com.microsoft.kapp.tasks;

import android.support.v4.app.Fragment;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.tasks.RestQueryTaskBase;
import com.microsoft.krestsdk.models.GoalOperationAddDto;
import com.microsoft.krestsdk.models.GoalOperationResultDto;
import com.microsoft.krestsdk.models.GoalTemplateDto;
import com.microsoft.krestsdk.models.GoalValueHistoryDto;
import com.microsoft.krestsdk.models.GoalValueTemplateDto;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public class GoalAddTask extends RestQueryTaskBase<GoalOperationResultDto, OnGoalAddTaskListener> {
    private final GoalTemplateDto mGoalTemplate;
    private final int mGoalValue;

    /* loaded from: classes.dex */
    public interface OnGoalAddTaskListener extends OnTaskStateChangedListener {
        void onGoalAdded(GoalOperationResultDto goalOperationResultDto);
    }

    /* loaded from: classes.dex */
    public static class Builder extends RestQueryTaskBase.Builder<Builder, OnGoalAddTaskListener> {
        private GoalTemplateDto mGoalTemplate;
        private int mGoalValue;

        public Builder usingGoalTemplate(GoalTemplateDto goalTemplate) {
            Validate.notNull(goalTemplate, "goalTemplate");
            this.mGoalTemplate = goalTemplate;
            return this;
        }

        public Builder withGoalValue(int value) {
            this.mGoalValue = value;
            return this;
        }

        @Override // com.microsoft.kapp.tasks.RestQueryTaskBase.Builder
        public GoalAddTask build() {
            validate();
            return new GoalAddTask(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.tasks.RestQueryTaskBase.Builder
        public void validate() {
            super.validate();
            if (this.mGoalTemplate == null) {
                throw new IllegalStateException("GoalTemplate is not set.");
            }
        }
    }

    private GoalAddTask(Builder builder) {
        super(builder);
        this.mGoalTemplate = builder.mGoalTemplate;
        this.mGoalValue = builder.mGoalValue;
    }

    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase, com.microsoft.kapp.tasks.StateListenerTask
    public void execute() {
        WeakReference<Fragment> fragmentWeakRef = getParentFragment();
        if (fragmentWeakRef != null && fragmentWeakRef.get() != null) {
            Callback<GoalOperationResultDto> callback = new ActivityScopedCallback<>(fragmentWeakRef.get(), new Callback<GoalOperationResultDto>() { // from class: com.microsoft.kapp.tasks.GoalAddTask.1
                @Override // com.microsoft.kapp.Callback
                public void callback(GoalOperationResultDto result) {
                    GoalAddTask.this.onSuccess(result);
                }

                @Override // com.microsoft.kapp.Callback
                public void onError(Exception ex) {
                    GoalAddTask.this.onFailed(ex);
                }
            });
            executeInternal(callback);
        }
    }

    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase
    protected void executeInternal(Callback<GoalOperationResultDto> callback) {
        GoalOperationAddDto operation = new GoalOperationAddDto();
        operation.setName(this.mGoalTemplate.getName());
        operation.setTemplateId(this.mGoalTemplate.getId());
        operation.setDescription(this.mGoalTemplate.getDescription());
        GoalValueTemplateDto existingTemplate = this.mGoalTemplate.getGoalValueTemplates().get(0);
        GoalValueTemplateDto newTemplate = new GoalValueTemplateDto();
        newTemplate.setName(existingTemplate.getName());
        newTemplate.setDescription(existingTemplate.getDescription());
        newTemplate.setThreshold(Integer.valueOf(this.mGoalValue));
        GoalValueHistoryDto history = new GoalValueHistoryDto();
        history.setValueTemplate(newTemplate);
        operation.getValueHistory().add(history);
        getRestService().addGoal(operation, callback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase
    public void onSuccess(GoalOperationResultDto goalOperationResult) {
        getListener().onGoalAdded(goalOperationResult);
    }
}

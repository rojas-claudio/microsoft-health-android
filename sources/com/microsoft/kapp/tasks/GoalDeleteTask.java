package com.microsoft.kapp.tasks;

import android.support.v4.app.Fragment;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.tasks.RestQueryTaskBase;
import com.microsoft.kapp.utils.Constants;
import java.lang.ref.WeakReference;
import org.apache.commons.lang3.StringUtils;
/* loaded from: classes.dex */
public class GoalDeleteTask extends RestQueryTaskBase<Void, OnGoalDeleteTaskListener> {
    private final String mGoalId;

    /* loaded from: classes.dex */
    public interface OnGoalDeleteTaskListener extends OnTaskStateChangedListener {
        void onGoalDeleted();
    }

    /* loaded from: classes.dex */
    public static class Builder extends RestQueryTaskBase.Builder<Builder, OnGoalDeleteTaskListener> {
        private String mGoalId;

        public Builder targettingGoalId(String goalId) {
            Validate.notNullOrEmpty(goalId, Constants.GOAL_ID);
            this.mGoalId = goalId;
            return this;
        }

        @Override // com.microsoft.kapp.tasks.RestQueryTaskBase.Builder
        public GoalDeleteTask build() {
            validate();
            return new GoalDeleteTask(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.tasks.RestQueryTaskBase.Builder
        public void validate() {
            super.validate();
            if (StringUtils.isBlank(this.mGoalId)) {
                throw new IllegalStateException("GoalId is not set.");
            }
        }
    }

    private GoalDeleteTask(Builder builder) {
        super(builder);
        this.mGoalId = builder.mGoalId;
    }

    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase, com.microsoft.kapp.tasks.StateListenerTask
    public void execute() {
        WeakReference<Fragment> fragmentWeakRef = getParentFragment();
        if (fragmentWeakRef != null && fragmentWeakRef.get() != null) {
            Callback<Void> callback = new ActivityScopedCallback<>(fragmentWeakRef.get(), new Callback<Void>() { // from class: com.microsoft.kapp.tasks.GoalDeleteTask.1
                @Override // com.microsoft.kapp.Callback
                public void callback(Void result) {
                    GoalDeleteTask.this.onSuccess(result);
                }

                @Override // com.microsoft.kapp.Callback
                public void onError(Exception ex) {
                    GoalDeleteTask.this.onFailed(ex);
                }
            });
            executeInternal(callback);
        }
    }

    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase
    protected void executeInternal(Callback<Void> callback) {
        getRestService().deleteGoal(this.mGoalId, callback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase
    public void onSuccess(Void result) {
        getListener().onGoalDeleted();
    }
}

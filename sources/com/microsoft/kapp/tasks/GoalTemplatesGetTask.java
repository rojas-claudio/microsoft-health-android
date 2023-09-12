package com.microsoft.kapp.tasks;

import com.microsoft.kapp.Callback;
import com.microsoft.kapp.tasks.RestQueryTaskBase;
import com.microsoft.krestsdk.models.GoalTemplateDto;
import java.util.List;
/* loaded from: classes.dex */
public class GoalTemplatesGetTask extends RestQueryTaskBase<List<GoalTemplateDto>, OnGoalTemplatesRetrieveTaskListener> {

    /* loaded from: classes.dex */
    public interface OnGoalTemplatesRetrieveTaskListener extends OnTaskStateChangedListener {
        void onGoalTemplatesRetrieved(List<GoalTemplateDto> list);
    }

    /* loaded from: classes.dex */
    public static class Builder extends RestQueryTaskBase.Builder<Builder, OnGoalTemplatesRetrieveTaskListener> {
        @Override // com.microsoft.kapp.tasks.RestQueryTaskBase.Builder
        public GoalTemplatesGetTask build() {
            validate();
            return new GoalTemplatesGetTask(this);
        }
    }

    private GoalTemplatesGetTask(Builder builder) {
        super(builder);
    }

    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase
    protected void executeInternal(Callback<List<GoalTemplateDto>> callback) {
        getRestService().getGoalTemplates(callback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase
    public void onSuccess(List<GoalTemplateDto> templates) {
        getListener().onGoalTemplatesRetrieved(templates);
    }
}

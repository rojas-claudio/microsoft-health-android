package com.microsoft.kapp.tasks;

import android.support.v4.app.Fragment;
import com.microsoft.kapp.ActivityScopedCallback;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.tasks.RestQueryTaskBase;
import java.lang.ref.WeakReference;
import org.apache.commons.lang3.StringUtils;
/* loaded from: classes.dex */
public class EventDeleteTask extends RestQueryTaskBase<Void, OnEventDeleteTaskListener> {
    private final String mEventId;

    /* loaded from: classes.dex */
    public interface OnEventDeleteTaskListener extends OnTaskStateChangedListener {
        void onEventDeleted();
    }

    /* loaded from: classes.dex */
    public static class Builder extends RestQueryTaskBase.Builder<Builder, OnEventDeleteTaskListener> {
        private String mEventId;

        public Builder targettingEventId(String eventId) {
            Validate.notNullOrEmpty(eventId, "eventId");
            this.mEventId = eventId;
            return this;
        }

        @Override // com.microsoft.kapp.tasks.RestQueryTaskBase.Builder
        public EventDeleteTask build() {
            validate();
            return new EventDeleteTask(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.microsoft.kapp.tasks.RestQueryTaskBase.Builder
        public void validate() {
            super.validate();
            if (StringUtils.isBlank(this.mEventId)) {
                throw new IllegalStateException("EventId is not set.");
            }
        }
    }

    private EventDeleteTask(Builder builder) {
        super(builder);
        this.mEventId = builder.mEventId;
    }

    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase, com.microsoft.kapp.tasks.StateListenerTask
    public void execute() {
        WeakReference<Fragment> fragmentWeakRef = getParentFragment();
        if (fragmentWeakRef != null && fragmentWeakRef.get() != null) {
            Callback<Void> callback = new ActivityScopedCallback<>(fragmentWeakRef.get(), new Callback<Void>() { // from class: com.microsoft.kapp.tasks.EventDeleteTask.1
                @Override // com.microsoft.kapp.Callback
                public void callback(Void result) {
                    EventDeleteTask.this.onSuccess(result);
                }

                @Override // com.microsoft.kapp.Callback
                public void onError(Exception ex) {
                    EventDeleteTask.this.onFailed(ex);
                }
            });
            executeInternal(callback);
        }
    }

    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase
    protected void executeInternal(Callback<Void> callback) {
        getRestService().deleteEvent(this.mEventId, callback);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.microsoft.kapp.tasks.RestQueryTaskBase
    public void onSuccess(Void result) {
        getListener().onEventDeleted();
    }
}

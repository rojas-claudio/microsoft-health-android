package com.microsoft.kapp.tasks;

import com.microsoft.kapp.OnTaskListener;
import com.microsoft.krestsdk.services.RestService;
/* loaded from: classes.dex */
public class EventDeleteTaskV1 extends RestQueryTaskBaseV1<Void, OnEventDeleteTaskListener> {
    private String mEventId;
    private OnEventDeleteTaskListener mEventListener;
    private RestService mRestService;

    /* loaded from: classes.dex */
    public interface OnEventDeleteTaskListener extends OnTaskStateChangedListenerV1 {
        void onEventDeleted();
    }

    public EventDeleteTaskV1(String eventId, RestService restService, OnTaskListener onTaskListener, OnEventDeleteTaskListener onEventListener) {
        super(onTaskListener);
        this.mEventId = eventId;
        this.mRestService = restService;
        this.mEventListener = onEventListener;
    }

    @Override // com.microsoft.kapp.tasks.RestQueryTaskBaseV1
    public void execute() {
        this.mRestService.deleteEvent(String.valueOf(this.mEventId), this);
    }

    @Override // com.microsoft.kapp.tasks.RestQueryTaskBaseV1
    public void onSuccess(Void result) {
        this.mEventListener.onEventDeleted();
    }

    @Override // com.microsoft.kapp.tasks.RestQueryTaskBaseV1
    public void onFailure(Exception ex) {
        this.mEventListener.onTaskFailed(2, ex);
    }
}

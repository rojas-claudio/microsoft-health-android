package com.microsoft.kapp.tasks;

import com.google.gson.JsonObject;
import com.microsoft.kapp.OnTaskListener;
import com.microsoft.krestsdk.services.RestService;
/* loaded from: classes.dex */
public class EventRenameTaskV1 extends RestQueryTaskBaseV1<Void, OnEventRenameTaskListener> {
    private static final String EVENT_NAME_PROPERTY = "Name";
    private String mEventId;
    private OnEventRenameTaskListener mEventListener;
    private String mEventName;
    private RestService mRestService;

    /* loaded from: classes.dex */
    public interface OnEventRenameTaskListener extends OnTaskStateChangedListenerV1 {
        void onEventRenamed();
    }

    public EventRenameTaskV1(String eventId, String eventName, RestService restService, OnTaskListener onTaskListener, OnEventRenameTaskListener onEventListener) {
        super(onTaskListener);
        this.mEventId = eventId;
        this.mEventName = eventName;
        this.mRestService = restService;
        this.mEventListener = onEventListener;
    }

    @Override // com.microsoft.kapp.tasks.RestQueryTaskBaseV1
    public void execute() {
        JsonObject nameOfEvent = new JsonObject();
        nameOfEvent.addProperty("Name", this.mEventName);
        this.mRestService.nameEvent(this.mEventId, nameOfEvent, this);
    }

    @Override // com.microsoft.kapp.tasks.RestQueryTaskBaseV1
    public void onSuccess(Void result) {
        this.mEventListener.onEventRenamed();
    }

    @Override // com.microsoft.kapp.tasks.RestQueryTaskBaseV1
    public void onFailure(Exception ex) {
        this.mEventListener.onTaskFailed(1, ex);
    }
}

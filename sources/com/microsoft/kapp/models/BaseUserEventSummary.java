package com.microsoft.kapp.models;

import com.microsoft.krestsdk.models.UserEvent;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class BaseUserEventSummary {
    private DateTime mDate;
    private String mDetails;
    private String mName;
    private UserEvent mUserEvent;

    public BaseUserEventSummary(String name, String details, DateTime date, UserEvent userEvent) {
        this.mName = name;
        this.mDetails = details;
        this.mDate = date;
        this.mUserEvent = userEvent;
    }

    public DateTime getDate() {
        return this.mDate;
    }

    public String getName() {
        return this.mName;
    }

    public String getDetails() {
        return this.mDetails;
    }

    public UserEvent getUserEvent() {
        return this.mUserEvent;
    }

    public String getEventId() {
        return this.mUserEvent.getEventId();
    }
}

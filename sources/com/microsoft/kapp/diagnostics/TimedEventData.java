package com.microsoft.kapp.diagnostics;

import com.microsoft.applicationinsights.contracts.EventData;
/* loaded from: classes.dex */
public class TimedEventData extends EventData {
    private long mStartTime;

    public long getStartTime() {
        return this.mStartTime;
    }

    public void setStartTime(long startTime) {
        this.mStartTime = startTime;
    }
}

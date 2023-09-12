package com.microsoft.kapp.sensor.models;

import org.joda.time.DateTime;
/* loaded from: classes.dex */
public abstract class KSensorEvent {
    private DateTime mEventTime;
    private double mValue;

    public abstract KSensorEventType getSensorEventType();

    public DateTime getEventTime() {
        return this.mEventTime;
    }

    public void setEventTime(DateTime mEventTime) {
        this.mEventTime = mEventTime;
    }

    public double getValue() {
        return this.mValue;
    }

    public void setValue(double mValue) {
        this.mValue = mValue;
    }
}

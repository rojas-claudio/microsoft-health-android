package com.microsoft.kapp.sensor;

import com.microsoft.kapp.sensor.models.KSensorEvent;
import com.microsoft.kapp.sensor.models.KSensorEventType;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public interface SensorDataProvider {
    boolean clearEventsBetween(DateTime dateTime, DateTime dateTime2);

    String getDeviceId();

    KSensorEvent[] getEventsBetween(KSensorEventType kSensorEventType, DateTime dateTime, DateTime dateTime2);

    DateTime getOldestEventTime();

    DateTime getOldestEventTime(KSensorEventType kSensorEventType);

    KSensorEventType[] getSupportedEventTypes();
}

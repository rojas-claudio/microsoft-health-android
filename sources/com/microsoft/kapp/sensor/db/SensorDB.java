package com.microsoft.kapp.sensor.db;

import org.joda.time.DateTime;
/* loaded from: classes.dex */
public interface SensorDB {
    boolean addEvent(DBSensorEvent dBSensorEvent);

    boolean addEvent(DBSensorType dBSensorType, DateTime dateTime, Double d);

    boolean clearAllEventsAfter(DBSensorType dBSensorType, DateTime dateTime);

    boolean clearAllEventsBefore(DBSensorType dBSensorType, DateTime dateTime);

    boolean clearEventsBetween(DBSensorType dBSensorType, DateTime dateTime, DateTime dateTime2);

    Iterable<DBSensorEvent> getAllEventsAfter(DBSensorType dBSensorType, DateTime dateTime);

    Iterable<DBSensorEvent> getAllEventsBefore(DBSensorType dBSensorType, DateTime dateTime);

    Iterable<DBSensorEvent> getEventsBetween(DBSensorType dBSensorType, DateTime dateTime, DateTime dateTime2);

    DateTime getOldestEventTime();

    DateTime getOldestEventTime(DBSensorType dBSensorType);

    Long getTotalEventCount(DBSensorType dBSensorType);
}

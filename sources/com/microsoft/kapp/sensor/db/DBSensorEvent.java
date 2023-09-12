package com.microsoft.kapp.sensor.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.microsoft.kapp.diagnostics.TelemetryConstants;
import org.joda.time.DateTime;
@DatabaseTable
/* loaded from: classes.dex */
public class DBSensorEvent {
    @DatabaseField(canBeNull = false, columnName = "EventTime", index = true, uniqueCombo = true)
    private Long mEventTime;
    @DatabaseField(canBeNull = false, columnName = "SensorType", uniqueCombo = true)
    private DBSensorType mEventType;
    @DatabaseField(columnName = "id", id = true)
    private String mKey;
    @DatabaseField(columnName = TelemetryConstants.Events.ShakeDialogPreferences.Dimensions.VALUE)
    private Double mValue;
    @DatabaseField(canBeNull = false, columnName = "Version")
    private Integer mVersion;

    public DBSensorEvent() {
    }

    public DBSensorEvent(DBSensorType sensorType, DateTime eventTime, Double value) {
        this.mEventType = sensorType;
        this.mEventTime = Long.valueOf(eventTime.getMillis());
        this.mValue = value;
        this.mVersion = 1;
    }

    public String getKey() {
        return this.mEventType.name() + this.mEventTime;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public DateTime getEventTime() {
        return new DateTime(this.mEventTime);
    }

    public void setEventTime(DateTime mEventTime) {
        this.mEventTime = Long.valueOf(mEventTime.getMillis());
    }

    public DBSensorType getEventType() {
        return this.mEventType;
    }

    public void setEventType(DBSensorType mEventType) {
        this.mEventType = mEventType;
    }

    public Integer getVersion() {
        return this.mVersion;
    }

    public void setVersion(Integer mVersion) {
        this.mVersion = mVersion;
    }

    public Double getValue() {
        return this.mValue;
    }

    public void setValue(Double mValue) {
        this.mValue = mValue;
    }
}

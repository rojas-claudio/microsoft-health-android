package com.microsoft.kapp.sensor;

import android.content.Context;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.sensor.db.DBSensorEvent;
import com.microsoft.kapp.sensor.db.DBSensorType;
import com.microsoft.kapp.sensor.db.SensorDB;
import com.microsoft.kapp.sensor.models.KSensorEvent;
import com.microsoft.kapp.sensor.models.KSensorEventType;
import com.microsoft.kapp.sensor.models.StepSensorEvent;
import com.microsoft.kapp.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
/* loaded from: classes.dex */
public class PhoneSensorDataProviderImpl implements PhoneSensorDataProvider {
    private static final String TAG = PhoneSensorDataProviderImpl.class.getName();
    private String mId;
    private SensorDB mSensorDB;

    public PhoneSensorDataProviderImpl(Context context, SensorDB sensorDB, SensorUtils sensorUtils) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Validate.notNull(sensorDB, "sensorDB");
        this.mSensorDB = sensorDB;
        this.mId = sensorUtils.createUuidFromPhoneId();
    }

    @Override // com.microsoft.kapp.sensor.SensorDataProvider
    public String getDeviceId() {
        return this.mId;
    }

    @Override // com.microsoft.kapp.sensor.SensorDataProvider
    public KSensorEventType[] getSupportedEventTypes() {
        return new KSensorEventType[]{KSensorEventType.STEP};
    }

    @Override // com.microsoft.kapp.sensor.SensorDataProvider
    public DateTime getOldestEventTime(KSensorEventType eventType) {
        switch (eventType) {
            case STEP:
                return this.mSensorDB.getOldestEventTime(DBSensorType.STEP);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override // com.microsoft.kapp.sensor.SensorDataProvider
    public DateTime getOldestEventTime() {
        return this.mSensorDB.getOldestEventTime();
    }

    @Override // com.microsoft.kapp.sensor.SensorDataProvider
    public KSensorEvent[] getEventsBetween(KSensorEventType eventType, DateTime startTime, DateTime endTime) {
        Iterable<DBSensorEvent> sensorEvents = this.mSensorDB.getEventsBetween(getDBSensorType(eventType), startTime, endTime);
        switch (eventType) {
            case STEP:
                return getSteps(sensorEvents);
            default:
                KLog.e(TAG, "unsupported sensor type");
                throw new IllegalArgumentException();
        }
    }

    @Override // com.microsoft.kapp.sensor.SensorDataProvider
    public boolean clearEventsBetween(DateTime startTime, DateTime endTime) {
        boolean success = true;
        DBSensorType[] arr$ = DBSensorType.values();
        for (DBSensorType sensorType : arr$) {
            success &= this.mSensorDB.clearEventsBetween(sensorType, startTime, endTime);
        }
        return success;
    }

    private KSensorEvent[] getSteps(Iterable<DBSensorEvent> sensorEvents) {
        List<KSensorEvent> kSensorEvent = new ArrayList<>();
        if (sensorEvents != null) {
            for (DBSensorEvent event : sensorEvents) {
                StepSensorEvent stepEvent = new StepSensorEvent();
                stepEvent.setEventTime(event.getEventTime());
                stepEvent.setValue(event.getValue().doubleValue());
                kSensorEvent.add(stepEvent);
            }
        }
        return (KSensorEvent[]) kSensorEvent.toArray(new KSensorEvent[kSensorEvent.size()]);
    }

    private DBSensorType getDBSensorType(KSensorEventType eventType) {
        switch (eventType) {
            case STEP:
                return DBSensorType.STEP;
            default:
                KLog.e(TAG, "unsupported sensor type");
                throw new IllegalArgumentException();
        }
    }

    @Override // com.microsoft.kapp.sensor.SensorDataDebugProvider
    public long getStepCountForTodayLocally() {
        long stepCount = 0;
        Iterable<DBSensorEvent> events = this.mSensorDB.getEventsBetween(DBSensorType.STEP, DateTime.now().withTimeAtStartOfDay(), DateTime.now());
        if (events != null) {
            for (DBSensorEvent event : events) {
                if (event.getEventType() == DBSensorType.STEP && event.getValue() != null) {
                    stepCount += event.getValue().longValue();
                }
            }
        }
        return stepCount;
    }

    @Override // com.microsoft.kapp.sensor.SensorDataDebugProvider
    public long getTotalStepEventCount() {
        Long result = this.mSensorDB.getTotalEventCount(DBSensorType.STEP);
        if (result != null) {
            return result.longValue();
        }
        return 0L;
    }

    @Override // com.microsoft.kapp.sensor.SensorDataDebugProvider
    public List<StepSensorEvent> getStepSensorEventsListForToday() {
        List<StepSensorEvent> stepEvents = new ArrayList<>();
        Iterable<DBSensorEvent> events = this.mSensorDB.getEventsBetween(DBSensorType.STEP, DateTime.now().withTimeAtStartOfDay(), DateTime.now());
        if (events != null) {
            for (DBSensorEvent event : events) {
                StepSensorEvent stepEvent = new StepSensorEvent();
                stepEvent.setEventTime(event.getEventTime());
                stepEvent.setValue(event.getValue() == null ? Constants.SPLITS_ACCURACY : event.getValue().doubleValue());
                stepEvents.add(stepEvent);
            }
        }
        return stepEvents;
    }
}

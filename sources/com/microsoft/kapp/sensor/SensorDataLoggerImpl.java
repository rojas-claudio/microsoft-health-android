package com.microsoft.kapp.sensor;

import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.sensor.db.DBSensorType;
import com.microsoft.kapp.sensor.db.SensorDB;
import com.microsoft.kapp.sensor.models.KSensorEvent;
import com.microsoft.kapp.sensor.models.StepSensorEvent;
/* loaded from: classes.dex */
public class SensorDataLoggerImpl implements SensorDataLogger {
    private SensorDB mSensorDB;

    public SensorDataLoggerImpl(SensorDB sensorDB) {
        this.mSensorDB = sensorDB;
    }

    @Override // com.microsoft.kapp.sensor.SensorDataLogger
    public boolean logStepEvent(StepSensorEvent stepSensorEvent) {
        Validate.notNull(stepSensorEvent, "stepSensorEvent");
        return this.mSensorDB.addEvent(DBSensorType.STEP, stepSensorEvent.getEventTime(), Double.valueOf(stepSensorEvent.getValue()));
    }

    @Override // com.microsoft.kapp.sensor.SensorDataLogger
    public boolean logSensorEvent(KSensorEvent sensorEvent) {
        switch (sensorEvent.getSensorEventType()) {
            case STEP:
                return logStepEvent((StepSensorEvent) sensorEvent);
            default:
                return false;
        }
    }
}

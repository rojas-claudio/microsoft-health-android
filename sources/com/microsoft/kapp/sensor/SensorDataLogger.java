package com.microsoft.kapp.sensor;

import com.microsoft.kapp.sensor.models.KSensorEvent;
import com.microsoft.kapp.sensor.models.StepSensorEvent;
/* loaded from: classes.dex */
public interface SensorDataLogger {
    boolean logSensorEvent(KSensorEvent kSensorEvent);

    boolean logStepEvent(StepSensorEvent stepSensorEvent);
}

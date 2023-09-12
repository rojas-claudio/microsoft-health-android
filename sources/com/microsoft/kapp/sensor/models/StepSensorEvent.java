package com.microsoft.kapp.sensor.models;
/* loaded from: classes.dex */
public class StepSensorEvent extends KSensorEvent {
    @Override // com.microsoft.kapp.sensor.models.KSensorEvent
    public KSensorEventType getSensorEventType() {
        return KSensorEventType.STEP;
    }
}

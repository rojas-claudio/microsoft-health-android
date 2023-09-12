package com.microsoft.kapp.sensor;

import com.microsoft.kapp.sensor.models.StepSensorEvent;
import java.util.List;
/* loaded from: classes.dex */
public interface SensorDataDebugProvider {
    long getStepCountForTodayLocally();

    @Deprecated
    List<StepSensorEvent> getStepSensorEventsListForToday();

    long getTotalStepEventCount();
}

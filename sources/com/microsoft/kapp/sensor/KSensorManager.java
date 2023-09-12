package com.microsoft.kapp.sensor;

import org.joda.time.DateTime;
/* loaded from: classes.dex */
public interface KSensorManager {
    DateTime getOldestStepEventTime();

    boolean syncSensorDataToCloud();
}

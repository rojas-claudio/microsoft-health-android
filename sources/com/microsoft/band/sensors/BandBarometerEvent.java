package com.microsoft.band.sensors;
/* loaded from: classes.dex */
public interface BandBarometerEvent extends BandSensorEvent {
    double getAirPressure();

    double getTemperature();
}

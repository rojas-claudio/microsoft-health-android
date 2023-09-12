package com.microsoft.band.sensors;
/* loaded from: classes.dex */
public interface BandAltimeterEvent extends BandSensorEvent {
    long getFlightsAscended();

    long getFlightsDescended();

    float getRate();

    long getSteppingGain();

    long getSteppingLoss();

    long getStepsAscended();

    long getStepsDescended();

    long getTotalGain();

    long getTotalLoss();
}

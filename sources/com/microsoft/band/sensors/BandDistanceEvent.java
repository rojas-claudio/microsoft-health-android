package com.microsoft.band.sensors;
/* loaded from: classes.dex */
public interface BandDistanceEvent extends BandSensorEvent {
    MotionType getMotionType();

    float getPace();

    float getSpeed();

    long getTotalDistance();
}

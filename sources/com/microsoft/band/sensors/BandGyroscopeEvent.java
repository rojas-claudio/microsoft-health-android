package com.microsoft.band.sensors;
/* loaded from: classes.dex */
public interface BandGyroscopeEvent extends BandSensorEvent {
    float getAccelerationX();

    float getAccelerationY();

    float getAccelerationZ();

    float getAngularVelocityX();

    float getAngularVelocityY();

    float getAngularVelocityZ();
}

package com.microsoft.band.sensors;
/* loaded from: classes.dex */
public interface BandHeartRateEvent extends BandSensorEvent {
    int getHeartRate();

    HeartRateQuality getQuality();
}

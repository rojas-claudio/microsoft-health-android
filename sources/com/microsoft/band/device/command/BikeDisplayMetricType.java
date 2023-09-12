package com.microsoft.band.device.command;
/* loaded from: classes.dex */
public enum BikeDisplayMetricType {
    NONE(0),
    DURATION(1),
    HEART_RATE(2),
    CALORIES(3),
    DISTANCE(4),
    SPEED(5),
    ELEVATION_GAIN(6),
    AVERAGE_SPEED(7),
    TIME(8);
    
    public static final int CARGO_LIMIT = 5;
    public static final int OPTION_COUNT = 9;
    public final int code;

    BikeDisplayMetricType(int code) {
        this.code = code;
    }

    public static BikeDisplayMetricType lookup(int setting) {
        BikeDisplayMetricType[] arr$ = values();
        for (BikeDisplayMetricType s : arr$) {
            if (s.code == setting) {
                return s;
            }
        }
        return NONE;
    }
}

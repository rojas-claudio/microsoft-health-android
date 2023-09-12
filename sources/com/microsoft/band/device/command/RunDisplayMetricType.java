package com.microsoft.band.device.command;
/* loaded from: classes.dex */
public enum RunDisplayMetricType {
    DURATION(0),
    HEART_RATE(1),
    CALORIES(2),
    DISTANCE(3),
    PACE(4),
    NONE(5),
    ELEVATION_GAIN(6),
    AVERAGE_PACE(7),
    TIME(8);
    
    public static final int CARGO_LIMIT = 4;
    public static final int OPTION_COUNT = 9;
    public final int code;

    RunDisplayMetricType(int code) {
        this.code = code;
    }

    public static RunDisplayMetricType lookup(int setting) {
        RunDisplayMetricType[] arr$ = values();
        for (RunDisplayMetricType s : arr$) {
            if (s.code == setting) {
                return s;
            }
        }
        return DURATION;
    }
}

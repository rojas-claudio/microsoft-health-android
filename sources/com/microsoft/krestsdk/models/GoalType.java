package com.microsoft.krestsdk.models;

import android.util.SparseArray;
/* loaded from: classes.dex */
public enum GoalType {
    UNKNOWN(0),
    STEP(1),
    CALORIE(2),
    SLEEP(3),
    RUN(4),
    EXERCISE(5),
    BIKE(6);
    
    private static final int MAXIMUM_VALUE = 6;
    private static final int MINIMUM_VALUE = 0;
    private static final SparseArray<GoalType> mMapping = new SparseArray<>();
    private final int mValue;

    static {
        GoalType[] arr$ = values();
        for (GoalType type : arr$) {
            mMapping.put(type.value(), type);
        }
    }

    GoalType(int value) {
        if (!isValid(value)) {
            this.mValue = 0;
        } else {
            this.mValue = value;
        }
    }

    public static GoalType valueOf(int value) {
        return !isValid(value) ? UNKNOWN : mMapping.get(value);
    }

    public int value() {
        return this.mValue;
    }

    private static boolean isValid(int value) {
        return value >= 0 && value <= 6;
    }
}

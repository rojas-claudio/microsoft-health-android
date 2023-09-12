package com.microsoft.kapp.models.guidedworkout;

import android.util.SparseArray;
/* loaded from: classes.dex */
public enum GuidedWorkoutUnitType {
    UNKNOWN(0),
    Time(1),
    Calories(2),
    Reps(3),
    Distance(4),
    Hide(5);
    
    private static final int MAXIMUM_VALUE = 5;
    private static final int MINIMUM_VALUE = 0;
    private static final SparseArray<GuidedWorkoutUnitType> mMapping = new SparseArray<>();
    private final int mValue;

    static {
        GuidedWorkoutUnitType[] arr$ = values();
        for (GuidedWorkoutUnitType type : arr$) {
            mMapping.put(type.value(), type);
        }
    }

    GuidedWorkoutUnitType(int value) {
        if (!isValid(value)) {
            this.mValue = 0;
        } else {
            this.mValue = value;
        }
    }

    public static GuidedWorkoutUnitType valueOf(int value) {
        return !isValid(value) ? UNKNOWN : mMapping.get(value);
    }

    public int value() {
        return this.mValue;
    }

    private static boolean isValid(int value) {
        return value >= 0 && value <= 5;
    }
}

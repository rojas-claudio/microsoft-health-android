package com.microsoft.krestsdk.models;

import android.util.SparseArray;
/* loaded from: classes.dex */
public enum ExerciseTraversalType {
    UNKNOWN(0),
    SetOrder(1),
    ExerciseOrder(2);
    
    private static final int MAXIMUM_VALUE = 2;
    private static final int MINIMUM_VALUE = 0;
    private static final SparseArray<ExerciseTraversalType> mMapping = new SparseArray<>();
    private final int mValue;

    static {
        ExerciseTraversalType[] arr$ = values();
        for (ExerciseTraversalType type : arr$) {
            mMapping.put(type.value(), type);
        }
    }

    ExerciseTraversalType(int value) {
        if (!isValid(value)) {
            this.mValue = 0;
        } else {
            this.mValue = value;
        }
    }

    public static ExerciseTraversalType valueOf(int value) {
        return !isValid(value) ? UNKNOWN : mMapping.get(value);
    }

    public int value() {
        return this.mValue;
    }

    private static boolean isValid(int value) {
        return value >= 0 && value <= 2;
    }
}

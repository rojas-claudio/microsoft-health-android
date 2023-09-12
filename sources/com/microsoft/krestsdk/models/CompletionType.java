package com.microsoft.krestsdk.models;

import android.util.SparseArray;
/* loaded from: classes.dex */
public enum CompletionType {
    UNKNOWN(0),
    SECONDS(1),
    REPETITIONS(2),
    METERS(3),
    CALORIES(4),
    HEART_RATE(5),
    JOULES(6);
    
    private static final int MAXIMUM_VALUE = 6;
    private static final int MINIMUM_VALUE = 0;
    private static final SparseArray<CompletionType> mMapping = new SparseArray<>();
    private final int mValue;

    static {
        CompletionType[] arr$ = values();
        for (CompletionType type : arr$) {
            mMapping.put(type.value(), type);
        }
    }

    CompletionType(int value) {
        if (!isValid(value)) {
            this.mValue = 0;
        } else {
            this.mValue = value;
        }
    }

    public static CompletionType valueOf(int value) {
        return !isValid(value) ? UNKNOWN : mMapping.get(value);
    }

    public int value() {
        return this.mValue;
    }

    private static boolean isValid(int value) {
        return value >= 0 && value <= 6;
    }
}

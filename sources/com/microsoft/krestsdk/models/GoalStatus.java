package com.microsoft.krestsdk.models;

import android.util.SparseArray;
/* loaded from: classes.dex */
public enum GoalStatus {
    UNKNOWN(0),
    ACTIVE(1),
    ACHIEVED(2),
    FAILED(3),
    CLOSED(4);
    
    private static final int MAXIMUM_VALUE = 4;
    private static final int MINIMUM_VALUE = 0;
    private static final SparseArray<GoalStatus> mMapping = new SparseArray<>();
    private final int mValue;

    static {
        GoalStatus[] arr$ = values();
        for (GoalStatus type : arr$) {
            mMapping.put(type.value(), type);
        }
    }

    GoalStatus(int value) {
        if (!isValid(value)) {
            this.mValue = 0;
        } else {
            this.mValue = value;
        }
    }

    public static GoalStatus valueOf(int value) {
        return !isValid(value) ? UNKNOWN : mMapping.get(value);
    }

    public int value() {
        return this.mValue;
    }

    private static boolean isValid(int value) {
        return value >= 0 && value <= 4;
    }
}

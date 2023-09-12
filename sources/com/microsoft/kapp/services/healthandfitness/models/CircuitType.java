package com.microsoft.kapp.services.healthandfitness.models;

import android.util.SparseArray;
/* loaded from: classes.dex */
public enum CircuitType {
    UNKNOWN(0),
    Regular(1),
    CoolDown(2),
    WarmUp(3);
    
    private static final int MAXIMUM_VALUE = 3;
    private static final int MINIMUM_VALUE = 0;
    private static final SparseArray<CircuitType> mMapping = new SparseArray<>();
    private final int mValue;

    static {
        CircuitType[] arr$ = values();
        for (CircuitType type : arr$) {
            mMapping.put(type.value(), type);
        }
    }

    CircuitType(int value) {
        if (!isValid(value)) {
            this.mValue = 0;
        } else {
            this.mValue = value;
        }
    }

    public static CircuitType valueOf(int value) {
        return !isValid(value) ? UNKNOWN : mMapping.get(value);
    }

    public int value() {
        return this.mValue;
    }

    private static boolean isValid(int value) {
        return value >= 0 && value <= 3;
    }
}

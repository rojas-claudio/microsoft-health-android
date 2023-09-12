package com.microsoft.krestsdk.models;

import android.util.SparseArray;
/* loaded from: classes.dex */
public enum CategoryType {
    UNKNOWN(0),
    CONNECTION(1),
    TOTALS(2),
    PERSONALGOAL(3),
    OBJECTIVES(4),
    BESTS(5),
    USERDEFINED(6);
    
    private static final int MAXIMUM_VALUE = 6;
    private static final int MINIMUM_VALUE = 0;
    private static final SparseArray<CategoryType> mMapping = new SparseArray<>();
    private final int mValue;

    static {
        CategoryType[] arr$ = values();
        for (CategoryType type : arr$) {
            mMapping.put(type.value(), type);
        }
    }

    CategoryType(int value) {
        if (!isValid(value)) {
            this.mValue = 0;
        } else {
            this.mValue = value;
        }
    }

    public static CategoryType valueOf(int value) {
        return !isValid(value) ? UNKNOWN : mMapping.get(value);
    }

    public int value() {
        return this.mValue;
    }

    private static boolean isValid(int value) {
        return value >= 0 && value <= 6;
    }
}

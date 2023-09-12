package com.microsoft.kapp.telephony;

import android.util.SparseArray;
import org.apache.commons.lang3.Validate;
/* loaded from: classes.dex */
public enum PhoneState {
    NONE(0),
    RINGING(1),
    PICKED_UP(2),
    HUNG_UP(3),
    MISSED(4);
    
    private static final int MAXIMUM_VALUE = 4;
    private static final int MINIMUM_VALUE = 0;
    private static final SparseArray<PhoneState> mMapping = new SparseArray<>();
    private final int mState;

    static {
        PhoneState[] arr$ = values();
        for (PhoneState state : arr$) {
            mMapping.put(state.value(), state);
        }
    }

    PhoneState(int state) {
        validate(state);
        this.mState = state;
    }

    public static PhoneState valueOf(int value) {
        validate(value);
        return mMapping.get(value);
    }

    public int value() {
        return this.mState;
    }

    @Override // java.lang.Enum
    public String toString() {
        switch (this) {
            case NONE:
                return "None";
            case RINGING:
                return "Ringing";
            case PICKED_UP:
                return "Picked-up";
            case HUNG_UP:
                return "Hung-up";
            case MISSED:
                return "Missed";
            default:
                return "Unknown";
        }
    }

    private static void validate(int value) {
        Validate.isTrue(value >= 0 && value <= 4, "value must be greater than or equal to %d and less than or equal to %d", 0, 4);
    }
}

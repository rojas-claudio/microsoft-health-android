package com.microsoft.kapp.telephony;

import android.util.SparseArray;
import org.apache.commons.lang3.Validate;
/* loaded from: classes.dex */
public enum CallState {
    IDLE(0),
    RINGING(1),
    OFFHOOK(2);
    
    private static final SparseArray<CallState> mMapping = new SparseArray<>();
    private final int mState;

    static {
        CallState[] arr$ = values();
        for (CallState state : arr$) {
            mMapping.put(state.value(), state);
        }
    }

    CallState(int state) {
        validate(state);
        this.mState = state;
    }

    public static CallState valueOf(int value) {
        validate(value);
        return mMapping.get(value);
    }

    public int value() {
        return this.mState;
    }

    @Override // java.lang.Enum
    public String toString() {
        switch (this) {
            case IDLE:
                return "Idle";
            case RINGING:
                return "Ringing";
            case OFFHOOK:
                return "Off-Hook";
            default:
                return "Unknown";
        }
    }

    private static void validate(int value) {
        Validate.isTrue(value >= 0 && value <= 2, "value must be greater than or equal to %d and less than or equal to %d", 0, 2);
    }
}

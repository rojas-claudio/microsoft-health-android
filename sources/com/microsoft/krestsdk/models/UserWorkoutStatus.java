package com.microsoft.krestsdk.models;
/* loaded from: classes.dex */
public enum UserWorkoutStatus {
    UNKNOWN(0),
    SKIPPED_DEPRECATED(1),
    COMPLETED(2),
    NOT_STARTED(3),
    LOOPED(4),
    ERRORED(5),
    SKIPPED(6),
    ABORTED(10),
    INCOMPLETE(99);
    
    private int mType;

    UserWorkoutStatus(int i) {
        this.mType = i;
    }

    public int getNumericType() {
        return this.mType;
    }
}

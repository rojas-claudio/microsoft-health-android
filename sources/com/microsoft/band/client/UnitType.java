package com.microsoft.band.client;
/* loaded from: classes.dex */
public enum UnitType {
    IMPERIAL(1),
    METRIC(2);
    
    private final int mValue;

    UnitType(int i) {
        this.mValue = i;
    }

    public int getValue() {
        return this.mValue;
    }

    public static UnitType lookup(int v) {
        UnitType[] arr$ = values();
        for (UnitType k : arr$) {
            if (k.getValue() == v) {
                return k;
            }
        }
        return IMPERIAL;
    }
}

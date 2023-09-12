package com.microsoft.band.client;
/* loaded from: classes.dex */
public enum CargoDateFormat {
    YYYYMMDD(1),
    DDMMYYYY(2),
    DMMYYYY(3),
    MMDDYYYY(4),
    MDYYYY(5);
    
    private final int mValue;

    CargoDateFormat(int i) {
        this.mValue = i;
    }

    public int getValue() {
        return this.mValue;
    }

    public static CargoDateFormat lookup(int v) {
        CargoDateFormat[] arr$ = values();
        for (CargoDateFormat k : arr$) {
            if (k.getValue() == v) {
                return k;
            }
        }
        return YYYYMMDD;
    }
}

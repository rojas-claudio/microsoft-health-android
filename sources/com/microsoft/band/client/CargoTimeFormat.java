package com.microsoft.band.client;
/* loaded from: classes.dex */
public enum CargoTimeFormat {
    HHmmss(1),
    Hmmss(2),
    hhmmss(3),
    hmmss(4);
    
    private final int mValue;

    CargoTimeFormat(int i) {
        this.mValue = i;
    }

    public int getValue() {
        return this.mValue;
    }

    public static CargoTimeFormat lookup(int v) {
        CargoTimeFormat[] arr$ = values();
        for (CargoTimeFormat k : arr$) {
            if (k.getValue() == v) {
                return k;
            }
        }
        return HHmmss;
    }
}

package com.microsoft.band.client;
/* loaded from: classes.dex */
public enum CargoLocation {
    US(1),
    GB(2),
    CA(3),
    FR(4),
    DE(5),
    IT(6),
    MX(7),
    ES(8),
    AU(9),
    NZ(10),
    DK(11),
    FI(12),
    NO(13),
    NL(14),
    PT(15),
    SE(16),
    PL(17),
    CN(18),
    TW(19),
    JP(20),
    KR(21),
    AT(22),
    BE(23),
    HK(24),
    IE(25),
    SG(26),
    CH(27),
    ZA(28),
    SA(29),
    AE(30);
    
    private final int mValue;

    CargoLocation(int i) {
        this.mValue = i;
    }

    public int getValue() {
        return this.mValue;
    }

    public static CargoLocation lookup(int v) {
        CargoLocation[] arr$ = values();
        for (CargoLocation k : arr$) {
            if (k.getValue() == v) {
                return k;
            }
        }
        return US;
    }
}

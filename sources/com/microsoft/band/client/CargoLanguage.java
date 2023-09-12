package com.microsoft.band.client;
/* loaded from: classes.dex */
public enum CargoLanguage {
    EN_US(1),
    EN_GB(2),
    FR_CA(3),
    FR_FR(4),
    DE_DE(5),
    IT_IT(6),
    ES_MX(7),
    ES_ES(8),
    ES_US(9),
    DA_DK(10),
    FI_FI(11),
    NB_NO(12),
    NL_NL(13),
    PT_PT(14),
    SV_SE(15),
    PL_PL(16),
    ZH_CN(17),
    ZH_TW(18),
    JP_JP(19),
    KO_KR(20);
    
    private final int mValue;

    CargoLanguage(int i) {
        this.mValue = i;
    }

    public int getValue() {
        return this.mValue;
    }

    public static CargoLanguage lookup(int v) {
        CargoLanguage[] arr$ = values();
        for (CargoLanguage k : arr$) {
            if (k.getValue() == v) {
                return k;
            }
        }
        return EN_US;
    }
}

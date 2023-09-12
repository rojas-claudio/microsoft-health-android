package com.microsoft.krestsdk.models;
/* loaded from: classes.dex */
public enum BandVersion {
    UNKNOWN,
    CARGO,
    NEON;

    public static BandVersion getValueOf(String str) {
        BandVersion version = UNKNOWN;
        try {
            BandVersion version2 = valueOf(str);
            return version2;
        } catch (IllegalArgumentException e) {
            return version;
        }
    }
}

package com.microsoft.kapp.utils;
/* loaded from: classes.dex */
public class ColorUtils {
    public static int lowerLimitBlack(int themeColor, int defaultColor) {
        boolean redIsDark = (16711680 & themeColor) < 3932160;
        boolean greenIsDark = (65280 & themeColor) < 15360;
        boolean blueIsDark = (themeColor & 255) < 60;
        if (redIsDark && greenIsDark && blueIsDark) {
            return defaultColor;
        }
        return themeColor;
    }
}

package com.microsoft.kapp.utils;

import java.util.Locale;
/* loaded from: classes.dex */
public class RegionUtils {
    public static boolean isMetric() {
        String countryCode = Locale.getDefault().getISO3Country();
        return (countryCode.equals("USA") || countryCode.equals("MM") || countryCode.equals("LR")) ? false : true;
    }

    public static String getMarketString(Locale locale) {
        return locale.getLanguage() + "-" + locale.getCountry();
    }

    public static String getLocaleString(Locale locale) {
        return locale.toString().replace('_', '-');
    }

    public static String getLocaleWithCountryCaps(Locale locale) {
        return locale.getLanguage() + "-" + locale.getCountry().toUpperCase(locale);
    }
}

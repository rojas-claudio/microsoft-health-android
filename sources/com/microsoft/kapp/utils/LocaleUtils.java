package com.microsoft.kapp.utils;

import java.util.Locale;
/* loaded from: classes.dex */
public class LocaleUtils {
    public static final Locale DEFAULT_LOCALE = Locale.US;

    public static String getLocaleHttpHeader() {
        Locale currentCultureCode = Locale.getDefault();
        String currentCultureAsString = currentCultureCode.toString();
        if (!DEFAULT_LOCALE.equals(currentCultureCode)) {
            String languageCode = DEFAULT_LOCALE.getLanguage();
            if (currentCultureAsString.equals(languageCode)) {
                return currentCultureAsString + ", " + DEFAULT_LOCALE.toString() + ";q=0.8";
            }
            return currentCultureAsString + ", " + languageCode + ";q=0.8, " + DEFAULT_LOCALE.toString() + ";q=0.7";
        }
        return currentCultureAsString;
    }
}

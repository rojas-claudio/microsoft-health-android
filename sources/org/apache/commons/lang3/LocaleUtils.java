package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
/* loaded from: classes.dex */
public class LocaleUtils {
    private static final ConcurrentMap<String, List<Locale>> cLanguagesByCountry = new ConcurrentHashMap();
    private static final ConcurrentMap<String, List<Locale>> cCountriesByLanguage = new ConcurrentHashMap();

    public static Locale toLocale(String str) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len != 2 && len != 5 && len < 7) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        char ch0 = str.charAt(0);
        char ch1 = str.charAt(1);
        if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (len == 2) {
            return new Locale(str, "");
        }
        if (str.charAt(2) != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        char ch3 = str.charAt(3);
        if (ch3 == '_') {
            return new Locale(str.substring(0, 2), "", str.substring(4));
        }
        char ch4 = str.charAt(4);
        if (ch3 < 'A' || ch3 > 'Z' || ch4 < 'A' || ch4 > 'Z') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (len == 5) {
            return new Locale(str.substring(0, 2), str.substring(3, 5));
        }
        if (str.charAt(5) != '_') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
    }

    public static List<Locale> localeLookupList(Locale locale) {
        return localeLookupList(locale, locale);
    }

    public static List<Locale> localeLookupList(Locale locale, Locale defaultLocale) {
        List<Locale> list = new ArrayList<>(4);
        if (locale != null) {
            list.add(locale);
            if (locale.getVariant().length() > 0) {
                list.add(new Locale(locale.getLanguage(), locale.getCountry()));
            }
            if (locale.getCountry().length() > 0) {
                list.add(new Locale(locale.getLanguage(), ""));
            }
            if (!list.contains(defaultLocale)) {
                list.add(defaultLocale);
            }
        }
        return Collections.unmodifiableList(list);
    }

    public static List<Locale> availableLocaleList() {
        return SyncAvoid.AVAILABLE_LOCALE_LIST;
    }

    public static Set<Locale> availableLocaleSet() {
        return SyncAvoid.AVAILABLE_LOCALE_SET;
    }

    public static boolean isAvailableLocale(Locale locale) {
        return availableLocaleList().contains(locale);
    }

    public static List<Locale> languagesByCountry(String countryCode) {
        if (countryCode == null) {
            return Collections.emptyList();
        }
        List<Locale> langs = cLanguagesByCountry.get(countryCode);
        if (langs == null) {
            List<Locale> langs2 = new ArrayList<>();
            List<Locale> locales = availableLocaleList();
            for (int i = 0; i < locales.size(); i++) {
                Locale locale = locales.get(i);
                if (countryCode.equals(locale.getCountry()) && locale.getVariant().length() == 0) {
                    langs2.add(locale);
                }
            }
            cLanguagesByCountry.putIfAbsent(countryCode, Collections.unmodifiableList(langs2));
            return cLanguagesByCountry.get(countryCode);
        }
        return langs;
    }

    public static List<Locale> countriesByLanguage(String languageCode) {
        if (languageCode == null) {
            return Collections.emptyList();
        }
        List<Locale> countries = cCountriesByLanguage.get(languageCode);
        if (countries == null) {
            List<Locale> countries2 = new ArrayList<>();
            List<Locale> locales = availableLocaleList();
            for (int i = 0; i < locales.size(); i++) {
                Locale locale = locales.get(i);
                if (languageCode.equals(locale.getLanguage()) && locale.getCountry().length() != 0 && locale.getVariant().length() == 0) {
                    countries2.add(locale);
                }
            }
            cCountriesByLanguage.putIfAbsent(languageCode, Collections.unmodifiableList(countries2));
            return cCountriesByLanguage.get(languageCode);
        }
        return countries;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SyncAvoid {
        private static List<Locale> AVAILABLE_LOCALE_LIST;
        private static Set<Locale> AVAILABLE_LOCALE_SET;

        SyncAvoid() {
        }

        static {
            List<Locale> list = new ArrayList<>(Arrays.asList(Locale.getAvailableLocales()));
            AVAILABLE_LOCALE_LIST = Collections.unmodifiableList(list);
            AVAILABLE_LOCALE_SET = Collections.unmodifiableSet(new HashSet(LocaleUtils.availableLocaleList()));
        }
    }
}

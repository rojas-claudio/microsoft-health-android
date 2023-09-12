package net.minidev.asm;

import com.google.android.gms.appstate.AppStateClient;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TreeMap;
/* loaded from: classes.dex */
public class ConvertDate {
    static TreeMap<String, Integer> monthsTable = new TreeMap<>(new StringCmpNS());
    static TreeMap<String, Integer> daysTable = new TreeMap<>(new StringCmpNS());
    private static HashSet<String> voidData = new HashSet<>();

    static {
        voidData.add("CET");
        voidData.add("MEZ");
        voidData.add("Uhr");
        voidData.add("h");
        voidData.add("pm");
        voidData.add("PM");
        voidData.add("o'clock");
        Locale[] arr$ = DateFormatSymbols.getAvailableLocales();
        for (Locale locale : arr$) {
            if (!"ja".equals(locale.getLanguage()) && !"ko".equals(locale.getLanguage()) && !"zh".equals(locale.getLanguage())) {
                DateFormatSymbols dfs = DateFormatSymbols.getInstance(locale);
                String[] keys = dfs.getMonths();
                for (int i = 0; i < keys.length; i++) {
                    if (keys[i].length() != 0) {
                        fillMap(monthsTable, keys[i], Integer.valueOf(i));
                    }
                }
                String[] keys2 = dfs.getShortMonths();
                for (int i2 = 0; i2 < keys2.length; i2++) {
                    String s = keys2[i2];
                    if (s.length() != 0 && !Character.isDigit(s.charAt(s.length() - 1))) {
                        fillMap(monthsTable, keys2[i2], Integer.valueOf(i2));
                        fillMap(monthsTable, keys2[i2].replace(".", ""), Integer.valueOf(i2));
                    }
                }
                String[] keys3 = dfs.getWeekdays();
                for (int i3 = 0; i3 < keys3.length; i3++) {
                    String s2 = keys3[i3];
                    if (s2.length() != 0) {
                        fillMap(daysTable, s2, Integer.valueOf(i3));
                        fillMap(daysTable, s2.replace(".", ""), Integer.valueOf(i3));
                    }
                }
                String[] keys4 = dfs.getShortWeekdays();
                for (int i4 = 0; i4 < keys4.length; i4++) {
                    String s3 = keys4[i4];
                    if (s3.length() != 0) {
                        fillMap(daysTable, s3, Integer.valueOf(i4));
                        fillMap(daysTable, s3.replace(".", ""), Integer.valueOf(i4));
                    }
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class StringCmpNS implements Comparator<String> {
        @Override // java.util.Comparator
        public int compare(String o1, String o2) {
            return o1.compareToIgnoreCase(o2);
        }
    }

    public static Integer getMonth(String month) {
        return monthsTable.get(month);
    }

    private static Integer parseMonth(String s1) {
        if (Character.isDigit(s1.charAt(0))) {
            return Integer.valueOf(Integer.parseInt(s1) - 1);
        }
        Integer month = monthsTable.get(s1);
        if (month == null) {
            throw new NullPointerException("can not parse " + s1 + " as month");
        }
        return Integer.valueOf(month.intValue());
    }

    private static void fillMap(TreeMap<String, Integer> map, String key, Integer value) {
        map.put(key, value);
        map.put(key.replace("é", "e").replace("û", "u"), value);
    }

    public static Date convertToDate(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Date) {
            return (Date) obj;
        }
        if (obj instanceof String) {
            StringTokenizer st = new StringTokenizer((String) obj, " -/:,.");
            if (st.hasMoreTokens()) {
                String s1 = st.nextToken();
                if (s1.length() == 4 && Character.isDigit(s1.charAt(0))) {
                    return getYYYYMMDD(st, s1);
                }
                if (daysTable.containsKey(s1)) {
                    if (!st.hasMoreTokens()) {
                        return null;
                    }
                    s1 = st.nextToken();
                }
                if (monthsTable.containsKey(s1)) {
                    return getMMDDYYYY(st, s1);
                }
                if (Character.isDigit(s1.charAt(0))) {
                    return getDDMMYYYY(st, s1);
                }
                return null;
            }
            return null;
        }
        throw new RuntimeException("Primitive: Can not convert " + obj.getClass().getName() + " to int");
    }

    private static Date getYYYYMMDD(StringTokenizer st, String s1) {
        GregorianCalendar cal = new GregorianCalendar(AppStateClient.STATUS_WRITE_OUT_OF_DATE_VERSION, 0, 0, 0, 0, 0);
        cal.setTimeInMillis(0L);
        int year = Integer.parseInt(s1);
        cal.set(1, year);
        if (!st.hasMoreTokens()) {
            return cal.getTime();
        }
        String s12 = st.nextToken();
        cal.set(2, parseMonth(s12).intValue());
        if (!st.hasMoreTokens()) {
            return cal.getTime();
        }
        String s13 = st.nextToken();
        if (Character.isDigit(s13.charAt(0))) {
            int day = Integer.parseInt(s13);
            cal.set(5, day);
            return addHour(st, cal);
        }
        return cal.getTime();
    }

    private static int getYear(String s1) {
        int year = Integer.parseInt(s1);
        if (year < 100) {
            if (year > 23) {
                return year + AppStateClient.STATUS_WRITE_OUT_OF_DATE_VERSION;
            }
            return year + 1900;
        }
        return year;
    }

    private static Date getMMDDYYYY(StringTokenizer st, String s1) {
        GregorianCalendar cal = new GregorianCalendar(AppStateClient.STATUS_WRITE_OUT_OF_DATE_VERSION, 0, 0, 0, 0, 0);
        Integer month = monthsTable.get(s1);
        if (month == null) {
            throw new NullPointerException("can not parse " + s1 + " as month");
        }
        cal.set(2, month.intValue());
        if (st.hasMoreTokens()) {
            int day = Integer.parseInt(st.nextToken());
            cal.set(5, day);
            if (st.hasMoreTokens()) {
                String s12 = st.nextToken();
                if (Character.isLetter(s12.charAt(0))) {
                    if (!st.hasMoreTokens()) {
                        return null;
                    }
                    s12 = st.nextToken();
                }
                cal.set(1, getYear(s12));
                return addHour(st, cal);
            }
            return null;
        }
        return null;
    }

    private static Date getDDMMYYYY(StringTokenizer st, String s1) {
        GregorianCalendar cal = new GregorianCalendar(AppStateClient.STATUS_WRITE_OUT_OF_DATE_VERSION, 0, 0, 0, 0, 0);
        int day = Integer.parseInt(s1);
        cal.set(5, day);
        if (st.hasMoreTokens()) {
            String s12 = st.nextToken();
            cal.set(2, parseMonth(s12).intValue());
            if (st.hasMoreTokens()) {
                String s13 = st.nextToken();
                cal.set(1, getYear(s13));
                return addHour(st, cal);
            }
            return null;
        }
        return null;
    }

    private static Date addHour(StringTokenizer st, Calendar cal) {
        if (!st.hasMoreTokens()) {
            return cal.getTime();
        }
        String s1 = st.nextToken();
        cal.set(11, Integer.parseInt(s1));
        if (!st.hasMoreTokens()) {
            return cal.getTime();
        }
        String s12 = st.nextToken();
        String s13 = trySkip(st, s12, cal);
        if (s13 == null) {
            return cal.getTime();
        }
        cal.set(12, Integer.parseInt(s13));
        if (!st.hasMoreTokens()) {
            return cal.getTime();
        }
        String s14 = st.nextToken();
        String s15 = trySkip(st, s14, cal);
        if (s15 == null) {
            return cal.getTime();
        }
        cal.set(13, Integer.parseInt(s15));
        if (!st.hasMoreTokens()) {
            return cal.getTime();
        }
        String s16 = st.nextToken();
        String s17 = trySkip(st, s16, cal);
        if (s17 == null) {
            return cal.getTime();
        }
        trySkip(st, s17, cal);
        return cal.getTime();
    }

    private static String trySkip(StringTokenizer st, String s1, Calendar cal) {
        while (voidData.contains(s1)) {
            if (s1.equalsIgnoreCase("pm")) {
                cal.add(11, 12);
            }
            if (!st.hasMoreTokens()) {
                return null;
            }
            s1 = st.nextToken();
        }
        return s1;
    }
}

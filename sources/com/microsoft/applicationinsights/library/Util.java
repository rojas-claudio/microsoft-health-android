package com.microsoft.applicationinsights.library;

import android.os.Build;
import android.os.Debug;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.lang3.time.DateUtils;
/* loaded from: classes.dex */
class Util {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ROOT);

    static {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DATE_FORMAT.setTimeZone(timeZone);
    }

    private Util() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String dateToISO8601(Date date) {
        Date localDate = date;
        if (localDate == null) {
            localDate = new Date();
        }
        return DATE_FORMAT.format(localDate);
    }

    protected static String msToTimeSpan(long durationMs) {
        long localDurationMs = durationMs;
        if (localDurationMs <= 0) {
            localDurationMs = 0;
        }
        long ms = localDurationMs % 1000;
        long sec = (localDurationMs / 1000) % 60;
        long min = (localDurationMs / DateUtils.MILLIS_PER_MINUTE) % 60;
        long hour = (localDurationMs / 3600000) % 24;
        long days = localDurationMs / 86400000;
        if (days == 0) {
            String result = String.format(Locale.ROOT, "%02d:%02d:%02d.%03d", Long.valueOf(hour), Long.valueOf(min), Long.valueOf(sec), Long.valueOf(ms));
            return result;
        }
        String result2 = String.format(Locale.ROOT, "%d.%02d:%02d:%02d.%03d", Long.valueOf(days), Long.valueOf(hour), Long.valueOf(min), Long.valueOf(sec), Long.valueOf(ms));
        return result2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static String tryHashStringSha256(String input) {
        try {
            MessageDigest hash = MessageDigest.getInstance("SHA-256");
            hash.reset();
            hash.update(input.getBytes());
            hash.update("oRq=MAHHHC~6CCe|JfEqRZ+gc0ESI||g2Jlb^PYjc5UYN2P 27z_+21xxd2n".getBytes());
            byte[] hashedBytes = hash.digest();
            char[] hexChars = new char[hashedBytes.length * 2];
            for (int j = 0; j < hashedBytes.length; j++) {
                int v = hashedBytes[j] & 255;
                hexChars[j * 2] = HEX_ARRAY[v >>> 4];
                hexChars[(j * 2) + 1] = HEX_ARRAY[v & 15];
            }
            return new String(hexChars);
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static boolean isEmulator() {
        return Build.BRAND.equalsIgnoreCase("generic");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static boolean isDebuggerAttached() {
        return Debug.isDebuggerConnected();
    }
}

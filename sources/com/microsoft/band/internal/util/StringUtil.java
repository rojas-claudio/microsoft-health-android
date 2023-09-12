package com.microsoft.band.internal.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.Locale;
/* loaded from: classes.dex */
public final class StringUtil {
    public static final String EMPTY_STRING = "";
    private static final BitSet URL_NON_ESCAPED_CHAR_SET;
    private static final String TAG = StringUtil.class.getSimpleName();
    private static final String[] HEX_OCTETS = new String[256];

    static {
        for (int byteIndex = 0; byteIndex < HEX_OCTETS.length; byteIndex++) {
            HEX_OCTETS[byteIndex] = String.format("%02X", Integer.valueOf(byteIndex)).intern();
        }
        URL_NON_ESCAPED_CHAR_SET = new BitSet(256);
        for (int i = 97; i <= 122; i++) {
            URL_NON_ESCAPED_CHAR_SET.set(i);
        }
        for (int i2 = 65; i2 <= 90; i2++) {
            URL_NON_ESCAPED_CHAR_SET.set(i2);
        }
        for (int i3 = 48; i3 <= 57; i3++) {
            URL_NON_ESCAPED_CHAR_SET.set(i3);
        }
        URL_NON_ESCAPED_CHAR_SET.set(45);
        URL_NON_ESCAPED_CHAR_SET.set(95);
        URL_NON_ESCAPED_CHAR_SET.set(46);
        URL_NON_ESCAPED_CHAR_SET.set(42);
    }

    public static String toHexOctet(byte byteValue) {
        int byteIndex = byteValue & 255;
        return HEX_OCTETS[byteIndex];
    }

    public static boolean isNullOrEmpty(String strValue) {
        return strValue == null || strValue.length() == 0;
    }

    public static boolean isNullOrWhitespace(String strValue) {
        return isNullOrEmpty(strValue) || isWhitespace(strValue);
    }

    public static boolean isWhitespace(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (!Character.isWhitespace(string.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String URLEncodeWithLowerCaseHex(String str) {
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            int c = str.charAt(i);
            if (URL_NON_ESCAPED_CHAR_SET.get(c)) {
                sb.append((char) c);
            } else {
                if ((65280 & c) != 0) {
                    sb.append('%');
                    sb.append(toHexOctet((byte) (c >> 8)).toLowerCase(Locale.US));
                }
                sb.append('%');
                sb.append(toHexOctet((byte) (c & 255)).toLowerCase(Locale.US));
            }
        }
        return sb.toString();
    }

    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x ", Integer.valueOf(b & 255)));
        }
        return sb.toString();
    }

    public static byte[] toMD5Hash(String name) {
        Validation.notNull(name, "String cannot be null");
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(name.getBytes());
            byte[] hash = digest.digest();
            return hash;
        } catch (NoSuchAlgorithmException e) {
            KDKLog.e(TAG, "No MD5 algorithm: %s", e.getMessage());
            return null;
        }
    }

    private StringUtil() {
        throw new UnsupportedOperationException();
    }

    public static String truncateString(String str, int maxChars) {
        int limit = Math.min(str.length(), maxChars);
        return str.substring(0, limit);
    }
}

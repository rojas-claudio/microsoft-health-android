package com.microsoft.band.util;

import com.microsoft.band.internal.util.BufferUtil;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public final class StringHelper {
    private static final String DEFAULT_CHARACTER_SET = "UTF-16LE";

    private StringHelper() {
        throw new UnsupportedOperationException();
    }

    public static byte[] toString(char[] arr) {
        byte[] bytes = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            bytes[i] = (byte) arr[i];
        }
        return bytes;
    }

    public static byte[] getBytes(String str) {
        byte[] result = BufferUtil.EMPTY;
        if (str != null && str.length() > 0) {
            try {
                return str.getBytes("UTF-16LE");
            } catch (UnsupportedEncodingException e) {
                return result;
            }
        }
        return result;
    }

    public static byte[] getBytes(String str, String characterSet) throws UnsupportedEncodingException {
        return str.getBytes(characterSet);
    }

    public static String valueOf(byte[] bytes) {
        if (bytes.length != 0) {
            try {
                return valueOf(bytes, 0, bytes.length, "UTF-16LE");
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        }
        return "";
    }

    public static String valueOf(byte[] bytes, int offcet, int length) throws UnsupportedEncodingException {
        return valueOf(bytes, offcet, length, "UTF-16LE");
    }

    public static String valueOf(byte[] bytes, int offset, int length, String characterSet) throws UnsupportedEncodingException {
        return length != 0 ? new String(bytes, offset, length, characterSet) : "";
    }

    public static String valueOfNullTerminated(byte[] bytes) {
        try {
            return valueOfNullTerminated(bytes, 0, bytes.length, "UTF-16LE");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static String valueOfNullTerminated(byte[] bytes, int offset, int maxLength, String characterSet) throws UnsupportedEncodingException {
        int length = getNullTerminatedLength(bytes, offset, maxLength);
        return length != 0 ? new String(bytes, offset, length, characterSet) : "";
    }

    private static int getNullTerminatedLength(byte[] bytes, int offset, int maxLength) {
        for (int i = offset; i - offset < maxLength; i++) {
            if (bytes[i] == 0) {
                return i - offset;
            }
        }
        throw new IllegalArgumentException("bytes");
    }

    public static String valueOfNullTerminated(ByteBuffer buffer, int offset, int max_length) {
        byte[] str = new byte[max_length];
        buffer.get(str, offset, max_length);
        int len = 0;
        while (str[len] != 0) {
            len++;
        }
        return new String(str, offset, len);
    }
}

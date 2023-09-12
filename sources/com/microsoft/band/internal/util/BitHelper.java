package com.microsoft.band.internal.util;
/* loaded from: classes.dex */
public final class BitHelper {
    public static final int UNSIGNED_BYTE_TO_INT_MASK = 255;
    public static final long UNSIGNED_INT_CHECK_MASK = -4294967296L;
    public static final long UNSIGNED_INT_TO_LONG_MASK = 4294967295L;
    public static final int UNSIGNED_SHORT_TO_INT_MASK = 65535;

    private BitHelper() {
        throw new UnsupportedOperationException();
    }

    public static short unsignedByteToShort(byte num) {
        return (short) (num & 255);
    }

    public static int unsignedByteToInteger(byte num) {
        return num & 255;
    }

    public static int unsignedShortToInteger(short num) {
        return 65535 & num;
    }

    public static long unsignedIntegerToLong(int num) {
        return num & 4294967295L;
    }

    public static int longToUnsignedInt(long num) {
        return (int) num;
    }

    public static byte intToUnsignedByte(int num) {
        return (byte) num;
    }

    public static byte shortToUnsignedByte(short num) {
        return (byte) num;
    }

    public static short intToUnsignedShort(int num) {
        return (short) num;
    }

    public static boolean checkUInt32Range(long value) {
        return (UNSIGNED_INT_CHECK_MASK & value) == 0;
    }

    public static void checkUInt32RangeException(long value) {
        if (!checkUInt32Range(value)) {
            throw new IllegalArgumentException("Value " + value + " is out of range from 0 to UInt32.MaxValue");
        }
    }
}

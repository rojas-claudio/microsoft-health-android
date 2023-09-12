package com.microsoft.band.util;
/* loaded from: classes.dex */
public final class ArraysUtil {
    private ArraysUtil() {
        throw new UnsupportedOperationException();
    }

    public static boolean isOutOfBounds(int arrayLength, int offset, int count) {
        return (offset | count) < 0 || offset > arrayLength || arrayLength - offset < count;
    }

    public static void checkOffsetAndCount(int arrayLength, int offset, int count) {
        if (isOutOfBounds(arrayLength, offset, count)) {
            throw new ArrayIndexOutOfBoundsException("Length=" + arrayLength + ", offset=" + offset + ", count=" + count);
        }
    }
}

package com.microsoft.kapp.utils;

import android.os.Parcelable;
import java.lang.reflect.Array;
/* loaded from: classes.dex */
public final class ParcelableUtils {
    private ParcelableUtils() {
        throw new UnsupportedOperationException();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T[] castArray(Parcelable[] array, Class<T> classType) {
        if (array != null) {
            T[] convertedArray = (T[]) ((Object[]) Array.newInstance((Class<?>) classType, array.length));
            for (int pos = 0; pos < array.length; pos++) {
                convertedArray[pos] = array[pos];
            }
            return convertedArray;
        }
        return null;
    }
}

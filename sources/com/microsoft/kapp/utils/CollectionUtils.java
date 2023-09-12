package com.microsoft.kapp.utils;
/* loaded from: classes.dex */
public class CollectionUtils {
    public static boolean isNullOrEmpty(Iterable<?> iterable) {
        return iterable == null || iterable.iterator() == null || !iterable.iterator().hasNext();
    }
}

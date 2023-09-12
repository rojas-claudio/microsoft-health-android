package com.microsoft.krestsdk.services;

import com.microsoft.krestsdk.models.DeserializedObjectValidation;
/* loaded from: classes.dex */
final class QueryUtils {
    private QueryUtils() {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static void validateDeserializedObject(Object deserializedObject) {
        if (deserializedObject instanceof DeserializedObjectValidation) {
            ((DeserializedObjectValidation) deserializedObject).validateDeserializedObject();
        } else if (deserializedObject instanceof Iterable) {
            Iterable iterable = (Iterable) deserializedObject;
            for (Object object : iterable) {
                if (object instanceof DeserializedObjectValidation) {
                    ((DeserializedObjectValidation) object).validateDeserializedObject();
                }
            }
        }
    }
}

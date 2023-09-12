package com.microsoft.kapp.utils;

import android.content.Intent;
import android.os.Parcelable;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutSummary;
import java.io.Serializable;
/* loaded from: classes.dex */
public class IntentUtils {
    public static <T extends Parcelable> T getParcelableExtra(Intent intent, String name) {
        Validate.notNull(intent, "intent");
        Validate.notNullOrEmpty(name, WorkoutSummary.NAME);
        T item = (T) intent.getParcelableExtra(name);
        if (item == null) {
            throw createMissingExtendedDataException(name);
        }
        return item;
    }

    public static <T extends Serializable> T getSerializableExtra(Intent intent, String name, Class<T> clazz) {
        Validate.notNull(intent, "intent");
        Validate.notNullOrEmpty(name, WorkoutSummary.NAME);
        Serializable item = intent.getSerializableExtra(name);
        if (item == null) {
            throw createMissingExtendedDataException(name);
        }
        if (clazz.isAssignableFrom(item.getClass())) {
            return clazz.cast(item);
        }
        String message = String.format("Required extended data '%s' has wrong type.", name);
        throw new IllegalStateException(message);
    }

    private static IllegalStateException createMissingExtendedDataException(String name) {
        Validate.notNullOrEmpty(name, WorkoutSummary.NAME);
        String message = String.format("Required extended data '%s' is missing from the intent.", name);
        return new IllegalStateException(message);
    }
}

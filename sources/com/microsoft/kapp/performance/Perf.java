package com.microsoft.kapp.performance;

import android.util.Log;
/* loaded from: classes.dex */
public class Perf {
    private static final String DELIMITER = "|";
    private static final String TAG = "KPerf";

    public static void mark(String action, String attribute, String... values) {
        StringBuilder builder = new StringBuilder();
        builder.append(action);
        builder.append(DELIMITER);
        builder.append(attribute);
        for (String value : values) {
            builder.append(DELIMITER);
            builder.append(value.replace("\\", "\\\\").replace(DELIMITER, "\\|"));
        }
        Log.d(TAG, builder.toString());
    }
}

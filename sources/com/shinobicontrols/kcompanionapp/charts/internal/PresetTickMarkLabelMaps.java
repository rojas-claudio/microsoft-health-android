package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;
import android.util.SparseArray;
import com.microsoft.kapp.R;
/* loaded from: classes.dex */
public class PresetTickMarkLabelMaps {
    public static SparseArray<String> createHoursOfTheDayMap(Context context) {
        SparseArray<String> map = new SparseArray<>();
        if (context != null) {
            Resources resources = context.getResources();
            boolean is24Hour = DateFormat.is24HourFormat(context);
            String[] values = is24Hour ? resources.getStringArray(R.array.shinobicharts_hours_24) : resources.getStringArray(R.array.shinobicharts_hours_12);
            for (int i = 0; i < values.length; i++) {
                map.put(i, values[i]);
            }
        }
        return map;
    }

    public static SparseArray<String> createSleepTypesMap(Resources resources) {
        SparseArray<String> map = new SparseArray<>();
        map.put(resources.getInteger(R.integer.shinobicharts_sleep_awake_value), resources.getString(R.string.shinobicharts_awake));
        map.put(resources.getInteger(R.integer.shinobicharts_sleep_light_value), resources.getString(R.string.shinobicharts_light));
        map.put(resources.getInteger(R.integer.shinobicharts_sleep_restful_value), resources.getString(R.string.shinobicharts_restful));
        return map;
    }
}

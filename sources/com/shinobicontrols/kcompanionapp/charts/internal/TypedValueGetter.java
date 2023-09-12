package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.res.Resources;
import android.util.TypedValue;
/* loaded from: classes.dex */
public class TypedValueGetter {
    private final TypedValue outValue = new TypedValue();
    private final Resources resources;

    public TypedValueGetter(Resources resources) {
        this.resources = resources;
    }

    public float getFloat(int valueResourceId) {
        this.resources.getValue(valueResourceId, this.outValue, true);
        return this.outValue.getFloat();
    }
}

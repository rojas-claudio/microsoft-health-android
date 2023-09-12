package com.shinobicontrols.kcompanionapp.providers;

import android.content.Context;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.kcompanionapp.charts.internal.RangeAndFrequencySettingStrategy;
import com.shinobicontrols.kcompanionapp.properties.ChartDataProperties;
/* loaded from: classes.dex */
public interface IStrategyProvider<T extends ChartDataProperties> {
    RangeAndFrequencySettingStrategy getRangeAndFrequencyStrategy(Context context, Axis axis, double d, double d2, T t);
}

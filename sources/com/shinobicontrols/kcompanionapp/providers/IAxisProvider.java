package com.shinobicontrols.kcompanionapp.providers;

import android.content.Context;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.kcompanionapp.charts.internal.ChartThemeCache;
/* loaded from: classes.dex */
public interface IAxisProvider {
    Axis<Double, Double> getAxis(Context context, ChartThemeCache chartThemeCache, boolean z);
}

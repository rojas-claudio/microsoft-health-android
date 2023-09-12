package com.shinobicontrols.kcompanionapp.charts;

import android.view.View;
import android.widget.FrameLayout;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.kcompanionapp.charts.internal.AxisMapping;
import com.shinobicontrols.kcompanionapp.charts.internal.ChartThemeCache;
/* loaded from: classes.dex */
public interface IChartView {
    void createAdditionalViews(FrameLayout frameLayout);

    void createAnnotations();

    View createIcon();

    void createSeries();

    AxisMapping getAxisMapping();

    Axis<?, ?> getYAxis();

    void onChartViewCreated(ShinobiChart shinobiChart);

    void onInternalLayout(ShinobiChart shinobiChart, Axis<?, ?> axis);

    void reload(Axis axis);

    void setChartThemeCache(ChartThemeCache chartThemeCache);
}

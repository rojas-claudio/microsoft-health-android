package com.microsoft.kapp.utils;

import android.app.Activity;
import com.shinobicontrols.charts.ChartView;
import com.shinobicontrols.charts.ShinobiChart;
import com.shinobicontrols.kcompanionapp.charts.internal.ChartThemeCache;
/* loaded from: classes.dex */
public class ChartUtils {
    private static String TAG = ChartUtils.class.getSimpleName();

    public static void prepareChartForScreenshot(ChartView chartView, Activity activity) {
        if (chartView != null && chartView.getShinobiChart() != null) {
            ShinobiChart chart = chartView.getShinobiChart();
            ChartThemeCache chartThemeCache = new ChartThemeCache(activity.getResources());
            int chartBackgroundColor = chartThemeCache.getChartBackgroundColor();
            chart.getStyle().setCanvasBackgroundColor(chartBackgroundColor);
            chart.getStyle().setPlotAreaBackgroundColor(chartBackgroundColor);
        }
    }

    public static void recoverChartAfterTakingScreenshot(ChartView chartView) {
        if (chartView != null && chartView.getShinobiChart() != null) {
            ShinobiChart chart = chartView.getShinobiChart();
            chart.getStyle().setCanvasBackgroundColor(0);
            chart.getStyle().setPlotAreaBackgroundColor(0);
            chart.redrawChart();
        }
    }
}

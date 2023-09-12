package com.shinobicontrols.kcompanionapp.charts.internal;

import android.content.Context;
import android.view.ViewGroup;
import com.shinobicontrols.charts.Annotation;
import com.shinobicontrols.charts.AnnotationsManager;
import com.shinobicontrols.charts.Axis;
import com.shinobicontrols.charts.DataPoint;
import com.shinobicontrols.charts.ShinobiChart;
/* loaded from: classes.dex */
public class ColumnMarkerManager implements ShinobiChart.OnInternalLayoutListener {
    private final ShinobiChart chart;
    private final int color;
    private final Context context;
    private final int numberOfColumns;
    private final double rangeMax;
    private final Axis<Double, Double> xAxis;
    private final Axis<Double, Double> yAxis;

    public ColumnMarkerManager(Context context, ShinobiChart chart, Axis<Double, Double> xAxis, Axis<Double, Double> yAxis, int numberOfColumns, double rangeMax, int color) {
        this.context = context;
        this.chart = chart;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.numberOfColumns = numberOfColumns;
        this.rangeMax = rangeMax;
        this.color = color;
    }

    @Override // com.shinobicontrols.charts.ShinobiChart.OnInternalLayoutListener
    public void onInternalLayout(ShinobiChart chart1) {
        double columnWidth = ColumnWidthCalculator.getColumnWidthExcludingPadding(this.xAxis, this.numberOfColumns);
        chart1.getPlotAreaRect();
        AnnotationsManager am = chart1.getAnnotationsManager();
        for (Annotation annotation : am.getAnnotations()) {
            ColumnMarkerView marker = (ColumnMarkerView) annotation.getView();
            marker.setColumnWidth((float) columnWidth);
            double x = ((Number) annotation.getXValue()).doubleValue();
            double y = ((Number) annotation.getYValue()).doubleValue();
            marker.setXDataValueInPixels(this.xAxis.getPixelValueForUserValue(Double.valueOf(x)) - chart1.getPlotAreaRect().left);
            marker.setYDataValueInPixels(this.yAxis.getPixelValueForUserValue(Double.valueOf(y)) - chart1.getPlotAreaRect().top);
        }
    }

    public void addIfBeyondRangeMax(DataPoint<?, ?> dataPoint) {
        double yValue = ((Number) dataPoint.getY()).doubleValue();
        if (yValue > this.rangeMax) {
            ColumnMarkerView marker = new ColumnMarkerView(this.context, this.color);
            marker.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            this.chart.getAnnotationsManager().addViewAnnotation(marker, dataPoint.getX(), Double.valueOf(this.rangeMax), this.xAxis, this.yAxis);
        }
    }

    public void removeAllMarkers() {
        this.chart.getAnnotationsManager().removeAllAnnotations();
    }
}
